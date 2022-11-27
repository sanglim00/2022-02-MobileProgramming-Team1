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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Post;
import ac.kr.kookmin.petdiary.models.User;

public class MainActivity extends AppCompatActivity {
    private long backpressedTime = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView mainView;
    private MainRecyclerAdapter mainAdapter;
    private RadioGroup  radio_tags;
    private RadioButton radio_tag_btn;
    private String      current_tag = "dog";

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
        setContentView(R.layout.activity_main);

        ArrayList<MainItemList> mainItems = new ArrayList<>();
        mainView = (RecyclerView) findViewById(R.id.mainRecycler);
        radio_tag_btn = (RadioButton) findViewById(R.id.btn_main_petType1);
        radio_tag_btn.setChecked(true);
        mainAdapter = new MainRecyclerAdapter();
        mainView.setAdapter(mainAdapter);
        mainView.setLayoutManager(new LinearLayoutManager(this));
        RadioSelectListener(mainItems);
        RecyclerItemUpdate();

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
                        setExtraTag();
                        break;
                }
            }
        });
    }

    public void setExtraTag() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("태그 추가");
        alert.setMessage("태그를 입력하세요.");

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
        mainAdapter.clearMainList();
        db.collection("posts").whereEqualTo("petType", current_tag).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.exists()) {
                                Log.d("help", "살려줘");
                                Post post = doc.toObject(Post.class);
                                Log.d("help", doc.getId());
                                Log.d("help", post.getContent());
                                String postId = doc.getId();
                                String uid = post.getFrom();
                                db.collection("users").document(uid).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                User user = task.getResult().toObject(User.class);
                                                MainItemList item = new MainItemList(uid, user.getUserName(), uid, postId);
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