//package com.example.saul.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.example.saul.config.ConfigValue;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import static com.example.saul.utils.Util.getSeq;
//
///**
// * @Auther: James
// * @Date: 2020/9/22 14:29
// * @Description: to
// */
//@Slf4j
//@Service
//public class TokenUtilService {
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Resource
//    ConfigValue configValue;
//
//    @Value("${jwt.requstUrl}")
//    private String jwtRequstUrl;
//
//
//    /**
//     * sSrc         明⽂
//     * sKey         数据秘钥，DataSecret
//     * dataSecretIV 初始向量，DataSecretIV
//     *
//     * @return token  获取
//     */
//    public String getAccessToken() {
//        if (redisTemplate.hasKey("accessToken")) {
//            return redisTemplate.opsForValue().get("accessToken").toString();
//        }
//        JSONObject requestData = new JSONObject();
//        requestData.put("OperatorID", configValue.getOperatorID());
//        requestData.put("OperatorSecret", configValue.getOperatorSecret());
//        String dataStr = JSON.toJSONString(requestData);
//        log.info("请求参数[加密前]=" + dataStr);
//        // 生成加密签名参数
//        String data = QEncodeUtil.encrypt(dataStr, configValue.getSKey(), configValue.getDataSecretIV()).replace("\n", "").replace("\r", "");
//
//        Map parameter = new HashMap();
//        parameter.put("OperatorID", configValue.getOperatorID());
//
//        parameter.put("Data", data);
//        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        parameter.put("TimeStamp", dateStr);
//        String seq = getSeq(dateStr);
//        parameter.put("Seq", seq);
//        String signMsg = configValue.getOperatorID() + data + dateStr + seq;
//        try {
//            parameter.put("Sig", HMacMD5Util.getHmacMd5Str(configValue.getSigSecret(), signMsg));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        String parameStr = JSON.toJSONString(parameter);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), parameStr);
//        String result = OkHttpUtils.post(requestBody, configValue.getRequstUrl() + "query_token");
//        log.info("回调 queryToken=" + result);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        String jsonStr = resultJson.getString("Data");
//        String queryTokenStr = QEncodeUtil.decrypt(jsonStr, configValue.getSKey(), configValue.getDataSecretIV()).replace("\n", "").replace("\r", "");
//        log.info("回调 queryToken=" + queryTokenStr);
//        JSONObject queryToken = JSONObject.parseObject(queryTokenStr);
//        redisTemplate.opsForValue().set("accessToken", queryToken.getString("AccessToken"));
//        redisTemplate.expire("accessToken", queryToken.getInteger("TokenAvailableTime"), TimeUnit.SECONDS);
//        return queryToken.getString("AccessToken");
//    }
//
//    public String getJwtAccessToken(String OperatorID) {
//        if (redisTemplate.hasKey("jwtAccessToken")) {
//            return redisTemplate.opsForValue().get("jwtAccessToken").toString();
//        }
//        JSONObject requestData = new JSONObject();
//        requestData.put("OperatorID", OperatorID);
//        requestData.put("OperatorSecret", configValue.getOperatorSecret());
//        String dataStr = JSON.toJSONString(requestData);
//        log.info("请求参数[加密前]=" + dataStr);
//        // 生成加密签名参数
//        String data = QEncodeUtil.encrypt(dataStr, configValue.getSKey(), configValue.getDataSecretIV()).replace("\n", "").replace("\r", "");
//
//        Map parameter = new HashMap();
//        parameter.put("OperatorID", OperatorID);
//
//        parameter.put("Data", data);
//        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        parameter.put("TimeStamp", dateStr);
//        String seq = getSeq(dateStr);
//        parameter.put("Seq", seq);
//        String signMsg = OperatorID + data + dateStr + seq;
//        try {
//            parameter.put("Sig", HMacMD5Util.getHmacMd5Str(configValue.getSigSecret(), signMsg));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        String parameStr = JSON.toJSONString(parameter);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), parameStr);
//        String result = OkHttpUtils.post(requestBody, jwtRequstUrl + "query_token");
//        log.info("回调 queryToken=" + result);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        String jsonStr = resultJson.getString("Data");
//        String queryTokenStr = QEncodeUtil.decrypt(jsonStr, configValue.getSKey(), configValue.getDataSecretIV()).replace("\n", "").replace("\r", "");
//        log.info("回调 queryToken=" + queryTokenStr);
//        JSONObject queryToken = JSONObject.parseObject(queryTokenStr);
//        redisTemplate.opsForValue().set("jwtAccessToken", queryToken.getString("AccessToken"));
//        redisTemplate.expire("jwtAccessToken", queryToken.getInteger("TokenAvailableTime"), TimeUnit.SECONDS);
//        return queryToken.getString("AccessToken");
//    }
//
//    /**
//     * 处理数据，对Data进行加密后签名 生成处理后的数据
//     *
//     * @param jsonParam aesKey
//     *                  aesIV
//     *                  signKey
//     * @throws Exception
//     */
//    public String dealParam(String jsonParam,String OperatorID) {
//        HashMap<String, String> paramObj = new HashMap<>();
//        String encryptStr = QEncodeUtil.encrypt(jsonParam, configValue.getSKey(), configValue.getDataSecretIV());
//        paramObj.put("Data", encryptStr);
//        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        paramObj.put("TimeStamp", dateStr);
//        String seq = getSeq(dateStr);
//        paramObj.put("Seq", seq);
//        paramObj.put("OperatorID",OperatorID);
//        String signMsg = OperatorID + encryptStr + dateStr + seq;
//        try {
//            paramObj.put("Sig", HMacMD5Util.getHmacMd5Str(configValue.getSigSecret(), signMsg));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return JSON.toJSONString(paramObj);
//    }
//
//
//    public static void main(String... args) throws Exception {
//        String OperatorID = "MA01H3BQ3";
//        JSONObject requestData = new JSONObject();
//        requestData.put("OperatorID", OperatorID);
//        requestData.put("OperatorSecret", "a762796b2a8989b8");
//        String dataStr = JSON.toJSONString(requestData);
//        log.info("请求参数[加密前]=" + dataStr);
//        // 生成加密签名参数
//        String data = QEncodeUtil.encrypt(dataStr, "98f46ab6481d87c4", "978170fd1c11a70e").replace("\n", "").replace("\r", "");
//
//        Map parameter = new HashMap();
//        parameter.put("OperatorID",OperatorID);
//
//        parameter.put("Data", data);
//        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        parameter.put("TimeStamp", dateStr);
//        String seq = getSeq(dateStr);
//        parameter.put("Seq", seq);
//        String signMsg = OperatorID + data + dateStr + seq;
//        try {
//            parameter.put("Sig", HMacMD5Util.getHmacMd5Str("52cd107eb677c004", signMsg));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        String parameStr = JSON.toJSONString(parameter);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), parameStr);
////        String result = OkHttpUtils.post(requestBody, "http://test-hlht-dipper-xmn.uniev.com/evcs/v1.0.0/query_token");
//        String result = OkHttpUtils.post(requestBody, "http://nesp.jwt520.com:8888/notify/J1tGyzocT/query_token");
//        log.info("回调 queryToken=" + result);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        String res = resultJson.getString("Data");
//        String token = QEncodeUtil.decrypt(res, "98f46ab6481d87c4", "978170fd1c11a70e").replace("\n", "").replace("\r", "");
//        log.info("回调 queryToken=" + token);
//    }
//}
