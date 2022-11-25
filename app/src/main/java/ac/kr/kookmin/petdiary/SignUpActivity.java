package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    CircleImageView joinProfile;
    ImageButton joinPfEdit;
    CalendarView joinMeetDate;
    TextInputEditText joinEmail, joinPW, joinPWChk, joinID, joinPhone, joinPetName;
    Button dog, cat, fish, pig, plus, completion, back;
    RadioButton accept;
    public String showTxt, joinEmailTxt, joinPWTxt, joinPWChkTxt, joinIDTxt, joinPhoneTxt,
            joinPetNameTxt, joinPetType, joinGender, joinDate;
    String[] items = {"성별을 선택해주세요", "남 (♂)", "여 (♀)", "공개 안 함"};
    public boolean joinCheckEmail, joinCheckPW, joinCheckPhone, joinBtnCheck, joinCheckGender, joinCheckDate;
    private final int CALL_GALLERY = 0;
    private Bitmap bit;
    private BitmapFactory.Options bitOption;
    boolean image_changed = false;

    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 변수 초기화
        joinProfile = findViewById(R.id.iv_profile);
        joinPfEdit = findViewById(R.id.imgBtn_pf_edit_editimage);
        joinMeetDate = findViewById(R.id.cv_meetDate);
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

        // 프로필 사진 변경 버튼
        joinPfEdit.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Profile Image Add");
            alert.setMessage("프로필 사진을 추가(변경)하시겠습니까?");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { //확인 버튼을 클릭했을때
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, CALL_GALLERY);
                }
            });
            alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { //취소 버튼을 클ㅣ
                }
            });
            alert.show();
        });

        // 이메일 엔터 방지
        joinEmail.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 비밀번호 엔터 방지
        joinPW.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 비밀번호 확인 엔터 방지
        joinPWChk.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 아이디 엔터 방지
        joinID.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 전화번호 엔터 방지
        joinPhone.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
        });

        // 반려동물 이름 엔터 방지
        joinPetName.setOnKeyListener((v, keyCode, event) -> {
            return KeyEvent.KEYCODE_ENTER == keyCode;
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
            alert.setMessage("추가할 Pet Type를 적어주세요");
            final EditText petType = new EditText(this);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(8); //글자수 제한
            petType.setFilters(FilterArray);
            alert.setView(petType);
            alert.setPositiveButton("확인", (dialog, whichButton) -> { //확인 버튼을 클릭했을때
                String input = petType.getText().toString();
                plus.setText(input);
                joinPetType = input;
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nickname", input);
                editor.commit();
            });
            alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { //취소 버튼을 클ㅣ
                }
            });
            alert.show();
        });

        // 반려동물 성별 스피너
        ArrayAdapter<String> adapter  = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (items[position] == "성별을 선택해주세요") {

                }
                joinGender = items[position];
                joinCheckGender = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                joinGender = "";
            }
        });

        // 만난 날짜 설정 함수
        joinMeetDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            joinCheckDate = true;
            joinDate = String.format("%d / %d / %d", year, month + 1, dayOfMonth);
        });

        // 회원가입 완료하기 버튼 클릭 함수
        completion.setOnClickListener(view -> {
            // 문자열 추출
            joinEmailTxt = joinEmail.getText().toString();
            joinPWTxt = joinPW.getText().toString();
            joinPWChkTxt = joinPWChk.getText().toString();
            joinIDTxt = joinID.getText().toString();
            joinPhoneTxt = joinPhone.getText().toString();
            joinPetNameTxt = joinPetName.getText().toString();


            // 모든 항목이 채워져 있는지 확인
            if (!(hasTxt(joinEmail) && hasTxt(joinPW) && hasTxt(joinPWChk) && hasTxt(joinID) && hasTxt(joinPhone) && hasTxt(joinPetName))) {
                showTxt = "모든 항목을 채워주세요.";
            } else if (!joinBtnCheck) {
                showTxt = "반려동물 종류를 선택해주세요";
            } else if (!joinCheckGender) {
                showTxt = "성별을 선택해주세요";
            } else if (!joinCheckDate) {
                showTxt = "만난 날짜를 선택해주세요";
            } else if (!accept.isChecked()) {
                showTxt = "개인정보 이용약관 동의가 필요합니다";
            }

            if (!showTxt.equals("")) {
                Toast.makeText(this, showTxt, Toast.LENGTH_SHORT).show();
                return;
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
                showTxt = "올바른 이메일을 입력해주세요";
            }

            // 비밀번호 5글자 이상 입력되었는지 확인
            else if (joinPWTxt.length() < 5) {
                joinCheckPW = false;
                showTxt = "비밀번호는 5글자 이상 입력해주세요";
            }

            // 비밀번호 유효성 검사
            else if (!Msymbol.find() || !Malpha.find()) {
                joinCheckPW = false;
                showTxt = "비밀번호에 숫자, 특수문자, 대소문자가 포함되어야합니다";
            }
            // 비밀번호와 비밀번호 확인 일치여부 확인
            else if (!joinPWTxt.equals(joinPWChkTxt)){
                joinCheckPW = false;
                showTxt = "두 비밀번호가 다릅니다";
            }

            // 전화번호 유효성 검사
            else if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", joinPhoneTxt)) {
                joinCheckPhone = false;
                showTxt = "올바른 전화번호를 입력해주세요";
            }

            if (!showTxt.equals("")) {
                Toast.makeText(this, showTxt, Toast.LENGTH_SHORT).show();
                return;
            }

            // 회원가입 성공시
            Intent intentSign = new Intent(getApplication(), LoginActivity.class);
            startActivity(intentSign);
            finish();
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
}