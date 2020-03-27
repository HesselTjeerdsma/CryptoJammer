from django.urls import path
from .views import ListInfectedPcView


urlpatterns = [
    path('infectedpc/', ListInfectedPcView.as_view(), name="infectedpc-all")
]