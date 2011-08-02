package com.upopple.android.seethatmovie.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class RestJsonClient {

    public static Reader connect(String url) throws Exception
    {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 

        // Execute the request
        HttpResponse response;
        
        Reader reader = null;

        try {
            response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();

                reader = new InputStreamReader(instream);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw(e);
        } catch(Exception e){
        	throw(e);
        }

        return reader;
    }

}