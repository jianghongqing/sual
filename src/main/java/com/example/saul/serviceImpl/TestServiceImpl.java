package com.example.saul.serviceImpl;

import com.example.saul.dao.TestMapper;
import com.example.saul.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: James
 * @Date: 2019/8/21 09:28
 * @Description:
 */
@Service
public class TestServiceImpl implements TestService {
    @Resource
    private TestMapper testMapper;

    @Override
    public Map<String, Object> getPersonById(int personId) {

        return testMapper.getPersonById(personId);
    }
}
