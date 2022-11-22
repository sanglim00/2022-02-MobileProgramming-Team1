package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    ImageView joinProfile;
    TextInputEditText joinEmail, joinPW, joinPWChk, joinName, joinPhone, joinPetName;
    Button dog, cat, fish, pig, plus, completion, back;
    RadioButton accept;
    public String showTxt, joinEmailTxt, joinPWTxt, joinPWChkTxt, joinNameTxt, joinPhoneTxt,
            joinPetNameTxt, petType, gender;
    String[] items = {"성별을 선택해주세요.", "여성", "남성", "성별 없음(또는 공개 안 함)"};
    public boolean joinCheckEmail, joinCheckPW, joinCheckPhone, joinBtnCheck, joinCheckGender;

    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 변수 초기화
        joinProfile = findViewById(R.id.iv_profile);
        joinEmail = findViewById(R.id.tit_email);
        joinPW = findViewById(R.id.tit_password);
        joinPWChk = findViewById(R.id.tit_passwordCheck);
        joinName = findViewById(R.id.tit_name);
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

        // pet type 저장
        dog.setOnClickListener(view -> {
            petType = "dog";
            showTxt = "🐶가 선택되었습니다";
            joinBtnCheck = true;
            Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
        });
        cat.setOnClickListener(view -> {
            petType = "cat";
            showTxt = "🐱가 선택되었습니다";
            joinBtnCheck = true;
            Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
        });
        fish.setOnClickListener(view -> {
            petType = "fish";
            showTxt = "🐟가 선택되었습니다";
            joinBtnCheck = true;
            Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
        });
        pig.setOnClickListener(view -> {
            petType = "pig";
            showTxt = "🐷가 선택되었습니다";
            joinBtnCheck = true;
            Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
        });
        // + 버튼 클릭 함수
        plus.setOnClickListener(view -> {
            joinBtnCheck = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Pet Type Add");
            alert.setMessage("추가할 Pet Type를 적어주세요");
            final EditText name = new EditText(this);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(8); //글자수 제한
            name.setFilters(FilterArray);
            alert.setView(name);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { //확인 버튼을 클릭했을때
                    String username = name.getText().toString();
                    plus.setText(username);
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("nickname", username);
                    editor.commit();
                }
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
                gender = items[position];
                joinCheckGender = true;
                Toast.makeText(getApplicationContext(), "성별이 선택되었습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = "";
            }
        });

        // 회원가입 완료하기 버튼 클릭 함수
        completion.setOnClickListener(view -> {
            // 문자열 추출
            joinEmailTxt = joinEmail.getText().toString();
            joinPWTxt = joinPW.getText().toString();
            joinPWChkTxt = joinPWChk.getText().toString();
            joinNameTxt = joinName.getText().toString();
            joinPhoneTxt = joinPhone.getText().toString();
            joinPetNameTxt = joinPetName.getText().toString();

            // 모든 항목이 채워져 있는지 확인
            if (!(hasTxt(joinEmail) && hasTxt(joinPW) && hasTxt(joinPWChk) && hasTxt(joinName) && hasTxt(joinPhone) && hasTxt(joinPetName))) {
                showTxt = "모든 항목을 채워주세요.";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
            } else if (!joinBtnCheck) {
                showTxt = "반려동물 종류를 선택해주세요";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
            } else if (!joinCheckGender) {
                showTxt = "성별을 선택해주세요";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
            } else if (!accept.isChecked()) {
                showTxt = "개인정보 이용약관 동의가 필요합니다";
                Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
            } else {
                // 이메일 유효성 검사
                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if (!pattern.matcher(joinEmailTxt).matches()){
                    showTxt = "올바른 이메일을 입력해주세요";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }

                // 비밀번호 유효성 검사(숫자, 특수문자가 포함)
                String symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
                // 비밀번호 유효성 검사(영문자, 대소문자 적어도 하나씩 포함)
                String alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";

                Pattern Psymbol = Pattern.compile(symbol);
                Pattern Palpha = Pattern.compile(alpha);

                Matcher Msymbol = Psymbol.matcher(joinPWTxt);
                Matcher Malpha = Palpha.matcher(joinPWTxt);

                // 비밀번호 5글자 이상 입력되었는지 확인
                if (joinPWTxt.length() < 5) {
                    joinCheckPW = false;
                    showTxt = "비밀번호는 5글자 이상 입력해주세요";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }

                // 비밀번호 유효성 검사
                if (!Msymbol.find() || !Malpha.find()) {
                    joinCheckPW = false;
                    showTxt = "비밀번호에 숫자, 특수문자, 대소문자가 포함되어야합니다";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }
                // 비밀번호와 비밀번호 확인 일치여부 확인
                if (!joinPWTxt.equals(joinPWChkTxt)){
                    joinCheckPW = false;
                    showTxt = "두 비밀번호가 다릅니다";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }

                // 전화번호 유효성 검사
                if (!Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", joinPhoneTxt)) {
                    joinCheckPhone = false;
                    showTxt = "올바른 전화번호를 입력해주세요";
                    Toast.makeText(getApplicationContext(), showTxt, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 뒤로가기 버튼 클릭시
        back.setOnClickListener(view -> {
            Intent intentSign = new Intent(getApplication(), LoginActivity.class);
            startActivity(intentSign);
            finish();
        });
    }

    // 백버튼 클릭 방지
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}