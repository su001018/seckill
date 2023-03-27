package com.example.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    private static final String SLAT = "F9B1187306841F6B93COB5DCEA8B163F";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass) {
        String s = ""+SLAT.charAt(4) + SLAT.charAt(10) + inputPass + SLAT.charAt(3) + SLAT.charAt(15);
        return md5(s);
    }

    public static String formPassToDBPass(String formPass, String slat) {
        String s = ""+slat.charAt(0) + slat.charAt(2) + formPass + slat.charAt(4) + slat.charAt(5);
        return md5(s);
    }

    public static String inputPassToDBPass(String inputPass,String slat){
        return formPassToDBPass(inputPassToFormPass(inputPass),slat);
    }

    public static void main(String[] args){
        //224c3c46da29814ccaa32f069c1668a2
        System.out.println(inputPassToFormPass("123456"));
        //9dc60ab00ba58e0245398628e5fae279
        System.out.println(formPassToDBPass("224c3c46da29814ccaa32f069c1668a2",SLAT));
    }
}
