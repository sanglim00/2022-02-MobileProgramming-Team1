package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Profile_EditActivity extends AppCompatActivity {
    String[] genders = {"남 (♂)", "여 (♀)", "기타"};
    boolean complete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        EditText et_edit_name = findViewById(R.id.et_pf_edit_name); // 이름 편집 EditText
        Spinner spinner_gender = findViewById(R.id.spiner_pf_gender); // 성별 선택 Spinner
        TextView txt_gender = findViewById(R.id.txt_pf_edit_gender); // 성별 선택 txt
        EditText et_edit_meetDate = findViewById(R.id.et_pf_edit_meetDate); // 만난 날짜 편집 EditText
        EditText et_edit_one_line_info = findViewById(R.id.et_pf_edit_one_line_info); // 한줄 소개 편집 EditText

        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼

        Button btn_edit_complete = findViewById(R.id.btn_pf_edit_complete); // 편집 완료 버튼



        Intent editIntent = getIntent();
        String nameIntent = editIntent.getStringExtra("name");
        String genderIntent =  editIntent.getStringExtra("gender");
        String meetDateIntent = editIntent.getStringExtra("meetDate");
        String oneLineIntent = editIntent.getStringExtra("one_line");

        et_edit_name.setText(nameIntent);
        txt_gender.setText(genderIntent);
        et_edit_meetDate.setText(meetDateIntent);
        et_edit_one_line_info.setText(oneLineIntent); // 기존값 받아옴





        ArrayAdapter<String> adapter = new ArrayAdapter<String>( // 성별 선택 Spinner Adapter
                this, android.R.layout.simple_spinner_item,genders);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner_gender.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 성별 선택 Spinner item 선택
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                txt_gender.setText(genders[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_gender.setText("");
            }
        });





        btn_edit_complete.setOnClickListener(new View.OnClickListener() { // 편집 완료 버튼 onclick
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                complete = true; // 완료 버튼을 눌렀을 때만 수정 사항이 반영되도록 함.

                intent.putExtra("name",et_edit_name.getText().toString()); // 이름 설정
                intent.putExtra("gender",txt_gender.getText().toString()); // 성별 설정
                intent.putExtra("meetDate",et_edit_meetDate.getText().toString()); // 만난 날짜 설정
                intent.putExtra("one_line", et_edit_one_line_info.getText().toString()); // 한줄 프로필 설정
                intent.putExtra("complete2", complete);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



        imgBtn_setting.setOnClickListener(new View.OnClickListener() { // 환경설정 버튼 onclick
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_EditActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });




    }
}