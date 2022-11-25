package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private long backpressedTime = 0;
    private FirebaseAuth mAuth;
    TextInputEditText loginEmail, loginPW;
    Button login, sign;
    String showTxt, loginEmailTxt, loginPWTxt;
    public boolean loginCheckEmail;

    // 이메일, 비밀번호 입력되었는지 확인
    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intentSign = new Intent(getApplication(), MainActivity.class);
            startActivity(intentSign);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 변수 초기화
        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.tit_loginEmail);
        loginPW = findViewById(R.id.tit_loginPassword);
        login = findViewById(R.id.btn_login);
        sign = findViewById(R.id.btn_signup);
        showTxt = "";
        loginCheckEmail = true;

        // 이메일 엔터 방지
        loginEmail.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 비밀번호 엔터 방지
        loginPW.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 로그인 버튼 클릭
        login.setOnClickListener(view -> {
            loginEmailTxt = loginEmail.getText().toString();
            loginPWTxt = loginPW.getText().toString();

            Pattern pattern = Patterns.EMAIL_ADDRESS;

            // 모든 항목 입력되었는지 확인
            if (!(hasTxt(loginEmail) && hasTxt(loginPW))) {
                showTxt = "모든 항목을 채워주세요";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                return;
            }

            // 이메일 유효성 검사
            else if (!pattern.matcher(loginEmailTxt).matches()) {
                showTxt = "올바른 이메일을 입력해주세요";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                return;
            }

            // 모든 조건이 충족되었을 경우
            loginAccount(loginEmailTxt, loginPWTxt);
        });

        // 회원가입 버튼 클릭
        sign.setOnClickListener(view -> {
            Intent intentSign = new Intent(getApplication(), SignUpActivity.class);
            startActivity(intentSign);
        });
    }

    private void loginAccount(String id, String pw) {
        mAuth.signInWithEmailAndPassword(id, pw)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String toastMsg = "아이디 또는 비밀번호를 확인해주세요.";
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("200", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        toastMsg = "로그인에 성공하였습니다!";
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                        Intent intentSign = new Intent(getApplication(), MainActivity.class);
                        intentSign.putExtra("uid", user.getUid());
                        startActivity(intentSign);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("404", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    // 백버튼 두번 클릭시 앱 종료
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }
    }
}