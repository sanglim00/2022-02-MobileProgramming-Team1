package ac.kr.kookmin.petdiary;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class calendarActivity extends AppCompatActivity {

    public String str = null;
    public CalendarView calendarView;
    public Button save_Btn;
    public TextView diaryTextView, textView3;
    boolean complete_date;
    Date now_date; // 현재 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);

        textView3 = findViewById(R.id.textView3);

        diaryTextView.setText(getIntent().getStringExtra("current_date")); // 프로필에서 넘어온 날짜

        long now = System.currentTimeMillis();
        now_date = new Date(now); // 현재 날짜



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                String str_gun = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                diaryTextView.setText(str_gun);
            }
        });



        save_Btn.setOnClickListener(new View.OnClickListener() { // save button clicked
            @Override
            public void onClick(View view) {

                String date_selected = diaryTextView.getText().toString();
                if(dateCompare(date_selected, now_date)){

                    Intent intent = new Intent();
                    complete_date = true;
                    intent.putExtra("complete_date", complete_date);
                    intent.putExtra("new_date", diaryTextView.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    Toast.makeText(calendarActivity.this, "미래에 만날 아이들은 미래에 등록해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean dateCompare(String str1, Date now_date){ // 비교할 날짜 (diaryTextView.getText()), 현재 날짜
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String now_str = dateFormat1.format(now_date); // date to String (format 조정)
        Date date = null;
        Date date_now = null;
        try{
            date_now = dateFormat1.parse(now_str); // string to date(now, format 조정)
            date = dateFormat1.parse(str1); // string to date
        } catch (ParseException e){
            e.printStackTrace();
        }
        int compare = date_now.compareTo(date);

        if (compare < 0) {
            return false;
            // date 가 date_now 보다 큰 경우 처리할 부분
        }
        else{
            return true;
        }




    }



}
