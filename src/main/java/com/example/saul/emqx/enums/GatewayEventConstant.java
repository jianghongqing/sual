package com.sdmc.emqx.emqx.enums;

/**
 * @author James
 * @version 1.0.0
 * @ClassName GatewayEvent.java
 * @Description TODO
 * @createTime 2022年03月30日 17:19:00
 */
public class GatewayEventConstant {

    public final static String EVENT_PROPERTIES_POST = "event.properties.post";
    /**
     * 网关配网状态
     */
    public final static String EVENT_PERMITJOIN_POST = "event.permitJoin.post";

    /**
     * 网关配网状态
     */
    public final static String EVENT_PERMITJOIN_SET = "event.permitJoin.set";
    /**
     * 设备添加成功
     */
    public final static String EVENT_DEVICE_ADD = "event.device.add";

    /**
     * 设备添加成功
     */
    public final static String EVENT_DEVICE_UPDATE = "event.device.update";
    /**
     * 设备信息
     */
    public final static String EVENT_DEVICES_POST = "event.devices.post";
    /**
     * 设备数量
     */
    public final static String EVENT_DEVICENUMBER_POST = "event.deviceNumber.post";
    /**
     * 自动化场景详情上报
     */
    public final static String EVENT_AUTOSCENE_POST = "event.autoScene.post";
    /**
     * 自动化场景的添加上报
     */
    public final static String EVENT_AUTOSCENE_ADD = "event.autoScene.add";
    /**
     * 自动化场景的删除上报
     */
    public final static String EVENT_AUTOSCENE_DEL = "event.autoScene.del";
    /**
     * 自动化场景执行的应答上报
     */
    public final static String EVENT_AUTOSCENE_EXEC = "event.autoScene.exec";
    /**
     * 自动化场景状态设置的应答上报
     */
    public final static String EVENT_AUTOSCENE_STATE_SET = "event.autoScene_state.set";
    /**
     * 场景触发上报
     */
    public final static String EVENT_SCENETRIGGER_POST = "event.sceneTrigger.post";
    /**
     * 告警上报
     */
    public final static String EVENT_SCENEALARM_POST = "event.sceneAlarm.post";
    /**
     * 场景的异常上报
     */
    public final static String event_autoScene_error = "event.autoScene.error";
    /**
     * 网关在线
     */
    public final static String EVENT_ONLINE_POST = "event.online.post";

    public final static String EVENT_GROUPS_POST = "event.groups.post";

    public final static String EVENT_DEFENSES_POST = "event.defenses.post";
    /**
     *
     */
    public final static String EVENT_STATELINKGRPS_POST = "event.stateLinkGrps.post";
    /**
     * 协议信息
     */
    public final static String EVENT_ZGBINFO_POST = "event.zgbinfo.post";
    /**
     * 第三方云信息(数据上报来自第三方云固件信息的event.customCloud.localInfo)
     */
    public final static String EVENT_CUSTOMCLOUD_LOCALINFO = "event.customCloud.localInfo";
    /**
     * 人体红外
     */
    public final static String EVENT_SENSOR_BODY_STATE_POST = "event.sensor_body_state.post";

    /**
     * 水浸
     */
    public final static String EVENT_SENSOR_WATER_STATE_POST = "event.sensor_water_state.post";

    /**
     * 声光报警传感器
     */
    public final static String EVENT_SOUND_LIGHTALARM_STATE_POST = "event.sound_lightAlarm_state.post";
    /**
     * 燃气报警
     */
    public final static String EVENT_SENSOR_GAS_STATE_POST = "event.sensor_gas_state.post";

    /***
     * 门磁
     */
    public final static String EVENT_SENSOR_DOOR_STATE_POST = "event.sensor_door_state.post";

    /**
     * 烟雾报警  event.sensor_smoke_state.post
     */
    public final static String EVENT_SENSOR_SMOKE_STATE_POST = "event.sensor_smoke_state.post";

    /**
     * 电池电量更新
     */
    public final static String EVENT_BATPERCENT_POST = "event.batPercent.post";


    public final static String EVENT_LOWVOLTAGE_POST = "event.lowVoltage.post";

    /**
     * 开关状态
     */
    public final static String EVENT_STATE_POST = "event.state.post";

    /**
     * 网关重置
     */
    public final static String EVENT_GATEWAY_RESET = "event.gateway.reset";

    /**
     * 设备删除
     */
    public final static String EVENT_DEVICE_DEL = "event.device.del";


    /**
     * 网关信息上报
     */
    public final static String EVENT_CHILDGATEWAY_POST = "event.childGateway.post";

    /**
     * 安防设备删除
     */
    public final static String EVENT_DEFENSEDEL_POST = "event.defenseDel.post";


    /**
     * 安防设备添加
     */
    public final static String EVENT_DEFENSEADD_POST = "event.defenseAdd.post";

    /**
     * 湿度
     */
    public final static String EVENT_HUMIDITY_POST = "event.humidity.post";


    /**
     * 温度
     */
    public final static String EVENT_TEMPERATURE_POST = "event.temperature.post";


    /**
     * 防拆
     */
    public final static String EVENT_TAMPER_POST = "event.tamper.post";


    /**
     * 防拆
     */
    public final static String GATEWAY_DISCONNECTED_POST = "gateway.disconnected.post";

    public final static String GATEWAY_CONNECTED_POST = "gateway.connected.post";


}
