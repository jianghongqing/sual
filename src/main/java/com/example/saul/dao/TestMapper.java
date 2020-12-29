package com.example.saul.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2019/8/21 09:24
 * @Description:
 */
@Mapper
public interface TestMapper extends BaseMapper {
    Map<String, Object> getPersonById(@Param("personId") int personId);

}
