package com.example.readingbetter;

import com.example.readingbetter.connect.GetConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetQuizList {
    String quiz;
    static String[] qQuizList;
    static String[] qEx1List;
    static String[] qEx2List;
    static String[] qEx3List;
    static String[] qEx4List;
    static String[] answerList;

    public void getQuizList(int no) {
        //  question.php를 Connect로 연결
        quiz ="http://220.67.115.225:8088/readingbetter/quizapp/solvequiz?no=" + no;
        GetConnect task = new GetConnect(quiz);
        task.start();

        try {
            task.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
        }

        String result = task.getResult();
        System.out.println("here");
        result="{'result':"+result+"}";
        System.out.println(result);

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();
            qQuizList = new String[ARRAY_LENGTH];
            qEx1List = new String[ARRAY_LENGTH];
            qEx2List = new String[ARRAY_LENGTH];
            qEx3List = new String[ARRAY_LENGTH];
            qEx4List = new String[ARRAY_LENGTH];
            answerList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);
                String qQuiz = jo.getString("quiz");
                String qEx1 = jo.getString("ex1");
                String qEx2 = jo.getString("ex2");
                String qEx3 = jo.getString("ex3");
                String qEx4 = jo.getString("ex4");
                String answer = jo.getString("answer");
                qQuizList[i] = qQuiz;
                qEx1List[i] = qEx1;
                qEx2List[i] = qEx2;
                qEx3List[i] = qEx3;
                qEx4List[i] = qEx4;
                answerList[i] = answer;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
