from django.urls import path
from .views import ListInfectedPcView, decrypt, index


urlpatterns = [
    path('infectedpc/', ListInfectedPcView.as_view(), name="infectedpc-all"),
    path('decrypt/', decrypt, name='decrypt'),
    path('', index, name='index')
    #path('payment/', payment, name='payment')
]