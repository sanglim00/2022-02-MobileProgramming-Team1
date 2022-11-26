package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ac.kr.kookmin.petdiary.models.Notification;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotiRecyclerAdapter extends RecyclerView.Adapter<NotiRecyclerAdapter.ViewHolder> {

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  postid;
                Intent  intent;
                int     pos;

                pos = holder.getAdapterPosition();
                postid = notiList.get(pos).getPostId();
                Toast.makeText(view.getContext(), postid, Toast.LENGTH_SHORT).show();
                intent = new Intent(view.getContext(), PostDetailActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
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

        void onBind(Notification item){
            title.setText(item.getTitle());
            content.setText(item.getContent());
        }
    }
}
