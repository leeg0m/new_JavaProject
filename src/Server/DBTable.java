package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBTable {
    public static void main(String[] args) {
        //데이터베이스와의 연결에 사용할 변수들

        Connection conn = null;
        Statement stmt = null;
        String url ="jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
        String user = "root";
        String passwd = "root";

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,passwd);
            stmt = conn.createStatement();

            // 회원정보
            String member_createStr = "CREATE TABLE member (nickname varchar(20) not null, " +
                    "id varchar(20) not null, password varchar(20) not null, " +
                    "win int, lose int, mmr int, PRIMARY KEY (nickname, id))";
            stmt.executeUpdate(member_createStr);

            // 싱글기록
            String Record_createStr = "CREATE TABLE record (nickname varchar(20) not null, time int not null)";
            stmt.executeUpdate(Record_createStr);

            System.out.println("[Server] 테이블 생성 성공");
        }catch (Exception e){
            System.out.println("[Server] 데이터베이스 연결 혹은 테이블 생성에 문제 발생" + e.toString());
        }
    }
}
