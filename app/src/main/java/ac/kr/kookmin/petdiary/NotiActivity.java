package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        SharedPreferences preferences = getSharedPreferences("notification", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("notifications", "");
        Type type = new TypeToken<List<Notification>>(){}.getType();
        List<ac.kr.kookmin.petdiary.models.Notification> notiList = gson.fromJson(json, type);
        if (notiList != null) {
            Collections.reverse(notiList);
            notiItems.addAll(notiList);
            notiAdapter.setNotiList(notiItems);
        }

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