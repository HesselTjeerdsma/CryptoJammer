import random 
import sympy 
from math import pow

  
a = random.randint(2, 10) 
  
#Make a function for the greatest common divisor  
def gcd(a, b): 
    if a < b: 
        return gcd(b, a) 
    elif a % b == 0: 
        return b; 
    else: 
        return gcd(b, a % b) 

def inverse(A, M):
    """
    Returns multiplicative modulo inverse of A under M, if exists
    Returns -1 if doesn't exist
    """
    # This will iterate from 0 to M-1
    for i in range(0, M):
        # If we have our multiplicative inverse then return it
        if (A*i) % M == 1:
            return i
    # If we didn't find the multiplicative inverse in the loop above
    # then it doesn't exist for A under M
    return -1


def pair(s):
	safe_prime = 0
	while(True):
		p = sympy.randprime(10^(s-1), (10^s)-1) 
		print(p)                                     
		safe_prime = 2*(p+1)
		if(sympy.isprime(safe_prime)):
			break
	while(True):
		a = random.randint(2, safe_prime - 1)
		if((safe_prime-1)%a != 1):
			break
		return safe_prime, a

def egKey(s):
	p,a = pair(s)
	x = random.randint(1, p-2)
	y = pow(a,x,p)
	return p, a, x, y

""" Signature Generation """
def egGen(p, a, x, m):
	while 1:
		k = random.randint(1,p-2)
		if gcd(k,p-1)==1: break
	r = pow(a,k,p)
	l = inverse(k, p-1)
	s = l*(m-x*r)%(p-1)
	return r,s

""" Signature Verification """
def egVer(p, a,	y, r, s, m):
	if r < 1 or r > p-1 : return False
	v1 = pow(y,r,p)%p * pow(r,s,p)%p
	v2 = pow(a,m,p)
	return v1 == v2



if __name__ == "__main__":
	message = 'cryptoJammer'
	print("Message: ", message)
	prime,alpha,private,public = egKey(10)
	print("prime,alpha,private,public", prime,alpha,private,public)
	rr,ss = egGen(prime,alpha,private, message)
	print("rr,ss", rr, ss)
	isValid = egVer(prime, alpha, public, rr, ss, message)
	print("Valid Signature: " ,  isValid)