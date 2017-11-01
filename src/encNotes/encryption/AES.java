/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.encryption;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author mwlltr
 */
public class AES {

    
    
    public static SecretKeySpec createSecretKey(String keyString){
        System.out.println(keyString);
        SecretKeySpec secretKey = null;
        
        try {
            
            // create byte array from key string
            byte[] key = (keyString).getBytes("UTF-8");
            
            // create a hash
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            
            key = sha.digest(key);
            
            key = Arrays.copyOf(key, 16);
            
            secretKey = new SecretKeySpec(key, "AES");
             
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return secretKey;
    }
    
    
    

    
    public static String encrypt(String text, SecretKeySpec secretKey){
        
        String encryptedString = "";
        
        try {
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            BASE64Encoder myEncoder = new BASE64Encoder();
            encryptedString = myEncoder.encode(encrypted);
                    
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedString;
    }



        
    public static String decrypt(String encryptedText, SecretKeySpec secretKey){
        
        String decryptedText = "";
        try {
            
            // BASE64 String zu Byte-Array konvertieren
            BASE64Decoder myDecoder = new BASE64Decoder();
            byte[] crypted = myDecoder.decodeBuffer(encryptedText);
            
            // Entschluesseln
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherData = cipher2.doFinal(crypted);
            decryptedText = new String(cipherData);
            

        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return decryptedText;
        
    }
}
