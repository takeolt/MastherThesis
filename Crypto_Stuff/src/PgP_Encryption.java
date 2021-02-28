import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;


public class PgP_Encryption {

    private String message;
    private PublicKey sendersPublicKey;
    private PublicKey recieversPublicKey;
    private PrivateKey privateKey;
    private SecretKey randomKey;
    private byte[] encryptedKey;
    private byte[] encryptedText;


    public PgP_Encryption(String text, PublicKey publicKey ) {
        this.message = text;
        this.sendersPublicKey = publicKey;
    }

    public PgP_Encryption(byte[] encryptedText, byte[] encryptedKey, PrivateKey privateKey) {
        this.encryptedText = encryptedText;
        this.encryptedKey = encryptedKey;
        this.privateKey = privateKey;
    }

    public PgP_Encryption(String text, PublicKey sendersPublicKey, PublicKey recieversPublicKey) {
        this.message = text;
        this.sendersPublicKey = sendersPublicKey;
        this.recieversPublicKey = recieversPublicKey;
    }

    public byte[] encrypt() throws NoSuchAlgorithmException {

        if(recieversPublicKey == null) {
            System.out.println("Don't Have public key sending without encryption");
            return message.getBytes();
        }

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        randomKey = keyGen.generateKey();

        try {
            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, randomKey);

            byte[] temp = message.getBytes();

            byte[] temp2 = randomKey.getEncoded();

            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, recieversPublicKey);

            encryptedKey = rsaCipher.doFinal(temp2);

            return aesCipher.doFinal(temp);
        }
        catch (NoSuchPaddingException e ) {
            System.out.println("No such padding exist");
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            System.out.println("No such Key");
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            System.out.println("Too big of a message");
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            System.out.println("Bad padding");
            e.printStackTrace();
        }


        return null;

    }

    public byte[] decrypt() {
        try {
            Cipher rsaCipher  = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] temp = rsaCipher.doFinal(encryptedKey);

            SecretKey secretKey = new SecretKeySpec(temp, 0, temp.length, "AES");

            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

            return aesCipher.doFinal(encryptedText);
        }
        catch (NoSuchPaddingException e ) {
            System.out.println("No such padding exist");
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            System.out.println("No such Key");
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            System.out.println("Too big of a message");
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            System.out.println("Bad padding");
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Nop such algorithm");
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }
}