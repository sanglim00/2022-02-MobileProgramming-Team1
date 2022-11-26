package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<MainItemList> mainList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_main_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mainList.get(position));
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public void setMainList(ArrayList<MainItemList> list) {
        this.mainList = list;
        notifyDataSetChanged();
    }

    public void clearMainList() {
        mainList.clear();
        notifyDataSetChanged();
    }

    public void addItem(MainItemList item) {
        mainList.add(item);
        notifyDataSetChanged();
    }
// 아직 사용 안함
    interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onLikeBtnClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
//

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_img;
        TextView        username;
        ImageView       content_img;
        ImageButton     like_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (CircleImageView) itemView.findViewById(R.id.recycler_ID_pic);
            username = (TextView) itemView.findViewById(R.id.recycler_ID_text);
            content_img = (ImageView) itemView.findViewById(R.id.recycler_content_pic);
            like_btn = (ImageButton) itemView.findViewById(R.id.recycler_like_btn);
            profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (profile_img.isPressed()) {
                        Intent intent;
                        int     pos;
                        String  uid; // 현재 클릭한 post의 uid값입니다.

                        pos = getAdapterPosition();
                        uid = mainList.get(pos).getUid();
                        String userUid = mAuth.getCurrentUser().getUid();
                        if (uid != null && userUid != null && uid.equals(userUid)) {
                            intent = new Intent(view.getContext(), ProfileActivity.class);
                            view.getContext().startActivity(intent);
                        } else {
                            intent = new Intent(view.getContext(), Profile_OthersActivity.class);
                            intent.putExtra("uid", uid);
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    int     pos;
                    String  uid; // 현재 클릭한 post의 uid값입니다.

                    pos = getAdapterPosition();
                    uid = mainList.get(pos).getUid();
                    String userUid = mAuth.getCurrentUser().getUid();
                    if (uid != null && userUid != null && uid.equals(userUid)) {
                        intent = new Intent(view.getContext(), ProfileActivity.class);
                        view.getContext().startActivity(intent);
                    } else {
                        intent = new Intent(view.getContext(), Profile_OthersActivity.class);
                        intent.putExtra("uid", uid);
                        view.getContext().startActivity(intent);
                    }
                }
            });
            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like_btn.setSelected(!like_btn.isSelected());
                    if (like_btn.isSelected()) {
                        like_btn.setImageResource(R.drawable.img_like_active);
                    }
                    else {
                        like_btn.setImageResource(R.drawable.img_like_unactive);
                    }
                }
            });
            content_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (content_img.isPressed()) {
                        intent = new Intent(view.getContext(), PostDetailActivity.class);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
        void onBind(MainItemList item) {
            username.setText(item.getUsername());

            Context ctx = this.itemView.getContext();
            StorageReference post = storage.getReference().child("images/" + item.getUser_content_img_src());
            StorageReference profile = storage.getReference().child("profiles/" + item.getUser_icon_img_src());

            post.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> postTask) {
                    if (postTask.isSuccessful()) {
                        profile.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> profileTask) {
                                if (profileTask.isSuccessful()) {
                                    if (((Activity) ctx).isFinishing()) return;
                                    Glide.with(ctx)
                                            .load(postTask.getResult())
                                            .into(content_img);
                                    Glide.with(ctx)
                                            .load(profileTask.getResult())
                                            .into(profile_img);
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
