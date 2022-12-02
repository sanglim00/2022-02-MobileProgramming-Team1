package ac.kr.kookmin.petdiary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import ac.kr.kookmin.petdiary.models.Comment;
import ac.kr.kookmin.petdiary.models.Post;

public class PostDetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    int itemCount;
    ImageView img_profile_detail_post; // 프로필 이미지
    ImageView img_detail_post; // 게시글 이미지
    ImageView imgbtn_like_detail_post; // 좋아요 버튼

    ImageButton imgbtn_download_img_detail_post; // 이미지 다운로드 버튼


    TextView txt_id_detail_post; // 프로필 id
    TextView txt_like_detail_post; // 좋아요 수
    TextView txt_content_detail_post; // 게시글 내용

    RecyclerView comment_recycler; // 댓글 RecyclerView

    View comment_view;

    Button btn_addComment;
    EditText et_Comment;

    Comment_RecyclerViewAdapter adapter;
    int str_like;
    boolean isLiked = false; // 좋아요 여부
    boolean canDownloadImg = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_detail_post);
        init();
        progressBar = (ProgressBar) findViewById(R.id.detail_progress_bar);
        img_profile_detail_post = findViewById(R.id.img_profile_detail_post);
        img_detail_post = findViewById(R.id.img_detail_post);

        imgbtn_like_detail_post = findViewById(R.id.imgbtn_like_detail_post);
        imgbtn_download_img_detail_post = findViewById(R.id.imgbtn_download_img_detail_post);

        txt_id_detail_post = findViewById(R.id.txt_id_detail_post);
        txt_like_detail_post = findViewById(R.id.txt_like_detail_post); // 좋아요 수
        txt_content_detail_post = findViewById(R.id.txt_content_detail_post);

        comment_view = findViewById(R.id.comment_post_detail);
        btn_addComment = findViewById(R.id.btn_add_comment);
        et_Comment = findViewById(R.id.et_comment);

        String postId = getIntent().getStringExtra("postId");
        String userId = getIntent().getStringExtra("userId");

        initPostDetail(postId, userId);

        imgbtn_like_detail_post.setOnClickListener(new View.OnClickListener() { // 좋아요 버튼
            @Override
            public void onClick(final View view) {

                view.setActivated(!view.isActivated());
                isLiked = isLiked == false ? true : false;
                str_like = isLiked == false ? str_like - 1 : str_like + 1;
                txt_like_detail_post.setText(Integer.toString(str_like));

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
                            json.put("postId", postId);

                            // Request Body에 데이터를 담기위한 OutputStream 객체 생성
                            OutputStream outputStream;
                            outputStream = conn.getOutputStream();
                            outputStream.write(json.toString().getBytes());
                            outputStream.flush();

                            // 실제 서버로 Request 요청 하는 부분 (응답 코드를 받음, 200은 성공, 나머지 에러)
                            int response = conn.getResponseCode();

                            if (response != 201)
                                Toast.makeText(PostDetailActivity.this, "좋아요에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        imgbtn_download_img_detail_post.setOnClickListener(new View.OnClickListener() { // 이미지 다운로드 버튼
            @Override
            public void onClick(View view) {
                if(canDownloadImg) {
                    Toast.makeText(PostDetailActivity.this, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    saveImageToGallery();
                }
                else if(!canDownloadImg){
                    Toast.makeText(PostDetailActivity.this, "작성자가 이미지 다운로드를 허용하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et_Comment.getText().toString();
                if (content == null || content.trim().length() == 0) {
                    Toast.makeText(PostDetailActivity.this, "댓글 내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_Comment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_Comment.getWindowToken(), 0);
                adapter.addItem(new Comment_Item(content, "업로드 중 ..."));
                AsyncTask.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection conn;
                            URL url = new URL("http://20.249.4.187/api/post/comment");

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
                            json.put("postId", postId);
                            json.put("content", content);

                            // Request Body에 데이터를 담기위한 OutputStream 객체 생성
                            OutputStream outputStream;
                            outputStream = conn.getOutputStream();
                            outputStream.write(json.toString().getBytes());
                            outputStream.flush();

                            // 실제 서버로 Request 요청 하는 부분 (응답 코드를 받음, 200은 성공, 나머지 에러)
                            int response = conn.getResponseCode();

                            if (response == 201) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String data = br.lines().collect(Collectors.joining());
                                JSONObject parseData = new JSONObject(data);
                                adapter.changeItem(new Comment_Item(content, parseData.getString("userName")));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



    }

    private void initPostDetail(String postId, String userId) {
        progressBar.setVisibility(View.VISIBLE);
        if (postId != null && userId != null) {
            db.collection("posts").document(postId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Post post = task.getResult().toObject(Post.class);
                                txt_id_detail_post.setText(userId);
                                txt_content_detail_post.setText(post.getContent());
                                txt_like_detail_post.setText(post.getLikes() + "");
                                str_like = post.getLikes();
                                canDownloadImg = post.isAcceptDown();
                                isLiked = post.getLikeUid().contains(mAuth.getCurrentUser().getUid());
                                imgbtn_like_detail_post.setActivated(isLiked);
                                for (Comment comment : post.getComments()) {
                                    adapter.addItem(new Comment_Item(comment.getContent(), comment.getUserName()));
                                }
                                StorageReference profile = storage.getReference().child("profiles/" + post.getFrom());
                                StorageReference image = storage.getReference().child("images/" + postId);
                                image.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> imageTask) {
                                        if (imageTask.isSuccessful()) {
                                            if (PostDetailActivity.this.isFinishing()) return;
                                            Glide.with(PostDetailActivity.this)
                                                            .load(imageTask.getResult())
                                                            .into(img_detail_post);
                                            profile.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> profileTask) {
                                                    if (profileTask.isSuccessful()) {
                                                        if (PostDetailActivity.this.isFinishing()) return;
                                                        Glide.with(PostDetailActivity.this)
                                                                .load(profileTask.getResult())
                                                                .into(img_profile_detail_post);
                                                    } else {
                                                        if (PostDetailActivity.this.isFinishing()) return;
                                                        Glide.with(PostDetailActivity.this)
                                                                .load(R.drawable.default_profile)
                                                                .into(img_profile_detail_post);
                                                    }
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }

    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.comment_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new Comment_RecyclerViewAdapter();

        recyclerView.setAdapter(adapter);
    }

    private void saveImageToGallery(){ // 이미지 다운로드
        img_detail_post.setDrawingCacheEnabled(true);
        Bitmap bitmap = img_detail_post.getDrawingCache();
        MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, txt_id_detail_post.getText().toString(),"gun");
    }

}
