package ac.kr.kookmin.petdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainView;
    MainRecyclerAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (RecyclerView) findViewById(R.id.mainRecycler);

        mainAdapter = new MainRecyclerAdapter();

        mainView.setAdapter(mainAdapter);
        mainView.setLayoutManager(new LinearLayoutManager(this));
    }
}