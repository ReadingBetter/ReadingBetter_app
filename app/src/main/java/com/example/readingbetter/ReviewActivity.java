package com.example.readingbetter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readingbetter.connect.GetConnect;
import com.example.readingbetter.connect.PutConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ReviewInfo{
    private String writerID;
    private String review;
    private String writeDate;

    public ReviewInfo (String writerID, String review, String writeDate) {
        this.writerID = writerID;
        this.review = review;
        this.writeDate = writeDate;
    }

    public String getWriterID() {
        return this.writerID;
    }

    public String getReview() {
        return this.review;
    }

    public String getWriteDate(){
        return this.writeDate;
    }
}

public class ReviewActivity extends Activity{
    WebView bCover;
    TextView bTitle;
    ListView lvReview;
    Button btnPrev, btnWrite;
    EditText edtReview;
    private String review;
    private Long writer;

    private String[] arrayWriterID;
    private String[] arrayReview;
    private String[] arrayWriteDate;
    private int arrayLength;

    ReviewListAdapter adapter;
    ArrayList<ReviewInfo> arraylist = new ArrayList<ReviewInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);

        // 각 변수에 위젯 대입
        bCover = (WebView) findViewById(R.id.cover);
        bTitle = (TextView) findViewById(R.id.title);
        lvReview = (ListView) findViewById(R.id.reviewList);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        edtReview = (EditText) findViewById(R.id.review);

        final int no;
        final String title, cover;
        no = getIntent().getIntExtra("no", 0);
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");

        // 넘겨받은 정보 추가
        bTitle.setText(title);
        bCover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(cover), "text/html", "utf-8", null);

        // GetReview.php를 GetConnect로 연결, book_num을 넘겨줌
        String url = "http://220.67.115.225:8088/readingbetter/bookapp/review?bookNo=" + no;
        GetConnect task = new GetConnect(url);
        task.start();


        try{
            task.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e){
        }
        String result = task.getResult();
        System.out.println("here");
        result="{'result':"+result+"}";
        System.out.println(result);

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ja = root.getJSONArray("result");
            arrayLength = ja.length();
            arrayWriterID = new String[arrayLength];
            arrayReview = new String[arrayLength];
            arrayWriteDate = new String[arrayLength];

            // 작성자, 리뷰내용, 작성일 배열에 대입
            for (int i = 0; i < arrayLength; i++){
                JSONObject jo = ja.getJSONObject(i);
                String writerID = jo.getString("id");
                String review = jo.getString("review");
                String writeDate = jo.getString("regDate");
                arrayWriterID[i] = writerID;
                arrayReview[i] = review.replace("_", " ");
                arrayWriteDate[i] = writeDate;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        // 리뷰 출력
        for (int i = 0; i < arrayWriterID.length; i++) {
            ReviewInfo ri = new ReviewInfo(arrayWriterID[i], arrayReview[i], arrayWriteDate[i]);
            // Binds all strings into an array
            arraylist.add(ri);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ReviewListAdapter(this, arraylist);
        // Binds the Adapter to the ListView
        lvReview.setAdapter(adapter);

        // 등록 버튼(리뷰쓰기) 클릭시 동작
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review = edtReview.getText().toString();
                writer = LoginActivity.setting.getLong("no", 0);
                review = review.replace(" ", "_");
                if(review.equals("")){
                    Toast.makeText(ReviewActivity.this, "리뷰를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // PutReview.php로 데이터 전달
                    PutConnect task_insert = new PutConnect();
                    String url = "http://220.67.115.225:8088/readingbetter/bookapp/insertreview?bookNo=" + no +
                            "&review=" + review + "&memberNo=" + writer;
                    task_insert.execute(url);

                    Toast.makeText(ReviewActivity.this, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    edtReview.setText("");
                }
            }
        });

        // 이전 버튼 클릭시 동작
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 신고 버튼 클릭시 동작
    }
}