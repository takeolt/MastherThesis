import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Scanner;

public class Alice {


    public static void main (String[] abs) {

        String msg1 = "Hey, I am Alice, i want to start a secure channel chat with you Bob is that okey?";
        String msg2 = "Wow that is great to hear, now this is my secret message only to you hear, this system works and" +
                "everything works as it should";
        String msg3 = "But lets test it even more";
        String msg4 = "";

        try{
            File long_text = new File("src/Long_text.txt");


            Scanner read = new Scanner(long_text);

            StringBuilder temp = new StringBuilder();

            while(read.hasNextLine()) {
                temp.append(read.nextLine());
                temp.append("\n");

            }

            msg4 = temp.toString();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error has happen");
            e.printStackTrace();

        }

        try {
            KeysGenerator keys = new KeysGenerator();
            PgP_Encryption pgp = new PgP_Encryption(msg1,keys.getPublicKey());

            byte[] firstMsg = pgp.encrypt();

            Bob bob = new Bob();

            byte[] answear1 = bob.send(firstMsg, keys.getPublicKey());

            String temp = new String(answear1, StandardCharsets.UTF_8);

            System.out.println("Bobs first message: \n " + temp);

            PublicKey bobsPublicKey = bob.getBobsPublicKey();

            pgp = new PgP_Encryption(msg2, keys.getPublicKey(), bobsPublicKey);

            byte[] secondMsg = pgp.encrypt();

            byte[] answear2 = bob.sendEncrypted(secondMsg, pgp.getEncryptedKey());


        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("An error has happen with trying creating Keys");
            e.printStackTrace();
        }



    }
}
