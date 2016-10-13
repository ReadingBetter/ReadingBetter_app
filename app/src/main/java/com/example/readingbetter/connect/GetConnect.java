package com.example.readingbetter.connect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetConnect extends Thread{
    private String result;
    private String URL;

    public GetConnect(String url){
        URL = url;
    }

    @Override
    public void run() {
        System.out.println(URL);
        final String output = request(URL);
        System.out.println(output);
        result = output;
    }

    public String getResult(){
        return result;
    }

    private String request(String urlStr){
        System.out.println("requset진입");
        StringBuilder output = new StringBuilder();
        try {
            java.net.URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            System.out.println("연결시작");
            System.out.println(conn);
            if (conn != null){

                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
              /*  conn.setDoInput(false);
                conn.setDoOutput(true);*/

                int resCode = conn.getResponseCode();
                System.out.println("resCode:"+resCode);
                if (resCode == HttpURLConnection.HTTP_OK){
                    System.out.println("성공이면");
                    System.out.println(resCode);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while (true){
                        line = reader.readLine();
                        if (line == null){
                            break;
                        }
                        output.append(line + "\n");
                        System.out.println("루프");
                    }
                    reader.close();
                    conn.disconnect();
                }
            }
        } catch (Exception ex){
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        }
        return output.toString();
    }
}