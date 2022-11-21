package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginEmail, loginPW;
    Button login, sign;
    String showTxt, loginEmailTxt, loginPWTxt;
    public boolean loginCheckEmail, loginCheck;

    // 이메일, 비밀번호 입력되었는지 확인
    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 변수 초기화
        loginEmail = findViewById(R.id.tit_loginEmail);
        loginPW = findViewById(R.id.tit_loginPassword);
        login = findViewById(R.id.btn_login);
        sign = findViewById(R.id.btn_signup);
        showTxt = "";
        loginCheckEmail = true;
        loginCheck = false;

        // 로그인 버튼 클릭
        login.setOnClickListener(view -> {
            loginEmailTxt = loginEmail.getText().toString();
            loginPWTxt = loginPW.getText().toString();

            Pattern pattern = Patterns.EMAIL_ADDRESS;

            // 모든 항목 입력되었는지 확인
            if (!(hasTxt(loginEmail) && hasTxt(loginPW))) {
                showTxt = "모든 항목을 채워주세요";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
            } else {
                // 이메일 유효성 검사
                if (pattern.matcher(loginEmailTxt).matches()) {}
                else {
                    loginCheckEmail = false;
                    showTxt = "올바른 이메일을 입력해주세요";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }

                // 이메일 비밀번호 매칭

            }

            // 모든 조건이 충족되었을 경우
            if (loginCheck) {
                showTxt = "로그인 성공!";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                Intent intentSign = new Intent(getApplication(), MainActivity.class);
                startActivity(intentSign);
                finish();
            }
        });

        // 회원가입 버튼 클릭
        sign.setOnClickListener(view -> {
            Intent intentSign = new Intent(getApplication(), SignUpActivity.class);
            startActivity(intentSign);
            finish();
        });
    }
}