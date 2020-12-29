package com.example.saul.pojo;

import lombok.Data;

/**
 * @Auther: James
 * @Date: 2020/9/21 16:57
 * @Description:
 */
@Data
public class ChargingDetail {
    private double electricityMeterRecord;

    private int startSoc;

    private int endSoc;

    private String startTime;

    private String endTime;

    private int status;

    private double powerConsumption;

    private int mark;

    private String orderNum;
}
