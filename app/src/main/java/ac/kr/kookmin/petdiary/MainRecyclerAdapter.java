package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<MainItemList> mainList = new ArrayList<>();

    ProgressBar progressBar;
    RecyclerView recyclerView;

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
        recyclerView.setVisibility(View.INVISIBLE);
        mainList.clear();
        notifyDataSetChanged();
    }

    public void addItem(MainItemList item) {
        mainList.add(item);
        notifyDataSetChanged();
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setRecyclerView(RecyclerView recyclerView) { this.recyclerView = recyclerView; }

// 아직 사용 안함
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
        ImageView       like_btn;
        TextView        txt_like_main;
        int str_like;
        boolean isLiked = false; // 좋아요 여부

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (CircleImageView) itemView.findViewById(R.id.recycler_ID_pic);
            username = (TextView) itemView.findViewById(R.id.recycler_ID_text);
            content_img = (ImageView) itemView.findViewById(R.id.recycler_content_pic);
            txt_like_main = (TextView) itemView.findViewById(R.id.recycler_main_like_text);
            like_btn = (ImageView) itemView.findViewById(R.id.recycler_like_btn);

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
                    int pos = getAdapterPosition();
                    view.setActivated(!view.isActivated());
                    isLiked = isLiked == false ? true : false;
                    str_like = isLiked == false ? str_like - 1 : str_like + 1;
                    txt_like_main.setText(Integer.toString(str_like));

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpURLConnection conn;
                                URL url = new URL("http://20.249.4.187/api/post/like");

                                conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(100000);
                                conn.setReadTimeout(100000);

                                conn.setRequestMethod("POST");

                                // 타입설정
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestProperty("Accept", "application/json");

                                // OutputStream으로 Post 데이터를 넘겨주겠다는 옵션
                                conn.setDoOutput(true);

                                // InputStream으로 서버로 부터 응답을 받겠다는 옵션
                                conn.setDoInput(true);

                                // 서버로 전달할 Json객체 생성
                                JSONObject json = new JSONObject();

                                // Json객체에 유저의 name, phone, address 값 세팅
                                // Json의 파라미터는 Key, Value 형식
                                json.put("uid", mAuth.getCurrentUser().getUid());
                                json.put("postId", mainList.get(pos).getUser_content_img_src());

                                // Request Body에 데이터를 담기위한 OutputStream 객체 생성
                                OutputStream outputStream;
                                outputStream = conn.getOutputStream();
                                outputStream.write(json.toString().getBytes());
                                outputStream.flush();

                                // 실제 서버로 Request 요청 하는 부분 (응답 코드를 받음, 200은 성공, 나머지 에러)
                                int response = conn.getResponseCode();

                                conn.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
            content_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent;
                    if (content_img.isPressed()) {
                        intent = new Intent(view.getContext(), PostDetailActivity.class);
                        intent.putExtra("postId", mainList.get(pos).getUser_content_img_src());
                        intent.putExtra("userId", mainList.get(pos).getUsername());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
        void onBind(MainItemList item) {
            username.setText(item.getUsername());
            isLiked = item.isLiked();
            Log.d("like test2", "" + isLiked);
            str_like = item.getLikes();
            like_btn.setActivated(isLiked);
            txt_like_main.setText(Integer.toString(item.getLikes()));
            Context ctx = this.itemView.getContext();
            StorageReference post = storage.getReference().child("images/" + item.getUser_content_img_src());
            StorageReference profile = storage.getReference().child("profiles/" + item.getUser_icon_img_src());

            post.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> postTask) {
                    if (postTask.isSuccessful()) {
                        if (((Activity) ctx). isFinishing()) return;
                        Glide.with(ctx)
                                .load(postTask.getResult())
                                .into(content_img);
                        profile.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> profileTask) {
                                if (profileTask.isSuccessful()) {
                                    if (((Activity) ctx).isFinishing()) return;
                                    Glide.with(ctx)
                                            .load(profileTask.getResult())
                                            .into(profile_img);
                                } else {
                                    if (((Activity) ctx).isFinishing()) return;
                                    Glide.with(ctx)
                                            .load(R.drawable.default_profile)
                                            .into(profile_img);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }
}
