package com.futuretongfu.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class SHA1Util {

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    public static String getHmacSHA1(String encryptText, String encryptKey) throws Exception
    {
        return convertToHex(hmacSHA1Encrypt(encryptText, encryptKey));
    }

    public static String getHmacSha1Base64(String base, String key) throws NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secret);

        byte[] digest = mac.doFinal(base.getBytes());

        return Base64.encodeToString(digest, Base64.URL_SAFE | Base64.NO_WRAP);

    }

    public static byte[] hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
    {
        byte[] data = encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作
        return mac.doFinal(text);
    }

    public static String getSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();

        return convertToHex(sha1hash);
    }

    public static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append(
                        (0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10))
                );
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

}
