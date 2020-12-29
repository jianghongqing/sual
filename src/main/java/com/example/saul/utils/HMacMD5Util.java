package com.example.saul.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Auther: James
 * @Date: 2020/9/22 14:01
 * @Description:
 */
public class HMacMD5Util {
    /**
     * 计算签名
     *
     * @param key  签名秘钥，SigSecret
     * @param data 要进⾏签名的字符串
     * @return 根据data计算的签名字符串
     */
    public static String getHmacMd5Str(String key, String data) {
        String result = "";
        try {
            byte[] keyByte = key.getBytes("UTF-8");
            byte[] dataByte = data.getBytes("UTF-8");
            byte[] hmacMd5Byte = getHmacMd5Bytes(keyByte, dataByte);
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < hmacMd5Byte.length; i++) {
                if (Integer.toHexString(0xFF & hmacMd5Byte[i]).length() == 1)
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & hmacMd5Byte[i]));
                else
                    md5StrBuff.append(Integer
                            .toHexString(0xFF & hmacMd5Byte[i]));
            }
            result = md5StrBuff.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] getHmacMd5Bytes(byte[] key, byte[] data)
            throws NoSuchAlgorithmException {
     /*
     * HmacMd5 calculation formula: H(K XOR opad, H(K XOR ipad, text))
     * HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
     * H代表hash算法，本类中使⽤MD5算法，K代表密钥，text代表要加密的数据 ipad为
    0x36，opad为0x5C。
     */
        int length = 64;
        byte[] ipad = new byte[length];
        byte[] opad = new byte[length];
        for (int i = 0; i < 64; i++) {
            ipad[i] = 0x36;
            opad[i] = 0x5C;
        }
        byte[] actualKey = key; // Actual key.
        byte[] keyArr = new byte[length]; // Key bytes of 64 bytes length
     /*
     * If key's length is longer than 64,then use hash to digest it and
    use
     * the result as actual key. 如果密钥⻓度，⼤于64字节，就使⽤哈希算法，计算其
    摘要，作为真正的密钥。
     */
        if (key.length > length) {
            actualKey = md5(key);
        }
        for (int i = 0; i < actualKey.length; i++) {
            keyArr[i] = actualKey[i];
        }
        /*
         * append zeros to K 如果密钥⻓度不⾜64字节，就使⽤0x00补⻬到64字节。
         */
        if (actualKey.length < length) {
            for (int i = actualKey.length; i < keyArr.length; i++)
                keyArr[i] = 0x00;
        }
        /*
         * calc K XOR ipad 使⽤密钥和ipad进⾏异或运算。
         */
        byte[] kIpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kIpadXorResult[i] = (byte) (keyArr[i] ^ ipad[i]);
        }
         /*
         * append "text" to the end of "K XOR ipad" 将待加密数据追加到K XOR ipad计
        算结果后⾯。
         */
        byte[] firstAppendResult = new byte[kIpadXorResult.length +
                data.length];
        for (int i = 0; i < kIpadXorResult.length; i++) {
            firstAppendResult[i] = kIpadXorResult[i];
        }
        for (int i = 0; i < data.length; i++) {
            firstAppendResult[i + keyArr.length] = data[i];
        }

        /*
         * calc H(K XOR ipad, text) 使⽤哈希算法计算上⾯结果的摘要。
         */
        byte[] firstHashResult = md5(firstAppendResult);
        /*
         * calc K XOR opad 使⽤密钥和opad进⾏异或运算。
         */
        byte[] kOpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kOpadXorResult[i] = (byte) (keyArr[i] ^ opad[i]);
        }
        /*
         * append "H(K XOR ipad, text)" to the end of "K XOR opad" 将H(K XOR
         * ipad, text)结果追加到K XOR opad结果后⾯
         */
        byte[] secondAppendResult = new byte[kOpadXorResult.length
                + firstHashResult.length];
        for (int i = 0; i < kOpadXorResult.length; i++) {
            secondAppendResult[i] = kOpadXorResult[i];
        }
        for (int i = 0; i < firstHashResult.length; i++) {
            secondAppendResult[i + keyArr.length] = firstHashResult[i];
        }
        /*
         * H(K XOR opad, H(K XOR ipad, text)) 对上⾯的数据进⾏哈希运算。
         */
        byte[] hmacMd5Bytes = md5(secondAppendResult);
        return hmacMd5Bytes;
    }

    private static byte[] md5(byte[] str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str);
        return md.digest();
    }

}
