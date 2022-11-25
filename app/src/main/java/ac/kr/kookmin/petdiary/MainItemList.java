package ac.kr.kookmin.petdiary;

public class MainItemList {
    private String username;
    private String user_icon_img_src;
    private String user_content_img_src;

    public MainItemList(String username, String content, String user_icon_img_src, String user_content_img_src) {
        this.username = username;
        this.user_icon_img_src = user_icon_img_src;
        this.user_content_img_src = user_content_img_src;
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
}




