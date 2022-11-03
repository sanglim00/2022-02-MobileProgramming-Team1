package ac.kr.kookmin.petdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class NotiActivity extends AppCompatActivity {

    RecyclerView notiView;
    NotiRecyclerAdapter notiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notiView = (RecyclerView) findViewById(R.id.notiRecyclerView);

        notiAdapter = new NotiRecyclerAdapter();

        notiView.setAdapter(notiAdapter);
        notiView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<NotiItem> notiItems = new ArrayList<>();

        for(int i = 1; i <= 20; i++){
            if (i % 3 == 0)
                notiItems.add(new NotiItem("좋아요 알림", "h._.gunn님이 회원님의 게시물을 좋아합...", "", "", i));
            else if (i % 3 == 1)
                notiItems.add(new NotiItem("게시물 알림", "h._.gunn님이 회원님의 게시물에 댓글을...", "", "", i));
            else
                notiItems.add(new NotiItem("구독 알림", "h._.gunn님의 새로운 게시물.", "", "", i));
        }
        notiAdapter.setNotiList(notiItems);
    }
}