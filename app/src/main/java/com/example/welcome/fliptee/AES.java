package com.example.welcome.fliptee;

/**
 * Created by welcome on 22-11-2016.
 */
import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class AES
{
    private static String algorithm = "AES";
    private static byte[] keyValue=new byte[] { 'A', 'S', 'e', 'c', 'u', 'r', 'e', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    // Performs Encryption
    public static String encrypt(String plainText) throws Exception
    {
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(plainText.getBytes());
        //String encryptedValue = new Base64.encodeToString(encVal,Base64.DEFAULT);
        String encryptedValue= Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    // Performs decryption
    public static String decrypt(String encryptedText) throws Exception
    {
        // generate key
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.DECRYPT_MODE, key);
        //byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedText);
        byte[] decordedValue= Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decValue = chiper.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    //generateKey() is used to generate a secret key for AESenc algorithm
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }

    // performs encryption & decryption
    public static void main(String[] args) throws Exception
    {

        /*String plainText = "Password";
        String encryptedText = AESenc.encrypt(plainText);
        String decryptedText = AESenc.decrypt(encryptedText);

        System.out.println("Plain Text : " + plainText);
        System.out.println("Encrypted Text : " + encryptedText);
        System.out.println("Decrypted Text : " + decryptedText);*/
    }
}