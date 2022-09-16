package kr.nanoit.db.impl;

import kr.nanoit.object.config.DataBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class PostgreSqlDbcp {

    private final DataBaseConfig config;
    private final DataSource pool;

    public PostgreSqlDbcp(DataBaseConfig config) throws ClassNotFoundException {
        this.config = config;
        String connectUrl = String.format("jdbc:postgresql://%s:%d/%s", config.getIp(), config.getPort(), config.getDataBaseName());
        this.pool = setupDataSource(connectUrl, config.getUsername(), config.getPassword());
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public DataSource setupDataSource(String connectURI, String username, String password) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, username, password); // Connection factory로 Connection을 생성 관리
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        return new PoolingDataSource<>(connectionPool);
    }

//    public List<UserEntity> convertedSelectTemplate(String query) throws Exception {
//        List<List<Pair>> pairList =  selectTemplate(query);
//        for (List<Pair> pairs : pairList) {
//            for (Pair pair : pairs) {
//                UserEntity user = new UserEntity();
//                if (pair.getLeft().equals("id")) {
//                    user.setId(Long.parseLong(pair.getRight()));
//                }
//            }
//        }
//    }
//
//    public List<List<Pair>> selectTemplate(String query) throws Exception {
//        Statement stmt = null;
//        ResultSet rset = null;
//
//        try (Connection conn = getConnection()) {
//            stmt = conn.createStatement();
//            rset = stmt.executeQuery(query);
//
//            List<List<Pair>> templateData = new ArrayList<>();
//
//            int numcols = rset.getMetaData().getColumnCount();
//            while (rset.next()) {
//                List<Pair> row = new ArrayList<>();
//                for (int i = 1; i <= numcols; i++) {
//                    row.add(new Pair(rset.getMetaData().getColumnName(i), rset.getString(i)));
//                }
//                templateData.add(row);
//            }
//            return templateData;
//        } catch (SQLException e) {
//            throw new SQLException(e);
//        } finally {
//            try {
//                if (rset != null) rset.close();
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//            try {
//                if (stmt != null) stmt.close();
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//        }
//    }
}
