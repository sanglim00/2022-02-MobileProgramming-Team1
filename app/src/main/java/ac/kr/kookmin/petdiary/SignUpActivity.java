package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public String joinEmailTxt, joinPWTxt, joinPWChkTxt, joinIDTxt, joinPhoneTxt,
            joinPetNameTxt, joinPetType, joinGender, joinDate;
    String[] items = {"성별을 선택해주세요.", "남 (♂)", "여 (♀)", "공개 안 함"};
    boolean isImageSelected, joinBtnCheck, joinCheckGender, joinCheckDate, joinFocus;
    int control;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    File file;
    private Bitmap bit;
    private BitmapFactory.Options bitOption;
    Date now_date;
    ProgressBar progressBar;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

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
        isImageSelected = false;
        joinBtnCheck = false;
        joinCheckGender = false;
        joinCheckDate = false;
        joinFocus = false;
        joinDateBox.setFocusable(true);
        control = 0;
        bitOption = new BitmapFactory.Options();
        bitOption.inSampleSize = 4;
        long now = System.currentTimeMillis();
        now_date = new Date(now); // 현재 날짜
        progressBar = findViewById(R.id.signup_progress_bar);

        // 프로필 사진 변경 버튼
        joinPfEdit.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Profile Image Add");
            alert.setMessage("프로필 사진을 추가(변경)하시겠습니까?");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    uploadImg();
                }
            });
            alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        });

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

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
            alert.setMessage("추가할 Pet Type을 적어주세요.(15글자 이하만 입력 가능)");
            final EditText petType = new EditText(this);
            petType.addTextChangedListener(new TextWatcher() {
                String maxText = "";
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    maxText = charSequence.toString();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(petType.getLineCount() > 1){
                        petType.setText(maxText);
                        petType.setSelection(petType.length());
                    }
                    if(petType.length() > 16){
                        petType.setText(maxText);
                        petType.setSelection(petType.length());
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            alert.setView(petType);
            alert.setPositiveButton("확인", (dialog, whichButton) -> {
                String input = petType.getText().toString().replaceAll("\\s", "");
                plus.setText(input);
                joinPetType = input.toLowerCase();
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
            joinDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
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
            // 포커스 초기화
            joinFocus = false;
            joinEmailBox.clearFocus();
            joinPWBox.clearFocus();
            joinPWChkBox.clearFocus();
            joinIDBox.clearFocus();
            joinPhoneBox.clearFocus();
            joinPetNameBox.clearFocus();
            joinTypeBox.clearFocus();
            joinGenderBox.clearFocus();
            joinDateBox.clearFocus();
            joinPrivacyBox.clearFocus();

            // 문자열 추출
            joinEmailTxt = joinEmail.getText().toString().replaceAll("\\s", "");
            joinPWTxt = joinPW.getText().toString().replaceAll("\\s", "");
            joinPWChkTxt = joinPWChk.getText().toString().replaceAll("\\s", "");
            joinIDTxt = joinID.getText().toString().replaceAll("\\s", "");
            joinPhoneTxt = joinPhone.getText().toString().replaceAll("\\s", "");
            joinPetNameTxt = joinPetName.getText().toString().replaceAll("\\s", "");

            // TextInputLayout Focus 주기 위한 초기화
            joinTypeBox.setFocusableInTouchMode(true);
            joinGenderBox.setFocusableInTouchMode(true);
            joinDateBox.setFocusableInTouchMode(true);
            joinPrivacyBox.setFocusableInTouchMode(true);

            // 이메일 유효성 검사(이메일 형식이 맞는지 확인)
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(joinEmailTxt);

            // 비밀번호 유효성 검사(숫자, 특수문자가 포함)
            String symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
            // 비밀번호 유효성 검사(영문자, 대소문자 적어도 하나씩 포함)
            String alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";

            Pattern Psymbol = Pattern.compile(symbol);
            Pattern Palpha = Pattern.compile(alpha);

            Matcher Msymbol = Psymbol.matcher(joinPWTxt);
            Matcher Malpha = Palpha.matcher(joinPWTxt);

            // '이메일' 조건 확인
            if (!hasTxt(joinEmail)){
                joinEmailBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!matcher.find()){
                joinEmailBox.setError("올바른 이메일을 입력해주세요.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinEmailBox.setError(null);
                joinFocus = false;
            }

            // '비밀번호' 조건 확인
            if (!hasTxt(joinPW)){
                joinPWBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (joinPWTxt.length() < 5) {
                joinPWBox.setError("비밀번호는 5글자 이상 입력해주세요.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!Msymbol.find() || !Malpha.find()) {
                joinPWBox.setError("비밀번호에 숫자, 특수문자, 대소문자가 포함되어야합니다.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPWBox.setError(null);
                joinFocus = false;
            }

            // '비밀번호 확인' 조건 확인
            if (!hasTxt(joinPWChk)){
                joinPWChkBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!joinPWTxt.equals(joinPWChkTxt)){
                joinPWBox.setError("두 비밀번호가 다릅니다.");
                joinPWChkBox.setError("두 비밀번호가 다릅니다.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPWBox.setError(null);
                joinPWChkBox.setError(null);
                joinFocus = false;
            }

            // '아이디' 조건 확인
            if (!hasTxt(joinID)) {
                joinIDBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinIDBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinIDBox.setError(null);
                joinFocus = false;
            }

            // '전화번호' 조건 확인
            if (!hasTxt(joinPhone)) {
                joinPhoneBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinPhoneBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", joinPhoneTxt)) {
                joinPhoneBox.setError("올바른 전화번호를 입력해주세요.");
                if (!joinFocus) {
                    joinPhoneBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPhoneBox.setError(null);
                joinFocus = false;
            }

            // '반려동물 이름' 조건 확인
            if (!hasTxt(joinPetName)) {
                joinPetNameBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinPetNameBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPetNameBox.setError(null);
                joinFocus = false;
            }

            // '반려동물 종류' 조건 확인
            if (!joinBtnCheck) {
                joinTypeBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinTypeBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinTypeBox.setError(null);
                joinFocus = false;
            }

            // '반려동물 성별' 조건 확인
            if (!joinCheckGender) {
                joinGenderBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinGenderBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinGenderBox.setError(null);
                joinFocus = false;
            }

            // '만난 날짜' 조건 확인
            if (!joinCheckDate) {
                joinDateBox.setError("항목을 채워주세요.");
                if (!joinFocus) {
                    joinDateBox.requestFocus();
                    joinFocus = true;
                }
            } else if(!dateCompare(joinDate, now_date)){
                joinDateBox.setError("반려동물과 만난 날짜는 오늘 이후로 설정할 수 없습니다.");
                if (!joinFocus) {
                    joinDateBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinCheckDate = true;
                joinDateBox.setError(null);
                joinFocus = false;
            }

            // '개인정보 동의 여부' 확인
            if (!accept.isChecked()) {
                joinPrivacyBox.setError("개인정보 이용약관 동의가 필요합니다.");
                if (!joinFocus) {
                    joinPrivacy.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPrivacyBox.setError(null);
                joinFocus = false;
            }

            // 위의 조건들 중 하나 이상에 걸렸을 경우
            if (!(joinEmailBox.getError() == null && joinPWBox.getError() == null && joinPWChkBox.getError() == null
                    && joinIDBox.getError() == null && joinPhoneBox.getError() == null && joinPetNameBox.getError() == null
                    && joinTypeBox.getError() == null && joinGenderBox.getError() == null && joinDateBox.getError() == null
                    && joinPrivacyBox.getError() == null)) {
               return;
            }

            // 모든 조건이 충족되었을 경우
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

    // 비어있는 문자열인지 확인하는 함수
    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    // 프로필 사진 업로드시 작동하는 함수
    private void uploadImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 업로드").setMessage("아래 버튼을 클릭하여 이미지를 업로드 해주세요.");
        builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        });
        builder.setNegativeButton("앨범선택", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                takeAlbum();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 사진 촬영 함수
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    // 앨범 사진 선택 함수
    public void takeAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    // 프로필 사진 업로드 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // 앨범에서 선택 시
        if(requestCode == 0 && resultCode == RESULT_OK) {
            Glide.with(getApplicationContext()).load(data.getData()).override(360, 360).into(joinProfile);
            isImageSelected = true;
        }
        // 카메라 구동하여 선택 시
        else if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); // Bundle로 데이터를 입력
            Bitmap imageBitmap = (Bitmap) extras.get("data"); // Bitmap으로 컨버전
            joinProfile.setImageBitmap(imageBitmap);  // 이미지뷰에 Bitmap으로 이미지를 입력
            isImageSelected = true;
        }
        else {
            isImageSelected = false;
        }
    }

    // 날짜 비교 함수
    public boolean dateCompare(String str1, Date now_date){ // 비교할 날짜, 현재 날짜
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String now_str = dateFormat1.format(now_date); // date to String (format 조정)
        Date date = null;
        Date date_now = null;
        try{
            date_now = dateFormat1.parse(now_str); // string to date(now, format 조정)
            date = dateFormat1.parse(str1); // string to date
        } catch (ParseException e){
            e.printStackTrace();
        }
        int compare = date_now.compareTo(date);

        if (compare < 0) {
            return false;
        }
        else{
            return true;
        }
    }

    // 회원가입 함수
    private void signUp(String email, String pw, User user) {
        progressBar.setVisibility(View.VISIBLE);
        // 같은 계정으로 가입되어 있는게 있는지 체크
        db.collection("users").whereEqualTo("email", email).get()
            .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                Toast.makeText(SignUpActivity.this, "이미 회원가입 된 유저입니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
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
                                            progressBar.setVisibility(View.INVISIBLE);
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

    // 계정 생성 함수
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
                                        if(isImageSelected) uploadProfile(mAuth.getCurrentUser().getUid());
                                        else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intentSign = new Intent(getApplication(), LoginActivity.class);
                                            startActivity(intentSign);
                                            finish();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.w("500", "Error Adding User Document", e);
                                    }
                                });
                    } else {
                        // 실패 시,
                        Toast.makeText(SignUpActivity.this, "회원가입에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
    }

    private void uploadProfile(String uid) {
        StorageReference profile = FirebaseStorage.getInstance().getReference().child("profiles/" + uid);
        joinProfile.setDrawingCacheEnabled(true);
        joinProfile.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) joinProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        profile.putBytes(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "프로필 사진 업로드에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intentSign = new Intent(getApplication(), LoginActivity.class);
                startActivity(intentSign);
                finish();
            }
        });
    }

}