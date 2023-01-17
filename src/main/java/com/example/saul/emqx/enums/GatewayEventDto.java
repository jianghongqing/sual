package com.sdmc.emqx.emqx.enums;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author James
 * @version 1.0.0
 * @ClassName GatewayEventDto.java
 * @Description TODO
 * @createTime 2022年03月30日 17:43:00
 */
@Data
public class GatewayEventDto {
    private String id;

    private String version;

    private JSONObject params;

    private String method;

    private int uptime;
}
