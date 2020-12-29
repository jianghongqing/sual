package com.example.saul.utils;

import java.util.Random;

/**
 * @Auther: James
 * @Date: 2019/6/27 18:14
 * @Description:
 */
public class AppointmentCodeUtils {
    public static String appointmentCode() {
        StringBuffer flag = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
            Random rand = new Random();
            flag.append(sources.charAt(rand.nextInt(6)) + "");
        }
        System.out.println(flag.toString());
        return flag.toString();
    }

}
