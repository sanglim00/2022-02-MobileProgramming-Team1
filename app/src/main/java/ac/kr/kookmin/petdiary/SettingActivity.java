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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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