package com.example.saul.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * ConnectorStatus
 */
@Data
public class N5Request {

    @JSONField(name = "ConnectorStatusInfo")
    private N5ConnectorStatusInfo ConnectorStatusInfo;

}
