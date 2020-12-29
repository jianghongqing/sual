package com.example.saul.nettyServer;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: James
 * @Date: 2020/12/28 20:45
 * @Description:
 */
@Slf4j
public class Utils {
    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * 生成ID, time: 13位 + random: 3位
     *
     * @return id
     */
    public static long generateID() {
        return 1000000000 + counter.getAndIncrement();
    }

    /**
     * 打印
     *
     * @param s
     */
    public static void log(String msg) {
        log.info("[" + new Date().toString() + "]  " + msg);
    }
}
