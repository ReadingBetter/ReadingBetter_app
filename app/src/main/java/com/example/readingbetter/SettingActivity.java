package com.example.readingbetter;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.*;

// 설정 화면
public class SettingActivity extends Activity {
    private final static double CURRENT_VERSION = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page); // Layout setting_page의 자바 파일

        // Layout widget id 연결
        Button btnShop = (Button) findViewById(R.id.btnShop);
        Button btnHistory = (Button) findViewById(R.id.btnHistory);
        TextView id = (TextView) findViewById(R.id.id);
        TextView version = (TextView) findViewById(R.id.version);

        // 개인정보 출력
        id.setText(String.valueOf(LoginActivity.setting.getString("id", "")));
        version.setText(String.valueOf(CURRENT_VERSION));

        // 상점 버튼을 클릭했을 때 동작하는 Listener
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goGift = new Intent(getApplicationContext(), GifticonActivity.class);
                startActivity(goGift);
            }
        });

        // 히스토리 버튼을 클릭했을 때 동작하는 Listener
        btnHistory.setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                Intent goHistory = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(goHistory);
            }
        });
    }
}