package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBTable {
    public static void main(String[] args) {
        //�����ͺ��̽����� ���ῡ ����� ������

        Connection con = null;
        Statement stmt = null;
        String url ="jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
        String user = "root";
        String passwd = "root";

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            con = DriverManager.getConnection(url,user,passwd);
            stmt = con.createStatement();
            // ȸ�����Խ� 0�� 0��, 1000������ �ʱ�ȭ
            String createStr = "CREATE TABLE member (name varchar(20) not null, nickname varchar(20) not null, " +
                    "id varchar(20) not null, password varchar(20) not null, email varchar(40) not null, " +
                    "win int not null, lose int not null, mmr int not null, PRIMARY KEY (nickname, id))";

            stmt.executeUpdate(createStr);
            System.out.println("[Server] ���̺� ���� ����");
        }catch (Exception e){
            System.out.println("[Server] �����ͺ��̽� ���� Ȥ�� ���̺� ������ ���� �߻�" + e.toString());
        }
    }
}
