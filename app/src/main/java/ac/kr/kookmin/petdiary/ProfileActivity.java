package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.BreakIterator;

public class ProfileActivity extends AppCompatActivity {
    TextView txt_pf_name;
    TextView txt_pf_gender;
    TextView txt_pf_meetDate;
    TextView txt_pf_one_line_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txt_pf_name = findViewById(R.id.txt_pf_name); // 프로필 layout - 이름
        txt_pf_gender = findViewById(R.id.txt_pf_gender); // 프로필 layout - 성별
        txt_pf_meetDate = findViewById(R.id.txt_pf_meetDate); // 프로필 layout - 만난 날짜
        txt_pf_one_line_info = findViewById(R.id.txt_pf_one_line_info); // 프로필 layout - 한줄 소개개


       ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼
        Button btn_edit_profile = findViewById(R.id.btn_pf_edit_profile); // 프로필 편집



        imgBtn_setting.setOnClickListener(new View.OnClickListener() { // 환경설정 버튼
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() { // 프로필 편집
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ProfileActivity.this, Profile_EditActivity.class);
                intent.putExtra("name", txt_pf_name.getText());
                intent.putExtra("gender", txt_pf_gender.getText());
                intent.putExtra("meetDate", txt_pf_meetDate.getText());
                intent.putExtra("one_line", txt_pf_one_line_info.getText());

                startActivityForResult(intent,0);
            }

        });

    }
    @Override
    protected void onActivityResult(int req, int resultcode, @Nullable Intent data){ // Profile_EditActivity 종료 : 값 받아오기 (intent)
        super.onActivityResult(req, resultcode, data);
        if(req == 0 && resultcode == RESULT_OK){
            boolean getcomplete = data.getBooleanExtra("complete", false);
            if(getcomplete){ // 완료 버튼을 눌렀을 때만 수정사항이 반영되도록 함. ( 취소 버튼 제외 )
                String nameIntent = data.getStringExtra("name");
                String genderIntent = data.getStringExtra("gender");
                String meetDateIntent = data.getStringExtra("meetDate");
                String oneLineIntent = data.getStringExtra("one_line");

                txt_pf_name.setText(nameIntent);
                txt_pf_gender.setText(genderIntent);
                txt_pf_meetDate.setText(meetDateIntent);
                txt_pf_one_line_info.setText(oneLineIntent);
            }


        }
    }

}