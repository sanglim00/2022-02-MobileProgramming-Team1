package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    // 검색 결과 유저를 recycleerview로 보여주기 위함
    RecyclerView searchView;
    SearchRecyclerAdapter searchAdapter;
    RadioGroup footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (RecyclerView) findViewById(R.id.searchRecyclerView);

        searchAdapter = new SearchRecyclerAdapter();

        searchView.setAdapter(searchAdapter);
        searchView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SearchItem> searchItems = new ArrayList<>();
        for(int i = 1; i <= 20; i++){
            if (i % 3 == 0)
                searchItems.add(new SearchItem("my._.ddaengsun", "지겨운 견생....", ""));
            else if (i % 3 == 1)
                searchItems.add(new SearchItem("__catvely__", "캣타워 사줄 때까지 숨참는 중...", ""));
            else
                searchItems.add(new SearchItem("gae_ggum", "개껌조아 너무조아", ""));
        }
        searchAdapter.setSearchList(searchItems);

        RadioButton menu = findViewById(R.id.menu_search);
        menu.setChecked(true);
        footer = findViewById(R.id.footer_menu);
        footer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;
                RadioButton menu = radioGroup.findViewById(i);
                if(!menu.getText().equals("검색")) finish();

                if(menu.getText().equals("메인")) {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
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
                menu = findViewById(R.id.menu_search);
                menu.setChecked(true);
            }
        });
    }

}
