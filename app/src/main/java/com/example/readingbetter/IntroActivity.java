package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.os.*;

// 인트로 화면
public class IntroActivity extends Activity {
    private static int SPLASH_TIME_OUT = 1000; // 인트로 화면이 떠있을 시간
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page); // Layout intro_page의 자바 파일

        // 일정 시간 지난 후 Activity를 종료하고 BookList 호출
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}