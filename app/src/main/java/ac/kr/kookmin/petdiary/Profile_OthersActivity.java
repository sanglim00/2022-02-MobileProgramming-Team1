package ac.kr.kookmin.petdiary;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;


public class Profile_OthersActivity extends AppCompatActivity {
    boolean issubcribed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);


        ToggleButton btn_subcribe = findViewById(R.id.btn_pf_others_subcribe);


        btn_subcribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
