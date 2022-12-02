package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import ac.kr.kookmin.petdiary.models.Post;
import ac.kr.kookmin.petdiary.models.User;


public class WritingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ImageView uploadImg;
    Button uploadImgBtn;
    EditText postContents;
    CheckBox Download;

    File file;

    boolean isImageSelected = false;    // 이미지 유무 여부
    boolean permitToDownload = false;   // 사진 다운로드 허용 여부

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        mAuth = FirebaseAuth.getInstance();
        uploadImg = findViewById( R.id.Img_upload);
        uploadImgBtn = findViewById(R.id.btn_uploadImg);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImg();
            }
        });

        progressBar = findViewById(R.id.writing_progress_bar);

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

        postContents = findViewById(R.id.et_postContents);
        Download = findViewById(R.id.ck_download);


        postContents.addTextChangedListener(new TextWatcher() {
            String maxText = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                maxText = charSequence.toString();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(postContents.getLineCount() > 50){
                    Toast.makeText(WritingActivity.this,"최대 50줄까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    postContents.setText(maxText);
                    postContents.setSelection(postContents.length());
                }
                if(postContents.length() > 500){
                    Toast.makeText(WritingActivity.this,"최대 500글자까지 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    postContents.setText(maxText);
                    postContents.setSelection(postContents.length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Download.isChecked()) {
                    Toast.makeText(getApplicationContext(), "다른 유저가 내 사진을 다운받을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    permitToDownload = true;
                }
                else{
                    Toast.makeText(getApplicationContext(), "다른 유저가 내 사진을 다운받지 못합니다.", Toast.LENGTH_SHORT).show();
                    permitToDownload = false;
                }
           }
        });

    }
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


    public void TakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    public void TakeAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 앨범에서 선택 시
        if(requestCode == 0 && resultCode == RESULT_OK) {
            // load: 가져올 이미지, override: 이미지 크기 조정, into: 이미지를 출력할 객체
            Glide.with(getApplicationContext()).load(data.getData()).override(360, 360).into(uploadImg);
            isImageSelected = true;
        }
        // 카메라 구동하여 선택 시
        else if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras(); // Bundle로 데이터를 입력
            Bitmap imageBitmap = (Bitmap) extras.get("data"); // Bitmap으로 컨버전
            uploadImg.setImageBitmap(imageBitmap);  // 이미지뷰에 Bitmap으로 이미지를 입력
            isImageSelected = true;
        }
        else {
            isImageSelected = false;
        }
    }

    public void UploadPost(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (!isImageSelected) {
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        } else if (postContents.getText().toString().length() == 0) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }else if(isImageSelected && postContents.getText().toString().length() != 0){
            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    Post post = new Post(mAuth.getCurrentUser().getUid(), postContents.getText().toString(), permitToDownload, user.getPetType(), new Timestamp(new Date()));
                    db.collection("posts").add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("201", "Post DocumentSnapshot Id: " + documentReference.getId());

                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    StorageReference imageRef = storageRef.child("images/" + documentReference.getId());
                                    uploadImg.setDrawingCacheEnabled(true);
                                    uploadImg.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) uploadImg.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask uploadTask = imageRef.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            // 사진 업로드 실패 시,
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // 사진 업로드 성공 시,
                                            pushNotification(mAuth.getCurrentUser().getUid(), documentReference.getId());
                                            Toast.makeText(WritingActivity.this, "업로드가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("500", "Error Adding Post Document", e);
                                    Toast.makeText(WritingActivity.this, "게시물 올리기에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

        }
    }

    public void pushNotification(String uid, String postId) {
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    HttpURLConnection conn;
                    URL url = new URL("http://20.249.4.187/api/subscribe/new?uid=" + uid + "&postId=" + postId);
                    Log.d("url", url.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(100000);
                    conn.setReadTimeout(100000);

                    // 타입설정
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");

                    // InputStream으로 서버로 부터 응답을 받겠다는 옵션
                    conn.setDoInput(true);

                    // 실제 서버로 Request 요청 하는 부분 (응답 코드를 받음, 200은 성공, 나머지 에러)
                    int response = conn.getResponseCode();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}