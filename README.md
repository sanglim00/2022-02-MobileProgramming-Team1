# KMU-Mobile-Programming
2022학년도 2학기 모바일프로그래밍 팀 프로젝트 - 반려일지

### 개발주제
- 반려동물과의 추억을 기록할 수 있는 반려동물 전용 SNS :iphone:

### 개발기간
- 22.09.12 - 22.12.03

### 개발환경
- 안드로이드 스튜디오
- 개발언어 : JAVA
- Real Device: Galaxy S21+ (Android 12), Galaxy Note 10(Android 12)
- Android Virtual Device (AVD): Pixel 2 API 31 (Android 12)
 <img width="327" alt="image" src="https://user-images.githubusercontent.com/54923245/198889938-7a2eca21-8f53-423b-8295-da7091d3b496.png"> 


### 설치 및 실행방법
- Github Repository 최상단 경로의 app-debug.apk 설치
   - 디버깅 용 애플리케이션으로 빌드됨.
- Github Repository Clone 및 Android Studio Run 'app'

### 프로젝트 진행멤버
- [곽희건](https://github.com/VarGun)
- [김민선](https://github.com/CLM-BONNY)
- [남상림](https://github.com/Sanglim00)
- [박정명](https://github.com/j-myeong)
- [송재룡](https://github.com/ft-jasong)

### 페이지별 화면 설명 및 주요 기능 설명
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

### 기능위주 실행화면 예시
