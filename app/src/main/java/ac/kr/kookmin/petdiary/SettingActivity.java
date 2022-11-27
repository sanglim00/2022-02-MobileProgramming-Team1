package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class SettingActivity extends AppCompatActivity {

    RadioGroup footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        RadioButton menu = findViewById(R.id.menu_user);
        menu.setChecked(true);
        footer = findViewById(R.id.footer_menu);
        footer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                RadioButton menu = radioGroup.findViewById(i);

                if(menu.getText().equals("메인")) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("검색")) {
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("작성")) {
                    intent = new Intent(getApplicationContext(), WritingActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("알림")) {
                    intent = new Intent(getApplicationContext(), NotiActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("유저")) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void ChangePW(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void ShowPrivacyPolicy(View view) {
        Intent intent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    public void ShowServiceContents(View view) {
        Intent intent = new Intent(getApplicationContext(), ServiceContentsActivity.class);
        startActivity(intent);
    }


}