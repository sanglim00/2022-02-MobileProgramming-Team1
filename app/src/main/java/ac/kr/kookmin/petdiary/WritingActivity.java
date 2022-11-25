package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import ac.kr.kookmin.petdiary.models.Post;


public class WritingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView uploadImg;
    Button uploadImgBtn;
    EditText postContents;

    boolean isImageSelected = false;

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

        postContents = findViewById(R.id.et_postContents);


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
        if(requestCode == 0 && requestCode == 0) {
            // load: 가져올 이미지, override: 이미지 크기 조정, into: 이미지를 출력할 객체
            Glide.with(getApplicationContext()).load(data.getData()).override(360, 360).into(uploadImg);
            isImageSelected = true;
        }
    }

    public void UploadPost(View view) {
        if (!isImageSelected) {
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (postContents.getText().toString().length() == 0) {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        Post post = new Post(mAuth.getCurrentUser().getUid(), "", postContents.getText().toString(), true);
        db.collection("posts").add(post)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("201", "Post DocumentSnapshot Id: " + documentReference.getId());

                    StorageReference storeageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storeageRef.child("images/" + documentReference.getId());
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
                            // 사진 업로드 실패 시,
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // 사진 업로드 성공 시,
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

}