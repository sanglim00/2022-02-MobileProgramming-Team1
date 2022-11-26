package ac.kr.kookmin.petdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainView;
    MainRecyclerAdapter mainAdapter;
    BottomNavigationView bottomNavigationView;
    private RadioGroup  radio_tags;
    private RadioButton radio_tag_btn;
    private String      current_tag = "DOG";

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
        RadioSelectListener();
        for (int i = 0; i < 20; i++) {
            mainItems.add(new MainItemList(current_tag, "", ""));
        }
        mainAdapter.setMainList(mainItems);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_one);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_one:
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

    public void RadioSelectListener() {
        radio_tags = (RadioGroup) findViewById(R.id.main_radioGroup);
        radio_tags.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_main_petType1:
                        current_tag = "DOG";
                        Toast.makeText(MainActivity.this, current_tag, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_main_petType2:
                        current_tag = "CAT";
                        Toast.makeText(MainActivity.this, current_tag, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_main_petType3:
                        current_tag = "FISH";
                        Toast.makeText(MainActivity.this, current_tag, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_main_petType4:
                        current_tag = "PIG";
                        Toast.makeText(MainActivity.this, current_tag, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_main_petType_extra:
                        break;
                }
            }
        });
    }
}