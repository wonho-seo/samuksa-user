DB 테이블 : user/user.mwb

회원가입 : http://localhost:8080/user/signUp
        변수:
           아이디   : userId
           비밀번호 : passwd
           이메일   : userEmail
           사용자 이름 : userName
           ex) http://localhost:8080/user/signUp?userId=sjdl&passwd=123&userEmail=kdksl@naver.com&userName=wseo

        db에 저장
       
로그인  : http://localhost:8080/user/login
        변수 :
            아이디   : userId
            비밀번호 : passwd
         ex) http://localhost:8080/user/login?userId=sjdl&passwd=123
         
        정상적인 로그인시 response로 jwt토큰값이 들어옴
         ex) eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzamRsIiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNjU5NDUwMjI1LCJleHAiOjE2NTk0NTM4MjV9._d2T0fq-5SsiBgcfzd7RO7kRSkTILXXlWklaATupbN4

        이 jwt토큰값을 헤더에 넣으면 앞으로 아이디 비밀번호를 body에 적지않아도 로그인 상태 유지
        ex) /user/test , /user/user_info , /user/user_delete 는     (1. jwt토큰을 헤더에 넣거나 , 2. body에 아이디 비밀번호를 )  넣을시 작동 아니면 /user/login을 호출하여 로그인 유도
                                                                            

회원탈퇴 : http://localhost:8080/user/user_delete
          변수 :  없음
          
          현재 로그인(jwt토큰 or body에 아이디, 비밀번호) 된 계정을 db에서 제거
