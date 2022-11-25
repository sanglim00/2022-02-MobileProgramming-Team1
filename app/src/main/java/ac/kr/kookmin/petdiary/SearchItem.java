package ac.kr.kookmin.petdiary;

public class SearchItem {
    private String userId;          // 검색된 유저의 아이디
    private String userInfo;        // 검색된 유저의 한 줄 소개
    private String profileSrc;      // 검색된 유저의 프로필 사진

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
