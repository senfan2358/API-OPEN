package com.senfan.senfanapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.senfan.senfanapiclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.senfan.senfanapiclientsdk.utils.SignUtils.getSign;

@Slf4j
public class SenfanAPIClient {
    private String accessKey;
    private String secretKey;
    private static String GATEWAY_HOST = "http://localhost:8090";

    public SenfanAPIClient(String accessKey, String secretKey) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    public void setGateway_Host(String gatewayHost) {
        GATEWAY_HOST = gatewayHost;
    }


    private Map<String, String> getHeaderMap(long id,String body, String url, String path, String method) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
        // hashMap.put("secretKey",secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        hashMap.put("id", String.valueOf(id));
        hashMap.put("url", url);
        hashMap.put("path", path);
        hashMap.put("method", method);
        // 处理参数中文问题
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        hashMap.put("body", body);
        // 一定时间内有效
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 签名
        hashMap.put("sign", getSign(body, secretKey));
        return hashMap;
    }


    public String invokeInterface(long id, String params, String url, String method, String path) {
        String result;
        log.info("SDK正在转发至GATEWAY_HOST:{}", GATEWAY_HOST);
        try(
                HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + path)
                        // 处理中文编码
                        .header("Accept-Charset", CharsetUtil.UTF_8)
                        .addHeaders(getHeaderMap(id,params, method,path,url))
                        .body(params)
                        .execute())
        {
            String body = httpResponse.body();
            /*
            // 可以在SDK处理接口404的情况
            if(httpResponse.getStatus()==404){
                body = String.format("{\"code\": %d,\"msg\":\"%s\",\"data\":\"%s\"}",
                        httpResponse.getStatus(), "接口请求路径不存在", "null");
                log.info("响应结果：" + body);
            }
            */
            // 将返回的JSON结果格式化，其实就是加换行符
            result=JSONUtil.formatJsonStr(body);
        }
        log.info("SDK调用接口完成，响应数据：{}",result);
        return result;
    }
}