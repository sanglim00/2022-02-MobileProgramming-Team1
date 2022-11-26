package ac.kr.kookmin.petdiary.models;

public class Notification {
    private String title;
    private String content;
    private String profileSrc;
    private String postImageSrc;
    private String postId;

    public Notification(String title, String content, String profileSrc, String postImageSrc, String postId) {
        this.title = title;
        this.content = content;
        this.profileSrc = profileSrc;
        this.postImageSrc = postImageSrc;
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfileSrc() {
        return profileSrc;
    }

    public void setProfileSrc(String profileSrc) {
        this.profileSrc = profileSrc;
    }

    public String getPostImageSrc() {
        return postImageSrc;
    }

    public void setPostImageSrc(String postImageSrc) {
        this.postImageSrc = postImageSrc;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
