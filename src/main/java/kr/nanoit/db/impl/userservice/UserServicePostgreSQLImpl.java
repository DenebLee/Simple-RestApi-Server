package kr.nanoit.db.impl.userservice;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.db.impl.UserServicePostgreSqlQuerys;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.exception.DeleteException;
import kr.nanoit.exception.FindFailedException;
import kr.nanoit.exception.UpdateException;
import kr.nanoit.object.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class UserServicePostgreSQLImpl implements UserService {

    private final PostgreSqlDbcp dbcp;

    public UserServicePostgreSQLImpl(PostgreSqlDbcp dbcp) {
        this.dbcp = dbcp;
        createTable(UserServicePostgreSqlQuerys.createUserTable);
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
        ResultSet resultSet = null;
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserServicePostgreSqlQuerys.selectUser(userId))) {
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(resultSet.getLong("id"));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setPassword(resultSet.getString("password"));
                    userEntity.setEmail(resultSet.getString("email"));
                    return userEntity;
                }
            } else {
                throw new FindFailedException("not found result set");

            }
        } catch (Exception e) {
            log.error("failed found query", e);
        }
        return null;
    }

    @Override
    public UserEntity save(UserEntity userEntity) throws CreateFailedException {
        try (Connection connection = dbcp.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(UserServicePostgreSqlQuerys.columnDuplicateValue(userEntity.getUsername(), userEntity.getEmail()));
            int resultCount = 0;
            if (rs.next()) {
                resultCount = rs.getInt(1);
            }
            if (resultCount > 0) {
                throw new CreateFailedException("Duplicate value exists in column");
            }

            int affectedRows = statement.executeUpdate(UserServicePostgreSqlQuerys.insertUser(userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail()), statement.RETURN_GENERATED_KEYS);
            if (affectedRows == 0) {
                throw new CreateFailedException("not found result set");
            }
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    userEntity.setId(resultSet.getLong(1));
                    return userEntity;
                } else {
                    throw new CreateFailedException("not found result set");
                }
            }
        } catch (CreateFailedException e) {
            throw new CreateFailedException(e.getReason());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CreateFailedException(e.getMessage());
        }
    }


    @Override
    public boolean deleteById(long userId) {
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserServicePostgreSqlQuerys.deleteUser(userId))) {
            int affectedRow = preparedStatement.executeUpdate();
            if (affectedRow == 0) {
                throw new DeleteException("There are no deleted rows");
            }
        } catch (Exception e) {
            log.error("failed deleted query", e);
            return false;
        }
        return true;
    }

    @Override

    public UserEntity update(UserEntity userEntity) throws UpdateException {
        if (userEntity != null) {
            try (Connection connection = dbcp.getConnection(); Statement statement = connection.createStatement();) {
                int affectRow = 0;

                if (userEntity.getUsername() != null) {
                    affectRow = statement.executeUpdate(UserServicePostgreSqlQuerys.updateUser("username", userEntity.getUsername(), userEntity.getId()));
                }
                if (userEntity.getPassword() != null) {
                    affectRow = statement.executeUpdate(UserServicePostgreSqlQuerys.updateUser("password", userEntity.getPassword(), userEntity.getId()));
                }
                if (userEntity.getEmail() != null) {
                    affectRow = statement.executeUpdate(UserServicePostgreSqlQuerys.updateUser("email", userEntity.getEmail(), userEntity.getId()));
                }
                if (affectRow == 0) {
                    throw new UpdateException("No correction made");
                }
                userEntity = findById(userEntity.getId());
                statement.close();
                return userEntity;

            } catch (UpdateException e) {
                log.error(e.getMessage());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new UpdateException("userEntity is null");
    }

    @Override
    public boolean containsById(long id) {
        try (Connection connection = dbcp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserServicePostgreSqlQuerys.containsById(id))) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    return resultSet.getInt("COUNT") == 1;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("failed updated query", e);
        }
        return false;
    }
}
