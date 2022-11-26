package ac.kr.kookmin.petdiary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);




        ImageButton button = findViewById(R.id.btn_like_post);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.isSelected()) {
                    button.setImageResource(R.drawable.btn_like_post_on);
                } else {
                    button.setImageResource(R.drawable.btn_like_post_off);
                }
                button.setSelected(!button.isSelected());
            }
        });

    }
}
