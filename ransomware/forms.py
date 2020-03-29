from django import forms

class decryptForm(forms.Form):
    uniqueId = forms.CharField(label='Your unique pc ID', max_length=100)