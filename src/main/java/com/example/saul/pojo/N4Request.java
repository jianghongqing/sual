package com.example.saul.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


@Data
public class N4Request  {
    @JSONField(name = "StartChargeSeq")
    private String StartChargeSeq;//充电订单号，必填，格式"运营商ID+唯一编号"，27字符
    @JSONField(name = "ConnectorID")
    private String ConnectorID;//充电设备接口编码，必填
    @JSONField(name = "StartTime")
    private String StartTime;//充电开始时间，必填，格式"yyyy-MM-dd HH:mm:ss"
    @JSONField(name = "EndTime")
    private String EndTime;//充电结束时间，必填，格式"yyyy-MM-dd HH:mm:ss"
    @JSONField(name = "TotalPower")
    private float TotalPower;//累计充电量，必填，2位小数
    @JSONField(name = "ElecMoney")
    private float ElecMoney;//总电费，必填，2位小数
    @JSONField(name = "SeviceMoney")
    private float SeviceMoney;//总服务费，必填，2位小数
    @JSONField(name = "TotalMoney")
    private float TotalMoney;//累计总金额，必填，2位小数

    /**
     * 充电结束原因，必填
     * 0：用户手动停止充电
     * 1：客户归属地运营商平台停止充电
     * 2：BMS停止充电
     * 3：充电机设备故障
     * 4：连接器断开
     * 5~99：自定义
     */
    @JSONField(name = "StopReason")
    private int StopReason;
    @JSONField(name = "SumPeriod")
    private int SumPeriod;//时段数N,范围0~32，不必填
}
