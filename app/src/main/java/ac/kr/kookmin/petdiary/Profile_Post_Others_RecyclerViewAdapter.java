package ac.kr.kookmin.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Profile_Post_Others_RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<PostItem_Profile_Others> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_post_profile_others_item, parent, false);
        return new ViewHolder_Post_Profile_Others(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder_Post_Profile_Others)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(PostItem_Profile_Others data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
}
