package com.example.saul.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static Map<String, String> map = new ConcurrentHashMap<String, String>(3);


    /**
     * 方法用途: 接收HttpPost请求的json参数<br>
     * 实现步骤: <br>
     *
     * @param request
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("deprecation")
    public static String receivePost(HttpServletRequest request) throws IOException, UnsupportedEncodingException {

        // 读取请求内容
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // 将资料解码
        String reqBody = sb.toString();
        //URLDecoder.decode(reqBody, HTTP.UTF_8)
        return reqBody;
    }


    /**
     * 方法用途:获取随机token字符串 <br>
     *
     * @param length
     * @return
     * @author yxb
     * @version 1.0
     * @date 2018年1月26日上午11:07:32
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);//[0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 处理数据，对Data进行加密后签名 生成处理后的数据
     *
     * @param jsonParam
     * @param aesKey
     * @param aesIV
     * @param signKey
     * @return
     * @throws Exception
     */
    public static String dealParam(String jsonParam, String aesKey,
                                   String aesIV, String signKey, String OurOperatorID) {
        HashMap<String, String> paramObj = new HashMap<>();
        paramObj.put("OperatorID", OurOperatorID);
        String Data = QEncodeUtil.encrypt(jsonParam,"50a61b93919c9604","7c8ac6861661d584").replace("\n","").replace("\r","");

        paramObj.put("Data", Data);
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        paramObj.put("TimeStamp", dateStr);
        String seq = getSeq(dateStr);
        paramObj.put("Seq", seq);
        String signMsg = OurOperatorID + Data + dateStr + seq;
        try {
            paramObj.put("Sig", HMacMD5Util.getHmacMd5Str(signKey, signMsg));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSON.toJSONString(paramObj);

    }

    /**
     * 处理Seq
     *
     * @param timeStamp
     * @return
     */
    public static String getSeq(String timeStamp) {

        if (map.containsKey(timeStamp)) {
            Integer count = Integer.parseInt(map.get(timeStamp)) + 1;
            switch (count.toString().length()) {
                case 1:
                    map.put(timeStamp, "000" + count);
                    break;
                case 2:
                    map.put(timeStamp, "00" + count);
                    break;
                case 3:
                    map.put(timeStamp, "0" + count);
                    break;
                case 4:
                    map.put(timeStamp, "" + count);
                    break;
                default:
                    map.put(timeStamp, "" + count);
                    break;
            }
            return map.get(timeStamp);
        } else {
            map.put(timeStamp, "0001");
            return "0001";
        }
    }
}
