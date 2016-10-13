package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;

// 랭킹 화면의 기본이 되는 탭 화면
public class RankingActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_page); // Layout ranking_page의 자바 파일

        TabHost tabHost = this.getTabHost(); // 뼈대로 쓰일 TabHost 생성

        // TabHost에 한 달 랭킹(회원) 화면 추가, RankingMonthly 호출
        Intent goMonthly = new Intent(this, RankingMonthlyActivity.class);
        TabHost.TabSpec tabSpecTotal = tabHost.newTabSpec("Monthly").setIndicator("한 달");
        tabSpecTotal.setContent(goMonthly);
        tabHost.addTab(tabSpecTotal);

        // TabHost에 한 달 랭킹(학교) 화면 추가, RankingSchool 호출
        Intent goSchool = new Intent(this, RankingSchoolActivity.class);
        TabHost.TabSpec tabSpecSchool = tabHost.newTabSpec("School").setIndicator("학교");
        tabSpecSchool.setContent(goSchool);
        tabHost.addTab(tabSpecSchool);

        // TabHost에 명예의 전당(회원) 화면 추가, RankingHonor 호출
        Intent goHonor = new Intent(this, RankingHonorActivity.class);
        TabHost.TabSpec tabSpecHonor = tabHost.newTabSpec("Honor").setIndicator("명예의 전당");
        tabSpecHonor.setContent(goHonor);
        tabHost.addTab(tabSpecHonor);

        tabHost.setCurrentTab(0);
    }
}