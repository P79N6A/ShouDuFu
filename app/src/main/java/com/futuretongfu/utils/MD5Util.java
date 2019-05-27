package com.futuretongfu.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class MD5Util {

    /**
     * 获取MD5字符串
     *
     * @param content 需要转换的字符串
     * @return String
     */
    public static String getMD5(String content) {
        try {

            byte[] resultByte = DigestUtils.getMd5Digest().digest(content.getBytes(Charset.forName("UTF8")));
            return new String(Hex.encodeHex(resultByte));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5(byte[] s) {
        return new String(Hex.encodeHex(DigestUtils.getMd5Digest().digest(s)));
    }

}
