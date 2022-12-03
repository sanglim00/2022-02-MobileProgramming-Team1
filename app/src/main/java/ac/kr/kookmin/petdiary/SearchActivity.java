package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.User;

public class SearchActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 검색 결과 유저를 recycleerview로 보여주기 위함
    SearchView search;
    RecyclerView searchView;
    SearchRecyclerAdapter searchAdapter;
    RadioGroup footer;
    ProgressBar progressBar;
    ArrayList<String> userNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = findViewById(R.id.searchView);
        searchView = (RecyclerView) findViewById(R.id.searchRecyclerView);

        searchAdapter = new SearchRecyclerAdapter();

        searchView.setAdapter(searchAdapter);
        searchView.setLayoutManager(new LinearLayoutManager(this));
        search.setSubmitButtonEnabled(true);

        ArrayList<SearchItem> searchItems = new ArrayList<>();
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

        progressBar = findViewById(R.id.search_progress_bar);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userNames.clear();
                searchAdapter.clearSearchItem();
                searchAdapter.setProgressBar(progressBar);
                progressBar.setVisibility(View.VISIBLE);
                db.collection("users").whereGreaterThanOrEqualTo("userName", query).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() <= 0) {
                                        Toast.makeText(SearchActivity.this, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        return;
                                    }
                                    for(QueryDocumentSnapshot doc : task.getResult()) {
                                        if (doc.exists()) {
                                            User user = doc.toObject(User.class);
                                            if (userNames.contains(user.getUserName())) continue;
                                            userNames.add(user.getUserName());
                                            String comment = user.getComment();
                                            SearchItem item = new SearchItem(user.getUserName(), comment == null || comment.length() == 0 ? "한 줄 소개가 없습니다." : comment, doc.getId());
                                            searchAdapter.addSearchItem(item);
                                        }
                                    }
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
