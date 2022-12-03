package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ac.kr.kookmin.petdiary.models.User;


public class Profile_OthersActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    boolean issubcribed = false;
    TextView txt_pf_name;
    TextView txt_pf_gender;
    TextView txt_pf_meetDate;
    TextView txt_pf_one_line_info;
    TextView txt_pf_id;
    ImageView img_pf;

    RadioGroup footer;


    Profile_Post_RecyclerViewAdapter adapter;

    ImageButton openSetting;

    ProgressBar progressBar;

    ToggleButton btn_subcribe;

    String uid;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        setProfileData(intent.getStringExtra("uid"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_profile_others);

        init();

        txt_pf_id = findViewById(R.id.txt_pf_others_id);
        txt_pf_name = findViewById(R.id.txt_pf_others_name);
        txt_pf_gender = findViewById(R.id.txt_pf_others_gender);
        txt_pf_meetDate = findViewById(R.id.txt_pf_others_meetDate);
        txt_pf_one_line_info = findViewById(R.id.txt_pf_others_one_line_info);
        img_pf = findViewById(R.id.img_pf_others);

        progressBar = findViewById(R.id.profile_other_progress_bar);

        btn_subcribe = findViewById(R.id.btn_pf_others_subcribe); // 구독 버튼

        openSetting = findViewById(R.id.imgBtn_setting);
        openSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });


        btn_subcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                AsyncTask.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection conn;
                            URL url = new URL("http://20.249.4.187/api/subscribe");

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(100000);
                            conn.setReadTimeout(100000);

                            conn.setRequestMethod("POST");

                            // 타입설정
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("Accept", "application/json");

                            // OutputStream으로 Post 데이터를 넘겨주겠다는 옵션
                            conn.setDoOutput(true);

                            // InputStream으로 서버로 부터 응답을 받겠다는 옵션
                            conn.setDoInput(true);

                            // 서버로 전달할 Json객체 생성
                            JSONObject json = new JSONObject();

                            // Json객체에 유저의 name, phone, address 값 세팅
                            // Json의 파라미터는 Key, Value 형식
                            json.put("uid", mAuth.getCurrentUser().getUid());
                            json.put("ownerUid", uid);

                            // Request Body에 데이터를 담기위한 OutputStream 객체 생성
                            OutputStream outputStream;
                            outputStream = conn.getOutputStream();
                            outputStream.write(json.toString().getBytes());
                            outputStream.flush();

                            // 실제 서버로 Request 요청 하는 부분 (응답 코드를 받음, 200은 성공, 나머지 에러)
                            int response = conn.getResponseCode();

                            if (response != 201) {
                                Toast.makeText(Profile_OthersActivity.this, "구독에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                btn_subcribe.setChecked(false);
                            }
                            conn.disconnect();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
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
    private void init(){
        RecyclerView recyclerView = findViewById(R.id.post_recyclerView_others);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);


        adapter = new Profile_Post_RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initPostData(String uid) {
        db.collection("posts").whereEqualTo("from", uid).orderBy("time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (doc.exists()) {
                                    adapter.addItem(new PostItem_Profile(doc.getId()));
                                }
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    private void setProfileData(String uid) {
        if (uid == null) return;
        this.uid = uid;
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
        issubcribed = user.getFollower() == null ? false : user.getFollower().contains(mAuth.getUid());
        btn_subcribe.setChecked(issubcribed);
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
