

// uses Singleton design pattern
public class UniqueID {
    
    private static UniqueID uniqueID;
    
    private UniqueID() {
        generateUniqueID();
    }
    
    // get single instance of uniqueID
    public static UniqueID getSymmetricKey(){
        if (uniqueID == null) {
            uniqueID = new UniqueID(); 
        }
        return uniqueID;
    }
    
    // generate new AES key
    private void generateUniqueID() {
        
    } 

}
