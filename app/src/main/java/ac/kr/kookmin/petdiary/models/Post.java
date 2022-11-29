package ac.kr.kookmin.petdiary.models;

import java.util.ArrayList;

public class Post {
    private String from;
    private String content;
    private int likes = 0;
    private boolean acceptDown;
    private String petType;
    private ArrayList<String> likeUid = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    public Post() {

    }

    public Post(String from, String content, boolean acceptDown, String petType) {
        this.from = from;
        this.content = content;
        this.acceptDown = acceptDown;
        this.petType = petType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isAcceptDown() {
        return acceptDown;
    }

    public void setAcceptDown(boolean acceptDown) {
        this.acceptDown = acceptDown;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public ArrayList<String> getLikeUid() {
        return likeUid;
    }

    public void setLikeUid(ArrayList<String> likeUid) {
        this.likeUid = likeUid;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
