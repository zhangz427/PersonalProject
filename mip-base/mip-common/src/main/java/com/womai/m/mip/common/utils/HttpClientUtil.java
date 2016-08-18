package com.womai.m.mip.common.utils;



import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zheng.zhang on 2016/3/10.
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final int REQUEST_TIMEOUT = 10 * 1000;

    private static final int MAX_TOTAL = 400;

    private static final int MAX_CONNECTION_PER_ROUTE = 200;

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager ;

    static {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_CONNECTION_PER_ROUTE);
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(REQUEST_TIMEOUT).build();
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
    }


    public static String get(String url, Map<String, String> parameterMap) throws Exception{

        List<NameValuePair> parametersList = convertNameValuePairList(parameterMap);

        CloseableHttpClient httpclient = getConnection();
        HttpGet httpget = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            if (parametersList.size() != 0) {
                uriBuilder.addParameters(parametersList);
            }

            URI uri = uriBuilder.build();

            httpget = new HttpGet(uri);

            logger.debug("Executing request {}", httpget.getRequestLine());

            String responseBody = httpclient.execute(httpget, getStringResponseHandler());

            return responseBody;
        } finally {
            if (httpget != null) {
                httpget.releaseConnection();
            }
        }

    }

    public static String post(String url, Map<String, String> parameterMap) throws Exception{
        return post(url, parameterMap, null, -1);
    }

    public static String post(String url, Map<String, String> parameterMap, int timeout) throws Exception{
        return post(url, parameterMap, null, timeout);
    }

    public static String post(String url, Map<String, String> parameterMap, Map<String, String> headerMap, int timeout) throws Exception{

        List<NameValuePair> parametersList = convertNameValuePairList(parameterMap);

        CloseableHttpClient httpclient = getConnection();
        HttpPost httppost = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            URI uri = uriBuilder.build();

            httppost = new HttpPost(uri);

            if (timeout != -1) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
                httppost.setConfig(requestConfig);
            }

            if (parametersList.size() != 0) {
                UrlEncodedFormEntity entity =new UrlEncodedFormEntity(parametersList,"UTF-8");
                httppost.setEntity(entity);
            }

            if (headerMap != null && headerMap.size() > 0) {
                for (String headerName : headerMap.keySet()) {
                    String headerValue = headerMap.get(headerName);
                    if (StringUtils.isNotBlank(headerValue)) {
                        httppost.setHeader(headerName, headerValue);
                    }
                }
            }

            logger.debug("Executing request {}", httppost.getRequestLine());

            String responseBody = httpclient.execute(httppost, getStringResponseHandler());

            return responseBody;
        } finally {
            if (httppost != null) {
                httppost.releaseConnection();
            }
        }

    }

    private static CloseableHttpClient getConnection() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .build();

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).setDefaultRequestConfig(requestConfig).build();
        return httpclient;
    }

    private static List<NameValuePair> convertNameValuePairList(Map<String, String> parameterMap) {
        List<NameValuePair> parametersList = new ArrayList<NameValuePair>();
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                BasicNameValuePair pair = new BasicNameValuePair(key, parameterMap.get(key));
                parametersList.add(pair);
            }
        }
        return parametersList;
    }

    private static ResponseHandler<String> getStringResponseHandler() {
        return new ResponseHandler<String>() {
            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
    }

}
