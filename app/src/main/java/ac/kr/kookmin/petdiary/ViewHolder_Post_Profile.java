package ac.kr.kookmin.petdiary;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Post_Profile extends RecyclerView.ViewHolder{

    ImageButton img_post;

    public ViewHolder_Post_Profile(@NonNull View itemView) {
        super(itemView);
        img_post = itemView.findViewById(R.id.img_button_post);



    }

    public void onBind(PostItem_Profile data){
        img_post.setImageResource(data.getImage());
    }


}
