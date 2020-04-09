import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import org.bouncycastle.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionDecription {
    
    public byte[] getBytesFile(Path path) {

        File inputFile = new File(path.toString());
        
        InputStream input = null;
        try {
            input = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        byte[] content = null;
        try {
            content = new byte[input.available()];
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        try {
            input.read(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return content;
    }
    
    public byte[][] divideContentInBlocks(byte[] content, int blockSize){
        int numberBlocks = Math.floorDiv(content.length, blockSize) + 1;
        int iterator = 0;       
        byte[][] contentInBlocks = new byte[numberBlocks][];
        
        while(iterator<numberBlocks) {
            byte[] reducedContent = Arrays.copyOfRange(content, iterator, content.length);
            if (reducedContent.length <= blockSize) {
                contentInBlocks[iterator] = reducedContent; 
            }
            if (reducedContent.length > blockSize) {
                contentInBlocks[iterator] = Arrays.copyOfRange(reducedContent, 0, blockSize);
            }
            iterator++;
        }
        
        return contentInBlocks;    
    }
    
    public byte[] mergeContentBlocks(byte[][] blocks){
        int numberBlocks = blocks.length;
        int iterator = 0;       
        ArrayList<Byte> content = new ArrayList<>();
        
        while(iterator<numberBlocks) {
            for(byte element : blocks[iterator]) {
                content.add(element);
            }
            iterator++;
        }
        
        byte[] merged = new byte[content.size()];
        for(int i = 0; i < content.size(); i++) merged[i] = content.get(i);
        
        return merged;    
    }

    public byte[] encryptBytesFile(Key publicKey, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            System.out.println(content.length);
            cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;

    }

    public byte[] decryptBytesFile(Key privateKey, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            System.out.println(textCryp.length);
            cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public void bytesToFile(byte[] bytes, Path path) throws IOException {
        FileOutputStream output = new FileOutputStream(path.toString());
        output.write(bytes);
        output.close();

    }

}