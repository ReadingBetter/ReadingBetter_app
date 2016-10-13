package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;

// 기본이 되는 탭화면
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main의 자바 파일

        TabHost tabHost = this.getTabHost(); // 뼈대로 쓰일 TabHost 생성

        // TabHost에 화면 메인, 랭킹, 설정 3개를 추가
        tabHost.addTab(tabHost.newTabSpec("mtb1").setIndicator("메인").setContent(new Intent(this, BookListActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("mtb2").setIndicator("랭킹").setContent(new Intent(this, RankingActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("mtb3").setIndicator("설정").setContent(new Intent(this, SettingActivity.class)));
    }
}