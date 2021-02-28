package com.devglan.rsa;



import java.math.BigInteger;
import java.util.*;

class PgP_Encryption {

    public static void main(String[] args) {

        BigInteger p = new BigInteger(1024, 100, new Random());
        BigInteger q = new BigInteger(1024, 100, new Random());

        BigInteger n = p.multiply(q);

        BigInteger Carmi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));


        BigInteger e = new BigInteger("65");


        for(BigInteger i = BigInteger.TWO; i.compareTo(e) < 0; i = i.add(BigInteger.ONE)) {
            
            BigInteger[] temp = Carmi.divideAndRemainder(i);

            if()dddd
        }

    }

}