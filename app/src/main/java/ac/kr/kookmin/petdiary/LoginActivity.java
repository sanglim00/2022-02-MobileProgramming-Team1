package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import ac.kr.kookmin.petdiary.models.User;

public class LoginActivity extends AppCompatActivity {
    private long backpressedTime = 0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputLayout loginEmailBox,loginPWBox;
    TextInputEditText loginEmail, loginPW;
    Button login, sign;
    String showTxt, loginEmailTxt, loginPWTxt;
    public boolean loginCheckEmail;
    ProgressBar progressBar;

    // 이메일, 비밀번호 입력되었는지 확인
    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 변수 초기화
        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.tit_loginEmail);
        loginEmailBox = findViewById(R.id.til_loginEmail);
        loginPWBox = findViewById(R.id.til_loginPassword);
        loginPW = findViewById(R.id.tit_loginPassword);
        login = findViewById(R.id.btn_login);
        sign = findViewById(R.id.btn_signup);
        showTxt = "";
        loginCheckEmail = true;
        progressBar = findViewById(R.id.login_progress_bar);

        // 로그인 버튼 클릭
        login.setOnClickListener(view -> {
            loginEmailTxt = loginEmail.getText().toString().replaceAll("\\s", "");
            loginPWTxt = loginPW.getText().toString().replaceAll("\\s", "");

            Pattern pattern = Patterns.EMAIL_ADDRESS;

            // '이메일' 조건 확인
            if (!hasTxt(loginEmail)) {
                loginEmailBox.setError("항목을 채워주세요.");
            } else if (!pattern.matcher(loginEmailTxt).matches()) {
                loginEmailBox.setError("올바른 이메일을 입력해주세요.");
            } else {
                loginEmailBox.setError(null);
            }

            // '비밀번호' 조건 확인
            if (!hasTxt(loginPW)) {
                loginPWBox.setError("항목을 채워주세요.");
            } else {
                loginPWBox.setError(null);
            }

            // 위의 조건들 중 하나 이상에 걸렸을 경우
            if (!(loginEmailBox.getError() == null && loginPWBox.getError() == null)) {
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
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(id, pw)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    showTxt = "아이디 또는 비밀번호를 확인해주세요.";
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("200", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        fcmTokenRegisterAndStartMain(user.getUid());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("404", "signInWithEmail:failure", task.getException());
                        loginEmailBox.setError(showTxt);
                        loginPWBox.setError(showTxt);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
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

    private void fcmTokenRegisterAndStartMain(String uid) {
        final User[] user = new User[1];
        db.collection("users").document(uid).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user[0] = documentSnapshot.toObject(User.class);
                    String fcmToken = "";
                    SharedPreferences preferences = getSharedPreferences("fcmToken", Activity.MODE_PRIVATE);
                    if (preferences != null && preferences.contains("fcmToken"))
                        fcmToken = preferences.getString("fcmToken", "");
                    if (user[0] == null) {
                        mAuth.signOut();
                        Toast.makeText(LoginActivity.this, "사용자 정보를 확인할 수 없습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (fcmToken.length() != 0 && user[0].getFcmToken() != null && fcmToken.equals(user[0].getFcmToken())) {
                        Log.d("200", "fcm_token passed");
                    } else {
                        user[0].setFcmToken(fcmToken);
                        db.collection("users").document(uid).set(user[0])
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("500", "Failed Save New FCM Token", e);
                                    }
                                });
                    }
                    Intent intentSign = new Intent(getApplication(), MainActivity.class);
                    startActivity(intentSign);
                    finish();
                }
            });

    }
}