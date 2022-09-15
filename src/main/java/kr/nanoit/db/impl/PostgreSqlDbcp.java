package kr.nanoit.db.impl;

import kr.nanoit.object.config.DataBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;

@Slf4j
public class PostgreSqlDbcp {

    private final DataBaseConfig config;
    private final BasicDataSource basicDataSource;

    public PostgreSqlDbcp(DataBaseConfig config) {
        this.config = config;
        basicDataSource = new BasicDataSource();
        config.setIp("postgres://postgres:postgrespw@localhost")
                .setPort(49154)
                .setDataBaseName("lee")
                .setUsername("lee")
                .setPassword("123123");
    }

    public Connection getConnection()  {
        try {
            basicDataSource.setDriverClassName("org.postgresql.Driver");
            basicDataSource.setUrl(config.getIp() + ":" + config.getPort());
            basicDataSource.setUsername(config.getUsername());
            basicDataSource.setPassword(config.getPassword());

            return basicDataSource.getConnection();

        } catch (Exception e) {
            log.error("Connection error", e);
        }
        return null;
    }
}
