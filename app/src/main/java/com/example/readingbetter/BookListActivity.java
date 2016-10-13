package com.example.readingbetter;

import android.app.*;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;

import com.example.readingbetter.connect.GetConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

class BookInfo{
    private Integer no;
    private String title;
    private String recommend;
    private String author;
    private String publisher;
    private String cover;

    public BookInfo (Integer no, String title, String author, String recommend, String publisher, String cover) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.recommend = recommend;
        this.publisher = publisher;
        this.cover = cover;
    }

    public Integer getNo() {
        return this.no;
    }

    public String getTitle() {
        return this.title;
    }

    public  String getAuthor(){
        return this.author;
    }

    public String getRecommend(){
        return this.recommend;
    }

    public String getPublisher(){
        return this.publisher;
    }

    public String getCover(){
        return this.cover;
    }
}

public class BookListActivity extends Activity {
    public String bookInfo;
    EditText search; // 검색할 텍스트를 입력할 EditText
    ListView bookList; // 책 목록을 담을 ListView

    Integer[] noList;
    String[] titleList;
    String[] authorList;
    String[] recommendList;
    String[] publisherList;
    String[] coverList;

    BookListAdapter adapter;
    ArrayList<BookInfo> arraylist = new ArrayList<BookInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist_page);

        // Layout id와 연결
        search = (EditText)findViewById(R.id.search);
        bookList = (ListView)findViewById(R.id.bookList);

        // Members.php를 MembersConnect로 연결
        bookInfo =  "http://220.67.115.225:8088/readingbetter/bookapp/list";
        GetConnect task = new GetConnect(bookInfo);
        task.start();

        try {
            task.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) { }

        String result = task.getResult();
        System.out.println("here");
        result="{'result':"+result+"}";
        System.out.println(result);

        // JSON 파싱
        try {
            JSONObject root = new JSONObject(result);
            JSONArray ja = root.getJSONArray("result");
            final int ARRAY_LENGTH = ja.length();

            noList = new Integer[ARRAY_LENGTH];
            titleList = new String[ARRAY_LENGTH];
            authorList = new String[ARRAY_LENGTH];
            recommendList = new String[ARRAY_LENGTH];
            publisherList = new String[ARRAY_LENGTH];
            coverList = new String[ARRAY_LENGTH];

            // 아이디 배열에 대입
            for (int i = 0; i < ARRAY_LENGTH; i++) {
                JSONObject jo = ja.getJSONObject(i);
                Integer no = jo.getInt("no");
                String title = jo.getString("title");
                String author = jo.getString("authorName");
                String recommend = jo.getString("recommend");
                String publisher = jo.getString("publisherTitle");
                String cover = jo.getString("cover");

                noList[i] = no;
                titleList[i] = title;
                authorList[i] = author;
                recommendList[i] = recommend;
                publisherList[i] = publisher;
                coverList[i] = cover;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < noList.length; i++) {
            BookInfo bi = new BookInfo(noList[i], titleList[i], authorList[i], recommendList[i], publisherList[i], coverList[i]);
            // Binds all strings into an array
            arraylist.add(bi);
        }

        // Pass results to ListViewAdapter Class
        adapter = new BookListAdapter(this, arraylist);
        // Binds the Adapter to the ListView
        bookList.setAdapter(adapter);

        // Capture Text in EditText
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });
    }
}