package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Profile_OthersActivity extends AppCompatActivity {
    boolean issubcribed = false;
    BottomNavigationView bottomNavigationView; // footer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);


        ToggleButton btn_subcribe = findViewById(R.id.btn_pf_others_subcribe); // 구독 버튼


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation); // footer
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
                        intent = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.action_three:
                        intent = new Intent(getApplicationContext(), WritingActivity.class);
                        startActivity(intent);
                        finish();
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




        btn_subcribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // 구독 버튼
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    issubcribed = true;
                }
                else{
                    issubcribed = false;
                }
            }
        });




    }
}
