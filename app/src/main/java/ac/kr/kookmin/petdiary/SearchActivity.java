package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
    // 푸터
    BottomNavigationView bottomNavigationView;

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

        // 푸터 메뉴 클릭 시 이벤트
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // 클릭 시 현재 페이지인 푸터를 active 하게
        bottomNavigationView.setSelectedItemId(R.id.action_two);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if(item.getItemId() != R.id.action_two) finish();
                switch (item.getItemId()) {
                    case R.id.action_one:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_two:
                        // 현재 페이지는 클릭되지 않도록
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
    }
}
