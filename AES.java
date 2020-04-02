/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AES;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

/**
 *
 * @author 20175707
 */
 
public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static void encrypt(File inputImage, String secret) 
    {
        try
        {
            setKey(secret);
            BufferedImage input = ImageIO.read(inputImage);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            FileOutputStream output = new FileOutputStream("C:\\Users\\20175707\\Documents\\NetBeansProjects\\DataEncryptionDecryption\\src\\encryptedImage.png");
            CipherOutputStream cos = new CipherOutputStream(output, cipher);
            ImageIO.write(input,"PNG",cos);
            cos.close(); 
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
    }
 
    public static String decrypt(File encryptedImage, String secret) 
    {
        try
        {
            setKey(secret);
            BufferedImage input = ImageIO.read(encryptedImage);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            FileOutputStream output = new FileOutputStream("C:\\Users\\20175707\\Documents\\NetBeansProjects\\DataEncryptionDecryption\\src\\decrypted.jpg");
            CipherOutputStream cos = new CipherOutputStream(output, cipher);
            ImageIO.write(input,"JPG",  cos);
            cos.close();

        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    
    public static void main(String[] args)
    {
        final String secretKey = "something";
        File image = new File("C:\\Users\\20175707\\Documents\\NetBeansProjects\\DataEncryptionDecryption\\src\\test.jpg");
        AES.encrypt(image, secretKey);
       // File dImage = new File("C:\\Users\\20175707\\Documents\\NetBeansProjects\\DataEncryptionDecryption\\src\\encryptedImage.pngZ");
       // AES.decrypt(image, secretKey);
    }
    
}
