package com.zpy.spider;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author: liaog
 * @Date: 2018-08-01 13:24
 * @description:
 */
public class HttpResponse {

    private int responseCode;
    private Map<String, List<String>> headers;
    private byte[] responseData;
    private String defaultCharset;
    private Map<String ,String> cookies;

    public HttpResponse(String defaultCharset, int responseCode, Map<String, List<String>> headers, byte[] responseData,Map<String ,String> cookies) {
        this.defaultCharset = defaultCharset;
        this.responseCode = responseCode;
        this.headers = headers;
        this.responseData = responseData;
        this.cookies = cookies;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public String getResponseString() {
        try {
            return new String(responseData, getResponseCharset());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getHeaders(String name) {
        return headers.get(name);
    }

    public String getHeader(String name) {
        List<String> theHeaders = getHeaders(name);
        if (theHeaders == null || theHeaders.isEmpty()) {
            return null;
        }
        return theHeaders.get(0);
    }


    public String getResponseCharset() {
        String contentType = getHeader("Content-Type");
        if (contentType == null || contentType.length() == 0) {
            return defaultCharset;
        }
        String[] parts = contentType.split(";");
        for (String part : parts) {
            part = part.trim();
            String[] kvParts = part.split("=");
            if (kvParts.length < 2) {
                continue;
            }
            String key = kvParts[0].trim();
            String value = kvParts[1].trim();
            if (key.equals("charset")) {
                return value;
            }
        }
        return defaultCharset;
    }

    public Map<String ,String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String ,String> cookies) {
        this.cookies = cookies;
    }
}
