package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.example.readingbetter.connect.GetConnect;
import com.example.readingbetter.vo.MemberVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity{
    //Preference 정의
    public static SharedPreferences setting;
    public static SharedPreferences.Editor editor;

    private String id;
    private String pw;
    int arrayLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        //각 변수에 위젯 대입
        final EditText memID = (EditText)findViewById(R.id.id);
        final EditText memPW = (EditText)findViewById(R.id.pw);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnJoin = (Button)findViewById(R.id.btnJoin);
        CheckBox autoLogin = (CheckBox)findViewById(R.id.autoLogin);

        // 로그인 버튼 클릭시 동작 코드
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = memID.getText().toString();
                pw = memPW.getText().toString();

                MemberVo vo = new MemberVo();
                vo.setId(id);
                vo.setPw(pw);

                // GetMember.php를 GetConnect로 연결
                String url = "http://220.67.115.225:8088/readingbetter/memberapp/join?id="+ vo.getId() + "&pw=" + vo.getPw();
                GetConnect task = new GetConnect(url);
                task.start();

                try{
                    task.join();
                    System.out.println("waiting... for result");
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                String result = task.getResult();
                System.out.println("here");
                result="{'result':["+result+"]}";
                System.out.println(result);

                // JSON 파싱
                try {
                    JSONObject root = new JSONObject(result);
                    JSONArray ja = root.getJSONArray("result");
                    arrayLength = ja.length();

                    // 아이디, 비밀번호 일치
                    if(arrayLength != 0){
                        // 아이디, 비밀번호 배열에 대입
                        JSONObject jo = ja.getJSONObject(0);
                        MemberVo authMember = new MemberVo();
                        authMember.setNo(jo.getLong("no"));
                        authMember.setId(jo.getString("id"));

                        editor.putLong("no", authMember.getNo());
                        editor.putString("id", authMember.getId());
                        editor.commit();

                        Intent goMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goMain);
                        return;
                    } else {
                        Toast.makeText(LoginActivity.this, "아이디 혹은 패스워드가 틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        //회원가입 버튼 클릭 시 동작 코드
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMem = new Intent(getApplicationContext(), MembershipActivity.class);
                startActivity(goMem);
            }
        });

        //자동 로그인 체크박스 동작 코드
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String ID = memID.getText().toString();
                    String PW = memPW.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("chAutoLogin_enabled", true);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
            }
        });

        //자동 로그인 체크 시 로그인 화면에서 ID, PW 값 유지
        if(setting.getBoolean("chAutoLogin_enabled", false)){
            memID.setText(setting.getString("ID", ""));
            memPW.setText(setting.getString("PW", ""));
            autoLogin.setChecked(true);
        }
    }
}