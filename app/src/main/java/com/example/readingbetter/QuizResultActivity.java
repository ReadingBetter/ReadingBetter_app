package com.example.readingbetter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readingbetter.connect.GetConnect;
import com.example.readingbetter.connect.PutConnect;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// 퀴즈 결과 화면
public class QuizResultActivity extends Activity {
    private Long loginNo;
    private String loginId;
    private int per1;
    private int scores, point;
    public String url;
    public String certification;
    String[] coverList, bonusList, titleList;
    PutConnect task_insert = new PutConnect();
    PutConnect task_insert2 = new PutConnect();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizresult_page);  // Layout quizresult_page의 자바 파일

        // Layout widget id 연결
        //카카오 버튼
        Button btnKakao = (Button) findViewById(R.id.btnKakao);
        TextView bName = (TextView) findViewById(R.id.title);
        WebView bCover = (WebView) findViewById(R.id.cover);
        final TextView score = (TextView) findViewById(R.id.score);
        TextView percent = (TextView) findViewById(R.id.percent);
        TextView candy = (TextView) findViewById(R.id.candy);
        TextView certify = (TextView) findViewById(R.id.certify);
        Button btnResolve = (Button) findViewById(R.id.btnResolve);
        Button btnEnd = (Button) findViewById(R.id.btnEnd);
        ImageView Monsterball1 = (ImageView) findViewById(R.id.monster1);
        ImageView Monsterball2 = (ImageView) findViewById(R.id.monster2);
        ImageView Monsterball3 = (ImageView) findViewById(R.id.monster3);
        final GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);



        /// BookInfo에서 넘겨받은 책 정보
        final int no;
        final String title, cover;

        no = getIntent().getIntExtra("no", 0);
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");

        // 넘겨받은 정보 추가
        bName.setText(title);
        bCover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(cover), "text/html", "utf-8", null);

        loginNo = LoginActivity.setting.getLong("no", 0);
        System.out.println(loginNo);

        // Solvequiz에서 넘겨받은 퀴즈 풀기 정보
        final int correct;
        correct = getIntent().getIntExtra("correct", 0);

        //포켓몬 카드 보여주기
        url = "http://220.67.115.225:8088/readingbetter/quizapp/card";
        GetConnect taskcard = new GetConnect(url);
        taskcard.start();

        try {
            taskcard.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String resultcard = taskcard.getResult();
        System.out.println("here");
        resultcard="{'result':["+resultcard+"]}";
        System.out.println(resultcard);

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(resultcard);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();

            coverList = new String[ARRAY_LENGTH];
            titleList = new String[ARRAY_LENGTH];
            bonusList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);

                String card_cover = jo.getString("cover");
                String card_title = jo.getString("title");
                String card_bonus = jo.getString("bonus");

                // String 배열에 저장
                coverList[i] = card_cover;
                titleList[i] = card_title;
                bonusList[i] = card_bonus;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //몬스터볼 누를때
        Monsterball1.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                View dialogView = (View) View.inflate(QuizResultActivity.this,R.layout.quizresult_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(QuizResultActivity.this);

                WebView Cover = (WebView) dialogView.findViewById(R.id.coverCard);
                dlg.setView(dialogView);

                Cover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(coverList[0]), "text/html", "utf-8", null);
                dlg.setTitle("카드확인");
                dlg.setNegativeButton("결과보기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linearLayout.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                        score.setVisibility(View.VISIBLE);

                    }
                });
                dlg.show();


            }
        });
        Monsterball2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(QuizResultActivity.this,R.layout.quizresult_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(QuizResultActivity.this);

                WebView Cover = (WebView) dialogView.findViewById(R.id.coverCard);
                dlg.setView(dialogView);

                Cover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(coverList[0]), "text/html", "utf-8", null);
                dlg.setTitle("카드확인");
                dlg.setNegativeButton("결과보기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linearLayout.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                        score.setVisibility(View.VISIBLE);

                    }
                });
                dlg.show();


            }
        });
        Monsterball3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(QuizResultActivity.this,R.layout.quizresult_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(QuizResultActivity.this);

                WebView Cover = (WebView) dialogView.findViewById(R.id.coverCard);
                dlg.setView(dialogView);

                Cover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(coverList[0]), "text/html", "utf-8", null);
                dlg.setTitle("카드확인");
                dlg.setNegativeButton("결과보기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linearLayout.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                        score.setVisibility(View.VISIBLE);

                    }
                });
                dlg.show();


            }
        });


        // 퀴즈 풀기의 정답률
        String per2;
        per1 = correct * 20;
        per2 = Integer.toString(per1);
        percent.setText(per2 + "%");
        scores = (correct*20)+Integer.parseInt( bonusList[0]);


        //캔디 지급
        // 퀴즈 풀기 결과 정답률이 80%이상일 경우 독서 인증 성공
        //인증여부
        // Members.php를 MembersConnect로 연결
        certification= "http://220.67.115.225:8088/readingbetter/quizapp/certification?memberNo="+loginNo+"&bookNo="+no;
        GetConnect task = new GetConnect(certification);
        task.start();

        try {
            task.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = task.getResult();
        System.out.println("here");
        System.out.println(result);

       if(result != "") {
           certify.setText("인증된 도서 입니다");

           point=0;
           candy.setText("0개");
           score.setText(scores + "점");
           System.out.println(loginNo + "," + scores + "," + point + "," + no);
           String url1 = "http://220.67.115.225:8088/readingbetter/quizapp/historyScores?memberNo=" + loginNo +
                   "&score=" + scores + "&point=" + point + "&bookNo=" + no;
           task_insert.execute(url1);

       }else {
           if (per1 >= 80) {
               certify.setText("성공");
               point = 3;
               candy.setText(point + "개");
               score.setText(scores + "점");
               System.out.println(loginNo + "," + scores + "," + point + "," + no);
               String url2 = "http://220.67.115.225:8088/readingbetter/quizapp/cert?memberNo="+loginNo+"&bookNo="+no;
               task_insert.execute(url2);
               String url3 = "http://220.67.115.225:8088/readingbetter/quizapp/historyScores?memberNo=" + loginNo +
                       "&score=" + scores + "&point=" + point + "&bookNo=" + no;
               task_insert2.execute(url3);

           } else {
               certify.setText("실패");
               point = 0;
               candy.setText(point + "개");
               score.setText(scores + "점");
               String url3 = "http://220.67.115.225:8088/readingbetter/quizapp/historyScores?memberNo=" + loginNo +
                       "&score=" + scores + "&point=" + point + "&bookNo=" + no;
               task_insert.execute(url3);

           }

       }


        loginId = LoginActivity.setting.getString("id", null);

        // 공유하기를 눌렀을 때 동작
        btnKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    KakaoLink kakaoLink = KakaoLink.getKakaoLink(QuizResultActivity.this);
                    KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                    String text = loginId + "님이 " + title + "에서" + scores + "점을 획득 하셨습니다\n앱에 접속하셔서 점수를 겨뤄보세요!";

                    final String linkContents = kakaoTalkLinkMessageBuilder
                            .addImage(cover, 200, 200)
                            .addText(text)
                            .addAppButton("앱으로 이동")
                            .build();

                    kakaoLink.sendMessage(linkContents, QuizResultActivity.this);

                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
            }
        });
        // PutReview.php로 데이터 전달
        // 다시 풀기 버튼을 클릭했을 때 동작하는 Listener
        btnResolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSolveQuiz = new Intent(getApplicationContext(), SolveQuizActivity.class);
                goSolveQuiz.putExtra("no", no);
                goSolveQuiz.putExtra("cover", cover);
                goSolveQuiz.putExtra("title", title);
                startActivity(goSolveQuiz);
                finish();
            }
        });

        // 끝내기 버튼을 클릭했을 때 동작하는 Listener
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}