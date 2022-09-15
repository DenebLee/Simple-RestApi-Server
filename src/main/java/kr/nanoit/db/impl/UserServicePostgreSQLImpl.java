package kr.nanoit.db.impl;

import kr.nanoit.db.UserService;
import kr.nanoit.object.config.DataBaseConfig;
import kr.nanoit.object.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class UserServicePostgreSQLImpl implements UserService {

    private final DataBaseConfig config;
    private final PostgreSqlDbcp dbcp;
    private final Statement statement;
    private final Connection connection;

    public UserServicePostgreSQLImpl(DataBaseConfig config, String sql) throws SQLException {
        // 초기화 로직
        this.config = config;
        this.dbcp = new PostgreSqlDbcp(config);
        this.connection = dbcp.getConnection();
        this.statement = connection.createStatement();

        // DATABASE 에 USER TABLE 이 있는지 확인
        // 없으면 생성
        try {
            if (dbcp.getConnection() == null) {

                statement.executeQuery("CREATE TABLE IF NOT EXITSTS USER (ID SERIAL PRIMARY KEY ," +
                        " USERNAME VARCHAR(50) UNIQUE NOT NULL ," +
                        " PASSWORD VARCHAR(50) NOT NULL , " +
                        "EMAIL VARCHAR(355) UNIQUE NOT NULL" +
                        ")");
            }
        } catch (SQLException e) {
            log.error("TABLE CREATE FAILED", e);
        }
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        try {
            statement.executeQuery("INSERT INTO USER (ID, USERNAME, PASSWORD,EMAIL) VALUES ()");

            // 실제 저장 쿼리 로직

            // INSERT INTO user (id, username, password, email) VALUE ($1, $2, $3, $4);

            // affected rows = 변경된 로우 수 ( ex 1일때, 0일때 )
            // 1일때는 성공이므로 ID와 함께 리턴
            // 0일때는 실패 이므로 NULL 리턴

            // INSERT 시 ID Return 하는 방법이 있으니 찾아볼것
        } catch (Exception e) {
            // 쿼리 로직 실행 중 알수없는 에러가 발생할때
        } finally {
            // 자원 정리
        }
        return null;
    }

    @Override
    public UserEntity findById(long userId) {
//        statement.executeUpdate()
        // executeUpdate는 테이블에  INSERT / DELETE / UPDATE 할 시 사용함 수행결과로 int 타입의 값을 반환

        // SELECT id, username, password, email FROM user WHERE id = $1

        // Rows
        // UserEntity userEntity = new UserEntity()';
        // userEntity.setId(rows[0].getString("id"));

        // return userEntity;
        return null;
    }

    @Override
    public boolean deleteById(long userId) {
        // DELETE FROM user
        // affected row = user 테이블에 로우 수만큼

        // DELETE FROM user WHERE id = $1;
        // 0 또는 1
        return false;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        // String query = UPDATE user SET
//        if (userEntity.getId().isNotBlank()) {
//            query + "id = $1"
//        }

        // WHERE id = $1 (userEntity.getId)
        // affected rows =

        return null;
    }

    @Override
    public boolean containsById(long id) {
        return false;
    }
}
