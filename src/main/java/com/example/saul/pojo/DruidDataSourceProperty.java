package com.example.saul.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: James
 * @Date: 2019/8/29 11:06
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
@Data
public class DruidDataSourceProperty {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize = 0;
    private int minIdle;
    private int maxActive = 8;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis = 1000 * 60;
    private int minEvictableIdleTimeMillis = 1000 * 60 * 30;
    private String validationQuery;
    private boolean testWhileIdle = false;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;
    private boolean poolPreparedStatements = false;
    private int maxPoolPreparedStatementPerConnectionSize = -1;
    private String filters;
    private boolean useGlobalDataSourceStat = false;
    private String connectionProperties;
}
