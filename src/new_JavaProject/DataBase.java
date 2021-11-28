package new_JavaProject;

//프로그램 첫 실행 시 프로그램에 필요한 테이블을 생성하는 클래스.
//Server 클래스에도 main메소드가 있기 때문에 Server와 상관없이 단독으로 실행된다.

import java.sql.*;

public class DataBase {
    Connection db_connection = null;
    Statement db_statement = null;
    String url = ""; //데이터베이스 로케이션
    String user = ""; //데이터베이스 계정 이름
    String password = ""; //데이터베이스 계정 비밀번호

    DataBase() {
        //try-catch문으로 서버 연동 성공 유무를 알려줌줌
        try { // database 객체 생성 시 DB서버와 연결
            //DB와 연결
            Class.forName(""); //com.mysql.cj.jdbc.Driver
            db_connection = DriverManager.getConnection(url, user, password);
            db_statement = db_connection.createStatement();
            System.out.println("[SERVER] 서버 연동 성공!");
        } catch (Exception e) {
            //DB와 연결
            System.out.println("[SERVER] 서버 연동 실패 > " + e.toString());
        }
    }

    //로그인 여부를 확인하는 메소드
    String loginCheck(String _id, String _pw) {
        String nickname = "null";

        String user_id = _id;
        String user_pw = _pw;

        try {
            //id와 일치하는 비밀번호와 닉네임이 있는지 조회한다.
            String checkingStr = "SELECT password, nickname FROM member WHERE id=' " + user_id + " ' ";//DB에서 조회해서 비교하는 select 구문
            ResultSet checkResult = db_statement.executeQuery(checkingStr);

            int count = 0;

            while(checkResult.next()) {
                //조회한 비밀번호와 pw 비교
                if (user_pw.equals(checkResult.getString("password"))) {
                    //true 일 경우 nickname 에 조회한 닉네임에반화
                    nickname = checkResult.getString("nickname");
                    System.out.println("[SERVER] 로그인 성공");
                } else {
                    //false일때 nickname을 null 로 초기화하고 콘솔로 알림
                    nickname = "null";
                    System.out.println("[SERVER] 로그인 실패");
                }
                count++;
            }
        } catch(Exception e) { //조회에 실패앴을때 닉네임을 null 로 초기화
            nickname = "null";
            System.out.println("[SERVER] 로그인 실패 > " + e.toString());
        }
        return nickname; //닉네임 반환
    }

    //회원가입을 수행하는 메소드. 성공하면 true, 실패하면 false
    boolean signupCheck(String _nickname, String _id, String _password) {
        boolean flag = false; // 참 거짓을 반환나는 flag 변수, 초기값은 false

        String user_nickname = _nickname;
        String user_id = _id;
        String user_password = _password;

        try {
            //member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 승, 패는 초기값을 숫자 0으로 한다.
            String insertStr = "INSERT INTO member VALUES('" + user_nickname + "', '" + user_id + "', '" + user_password + "', 0,0)";
            db_statement.executeUpdate(insertStr);

            flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
            System.out.println("[Server] 회원가입 성공");
        } catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
            flag = false;
            System.out.println("[Server] 회원가입 실패 > " + e.toString());
        }
        return flag;	//flag 반환
    }

    //넥네임 이나 아이디 가 중복되었는지 확인해주는 메소드 , 중복이면 false 중복이지 않으면 true
    boolean overCheck(String _a, String _v) {
        boolean flag =false; // 참거짓, 초기값이 false

        String attribute = _a; //속성 - 아이디 비번
        String val = _v; // 확인할 값이 초기화 ?? 무스말인지 몰겠음

        try {
            //member 테이블에 존재하는 아이디 or 닉네임을 모두 찾는다

            String selectStr = "SELECT" + attribute + "FROM member ";
            ResultSet result = db_statement.executeQuery(selectStr);

            int count = 0;

            while (result.next() ) {
                //조회한 아이디 or 닉네임과 val을 비교
                if (val.equals(result.getString(attribute))) {  //val 과 같은 것이 있다면 flag값을 true 로 변경
                    flag = true;
                } else { //val 과 갑튼 것이 없다면 false로 변경함
                    flag = false;
                }
                count++;
            }
            System.out.println("[SERVER] 중복 확인 성공! > " );
        } catch (Exception e) {
            System.out.println("[SERVER] 중복 확인 실패! > " + e.toString());
        }
            return flag;
    }


    //데이터 베이스에 저장된 자신의 정보를 조회하는 메소드. 조회한 정보를 문자열로 반환
    /*
        //데이터 베이스에 저장된 자신의 정보를 조회하는 메소드. 조회한 정보를 문자열로 반환
        // 없어도 될꺼 같아서 일단 건너뜀
    String view_Information (String _nickname) {
        String msg ="null"; //반환할 정보

        String user_nickname = _nickname; //받은 닉네임을 user)_nickname에 초기화함

        try {
            Sting viewStr = "SELECT name, "
        } catch(Exception e) {

        }
        return msg;
    }
    */


    //전체회원 전적조회

    //전적시스템 개편에따라 변경 가능 현재 승 / 패만 집계 가능
    String view_allRank() {
        String msg = ""; //전적을 받을 메소드 최기값은 " "'

        try {
            //member 테이블의 닉네임, 승 , 패를 모두 조회
            String viewStr = "SELECT nickanme, win, lose FROM member";
            ResultSet result = db_statement.executeQuery(viewStr);

            int count = 0;

            while(result.next()) {
                //기존의 msg 문자열에 "닉네임 : n승 n패 " 형태의 문자열을 계속 추가함
                msg = msg + result.getString("nickname") + " : " + result.getInt("win") + " 승 " + result.getInt("lose") + " 패 ";
                count++;

            }
            System.out.println("[SERVER] 전적 조회 성공");
        } catch(Exception e) {
            System.out.println("[SERVER] 전적 조회 실패 > " + e.toString());
        }
        return msg;
    }


    //게임 승리 시 전적 업데이트
    //기존 승리 횟수에 +1 을 하는 형태가 아니라 초기화 후 업데이트 형식임
    boolean win_Record(String _nickname) {
            boolean flag = false;

            String user_nickname = _nickname;

            int num = 0;

            try {
                //member 테이블에서 해당 닉네임을 가진 회원의 승리횟수를 조회
                String searchStr = "SELECT win FROM member WHERE nickname='" + user_nickname + "'";
                ResultSet result = db_statement.executeQuery(searchStr);

                int count = 0;

                while (result.next()) {
                    //num에 조회한 승수를 초기화
                    num = result.getInt("win");
                    count++;
                }
                num++;

                //member 테이블에서 해당 닉네임을 가진 회원의 승리횟수를 num으로 업데이트
                String updateStr = "UPDATE member SET win =" + num + " WHERE nickname='" + user_nickname + "'";
                db_statement.executeUpdate(updateStr);
                flag =true;
                System.out.println("[SERVER] 전적 업데이트 성공");

            } catch (Exception e) {
                flag = false;
                System.out.println("[SERVER] 전적 업데이트 실패 >" + e.toString() );
            }
        return flag;
    }


    //게임 패배 시 전적 업데이트
    //기존 승리 횟수에 +1 을 하는 형태가 아니라 초기화 후 업데이트 형식임
    boolean lose_Record(String _nickname) {
        boolean flag = false;

        String user_nickname = _nickname;

        int num = 0;

        try {
            //member 테이블에서 해당 닉네임을 가진 회원의 승리횟수를 조회
            String searchStr = "SELECT lose FROM member WHERE nickname='" + user_nickname + "'";
            ResultSet result = db_statement.executeQuery(searchStr);

            int count = 0;

            while (result.next()) {
                //num에 조회한 승수를 초기화
                num = result.getInt("lose");
                count++;
            }
            num++;

            //member 테이블에서 해당 닉네임을 가진 회원의 승리횟수를 num으로 업데이트
            String updateStr = "UPDATE member SET lose =" + num + " WHERE nickname='" + user_nickname + "'";
            db_statement.executeUpdate(updateStr);
            flag =true;
            System.out.println("[SERVER] 전적 업데이트 성공");

        } catch (Exception e) {
            flag = false;
            System.out.println("[SERVER] 전적 업데이트 실패 >" + e.toString() );
        }
        return flag;
    }



    }
