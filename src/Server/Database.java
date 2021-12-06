package Server;

import java.sql.*;
//Ŭ���̾�Ʈ�� ��û�� �����ͺ��̽� ������Ʈ �� ���� �۾��� �����ϴ� Ŭ����.
//�������� ��ü ���� �ÿ� �����ͺ��̽� ���� �۾��� �����ϰ� �ٸ� �ΰ����� �۾����� �޼ҵ带 ���� �������� �ҷ����� �����ϵ��� �Ѵ�.

public class Database {
    /* �����ͺ��̽����� ���ῡ ����� ������ */
    Connection conn = null; // ����
    Statement state = null; // ����
    String url = "jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
    String user = "root";
    String passwd = "root";

    Database() {	//Database ��ü ���� �� �����ͺ��̽� ������ �����Ѵ�.
        try {	//�����ͺ��̽� ������ try-catch������ ���ܸ� ����ش�.
            //�����ͺ��̽��� �����Ѵ�.
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, passwd);
            state = conn.createStatement();
            System.out.println("[Server] mariaDB ���� ���� ����");	//�����ͺ��̽� ���ῡ �����ϸ� ������ �ַܼ� �˸���.
        } catch(Exception e) {	//�����ͺ��̽� ���ῡ ���ܰ� �߻����� �� ���и� �ַܼ� �˸���.
            System.out.println("[Server] mariaDB ���� ���� ����> " + e.toString());
        }
    }

    //�α��� ���θ� Ȯ���ϴ� �޼ҵ�! ������ �г����� String �������� ��ȯ
    String loginCheck(String _i, String _p) {
        String nickname = "null";	// ��ȯ�� �г��� ������ "null"�� �ʱ�ȭ.

        //�Ű������� ���� id�� password���� id�� pw���� �ʱ�ȭ�Ѵ�.
        String id = _i;
        String pw = _p;

        try {
            //id�� ��ġ�ϴ� ��й�ȣ�� �г����� �ִ��� ��ȸ�Ѵ�.
            String checkingStr = "SELECT password, nickname FROM member WHERE id = '" + id + "'";
            ResultSet result = state.executeQuery(checkingStr);

            int count = 0;
            while(result.next()) {
                //��ȸ�� ��й�ȣ�� pw ���� ��.
                if(pw.equals(result.getString("password"))) { //true�� ��� nickname�� ��ȸ�� �г��ӿ� ��ȯ�ϰ� �α��� ������ �ַܼ� �˸���.
                    nickname = result.getString("nickname");
                    System.out.println("[Server] �α��� ����");
                }

                else {	//false�� ��� nickname�� "null"�� �ʱ�ȭ�ϰ� �α��� ���и� �ַܼ� �˸���.
                    nickname = "null";
                    System.out.println("[Server] �α��� ����");
                }
                count++;
            }
        } catch(Exception e) {	//��ȸ�� �������� �� nickname�� "null"�� �ʱ�ȭ. ���и� �ַܼ� �˸���.
            nickname = "null";
            System.out.println("[Server] �α��� ���� > " + e.toString());
        }

        return nickname;	//nickname ��ȯ
    }

    //ȸ�������� �����ϴ� �޼ҵ�. �����ϸ� true, �����ϸ� false
    boolean signupCheck(String _nickname, String _id, String _password, String _email) {
        boolean flag = false; // �� ������ ��ȯ���� flag ����, �ʱⰪ�� false

        String user_nickname = _nickname;
        String user_id = _id;
        String user_password = _password;
        String user_email = _email;

        try {
            //member ���̺� �� ���ڿ����� ������� ������Ʈ�ϴ� ����. ���� �ʱⰪ�� �ʱ�/�߱�/���/��/�� �� 0, mmr������ 1000���� �Ѵ�.
            String insertStr = "INSERT INTO member VALUES('" + user_nickname + "', '" + user_id + "', '" + user_password + "', '" + user_email +"' ,0,0,0,0,0,1000)";
            state.executeUpdate(insertStr);

            flag = true;	//������Ʈ���� ���������� ����Ǹ� flag�� true�� �ʱ�ȭ�ϰ� ������ �ַܼ� �˸���.
            System.out.println("[Server] ȸ������ ����");
        } catch(Exception e) {	//ȸ������ ������ �������� ���ϸ� flag�� false�� �ʱ�ȭ�ϰ� ���и� �ַܼ� �˸���.
            flag = false;
            System.out.println("[Server] ȸ������ ���� > " + e.toString());
        }
        return flag;	//flag ��ȯ
    }

    //�г��� �̳� ���̵� �� �ߺ��Ǿ����� Ȯ�����ִ� �޼ҵ� , �ߺ��̸� false �ߺ����� ������ true
    boolean overCheck(String _a, String _v) {
        boolean flag = false;   //�������� ��ȯ�� flag ����. �ʱⰪ�� false

        //at t�� �Ӽ�(���̵�, �г���)�� �����ϰ�, val�� Ȯ���� ���� �ʱ�ȭ.
        String att = _a; // �÷�
        String val = _v; // ������ ��

        try {
            //member ���̺� �����ϴ� ���̵�(Ȥ�� �г���)�� ��� ã�´�.
            String selcectStr = "SELECT " + att + " FROM member";
            ResultSet result = state.executeQuery(selcectStr);

            int count = 0;
            // SELECT id FROM member �� Empty �� �� flag�� false ������ ��ȯ �ؾ���
            /*
            if(result == null)
                flag = false;

            */

            while (result.next()) {
                //��ȸ�� ���̵�(Ȥ�� �г���)�� val�� ��.
                if (!val.equals(result.getString(att))) {   // val�� ���� ���� �����ϸ� flag�� true�� �����Ѵ�.
                    flag = true;
                } else {   // val�� ���� ���� �������� ������ flag�� false�� �����Ѵ�.
                    flag = false;
                }
                count++;
            }

            System.out.println("[Server] �ߺ� Ȯ�� ����");   //���������� ����Ǿ��� �� ������ �ַܼ� �˸���.
        } catch(Exception e) {   //���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
            System.out.println("[Server] �ߺ� Ȯ�� ���� > " + e.toString());
        }

        return flag;

    }


    String viewID(String _nn){
        String msg = "null";    //��ȯ�� ���ڿ� ������ "null"�� �ʱ�ȭ.

        //�Ű������� ���� �̸����� _mm�� �ʱ�ȭ
        String _mm = _nn;

        try{
            //member ���̺��� _nn �̸����� ���� ȸ���� id ������ ��ȸ�Ѵ�.
            String findid = "SELECT id FROM member WHERE email = '" + _nn + "'";
            ResultSet result = state.executeQuery(findid);

            int count = 0;
            while(result.next()){
                //msg�� "���̵�" ���·� �ʱ�ȭ�Ѵ�.
                msg = result.getString("id");
                count++;
            }
            System.out.println("[Server] ���̵� ��ȸ ����");
       }catch (Exception e){
            System.out.println("[Server] ���̵� ��ȸ ���� > " + e.toString());
        }

        return msg;
    }

    String viewPW(String _nn, String _mm){
        String msg = "null";    //��ȯ�� ���ڿ� ������ "null"�� �ʱ�ȭ.

        //�Ű������� ���� �̸��ϰ� ���̵� ���� _zz, _xx�� �ʱ�ȭ

        String _zz = _nn;   //�̸���
        String _xx = _mm;   //���̵�

        try{
            //member ���̺��� _nn �̸����� ���� ȸ���� id ������ ��ȸ�Ѵ�.
            String findid = "SELECT password FROM member WHERE email = '" + _nn + "' AND id = '"+ _mm +"'";
            ResultSet result = state.executeQuery(findid);

            int count = 0;
            while(result.next()){
                //msg�� "��й�ȣ" ���·� �ʱ�ȭ�Ѵ�.
                msg = result.getString("password");
                count++;
            }
            System.out.println("[Server] ��й�ȣ ��ȸ ����");
        }catch (Exception e){
            System.out.println("[Server] ��й�ȣ ��ȸ ���� > " + e.toString());
        }

        return msg;
    }


    //DB�� ����� �ڽ��� ������ ��ȸ�ϴ� �޼ҵ�! ��ȸ�� �������� String ���·� ��ȯ
    String viewInfo(String _nn) {
        String msg = "null";	//��ȯ�� ���ڿ� ������ "null"�� �ʱ�ȭ.

        //�Ű������� ���� �г����� nick�� �ʱ�ȭ�Ѵ�.
        String nick = _nn;

        try {
            //member ���̺��� nick�̶�� �г����� ���� ȸ���� �̸��� �̸��� ������ ��ȸ�Ѵ�.
            String viewStr = "SELECT name, email FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(viewStr);

            int count = 0;
            while(result.next()) {
                //msg�� "�̸�//�г���//�̸���" ���·� �ʱ�ȭ�Ѵ�.
                msg = result.getString("name") + "//" + nick + "//" + result.getString("email");
                count++;
            }
            System.out.println("[Server] ȸ������ ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
        } catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
            System.out.println("[Server] ȸ������ ��ȸ ���� > " + e.toString());
        }

        return msg;	//msg ��ȯ
    }


    /*
    //ȸ�������� ������ �����ϴ� �޼ҵ�! ���濡 �����ϸ� true, �����ϸ� false�� ��ȯ�Ѵ�.
    boolean changeInfo(String _nn, String _a, String _v) {
        boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.

        //�Ű������� ���� �������� �ʱ�ȭ�Ѵ�. att�� �Ӽ�(�̸�, �̸���, ��й�ȣ) ���п��̰� val�� �ٲ� ��.
        String nick = _nn;
        String att = _a;
        String val = _v;

        try {
            //member ���̺��� nick�̶�� �г����� ���� ȸ���� att(�̸�, �̸���, ��й�ȣ)�� val�� �����Ѵ�.
            String changeStr = "UPDATE member SET " + att + "='" + val + "' WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);

            flag = true;	//���������� ����Ǹ� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
            System.out.println("[Server] ȸ������ ���� ����");
        } catch(Exception e) {	//���������� �������� ���ϸ� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
            flag = false;
            System.out.println("[Server] ȸ������ ���� ���� > " + e.toString());
        }
        return flag;	//flag ��ȯ
    }
    */

    //��ü ȸ���� ������ ��ȸ�ϴ� �޼ҵ�. ��� ȸ���� ������ String ���·� ��ȯ�Ѵ�.
    String viewRank() {
        String record_msg = ""; //�̱۸�� ������ ���� ���ڿ�.
        String member_msg = "";	//��Ƽ��� ������ ���� ���ڿ�. �ʱⰪ�� ""�� �Ѵ�.
        String msg = "";

        try {
            //single ���̺��� �г���, �ð��� ��� ��ȸ
            //member ���̺��� �г���, ��, �и� ��� ��ȸ
            String record_viewStr = "SELECT nickname, easyrecord, normalrecord, hardrecord FROM member";
            ResultSet record_result = state.executeQuery(record_viewStr);
            String member_viewStr = "SELECT nickname, win, lose, mmr FROM member";
            ResultSet member_result = state.executeQuery(member_viewStr);

            int count = 0;
            while(record_result.next()) {
                //������ msg�� "�г��� : n�� n��@" ������ ���ڿ��� ����ؼ� �߰��Ѵ�.
                record_msg = record_msg + member_result.getString("nickname") + " : " + member_result.getInt("win") + "�� " + member_result.getInt("lose") + "��"
                        + member_result.getInt("mmr") + "��";
                count++;
            }
            while(member_result.next()) {
                //������ msg�� "�г��� : n�� n��@" ������ ���ڿ��� ����ؼ� �߰��Ѵ�.
                member_msg = member_msg + member_result.getString("nickname") + " : " + member_result.getInt("win") + "�� " + member_result.getInt("lose") + "��"
                        + member_result.getInt("mmr") + "��";
                count++;
            }
            System.out.println("[Server] ���� ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
        } catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
            System.out.println("[Server] ���� ��ȸ ���� > " + e.toString());
        }
        msg = record_msg + "//" + member_msg;
        return msg;	//�̱۸��, ��Ƽ��� ��� ���� ��ȯȯ
    }
    /*
    //�� ���� ȸ���� ������ ��ȸ�ϴ� �޼ҵ�. �ش� ȸ���� ������ String ���·� ��ȯ�Ѵ�.
    String searchRank(String _nn) {
        String msg = "null";	//������ ���� ���ڿ�. �ʱⰪ�� "null"�� �Ѵ�.

        //�Ű������� ���� �г����� �ʱ�ȭ�Ѵ�.
        String nick = _nn;

        try {
            //member ���̺��� nick�̶�� �г����� ���� ȸ���� ��, �и� ��ȸ�Ѵ�.
            String searchStr = "SELECT win, lose FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //msg�� "�г��� : n�� n��" ������ ���ڿ��� �ʱ�ȭ�Ѵ�.
                msg = nick + " : " + result.getInt("win") + "�� " + result.getInt("lose") + "��";
                count++;
            }
            System.out.println("[Server] ���� ��ȸ ����");	//���������� ����Ǹ� ������ �ַܼ� �˸���.
        } catch(Exception e) {	//���������� �������� ���ϸ� ���и� �ַܼ� �˸���.
            System.out.println("[Server] ���� ��ȸ ���� > " + e.toString());
        }

        return msg;	//msg ��ȯ
    }
    */
    // <�̱۸��> ���� �¸� �� ������ ����Ʈ���ϴ� �޼ҵ�! ��ȸ �� ������Ʈ ���� �� true, ���� �� false ��ȯ
    boolean Single_winRecord(String _tag, String _nn, int _time) {
        boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.

        //�Ű������� ���� �г��Ӱ� ��ȸ�� �¸� Ƚ���� ������ ����. num�� �ʱⰪ�� 0.
        String mode = _tag;
        String nick = _nn;
        int current_time = _time;
        int oldtime = 0;
        
        // �ʱ� ��� ��� ������Ʈ
        if(mode.equals("EASYMODE")) {
            try {
                //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� ��ȸ�Ѵ�.
                String searchStr = "SELECT easyrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num�� ��ȸ�� �¸� Ƚ���� �ʱ�ȭ.
                    oldtime = result.getInt("easyrecord");
                    if (current_time < oldtime) {
                        //tag�� �ʱ޸�� �� ���, member ���̺��� easyrecord�� ������Ʈ �Ѵ�.
                        String changeStr = "UPDATE member SET easyrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
                        System.out.println("[Server] ���� ������Ʈ ����");
                    }
                    count++;
                }

            } catch (Exception e) {    //��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
                flag = false;
                System.out.println("[Server] ���� ������Ʈ ���� > " + e.toString());
            }
        }
        
        // �߱� ��� ��� ������Ʈ
        if(mode.equals("NORMALMODE")) {
            try {
                //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� ��ȸ�Ѵ�.
                String searchStr = "SELECT easyrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num�� ��ȸ�� �¸� Ƚ���� �ʱ�ȭ.
                    oldtime = result.getInt("easyrecord");
                    if (current_time < oldtime) {
                        //tag�� �ʱ޸�� �� ���, member ���̺��� easyrecord�� ������Ʈ �Ѵ�.
                        String changeStr = "UPDATE member SET easyrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
                        System.out.println("[Server] ���� ������Ʈ ����");
                    }
                    count++;
                }

            } catch (Exception e) {    //��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
                flag = false;
                System.out.println("[Server] ���� ������Ʈ ���� > " + e.toString());
            }
        }
        
        // ��� ��� ��� ������Ʈ
        if(mode.equals("HARDMODE")) {
            try {
                //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� ��ȸ�Ѵ�.
                String searchStr = "SELECT easyrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num�� ��ȸ�� �¸� Ƚ���� �ʱ�ȭ.
                    oldtime = result.getInt("easyrecord");
                    if (current_time < oldtime) {
                        //tag�� �ʱ޸�� �� ���, member ���̺��� easyrecord�� ������Ʈ �Ѵ�.
                        String changeStr = "UPDATE member SET easyrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
                        System.out.println("[Server] ���� ������Ʈ ����");
                    }
                    count++;
                }

            } catch (Exception e) {    //��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
                flag = false;
                System.out.println("[Server] ���� ������Ʈ ���� > " + e.toString());
            }
        }
        return flag;	//flag ��ȯ
    }

    // <��Ƽ���> ���� �¸� �� ������ ������Ʈ�ϴ� �޼ҵ�! ��ȸ �� ������Ʈ ���� �� true, ���� �� false ��ȯ
    boolean Multi_winRecord(String _nn) {
        boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.

        //�Ű������� ���� �г��Ӱ� ��ȸ�� �¸� Ƚ���� ������ ����. num�� �ʱⰪ�� 0.
        String nick = _nn;
        int win_num = 0;
        int mmr_num = 0;

        try {
            //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� ��ȸ�Ѵ�.
            String searchStr = "SELECT win, mmr FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //num�� ��ȸ�� �¸� Ƚ���� �ʱ�ȭ.
                win_num = result.getInt("win");
                mmr_num = result.getInt("mmr");
                count++;
            }
            win_num++;	//�¸� Ƚ���� �ø�
            mmr_num += 20;  //mmr ������ �ø�

            //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� num���� ������Ʈ�Ѵ�.
            String changeStr = "UPDATE member SET win=" + win_num + ",mmr=" + mmr_num + " WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);
            flag = true;	//��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
            System.out.println("[Server] ���� ������Ʈ ����");
        } catch(Exception e) {	//��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
            flag = false;
            System.out.println("[Server] ���� ������Ʈ ���� > " + e.toString());
        }

        return flag;	//flag ��ȯ
    }

    //���� �й� �� ������ ������Ʈ�ϴ� �޼ҵ�! ��ȸ �� ������Ʈ ���� �� true, ���� �� false ��ȯ
    boolean loseRecord(String _nn) {
        boolean flag = false;	//�������� ��ȯ�� flag ����. �ʱⰪ�� false.

        //�Ű������� ���� �г��Ӱ� ��ȸ�� �й� Ƚ���� ������ ����. num�� �ʱⰪ�� 0.
        String nick =  _nn;
        int num = 0;

        try {
            //member ���̺��� nick�̶�� �г����� ���� ȸ���� �й� Ƚ���� ��ȸ�Ѵ�.
            String searchStr = "SELECT lose FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //num�� ��ȸ�� �й� Ƚ���� �ʱ�ȭ.
                num = result.getInt("lose");
                count++;
            }
            num++;	//�й� Ƚ���� �ø�

            //member ���̺��� nick�̶�� �г����� ���� ȸ���� �¸� Ƚ���� num���� ������Ʈ�Ѵ�.
            String changeStr = "UPDATE member SET lose=" + num + " WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);
            flag = true;	//��ȸ �� ������Ʈ ���� �� flag�� true�� �ٲٰ� ������ �ַܼ� �˸���.
            System.out.println("[Server] ���� ������Ʈ ����");
        } catch(Exception e) {	//��ȸ �� ������Ʈ ���� �� flag�� false�� �ٲٰ� ���и� �ַܼ� �˸���.
            flag = false;
            System.out.println("[Server] Error: > " + e.toString());
        }

        return flag;	//flag ��ȯ
    }
}
