//package kr.nanoit.db;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DBConnect {
//    private Connection connection;
//
//    public DBConnect(Connection connection) throws SQLException {
//        try{
//
//            // 드라이버 로딩
//            Class.forName("");
//
//            // 접속할 DB 정보
//            String url = "";
//
//            // 접속하고 Connection 객체의 참조값 얻어오기(DB 연동의 핵심 객체)
//
//            connection = DriverManager.getConnection(url, "test", "test");
//            System.out.println("DB접속 완료");
//
//
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Connection getConn() {
//        return connection;
//    }
//}