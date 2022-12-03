package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ac.kr.kookmin.petdiary.models.User;

public class SplashActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        if (mAuth.getCurrentUser() != null) {
            isActive = true;
            fcmTokenRegister(mAuth.getCurrentUser().getUid());
        }
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초

    }

    private class splashhandler implements Runnable{
        public void run(){
            if (isActive) startActivity(new Intent(getApplication(), MainActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            else startActivity(new Intent(getApplication(), LoginActivity.class));
            SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

    private void fcmTokenRegister(String uid) {
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
                            Toast.makeText(SplashActivity.this, "사용자 정보를 확인할 수 없습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplication(), LoginActivity.class));
                        }
                        if (fcmToken.length() != 0 && user[0].getFcmToken() != null && fcmToken.equals(user[0].getFcmToken())) {
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
                    }
                });

    }
}

