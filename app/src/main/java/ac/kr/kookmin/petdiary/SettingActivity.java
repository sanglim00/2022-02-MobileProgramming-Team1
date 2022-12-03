package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    Switch NotiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        NotiSwitch = findViewById(R.id.notificationSwitch);
        NotiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "알림 온오프 기능은 추후 구현 예정입니다 !", Toast.LENGTH_SHORT).show();
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