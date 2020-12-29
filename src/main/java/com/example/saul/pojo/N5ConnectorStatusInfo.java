package com.example.saul.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class N5ConnectorStatusInfo {

    @JSONField(name = "ConnectorID")
    private String ConnectorID;//充电设备接口编码，必要，<=26字符

    /**
     * 充电设备接口状态，必要
     * 0：离网 1：空闲
     * 2：占用（未充电）
     * 3：占用（充电中）
     * 4：占用（预约锁定）
     * 255：故障
     */
    @JSONField(name = "Status")
    private int Status;
    @JSONField(name = "ParkStatus")
    private int ParkStatus;//车位状态 0未知 10空闲 50占用，不必要
    @JSONField(name = "LockStatus")
    private int LockStatus;//地锁状态 0未知 10已解锁 50已上锁,不必要

}
