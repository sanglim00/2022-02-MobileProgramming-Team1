package ac.kr.kookmin.petdiary;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onLikeBtnClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

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
                        intent = new Intent(view.getContext(), ProfileActivity.class);
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
                        intent = new Intent(view.getContext(), NotiActivity.class);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
        void onBind(MainItemList item) {
            username.setText(item.getUsername());
        }
    }
}
