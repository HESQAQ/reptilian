package com.zpy.spider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author: liaogk
 * @Date: 2018-08-01 13:18
 * @description:
 */
public interface HttpClient {

    HttpResponse doRequest(HttpMethod method, String url, Map<String, String> userHeaders, InputStream data,Map<String,String> cookies)throws IOException;

    byte[] doGet(String url) throws IOException;

    HttpResponse doGet(String url, Map<String, String> headers) throws IOException;
    HttpResponse doGet2(String url, Map<String, String> headers,Map<String,String> cookies) throws IOException;
    HttpResponse dopost(String url, Map<String, String> headers,InputStream data,Map<String,String> cookies) throws IOException;

}
