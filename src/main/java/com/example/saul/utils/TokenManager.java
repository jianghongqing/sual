//package com.example.saul.utils;
//
//import com.alibaba.fastjson.JSON;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Locale;
//
//public class TokenManager {
//    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);
//
//    private static final HashMap<String, Boolean> queryStatus = new HashMap<>();
//
//    public synchronized static String getAccessToken(RedisTools redisTools, CompanyConfig config) {
//        String tokenKey = String.format(Locale.CHINA, "sodb_r_%s_%d", config.getOperatorID(), config.getServiceId());
//        TokenLogService tokenLogService = SpringUtil.getBean(TokenLogService.class);
////        String reqPara = "";
//        String resResult = "";
//        try {
//            long ttl = redisTools.ttl("", tokenKey);
//            if (ttl <= 300 || queryStatus.getOrDefault(tokenKey, true)) {
//                //当token有效期小于等于1时重新获取，避免双方有误差导致推送时，token失效推送失败
//                N0Request requestData = new N0Request();
//                requestData.setOperatorID(config.getCseOperatorID());
//                requestData.setOperatorSecret(config.getCesOperatorSecret());
//                String data = JSON.toJSONString(requestData);
//                logger.info("请求参数[加密前]=" + data);
//                // 生成加密签名参数
//                String data2 = Util.dealParam(data, config.getAesKey(), config.getAesIv(), config.getSignKey(), config.getCseOperatorID());
//                logger.info("请求参数[加密后]=" + data2);
//                reqPara = data2;
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data2);
//                String result = OkHttpUtils.post(requestBody, config.getUrl() + config.getQueryTokenMethod());
//
//                resResult = result;
//                logger.info("回调 queryToken=" + result);
//                if (result != null) {
//                    ResponseModelImp resultJson = JSON.parseObject(result, ResponseModelImp.class);
//                    if (resultJson != null && Constant.succ.equals(resultJson.getRet())) {
//                        String plaintStr = AESUtil.Decrypt(resultJson.getData(), config.getAesKey(), config.getAesIv());// 对返回结果解密
//                        N0Response tokenBean = JSON.parseObject(plaintStr, N0Response.class);
//                        redisTools.setWithExpireTime("", tokenKey, tokenBean.getAccessToken(), tokenBean.getTokenAvailableTime());
//                        queryStatus.put(tokenKey, false);
//                        if (tokenLogService!=null) {
////                            tokenLogService.saveTokenLog(reqPara, resResult, 1, "success");
//                        }
//                        return tokenBean.getAccessToken();
//                    }
//                }
//                if (tokenLogService!=null) {
//                    tokenLogService.saveTokenLog(reqPara, resResult, 2, "请求token失败");
//                }
//                logger.error("请求token发生异常，请求参数[加密前]=" + data+",回调 queryToken"+ result);
//            } else {
//                return redisTools.get(tokenKey);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (tokenLogService!=null) {
//                tokenLogService.saveTokenLog(reqPara, resResult, 3, "请求token发生异常:"+e.getMessage());
//            }
//            logger.error("请求token发生异常");
//        }
//        return null;
//    }
//
//    public static  void resetToken(CompanyConfig config) {
//        String tokenKey = String.format(Locale.CHINA, "sodb_r_%s_%d", config.getOperatorID(), config.getServiceId());
//        queryStatus.remove(tokenKey);
//    }
//
//
//    public static void main(String... args) {
//        N0Request requestData = new N0Request();
//        requestData.setOperatorID("MA06ELWD6");
//        requestData.setOperatorSecret("1234567890abcdef");
//        String data = JSON.toJSONString(requestData);
//        logger.info("请求参数[加密前]=" + data);
//        // 生成加密签名参数
//        String data2 = Util.dealParam(data, "1234567890abcdef", "1234567890abcdef", "1234567890abcdef", "MA06ELWD6").replace("\r\n", "");
//        logger.info("请求参数[加密后]=" + data2);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data2);
//        String result = OkHttpUtils.post(requestBody, "https://test-chargehubws.czb365.com/evcs/v1.0/query_token");
//        logger.info("回调 queryToken=" + result);
//    }
//}
