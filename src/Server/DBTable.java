package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBTable {
    public static void main(String[] args) {
        //�����ͺ��̽����� ���ῡ ����� ������

        Connection conn = null;
        Statement stmt = null;
        String url ="jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
        String user = "root";
        String passwd = "root";

        try{
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,passwd);
            stmt = conn.createStatement();

            // ȸ������
            String member_createStr = "CREATE TABLE member (nickname varchar(20) not null, " +
                    "id varchar(20) not null, password varchar(20) not null, " +
                    "win int, lose int, mmr int, PRIMARY KEY (nickname, id))";
            stmt.executeUpdate(member_createStr);

            // �̱۱��
            String Record_createStr = "CREATE TABLE record (nickname varchar(20) not null, time int not null)";
            stmt.executeUpdate(Record_createStr);

            System.out.println("[Server] ���̺� ���� ����");
        }catch (Exception e){
            System.out.println("[Server] �����ͺ��̽� ���� Ȥ�� ���̺� ������ ���� �߻�" + e.toString());
        }
    }
}
