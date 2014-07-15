package com.xjy.mua.util;

import android.util.Log;
import com.xjy.mua.exception.NetAccessException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ���IHttpRest�ӿ�ʵ�֣�����HttpClient
 */
public class HttpRestAchieve implements IHttpRest {
    private final String TAG = this.getClass().getSimpleName();
    private HttpClient httpClient;

    /**
     * ���ӳ�ʱʱ��
     */
    private static final int CONNECTION_TIMEOUT = 10000; /* 10 seconds */

    /**
     * �ȴ�����������ʱ��
     */
    private static final int SOCKET_TIMEOUT = 10000; /* 10 seconds */

    /**
     * ManagedClientConnection������ʱ��ʱ��
     */
    private static final long MCC_TIMEOUT = 10000; /* 10 seconds */

    private static final int MAX_CONNECTIONS_PER_ROUTE = 400;

    public HttpRestAchieve() {
        Log.d(TAG, "The HttpRestAchieve thread id = " + Thread.currentThread().getId() + "\n");
        getHttpClient();
    }

    /**
     * ���ó�ʱʱ��
     * @param params ���ò���
     */
    private static void setTimeouts(HttpParams params) {
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                CONNECTION_TIMEOUT);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
        params.setLongParameter(ConnManagerPNames.TIMEOUT, MCC_TIMEOUT);
        params.setIntParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,MAX_CONNECTIONS_PER_ROUTE);
    }

    /**
     * ��ʼ��httpclient�������
     */
    public void getHttpClient() {
        // create HttpClient instance
        httpClient = CustomHttpClient.getHttpClient();
    }

    /**
     * ����Ӧ��ַ��ȡ����
     * @param url ��ȡ�ĵ�ַ
     * @param paramsMap �����б�
     * @return ���ص�����
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity getRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        // add get request parameters
        StringBuilder paramStr = new StringBuilder();

        for (String key : paramsMap.keySet()) {
            String value = paramsMap.get(key);
            paramStr.append("&");
            paramStr.append(key);
            paramStr.append("=");
            paramStr.append(value);
        }
        String prams = paramStr.toString();

        if (!prams.equals("")) {
            prams = prams.replaceFirst("&", "?");
            url += prams;
        }

        HttpRequestBase getRequest = new HttpGet(url);
        setTimeouts(getRequest.getParams());
        HttpResponse response;
        NetBackDataEntity netBackData;

        try {
            response = httpClient.execute(getRequest);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData = new NetBackDataEntity();
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("call get request function exception");
            }
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }

            throw new NetAccessException("call get request function exception" + e.getMessage());
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("call get request function exception" + e.getMessage());
        }
        return netBackData;
    }

    /**
     * �������ݵ�ָ����ַ
     * @param url ���͵ĵ�ַ
     * @param paramsMap ���͵Ĳ���
     * @return ���ص�����
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity postRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        if (paramsMap != null) {
            // add parameters
            for (String key : paramsMap.keySet()) {
                param.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }

        }

        Log.d(TAG, url);

        HttpPost request = new HttpPost(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData;
        HttpResponse response;
        HttpEntity httpEntity;
        try {
            HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
            request.setEntity(entity);
            response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                httpEntity = response.getEntity();
                netBackData.setData(EntityUtils.toString(httpEntity, "UTF-8"));
            } else {
                throw new NetAccessException("invoke post request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        }
        return netBackData;
    }

    /**
     * �������ݵ�ָ����ַ
     * @param url ���͵ĵ�ַ
     * @param paramsMap ���͵Ĳ���
     * @return ���ص�����
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity putRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        // add parameters
        for (String key : paramsMap.keySet()) {
            param.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }

        HttpPut request = new HttpPut(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData;
        try {
            HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("invoke delete request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        }
        return netBackData;
    }

    /**
     * ɾ�����ݵ�ָ����ַ
     * @param url ɾ���ĵ�ַ
     * @return ���ص�����
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity deleteRequestData(String url) throws NetAccessException {

        HttpDelete request = new HttpDelete(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData;
        try {
            HttpResponse response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("invoke delete request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        }

        return netBackData;
    }
}
