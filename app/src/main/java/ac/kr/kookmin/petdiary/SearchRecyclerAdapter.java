package ac.kr.kookmin.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    private ArrayList<SearchItem> searchList;

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


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView userID;
        TextView userInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            userID = (TextView) itemView.findViewById(R.id.userID);
            userInfo = (TextView) itemView.findViewById(R.id.userInfo);
        }

        void onBind(SearchItem item){
            userID.setText(item.getUserId());
            userInfo.setText(item.getUserInfo());
        }
    }
}
