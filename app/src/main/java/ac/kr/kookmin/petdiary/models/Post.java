package ac.kr.kookmin.petdiary.models;

public class Post {
    private String from;
    private String content;
    private int likes = 0;
    private boolean acceptDown;
    private String petType;

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
}
