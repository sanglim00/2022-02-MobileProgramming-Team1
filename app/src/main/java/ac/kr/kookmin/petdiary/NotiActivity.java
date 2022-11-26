package ac.kr.kookmin.petdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Notification;

public class NotiActivity extends AppCompatActivity {

    RecyclerView notiView;
    NotiRecyclerAdapter notiAdapter;
    BottomNavigationView bottomNavigationView;

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


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_four);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() != R.id.action_four) finish();
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


    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if( keycode == KeyEvent.KEYCODE_BACK) {
            bottomNavigationView.setSelectedItemId(R.id.action_one);
            return true;
        }

        return false;
    }


}