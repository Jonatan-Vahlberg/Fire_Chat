package com.jonatan_vahlberg.firechat.helper;

import android.util.Base64;
import android.util.Log;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class KeyHelper {

    private static final String C_P_E = "a7!sgeiu56l/";
    public static final String  PASSWORD_KEY = "PASSWORD";
    public static final String  EMAIL_KEY = "EMAIL";

    public static String createEncryptKey(final String value){
        String encryptKey = "";
        try{
            DESKeySpec keySpec = new DESKeySpec(C_P_E.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] clearText = value.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE,key);

            String encryptedValue = Base64.encodeToString(cipher.doFinal(clearText),Base64.DEFAULT);
            Log.d("CRYPT", "createEncryptKeys: "+encryptedValue);
            encryptKey = encryptedValue;

        }catch (Exception e){
            Log.e("ERROR ENCRYPTING", "createEncryptKeys: ",e );
        }
        return encryptKey;
    }

    public static String decryptKey(String value){
        String decryptedKey = "";
        try{
            DESKeySpec keySpec = new DESKeySpec(C_P_E.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            Log.d("CRYPT", "Decrypted: " + value + " -> " + decrypedValue);
            decryptedKey = decrypedValue;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return decryptedKey;


    }
}
