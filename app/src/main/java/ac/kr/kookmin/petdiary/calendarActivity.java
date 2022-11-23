package ac.kr.kookmin.petdiary;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

public class calendarActivity extends AppCompatActivity {

    public String str = null;
    public CalendarView calendarView;
    public Button save_Btn;
    public TextView diaryTextView, textView2, textView3;
    boolean complete_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);


        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
            }
        });



        save_Btn.setOnClickListener(new View.OnClickListener() { // save button clicked
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                complete_date = true;
                textView2.setText(str);
                textView2.setVisibility(View.VISIBLE);
                intent.putExtra("complete_date", complete_date);
                intent.putExtra("new_date", diaryTextView.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}