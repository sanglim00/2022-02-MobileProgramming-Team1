package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ac.kr.kookmin.petdiary.models.Post;
import ac.kr.kookmin.petdiary.models.User;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    TextView txt_pf_name;
    TextView txt_pf_gender;
    TextView txt_pf_meetDate;
    TextView txt_pf_one_line_info;
    TextView txt_pf_id;
    ImageView img_pf;
    Bitmap bitmap;
    BottomNavigationView bottomNavigationView;

    Profile_Post_RecyclerViewAdapter adapter;

    ImageButton openSetting;

    String comment;

    @Override
    protected void onStart() {
        super.onStart();
        setProfileData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        mAuth = FirebaseAuth.getInstance();
        txt_pf_id = findViewById(R.id.txt_pf_id);
        txt_pf_name = findViewById(R.id.txt_pf_name); // 프로필 layout - 이름
        txt_pf_gender = findViewById(R.id.txt_pf_gender); // 프로필 layout - 성별
        txt_pf_meetDate = findViewById(R.id.txt_pf_meetDate); // 프로필 layout - 만난 날짜
        txt_pf_one_line_info = findViewById(R.id.txt_pf_one_line_info); // 프로필 layout - 한줄 소개개
        img_pf = findViewById(R.id.img_pf);

        ImageButton imgBtn_setting = findViewById(R.id.imgBtn_setting); // 환경설정 버튼
        Button btn_edit_profile = findViewById(R.id.btn_pf_edit_profile); // 프로필 편집

        openSetting = findViewById(R.id.imgBtn_setting);
        openSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation); // footer
        bottomNavigationView.setSelectedItemId(R.id.action_five);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() != R.id.action_five) finish();
                switch (item.getItemId()) {
                    case R.id.action_one:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_two:
                        intent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent);
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
                        return true;
                }
                return false;
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() { // 프로필 편집
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Profile_EditActivity.class);
                intent.putExtra("id", txt_pf_id.getText());
                intent.putExtra("name", txt_pf_name.getText());
                intent.putExtra("gender", txt_pf_gender.getText());
                intent.putExtra("meetDate", txt_pf_meetDate.getText());
                intent.putExtra("one_line", comment);
                intent.putExtra("uid", mAuth.getCurrentUser().getUid());
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

    private void initPostData(String uid) {
        db.collection("posts").whereEqualTo("from", uid).get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.exists()) {
                                    adapter.addItem(new PostItem_Profile(doc.getId()));
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if( keycode == KeyEvent.KEYCODE_BACK) {
            bottomNavigationView.setSelectedItemId(R.id.action_one);
            return true;
        }

        return false;
    }

    private void setProfileData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;
        db.collection("users").document(user.getUid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User userDoc = documentSnapshot.toObject(User.class);
                    adapter.setUserName(userDoc.getUserName());
                    initProfileData(userDoc, user.getUid());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }

    private void initProfileData(User user, String uid) {
        comment = user.getComment();
        txt_pf_id.setText(user.getUserName());
        txt_pf_name.setText(user.getPetName());
        txt_pf_gender.setText(user.getGender());
        txt_pf_meetDate.setText(user.getPetBirth());
        txt_pf_one_line_info.setText(comment == null || comment.equals("") ? "한 줄 소개가 없습니다." : user.getComment());

        StorageReference profile = storage.getReference().child("profiles/" + uid);

        profile.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    if (ProfileActivity.this.isFinishing())
                        return;
                    Glide.with(ProfileActivity.this)
                            .load(task.getResult())
                            .into(img_pf);
                    if (adapter.getItemCount() == 0) initPostData(uid);
                } else {
                    if (ProfileActivity.this.isFinishing())
                        return;
                    Glide.with(ProfileActivity.this)
                            .load(R.drawable.default_profile)
                            .into(img_pf);
                }
            }
        });


    }

}
