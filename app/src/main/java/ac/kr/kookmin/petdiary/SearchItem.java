package ac.kr.kookmin.petdiary;

public class SearchItem {
    private String userId;
    private String userInfo;
    private String profileSrc;

    public SearchItem(String userId, String userInfo, String profileSrc) {
        this.userId = userId;
        this.userInfo = userInfo;
        this.profileSrc = profileSrc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getProfileSrc() {
        return profileSrc;
    }

    public void setProfileSrc(String profileSrc) {
        this.profileSrc = profileSrc;
    }
}
