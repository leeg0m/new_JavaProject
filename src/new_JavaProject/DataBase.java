package new_JavaProject;

//���α׷� ù ���� �� ���α׷��� �ʿ��� ���̺��� �����ϴ� Ŭ����.
//Server Ŭ�������� main�޼ҵ尡 �ֱ� ������ Server�� ������� �ܵ����� ����ȴ�.

import java.sql.*;

public class DataBase {
    Connection db_connection = null;
    Statement db_statement = null;
    String url = ""; //�����ͺ��̽� �����̼�
    String user = ""; //�����ͺ��̽� ���� �̸�
    String password = ""; //�����ͺ��̽� ���� ��й�ȣ

    DataBase() {
        //try-catch������ ���� ���� ���� ������ �˷�����
        try { // database ��ü ���� �� DB������ ����
            //DB�� ����
            Class.forName(""); //com.mysql.cj.jdbc.Driver
            db_connection = DriverManager.getConnection(url, user, password);
            db_statement = db_connection.createStatement();
            System.out.println("[SERVER] ���� ���� ����!");
        } catch (Exception e) {
            //DB�� ����
            System.out.println("[SERVER] ���� ���� ���� > " + e.toString());
        }
    }

    //�α��� ���θ� Ȯ���ϴ� �޼ҵ�
    String loginCheck(String _id, String _pw) {
        String nickname = "null";

        String user_id = _id;
        String user_pw = _pw;

        try {
            //id�� ��ġ�ϴ� ��й�ȣ�� �г����� �ִ��� ��ȸ�Ѵ�.
            String checkingStr = "SELECT password, nickname FROM member WHERE id=' " + user_id + " ' ";//DB���� ��ȸ�ؼ� ���ϴ� select ����
            ResultSet checkResult = db_statement.executeQuery(checkingStr);

            int count = 0;

            while(checkResult.next()) {
                //��ȸ�� ��й�ȣ�� pw ��
                if (user_pw.equals(checkResult.getString("password"))) {
                    //true �� ��� nickname �� ��ȸ�� �г��ӿ���ȭ
                    nickname = checkResult.getString("nickname");
                    System.out.println("[SERVER] �α��� ����");
                } else {
                    //false�϶� nickname�� null �� �ʱ�ȭ�ϰ� �ַܼ� �˸�
                    nickname = "null";
                    System.out.println("[SERVER] �α��� ����");
                }
                count++;
            }
        } catch(Exception e) { //��ȸ�� ���о����� �г����� null �� �ʱ�ȭ
            nickname = "null";
            System.out.println("[SERVER] �α��� ���� > " + e.toString());
        }
        return nickname; //�г��� ��ȯ
    }

    //ȸ�������� �����ϴ� �޼ҵ�. �����ϸ� true, �����ϸ� false
    boolean signupCheck(String _nickname, String _id, String _password) {
        boolean flag = false; // �� ������ ��ȯ���� flag ����, �ʱⰪ�� false

        String user_nickname = _nickname;
        String user_id = _id;
        String user_password = _password;

        try {
            //member ���̺� �� ���ڿ����� ������� ������Ʈ�ϴ� ����. ��, �д� �ʱⰪ�� ���� 0���� �Ѵ�.
            String insertStr = "INSERT INTO member VALUES('" + user_nickname + "', '" + user_id + "', '" + user_password + "', 0,0)";
            db_statement.executeUpdate(insertStr);

            flag = true;	//������Ʈ���� ���������� ����Ǹ� flag�� true�� �ʱ�ȭ�ϰ� ������ �ַܼ� �˸���.
            System.out.println("[Server] ȸ������ ����");
        } catch(Exception e) {	//ȸ������ ������ �������� ���ϸ� flag�� false�� �ʱ�ȭ�ϰ� ���и� �ַܼ� �˸���.
            flag = false;
            System.out.println("[Server] ȸ������ ���� > " + e.toString());
        }
        return flag;	//flag ��ȯ
    }

    //�س��� �̳� ���̵� �� �ߺ��Ǿ����� Ȯ�����ִ� �޼ҵ� , �ߺ��̸� false �ߺ����� ������ true
    boolean overCheck(String _a, String _v) {
        boolean flag =false; // ������, �ʱⰪ�� false

        String attribute = _a; //�Ӽ� - ���̵� ���
        String val = _v; // Ȯ���� ���� �ʱ�ȭ ?? ���������� ������

        try {
            //member ���̺� �����ϴ� ���̵� or �г����� ��� ã�´�

            String selectStr = "SELECT" + attribute + "FROM member ";
            ResultSet result = db_statement.executeQuery(selectStr);

            int count = 0;

            while (result.next() ) {
                //��ȸ�� ���̵� or �г��Ӱ� val�� ��
                if (val.equals(result.getString(attribute))) {  //val �� ���� ���� �ִٸ� flag���� true �� ����
                    flag = true;
                } else { //val �� ��ư ���� ���ٸ� false�� ������
                    flag = false;
                }
                count++;
            }
            System.out.println("[SERVER] �ߺ� Ȯ�� ����! > " );
        } catch (Exception e) {
            System.out.println("[SERVER] �ߺ� Ȯ�� ����! > " + e.toString());
        }
            return flag;
    }


    //������ ���̽��� ����� �ڽ��� ������ ��ȸ�ϴ� �޼ҵ�. ��ȸ�� ������ ���ڿ��� ��ȯ
    /*
        //������ ���̽��� ����� �ڽ��� ������ ��ȸ�ϴ� �޼ҵ�. ��ȸ�� ������ ���ڿ��� ��ȯ
        // ��� �ɲ� ���Ƽ� �ϴ� �ǳʶ�
    String view_Information (String _nickname) {
        String msg ="null"; //��ȯ�� ����

        String user_nickname = _nickname; //���� �г����� user)_nickname�� �ʱ�ȭ��

        try {
            Sting viewStr = "SELECT name, "
        } catch(Exception e) {

        }
        return msg;
    }
    */


    //��üȸ�� ������ȸ

    //�����ý��� �������� ���� ���� ���� �� / �и� ���� ����
    String view_allRank() {
        String msg = ""; //������ ���� �޼ҵ� �ֱⰪ�� " "'

        try {
            //member ���̺��� �г���, �� , �и� ��� ��ȸ
            String viewStr = "SELECT nickanme, win, lose FROM member";
            ResultSet result = db_statement.executeQuery(viewStr);

            int count = 0;

            while(result.next()) {
                //������ msg ���ڿ��� "�г��� : n�� n�� " ������ ���ڿ��� ��� �߰���
                msg = msg + result.getString("nickname") + " : " + result.getInt("win") + " �� " + result.getInt("lose") + " �� ";
                count++;

            }
            System.out.println("[SERVER] ���� ��ȸ ����");
        } catch(Exception e) {
            System.out.println("[SERVER] ���� ��ȸ ���� > " + e.toString());
        }
        return msg;
    }


    //���� �¸� �� ���� ������Ʈ
    //���� �¸� Ƚ���� +1 �� �ϴ� ���°� �ƴ϶� �ʱ�ȭ �� ������Ʈ ������
    boolean win_Record(String _nickname) {
            boolean flag = false;

            String user_nickname = _nickname;

            int num = 0;

            try {
                //member ���̺��� �ش� �г����� ���� ȸ���� �¸�Ƚ���� ��ȸ
                String searchStr = "SELECT win FROM member WHERE nickname='" + user_nickname + "'";
                ResultSet result = db_statement.executeQuery(searchStr);

                int count = 0;

                while (result.next()) {
                    //num�� ��ȸ�� �¼��� �ʱ�ȭ
                    num = result.getInt("win");
                    count++;
                }
                num++;

                //member ���̺��� �ش� �г����� ���� ȸ���� �¸�Ƚ���� num���� ������Ʈ
                String updateStr = "UPDATE member SET win =" + num + " WHERE nickname='" + user_nickname + "'";
                db_statement.executeUpdate(updateStr);
                flag =true;
                System.out.println("[SERVER] ���� ������Ʈ ����");

            } catch (Exception e) {
                flag = false;
                System.out.println("[SERVER] ���� ������Ʈ ���� >" + e.toString() );
            }
        return flag;
    }


    //���� �й� �� ���� ������Ʈ
    //���� �¸� Ƚ���� +1 �� �ϴ� ���°� �ƴ϶� �ʱ�ȭ �� ������Ʈ ������
    boolean lose_Record(String _nickname) {
        boolean flag = false;

        String user_nickname = _nickname;

        int num = 0;

        try {
            //member ���̺��� �ش� �г����� ���� ȸ���� �¸�Ƚ���� ��ȸ
            String searchStr = "SELECT lose FROM member WHERE nickname='" + user_nickname + "'";
            ResultSet result = db_statement.executeQuery(searchStr);

            int count = 0;

            while (result.next()) {
                //num�� ��ȸ�� �¼��� �ʱ�ȭ
                num = result.getInt("lose");
                count++;
            }
            num++;

            //member ���̺��� �ش� �г����� ���� ȸ���� �¸�Ƚ���� num���� ������Ʈ
            String updateStr = "UPDATE member SET lose =" + num + " WHERE nickname='" + user_nickname + "'";
            db_statement.executeUpdate(updateStr);
            flag =true;
            System.out.println("[SERVER] ���� ������Ʈ ����");

        } catch (Exception e) {
            flag = false;
            System.out.println("[SERVER] ���� ������Ʈ ���� >" + e.toString() );
        }
        return flag;
    }



    }
