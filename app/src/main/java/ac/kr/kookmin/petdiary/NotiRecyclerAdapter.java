package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Notification;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotiRecyclerAdapter extends RecyclerView.Adapter<NotiRecyclerAdapter.ViewHolder> {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<Notification> notiList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_noti_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(notiList.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String  postid;
//                Intent  intent;
//                int     pos;
//
//                pos = holder.getAdapterPosition();
//                postid = notiList.get(pos).getPostId();
//                intent = new Intent(view.getContext(), PostDetailActivity.class);
//                intent.putExtra("postId", postid);
//                intent.putExtra("userId", )
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (notiList == null) return 0;
        return notiList.size();
    }

    public void setNotiList(ArrayList<Notification> list) {
        this.notiList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView title;
        TextView content;
        ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            title = (TextView) itemView.findViewById(R.id.notiTitle);
            content = (TextView) itemView.findViewById(R.id.notiContent);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
        }

        void onBind(Notification item) {
            Context ctx = this.itemView.getContext();
            title.setText(item.getTitle());
            content.setText(item.getContent());
            StorageReference postImg = storage.getReference().child("images/" + item.getPostImageSrc());
            StorageReference profileImg = storage.getReference().child("profiles/" + item.getProfileSrc());
            postImg.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        if (((Activity) ctx).isFinishing()) return;
                        Glide.with(ctx)
                                .load(task.getResult())
                                .into(postImage);
                        profileImg.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    if (((Activity) ctx).isFinishing()) return;
                                    Glide.with(ctx)
                                            .load(task.getResult())
                                            .into(profile);
                                } else {
                                    if (((Activity) ctx).isFinishing()) return;
                                    Glide.with(ctx)
                                            .load(R.drawable.default_profile)
                                            .into(profile);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
