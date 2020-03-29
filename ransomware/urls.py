from django.urls import path
from .views import ListInfectedPcView, decrypt


urlpatterns = [
    path('infectedpc/', ListInfectedPcView.as_view(), name="infectedpc-all"),
    path('decrypt/', decrypt, name='decrypt'),
    #path('payment/', payment, name='payment')
]