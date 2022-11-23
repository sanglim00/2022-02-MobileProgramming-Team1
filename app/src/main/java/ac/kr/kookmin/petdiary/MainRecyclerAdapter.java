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

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private ArrayList<MainItemList> mainList;
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
    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_img;
        TextView        username;
        ImageView       content_img;
        TextView        content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (CircleImageView) itemView.findViewById(R.id.recycler_ID_pic);
            username = (TextView) itemView.findViewById(R.id.recycler_ID_text);
            content_img = (ImageView) itemView.findViewById(R.id.recycler_content_pic);
            content = (TextView) itemView.findViewById(R.id.recycler_content_text);
        }
        void onBind(MainItemList item) {
            username.setText(item.getUsername());
            content.setText(item.getContent());
        }
    }
}
