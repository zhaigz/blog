package com.zgz.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: MD5Utils
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 17:52
 * @Version: 1.0
 **/
public class MD5Utils {
    public static String code(String str){

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] byteDigest = md5.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for(int offset = 0;offset < byteDigest.length;offset++){
                i = byteDigest[offset];
                if(i<0)
                    i+=256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            //16位加密
            // return buf.toString().substring(8,24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(code("123456"));
    }
}
