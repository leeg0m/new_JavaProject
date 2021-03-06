package Server;

import java.sql.*;
import java.util.Vector;
//클라이언트가 요청한 데이터베이스 업데이트 및 쿼리 작업을 수행하는 클래스.
//서버에서 객체 생성 시에 데이터베이스 연동 작업을 수행하고 다른 부가적인 작업들은 메소드를 통해 서버에서 불려지면 수행하도록 한다.

public class Database {
    /* 데이터베이스와의 연결에 사용할 변수들 */
    Connection conn = null; // 연결
    Statement state = null; // 상태
    String url = "jdbc:mariadb://localhost:3306/minesweeper?serverTimezone=Asia/Seoul";
    String user = "root";
    String passwd = "root";

    Database() {	//Database 객체 생성 시 데이터베이스 서버와 연결한다.
        try {	//데이터베이스 연결은 try-catch문으로 예외를 잡아준다.
            //데이터베이스와 연결한다.
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, passwd);
            state = conn.createStatement();
            System.out.println("[Server] mariaDB 서버 연동 성공");	//데이터베이스 연결에 성공하면 성공을 콘솔로 알린다.
        } catch(Exception e) {	//데이터베이스 연결에 예외가 발생했을 때 실패를 콘솔로 알린다.
            System.out.println("[Server] mariaDB 서버 연동 실패> " + e.toString());
        }
    }

    //로그인 여부를 확인하는 메소드! 서버에 닉네임을 String 형식으로 반환
    String loginCheck(String _i, String _p) {
        String nickname = "null";	// 반환할 닉네임 변수를 "null"로 초기화.

        //매개변수로 받은 id와 password값을 id와 pw값에 초기화한다.
        String id = _i;
        String pw = _p;

        try {
            //id와 일치하는 비밀번호와 닉네임이 있는지 조회한다.
            String checkingStr = "SELECT password, nickname FROM member WHERE id = '" + id + "'";
            ResultSet result = state.executeQuery(checkingStr);

            int count = 0;
            while(result.next()) {
                //조회한 비밀번호와 pw 값을 비교.
                if(pw.equals(result.getString("password"))) { //true일 경우 nickname에 조회한 닉네임에 반환하고 로그인 성공을 콘솔로 알린다.
                    nickname = result.getString("nickname");
                    System.out.println("[Server] 로그인 성공");
                }

                else {	//false일 경우 nickname을 "null"로 초기화하고 로그인 실패를 콘솔로 알린다.
                    nickname = "null";
                    System.out.println("[Server] 로그인 실패");
                }
                count++;
            }
        } catch(Exception e) {	//조회에 실패했을 때 nickname을 "null"로 초기화. 실패를 콘솔로 알린다.
            nickname = "null";
            System.out.println("[Server] 로그인 실패 > " + e.toString());
        }

        return nickname;	//nickname 반환
    }

    //회원가입을 수행하는 메소드. 성공하면 true, 실패하면 false
    boolean signupCheck(String _nickname, String _id, String _password, String _email) {
        boolean flag = false; // 참 거짓을 반환나는 flag 변수, 초기값은 false

        String user_nickname = _nickname;
        String user_id = _id;
        String user_password = _password;
        String user_email = _email;

        try {
            //member 테이블에 각 문자열들을 순서대로 업데이트하는 문장. 각각 초기값을 초급/중급/고급 는 10000초, 승/패 는 0, mmr점수는 1000으로 한다.
            String insertStr = "INSERT INTO member VALUES('" + user_nickname + "', '" + user_id + "', '" + user_password + "', '" + user_email +"' , '" + Integer.MAX_VALUE + "' , '" + Integer.MAX_VALUE + "' , '" +Integer.MAX_VALUE + "' ,0,0,1000)";
            state.executeUpdate(insertStr);

            flag = true;	//업데이트문이 정상적으로 수행되면 flag를 true로 초기화하고 성공을 콘솔로 알린다.
            System.out.println("[Server] 회원가입 성공");
        } catch(Exception e) {	//회원가입 절차를 수행하지 못하면 flag를 false로 초기화하고 실패를 콘솔로 알린다.
            flag = false;
            System.out.println("[Server] 회원가입 실패 > " + e.toString());
        }
        return flag;	//flag 반환
    }

    //닉네임 이나 아이디 가 중복되었는지 확인해주는 메소드 , 중복이면 false 중복이지 않으면 true
    boolean overCheck(String _a, String _v) {
        boolean flag = false;   //참거짓을 반환할 flag 변수. 초기값은 false

        //at t는 속성(아이디, 닉네임)을 구분하고, val은 확인할 값이 초기화.
        String att = _a; // 컬럼
        String val = _v; // 데이터 값

        try {
            //member 테이블에 존재하는 아이디(혹은 닉네임)를 모두 찾는다.
            String selcectStr = "SELECT " + att + " FROM member";
            ResultSet result = state.executeQuery(selcectStr);

            int count = 0;
            // SELECT id FROM member 가 Empty 일 때 flag를 false 값으로 반환 해야함
            /*
            if(result == null)
                flag = false;

            */

            while (result.next()) {
                //조회한 아이디(혹은 닉네임)과 val을 비교.
                if (!val.equals(result.getString(att))) {   // val과 같은 것이 존재하면 flag를 true로 변경한다.
                    flag = true;
                } else {   // val과 같은 것이 존재하지 않으면 flag를 false로 변경한다.
                    flag = false;
                }
                count++;
            }

            System.out.println("[Server] 중복 확인 성공");   //정상적으로 수행되었을 때 성공을 콘솔로 알린다.
        } catch(Exception e) {   //정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
            System.out.println("[Server] 중복 확인 실패 > " + e.toString());
        }

        return flag;

    }


    String viewID(String _nn){
        String msg = "null";    //반환할 문자열 변수를 "null"로 초기화.

        //매개변수로 받은 이메일을 _mm에 초기화
        String _mm = _nn;

        try{
            //member 테이블에서 _nn 이메일을 가진 회원의 id 정보를 조회한다.
            String findid = "SELECT id FROM member WHERE email = '" + _nn + "'";
            ResultSet result = state.executeQuery(findid);

            int count = 0;
            while(result.next()){
                //msg에 "아이디" 형태로 초기화한다.
                msg = result.getString("id");
                count++;
            }
            System.out.println("[Server] 아이디 조회 성공");
       }catch (Exception e){
            System.out.println("[Server] 아이디 조회 실패 > " + e.toString());
        }

        return msg;
    }

    String viewPW(String _nn, String _mm){
        String msg = "null";    //반환할 문자열 변수를 "null"로 초기화.

        //매개변수로 받은 이메일과 아이디를 각각 _zz, _xx에 초기화

        String _zz = _nn;   //이메일
        String _xx = _mm;   //아이디

        try{
            //member 테이블에서 _nn 이메일을 가진 회원의 id 정보를 조회한다.
            String findid = "SELECT password FROM member WHERE email = '" + _nn + "' AND id = '"+ _mm +"'";
            ResultSet result = state.executeQuery(findid);

            int count = 0;
            while(result.next()){
                //msg에 "비밀번호" 형태로 초기화한다.
                msg = result.getString("password");
                count++;
            }
            System.out.println("[Server] 비밀번호 조회 성공");
        }catch (Exception e){
            System.out.println("[Server] 비밀번호 조회 실패 > " + e.toString());
        }

        return msg;
    }


    //DB에 저장된 자신의 정보를 조회하는 메소드! 조회한 정보들을 String 형태로 반환
    String viewInfo(String _nn) {
        String msg = "null";	//반환할 문자열 변수를 "null"로 초기화.

        //매개변수로 받은 닉네임을 nick에 초기화한다.
        String nick = _nn;

        try {
            //member 테이블에서 nick이라는 닉네임을 가진 회원의 이름과 이메일 정보를 조회한다.
            String viewStr = "SELECT name, email FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(viewStr);

            int count = 0;
            while(result.next()) {
                //msg에 "이름//닉네임//이메일" 형태로 초기화한다.
                msg = result.getString("name") + "//" + nick + "//" + result.getString("email");
                count++;
            }
            System.out.println("[Server] 회원정보 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
        } catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
            System.out.println("[Server] 회원정보 조회 실패 > " + e.toString());
        }

        return msg;	//msg 반환
    }


    /*
    //회원정보를 변경을 수행하는 메소드! 변경에 성공하면 true, 실패하면 false를 반환한다.
    boolean changeInfo(String _nn, String _a, String _v) {
        boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.

        //매개변수로 받은 정보들을 초기화한다. att는 속성(이름, 이메일, 비밀번호) 구분용이고 val은 바꿀 값.
        String nick = _nn;
        String att = _a;
        String val = _v;

        try {
            //member 테이블에서 nick이라는 닉네임을 가진 회원의 att(이름, 이메일, 비밀번호)를 val로 변경한다.
            String changeStr = "UPDATE member SET " + att + "='" + val + "' WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);

            flag = true;	//정상적으로 수행되면 flag를 true로 바꾸고 성공을 콘솔로 알린다.
            System.out.println("[Server] 회원정보 변경 성공");
        } catch(Exception e) {	//정상적으로 수행하지 못하면 flag를 false로 바꾸고 실패를 콘솔로 알린다.
            flag = false;
            System.out.println("[Server] 회원정보 변경 실패 > " + e.toString());
        }
        return flag;	//flag 반환
    }
    */

    //전체 회원의 전적을 조회하는 메소드. 모든 회원의 전적을 String 형태로 반환한다.
    String viewRank() {
        String msg = "";	//전적을 받을 문자열. 초기값은 ""로 한다.

        try {
            //member 테이블의 닉네임, 승, 패를 모두 조회한다.
            String viewStr = "SELECT nickname, easyrecord, normalrecord, hardrecord, win, lose, mmr FROM member";
            ResultSet result = state.executeQuery(viewStr);

            int count = 0;
            while(result.next()) {
                //기존의 msg에 "닉네임 : n승 n패@" 형태의 문자열을 계속해서 추가한다.
                msg = msg + result.getString("nickname") + " : " + "초급(" +result.getInt("easyrecord")+ "초) 중급(" +result.getInt("normalrecord")+ "초) 고급(" +result.getInt("hardrecord")+ "초) " + result.getInt("win") + "승 " + result.getInt("lose") + "패 "+ result.getInt("mmr") + "점" + "//";
                count++;
            }
            System.out.println("[Server] 전적 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
        } catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
            System.out.println("[Server] 전적 조회 실패 > " + e.toString());
        }

        return msg;	//msg 반환
    }
    /*
    //한 명의 회원의 전적을 조회하는 메소드. 해당 회원의 전적을 String 형태로 반환한다.
    String searchRank(String _nn) {
        String msg = "null";	//전적을 받을 문자열. 초기값은 "null"로 한다.

        //매개변수로 받은 닉네임을 초기화한다.
        String nick = _nn;

        try {
            //member 테이블에서 nick이라는 닉네임을 가진 회원의 승, 패를 조회한다.
            String searchStr = "SELECT win, lose FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //msg에 "닉네임 : n승 n패" 형태의 문자열을 초기화한다.
                msg = nick + " : " + result.getInt("win") + "승 " + result.getInt("lose") + "패";
                count++;
            }
            System.out.println("[Server] 전적 조회 성공");	//정상적으로 수행되면 성공을 콘솔로 알린다.
        } catch(Exception e) {	//정상적으로 수행하지 못하면 실패를 콘솔로 알린다.
            System.out.println("[Server] 전적 조회 실패 > " + e.toString());
        }

        return msg;	//msg 반환
    }
    */
    // <싱글모드> 게임 승리 시 전적을 업데트하하는 메소드! 조회 및 업데이트 성공 → true, 실패 → false 반환
    boolean Single_winRecord(String _mode, String _nn, int _time) {
        boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.

        //매개변수로 받은 닉네임과 조회한 승리 횟수를 저장할 변수. num의 초기값은 0.
        String mode = _mode;
        String nick = _nn;
        int current_time = _time;
        int oldtime;

        // 초급 모드 기록 업데이트
        if(mode.equals("EASYMODE")) {
            try {
                //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
                String searchStr = "SELECT easyrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num에 조회한 승리 횟수를 초기화.
                    oldtime = result.getInt("easyrecord");
                    if (current_time < oldtime) {
                        //tag가 초급모드 일 경우, member 테이블에서 easyrecord를 업데이트 한다.
                        String changeStr = "UPDATE member SET easyrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
                        System.out.println("[Server] 전적 업데이트 성공");
                    }
                    count++;
                }

            } catch (Exception e) {    //조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
                flag = false;
                System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
            }
        }

        // 중급 모드 기록 업데이트
        if(mode.equals("NORMALMODE")) {
            try {
                //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
                String searchStr = "SELECT normalrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num에 조회한 승리 횟수를 초기화.
                    oldtime = result.getInt("normalrecord");
                    if (current_time < oldtime) {
                        //mode가 중급모드 일 경우, member 테이블에서 normalrecord를 업데이트 한다.
                        String changeStr = "UPDATE member SET normalrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
                        System.out.println("[Server] 전적 업데이트 성공");
                    }
                    count++;
                }

            } catch (Exception e) {    //조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
                flag = false;
                System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
            }
        }

        // 고급 모드 기록 업데이트
        if(mode.equals("HARDMODE")) {
            try {
                //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
                String searchStr = "SELECT hardrecord FROM member WHERE nickname='" + nick + "'";
                ResultSet result = state.executeQuery(searchStr);

                int count = 0;
                while (result.next()) {
                    //num에 조회한 승리 횟수를 초기화.
                    oldtime = result.getInt("hardrecord");
                    if (current_time < oldtime) {
                        //tag가 초급모드 일 경우, member 테이블에서 easyrecord를 업데이트 한다.
                        String changeStr = "UPDATE member SET hardrecord=" + current_time + " WHERE nickname='" + nick + "'";
                        state.executeUpdate(changeStr);
                        flag = true;    //조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
                        System.out.println("[Server] 전적 업데이트 성공");
                    }
                    count++;
                }

            } catch (Exception e) {    //조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
                flag = false;
                System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
            }
        }
        return flag;	//flag 반환
    }


    // <멀티모드> 게임 승리 시 전적을 업데이트하는 메소드! 조회 및 업데이트 성공 → true, 실패 → false 반환
    boolean Multi_winRecord(String _nn) {
        boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.

        //매개변수로 받은 닉네임과 조회한 승리 횟수를 저장할 변수. num의 초기값은 0.
        String nick = _nn;
        int win_num = 0;
        int mmr_num = 0;

        try {
            //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 조회한다.
            String searchStr = "SELECT win, mmr FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //num에 조회한 승리 횟수를 초기화.
                win_num = result.getInt("win");
                mmr_num = result.getInt("mmr");
                count++;
            }
            win_num++;	//승리 횟수를 올림
            mmr_num += 20;  //mmr 점수를 올림

            //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
            String changeStr = "UPDATE member SET win=" + win_num + ",mmr=" + mmr_num + " WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);
            flag = true;	//조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
            System.out.println("[Server] 전적 업데이트 성공");
        } catch(Exception e) {	//조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
            flag = false;
            System.out.println("[Server] 전적 업데이트 실패 > " + e.toString());
        }

        return flag;	//flag 반환
    }

    //게임 패배 시 전적을 업데이트하는 메소드! 조회 및 업데이트 성공 → true, 실패 → false 반환
    boolean loseRecord(String _nn) {
        boolean flag = false;	//참거짓을 반환할 flag 변수. 초기값은 false.

        //매개변수로 받은 닉네임과 조회한 패배 횟수를 저장할 변수. num의 초기값은 0.
        String nick =  _nn;
        int num = 0;

        try {
            //member 테이블에서 nick이라는 닉네임을 가진 회원의 패배 횟수를 조회한다.
            String searchStr = "SELECT lose FROM member WHERE nickname='" + nick + "'";
            ResultSet result = state.executeQuery(searchStr);

            int count = 0;
            while(result.next()) {
                //num에 조회한 패배 횟수를 초기화.
                num = result.getInt("lose");
                count++;
            }
            num++;	//패배 횟수를 올림

            //member 테이블에서 nick이라는 닉네임을 가진 회원의 승리 횟수를 num으로 업데이트한다.
            String changeStr = "UPDATE member SET lose=" + num + " WHERE nickname='" + nick +"'";
            state.executeUpdate(changeStr);
            flag = true;	//조회 및 업데이트 성공 시 flag를 true로 바꾸고 성공을 콘솔로 알린다.
            System.out.println("[Server] 전적 업데이트 성공");
        } catch(Exception e) {	//조회 및 업데이트 실패 시 flag를 false로 바꾸고 실패를 콘솔로 알린다.
            flag = false;
            System.out.println("[Server] Error: > " + e.toString());
        }

        return flag;	//flag 반환
    }
}
