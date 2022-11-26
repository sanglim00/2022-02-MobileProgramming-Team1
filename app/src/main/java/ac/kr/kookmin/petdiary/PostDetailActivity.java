package ac.kr.kookmin.petdiary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    int itemCount;
    ImageView img_profile_detail_post; // 프로필 이미지
    ImageView img_detail_post; // 게시글 이미지
    ImageView imgbtn_like_detail_post; // 좋아요 버튼
    ImageView imgbtn_comment_detail_post; // 댓글 버튼

    ImageButton imgbtn_download_img_detail_post; // 이미지 다운로드 버튼

    boolean isLiked = false; // 좋아요 여부

    TextView txt_id_detail_post; // 프로필 id
    TextView txt_like_detail_post; // 좋아요 수
    TextView txt_comment_detail_post; // 댓글 수
    TextView txt_content_detail_post; // 게시글 내용

    RecyclerView comment_recycler; // 댓글 RecyclerView

    Comment_RecyclerViewAdapter adapter;
    int str_like;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        init();
        getData();



        img_profile_detail_post = findViewById(R.id.img_profile_detail_post);
        img_detail_post = findViewById(R.id.img_detail_post);

        imgbtn_like_detail_post = findViewById(R.id.imgbtn_like_detail_post);
        imgbtn_comment_detail_post = findViewById(R.id.imgbtn_comment_detail_post);
        imgbtn_download_img_detail_post = findViewById(R.id.imgbtn_download_img_detail_post);

        txt_id_detail_post = findViewById(R.id.txt_id_detail_post);
        txt_like_detail_post = findViewById(R.id.txt_like_detail_post); // 좋아요 수
        str_like = Integer.parseInt(txt_like_detail_post.getText().toString()); // 좋아요 수 : parseInt
        txt_comment_detail_post = findViewById(R.id.txt_comment_detail_post); // 댓글 수
        txt_comment_detail_post.setText(Integer.toString(itemCount)); // 댓글 수 반영
        txt_content_detail_post = findViewById(R.id.txt_content_detail_post);




        imgbtn_like_detail_post.setOnClickListener(new View.OnClickListener() { // 좋아요 버튼
            @Override
            public void onClick(final View view) {

                view.setActivated(!view.isActivated());
                isLiked = isLiked == false ? true : false;
                str_like = isLiked == false ? str_like - 1 : str_like + 1;
                txt_like_detail_post.setText(Integer.toString(str_like));

            }
        });
        imgbtn_download_img_detail_post.setOnClickListener(new View.OnClickListener() { // 이미지 다운로드 버튼
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailActivity.this, "다운로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                saveImageToGallery();
            }
        });




    }
    private void init(){
        RecyclerView recyclerView = findViewById(R.id.comment_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new Comment_RecyclerViewAdapter();

        recyclerView.setAdapter(adapter);

    }

    private void getData(){
        Comment_Item data = new Comment_Item("loremloremloremloremloremloremloremloremlorem", "h_guun");
        adapter.addItem(data);
        Comment_Item data2 = new Comment_Item("loremloremloremloremloremlorem", "j_myeong_");
        adapter.addItem(data2);
        adapter.addItem(data);

        itemCount = adapter.getItemCount();

    }
    private void saveImageToGallery(){ // 이미지 다운로드
        img_detail_post.setDrawingCacheEnabled(true);
        Bitmap bitmap = img_detail_post.getDrawingCache();
        MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, txt_id_detail_post.getText().toString(),"gun");
    }

}
