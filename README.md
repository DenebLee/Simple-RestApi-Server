# History
## TODO
1. TodoServicePostgreSQLImpl 구현
2. TodoServicePostgreSQLImpl 테스트 작성
3. 싱글톤 패턴으로 리펙토링 

## DONE
- UserService.class 테스트 코드 작성 및 내부 코드 수정 ✔
- PostHandler.class 테스트 코드 작성 ✔
- PatchHandler.class 테스트 코드 작성 ✔
- DeleteHandler.class 테스트 코드 작성 ✔
- TodoHandler 시작 ✔
- PostgreSqlDbcp 구현 ✔
- PostgreSqlDbcp 테스트 작성 ✔
- UserServicePostgreSQLImpl 구현 ✔ 
- UserServicePostgreSQLImpl 테스트 작성 ✔
### COMMIT
####  2022-09-02
- http server 생성
- /user 패스에 GET, POST, PATCH, DELETE 핸들러 생성
- 고정값 리턴
#### 2022-09-06
- @GET,@POST,@DELETE,@PATCH Handler 생성
- @GET Handler 최적화 및 생성 
- Refactoring
#### 2022-09-14
- @GET,@POST,@DELETE,@PATCH TestHandler 코드 작성 완료 
- UserTest 
- UserServiceTestImpl 테스트 코드 작성 완료 
- TODO 작업 시작
#### 2022-09-16
- UserServicePostgreSQLImplTest 작성 
  - CreateTable 테스트 코드 작성 완료 
  - Save(user) 테스트 코드 작성 완료 
- Exception 관련 지식 습득
#### 2022-09-20
- UserServicePostgreSQLImpl 테스트 구현 완료 
  - 도중 프로젝트 sdk꼬여서 시간이 올래걸림
#### 2022-09-21
- TodoServicePostgreSQLImpl 구현
  - @POST, @SAVE 구현
  - TimeStamp 변환 과정에서 index parsing error로 자바 시간 관련 공부를 진행
  - 오류 수정 완료 
#### 2022-09-22 
- TodoServicePostgreSQLImpl 구현 중 
  - 
