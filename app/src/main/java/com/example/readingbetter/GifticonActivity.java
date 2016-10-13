package com.example.readingbetter;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.readingbetter.connect.GetConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Gifticon {
    private Long no;
    private String title;
    private String cover;
    private String getDate;
    private Long memberNo;

    public Gifticon(Long no, String title, String cover, String getDate, Long memberNo) {
        this.no = no;
        this.title = title;
        this.cover = cover;
        this.getDate = getDate;
        this.memberNo = memberNo;
    }

    public Long getNo() {
        return this.no;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCover() {
        return this.cover;
    }

    public String getGetDate() {
        return this.getDate;
    }

    public Long getMemberNo() {
        return this.memberNo;
    }
}

public class GifticonActivity extends Activity {
    public String gifticonInfo;
    ListView gifticonList;
    Button btnPrev;

    private Long loginNo;

    Long[] noList;
    String[] titleList;
    String[] coverList;
    String[] getDateList;
    Long[] memberNoList;

    GifticonAdapter adapter;
    ArrayList<Gifticon> arraylist = new ArrayList<Gifticon>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gifticon_page);

        gifticonList = (ListView) findViewById(R.id.gifticonList);
        btnPrev = (Button) findViewById(R.id.btnPrev);

        loginNo = LoginActivity.setting.getLong("no", 0);

        gifticonInfo = "http://220.67.115.226:8088/readingbetter/mypageapp/list?memberNo=" + loginNo;
        GetConnect task = new GetConnect(gifticonInfo);
        task.start();

        try {
            task.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
        }

        String result = task.getResult();
        result = "{'result':" + result + "}";

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();

            noList = new Long[ARRAY_LENGTH];
            titleList = new String[ARRAY_LENGTH];
            coverList = new String[ARRAY_LENGTH];
            getDateList = new String[ARRAY_LENGTH];
            memberNoList = new Long[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);
                Long no = jo.getLong("no");
                String title = jo.getString("title");
                String cover = jo.getString("cover");
                String getDate = jo.getString("getDate");
                Long memberNo = jo.getLong("memberNo");

                noList[i] = no;
                titleList[i] = title;
                coverList[i] = cover;
                getDateList[i] = getDate;
                memberNoList[i] = memberNo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < noList.length; i++) {
            Gifticon gc = new Gifticon(noList[i], titleList[i], coverList[i], getDateList[i], memberNoList[i]);
            arraylist.add(gc);
        }

        adapter = new GifticonAdapter(this, arraylist);
        gifticonList.setAdapter(adapter);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}