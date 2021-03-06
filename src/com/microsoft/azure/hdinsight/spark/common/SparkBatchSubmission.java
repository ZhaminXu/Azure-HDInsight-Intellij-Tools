package com.microsoft.azure.hdinsight.spark.common;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * Created by guizha on 8/27/2015.
 */
public class SparkBatchSubmission {
    // Singleton Instance
    private static SparkBatchSubmission instance = null;

    public static SparkBatchSubmission getInstance() {
        if(instance == null){
            synchronized (SparkBatchSubmission.class){
                if(instance == null){
                    instance = new SparkBatchSubmission();
                }
            }
        }

        return instance;
    }

    private CredentialsProvider credentialsProvider =  new BasicCredentialsProvider();

    /**
     * Set http request credential using username and password
     * @param username : username
     * @param password : password
     */
    public void setCredentialsProvider(String username, String password){
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    }

    /**
     * get all batches spark jobs
     * @param connectUrl : eg http://localhost:8998/batches
     * @return response result
     * @throws IOException
     */
    public String getAllBatchesSparkJobs(String connectUrl)throws IOException{
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpGet httpGet = new HttpGet(connectUrl);
        httpGet.addHeader("Content-Type", "application/json");

        try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
            return SparkHelper.getResultFromHttpResponse(response);
        }
    }

    /**
     * create batch spark job
     * @param connectUrl : eg http://localhost:8998/batches
     * @param submissonParameter : spark submission parameter
     * @return response result
     */
    public String createBatchSparkJob(String connectUrl,SparkSubmissionParameter submissonParameter)throws IOException{
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpPost httpPost = new HttpPost(connectUrl);
        httpPost.addHeader("Content-Type", "application/json");
        
        String jsonString = new Gson().toJson(submissonParameter);
        StringEntity postingString =new StringEntity(jsonString);
        httpPost.setEntity(postingString);
        try(CloseableHttpResponse response = httpclient.execute(httpPost)) {
            return SparkHelper.getResultFromHttpResponse(response);
        }
    }

    /**
     * get batch spark job status
     * @param connectUrl : eg http://localhost:8998/batches
     * @param batchId : batch Id
     * @return response result
     * @throws IOException
     */
    public String getBatchSparkJobStatus(String connectUrl, String batchId)throws IOException{
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpGet httpGet = new HttpGet(connectUrl + "/" + batchId);
        httpGet.addHeader("Content-Type", "application/json");

        try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
            return SparkHelper.getResultFromHttpResponse(response);
        }
    }

    /**
     * kill batch job
     * @param connectUrl : eg http://localhost:8998/batches
     * @param batchId : batch Id
     * @return response result
     * @throws IOException
     */
    public String killBatchJob(String connectUrl, String batchId)throws IOException {
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpDelete httpDelete = new HttpDelete(connectUrl +  "/" + batchId);
        httpDelete.addHeader("Content-Type", "application/json");

        try(CloseableHttpResponse response = httpclient.execute(httpDelete)) {
            return SparkHelper.getResultFromHttpResponse(response);
        }
    }

    /**
     * get batch job full log
     * @param connectUrl : eg http://localhost:8998/batches
     * @param batchId : batch Id
     * @return response result
     * @throws IOException
     */
    public String getBatchJobFullLog(String connectUrl, String batchId)throws IOException{
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        HttpGet httpGet = new HttpGet(connectUrl + "/" + batchId + "/log?from=0&size=" + Integer.MAX_VALUE);
        httpGet.addHeader("Content-Type", "application/json");

        try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
            return SparkHelper.getResultFromHttpResponse(response);
        }
    }
}
