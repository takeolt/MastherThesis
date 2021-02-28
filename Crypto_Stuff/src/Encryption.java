import java.security.*;


public class Encryption {

    private Key random_key;
    private PublicKey publicKey;
    private String message;

    public Encryption(Key random_key, PublicKey publicKey, String text) {
        this.random_key = random_key;
        this.publicKey = publicKey;
        this.message = text;
    }
}
