package com.example.readingbetter;

import android.app.*;
import android.os.*;
import android.text.TextUtils;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import com.example.readingbetter.connect.PutConnect;

public class MakeQuizActivity extends Activity {
    private String quiz, ex1, ex2, ex3, ex4, answer;
    private PutConnect task_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makequiz_page);

        //각변수에 위젯 대입한다.
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        WebView bCover = (WebView) findViewById(R.id.cover);
        TextView bTitle = (TextView) findViewById(R.id.title);
        final EditText qQuiz = (EditText) findViewById(R.id.quiz);
        final EditText qEx1 = (EditText) findViewById(R.id.ex1);
        final EditText qEx2 = (EditText) findViewById(R.id.ex2);
        final EditText qEx3 = (EditText) findViewById(R.id.ex3);
        final EditText qEx4 = (EditText) findViewById(R.id.ex4);
        final EditText qAnswer = (EditText) findViewById(R.id.answer);

        // BookInfo에서 넘겨받은 책 정보
        final int no;
        final String title, cover;
        no = getIntent().getIntExtra("no", 0);
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");

        // 넘겨받은 정보 추가
        bTitle.setText(title);
        bCover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(cover), "text/html", "utf-8", null);

        //이전버튼 클릭시 동작
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Quiz 테이블에 문제를 등록한다.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz = qQuiz.getText().toString().replace(" ", "_");
                ex1 = qEx1.getText().toString().replace(" ", "_");
                ex2 = qEx2.getText().toString().replace(" ", "_");
                ex3 = qEx3.getText().toString().replace(" ", "_");
                ex4 = qEx4.getText().toString().replace(" ", "_");
                answer = qAnswer.getText().toString();

                if (answer.equals("1")) {
                    answer = ex1;
                    putQuiz(no);
                } else if (answer.equals("2")) {
                    answer = ex2;
                    putQuiz(no);
                } else if (answer.equals("3")) {
                    answer = ex3;
                    putQuiz(no);
                } else if (answer.equals("4")) {
                    answer = ex4;
                    putQuiz(no);
                } else {
                    Toast.makeText(getApplicationContext(), "답 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void putQuiz(int num) {
        if (TextUtils.isEmpty(quiz) || TextUtils.isEmpty(ex1) || TextUtils.isEmpty(ex2) || TextUtils.isEmpty(ex3) ||
                TextUtils.isEmpty(ex4) || TextUtils.isEmpty(answer)) {
            Toast.makeText(getApplicationContext(), "정보를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            // putquiz.php로 데이터 전달
            task_insert = new PutConnect();
            String url = "http://220.67.115.225:8088/readingbetter/quizapp/insertquiz?bookNo=" + num + "&quiz=" + quiz + "&ex1=" + ex1
                    + "&ex2=" + ex2 + "&ex3=" + ex3 + "&ex4=" + ex4 + "&answer=" + answer;
            task_insert.execute(url);
            Toast.makeText(getApplicationContext(), "입력되었습니다 ", Toast.LENGTH_SHORT).show();
        }
    }
}