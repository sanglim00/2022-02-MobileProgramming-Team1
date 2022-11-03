package ac.kr.kookmin.petdiary;

public class NotiItem {
    private String title;
    private String content;
    private String profileSrc;
    private String postImageSrc;
    private int resourceId;

    public NotiItem(String title, String content, String profileSrc, String postImageSrc, int resourceId) {
        this.title = title;
        this.content = content;
        this.profileSrc = profileSrc;
        this.postImageSrc = postImageSrc;
        this.resourceId = resourceId;
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

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
