from rest_framework import serializers
from .models import InfectedPc


class InfectedPcSerializer(serializers.ModelSerializer):
    class Meta:
        model = InfectedPc
        fields = ("uniqueId", "encryptionKey", "paymentStatus")
        
        def update(self, instance, validated_data):
            instance.uniqueId = validated_data.get("uniqueId", instance.uniqueId)
            instance.encryptionKey = validated_data.get("encryptionKey", instance.encryptionKey)
            instance.encryptionKey = validated_data.get("paymentStatus", instance.paymentStatus)
            instance.save()
            return instance