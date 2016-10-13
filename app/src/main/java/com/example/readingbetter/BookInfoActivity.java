package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.WebView;
import android.widget.*;
import android.view.*;

// 책 정보 화면
public class BookInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookinfo_page); // Layout bookinfo_page의 자바 파일

        // Layout widget id 연결
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        Button btnMake = (Button) findViewById(R.id.btnMake);
        Button btnSolve = (Button) findViewById(R.id.btnSolve);
        Button btnReview = (Button) findViewById(R.id.btnReview);
        WebView bCover = (WebView) findViewById(R.id.cover);
        TextView bTitle = (TextView) findViewById(R.id.title);
        TextView bAuthor = (TextView) findViewById(R.id.author);
        TextView bPublisher = (TextView) findViewById(R.id.publisher);
        TextView bRecommend = (TextView) findViewById(R.id.recommend);

        // BookList에서 넘겨받은 책 정보
        final int no;
        final String title, author, publisher, recommend, cover;
        no = getIntent().getIntExtra("no", 0);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        recommend = getIntent().getStringExtra("recommend");
        cover = getIntent().getStringExtra("cover");

        // 넘겨받은 정보 추가
        bTitle.setText(title);
        bAuthor.setText(author);
        bPublisher.setText(publisher);
        bRecommend.setText(recommend);
        bCover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(cover), "text/html", "utf-8", null);

        // 퀴즈 내기 버튼을 클릭했을 때 동작하는 Listener
        btnMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Makequiz로 값을 넘겨주면서 호출
                Intent goMakeQuiz = new Intent(getApplicationContext(), MakeQuizActivity.class);
                goMakeQuiz.putExtra("no", no);
                goMakeQuiz.putExtra("cover", cover);
                goMakeQuiz.putExtra("title", title);
                startActivity(goMakeQuiz);
            }
        });

        // 퀴즈 풀기 버튼을 클릭했을 때 동작하는 Listener
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetQuizList gql = new GetQuizList();
                gql.getQuizList(no);
                // Solvequiz로 값을 넘겨주면서 호출
                Intent goSolveQuiz = new Intent(getApplicationContext(), SolveQuizActivity.class);
                goSolveQuiz.putExtra("no", no);
                goSolveQuiz.putExtra("cover", cover);
                goSolveQuiz.putExtra("title", title);
                startActivity(goSolveQuiz);
            }
        });

        // 리뷰 버튼을 클릭했을 때 동작하는 Listener
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Review 호출
                Intent goReview = new Intent(getApplicationContext(), ReviewActivity.class);
                goReview.putExtra("no", no);
                goReview.putExtra("cover", cover);
                goReview.putExtra("title", title);
                startActivity(goReview);
            }
        });

        // 이전 페이지 버튼을 클릭했을 때 동작하는 Listener
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}