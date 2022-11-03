package ac.kr.kookmin.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotiRecyclerAdapter extends RecyclerView.Adapter<NotiRecyclerAdapter.ViewHolder> {

    private ArrayList<NotiItem> notiList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_noti_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(notiList.get(position));
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    public void setNotiList(ArrayList<NotiItem> list) {
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

        void onBind(NotiItem item){
            title.setText(item.getTitle());
            content.setText(item.getContent());
        }
    }
}
