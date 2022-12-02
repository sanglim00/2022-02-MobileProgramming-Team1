package ac.kr.kookmin.petdiary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText email, name, id, pw, pw2;
    Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_change_pw);

        // 비밀번호 변경을 위해 유저확인용 값들
        email = findViewById(R.id.changeEmailInput);
        name = findViewById(R.id.changeNameInput);
        id = findViewById(R.id.changeIdInput);
        // 변경할 비밀번호와 비밀번호 확인
        pw = findViewById(R.id.changePwInput);
        pw2 = findViewById(R.id.changePwInput2);

        // 비밀번호 변경 버튼 클릭 시
        changeButton = findViewById(R.id.changePwButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "비밀번호 변경 기능은 추후에 구현할 예정입니다!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}