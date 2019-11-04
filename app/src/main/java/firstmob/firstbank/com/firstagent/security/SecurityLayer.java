package firstmob.firstbank.com.firstagent.security;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import firstmob.firstbank.com.firstagent.constants.Constants;
import firstmob.firstbank.com.firstagent.utils.Utility;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Named;


import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.base64Decode;
import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.base64Encode;
import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.decrypt;
import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.encrypt;
import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.initVector;
import static firstmob.firstbank.com.firstagent.utils.Utility.checkInternetConnection;
import static firstmob.firstbank.com.firstagent.utils.Utility.getDevImei;
import static firstmob.firstbank.com.firstagent.utils.Utility.toHex;
import static firstmob.firstbank.com.firstagent.security.AESCBCEncryption.key;

/**
 * Created by brian on 07/12/2016.
 */

public class SecurityLayer {

    public static boolean isDebug = true;
    public final static String KEY_PIV = "pvoke";
    public final static String KEY_PKEY = "pkey";
    public static final String KEY_SIV = "svoke";
    public static final String KEY_SKEY = "skey";
    public static final String KEY_APP_ID = "appid";
    public static final String KEY_DHASH = "DHASH";
    public static final String KEY_INP = "inp";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_SESSION_ID = "sessionID";
    public static final String KEY_ACCOUNT_NUMBER = "agentAccountNumber";
    public static Context context;




    @Inject
    public SecurityLayer(@Named("ApplicationContext") Context context) {
        SecurityLayer.context = context;

    }

    public static String firstLogin( String params, String endpoint) throws UnsupportedEncodingException {
        String finpoint = "";
        Log("Am i In?");


        if (checkInternetConnection()) {
            StringBuffer sb = new StringBuffer();
            String vers = "2.0.0";
            String year = Utility.getAppVersion();
            String hexkey = getrandkey();
            String imei = getDevImei();
            String session_id = UUID.randomUUID().toString();

            Prefs.putString(KEY_SESSION_ID, session_id);
            Log("Imei is " + imei);
            Log("Session ID is " + session_id);
            finpoint = sb.append(Constants.NET_URL + endpoint)
                    .append(toHex(encrypt(key, initVector, params)))
                    .append("/" + Utility.generateHashString(params))
                    .append("/" + toHex(hexkey))
                    .append(Constants.CH_KEY)
                    .append("/" +Constants.APP_ID)
                    .append("/1212")
                    .append("/" + toHex(encrypt(key, initVector, imei)))
                    .append("/" + toHex(encrypt(key, initVector, session_id)))
                    .append("/" + vers)
                    .append("/" + year)
                    .toString();
        }
        return  finpoint;
    }
    public static String getrandkey() {
        byte[] genkey = null;
        try {
            genkey = generateSessionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        String sessid = android.util.Base64.encodeToString(genkey, android.util.Base64.NO_WRAP);
        return sessid;

    }

    public static byte[] generateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey key = kgen.generateKey();
        byte[] symmKey = key.getEncoded();
        return symmKey;
    }

    public  String beforeLogin(String params,@Named("ApplicationContext") Context context, String endpoint) throws UnsupportedEncodingException {
        String finpoint = "";
        if (checkInternetConnection()) {
            StringBuffer sb = new StringBuffer();
            String vers = "2.0.0";
            String year = Utility.getAppVersion();
            String imei = getDevImei();

            String hexkey = getrandkey();
            try {
                hexkey = toHex(hexkey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            finpoint = sb.append(Constants.NET_URL + endpoint)
                    .append(toHex(encrypt(key, initVector, params)))
                    .append("/" + Utility.generateHashString(params))
                    .append("/" + hexkey)
                    .append(Constants.CH_KEY)
                    .append("/" + Constants.APP_OUTSIDEID)
                    .append("/1212")
                    .append("/" + toHex(encrypt(key, initVector, imei)))

                    .append("/" + vers)
                    .append("/" + year)
                    .toString();
        }
        return finpoint;
    }
    public static JSONObject decryptFirstTimeLogin(JSONObject jsonobj) throws Exception {

        String status = jsonobj.getString("status");
        String svoke = jsonobj.getString("svoke");
        String input = jsonobj.getString("inp");
        // System.out.println("svoke [" + svoke + "]");
        String pkey = jsonobj.getString("pkey");
        SecurityLayer.Log("pkey [" + pkey + "]");
        String pvoke = jsonobj.getString("pvoke");
        String skey = jsonobj.getString("skey");
        String dhash = jsonobj.getString("DHASH");
        SecurityLayer.Log(input);
        SecurityLayer.Log(pkey);
        JSONObject decjsonobj = new JSONObject();

        String finalresp = "";


String dehexcab = AESCBCEncryption.toString(pkey);
            SecurityLayer.Log("pkey_dehex [" + dehexcab + "]");
        SecurityLayer.Log("pkey_dehex",dehexcab);
            String pvokecab = AESCBCEncryption.toString(pvoke);
        SecurityLayer.Log("pvoke_cab",pvokecab);
            SecurityLayer.Log("pvokecab [" + pvokecab + "]");
            String pkey_dec = decrypt(key, initVector, AESCBCEncryption.toString(pkey));
            String pvoke_dec = decrypt(key, initVector, AESCBCEncryption.toString(pvoke));
            String sessionkey = decrypt(base64Decode(pkey_dec), base64Decode(pvoke_dec), AESCBCEncryption.toString(skey));
            String sessioniv = decrypt(base64Decode(pkey_dec), base64Decode(pvoke_dec), AESCBCEncryption.toString(svoke));

        Prefs.putString(KEY_PKEY, pkey_dec);//set pkey
            SecurityLayer.Log("setpkey ["+ pkey_dec+"]");
        Prefs.putString(KEY_PIV, pvoke_dec);//set piv
        SecurityLayer.Log("setpiv ["+ pvoke_dec+"]");
        Prefs.putString(KEY_SKEY, sessionkey);//set skey
        SecurityLayer.Log("setskey ["+ sessionkey+"]");
        Prefs.putString(KEY_SIV, sessioniv);
        SecurityLayer.Log("setsiv ["+ sessioniv+"]");
        Prefs.putString(KEY_DHASH, dhash);

            finalresp = decrypt(base64Decode(pkey_dec), base64Decode(pvoke_dec), AESCBCEncryption.toString(input));
JSONObject newjs = new JSONObject(finalresp);
String tken = newjs.optString("token");
        String nwappid = newjs.optString("appid");
        String encappid = toHex(encrypt(key, initVector, nwappid));


        Prefs.putString("NWAPPID",encappid);
        Prefs.putString("PLAINAPPID",nwappid);


        Prefs.putString(KEY_TOKEN, tken);
            SecurityLayer.Log("pkey_dec [" + pkey_dec + "]");
            SecurityLayer.Log("pvoke_dec [" + pvoke_dec + "");
            SecurityLayer.Log("session key [" + sessionkey + "]");
            SecurityLayer.Log("sessioniv [" + sessioniv + "]");
            SecurityLayer.Log("finalresp [" + finalresp + "]");

            String gen = Utility.generateHashString(finalresp);
            SecurityLayer.Log("Hashing Status [" + gen.equals(dhash) + "]");

            decjsonobj.put("pkey_dec", pkey_dec);
            decjsonobj.put("pvoke_dec", pvoke_dec);
            decjsonobj.put("sessionkey", sessionkey);
            decjsonobj.put("sessioniv", sessioniv);
            decjsonobj.put("finalresp", finalresp);
            decjsonobj.put("hashstatus", gen.equals(dhash));


        return newjs;
        //return decjsonobj;
        //System.out.println(decjsonobj);

    }

    public static String generalLogin( String params, String endpoint) throws Exception {
        String finpoint = "";
        if (checkInternetConnection()) {

            String skey = Prefs.getString(SecurityLayer.KEY_SKEY,"NA");
            //  String skey = "4UhIX09CelA75Rdao2u+j/vnnkAopFQbbO/nbHnebf4=";
            SecurityLayer.Log("skey", skey);
            String siv = Prefs.getString(SecurityLayer.KEY_SIV,"NA");
            //   String siv = "j3at/AyFvk4J7h32D+cECQ==";
            SecurityLayer.Log("siv", siv);
            String pkey = (Prefs.getString(SecurityLayer.KEY_PKEY,"NA"));
            //  String pkey = "xB8sn12WDeP8gM608ZkFjvd5CE/bxtmDb6MH9kk6R+4=";
            SecurityLayer.Log("pkey", pkey);
            //   String piv = "xLJ6Z4p+wgKik3jMG6V3lw==";
            String piv = (Prefs.getString(SecurityLayer.KEY_PIV,"NA"));
            SecurityLayer.Log("piv", piv);
            //  String appid = "113260437012100";
            int count = 0;




            String encappid = Prefs.getString("NWAPPID","NA");



            SecurityLayer.Log("appid gott", encappid);
            StringBuffer sb = new StringBuffer();

            String imei = getDevImei();

            byte[] randomKey = base64Decode(skey);
            byte[] randomSIV = base64Decode(siv);
            byte[] dummy_pkey = base64Decode(pkey);
            byte[] dummy_piv = base64Decode(piv);
            // String encryptedUrl = LoginAESProcess.getEncryptedUrlByPropKey(params, randkey);
            String encryptedUrl = encrypt(base64Decode(skey), base64Decode(siv), params);
            SecurityLayer.Log("Base Decode Pkey", new String(base64Decode(pkey)));
            SecurityLayer.Log("Base Decode PIV", new String(base64Decode(piv)));
            SecurityLayer.Log("Base Encode RandomKey", base64Encode(randomKey));
            String encryptedpkey = toHex(encrypt(base64Decode(pkey), base64Decode(piv), base64Encode(randomKey)));//LoginAESProcess.getEncryptedUrlByPropKey(randkey, pkey);
            String encryptedRandomIV = toHex(encrypt(base64Decode(pkey), base64Decode(piv), base64Encode(randomSIV)));



            String vers = Utility.getAppVersion();
SecurityLayer.Log("encappid",encappid);
String session_id = "121212";

            finpoint = sb.append(Constants.NET_URL + endpoint)
                    .append(toHex(encryptedUrl))
                    .append("/" + Utility.generateHashString(params))
                    .append("/" + encryptedpkey)
                    .append(Constants.CH_KEY)
                    .append("/" + encappid)
                    .append("/1212")
                    .append("/" + toHex(encrypt(dummy_pkey, dummy_piv, imei)))
                    .append("/" + toHex(encrypt(dummy_pkey, dummy_piv, session_id)))
                    .append("/" + encryptedRandomIV)
                    .append("/" + vers)
                    .toString();
        }
        return  finpoint;
    }
    public static JSONObject decryptGeneralLogin(JSONObject jsonobj) throws Exception {

        String status = jsonobj.getString("status");
        String svoke = jsonobj.getString("svoke");
        String input = jsonobj.getString("inp");
        Log("svoke [" + svoke + "]");
        //String pkey = jsonobj.getString("pkey");
        //String pvoke = jsonobj.getString("pvoke");
        String skey = jsonobj.getString("skey");
        String dhash = jsonobj.getString("DHASH");

        // System.out.println("pkey ["+pkey+"]\n pvoke ["+pvoke+"]");

        JSONObject decjsonobj = new JSONObject();

        byte[] pkey = base64Decode(Prefs.getString(KEY_PKEY,"NA"));    //generateSessionKey(); // 256 bit key
        byte[] pinitVector = base64Decode(Prefs.getString(KEY_PIV,"NA"));
        String finalresp = "";



            //String pkey_dec = decrypt(key, initVector, toString(pkey));
            //String pvoke_dec =  decrypt(key, initVector, toString(pvoke));
            String sessionkey = decrypt(pkey, pinitVector, AESCBCEncryption.toString(skey));
            String sessioniv = decrypt(pkey, pinitVector, AESCBCEncryption.toString(svoke));
            finalresp = decrypt(pkey, pinitVector, AESCBCEncryption.toString(input));
            JSONObject newjs = new JSONObject(finalresp);
            String tken = newjs.optString("token");

        Prefs.putString(KEY_TOKEN, tken);
        Prefs.putString(KEY_SKEY, sessionkey);
        Prefs.putString(KEY_SIV, sessioniv);

            ///System.out.println("pkey_dec ["+pkey_dec+"]");
            //System.out.println("pvoke_dec ["+pvoke_dec+"");
            Log("session key [" + sessionkey + "]");
            Log("sessioniv [" + sessioniv + "]");
            Log("finalresp [" + finalresp + "]");

            String gen = Utility.generateHashString(finalresp);
            Log("Hashing Status [" + gen.equals(dhash) + "]");

            decjsonobj.put("sessionkey", sessionkey);
            decjsonobj.put("sessioniv", sessioniv);
            decjsonobj.put("finalresp", finalresp);
            decjsonobj.put("hashstatus", gen.equals(dhash));


        Log(decjsonobj.toString());
        return new JSONObject(finalresp);

    }
    public  String genURLCBC(String params,String endpoint,  Context c) {
        String finpoint = "";
        if(checkInternetConnection()) {

            String token = Prefs.getString(KEY_TOKEN,"NA");
            SecurityLayer.Log("existing_token", token);
            String fnltkt = token;
            fnltkt = Utility.nextToken(fnltkt);
            SecurityLayer.Log("next_token", fnltkt);

            String skey = Prefs.getString(SecurityLayer.KEY_SKEY,"NA");
            SecurityLayer.Log("skey", skey);
            String siv = Prefs.getString(SecurityLayer.KEY_SIV,"NA");
            SecurityLayer.Log("siv", siv);
            String pkey = (Prefs.getString(SecurityLayer.KEY_PKEY,"NA"));
            SecurityLayer.Log("pkey", pkey);
            String piv = (Prefs.getString(SecurityLayer.KEY_PIV,"NA"));
            SecurityLayer.Log("piv", piv);
            int count = 0;


            String encappid = Prefs.getString("NWAPPID","NA");
            // String appid = session.getString(SecurityLayer.KEY_APP_ID);
            SecurityLayer.Log("appid gen url", encappid);
            System.out.println("appid gen url [" + encappid + "]");
            String encryptedpkey = "";
            String encryptedrandomIV = "";
            String encryptedUrl = "";

            String hash = null;


            String encryptedimei = "";


            String fsess = Prefs.getString(KEY_SESSION_ID,"NA");


            //String skkey = "EHxxOa9FpK256bvlaICg2bYVsxKodO4XekhsJEdNzaE=";
            //   String skkey = "4UhIX09CelA75Rdao2u+j/vnnkAopFQbbO/nbHnebf4=";
            //  SecurityLayer.Log("skey", skkey);
            // String siv = session.get(SecurityLayer.KEY_SIV);
            //   String siv ="j3at/AyFvk4J7h32D+cECQ==";
            SecurityLayer.Log("siv", siv);
            //String pkey = (session.get(SecurityLayer.KEY_PKEY));
            //  String pkkey = "xB8sn12WDeP8gM608ZkFjvd5CE/bxtmDb6MH9kk6R+4=";

            //String piv = (session.get(SecurityLayer.KEY_PIV));
            // String piv = "xLJ6Z4p+wgKik3jMG6V3lw==";
            //  String appid = "113260437012100";
            //  String appid = session.getString(SecurityLayer.KEY_APP_ID);

            SecurityLayer.Log("appid", encappid);
            StringBuffer sb = new StringBuffer();

            String imei = "Vokez";
            Log("Session Key", skey);
            Log("Personal  Key", pkey);
            Log("App ID", encappid);
            Log("Session ID", fsess);
            Log("Params", params);
            Log("Imei", imei);
System.out.println(imei);
SecurityLayer.Log("Imei chosen",imei);
            String year = Utility.getAppVersion();
            Log("vers",year);
            try {
                encryptedpkey = toHex(AESCBCEncryption.encrypt(base64Decode(pkey), base64Decode(piv), base64Encode(AESCBCEncryption.generateSessionKey())));
                encryptedrandomIV = toHex(AESCBCEncryption.encrypt(base64Decode(pkey), base64Decode(piv), base64Encode(AESCBCEncryption.generateIV())));
                encryptedUrl = toHex(AESCBCEncryption.encrypt(base64Decode(skey), base64Decode(siv), params));

                hash = Utility.generateHashString(params);
                encryptedimei = toHex(AESCBCEncryption.encrypt(base64Decode(skey), base64Decode(siv), imei));
                fsess = toHex(AESCBCEncryption.encrypt(base64Decode(skey), base64Decode(siv), fsess));
               // year = toHex(year);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String vers = encryptedrandomIV;

           finpoint = endpoint + "/" + encryptedUrl + "/" + hash + "/" + encryptedpkey + Constants.CH_KEY + "/" + encappid + "/" + fnltkt + "/" + encryptedimei + "/" + fsess + "/" + vers + "/" + year;


        }
        return  finpoint;
    }
    public static JSONObject decryptTransaction(JSONObject jsonobj,@Named("ApplicationContext") Context context) throws Exception {

        String status = jsonobj.getString("status");
        String svoke = jsonobj.getString("svoke");
        String input = jsonobj.getString("inp");
        System.out.println("svoke [" + svoke + "]");
        String dhash = jsonobj.getString("DHASH");

        // System.out.println("pkey ["+pkey+"]\n pvoke ["+pvoke+"]");

        JSONObject decjsonobj = new JSONObject();


        byte[] pkey = base64Decode(Prefs.getString(KEY_PKEY,"NA"));    //generateSessionKey(); // 256 bit key
        byte[] pinitVector = base64Decode(Prefs.getString(KEY_PIV,"NA"));

        byte[] skey = base64Decode(Prefs.getString(KEY_SKEY,"NA"));

        String finalresp = "";
        JSONObject data = null;

        if ("S".equals(status)) {

            //String pkey_dec = decrypt(key, initVector, toString(pkey));
            //String pvoke_dec =  decrypt(key, initVector, toString(pvoke));
            String sessioniv = decrypt(pkey, pinitVector, AESCBCEncryption.toString(svoke));
            finalresp = decrypt(skey, base64Decode(sessioniv), AESCBCEncryption.toString(input));

            Prefs.putString(KEY_SIV,sessioniv);
            data = new JSONObject(finalresp);

            String token = data.optString("token");
            SecurityLayer.Log("returned_token",token);
            Prefs.putString(KEY_TOKEN,token);

        //    String appid = data.optString("appid");

            int count = 0;




            System.out.println("finalresp [" + finalresp + "]");
            String gen = Utility.generateHashString(finalresp);
            System.out.println("Hashing Status [" + gen.equals(dhash) + "]");

            decjsonobj.put("pkey_dec", "");
            decjsonobj.put("pvoke_dec", "");
            decjsonobj.put("sessioniv", sessioniv);
            decjsonobj.put("finalresp", finalresp);
            decjsonobj.put("hashstatus", gen.equals(dhash));

        }
        SecurityLayer.Log(decjsonobj.toString());
        // System.out.println(decjsonobj);


    String respcode = data.optString("responseCode");


        return data;

    }

    public static void Log(String tag, String message) {
        if (isDebug) {
Log.v(tag,message);
        }
    }

    public static void Log(String message) {
        if (isDebug) {
            Log.v("",message);
        }
    }
}
