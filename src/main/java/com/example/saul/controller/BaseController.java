package com.example.saul.controller;

import com.example.saul.exception.MalciousException;
import com.example.saul.exception.PermissionException;
import com.example.saul.utils.AjaxResult;
import com.example.saul.utils.IPUtil;
import com.example.saul.utils.PageAjax;
import com.example.saul.utils.ParamData;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 基础Controller
 *
 * @author Cg
 */
@Controller
public class BaseController {

    protected static final MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");
    protected static OkHttpClient client = new OkHttpClient();
    @Value("test.userToken")
    protected String userToken;
    /**
     * 日志
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 得到request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected static String post(String url, String json) {


        try {
            RequestBody body = RequestBody.create(JSONMediaType, json);
            Request request = new Request.Builder().url(url).post(body).build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (Exception e) {
            return "";
        }
    }

    protected static String get(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String checkParam(ParamData params, String[] args) {
        String result = null;
        if (null != args && args.length > 0) {
            int size = args.length;
            for (int i = 0; i < size; i++) {
                String param = args[i];
                if (!params.containsKey(param)) {// 检验参数是否传递
                    result = "缺少参数:" + param;
                    break;
                }
                if (null == params.get(param)) {// 检验参数是否为空
                    result = "参数" + param + "不能为空";
                    break;
                }
            }
        }
        return result;
    }

    protected AjaxResult returnSuccessData() {
        return AjaxResult.returnSuccessData();
    }

    protected AjaxResult returnSuccessData(Object data) {
        return AjaxResult.returnSuccessData(data);
    }

    protected <T> PageAjax<T> returnSuccessDataByPage(List<T> data) {
        return new PageAjax<T>(data);
    }

    protected AjaxResult returnFailData(String errMsg) {
        return AjaxResult.returnFailData(errMsg);
    }

    protected AjaxResult checkUpdateOrAddResult(int changedRowCount) {
        if (changedRowCount == 1) {
            return returnSuccessData();
        } else {
            return returnFailData("修改失败");
        }
    }

    /**
     * ajax登录异常处理
     **/
    @ExceptionHandler({AjaxLoginException.class})
    @ResponseBody
    public AjaxResult ajaxLoginExceptionHandler(AjaxLoginException e) {
        logger.error("登录请求发生异常:", e);
        return returnFailData("登录请求发生异常:" + e.getMessage());
    }

    /**
     * 普通登录异常处理
     **/
    @ExceptionHandler({LoginException.class})
    public String loginExceptionHandler(LoginException e, HttpServletRequest request) {
        logger.error("登录请求发生异常:", e);
        request.setAttribute("err", e.getMessage());
        return "forward:/";
    }

    /**
     * 普通权限异常处理
     **/
    @ExceptionHandler({PermissionException.class})
    public String permissonExceptionHandler(PermissionException e) {
        return "common/no_permisson";
    }

    /**
     * ajax权限异常处理
     **/
    @ExceptionHandler({AjaxPermissionException.class})
    @ResponseBody
    public AjaxResult ajaxPermissionExceptionHandler(AjaxPermissionException e) {
        return returnFailData("权限异常:" + e.getMessage());
    }

    /**
     * 频繁请求异常处理
     **/
    @ExceptionHandler({MalciousException.class})
    public String malExceptionHandler(MalciousException e) {
        return "common/mal_request";
    }

    /**
     * 公共异常处理
     **/
    @ExceptionHandler({Exception.class})
    public Object exceptionHandler(Exception e, HttpServletRequest request) {
        ParamData params = new ParamData();
        logger.info("");
        StringBuilder sb = new StringBuilder(params.getString("loginIp")).append(request.getRequestURI()).append("请求发生异常:")

                .append(request.getServletPath()).append(":").append(params);
        logger.error(sb.toString(), e);
        return returnFailData(getMessage(e.getMessage()));
    }

    String getMessage(String message) {
        if (message.contains("PRIMARY")) return "主键冲突！";
        if (message.length() > 30) return "服务器内部错误，请联系系统管理员！";
        return message;
    }

    public void logBefore(String desc) {
        HttpServletRequest request = getRequest();
        logger.error("");
        StringBuilder sb = new StringBuilder(IPUtil.getIpAdd(request)).append(desc).append(":").append(request.getServletPath());
        logger.error(sb.toString());
    }

    /**
     * 生成N位纯数字验证码
     *
     * @return
     */
    protected String getVerificationCode(int n) {
        final Random random = new Random();
        String verificationCode = "";
        for (int i = 0; i < n; i++) {
            verificationCode += random.nextInt(10);
        }
        return verificationCode;
    }

    /**
     * 生成32位UUID
     *
     * @return
     */
    protected String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

//    public User DecryptUser(String token) {
//        User user = JWTUtil.unsign(token, User.class);
//        return user;
//    }
}
