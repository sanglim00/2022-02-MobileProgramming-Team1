package ac.kr.kookmin.petdiary;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewHolder_Post_Profile extends RecyclerView.ViewHolder{

    ImageButton img_post;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public ViewHolder_Post_Profile(@NonNull View itemView) {
        super(itemView);
        img_post = itemView.findViewById(R.id.img_button_post);

    }

    public void onBind(PostItem_Profile data, String userName){
        Context ctx = this.itemView.getContext();
        StorageReference post = storage.getReference().child("images/" + data.getImage());
        post.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    if (((Activity) ctx).isFinishing()) return;
                    Glide.with(ctx)
                            .load(task.getResult())
                            .into(img_post);
                }
            }
        });
        img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                intent.putExtra("postId", data.image);
                intent.putExtra("userId", userName);
                view.getContext().startActivity(intent);
            }
        });
    }


}
