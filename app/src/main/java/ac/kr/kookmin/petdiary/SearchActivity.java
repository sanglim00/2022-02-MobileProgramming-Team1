package ac.kr.kookmin.petdiary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView searchView;
    SearchRecyclerAdapter searchAdapter;

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
    }
}
