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

> ### 검색페이지

> ### 글작성페이지

> ### 알림페이지

> ### 유저페이지

> ### 세팅페이지

### 기능위주 실행화면 예시
