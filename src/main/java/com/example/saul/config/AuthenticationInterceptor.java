//package com.example.saul.config;
//
//import com.alibaba.fastjson.JSON;
//import com.example.saul.annotation.PassToken;
//import com.example.saul.annotation.UserLoginToken;
//import com.example.saul.utils.TokenUtils;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Auther: James
// * @Date: 2019/6/12 10:05
// * @Description:
// */
//public class AuthenticationInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
//        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
//        String uri = httpServletRequest.getRequestURI();//返回请求行中的资源名称
//        if (uri.indexOf("swagger") > -1 || uri.contains("druid") || uri.contains("api-docs")) {
//            return true;
//        }
//        // 如果不是映射到方法直接通过
//        if (!(object instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) object;
//        Method method = handlerMethod.getMethod();
//        //检查是否有passtoken注释，有则跳过认证
//        if (method.isAnnotationPresent(PassToken.class)) {
//            PassToken passToken = method.getAnnotation(PassToken.class);
//            if (passToken.required()) {
//                return true;
//            }
//        }
//        Map<String, String> map = new HashMap();
//        //检查有没有需要用户权限的注解
//        if (method.isAnnotationPresent(UserLoginToken.class)) {
//            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
//            if (userLoginToken.required()) {
//                // 执行认证
//                if (token == null) {
//                    map.put("retmsg", "无token，请重新登录");
//                    map.put("retcode", "401");
//                    response401(httpServletResponse, map);
//                    return false;
//                }
//                if (token != null) {
//                    Map<String, Object> validMap = TokenUtils.valid(token);
//                    int i = (int) validMap.get("Result");
//                    if (i == 0) {
//                        //System.out.println("token解析成功");
//                        return true;
//                    }
////                    else if (i == 2) {
////                        map.put("retmsg", "登录已超时，请重新登录");
////                        map.put("retcode", "401");
////                        response401(httpServletResponse, map);
////                        return false;
////                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest,
//                           HttpServletResponse httpServletResponse,
//                           Object o, ModelAndView modelAndView) throws Exception {
//        //logger.info("Interceptor cost=" + (System.nanoTime() - start));
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest,
//                                HttpServletResponse httpServletResponse,
//                                Object o, Exception e) throws Exception {
//    }
//
//    private void response401(HttpServletResponse response, Map<String, String> map) {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        try {
//            response.getWriter().print(JSON.toJSONString(map));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
