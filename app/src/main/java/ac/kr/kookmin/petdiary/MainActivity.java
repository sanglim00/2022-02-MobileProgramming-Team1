package ac.kr.kookmin.petdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Post;
import ac.kr.kookmin.petdiary.models.User;

public class MainActivity extends AppCompatActivity {
    private long backpressedTime = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView mainView;
    private MainRecyclerAdapter mainAdapter;
    private RadioGroup  radio_tags;
    private RadioButton radio_tag_btn;
    private RadioButton extraBtn;
    private String      current_tag = "dog";

    ProgressBar progressBar;

    RadioGroup footer;

    // 백버튼 두번 클릭시 앱 종료
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.detail_progress_bar);
        ArrayList<MainItemList> mainItems = new ArrayList<>();
        mainView = (RecyclerView) findViewById(R.id.mainRecycler);
        radio_tag_btn = (RadioButton) findViewById(R.id.btn_main_petType1);
        extraBtn = (RadioButton) findViewById(R.id.btn_main_petType_extra);
        radio_tag_btn.setChecked(true);
        mainAdapter = new MainRecyclerAdapter();
        mainView.setAdapter(mainAdapter);
        mainView.setLayoutManager(new LinearLayoutManager(this));
        RadioSelectListener(mainItems);
        RecyclerItemUpdate();
        extraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setExtraTag();
            }
        });

        RadioButton menu = findViewById(R.id.menu_main);
        menu.setChecked(true);
        footer = findViewById(R.id.footer_menu);
        footer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                RadioButton menu = radioGroup.findViewById(i);

                if(menu.getText().equals("검색")) {
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
                else if(menu.getText().equals("유저")) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }
                menu = findViewById(R.id.menu_main);
                menu.setChecked(true);
            }
        });

    }

    public void RadioSelectListener(ArrayList<MainItemList> mainItems) {
        radio_tags = (RadioGroup) findViewById(R.id.main_radioGroup);
        radio_tags.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_main_petType1:
                        current_tag = "dog";
                        RecyclerItemUpdate();
                        break;
                    case R.id.btn_main_petType2:
                        current_tag = "cat";
                        RecyclerItemUpdate();
                        break;
                    case R.id.btn_main_petType3:
                        current_tag = "fish";
                        RecyclerItemUpdate();
                        break;
                    case R.id.btn_main_petType4:
                        current_tag = "pig";
                        RecyclerItemUpdate();
                        break;
                    case R.id.btn_main_petType_extra:
                        break;
                }
            }
        });
    }

//    public void extraBtnEvent() {
//        RadioButton extraBtn;
//
//        extraBtn = (RadioButton) findViewById(R.id.btn_main_petType_extra);
//        extraBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setExtraTag();
//            }
//        });
//    }

    public void setExtraTag() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Pet Type Add");
        alert.setMessage("추가할 Pet Type를 적어주세요.");

        final EditText inputTag = new EditText(this);
        alert.setView(inputTag);

        alert.setPositiveButton("적용", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 current_tag = inputTag.getText().toString().toLowerCase();
                 RecyclerItemUpdate();
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    public void RecyclerItemUpdate() {
        mainView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mainAdapter.setProgressBar(progressBar);
        mainAdapter.setRecyclerView(mainView);
        mainAdapter.clearMainList();
        db.collection("posts").whereEqualTo("petType", current_tag).orderBy("time", Query.Direction.DESCENDING).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() <= 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "아직까지 작성 된 게시물이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                Post post = doc.toObject(Post.class);
                                String postId = doc.getId();
                                String uid = post.getFrom();
                                int likes = post.getLikes();
                                boolean isLiked = post.getLikeUid().contains(mAuth.getCurrentUser().getUid());
                                Log.d("like test", "" + isLiked);
                                db.collection("users").document(uid).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                User user = task.getResult().toObject(User.class);
                                                MainItemList item = new MainItemList(uid, user.getUserName(), uid, postId, likes, isLiked);
                                                mainAdapter.addItem(item);
                                            }
                                        }
                                    });
                            }
                        }
                    }
                }
            });
    }

}