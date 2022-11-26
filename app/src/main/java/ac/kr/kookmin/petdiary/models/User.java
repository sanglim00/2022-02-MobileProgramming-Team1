package ac.kr.kookmin.petdiary.models;

import java.util.ArrayList;

public class User {
    private String email;
    private String userName;
    private String phone;
    private String petName;
    private String petType;
    private String gender;
    private String petBirth;
    private String comment;
    private ArrayList<String> following;
    private ArrayList<String> follower;
    private String fcmToken;

    public User() {

    }

    public User(String email, String userName, String phone, String petName, String petType, String gender, String petBirth) {
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        this.petName = petName;
        this.petType = petType;
        this.gender = gender;
        this.petBirth = petBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetBirth() {
        return petBirth;
    }

    public void setPetBirth(String petBirth) {
        this.petBirth = petBirth;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
