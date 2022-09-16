package kr.nanoit.db.impl;

import kr.nanoit.db.UserService;
import kr.nanoit.exception.CreateUserFailedException;
import kr.nanoit.exception.DataBaseInternalError;
import kr.nanoit.object.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class UserServicePostgreSQLImpl implements UserService {

    private final PostgreSqlDbcp dbcp;

    public UserServicePostgreSQLImpl(PostgreSqlDbcp dbcp) {
        this.dbcp = dbcp;

        createTable(PostgreSqlQuerys.createUserTable);
    }

    private void createTable(String query) {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity findById(long userId) {
        try {
            Statement statement;
            ResultSet resultSet;

            try (Connection connection = dbcp.getConnection()) {
                statement = connection.createStatement();
                String sql = " SELECT id,username, password, email FROM user WHERE id = ('" + userId + "' ) ";
                resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(resultSet.getInt(0));
                    return userEntity;
                }
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } catch (Exception e) {
            throw e;
        }


//        statement.executeUpdate()
        // executeUpdate 는 테이블에  INSERT / DELETE / UPDATE 할 시 사용함 수행결과로 int 타입의 값을 반환

        // SELECT id, username, password, email FROM user WHERE id = $1

        // Rows
        // UserEntity userEntity = new UserEntity()';
        // userEntity.setId(rows[0].getString("id"));

        // return userEntity;
        return null;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        try {
            try (Connection conn = dbcp.getConnection()) {
                Statement statement = conn.createStatement();
                int affectedRows = statement.executeUpdate(PostgreSqlQuerys.insertUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail()), statement.RETURN_GENERATED_KEYS);

                if (affectedRows == 0) throw new CreateUserFailedException("Creating user failed");

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userEntity.setId(resultSet.getLong(1));
                        return userEntity;
                    } else {
                        throw new DataBaseInternalError("not found result set");
                    }
                }
            }

            // 실제 저장 쿼리 로직

            // INSERT INTO user (id, username, password, email) VALUE ($1, $2, $3, $4);

            // affected rows = 변경된 로우 수 ( ex 1일때, 0일때 )
            // 1일때는 성공이므로 ID와 함께 리턴
            // 0일때는 실패 이므로 NULL 리턴

            // INSERT 시 ID Return 하는 방법이 있으니 찾아볼것
        } catch (Exception e) {
            log.error("failed saved query", e);
        }
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
