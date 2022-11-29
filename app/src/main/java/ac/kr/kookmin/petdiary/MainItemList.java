package ac.kr.kookmin.petdiary;

public class MainItemList {
    private String uid;
    private String username;
    private String user_icon_img_src;
    private String user_content_img_src;
    private int likes;
    private boolean isLiked;

    public MainItemList(String uid, String username, String user_icon_img_src, String user_content_img_src, int likes, boolean isLiked) {
        this.uid = uid;
        this.username = username;
        this.user_icon_img_src = user_icon_img_src;
        this.user_content_img_src = user_content_img_src;
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUser_icon_img_src() {
        return user_icon_img_src;
    }

    public void setUser_icon_img_src(String user_icon_img_src) {
        this.user_icon_img_src = user_icon_img_src;
    }

    public String getUser_content_img_src() {
        return user_content_img_src;
    }

    public void setUser_content_img_src(String user_content_img_src) {
        this.user_content_img_src = user_content_img_src;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}




