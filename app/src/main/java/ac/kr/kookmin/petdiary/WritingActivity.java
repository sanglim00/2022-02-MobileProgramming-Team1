package ac.kr.kookmin.petdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class WritingActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);


    }

    public void UploadImg(View view) {
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

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    public void UploadPost(View view) {
        Toast.makeText(getApplicationContext(), "업로드 버튼 클릭", Toast.LENGTH_SHORT).show();
    }
}