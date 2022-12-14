package ac.kr.kookmin.petdiary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class Profile_EditActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String[] genders = {"공개 안 함","남 (♂)", "여 (♀)"};
    TextView et_edit_meetDate;
    ImageView img_pf;
    private final int CALL_GALLERY = 0;
    private Bitmap bit;
    private BitmapFactory.Options bitOption;
    boolean image_changed = false;
    String txt_gender = "";
    String uid;

    ImageButton openSetting;

    RadioGroup footer;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        checkSelfPermission();

        progressBar = findViewById(R.id.profile_edit_progress_bar);

        bitOption = new BitmapFactory.Options();
        bitOption.inSampleSize = 4;


        openSetting = findViewById(R.id.imgBtn_setting);
        openSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        img_pf = findViewById(R.id.img_pf);
        ImageButton imgBtn_edit_editimage = findViewById(R.id.imgBtn_pf_edit_editimage);

        TextView txt_pf_id = findViewById(R.id.txt_pf_id);
        EditText et_edit_name = findViewById(R.id.et_pf_edit_name); // 이름 편집 EditText
        Spinner spinner_gender = findViewById(R.id.spiner_pf_gender); // 성별 선택 Spinner
        et_edit_meetDate = findViewById(R.id.txt_pf_edit_meetDate); // 만난 날짜 편집 Text
        EditText et_edit_one_line_info = findViewById(R.id.et_pf_edit_one_line_info); // 한줄 소개 편집 EditText

        ImageButton imgBtn_pf_calendar = findViewById(R.id.imgBtn_pf_calendar); // 만난 날짜 편집 ImageButton
        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼

        Button btn_edit_complete = findViewById(R.id.btn_pf_edit_complete); // 편집 완료 버튼



        Intent editIntent = getIntent();
        String idIntent = editIntent.getStringExtra("id");
        String nameIntent = editIntent.getStringExtra("name");
        String genderIntent =  editIntent.getStringExtra("gender");
        String meetDateIntent = editIntent.getStringExtra("meetDate");
        String oneLineIntent = editIntent.getStringExtra("one_line");
        uid = editIntent.getStringExtra("uid");


        txt_pf_id.setText(idIntent);
        et_edit_name.setText(nameIntent);
        et_edit_meetDate.setText(meetDateIntent);
        et_edit_one_line_info.setText(oneLineIntent); // 기존값 받아옴

        storage.getReference().child("profiles/" + uid).getDownloadUrl()
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        if (Profile_EditActivity.this.isFinishing()) return;
                        Glide.with(getApplicationContext())
                                .load(task.getResult())
                                .into(img_pf);

                    } else {
                        if (Profile_EditActivity.this.isFinishing()) return;
                        Glide.with(getApplicationContext())
                                .load(R.drawable.default_profile)
                                .into(img_pf);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });




        imgBtn_edit_editimage.setOnClickListener(new View.OnClickListener(){ // 프로필 사진 변경 버튼
            @Override
            public void onClick(View v){
                UploadImg();
            }
        });

        et_edit_one_line_info.addTextChangedListener(new TextWatcher() {
            String maxText = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                maxText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(et_edit_one_line_info.getLineCount() > 2){
                    et_edit_one_line_info.setText(maxText);
                    et_edit_one_line_info.setSelection(et_edit_one_line_info.length());
                }
                if(et_edit_one_line_info.length() > 20){
                    et_edit_one_line_info.setText(maxText);
                    et_edit_one_line_info.setSelection(et_edit_one_line_info.length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_edit_name.addTextChangedListener(new TextWatcher() {
            String maxText = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                maxText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(et_edit_name.getLineCount() > 1){
                    et_edit_name.setText(maxText);
                    et_edit_name.setSelection(et_edit_name.length());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        imgBtn_pf_calendar.setOnClickListener(new View.OnClickListener() { // 만난 날짜 수정 버튼
            @Override
            public void onClick(View view) {
                Intent intent_calendar = new Intent(Profile_EditActivity.this, calendarActivity.class);
                intent_calendar.putExtra("current_date", et_edit_meetDate.getText().toString());
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
                txt_gender = (genders[position]).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_gender = ("공개 안함");
            }

        });


        if(genderIntent.equals("공개 안 함")){
            spinner_gender.setSelection(0);
            txt_gender = genders[0];
        }else if(genderIntent.equals("남 (♂)")){
            spinner_gender.setSelection(1);
            txt_gender = genders[1];
        }else if(genderIntent.equals("여 (♀)")){
            spinner_gender.setSelection(2);
            txt_gender = genders[2];
        }


        btn_edit_complete.setOnClickListener(new View.OnClickListener() { // 편집 완료 버튼 onclick
            @Override
            public void onClick(View view) {
                boolean editflag = true;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String name = et_edit_name.getText().toString();
                String gender = txt_gender;
                String date = et_edit_meetDate.getText().toString();
                String comment = et_edit_one_line_info.getText().toString();
                if (name == null || name.equals("")) {
                    Toast.makeText(Profile_EditActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    editflag = false;
                    return;
                } else if(name.length() >= 15){
                    Toast.makeText(Profile_EditActivity.this, "이름은 15글자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    editflag = false;
                    return;
                } else if (date == null || date.equals("")) {
                    Toast.makeText(Profile_EditActivity.this, "만난 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    editflag = false;
                    return;
                }

                if(editflag){
                    progressBar.setVisibility(View.VISIBLE);


                    db.collection("users").document(uid).update(
                            "petName", name,
                            "gender", gender,
                            "comment", comment,
                            "petBirth", date
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (image_changed) {
                                updateImage();
                            } else {
                                Intent intent = new Intent();
                                setResult(0, intent);
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile_EditActivity.this, "변경에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }else{
                    return;
                }

            }
        });


        imgBtn_setting.setOnClickListener(new View.OnClickListener() { // 환경설정 버튼 onclick
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_EditActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        RadioButton menu = findViewById(R.id.menu_user);
        menu.setChecked(true);
        footer = findViewById(R.id.footer_menu);
        footer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                RadioButton menu = radioGroup.findViewById(i);
                if(!menu.getText().equals("유저")) finish();

                if(menu.getText().equals("메인")) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("검색")) {
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("작성")) {
                    intent = new Intent(getApplicationContext(), WritingActivity.class);
                    startActivity(intent);
                }
                else if(menu.getText().equals("알림")) {
                    intent = new Intent(getApplicationContext(), NotiActivity.class);
                    startActivity(intent);
                }

                menu = findViewById(R.id.menu_user);
                menu.setChecked(true);
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
                case 1 :    // 카메라 촬영 시
                    Bundle extras = data.getExtras(); // Bundle로 데이터를 입력
                    Bitmap imageBitmap = (Bitmap) extras.get("data"); // Bitmap으로 컨버전
                    img_pf.setImageBitmap(imageBitmap);  // 이미지뷰에 Bitmap으로 이미지를 입력
                    image_changed = true;
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

    // 이미지 업로드 방법 선택을 위한 Dialog
    private void UploadImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 업로드").setMessage("아래 버튼을 클릭하여 이미지를 업로드 해주세요.");
        builder.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TakePhoto();
            }
        });
        builder.setNegativeButton("앨범선택", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                TakeAlbum();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    // 사진 촬영 후 업로드 시
    public void TakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    // 앨범에서 사진 선택 시
    public void TakeAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    private void updateImage() {
        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("profiles/" + uid);
        img_pf.setDrawingCacheEnabled(true);
        img_pf.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img_pf.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        imageRef.putBytes(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 사진 업로드 실패 시,
                Toast.makeText(Profile_EditActivity.this, "프로필 사진 변경에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Intent intent = new Intent();
                setResult(0, intent);
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }
        });
    }


}