package ac.kr.kookmin.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Comment_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Comment_Item> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comment, parent, false);
        return new ViewHolder_Comment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder_Comment)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        if(listData != null){
            return listData.size();
        }
        return 0;
    }

    void addItem(Comment_Item data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        notifyDataSetChanged();
    }

    void changeItem(Comment_Item data) {
        int lastIndex = this.getItemCount();
        listData.remove(lastIndex - 1);
        listData.add(data);
        notifyDataSetChanged();
    }



}
