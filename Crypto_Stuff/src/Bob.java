import java.nio.charset.StandardCharsets;
import java.security.*;

public class Bob {

    private String msg1 = "Yes, lets have private communication";
    private KeysGenerator bobsKeys;
    private PublicKey sendersPublicKey;

    public Bob() throws NoSuchAlgorithmException {
        this.bobsKeys = new KeysGenerator();

    }

    public byte[] send(byte[] msg, PublicKey publicKey) {

            System.out.println("This is Alice first msg so it is unencrypted");

            String temp = new String(msg, StandardCharsets.UTF_8);

            System.out.println(temp);

            sendersPublicKey = publicKey;

            return msg1.getBytes();
    }

    public byte[] sendEncrypted(byte[] message, byte[] encrypted_key) {
        PgP_Encryption pgp = new PgP_Encryption(message, encrypted_key, bobsKeys.getPrivateKey());
        byte[] temp = pgp.decrypt();

        String msg = new String(temp, StandardCharsets.UTF_8);

        System.out.println(msg);

        return null;
    }

    public PublicKey getBobsPublicKey() {
       return bobsKeys.getPublicKey();
    }
}
