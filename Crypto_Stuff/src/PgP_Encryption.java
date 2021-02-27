import java.math.BigInteger;
import java.util.*;

public class PgP_Encryption {

    public static void main(String[] args) {

        BigInteger p = new BigInteger(1024, 100, new Random());
        BigInteger q = new BigInteger(1024, 100, new Random());

        BigInteger n = p.multiply(q);

        BigInteger Carmi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));


        BigInteger e = new BigInteger("65");


        for(BigInteger i = BigInteger.TWO; i.compareTo(e) < 0; i = i.add(BigInteger.ONE)) {

            BigInteger[] temp = Carmi.divideAndRemainder(i);

            if(temp[1].compareTo(BigInteger.ZERO) == 0) {
                temp = e.divideAndRemainder(i);

                if(temp[1].compareTo(BigInteger.ZERO) == 0) {
                    System.out.println("This e doesn't work, increasing with one");

                    e = e.add(BigInteger.TWO);
                    i = BigInteger.TWO;

                }
            }
        }

        BigInteger d = euclidean_division(Carmi, e);

        System.out.println("D value is " + d.toString());


    }


    public static BigInteger euclidean_division(BigInteger Carmi, BigInteger e) {
        BigInteger k = BigInteger.ONE;

        while(true){

            BigInteger temp = k.multiply(Carmi);
            temp = BigInteger.ONE.add(temp);

            BigInteger[] rest = temp.divideAndRemainder(e);

            if(rest[1].compareTo(BigInteger.ZERO) == 0) {

                System.out.println("K value is: " + k.toString());
                return rest[0];
            }

            else {
                k = k.add(BigInteger.ONE);
            }

        }
    }
}
