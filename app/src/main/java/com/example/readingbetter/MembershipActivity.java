package com.example.readingbetter;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import com.example.readingbetter.connect.GetConnect;
import com.example.readingbetter.connect.PutConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MembershipActivity extends Activity {

    private Button btnPrev, btnJoin;
    private EditText memName, memId, memPW, checkPW, memEmail, memTel;
    private ImageButton btnCheckId, btnCheckEmail;
    private String id, pw, name, email, tel;
    private PutConnect task_insert;
    private int arrayLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_page);

        //각 변수에 위젯을 대입
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnJoin = (Button) findViewById(R.id.btnJoin);
        memName = (EditText) findViewById(R.id.name);
        memId = (EditText) findViewById(R.id.id);
        memPW = (EditText) findViewById(R.id.pw);
        checkPW = (EditText) findViewById(R.id.checkPW);
        memEmail = (EditText) findViewById(R.id.email);
        memTel = (EditText) findViewById(R.id.tel);
        btnCheckId = (ImageButton) findViewById(R.id.btnCheckId);
        btnCheckEmail = (ImageButton) findViewById(R.id.btnCheckEmail);

        //이전 버튼 클릭 시 액티비티 종료
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 아이디 텍스트 변화에 따른 동작
        memId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                btnCheckId.setEnabled(true);
                btnCheckId.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        memEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnCheckEmail.setEnabled(true);
                btnCheckEmail.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //회원가입 버튼 클릭 시 동작
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = memId.getText().toString();
                pw = memPW.getText().toString();
                name = memName.getText().toString();
                email = memEmail.getText().toString();
                tel = memTel.getText().toString();

                if (btnCheckId.isEnabled() == true) {
                    Toast.makeText(MembershipActivity.this, "아이디 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (btnCheckEmail.isEnabled() == true) {
                    Toast.makeText(MembershipActivity.this, "이메일 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println(pw +":"+ checkPW.getText().toString());
                //필수 입력 정보를 입력하지 않은 경우 토스트 메시지 출력
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(tel)) {
                    Toast.makeText(MembershipActivity.this, "필수 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //비밀번호와 비밀번호 확인이 일치하는 경우 회원정보 DB에 삽입
                if (!pw.equals(checkPW.getText().toString())) {
                    Toast.makeText(MembershipActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // PutMember.php로 데이터 전달
                task_insert = new PutConnect();
                String url = "http://220.67.115.225:8088/readingbetter/memberapp/login?id=" + id + "&pw=" + pw
                        + "&name=" + name + "&email=" + email + "&tel=" + tel;
                task_insert.execute(url);

                Toast.makeText(MembershipActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 체크(ID중복확인) 버튼 클릭 시 동작
        btnCheckId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = memId.getText().toString();

                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(MembershipActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "http://220.67.115.225:8088/readingbetter/memberapp/checkid?id=" + id;
                GetConnect task = new GetConnect(url);
                task.start();

                try {
                    task.join();
                    System.out.println("waiting... for result");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = task.getResult();
                result = "{'result':[" + result + "]}";

                // JSON 파싱
                try {
                    JSONObject root = new JSONObject(result);
                    JSONArray ja = root.getJSONArray("result");
                    arrayLength = ja.length();

                    if (arrayLength != 0) {
                        Toast.makeText(MembershipActivity.this, "다른 아이디를 사용해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(MembershipActivity.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    btnCheckId.setEnabled(false);
                    btnCheckId.setBackgroundColor(Color.CYAN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = memEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MembershipActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "http://220.67.115.225:8088/readingbetter/memberapp/checkemail?email=" + email;
                GetConnect task = new GetConnect(url);
                task.start();

                try {
                    task.join();
                    System.out.println("waiting... for result");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = task.getResult();
                result = "{'result':[" + result + "]}";

                // JSON 파싱
                try {
                    JSONObject root = new JSONObject(result);
                    JSONArray ja = root.getJSONArray("result");
                    arrayLength = ja.length();

                    if (arrayLength != 0) {
                        Toast.makeText(MembershipActivity.this, "다른 이메일을 사용해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(MembershipActivity.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                    btnCheckEmail.setEnabled(false);
                    btnCheckEmail.setBackgroundColor(Color.CYAN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}