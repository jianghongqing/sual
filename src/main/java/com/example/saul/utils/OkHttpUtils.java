package com.example.saul.utils;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import okhttp3.FormBody.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

/**
 *
 */
public class OkHttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 不带参数的get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        String returnBody = "";
//        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                returnBody = response.body().string();
            }
        } catch (Exception e) {
            logger.error(" get 请求失败！", e);
        }
        return returnBody;
    }

    public static int postBodyAsync(String url, String authKey, String authValue, String json, OkHttpCallBack callback) throws Exception {
        logger.info("Token = " + authValue);
        int code = 0;
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader(authKey, "Bearer " + authValue)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        try {
            if (callback == null) {
                Response response = call.execute();
                return response.code();
            } else {
                // 值传到callback的方法里去
                call.enqueue(callback);
            }
        } catch (Exception e) {
            logger.error(e.toString(),e.getMessage());
        }
        return code;
    }

    public static void postBodyAsync(String url, String json, OkHttpCallBack callback) {
//        OkHttpClient client = new OkHttpClient();
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(type, json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        try {
            if (callback == null) {
                call.execute();
            } else {
                // 值传到callback的方法里去
                call.enqueue(callback);
            }
        } catch (Exception e) {
            logger.error(" postBodyAsync 请求失败！", e);
        }
    }

    public static void postBodyAsync(String url, HashMap<String, Object> args, OkHttpCallBack callback) throws
            Exception {
        postBodyAsync(url, null, args, callback);
    }

    public static void postBodyAsync(String
                                             url, HashMap<String, Object> headers, HashMap<String, Object> args, OkHttpCallBack callback) throws
            Exception {
//        OkHttpClient client = new OkHttpClient();
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        String json = JSON.toJSONString(args);
        RequestBody requestBody = RequestBody.create(type, json);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, valueOf(headers.get(key)));
            }
        }
        Request request = builder.build();
        Call call = client.newCall(request);
//        try {
        if (callback == null) {
            call.execute();
        } else {
            // 值传到callback的方法里去
            call.enqueue(callback);
        }

    }

    public static void postAsync(String url, HashMap<String, Object> args, Callback callback) {

        try {
            Builder builder = new Builder();
            if (args != null && args.size() > 0) {
                for (String key : args.keySet()) {
                    builder.add(key, valueOf(args.get(key)));
                }
            }
            Request request = new Request.Builder().url(url)
                    .header("User-Agent", "OkHttp/Android")
                    .addHeader("Content-Type", "application/json;charset=UTF-8;")
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("Accept", "application/json;charset=UTF-8;")
                    .post(builder.build()).build();

            Call call = client.newCall(request);
            if (callback == null) {
                call.execute();
            } else {
                // 值传到callback的方法里去
                call.enqueue(callback);
            }
        } catch (Exception e) {
            logger.error(" postAsync 请求失败！", e);
        }
    }

    /**
     * 烤鱼测试接口
     *
     * @param url
     * @param callback
     */
    public static void postAsync(String url, Callback callback) {

        try {

            Request request = new Request.Builder().url(url)
                    .header("User-Agent", "OkHttp/Android")
                    .addHeader("Content-Type", "application/json;charset=UTF-8;")
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("Accept", "application/json;charset=UTF-8;")
                    .get()
                    .build();

            Call call = client.newCall(request);
            if (callback == null) {
                call.execute();
            } else {
                // 值传到callback的方法里去
                call.enqueue(callback);
            }
        } catch (Exception e) {
            logger.error(" postAsync 请求失败！", e);
        }
    }

    /*
     * post异步请求
     */
    public static void postAsync(String url, HashMap<String, Object> args, OkHttpCallBack callback) {
//        OkHttpClient client = new OkHttpClient();
        try {
            Builder builder = new Builder();
            if (args != null && args.size() > 0) {
                for (String key : args.keySet()) {
                    builder.add(key, valueOf(args.get(key)));
                }
            }
            Request request = new Request.Builder().url(url)
                    .header("User-Agent", "OkHttp/Android")
                    .addHeader("Content-Type", "application/json;charset=UTF-8;")
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("Accept", "application/json;charset=UTF-8;")
                    .post(builder.build()).build();

            Call call = client.newCall(request);
            if (callback == null) {
                call.execute();
            } else {
                // 值传到callback的方法里去
                call.enqueue(callback);
            }
        } catch (Exception e) {
            logger.error(" postAsync 请求失败！", e);
        }
    }

    public static void postImageAsync(String url, HashMap<String, Object> args, OkHttpCallBack callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .build();
        try {
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            if (args != null && args.size() > 0) {
                for (String key : args.keySet()) {
                    if ("file".equals(key)) {
                        File file = new File(valueOf(args.get(key)));
                        RequestBody rb = RequestBody.create(MEDIA_TYPE_PNG, file);
                        builder.addFormDataPart(key, file.getName(), rb);
                    } else {
                        builder.addFormDataPart(key, valueOf(args.get(key)));
                    }
                }
            }
            Request request = new Request.Builder().url(url).post(builder.build()).build();

            Call call = client.newCall(request);
            if (callback == null) {
                call.execute();
            } else {
                call.enqueue(callback);
            }

        } catch (Exception e) {
            logger.error(" postImageAsync 请求失败！", e);
        }

    }

    /**
     * 带参数的图片上传
     *
     * @param url
     * @param args
     * @param callback
     */
    public static void postVideoAsync(String url, String videoPath, HashMap<String, Object> args, Callback
            callback) {
//        OkHttpClient client = new OkHttpClient();
        try {
            if (videoPath != null) {
                File file = new File(videoPath);
                if (file.exists()) {
                    MediaType MEDIA_TYPE = MediaType.parse(guessMimeType(videoPath));

                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    RequestBody rb = RequestBody.create(MEDIA_TYPE, videoPath);
                    builder.addFormDataPart("content", file.getName(), rb);

                    if (args != null && args.size() > 0) {
                        for (String key : args.keySet()) {

                            builder.addFormDataPart(key, valueOf(args.get(key)));
                        }
                    }
                    Request request = new Request.Builder().url(url).post(builder.build()).build();
                    Call call = client.newCall(request);
                    if (callback == null) {
                        call.execute();
                    } else {
                        call.enqueue(callback);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(" postVideoAsync 请求失败！", e);
        }

    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static void postMutilImageAsync(String
                                                   url, List<String> files, HashMap<String, Object> args, OkHttpCallBack callback) {
        try {
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            if (args != null && args.size() > 0) {
                for (String key : args.keySet()) {

                    builder.addFormDataPart(key, valueOf(args.get(key)));
                }
            }
            if (files != null && files.size() > 0) {
                for (String filePath : files) {
                    File file = new File(filePath);
                    RequestBody rb = RequestBody.create(MEDIA_TYPE_PNG, file);
                    builder.addFormDataPart("file", file.getName(), rb);

                }
            }
            Request request = new Request.Builder().url(url).post(builder.build()).build();

            Call call = client.newCall(request);
            if (callback == null) {
                call.execute();
            } else {
                call.enqueue(callback);
            }

        } catch (Exception e) {
            logger.error(" postMutilImageAsync 请求失败！", e);

        }

    }

    /**
     * post带参数请求
     *
     * @param requestBody
     * @param url
     * @return
     */
    public static String post(RequestBody requestBody, String url) {
        String returnBody = "";
//        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                returnBody = response.body().string();
            }
        } catch (Exception e) {
            logger.error(" post 请求失败！", e);
        }
        return returnBody;
    }

    /**
     * 带参数的图片上传
     *
     * @param requestBody
     * @return
     */
    public static String postImage(RequestBody requestBody, String url) {
        String returnBody = "";

        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                returnBody = response.body().string();
            }
        } catch (Exception e) {
            logger.error(" postImage 请求失败！", e);
        }

        return returnBody;
    }

    public static void post_file(final String url, final Map<String, Object> map, File file, Callback callback) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("content", filename, body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);

    }
}

