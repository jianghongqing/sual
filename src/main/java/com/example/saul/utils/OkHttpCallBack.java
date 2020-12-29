package com.example.saul.utils;

import okhttp3.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class OkHttpCallBack<T> implements okhttp3.Callback {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpCallBack.class);
    /*
     * 自定义一个获取泛型类的方法 获取泛型类
     */
    protected Class<T> getClassType() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<T>) params[0];

    }



    public abstract void onSuccess(String result);
    public abstract void onNull(String errMsg);

    public abstract void onFail(int code, String errMsg);


    @Override
    public void onFailure(Call arg0, IOException arg1) {
        logger.error("=====net =====");
        arg1.printStackTrace();
        onFail(-1, "网络错误,请稍后再试!");
    }

    @Override
    public void onResponse(Call arg0, okhttp3.Response response)
            throws IOException {
        String text = response.body().string();
        logger.info("code:" + response.code());
        logger.info("response:\n" + text);
        if (text == null || text.length() < 1) {
            onNull("返回数据为空");
            return;
        }
        onSuccess(text);
    }


}
