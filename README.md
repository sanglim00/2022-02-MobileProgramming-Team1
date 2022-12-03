# KMU-Mobile-Programming
2022학년도 2학기 모바일프로그래밍 팀 프로젝트 - 반려일지
	:dog::cat::monkey_face::tiger::pig:
## 개발주제
- 반려동물과의 추억을 기록할 수 있는 반려동물 전용 SNS :iphone:

## 개발기간
- 22.09.12 - 22.12.03

## 개발환경
- 안드로이드 스튜디오
- 개발언어 : JAVA
- Real Device: Galaxy S21+ (Android 12), Galaxy Note 10(Android 12)
- Android Virtual Device (AVD): Pixel 2 API 31 (Android 12)
 <img width="327" alt="image" src="https://user-images.githubusercontent.com/54923245/198889938-7a2eca21-8f53-423b-8295-da7091d3b496.png"> 


## 설치 및 실행방법
- Github Repository 최상단 경로의 app-debug.apk 설치
   - 디버깅 용 애플리케이션으로 빌드됨.
- Github Repository Clone 및 Android Studio Run 'app'

## 프로젝트 진행멤버
- [곽희건](https://github.com/VarGun)
- [김민선](https://github.com/CLM-BONNY)
- [남상림](https://github.com/Sanglim00)
- [박정명](https://github.com/j-myeong)
- [송재룡](https://github.com/ft-jasong)

## 서버 및 데이터베이스 관련
> ### Model

- User: 기본적인 사용자 정보를 저장할 수 있는 객체
- Post: 기본적인 게시물 정보를 저장할 수 있는 객체
- Comment: 게시물에 존재하는 댓글 객체
- Notification: FCM을 통한 알림 객체

> ### Service

- FCMService
    - FCM에서 도착한 알림을 모바일에서 받아서 처리할 수 있도록 Broadcast Receiver 서비스 구현
    - 또한, 받아온 알림을 GSON을 이용하여 JSON 형식으로 SharedPreference에 저장할 수 있도록 구현

> ### Activity 공통

- ProgressBar
    - 서버에서 값을 불러오거나 저장하는 등, 시간이 어느정도 걸리는 작업에 대해 시각적으로 표현하기 위하여 로딩창 (ProgressBar) 구현

> ### SplashActivity

- 한 번 로그인 한 계정이 저장되어 있는지 getCurrentUser()를 통해 확인
- 로그인 된 계정이 있을 경우, 알림 전송 시에 필요한 FCM (Firebase Cloud Messaging) 개인 Token이 DB에 저장된 값과 동일한지(Token 갱신유무) 체크하여 새로운 값 저장 또는 기존 값 유지 및 MainActivity로 이동할 준비
- 로그인 된 계정이 없을 경우, LoginActivity로 이동할 준비

> ### LoginActivity

- 로그인 버튼 클릭 시, 입력받은 이메일 및 패스워드를 통해 FirebaseAuth의 signInWithEmailAndPassword() 함수를 통해 사용자 인증 진행
- 사용자 인증 성공 시, FirebaseAuth 내부적으로 SharedPreference에 계정정보 저장 및 자동 로그인 가능
- 사용자 인증 실패 시, Toast를 통해 계정인증 실패 (올바르지 못한 계정) 또는 서버 에러 메시지 구현

> ### SignUpActivity

- 회원가입 버튼 클릭 시, 입력받은 이메일 및 패스워드를 통해 FirebaseAuth의 createUserWithEmailAndPassword() 함수를 통해 사용자 생성 진행
- 사용자 생성 성공 시, 고유 UID 값을 Document ID 값으로 사용하여 회원가입에서 입력한 추가적인 정보 (ID, petName 등) 를 DB에 저장
- 사용자 생성 성공 시, 고유 UID 값을 Storage Reference의 파일 이름으로 사용하여 사용자의 프로필 사진을 저장 (없을 경우, 기본 drawable에 있는 사진)
- 사용자 생성 실패 시, Toast를 통해 계정 가입 실패 또는 서버 에러 메시지 구현
- 회원가입 진행 전, DB에서 같은 이메일로 가입된 회원정보가 있는지 확인 진행. 이미 가입된 회원이 있다면 Toast를 통해 “이미 가입된 회원” 메시지 구현

> ### MainActivity

- DB에서 게시물 (Post) 들을 PetType에 맞춰서 가져옴
- 가져온 Post의 ID 및 Content 값을 RecyclerView에 넣어주기 위하여 새로운 MainItem 객체를 생성하여 ArrayList<MainItem>가 존재하는 mainItemAdapter에 addItem() 함수로 저장
- Adapter Class의 존재하는 onBind() 함수에서 새로운 아이템이 생기면 FirebaseStorage에서 Post 사진 및 사용자의 Profile 사진을 받아와 Glide 라이브러리를 이용하여 화면에 보여지도록 구현 (없을 경우 기본 drawable 이미지)
- Post 클릭 시, Post ID를 PostDetailActivity로 Intent를 통해 넘겨주도록 구현
- PetType 변경 시, 서버에서 새로운 값을 맞춰 가져올 수 있도록 구현
- 좋아요 버튼 클릭 시, Express.js 서버에 Post ID 및 유저 UID를 전송하여 서버에서 좋아요 처리 및 FCM 알림을 전송하도록 AsyncTask를 이용한 구현

> ### Profile 관련 Activity

- ProfileActivity: 내 프로필
    - FirebaseStorage를 통하여 사용자 UID 값으로 저장되어 있는 고유의 프로필 이미지를 받아와 Glide 라이브러리를 통해 화면에 보여지도록 구현 (없을 경우 기본 drawable 이미지)
    - Post는 RecyclerView를 이용해 지금까지 작성했던 자신의 게시물을 시간 내림차순으로 정렬하여 addItem()을 통해 adapter에 저장하도록 구현
    - Adapter Class의 존재하는 onBind() 함수에서 새로운 아이템이 생기면 FirebaseStorage에서 Post 사진을 받아와 Glide 라이브러리를 이용하여 화면에 보여지도록 구현
    - Post 클릭 시, Post ID를 PostDetailActivity로 Intent를 통해 넘겨주도록 구현
- ProfileOtherActivity: 다른 사람의 프로필
    - 기존 ProfileActivity와 구현방식이 동일함
- ProfileEditActivity: 프로필 수정
    - 처음 화면에 들어올 시, FirebaseStorage에 사용자 UID 값으로 저장되어 있는 고유의 프로필 이미지를 받아와 Glide 라이브러리를 통해 화면에 보여지도록 구현 (없을 경우 기본 drawable 이미지)
    - 사용자가 변경한 값을 DB에 저장할 수 있도록 구현

> ### SearchActivity

- 사용자가 입력한 ID 값을 DB에서 맞춰서 사용자 (User) 를 가져옴
- FirebaseFirestore의 whereGreaterThanOrEqualTo() 함수를 이용하여 ID 값이 완벽하게 동일하지 않더라도 유사하다면 검색되도록 구현
- User에 담긴 userName (ID) 및 Comment값을 가져와서 adapter에 addItem() 하도록 구현
- 검색 후, 나타난 사용자의 프로필을 선택 시에 Intent에 검색 된 사용자의 UID값을 넣어 Profile 관련 Activity로 이동하도록 구현
- Adapter Class의 존재하는 onBind() 함수에서 새로운 아이템이 생기면 FirebaseStorage에서 Post 사진을 받아와 Glide 라이브러리를 이용하여 화면에 보여지도록 구현

> ### WritingActivity

- 게시물 작성버튼 클릭 시, FirebaseFirestore에 새로운 게시물 Document가 생성되어 입력한 Content값이 입력되도록 구현
- 새로운 게시물 Document의 ID를 받아와 FirebaseStorage에 ID 값으로 Post 이미지를 저장하도록 구현
- 새로운 게시물 사진 업로드까지 완료 시, 올린 사용자의 UID 값을 Express.js 서버로 전송하여 사용자를 구독하는 다른 사용자에게 FCM 알림을 전송하도록 AsyncTask를 이용한 구현

> ### NotificationActivity

- FCMService를 통해 작성된 SharedPreference를 불러와 JSON → GSON → List<String> 순으로 형식이 변환되고, 받아온 String List를 시간 내림차순으로 정렬하여 RecyclerView에 보여지도록 구현
- Adapter Class의 존재하는 onBind() 함수에서 새로운 아이템이 생기면 FirebaseStorage에서 Post 사진 및 사용자의 Profile 사진을 받아와 Glide 라이브러리를 이용하여 화면에 보여지도록 구현 (없을 경우 기본 drawable 이미지)

> ### PostDetailActivity

- FirebaseStorage에서 Post 사진 및 사용자의 Profile 사진을 받아와 Glide 라이브러리를 이용하여 화면에 보여지도록 구현 (없을 경우 기본 drawable 이미지)
- 또한, Intent를 통해 MainRecyclerView에서 받아온 User ID 값을 화면에 보여지도록 구현
- DB에서 가져온 Post의 댓글 (Comment) 값을 RecyclerView에 넣어주기 위하여 새로운 CommentItem 객체를 생성하여 ArrayList<CommentItem>가 존재하는 commentAdapter에 addItem() 함수로 저장
- 댓글 작성 버튼 클릭 시, Express.js 서버에 Post ID 및 유저 UID, 댓글 내용을 전송하여 서버에서 댓글 작성 처리 및 FCM 알림을 전송하도록 Custom AsyncTask를 이용한 구현

> ### SettingActivity

- 로그아웃 클릭 시, FirebaseAuth의 signOut() 함수를 이용하여 사용자 정보를 지우고 LoginActivity로 이동하는 Intent를 통해 구현


## 페이지별 화면 설명 및 주요 기능 설명
> ### 회원가입페이지 (activity_signup.xml , SignUpActivity)
  - ConstraintLayout 사용
  - ScrollView 사용으로 스크롤 가능
  - 회원가입 진행 화면
  - 이메일 조건 ((영문자, 숫자, “.”, “_”, “%”, “+”, “-“)@(영문자, 숫자, “.”, “-”).(2~6자리의 영문자, 숫자))
    1) 비어있는지 확인
    2) ^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$
      <p>ex) mobile21@.abc.com</p>
  - 비밀번호 조건 (특수키, 숫자, 영어대소문자 포함)
    1) 비어있는지 확인
    2) 비밀번호 글자수 확인(5글자 이상 입력해야 함)
    3) ([0-9].[!,@,#,^,&,,(,)])|([!,@,#,^,&,,(,)].[0-9])
    4) ([a-z].[A-Z])|([A-Z].[a-z])
    5) 비밀번호, 비밀번호 확인 일치 여부 확인
  - 아이디 조건 
    1) 비어있는지 확인
  - 전화번호 조건 (01(1자리의 숫자)(3~4자리의 숫자)(4자리의 숫자))
    1) 비어있는지 확인
    2) ^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$
  - 반려동물 이름 조건
    1) 비어있는지 확인
  - 반려동물 종류 조건
    1) 선택되었는지 확인
  - 반려동물 성별 조건
    1) 선택되었는지 확인
  - 만난 날짜 조건
    1) 선택되었는지 확인
    2) 오늘 이전의 날짜인지 확인
  - 개인정보 이용동의 조건
    1) 동의 선택되었는지 확인
  - 글자수 제한
    1) 추가할 반려동물 종류: 15글자
    2) ID: 15글자
    3) 반려동물 이름: 15글자


> ### 로그인페이지 (activity_login.xml , LoginActivity)
  - ConstraintLayout 사용
  - 로그인이 가능한 화면
  - 이메일 조건 ((영문자, 숫자, “.”, “_”, “%”, “+”, “-“)@(영문자, 숫자, “.”, “-”).(2~6자리의 영문자, 숫자))
    1) 비어있는지 확인
    2) ^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$
    <p>ex) mobile21@.abc.com</p>
  - 비밀번호 조건
    1) 비어있는지 확인
    2) 입력된 이메일과 맞는 비밀번호인지 확인
  - 회원가입 버튼 클릭 시 두 번째 화면으로 이동
 
 
> ### 메인페이지 (MainActivity.java, MainRecyclerAdapter.java, MainItemList.java, activity_main.xml, recycler_main_item.xml)
  -	ConstraintLayout 사용
  -	상단 Pet Type 버튼과 게시물 표시 화면
  -	게시물 View는 RecyclerView 사용
  -	상단 pet type button을 통해 pet type tag에 해당하는 게시물 출력
  -	[+] 버튼을 누를 시, 유저가 원하는 Pet type을 AlertDialog를 통해서 입력받으며, 해당 Pet type에 해당하는 게시물들을 출력. (15자 제한)
    1)	[+] 버튼에 기존에 입력한 값이 없는 경우, 바로 AlertDialog
    2)	기존에 입력한 값이 있는 경우에 눌렀을 경우, 해당 pet type의 게시물들을 보여줌. 다시 버튼을 눌렀을 경우, AlertDialog 출력
    3)	검색 시 모두 lowercase로 검색, 공백이 들어갔을 경우 모두 제거 (ex. “sn  ake  ” -> “snake”)
  -	게시물의 유저 프로필 사진 혹은 이름을 눌렀을 경우 내 uid값과 post한 유저의 uid값 비교 후, other profile activity 혹은 my profile activity로 이동.
  -	게시물의 사진을 눌렀을 경우, post id를 통해 해당 게시글의 Detail page로 이동
  -	좋아요 기능


> ### 게시글 세부사항 페이지 (activity_detail_post.xml, PostDetailActivity.java) 
  - ConstraintLayout 사용
  - 게시글 이미지, 좋아요 버튼, 이미지 다운로드 버튼, 게시글 내용, 댓글 목록, 댓글 작성 기능
  - 좋아요 버튼 클릭 시 ImageView 변경 및 작성자에게 알림 발송
  - 이미지 다운로드 버튼 : 작성자가 작성 시에 해당 게시글 이미지 다운로드 허용 여부 결정
     1) 작성자가 다운로드를 허용하였을 경우 이미지를 갤러리의 Pictures 폴더에 저장
     2) 작성자가 허용하지 않았을 경우 이미지 다운로드 불가
  - 댓글(EditText)란에 내용을 쓰고 등록 버튼 클릭 시 댓글 RecyclerView 생성 및 댓글 목록에 추가



> ### 검색페이지


> ### 글작성페이지


> ### 알림페이지


> ### 유저페이지 (activity_profile.xml, ProfileActivity.java)
  - ConstraintLayout 사용
  - 사용자 정보 (프로필 이미지, 반려동물 이름, 성별, 만난 날짜, 한줄 소개) 및 작성 게시글 목록
  - 게시글 클릭 시 게시글 세부 사항 화면으로 이동
  - 프로필 편집 버튼 클릭 시 프로필 편집 화면으로 이동
  - 프로필 편집 화면 이동 시 사용자 정보 프로필 편집 화면으로 Intent를 통해 전달
  - Setting 버튼 클릭 시 환경설정 화면으로 이동
  - 게시글 RecyclerView 로 구현
     1) View 생성자, Holder, Adapter 파일 생성


> ### 유저 프로필 편집페이지 (activity_profile_edit.xml, Profile_EditActivity.java)
  - LinearLayout 사용 (ProgressBar 제외)
  - 프로필 화면에 보여지는 사용자 정보 수정
  - 프로필 화면으로부터 Intent를 통해 사용자 정보를 받아 적용
  - 프로필 이미지 : 갤러리 혹은 사진 촬영을 통해 이미지 변경 가능
     1) 갤러리 접근 : 최초 이용 시 저장소 접근 권한 허용 요청. 이후 갤러리에 접근해 이미지의 Uri 값을 받아와 비트맵 속성을 이용해 파일 크기를 조정하여 사용
     2) 사진 촬영 : 카메라 어플을 실행하여 촬영한 사진의 이미지를 사용
  - 한줄 소개, 이름 : EditText를 통해 수정
     - TextWatcher 클래스를 사용해서 한줄 소개 및 이름 입력 글자 수 제한
  - 성별 : Spinner 를 사용해 미리 정해져 있는 값 내에서 선택이 가능하도록 함
  - 만난 날짜 : 달력을 구현한 별도의 액티비티(calendarActivity)로 이동해 날짜 정보를 전달 및 저장
     - 선택된 날짜 정보와 현시점의 날짜 정보를 비교해 미래의 날짜는 선택하지 못하도록 구현
  - 완료 버튼 클릭 시 액티비티 종료 후 프로필 화면으로 돌아감
     - 뒤로가기 버튼이 아닌 완료 버튼을 눌렀을 때만 프로필 화면에 수정된 정보가 적용되도록 Boolean 값을 이용해 구분

> ### 다른 사용자 프로필페이지 ( activity_profile_others.xml, Profile_OthersActivity.java )
  - ConstraintLayout 사용
  - 다른 사용자 정보 및 작성 게시글 목록
  - 게시글 클릭 시 게시글 세부 사항 화면으로 이동
  - 구독 버튼(Toggle Button) 클릭 시 해당 사용자 팔로우 기능
     - 구독한 사용자가 게시글을 올리면 알림을 받도록 구현
  - 게시글 RecyclerView 로 구현
     - View 생성자, Holder, Adapter 파일 생성

> ### 세팅페이지

> ### 앱 실행 시 대기화면 (activity_splash.xml, SplashActivity.java)
  - ImageView 사용
  - 앱 구동 시 정보를 받아오는 동안 화면에 나타나는 대기화면
  - 뒤로 가기 버튼으로 인한 종료 방지

## 기능위주 실행화면 예시
