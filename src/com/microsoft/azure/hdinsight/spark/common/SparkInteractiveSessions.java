package com.microsoft.azure.hdinsight.spark.common;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;

/**
 * Created by guizha on 8/27/2015.
 */
public class SparkInteractiveSessions {

    // Singleton Instance
    private static SparkInteractiveSessions instance = null;

    public static SparkInteractiveSessions getInstance() {
        if(instance == null){
            synchronized (SparkInteractiveSessions.class){
                if(instance == null){
                    instance = new SparkInteractiveSessions();
                }
            }
        }

        return instance;
    }

    /**
     * get all sessions
     * @param connectUrl : eg http://localhost:8998/sessions
     * @return
     * @throws IOException
     */
    public String getAllSessions(String connectUrl) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(connectUrl);
        httpGet.addHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }

    /**
     * create new session
     * @param connectUrl : eg http://localhost:8998/sessions
     * @param kind : spark or pyspark
     * @return json string result
     * @throws IOException
     */
    public String createNewSession(String connectUrl, String kind) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(connectUrl);
        httpPost.addHeader("Content-Type", "application/json");
        String jsonString = "{\"kind\" : \"" + kind + "\"}";
        StringEntity postingString =new StringEntity(jsonString);
        httpPost.setEntity(postingString);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }

    /**
     * @param connectUrl : eg http://localhost:8998/sessions
     * @param sessionId : session Id
     * @return
     * @throws IOException
     */
    public String getSessionState(String connectUrl, String sessionId) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(connectUrl + "/" + sessionId);
        httpGet.addHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }

    /**
     * @param connectUrl : eg http://localhost:8998/sessions
     * @param sessionId : session id
     * @param code scala or python code segment
     * @return
     * @throws IOException
     */
    public String executeInSession(String connectUrl, String sessionId, String code)throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(connectUrl + "/" + sessionId + "/statements");
        httpPost.addHeader("Content-Type", "application/json");
        String jsonString = "{\"code\" : \"" + code + "\"}";
        StringEntity postingString =new StringEntity(jsonString);
        httpPost.setEntity(postingString);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }

    /**
     * get job execution status
     * @param connectUrl eg http://localhost:8998/sessions
     * @param sessionId session id
     * @param statementId statement id
     * @return
     * @throws IOException
     */
    public String getExecutionState(String connectUrl, String sessionId, String statementId)throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(connectUrl + "/" + sessionId + "/statements/" + statementId);
        httpGet.addHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }

    /**
     * kill session
     * @param connectUrl : eg http://localhost:8998/sessions
     * @param sessionId : session id
     * @return
     * @throws IOException
     */
    public String killSession(String connectUrl, String sessionId) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(connectUrl +  "/" + sessionId);
        httpDelete.addHeader("Content-Type", "application/json");

        CloseableHttpResponse response = httpclient.execute(httpDelete);
        try {
            HttpEntity entity = response.getEntity();
            return SparkHelper.getResultFromInputStream(entity.getContent());
        }finally {
            response.close();
        }
    }
}
