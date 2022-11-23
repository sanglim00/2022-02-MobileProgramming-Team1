package ac.kr.kookmin.petdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainView;
    MainRecyclerAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MainItemList> mainItems = new ArrayList<>();
        mainView = (RecyclerView) findViewById(R.id.mainRecycler);
        mainAdapter = new MainRecyclerAdapter();
        mainView.setAdapter(mainAdapter);
        mainView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < 20; i++) {
            mainItems.add(new MainItemList("test_username", "테스트용입니다.", "", ""));
        }
        mainAdapter.setMainList(mainItems);
    }
}