package ac.kr.kookmin.petdiary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder_Comment extends RecyclerView.ViewHolder {

    TextView txt_comment_content;
    TextView txt_comment_user;
    public ViewHolder_Comment(@NonNull View itemView){
        super(itemView);
        txt_comment_content = itemView.findViewById(R.id.txt_comment_content);
        txt_comment_user = itemView.findViewById(R.id.txt_comment_user);

    }
    public void onBind(Comment_Item data){
        txt_comment_content.setText(data.getContents());
        txt_comment_user.setText(data.getUser());
    }


}
