package com.example.readingbetter;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

// 퀴즈 풀기 화면
public class SolveQuizActivity extends Activity {
    public String question;
    TextView quiz;
    RadioButton ex1, ex2, ex3, ex4;

    String[] quizList;
    String[] ex1List;
    String[] ex2List;
    String[] ex3List;
    String[] ex4List;
    String[] answerList;

    int count, correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solvequiz_page); // Layout solvequiz_page의 자바 파일

        // Layout widget id 연결
        WebView bCover = (WebView) findViewById(R.id.cover);
        TextView bTitle = (TextView) findViewById(R.id.title);
        final RadioGroup exGroup = (RadioGroup) findViewById(R.id.exGroup);
        quiz = (TextView) findViewById(R.id.quiz);
        ex1 = (RadioButton) findViewById(R.id.ex1);
        ex2 = (RadioButton) findViewById(R.id.ex2);
        ex3 = (RadioButton) findViewById(R.id.ex3);
        ex4 = (RadioButton) findViewById(R.id.ex4);
        final Button btnNext = (Button) findViewById(R.id.btnNext);

        // BookInfo에서 넘겨받은 책 정보
        final int no;
        final String title, cover;
        no = getIntent().getIntExtra("no", 0);
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");

        // 넘겨받은 정보 추가
        bTitle.setText(title);
        bCover.loadDataWithBaseURL(null, BookListAdapter.creHtmlBody(cover), "text/html", "utf-8", null);

        try {
            correct = getIntent().getIntExtra("correct", 0); // 맞은 개수 누적
            count = getIntent().getIntExtra("count", 0); // 문제 번호 호출

            quiz.setText(GetQuizList.qQuizList[count]);
            ex1.setText(GetQuizList.qEx1List[count]);
            ex2.setText(GetQuizList.qEx2List[count]);
            ex3.setText(GetQuizList.qEx3List[count]);
            ex4.setText(GetQuizList.qEx4List[count]);

            // 문제 풀기 화면 재호출, 문제 결과 화면 호출을 위한 Intent
            final Intent goSolveQuiz = new Intent(getApplicationContext(), SolveQuizActivity.class);
            final Intent goResult = new Intent(getApplicationContext(), QuizResultActivity.class);

            // 마지막 문제가 호출되면 버튼 btnNext의 텍스트를 '다음'에서 '결과'로 바꿈
            if (count == 4)
                btnNext.setText("결과");

            // 보깅에 해당하는 라디오 그룹의 라디오 버튼 클릭이 달라졌을 떄 동작하는 Listener
            exGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                /* 라디오 그룹에서 선택된 라디오 버튼의 아이디를 불러서,
                임의로 생성한 라디오 버튼에 아이디를 대입,
                선택된 라디오 버튼의 텍스트를 대입할 String 변수를 생성, 대입 */
                    RadioButton rb = (RadioButton) findViewById(exGroup.getCheckedRadioButtonId());
                    String str = rb.getText().toString();

                /* 선택된 라디오 버튼의 텍스트 값과,
                문제 번호에 해당하는 정답 배열의 값을 비교,
                일치하면 맞은 개수 누적 변수 qCorrect에 +1 증가
                아닐시 그대로 qCorrect로 함

                이때, 문제 번호가 5 이하일때는,
                문제 풀기 재호출을 위해 SolvequizActivity로 qCorrect값을 보내고,
                문제를 5번까지 다 푼 후에는,
                문제 결과 창인 QuizresultActivity로 qCorrect값을 보냄*/
                    if (count < 4) {
                        if (str.equals(GetQuizList.answerList[count]))
                            goSolveQuiz.putExtra("correct", correct + 1); // 누적 정답 +1 증가
                        else
                            goSolveQuiz.putExtra("correct", correct);

                    } else {
                        if (str.equals(GetQuizList.answerList[count]))
                            goResult.putExtra("correct", correct + 1);
                        else
                            goResult.putExtra("correct", correct);
                    }
                }
            });

            // 다음 문제 버튼을 클릭했을 때 동작하는 Listener
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /* 라디오 그룹에서 선택한 라디오 버튼이 없는채로 다음 문제를 누르면,
                 "답을 선택하세요"라는 Toast를 띄움,
                  아닐시에는 적절한 다음 화면을 띄움 */
                    if (exGroup.getCheckedRadioButtonId() == -1)
                        Toast.makeText(getApplicationContext(), "답을 선택하세요", Toast.LENGTH_SHORT).show();
                    else {
                     /* 문제 번호가 5보다 작을때까지,
                     문제을 바꿔 Solvequiz 재호출하면서,
                     이전 Solvequiz 화면 종료,
                     아닐시 결과 화면을 호출 */
                        if (count < 4) {
                            goSolveQuiz.putExtra("count", count + 1); // 문제 번호 +1 증가
                            // 책 표지 그림과 이름을 보냄
                            goSolveQuiz.putExtra("no", no);
                            goSolveQuiz.putExtra("cover", cover);
                            goSolveQuiz.putExtra("title", title);
                            startActivity(goSolveQuiz);
                            finish();
                        } else {
                            // 책 표지 그림과 이름을 보냄
                            goResult.putExtra("no", no);
                            goResult.putExtra("cover", cover);
                            goResult.putExtra("title", title);
                            finish(); // 문제 번호가 5번이 지나면 Solvequiz 완전히 종료
                            startActivity(goResult);
                        }
                    }
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            finish();
            Toast.makeText(getApplicationContext(), "문제가 아직 없습니다", Toast.LENGTH_SHORT).show();
        }
    }
}