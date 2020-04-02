from django.urls import path
from .views import ListInfectedPcView, decrypt, index, encrypt


urlpatterns = [
    path('list-infected-pc/', ListInfectedPcView.as_view(), name="infectedpc-all"),
    path('encrypt/', encrypt, name='encrypt'),
    path('decrypt/', decrypt, name='decrypt'),
    path('', index, name='index')
    #path('payment/', payment, name='payment')
]