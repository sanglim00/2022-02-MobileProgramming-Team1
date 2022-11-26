package ac.kr.kookmin.petdiary;

public class Comment_Item {
    String txt_comment_content;
    String txt_comment_user;
    public Comment_Item(String txt_comment_content, String txt_comment_user){
        this.txt_comment_content = txt_comment_content;
        this.txt_comment_user = txt_comment_user;
    }
    public String getContents(){
        return txt_comment_content;
    }
    public void setContents(String txt_comment_content){
        this.txt_comment_content = txt_comment_content;
    }

    public String getUser(){
        return txt_comment_user;
    }
    public void setUser(String txt_comment_user){
        this.txt_comment_user = txt_comment_user;
    }
}
