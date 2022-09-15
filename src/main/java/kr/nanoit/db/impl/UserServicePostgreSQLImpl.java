package kr.nanoit.db.impl;

import kr.nanoit.db.UserService;
import kr.nanoit.object.config.DataBaseConfig;
import kr.nanoit.object.entity.UserEntity;

import java.sql.Connection;

public class UserServicePostgreSQLImpl implements UserService {

    private final DataBaseConfig config;
    private final PostgreSqlDbcp dbcp;

    public UserServicePostgreSQLImpl(DataBaseConfig config) {
        this.config = config;
        this.dbcp = new PostgreSqlDbcp(config);

        // 초기화 로직
        // DATABASE 에 USER TABLE 이 있는지 확인
        // 없으면 생성
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        try (Connection connection = dbcp.getConnection()) {
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
