package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;


public class WritingActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    ImageView uploadImg;
    Button uploadImgBtn;
    EditText postContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        uploadImg = findViewById( R.id.Img_upload);
        uploadImgBtn = findViewById(R.id.btn_uploadImg);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImg();
            }
        });

        postContents = findViewById(R.id.et_postContents);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
            Glide.with(getApplicationContext()).load(data.getData()).override(400, 400).into(uploadImg);
        }
    }

    public void UploadPost(View view) {
        Toast.makeText(getApplicationContext(), postContents.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}