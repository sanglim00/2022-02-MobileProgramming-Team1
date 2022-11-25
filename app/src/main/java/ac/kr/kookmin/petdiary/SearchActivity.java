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

    RecyclerView searchView;
    SearchRecyclerAdapter searchAdapter;
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
        searchAdapter.setSearchList(searchItems);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_one:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.action_two:
                        return true;
                    case R.id.action_three:
                        intent = new Intent(getApplicationContext(), WritingActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.action_four:
                        intent = new Intent(getApplicationContext(), NotiActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.action_five:
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
