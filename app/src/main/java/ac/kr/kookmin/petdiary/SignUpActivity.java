package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.kr.kookmin.petdiary.models.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    CircleImageView joinProfile;
    ImageButton joinPfEdit;
    CalendarView joinMeetDate;
    TextView joinPrivacy,joinPrivacyTxt;
    TextInputLayout joinEmailBox, joinPWBox, joinPWChkBox, joinIDBox, joinPhoneBox, joinPetNameBox,
            joinTypeBox, joinGenderBox, joinDateBox, joinPrivacyBox;
    TextInputEditText joinEmail, joinPW, joinPWChk, joinID, joinPhone, joinPetName;
    Button dog, cat, fish, pig, plus, completion, back;
    RadioButton accept;
    public String showTxt, joinEmailTxt, joinPWTxt, joinPWChkTxt, joinIDTxt, joinPhoneTxt,
            joinPetNameTxt, joinPetType, joinGender, joinDate;
    String[] items = {"성별을 선택해주세요.", "남 (♂)", "여 (♀)", "공개 안 함"};
    public boolean joinCheckEmail, joinCheckPW, joinCheckPhone, joinBtnCheck, joinCheckGender, joinCheckDate, joinFocus;
    private final int CALL_GALLERY = 0;
    private int control;
    private Bitmap bit;
    private BitmapFactory.Options bitOption;
    boolean image_changed = false;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 변수 초기화
        mAuth = FirebaseAuth.getInstance();
        joinProfile = findViewById(R.id.iv_profile);
        joinPfEdit = findViewById(R.id.imgBtn_pf_edit_editimage);
        joinMeetDate = findViewById(R.id.cv_meetDate);
        joinPrivacy = findViewById(R.id.tv_privacyUsage);
        joinPrivacyTxt = findViewById(R.id.tv_privacyUsageText);
        joinEmailBox = findViewById(R.id.til_joinEmail);
        joinPWBox = findViewById(R.id.til_joinPassword);
        joinPWChkBox = findViewById(R.id.til_joinPasswordCheck);
        joinIDBox = findViewById(R.id.til_joinID);
        joinPhoneBox = findViewById(R.id.til_joinPhone);
        joinPetNameBox = findViewById(R.id.til_joinPetName);
        joinTypeBox = findViewById(R.id.til_joinPetType);
        joinGenderBox = findViewById(R.id.til_joinPetGender);
        joinDateBox = findViewById(R.id.til_joinMeetDate);
        joinPrivacyBox = findViewById(R.id.til_joinPrivacy);
        joinEmail = findViewById(R.id.tit_email);
        joinPW = findViewById(R.id.tit_password);
        joinPWChk = findViewById(R.id.tit_passwordCheck);
        joinID = findViewById(R.id.tit_id);
        joinPhone = findViewById(R.id.tit_phone);
        joinPetName = findViewById(R.id.tit_petName);
        Spinner spinner = findViewById(R.id.spinner_petGender);
        dog = findViewById(R.id.btn_petType1);
        cat = findViewById(R.id.btn_petType2);
        fish = findViewById(R.id.btn_petType3);
        pig = findViewById(R.id.btn_petType4);
        plus = findViewById(R.id.btn_petType5);
        accept = findViewById(R.id.btn_accept);
        completion = findViewById(R.id.btn_completion);
        back = findViewById(R.id.btn_back);
        showTxt = "";
        joinCheckEmail = true;
        joinCheckPW = true;
        joinCheckPhone = true;
        joinBtnCheck = false;
        joinCheckGender = false;
        joinCheckDate = false;
        joinFocus = false;
        joinDateBox.setFocusable(true);

        // 프로필 사진 변경 버튼
        joinPfEdit.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Profile Image Add");
            alert.setMessage("프로필 사진을 추가(변경)하시겠습니까?");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, CALL_GALLERY);
                }
            });
            alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        });

        // pet type 저장
        dog.setOnClickListener(view -> {
            joinPetType = "dog";
            joinBtnCheck = true;
        });
        cat.setOnClickListener(view -> {
            joinPetType = "cat";
            joinBtnCheck = true;
        });
        fish.setOnClickListener(view -> {
            joinPetType = "fish";
            joinBtnCheck = true;
        });
        pig.setOnClickListener(view -> {
            joinPetType = "pig";
            joinBtnCheck = true;
        });
        // + 버튼 클릭 함수
        plus.setOnClickListener(view -> {
            joinBtnCheck = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Pet Type Add");
            alert.setMessage("추가할 Pet Type를 적어주세요.");
            final EditText petType = new EditText(this);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(8); //글자수 제한
            petType.setFilters(FilterArray);
            alert.setView(petType);
            alert.setPositiveButton("확인", (dialog, whichButton) -> {
                String input = petType.getText().toString();
                plus.setText(input);
                joinPetType = input.toLowerCase();
                Toast.makeText(SignUpActivity.this, joinPetType, Toast.LENGTH_SHORT).show();
            });
            alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        });

        // 반려동물 성별 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                joinGender = items[position];
                if (Objects.equals(joinGender, "성별을 선택해주세요.")) {
                    joinCheckGender = false;
                } else {
                    joinCheckGender = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                joinGender = "";
            }
        });

        // 만난 날짜 설정 함수
        joinMeetDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            joinCheckDate = true;
            joinDate = String.format("%d/%d/%02d", year, month + 1, dayOfMonth);
        });

        // 개인정보 이용 동의 토글 함수
        joinPrivacy.setOnClickListener(view -> {
            control += 1;
            if (view==joinPrivacy && control%2==1) {
                joinPrivacyTxt.setVisibility(View.VISIBLE);
            } else {
                joinPrivacyTxt.setVisibility(View.GONE);
            }
        });

        // 회원가입 완료하기 버튼 클릭 함수
        completion.setOnClickListener(view -> {

            showTxt = "";
            // 문자열 추출
            joinEmailTxt = joinEmail.getText().toString().replaceAll("\\s", "");
            joinPWTxt = joinPW.getText().toString().replaceAll("\\s", "");
            joinPWChkTxt = joinPWChk.getText().toString().replaceAll("\\s", "");
            joinIDTxt = joinID.getText().toString().replaceAll("\\s", "");
            joinPhoneTxt = joinPhone.getText().toString().replaceAll("\\s", "");
            joinPetNameTxt = joinPetName.getText().toString().replaceAll("\\s", "");

            // 모든 항목이 채워져 있는지 확인
            if (!(hasTxt(joinEmail) && hasTxt(joinPW) && hasTxt(joinPWChk) && hasTxt(joinID)
                    && hasTxt(joinPhone) && hasTxt(joinPetName) && joinBtnCheck && joinCheckGender
                    && joinCheckDate)) {
                showTxt = "모든 항목을 채워주세요.";
                joinEmailBox.setError(showTxt);
                joinPWBox.setError(showTxt);
                joinPWChkBox.setError(showTxt);
                joinIDBox.setError(showTxt);
                joinPhoneBox.setError(showTxt);
                joinPetNameBox.setError(showTxt);
                joinTypeBox.setError(showTxt);
                joinGenderBox.setError(showTxt);
                joinDateBox.setError(showTxt);
                joinEmailBox.requestFocus();
                joinFocus = true;
                return;
            } else {
                joinEmailBox.setError(null);
                joinPWBox.setError(null);
                joinPWChkBox.setError(null);
                joinIDBox.setError(null);
                joinPhoneBox.setError(null);
                joinPetNameBox.setError(null);
                joinTypeBox.setError(null);
                joinGenderBox.setError(null);
                joinDateBox.setError(null);
                joinFocus = false;
            }

            if (!accept.isChecked()) {
                showTxt = "개인정보 이용약관 동의가 필요합니다.";
                joinPrivacyBox.setError(showTxt);
                if (!joinFocus) {
                    joinPrivacyBox.requestFocus();
                    joinFocus = true;
                }
                return;
            } else {
                joinPrivacyBox.setError(null);
                joinFocus = false;
            }

            // 이메일 유효성 검사
            Pattern pattern = Patterns.EMAIL_ADDRESS;

            // 비밀번호 유효성 검사(숫자, 특수문자가 포함)
            String symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
            // 비밀번호 유효성 검사(영문자, 대소문자 적어도 하나씩 포함)
            String alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";

            Pattern Psymbol = Pattern.compile(symbol);
            Pattern Palpha = Pattern.compile(alpha);

            Matcher Msymbol = Psymbol.matcher(joinPWTxt);
            Matcher Malpha = Palpha.matcher(joinPWTxt);

            if (!pattern.matcher(joinEmailTxt).matches()){
                joinCheckEmail = false;
                showTxt = "올바른 이메일을 입력해주세요.";
                joinEmailBox.setError(showTxt);
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinEmailBox.setError(null);
                joinFocus = false;
            }

            // 비밀번호 5글자 이상 입력되었는지 확인
            if (joinPWTxt.length() < 5) {
                joinCheckPW = false;
                showTxt = "비밀번호는 5글자 이상 입력해주세요.";
                joinPWBox.setError(showTxt);
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!Msymbol.find() || !Malpha.find()) {
                // 비밀번호 유효성 검사
                joinCheckPW = false;
                showTxt = "비밀번호에 숫자, 특수문자, 대소문자가 포함되어야합니다.";
                joinPWBox.setError(showTxt);
                joinPWBox.requestFocus();
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!joinPWTxt.equals(joinPWChkTxt)){
                // 비밀번호와 비밀번호 확인 일치여부 확인
                joinCheckPW = false;
                showTxt = "두 비밀번호가 다릅니다.";
                joinPWBox.setError(showTxt);
                joinPWChkBox.setError(showTxt);
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPWBox.setError(null);
                joinPWChkBox.setError(null);
                joinFocus = false;
            }

            // 전화번호 유효성 검사
            if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", joinPhoneTxt)) {
                joinCheckPhone = false;
                showTxt = "올바른 전화번호를 입력해주세요.";
                joinPhoneBox.setError(showTxt);
                if (!joinFocus) {
                    joinPhoneBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPhoneBox.setError(null);
                joinFocus = false;
            }

            if (!showTxt.equals("")) {
               return;
            }

            // 회원가입 성공시
            User user = new User(joinEmailTxt, joinIDTxt, joinPhoneTxt, joinPetNameTxt, joinPetType, joinGender, joinDate);
            signUp(joinEmailTxt, joinPWTxt, user);
        });

        // 뒤로가기 버튼 클릭시
        back.setOnClickListener(view -> {
            Intent intentSign = new Intent(getApplication(), LoginActivity.class);
            startActivity(intentSign);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CALL_GALLERY: // 프로필 사진 변경
                    Uri uri = data.getData();
                    try{
                        bit = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(uri), null, bitOption);
                        bit = Bitmap.createBitmap(bit);
                        joinProfile.setImageBitmap(bit);
                        image_changed = true;
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void signUp(String email, String pw, User user) {
        // 같은 계정으로 가입되어 있는게 있는지 체크
        db.collection("users").whereEqualTo("email", email).get()
            .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                Toast.makeText(SignUpActivity.this, "이미 회원가입 된 유저입니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        db.collection("users").whereEqualTo("userName", user.getUserName()).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        if (doc.exists()) {
                                            Toast.makeText(SignUpActivity.this, "이미 존재하는 아이디입니다. 다른 아이디를 사용해주세요.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    pushAccount(email, pw, user);
                                }
                            });
                    }
                }
            });

    }

    private void pushAccount(String email, String pw, User user) {
        mAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 성공 시,
                        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("201", "User DocumentSnapshot Id: " + mAuth.getCurrentUser().getUid());
                                        Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intentSign = new Intent(getApplication(), LoginActivity.class);
                                        startActivity(intentSign);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("500", "Error Adding User Document", e);
                                    }
                                });
                    } else {
                        // 실패 시,
                        Toast.makeText(SignUpActivity.this, "회원가입에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}