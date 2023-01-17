package com.sdmc.emqx.emqx.enums;

/**
 * @author James
 * @version 1.0.0
 * @ClassName GatewaySendConstant.java
 * @Description TODO
 * @createTime 2022年03月30日 17:50:00
 */
public class GatewaySendConstant {

    public final static String SERVICE_PERMITJOIN_SET =  "service.permitJoin.set";

    public final static String SERVICE_CHILDGATEWAY_ADD =  "service.childGateway.add";

    public final static String SERVICE_DEVICE_DEL =  "service.device.del";

    public final static String SERVICE_STATE_SET =  "service.state.set";

    public final static String SERVICE_CURTAINLEVEL_SET = "service.curtainLevel.set";

    public final static String SERVICE_GATEWAY_RESET = "service.gateway.reset";

    /**
     * 添加场景
     */
    public final static String SERVICE_AUTOSCENE_ADD = "service.autoScene.add";

    /**
     * 请求获取所有的自动化场景
     */
    public final static String SERVICE_AUTOSCENE_GET = "service.autoScene.get";

    /**
     * 删除指定名称的自动化场景
     */
    public final static String SERVICE_AUTOSCENE_DEL = "service.autoScene.del";

    /**
     * 执行指定名称的自动化场景(适用于设置手动条件的情况)
     */
    public final static String SERVICE_AUTOSCENE_EXEC = "service.autoScene.exec";

    /**
     * 设置场景的开关状态
     */
    public final static String SERVICE_AUTOSCENE_STATE_SET = "service.autoScene_state.set";

}
