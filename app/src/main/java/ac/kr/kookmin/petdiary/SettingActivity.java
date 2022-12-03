package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    Switch NotiSwitch;
    TextView Logout;

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

        Logout = findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialog();
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

    private void LogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃").setMessage("로그아웃 하시려면 확인 버튼을 클릭해주세요 !");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "성공적으로 로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return ;
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
