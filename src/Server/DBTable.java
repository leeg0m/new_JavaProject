package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBTable {
    public static void main(String[] args) {
        //데이터베이스와의 연결에 사용할 변수들

        Connection con = null;
        Statement stmt = null;
        String url ="jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
        String user = "root";
        String passwd = "root";

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(url,user,passwd);
            stmt = con.createStatement();
            // 회원가입시 0승 0패, 1000점으로 초기화
            String createStr = "CREATE TABLE member (name varchar(20) not null, nickname varchar(20) not null, " +
                    "id varchar(20) not null, password varchar(20) not null, email varchar(40) not null, " +
                    "win int not null, lose int not null, mmr int not null, PRIMARY KEY (nickname, id))";

            stmt.executeUpdate(createStr);
            System.out.println("[Server] 테이블 생성 성공");
        }catch (Exception e){
            System.out.println("[Server] 데이터베이스 연결 혹은 테이블 생성에 문제 발생" + e.toString());
        }
    }
}
