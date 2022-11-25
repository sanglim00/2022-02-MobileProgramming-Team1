package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.BreakIterator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ProfileActivity extends AppCompatActivity {
    TextView txt_pf_name;
    TextView txt_pf_gender;
    TextView txt_pf_meetDate;
    TextView txt_pf_one_line_info;
    ImageView img_pf;
    Bitmap bitmap;
    BottomNavigationView bottomNavigationView;

    Profile_Post_RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getData();

        txt_pf_name = findViewById(R.id.txt_pf_name); // 프로필 layout - 이름
        txt_pf_gender = findViewById(R.id.txt_pf_gender); // 프로필 layout - 성별
        txt_pf_meetDate = findViewById(R.id.txt_pf_meetDate); // 프로필 layout - 만난 날짜
        txt_pf_one_line_info = findViewById(R.id.txt_pf_one_line_info); // 프로필 layout - 한줄 소개개
        img_pf = findViewById(R.id.img_pf);

        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼
        Button btn_edit_profile = findViewById(R.id.btn_pf_edit_profile); // 프로필 편집


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




        btn_edit_profile.setOnClickListener(new View.OnClickListener() { // 프로필 편집
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Profile_EditActivity.class);



                intent.putExtra("name", txt_pf_name.getText());
                intent.putExtra("gender", txt_pf_gender.getText());
                intent.putExtra("meetDate", txt_pf_meetDate.getText());
                intent.putExtra("one_line", txt_pf_one_line_info.getText());

                startActivityResult.launch(intent);
            }
        });
    }


    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 0) {
                        boolean getimage_changed = result.getData().getBooleanExtra("image_changed", false); // 이미지 변경 시
                        boolean getcomplete = result.getData().getBooleanExtra("complete", false);

                        if (getcomplete){
                            if (getimage_changed) { // 완료 버튼을 눌렀을 때만 수정사항이 반영되도록 함. ( 취소 버튼 제외 )

                                byte[] byteArray = result.getData().getByteArrayExtra("image");

                                Bitmap bitmap;

                                if (byteArray != null) {
                                    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    img_pf.setImageBitmap(bitmap);

                                }

                            }

                            String nameIntent = result.getData().getStringExtra("name");
                            String genderIntent = result.getData().getStringExtra("gender");
                            String meetDateIntent = result.getData().getStringExtra("meetDate");
                            String oneLineIntent = result.getData().getStringExtra("one_line");
                            txt_pf_name.setText(nameIntent);
                            txt_pf_gender.setText(genderIntent);
                            txt_pf_meetDate.setText(meetDateIntent);
                            txt_pf_one_line_info.setText(oneLineIntent);
                        }


                    }
                }
            });

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.post_recyclerView);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter = new Profile_Post_RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        PostItem_Profile data = new PostItem_Profile(R.drawable.ddaeng2);
        adapter.addItem(data);
        data = new PostItem_Profile(R.drawable.ddaeng2);
        adapter.addItem(data);
        data = new PostItem_Profile(R.drawable.ddaeng2);
        adapter.addItem(data);
        data = new PostItem_Profile(R.drawable.ddaeng2);
        adapter.addItem(data);
        data = new PostItem_Profile(R.drawable.ddaeng2);
        adapter.addItem(data);
    }



}
