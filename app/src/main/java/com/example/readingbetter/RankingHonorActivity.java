package com.example.readingbetter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.readingbetter.connect.GetConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RankingHonorActivity extends Activity {
    public String honorRanking;   // 한 달 랭킹을 불러오는 서버 링크를 저장할 String 변수
    public String myHonorRanking;   // 나의 한 달 랭킹을 불러오는 서버 링크를 저장할 String 변수

    // TextView 주소값 배열
    int[] rank = {R.id.rank1, R.id.rank2, R.id.rank3, R.id.rank4, R.id.rank5};  //순위
    int[] id = {R.id.id1, R.id.id2, R.id.id3, R.id.id4, R.id.id5};  // ID
    int[] totalScore = {R.id.score1, R.id.score2, R.id.score3, R.id.score4, R.id.score5}; //점수

    // TextView 선언 (전체 상위 5위)
    TextView[] rankArray = new TextView[5];
    TextView[] idArray = new TextView[5];
    TextView[] scoreArray = new TextView[5];

    // TextView 선언 (본인 순위)
    TextView tMyRank, tMyID, tMyScore;

    // mysql에 저장된 값을 저장하는 문자열
    String[] rankList, idList, scoreList;

    // 개인 랭킹에 전달할 유저 번호
    Long userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankinghonor_page);

        userNo = LoginActivity.setting.getLong("no",0);
        System.out.println("no = "+userNo);

        // ranking.php를 GetConnect로 연결
        honorRanking = "http://220.67.115.225:8088/readingbetter/rankapp/honor";
        myHonorRanking = "http://220.67.115.225:8088/readingbetter/rankapp/myhonor?memberNo="+userNo;

        GetConnect totalTask = new GetConnect(honorRanking);
        GetConnect myTotalTask = new GetConnect(myHonorRanking);

        totalTask.start();
        myTotalTask.start();


        try {
            totalTask.join();
            myTotalTask.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String totalResult = totalTask.getResult();
        totalResult="{'result':"+totalResult+"}";
        System.out.println(totalResult);
        String myTotalResult = myTotalTask.getResult();
        myTotalResult="{'result':["+myTotalResult+"]}";

        // JSON 파싱 (전체 랭킹 목록 출력)
        try {
            JSONObject root = new JSONObject(totalResult);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();

            rankList = new String[ARRAY_LENGTH];
            idList = new String[ARRAY_LENGTH];
            scoreList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);

                String mem_rank = jo.getString("rank");
                String mem_ID = jo.getString("id");
                String mem_score = jo.getString("totalScore");

                // String 배열에 저장
                rankList[i] = mem_rank;
                idList[i] = mem_ID;
                scoreList[i] = mem_score;

                // TextView 주소 값(ID) 지정
                rankArray[i] = (TextView) findViewById(rank[i]);
                idArray[i] = (TextView) findViewById(id[i]);
                scoreArray[i] = (TextView) findViewById(totalScore[i]);

                // TextView 텍스트 지정
                rankArray[i].setText(rankList[i]);
                idArray[i].setText(idList[i]);
                scoreArray[i].setText(scoreList[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Json 파싱 (개인 랭킹 출력)
        try {
            JSONObject root = new JSONObject(myTotalResult);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();

            rankList = new String[ARRAY_LENGTH];
            idList = new String[ARRAY_LENGTH];
            scoreList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);

                String mem_rank = jo.getString("rank");
                String mem_ID = jo.getString("id");
                String mem_score = jo.getString("myTotalScore");

                // String 배열에 저장
                rankList[i] = mem_rank;
                idList[i] = mem_ID;
                scoreList[i] = mem_score;

                // 개인 랭킹 출력할 텍스트뷰 주소 값 지정
                tMyRank = (TextView) findViewById(R.id.my_rank);
                tMyID = (TextView) findViewById(R.id.my_id);
                tMyScore = (TextView) findViewById(R.id.my_score);

                // TextView 텍스트 지정
                tMyRank.setText(rankList[i]);
                tMyID.setText(idList[i]);
                tMyScore.setText(scoreList[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}