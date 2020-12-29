package com.example.saul.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2019/8/21 09:27
 * @Description:
 */

public interface TestService {
    public Map<String, Object> getPersonById(int personId);
}
