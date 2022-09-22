# History
## TODO
1. HttpServer에 DB 연동
2. UserHandler 오류 수정
3. TodoHandler 구현
4. TodoHandler 테스트 코드 작성 

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
- TodoServicePostgreSQLImpl 구현 ✔
- TodoServicePostgreSQLImpl 테스트 작성 ✔
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
- TodoServicePostgreSQLImpl 구현
  - 삭제된 시간 deleteAt을 빼고 (modified_date , start_date , finish_date , completed 로 수정)
  - 자잘한 오류 수정
- 프로젝트 내 테스트 코드 전체 통과 확인


## Project
└─src
├─main
│  ├─java
│  │  └─kr
│  │      └─nanoit
│  │          │  Main.java
│  │          │  SandBoxHttpServer.java
│  │          │
│  │          ├─db
│  │          │  └─impl
│  │          │      │  PostgreSqlDbcp.java
│  │          │      │  TodoServicePostgreSqlQuerys.java
│  │          │      │  UserServicePostgreSqlQuerys.java
│  │          │      │
│  │          │      ├─todoservice
│  │          │      │      TodoService.java
│  │          │      │      TodoServicePostgreSQLImpl.java
│  │          │      │      TodoServiceTestImpl.java
│  │          │      │
│  │          │      └─userservice
│  │          │              UserService.java
│  │          │              UserServicePostgreSQLImpl.java
│  │          │              UserServiceTestImpl.java
│  │          │
│  │          ├─exception
│  │          │      CreateFailedException.java
│  │          │      DataBaseInternalError.java
│  │          │      DeleteException.java
│  │          │      FindFailedException.java
│  │          │      UpdateException.java
│  │          │
│  │          ├─handler
│  │          │  │  HealthHandler.java
│  │          │  │
│  │          │  ├─common
│  │          │  │      HandlerUtil.java
│  │          │  │      QueryParsing.java
│  │          │  │      Validation.java
│  │          │  │
│  │          │  ├─todo
│  │          │  │      DeleteToto.java
│  │          │  │      GetTodo.java
│  │          │  │      PatchTodo.java
│  │          │  │      PostTodo.java
│  │          │  │      TodoHandler.java
│  │          │  │
│  │          │  └─user
│  │          │          DeleteUser.java
│  │          │          GetUser.java
│  │          │          PatchUser.java
│  │          │          PostUser.java
│  │          │          UserHandler.java
│  │          │
│  │          ├─object
│  │          │  ├─config
│  │          │  │      DataBaseConfig.java
│  │          │  │      DataBaseConfigValidation.java
│  │          │  │
│  │          │  ├─dto
│  │          │  │      HttpResponseDto.java
│  │          │  │      TodoDto.java
│  │          │  │      UserDto.java
│  │          │  │
│  │          │  ├─entity
│  │          │  │      TodoEntity.java
│  │          │  │      UserEntity.java
│  │          │  │
│  │          │  └─model
│  │          └─utils
│  │                  ExchangeRawPrinter.java
│  │                  GlobalVariable.java
│  │                  Mapper.java
│  │                  Pair.java
│  │
│  └─resources
│          logback.xml
│
└─test
├─java
│  └─kr
│      └─nanoit
│          │  SandBoxHttpServerTest.java
│          │
│          ├─db
│          │  └─impl
│          │      │  PostgreSqlDbcpTest.java
│          │      │
│          │      ├─todoservice
│          │      │      TodoServicePostgreSQLImpleTest.java
│          │      │      TodoServiceTestImplTest.java
│          │      │
│          │      └─userservice
│          │              UserServicePostgreSQLImplTest.java
│          │              UserServiceTestImplTest.java
│          │
│          ├─handler
│          │  └─user
│          │          DeleteUserTest.java
│          │          GetUserTest.java
│          │          PatchUserTest.java
│          │          PostUserTest.java
│          │
│          └─utils
│                  MapperTest.java
│
└─resources
logback-test.xml


