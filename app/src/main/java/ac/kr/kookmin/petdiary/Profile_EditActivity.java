package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_EditActivity extends AppCompatActivity {
    String[] genders = {"남 (♂)", "여 (♀)", "기타"};
    TextView txt_gender = findViewById(R.id.txt_pf_edit_gender);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // 성별 선택
        Spinner spinner_gender = findViewById(R.id.spiner_pf_gender);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,genders);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner_gender.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                txt_gender.setText(genders[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_gender.setText("");
            }
        });



        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼

        imgBtn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_EditActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }
}