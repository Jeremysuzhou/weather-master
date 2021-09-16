package util;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtils {
    public static String doGet(String url) {
        // 输入流
        InputStream is = null;
        BufferedReader br = null;
        String result = null;

        HttpClient httpClient = new HttpClient();

        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(500);

        GetMethod getMethod = new GetMethod(url);
        /*
          设置超时时间两秒钟 可以重试 重试三次
         */
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 2000);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        try {
            //接口调用
            int httpCode = httpClient.executeMethod(getMethod);
            // 判断返回码 也就是200
            if (httpCode != HttpStatus.SC_OK) {
                return null;
            } else {
                is = getMethod.getResponseBodyAsStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sbf.append(line).append("\r\n");
                }

                result = sbf.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 释放连接
            getMethod.releaseConnection();
        }
        return result;
    }
}
