package com.example.saul.nettyServer;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: James
 * @Date: 2020/12/28 20:38
 * @Description:
 */
public class MyChannelHandlerMap {
    /**
     * 保存映射关系的双向Hash表
     */
    public static BiDirectionHashMap<Long, Channel> biDirectionHashMap = new BiDirectionHashMap<>();

    /**
     * TODO: 不活跃连接/异常连接清除
     * 记录最后一次通信时间, 用于确定不活跃连接，然后清理掉
     */
    public static ConcurrentHashMap<Long, Date> lastUpdate = new ConcurrentHashMap<>();

    /**
     * 是否存在连接
     *
     * @param id
     * @return
     */
    public boolean existConnectionByID(Long id) {
        return biDirectionHashMap.containsKey(id);
    }
}
