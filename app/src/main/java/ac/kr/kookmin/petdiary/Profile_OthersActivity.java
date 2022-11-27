package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ac.kr.kookmin.petdiary.models.User;


public class Profile_OthersActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    boolean issubcribed = false;
    TextView txt_pf_name;
    TextView txt_pf_gender;
    TextView txt_pf_meetDate;
    TextView txt_pf_one_line_info;
    TextView txt_pf_id;
    ImageView img_pf;

    BottomNavigationView bottomNavigationView; // footer

    Profile_Post_RecyclerViewAdapter adapter;

    ImageButton openSetting;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        setProfileData(intent.getStringExtra("uid"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);

        init();

        txt_pf_id = findViewById(R.id.txt_pf_others_id);
        txt_pf_name = findViewById(R.id.txt_pf_others_name);
        txt_pf_gender = findViewById(R.id.txt_pf_others_gender);
        txt_pf_meetDate = findViewById(R.id.txt_pf_others_meetDate);
        txt_pf_one_line_info = findViewById(R.id.txt_pf_others_one_line_info);
        img_pf = findViewById(R.id.img_pf_others);

        ToggleButton btn_subcribe = findViewById(R.id.btn_pf_others_subcribe); // 구독 버튼

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
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });




        btn_subcribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // 구독 버튼
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    issubcribed = true;
                }
                else{
                    issubcribed = false;
                }
            }
        });




    }
    private void init(){
        RecyclerView recyclerView = findViewById(R.id.post_recyclerView_others);

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

    private void setProfileData(String uid) {
        if (uid == null) return;
        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User userDoc = documentSnapshot.toObject(User.class);
                        adapter.setUserName(userDoc.getUserName());
                        initProfileData(userDoc, uid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void initProfileData(User user, String uid) {
        String comment = user.getComment();
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
                    if (Profile_OthersActivity.this.isFinishing())
                        return;
                    Glide.with(Profile_OthersActivity.this)
                            .load(task.getResult())
                            .into(img_pf);
                } else {
                    if (Profile_OthersActivity.this.isFinishing())
                        return;
                    Glide.with(Profile_OthersActivity.this)
                            .load(R.drawable.default_profile)
                            .into(img_pf);
                }
                if (adapter.getItemCount() == 0) initPostData(uid);
            }
        });
    }

}
