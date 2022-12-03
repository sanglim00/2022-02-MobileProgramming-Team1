package ac.kr.kookmin.petdiary;

import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Post_Profile_Others extends RecyclerView.ViewHolder{

    ImageButton img_post_others;

    public ViewHolder_Post_Profile_Others(@NonNull View itemView) {
        super(itemView);
        img_post_others = itemView.findViewById(R.id.img_button_post_others);



    }

    public void onBind(PostItem_Profile_Others data){
        img_post_others.setImageResource(data.getImage());
    }


}
