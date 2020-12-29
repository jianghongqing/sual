package com.example.saul.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Auther: James
 * @Date: 2020/9/22 13:56
 * @Description:
 */
public class QEncodeUtil {
    /**
     * 加密
     *
     * @param sSrc         明⽂
     * @param sKey         数据秘钥，DataSecret
     * @param dataSecretIV 初始向量，DataSecretIV
     * @return 加密后的密⽂
     */
    public static String encrypt(String sSrc, String sKey, String dataSecretIV) {
        try {
            if (sKey == null) {
                return null;
            }
            //判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码⽅式"
            IvParameterSpec iv = new
                    IvParameterSpec(dataSecretIV.getBytes());//使⽤CBC模式，需要⼀个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());

            //此处使⽤BASE64做转码功能，同时能起到2次加密的作⽤。
            String result = new BASE64Encoder().encode(encrypted);
            result = result.replaceAll("\r", "");
            result = result.replaceAll("\n", "");
            return result;
        } catch (Exception ex) {
            return null;
        }
    }


    public static String Encrypt(String str, String key, String iv) {
        String resultObj = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            //使用加密模式初始化 密钥
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes("UTF-8")));
            //按单部分操作加密或解密数据，或者结束一个多部分操作。
            byte[] encode = cipher.doFinal(str.getBytes("UTF-8"));
            resultObj = new BASE64Encoder().encode(encode);
        } catch (Exception e) {
//            logger.error("AESUtil Encrypt1 : " , e);
        }
        return resultObj;
    }

    /**
     * 解密
     *
     * @param sSrc         密⽂
     * @param sKey         数据秘钥，DataSecret
     * @param dataSecretIV 初始向量，DataSecretIV
     * @return 明⽂
     */
    public static String decrypt(String sSrc, String sKey, String dataSecretIV) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key⻓度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(dataSecretIV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先⽤base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "UTF-8");
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
