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
    String[] items = {"????????? ??????????????????.", "??? (???)", "??? (???)", "?????? ??? ???"};
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

        // ?????? ?????????
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
        now_date = new Date(now); // ?????? ??????
        progressBar = findViewById(R.id.signup_progress_bar);

        // ????????? ?????? ?????? ??????
        joinPfEdit.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Profile Image Add");
            alert.setMessage("????????? ????????? ??????(??????)???????????????????");
            alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    uploadImg();
                }
            });
            alert.setNegativeButton("??????",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        });

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

        // pet type ??????
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
        // + ?????? ?????? ??????
        plus.setOnClickListener(view -> {
            joinBtnCheck = true;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Pet Type Add");
            alert.setMessage("????????? Pet Type??? ???????????????.(15?????? ????????? ?????? ??????)");
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
            alert.setPositiveButton("??????", (dialog, whichButton) -> {
                String input = petType.getText().toString().replaceAll("\\s", "");
                plus.setText(input);
                joinPetType = input.toLowerCase();
            });
            alert.setNegativeButton("??????",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        });

        // ???????????? ?????? ?????????
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                joinGender = items[position];
                if (Objects.equals(joinGender, "????????? ??????????????????.")) {
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

        // ?????? ?????? ?????? ??????
        joinMeetDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            joinCheckDate = true;
            joinDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        // ???????????? ?????? ?????? ?????? ??????
        joinPrivacy.setOnClickListener(view -> {
            control += 1;
            if (view==joinPrivacy && control%2==1) {
                joinPrivacyTxt.setVisibility(View.VISIBLE);
            } else {
                joinPrivacyTxt.setVisibility(View.GONE);
            }
        });

        // ???????????? ???????????? ?????? ?????? ??????
        completion.setOnClickListener(view -> {
            // ????????? ?????????
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

            // ????????? ??????
            joinEmailTxt = joinEmail.getText().toString().replaceAll("\\s", "");
            joinPWTxt = joinPW.getText().toString().replaceAll("\\s", "");
            joinPWChkTxt = joinPWChk.getText().toString().replaceAll("\\s", "");
            joinIDTxt = joinID.getText().toString().replaceAll("\\s", "");
            joinPhoneTxt = joinPhone.getText().toString().replaceAll("\\s", "");
            joinPetNameTxt = joinPetName.getText().toString().replaceAll("\\s", "");

            // TextInputLayout Focus ?????? ?????? ?????????
            joinTypeBox.setFocusableInTouchMode(true);
            joinGenderBox.setFocusableInTouchMode(true);
            joinDateBox.setFocusableInTouchMode(true);
            joinPrivacyBox.setFocusableInTouchMode(true);

            // ????????? ????????? ??????(????????? ????????? ????????? ??????)
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(joinEmailTxt);

            // ???????????? ????????? ??????(??????, ??????????????? ??????)
            String symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
            // ???????????? ????????? ??????(?????????, ???????????? ????????? ????????? ??????)
            String alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";

            Pattern Psymbol = Pattern.compile(symbol);
            Pattern Palpha = Pattern.compile(alpha);

            Matcher Msymbol = Psymbol.matcher(joinPWTxt);
            Matcher Malpha = Palpha.matcher(joinPWTxt);

            // '?????????' ?????? ??????
            if (!hasTxt(joinEmail)){
                joinEmailBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!matcher.find()){
                joinEmailBox.setError("????????? ???????????? ??????????????????.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinEmailBox.setError(null);
                joinFocus = false;
            }

            // '????????????' ?????? ??????
            if (!hasTxt(joinPW)){
                joinPWBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (joinPWTxt.length() < 5) {
                joinPWBox.setError("??????????????? 5?????? ?????? ??????????????????.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!Msymbol.find() || !Malpha.find()) {
                joinPWBox.setError("??????????????? ??????, ????????????, ??????????????? ????????????????????????.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPWBox.setError(null);
                joinFocus = false;
            }

            // '???????????? ??????' ?????? ??????
            if (!hasTxt(joinPWChk)){
                joinPWChkBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinEmailBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!joinPWTxt.equals(joinPWChkTxt)){
                joinPWBox.setError("??? ??????????????? ????????????.");
                joinPWChkBox.setError("??? ??????????????? ????????????.");
                if (!joinFocus) {
                    joinPWBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPWBox.setError(null);
                joinPWChkBox.setError(null);
                joinFocus = false;
            }

            // '?????????' ?????? ??????
            if (!hasTxt(joinID)) {
                joinIDBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinIDBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinIDBox.setError(null);
                joinFocus = false;
            }

            // '????????????' ?????? ??????
            if (!hasTxt(joinPhone)) {
                joinPhoneBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinPhoneBox.requestFocus();
                    joinFocus = true;
                }
            } else if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", joinPhoneTxt)) {
                joinPhoneBox.setError("????????? ??????????????? ??????????????????.");
                if (!joinFocus) {
                    joinPhoneBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPhoneBox.setError(null);
                joinFocus = false;
            }

            // '???????????? ??????' ?????? ??????
            if (!hasTxt(joinPetName)) {
                joinPetNameBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinPetNameBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPetNameBox.setError(null);
                joinFocus = false;
            }

            // '???????????? ??????' ?????? ??????
            if (!joinBtnCheck) {
                joinTypeBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinTypeBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinTypeBox.setError(null);
                joinFocus = false;
            }

            // '???????????? ??????' ?????? ??????
            if (!joinCheckGender) {
                joinGenderBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinGenderBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinGenderBox.setError(null);
                joinFocus = false;
            }

            // '?????? ??????' ?????? ??????
            if (!joinCheckDate) {
                joinDateBox.setError("????????? ???????????????.");
                if (!joinFocus) {
                    joinDateBox.requestFocus();
                    joinFocus = true;
                }
            } else if(!dateCompare(joinDate, now_date)){
                joinDateBox.setError("??????????????? ?????? ????????? ?????? ????????? ????????? ??? ????????????.");
                if (!joinFocus) {
                    joinDateBox.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinCheckDate = true;
                joinDateBox.setError(null);
                joinFocus = false;
            }

            // '???????????? ?????? ??????' ??????
            if (!accept.isChecked()) {
                joinPrivacyBox.setError("???????????? ???????????? ????????? ???????????????.");
                if (!joinFocus) {
                    joinPrivacy.requestFocus();
                    joinFocus = true;
                }
            } else {
                joinPrivacyBox.setError(null);
                joinFocus = false;
            }

            // ?????? ????????? ??? ?????? ????????? ????????? ??????
            if (!(joinEmailBox.getError() == null && joinPWBox.getError() == null && joinPWChkBox.getError() == null
                    && joinIDBox.getError() == null && joinPhoneBox.getError() == null && joinPetNameBox.getError() == null
                    && joinTypeBox.getError() == null && joinGenderBox.getError() == null && joinDateBox.getError() == null
                    && joinPrivacyBox.getError() == null)) {
               return;
            }

            // ?????? ????????? ??????????????? ??????
            User user = new User(joinEmailTxt, joinIDTxt, joinPhoneTxt, joinPetNameTxt, joinPetType, joinGender, joinDate);
            signUp(joinEmailTxt, joinPWTxt, user);
        });

        // ???????????? ?????? ?????????
        back.setOnClickListener(view -> {
            Intent intentSign = new Intent(getApplication(), LoginActivity.class);
            startActivity(intentSign);
            finish();
        });
    }

    // ???????????? ??????????????? ???????????? ??????
    private boolean hasTxt(TextInputEditText et){
        return (et.getText().toString().trim().length() > 0);
    }

    // ????????? ?????? ???????????? ???????????? ??????
    private void uploadImg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????? ?????????").setMessage("?????? ????????? ???????????? ???????????? ????????? ????????????.");
        builder.setPositiveButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        });
        builder.setNegativeButton("????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                takeAlbum();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // ?????? ?????? ??????
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    // ?????? ?????? ?????? ??????
    public void takeAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    // ????????? ?????? ????????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // ???????????? ?????? ???
        if(requestCode == 0 && resultCode == RESULT_OK) {
            Glide.with(getApplicationContext()).load(data.getData()).override(360, 360).into(joinProfile);
            isImageSelected = true;
        }
        // ????????? ???????????? ?????? ???
        else if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); // Bundle??? ???????????? ??????
            Bitmap imageBitmap = (Bitmap) extras.get("data"); // Bitmap?????? ?????????
            joinProfile.setImageBitmap(imageBitmap);  // ??????????????? Bitmap?????? ???????????? ??????
            isImageSelected = true;
        }
        else {
            isImageSelected = false;
        }
    }

    // ?????? ?????? ??????
    public boolean dateCompare(String str1, Date now_date){ // ????????? ??????, ?????? ??????
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String now_str = dateFormat1.format(now_date); // date to String (format ??????)
        Date date = null;
        Date date_now = null;
        try{
            date_now = dateFormat1.parse(now_str); // string to date(now, format ??????)
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

    // ???????????? ??????
    private void signUp(String email, String pw, User user) {
        progressBar.setVisibility(View.VISIBLE);
        // ?????? ???????????? ???????????? ????????? ????????? ??????
        db.collection("users").whereEqualTo("email", email).get()
            .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                Toast.makeText(SignUpActivity.this, "?????? ???????????? ??? ???????????????. ????????? ????????????.", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(SignUpActivity.this, "?????? ???????????? ??????????????????. ?????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
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

    // ?????? ?????? ??????
    private void pushAccount(String email, String pw, User user) {
        mAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // ?????? ???,
                        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("201", "User DocumentSnapshot Id: " + mAuth.getCurrentUser().getUid());
                                        if(isImageSelected) uploadProfile(mAuth.getCurrentUser().getUid());
                                        else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SignUpActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
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
                        // ?????? ???,
                        Toast.makeText(SignUpActivity.this, "??????????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignUpActivity.this, "????????? ?????? ???????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SignUpActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                Intent intentSign = new Intent(getApplication(), LoginActivity.class);
                startActivity(intentSign);
                finish();
            }
        });
    }

}