package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    private ArrayList<SearchItem> searchList;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ProgressBar progressBar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(searchList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void setSearchList(ArrayList<SearchItem> list) {
        this.searchList = list;
        notifyDataSetChanged();
    }

    public void addSearchItem(SearchItem item) {
        searchList.add(item);
        notifyDataSetChanged();
    }

    public void clearSearchItem() {
        searchList.clear();
        notifyDataSetChanged();
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView userID;
        TextView userInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.profileImg);
            userID = (TextView) itemView.findViewById(R.id.userID);
            userInfo = (TextView) itemView.findViewById(R.id.userInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    SearchItem item = searchList.get(position);
                    Intent intent;
                    String uid = searchList.get(position).getProfileSrc();
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
        }

        void onBind(SearchItem item){
            userID.setText(item.getUserId());
            userInfo.setText(item.getUserInfo());
            StorageReference profileRef = storage.getReference().child("profiles/" + item.getProfileSrc());
            Context ctx = this.itemView.getContext();
            profileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
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
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
