
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.crypto.spec.SecretKeySpec;

// uses Singleton design pattern
public class SymmetricKey {
    
    private static SymmetricKey symmetricKey;
    
    private SecretKey aesSessionKey;
    private byte[] keyBytes;
    
    private SymmetricKey() throws NoSuchAlgorithmException {
        generateNewKey();
    }
    
    // get single instance of symmetric key
    public static SymmetricKey getSymmetricKey() throws NoSuchAlgorithmException{
        if (symmetricKey == null) {
            symmetricKey = new SymmetricKey(); 
        }
        return symmetricKey;
    }
    
    // get sym key
    public SecretKey getSessionKey() {
        return this.aesSessionKey; 
    }
    
    // generate new AES key
    private void generateNewKey() throws NoSuchAlgorithmException{
        SecureRandom secureRandom = new SecureRandom();
        keyBytes = new byte[16];
        secureRandom.nextBytes(keyBytes);
        aesSessionKey = new SecretKeySpec(keyBytes, "AES");
    } 
    
    public byte[] encryptKey(Key publicRSAKey) throws Exception{
        if (aesSessionKey == null) throw new Exception("Symmetric key already encrypted or not existent yet.");
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	cipher.init(Cipher.ENCRYPT_MODE, publicRSAKey);
        byte[] encryptedKey = cipher.doFinal(keyBytes);
        
        destroyKey();
        
        return encryptedKey;
    } 
    
    private void destroyKey() throws DestroyFailedException {
        aesSessionKey = null;
        keyBytes = null;
        System.gc();
    }
    
    public SecretKey decryptKey(Key privateRSAKey, byte[] encryptedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	cipher.init(Cipher.DECRYPT_MODE, privateRSAKey);
        byte[] decryptedKey = cipher.doFinal(encryptedKey);
        
        SecretKey decriptionKey = new SecretKeySpec(decryptedKey, "AES");
        return decriptionKey; 
    }
}
