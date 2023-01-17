package com.sdmc.emqx.emqx.callback;

import com.alibaba.fastjson.JSONObject;
import com.sdmc.emqx.emqx.client.EmqClient;
import com.sdmc.emqx.emqx.enums.GatewayEventConstant;
import com.sdmc.emqx.emqx.enums.QosEnum;
import com.sdmc.emqx.entity.GatewayOnlineRecord;
import com.sdmc.emqx.reporthandder.ReportFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sdmc.emqx.utils.GetTopicUtils.getReportTopic;
import static com.sdmc.emqx.utils.GetTopicUtils.getSysTopic;

/**
 * @author James
 * @version 1.0.0
 * @ClassName EmqClient.java
 * @Description 实现MqttCallbackExtended接口
 * connectComplete会自动重连，需要重新订阅主题
 * @createTime 2021年10月20日 20:40:00
 */
@Slf4j
@Component
public class MessageCallback implements MqttCallbackExtended {

    @Resource
    private EmqClient emqClient;

    @Resource
    private ReportFactory reportFactory;

    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 丢失了对服务端的连接后触发的回调
     *
     * @param cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        // 丢失对服务端的连接后触发该方法回调，此处可以做一些特殊处理，比如重连
        log.info("丢失了对服务端的连接");
        emqClient.reConnect();
    }

    /**
     * 订阅到消息后的回调
     * 该方法由mqtt客户端同步调用,在此方法未正确返回之前，不会发送ack确认消息到broker
     * 一旦该方法向外抛出了异常客户端将异常关闭，当再次连接时；所有QoS1,QoS2且客户端未进行ack确认的 消息都将由
     * broker服务器再次发送到客户端
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.info("订阅者订阅到了消息,topic={},payload={}", topic, new String(message.getPayload()));
        try {
            //解析订阅的消息
            JSONObject dataParams = null;
            String gatewayId = topic.substring(8, 15);
            String jsonStr = new String(message.getPayload());
            JSONObject data = JSONObject.parseObject(jsonStr);
            String event = data.getString("method");
            if (topic.contains("test_username_")) {
                GatewayOnlineRecord record = GatewayOnlineRecord.builder()
                        .clientId(data.getString("clientid"))
                        .ip(data.getString("ipaddress"))
                        .userName(data.getString("username"))
                        .connectType(topic.contains("disconnected") ? 2 : 1)
                        .disconnectedAt(topic.contains("disconnected") ? data.getDate("disconnected_at") : null)
                        .connectedAt(topic.contains("disconnected") ? null : data.getDate("connected_at"))
                        .build();
                mongoTemplate.save(record);
                if (topic.contains("disconnected")) {
                    reportFactory.getReportIns(GatewayEventConstant.GATEWAY_DISCONNECTED_POST, data, data.getString("username"));
                    return;
                } else if (topic.contains("connected")) {
                    reportFactory.getReportIns(GatewayEventConstant.GATEWAY_CONNECTED_POST, data, data.getString("username"));
                    return;
                }
            }

            if (topic.contains("stateLink")) {
                //订阅者订阅到了消息,topic=/report/6066731/stateLink,payload={"id":"6","version":"1.0","params":[],"method":"event.stateLinkGrps.post"}
                //dataParams = data.getJSONObject("params");
                return;
            } else {
                dataParams = data.getJSONObject("params");
            }
            reportFactory.getReportIns(event, dataParams, gatewayId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 消息发布完成且收到ack确认后的回调
     * QoS0：消息被网络发出后触发一次
     * QoS1：当收到broker的PUBACK消息后触发
     * QoS2：当收到broer的PUBCOMP消息后触发
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        int messageId = token.getMessageId();
        String[] topics = token.getTopics();
        log.info("消息发布完成,messageid={},topics={}", messageId, topics);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            // 重连后要自己重新订阅topic，这样emq服务发的消息才能重新接收到，不然的话，断开后客户端只是重新连接了服务，并没有自动订阅，导致接收不到消息
            emqClient.subscribe(getReportTopic(), QosEnum.QoS1);
            emqClient.subscribe(getSysTopic(), QosEnum.QoS0);
            log.info("连接成功,连接方式:{}", reconnect ? "重连完成..." : "直连完成...");
        } catch (Exception e) {
            log.info("订阅出现异常:{}", e);
        }
    }

    public static void main(String[] args) {
        String topic = "$SYS/brokers/emqx@127.0.0.1/clients/sdmc351665737382/disconnected";
        String str = "{\"username\":\"sdmc\",\"ts\":1665737597466,\"sockport\":1883,\"reason\":\"tcp_closed\",\"protocol\":\"mqtt\",\"proto_ver\":4,\"proto_name\":\"MQTT\",\"ipaddress\":\"116.6.18.115\",\"disconnected_at\":1665737597466,\"connected_at\":1665737381651,\"clientid\":\"sdmc351665737382\"}";
        JSONObject data = JSONObject.parseObject(str);


        if (topic.contains("connected")) {
            GatewayOnlineRecord record = GatewayOnlineRecord.builder()
                    .clientId(data.getString("clientid"))
                    .ip(data.getString("ipaddress"))
                    .userName(data.getString("username"))
                    .connectType(topic.contains("disconnected") ? 2 : 1)
                    .disconnectedAt(topic.contains("disconnected") ? data.getDate("disconnected_at") : null)
                    .connectedAt(topic.contains("disconnected") ? null : data.getDate("connected_at"))
                    .build();
            log.info("GatewayOnlineRecord: " + record);
        }
    }

}