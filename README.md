# History

## NEXT
1. TODO API에 대한 유저 인증
2. 로그인
3. 세션 관리 또는 토큰 관리

## USER
- POST 중복 유저 생성
- UPDATE ID 없을때 INTERNAL 에러 발생

## TODO
1. 전달 받는 username, password, email에 대한 검증 로직
2. Exception 처리 
3. Todo 관련 에러 잡기

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
  - 도중 프로젝트 sdk 꼬여서 시간이 오래 걸림
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
#### 2022-09-26
- 전체 테스트 코드 구현 완료 
- Todo와 User @Post @Patch 메소드에서 content-type 검증 중 null체크 로직이 안먹혀 임시로 주석 처리함
#### 2022-09-27
- TodoDto, TodoEntity 수정 
  - {modified_date , start_date , finish_date , completed} -> {createdAt, modifiedAt, content, writer}로 변경 
- 각 로직에서 null로 처리했던 오류를 각 상황에 맞는 Exception 구현해서 던져줌
- 테이블에 중복 userData가 들어가지 못하도록  email에 unique 조건을 걸어 중복 배제 
  - 이에 따른 error 처리 해야됨 
- 기존 로직에선 @UPDATE 시 user{username, password, email} 에 대한 값을 모두 받았지만 셋중 한개만 받아도 처리할 수 있게 수정
#### 2022-09-28
- @PATCH 시 user 정보 전체를 받아야 했던 로직 수정 
- Exception 공부 
- @POST 시 중복 값 확인 후 Exception 처리
#### 2022-09-29
- e-mail 정규식 추가로 검증 로직 추가
- 중복 값 요청받았을때 테이블 조회해서 중복값있으면 bad Request 던져줌
- Todo 사용자 입장에서 writer, content 만 작성하고 넘기도록 수정


## Project Structure
```bash

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

```
