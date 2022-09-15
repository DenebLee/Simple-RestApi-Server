package kr.nanoit.db.impl;

import kr.nanoit.object.config.DataBaseConfig;

import java.sql.Connection;

public class PostgreSqlDbcp {

    private final DataBaseConfig config;
//    private final CP cp; // 2. CP 클래스를 인스턴스화

    public PostgreSqlDbcp(DataBaseConfig config) {
        this.config = config;

        // 초기화
        // 1. DBCP2, HikariCP ( 고르기 )
        // 2. CP 클래스를 인스턴스화
    }

    public Connection getConnection() {
//        return cp.getConnection();
        return null;
    }
}
