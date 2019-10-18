package firstmob.firstbank.com.firstagent.security;

import android.util.Base64;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AESCBCEncryption implements SecurityConstants {
    public static byte[] pkey = base64Decode("amIY7bP5pfeG5wmh3tnBl2ki86S+dxDZu991NxdONnw=");
    public static byte[] piv = base64Decode("cE9XEq+2I+NVp6YblJGOHw==");
    public static byte[] skey = base64Decode("EHxxOa9FpK256bvlaICg2bYVsxKodO4XekhsJEdNzaE=");
    public static byte[] siv = base64Decode("7TLKFfqtScBUm0eP+QIitg==");
    public static byte[] key = base64Decode("mYCvSane74ZV2rS5BXiWi0beWxFQ2037I00wLipnFhU=");    //generateSessionKey(); // 256 bit key
    public static byte[] initVector = base64Decode("Eq/cxCxt6YPHUdy65/FMuA==");
    public static byte[] dummy_pkey = base64Decode("sjhE1SXzA5VddkhS9NC8Sws7iC5BP/yuUigXliQGRr0=");
    public static byte[] dummy_piv = base64Decode("V3SwyU7/TjPJt3Zkye9klA==");
    public static byte[] dummy_skey = base64Decode("EHxxOa9FpK256bvlaICg2bYVsxKodO4XekhsJEdNzaE=");
    public static byte[] dummy_siv = base64Decode("l6Zy71TDy2BMiyPNuNCjcA==");

    static {

    }

    public static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(SYMETRICKEY_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception ex) {
            // ex.printStackTrace();
            SecurityLayer.Log(ex.toString());
        }

        return null;
    }


    public static String toString(String hex) throws UnsupportedEncodingException, DecoderException {
        return new String(Hex.decodeHex(hex.toCharArray()), StandardCharsets.UTF_8);
    }

    public static String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(SYMETRICKEY_ALG);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

            return new String(original);
        } catch (Exception ex) {
            // ex.printStackTrace();
            SecurityLayer.Log(ex.toString());
        }

        return null;
    }

    public static byte[] generateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance(KEYGEN_ALG);
        kgen.init(SYMETRIC_KEY_SIZE);
        SecretKey key = kgen.generateKey();
        byte[] symmKey = key.getEncoded();
        return symmKey;
    }

    public static byte[] generateIV() throws NoSuchAlgorithmException, NoSuchProviderException {
        return Arrays.copyOfRange(generateSessionKey(), 0, 16);
    }

    public static String base64Encode(byte[] binaryData) {
        return Base64.encodeToString(binaryData,Base64.NO_WRAP);
    }


    public static byte[] base64Decode(String base64String) {
        return Base64.decode(base64String, Base64.NO_WRAP);
    }

    public static String firstLoginDecrypt(String param) throws UnsupportedEncodingException {
        String plaintext = decrypt(key, initVector, param);
        return plaintext;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
       /* String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
*/

        //generateIV();

        //System.out.println("length : "+EnDesEncyp.base64Encode(initVector));

        //System.out.println(decrypt(key, initVector,
        //encrypt(key, initVector, "Hello World")));

        //System.out.println(firstLogin());
    }
}