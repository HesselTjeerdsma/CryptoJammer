source /root/CryptoJammer/venv/bin/activate
touch error.txt
while true; do
  echo "Re-starting Django runserver"
  python3 manage.py runserver > error.txt
  sleep 2
done
