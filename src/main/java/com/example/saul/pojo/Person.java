package com.example.saul.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Person {

    private long personId;
    private String wxName;
    private String phone;
    private String carNum;
    private String password;
    private String openId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registerTime;
    private String batterySerialNum;
    private long shiftStatus;
    private long isSubstitute;

}
