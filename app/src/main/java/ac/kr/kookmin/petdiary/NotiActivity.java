package ac.kr.kookmin.petdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Notification;

public class NotiActivity extends AppCompatActivity {

    RecyclerView notiView;
    NotiRecyclerAdapter notiAdapter;
    RadioGroup footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notiView = (RecyclerView) findViewById(R.id.notiRecyclerView);

        notiAdapter = new NotiRecyclerAdapter();

        notiView.setAdapter(notiAdapter);
        notiView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Notification> notiItems = new ArrayList<>();

        for(int i = 1; i <= 20; i++){
            if (i % 3 == 0)
                notiItems.add(new Notification("좋아요 알림", "h._.gunn님이 회원님의 게시물을 좋아합...", "", "", i + ""));
            else if (i % 3 == 1)
                notiItems.add(new Notification("게시물 알림", "h._.gunn님이 회원님의 게시물에 댓글을...", "", "", i + ""));
            else
                notiItems.add(new Notification("구독 알림", "h._.gunn님의 새로운 게시물.", "", "", i + ""));
        }
        notiAdapter.setNotiList(notiItems);



        RadioButton menu = findViewById(R.id.menu_noti);
        menu.setChecked(true);
        footer = findViewById(R.id.footer_menu);
        footer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                RadioButton menu = radioGroup.findViewById(i);
                if(!menu.getText().equals("알림")) finish();

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
                else if(menu.getText().equals("유저")) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }

                menu = findViewById(R.id.menu_noti);
                menu.setChecked(true);
            }
        });
    }


}