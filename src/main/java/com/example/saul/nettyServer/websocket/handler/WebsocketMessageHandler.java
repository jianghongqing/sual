package com.example.saul.nettyServer.websocket.handler;

import com.example.saul.nettyServer.MyChannelHandlerMap;
import com.example.saul.nettyServer.Utils;
import com.example.saul.nettyServer.service.DiscardService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;


/*
 * @Author James
 * @Description //TODO 
 * @Date 18:06 2020/12/25
 * @Param 
 * @return 
 **/
@Sharable
@Component
public class WebsocketMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketMessageHandler.class);

    @Autowired
    private DiscardService discardService;

    @Autowired
    private RedisTemplate redisTemplate;

    /** 客户端请求的心跳命令 */
    private static final ByteBuf HEARTBEAT_SEQUENCE =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("heartbeat", CharsetUtil.UTF_8));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            String text = textWebSocketFrame.text();
            // 业务层处理数据
            this.discardService.discard(text);
            //心跳机制
            if ("heartbeat".equals(text)) {
                LOGGER.info(ctx.channel().remoteAddress() + "===>server: " + text);
                ctx.channel().writeAndFlush(new TextWebSocketFrame("heartbeat"));
            }
            Channel channel = ctx.channel();
            freshTime(channel);
            Utils.log("read0: " + text);
            // 收到生成ID的指令, 返回 id:xxxxxxxx
            if (text.equals("getID")) {
                // 已建立连接, 则返回已有ID
                if (MyChannelHandlerMap.biDirectionHashMap.containsValue(channel)) {
                    Long id = MyChannelHandlerMap.biDirectionHashMap.getByValue(channel);
                    channel.writeAndFlush(new TextWebSocketFrame("id:" + id));
                    return;
                }
                Long id = Utils.generateID();  // 创建ID
                Utils.log("id ->  " + id);
                channel.writeAndFlush(new TextWebSocketFrame("id:" + id));
                MyChannelHandlerMap.biDirectionHashMap.put(id, ctx.channel());
                MyChannelHandlerMap.lastUpdate.put(id, new Date());
            }
        } else {
            // 不接受文本以外的数据帧类型
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        if (!MyChannelHandlerMap.biDirectionHashMap.containsValue(channel)) {
            LOGGER.info("该客户端未注册");
            return;
        }
        MyChannelHandlerMap.biDirectionHashMap.removeByValue(channel);
        LOGGER.info("链接断开：{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("链接创建：{}", ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(IdleState.READER_IDLE.equals(event.state())) {
                LOGGER.info("读超时检测 {}",ctx.channel().remoteAddress());
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE) ;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 刷新最后一次通信时间
     * @param channel 通道
     */
    private void freshTime (Channel channel) {
        if (MyChannelHandlerMap.biDirectionHashMap.containsValue(channel)) {
            long id = MyChannelHandlerMap.biDirectionHashMap.getByValue(channel);
            MyChannelHandlerMap.lastUpdate.put(id, new Date());
        }
    }

}
