package ac.kr.kookmin.petdiary;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;


public class Profile_EditActivity extends AppCompatActivity {
    String[] genders = {"남 (♂)", "여 (♀)", "기타"};
    boolean complete = false;
    TextView et_edit_meetDate;
    ImageView img_pf;
    private final int CALL_GALLERY = 0;
    private Bitmap bit;
    private BitmapFactory.Options bitOption;
    boolean image_changed = false;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        checkSelfPermission();

        bitOption = new BitmapFactory.Options();
        bitOption.inSampleSize = 4;


        img_pf = findViewById(R.id.img_pf);
        ImageButton imgBtn_edit_editimage = findViewById(R.id.imgBtn_pf_edit_editimage);


        EditText et_edit_name = findViewById(R.id.et_pf_edit_name); // 이름 편집 EditText
        Spinner spinner_gender = findViewById(R.id.spiner_pf_gender); // 성별 선택 Spinner
        TextView txt_gender = findViewById(R.id.txt_pf_edit_gender); // 성별 선택 txt
        et_edit_meetDate = findViewById(R.id.txt_pf_edit_meetDate); // 만난 날짜 편집 Text
        EditText et_edit_one_line_info = findViewById(R.id.et_pf_edit_one_line_info); // 한줄 소개 편집 EditText

        ImageButton imgBtn_pf_calendar = findViewById(R.id.imgBtn_pf_calendar); // 만난 날짜 편집 ImageButton
        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼

        Button btn_edit_complete = findViewById(R.id.btn_pf_edit_complete); // 편집 완료 버튼



        Intent editIntent = getIntent();
        String nameIntent = editIntent.getStringExtra("name");
        String genderIntent =  editIntent.getStringExtra("gender");
        String meetDateIntent = editIntent.getStringExtra("meetDate");
        String oneLineIntent = editIntent.getStringExtra("one_line");



        et_edit_name.setText(nameIntent);
        txt_gender.setText(genderIntent);
        et_edit_meetDate.setText(meetDateIntent);
        et_edit_one_line_info.setText(oneLineIntent); // 기존값 받아옴



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation); // footer
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_one:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_two:
                        return true;
                    case R.id.action_three:
                        intent = new Intent(getApplicationContext(), WritingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_four:
                        intent = new Intent(getApplicationContext(), NotiActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_five:
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });



        imgBtn_edit_editimage.setOnClickListener(new View.OnClickListener(){ // 프로필 사진 변경 버튼
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, CALL_GALLERY);
            }
        });



        imgBtn_pf_calendar.setOnClickListener(new View.OnClickListener() { // 만난 날짜 수정 버튼
            @Override
            public void onClick(View view) {
                Intent intent_calendar = new Intent(Profile_EditActivity.this, calendarActivity.class);
                startActivityForResult(intent_calendar,102);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>( // 성별 선택 Spinner Adapter
                this, android.R.layout.simple_spinner_item,genders);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner_gender.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 성별 선택 Spinner item 선택
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                txt_gender.setText(genders[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_gender.setText("");
            }

        });



        btn_edit_complete.setOnClickListener(new View.OnClickListener() { // 편집 완료 버튼 onclick
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("image_changed", image_changed);
                // 프로필 이미지 변경 여부 - 프로필 이미지 변경을 안하고 완료 시 intent 용량이 초과되어 강제종료 되는 듯하여 이미지 변경시에만 인텐트를 주고 받도록 하였습니다.
                complete = true; // 완료 버튼을 눌렀을 때만 수정 사항이 반영되도록 함.
                if (image_changed){

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    Bitmap bitmap = ((BitmapDrawable) img_pf.getDrawable()).getBitmap();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("image", byteArray); // 이미지 전달

                }
                intent.putExtra("name", et_edit_name.getText().toString()); // 이름 설정
                intent.putExtra("gender", txt_gender.getText().toString()); // 성별 설정
                intent.putExtra("meetDate", et_edit_meetDate.getText().toString()); // 만난 날짜 설정
                intent.putExtra("one_line", et_edit_one_line_info.getText().toString()); // 한줄 프로필 설정
                intent.putExtra("complete", complete);
                setResult(0, intent);
                finish();
            }
        });




        imgBtn_setting.setOnClickListener(new View.OnClickListener() { // 환경설정 버튼 onclick
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_EditActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }





    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }


    }

    private void checkSelfPermission() {
        String temp = "";

        // 파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + "";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
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
                        img_pf.setImageBitmap(bit);
                        image_changed = true;
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    break;

                case 102:
                    boolean getcomplete_date = data.getBooleanExtra("complete_date", false);
                    if(getcomplete_date){
                        String getMeetDate = data.getStringExtra("new_date");
                        et_edit_meetDate.setText(getMeetDate);
                    }
                    break;
            }

        }

    }

    public boolean onKeyDown(int keycode, KeyEvent event){
        if(keycode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("image_changed", false);
            intent.putExtra("complete", false);
            setResult(0,intent);
            finish();
            return true;
        }
        return false;
    }



}