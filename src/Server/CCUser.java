package Server;


import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

//서버에 접속한 유저와의 메시지 송수신을 관리하는 클래스.
//스레드를 상속받아 연결 요청이 들어왔을 때도 독립적으로 동작할 수 있도록 한다.
class CCUser extends Thread{
    Server server;
    Socket socket;

    /* 각 객체를 Vector로 관리 */
    Vector<CCUser> auser;	//연결된 모든 클라이언트
    Vector<CCUser> wuser;	//대기실에 있는 클라이언트
    Vector<Room> room;		//생성된 Room

    Database db = new Database();

    /* 메시지 송수신을 위한 필드 */
    OutputStream os;
    DataOutputStream dos;
    InputStream is;
    DataInputStream dis;

    String msg;			//수신 메시지를 저장할 필드
    String nickname;	//클라이언트의 닉네임을 저장할 필드
    String mode;        //싱글모드 게임난이도를 저장할 필드
    int currenttime;           //싱글모드 시간기록을 저장할 필드
    String findID;          //클라이언트의 아이디를 저장할 필드
    String findPassword;    //클라이언트의 비밀번호를 저장할 필드
    Room myRoom;		//입장한 방 객체를 저장할 필드

    static int auser_count = 0; // auser 벡터 카운트 초기화

    /* 각 메시지를 구분하기 위한 태그 */
    final String loginTag = "LOGIN";	//로그인
    final String logoutTag = "LOGOUT";  //로그아웃
    final String signupTag = "SIGNUP";	//회원가입
    final String overTag = "OVER";		//중복확인
    final String viewTag = "VIEW";		//회원정보조회
    final String changeTag = "CHANGE";	//회원정보변경
    final String rankTag = "RANK";		//전적조회(전체회원)
    final String croomTag = "C_ROOM";	//방생성
    final String vroomTag = "V_ROOM";	//방목록
    final String uroomTag = "U_ROOM";	//방유저
    final String eroomTag = "E_ROOM";	//방입장
    final String cuserTag = "C_USER";	//접속유저
    final String searchTag = "SEARCH";	//전적조회(한명)
    final String pexitTag = "PEXIT";	//프로그램종료
    final String rexitTag = "REXIT";	//방퇴장
    final String omokTag = "OMOK";		//오목
    final String Multi_winTag = "MULTIWIN";	//싱글모드 승리
    final String Single_winTag = "SINGLEWIN"; //멀티모드 승리
    final String loseTag = "LOSE";		//패배
    final String recordTag = "RECORD";	//전적업데이트
    static String find_id = "FINDID";
    static String find_pw = "FINDPW";


    CCUser(Socket _s, Server _ss) {
        this.socket = _s;
        this.server = _ss;

        auser = server.alluser; // 현재 접속 중인 모든 유저
        wuser = server.waituser; // 현재 대기실에 있는 유저
        room = server.room;
    }  //CCUser()
    public void run() {
        try {
            System.out.println("[Server] 클라이언트 접속 > " + this.socket.toString());

            os = this.socket.getOutputStream();
            dos = new DataOutputStream(os);
            is = this.socket.getInputStream();
            dis = new DataInputStream(is);

            while(true) {
                msg = dis.readUTF();	//메시지 수신을 상시 대기한다.
                // msg = "LOGIN//admin//admin"
                String[] m = msg.split("//");	//msg를 "//"로 나누어 m[] 배열에 차례로 집어넣는다.
                // m["LOGIN", "admin", "admin"]
                System.out.println(Arrays.toString(m));
                // 수신받은 문자열들의 첫 번째 배열(m[0])은 모두 태그 문자. 각 기능을 분리한다.



                /* 로그인 */
                if(m[0].equals(loginTag)) {
                    // loginCheck은 nickname을 반환 m[1]:id, m[2]:pw 에 맞는 nickname
                    String mm = db.loginCheck(m[1], m[2]);

                    if(!mm.equals("null")) {	//로그인 성공
                        nickname = mm;		//로그인한 사용자의 닉네임을 필드에 저장

                        auser.add(this);	//모든 접속 인원에 추가
                        wuser.add(this);	//대기실 접속 인원에 추가

                        System.out.println(auser.get(0).nickname);

                        //System.out.println(auser.get(1));
                        //System.out.println(auser.get(2));

                        dos.writeUTF(loginTag + "//success" + "//" + nickname);

                        sendWait(connectedUser());	//대기실 접속 유저에 모든 접속 인원을 전송

                        if(room.size() > 0) {	//생성된 방의 개수가 0 이상일 때
                            sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
                        }

                    }

                    else {	//로그인 실패
                        dos.writeUTF(loginTag + "//fail");
                    }
                }  //로그인 if문

                /* 회원가입 */
                else if(m[0].equals(signupTag)) {
                    if(db.signupCheck(m[1], m[2], m[3], m[4])) {	//회원가입 성공
                        dos.writeUTF(signupTag + "//success");
                    }

                    /*
                    else {	//회원가입 실패
                        dos.writeUTF(signupTag + "//fail");
                    }
                    */
                }  //회원가입 if문

                /* 중복확인 */
                else if(m[0].equals(overTag)) {
                    if(db.overCheck(m[1], m[2])) {	//사용 가능
                        dos.writeUTF(overTag + "//success");
                    }

                    else {	//사용 불가능
                        dos.writeUTF(overTag + "//fail");
                    }
                }  //중복확인 if문


                //아이디 조회
                else if(m[0].equals(find_id)){  //
                    if(!db.viewID(m[1]).equals("null")){
                        dos.writeUTF(find_id + "//success//" + db.viewID(m[1]));
                    }
                    else
                        dos.writeUTF(find_id + "//FAIL");
                }
                //비밀번호 조회
                else if(m[0].equals(find_pw)){
                    if(!db.viewPW(m[1],m[2]).equals("null")){
                        System.out.println(db.viewPW(m[1], m[2]));
                        dos.writeUTF(find_pw + "//success//" + db.viewPW(m[1],m[2]));
                    }else
                        dos.writeUTF(find_pw + "//FAIL");
                }


                /*
                // 회원정보 조회
                else if(m[0].equals(viewTag)) {
                    if(!db.viewInfo(nickname).equals("null")) {	//조회 성공
                        dos.writeUTF(viewTag + "//" + db.viewInfo(nickname));	//태그와 조회한 내용을 같이 전송
                    }

                    else {	//조회 실패
                        dos.writeUTF(viewTag + "//FAIL");
                    }
                }  //회원정보 조회 if문

                /*
                 */

                /*
                // 회원정보 변경
                else if(m[0].equals(changeTag)) {
                    if(db.changeInfo(nickname, m[1], m[2])) {	//변경 성공
                        dos.writeUTF(changeTag + "//OKAY");
                    }

                    else {	//변경 실패
                        dos.writeUTF(changeTag + "//FAIL");
                    }
                }  //회원정보 변경 if문
                */

                /* 전체 전적 조회 */
                else if(m[0].equals(rankTag)) {
                    if(!db.viewRank().equals("")) {	//조회 성공
                        dos.writeUTF(rankTag + "//" + db.viewRank());	//태그와 조회한 내용을 같이 전송
                    }
                    // db.viewRank() = record//member
                    else {	//조회 실패
                        dos.writeUTF(rankTag + "//FAIL");
                    }
                }  //전체 전적 조회 if문

                /* 방 생성 */
                else if(m[0].equals(croomTag)) {
                    myRoom = new Room();	//새로운 Room 객체 생성 후 myRoom에 초기화
                    myRoom.title = m[1];	//방 제목을 m[1]로 설정
                    myRoom.count++;			//방의 인원수 하나 추가

                    room.add(myRoom);		//room 배열에 myRoom을 추가

                    myRoom.ccu.add(this);	//myRoom의 접속인원에 클라이언트 추가
                    wuser.remove(this);		//대기실 접속 인원에서 클라이언트 삭제

                    dos.writeUTF(croomTag + "//OKAY");
                    System.out.println("[Server] "+ nickname + " : 방 '" + m[1] + "' 생성");

                    sendWait(roomInfo());	//대기실 접속 인원에 방 목록을 전송
                    sendRoom(roomUser());	//방에 입장한 인원에 방 인원 목록을 전송
                }  //방 생성 if문
                /* 오목 */
                else if(m[0].equals(omokTag)) {
                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복

                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트에게만 전송
                            myRoom.ccu.get(i).dos.writeUTF(omokTag + "//" + m[1] + "//" + m[2] + "//" + m[3]);
                        }
                    }
                }  //오목 if문

                /* 싱글모드 승리 및 전적 업데이트 */
                else if(m[0].equals(Single_winTag)) {
                    System.out.println("[Server] " + nickname + " 승리");

                    if(db.Single_winRecord(mode, nickname, currenttime)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                        dos.writeUTF(recordTag + "//OKAY");
                    } else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
                        dos.writeUTF(recordTag + "//FAIL");
                    }

                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복

                        /* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                            myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");

                            if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                            } else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                            }
                        }
                    }
                }

                /* 멀티모드 승리 및 전적 업데이트 */
                else if(m[0].equals(Multi_winTag)) {
                    System.out.println("[Server] " + nickname + " 승리");

                    if(db.Multi_winRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                        dos.writeUTF(recordTag + "//OKAY");
                    } else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
                        dos.writeUTF(recordTag + "//FAIL");
                    }

                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복

                        /* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                            myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");

                            if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                            } else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                            }
                        }
                    }
                }  //승리 및 전적 업데이트 if문

                /* 패배, 기권 및 전적 업데이트 */
                else if(m[0].equals(loseTag)) {
                    if(myRoom.count==1) {	//기권을 했는데 방 접속 인원이 1명일 때 전적 미반영을 전송
                        dos.writeUTF(recordTag + "//NO");
                    }

                    else if(myRoom.count==2) {	//기권 및 패배를 했을 때 방 접속 인원이 2명일 때
                        dos.writeUTF(loseTag + "//");

                        if(db.loseRecord(nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                            dos.writeUTF(recordTag + "//OKAY");
                        } else {						//전적 업데이트가 실패하면 업데이트 실패를 전송
                            dos.writeUTF(recordTag + "//FAIL");
                        }

                        for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom의 인원수만큼 반복

                            /* 방 접속 인원 중 클라이언트와 다른 닉네임의 클라이언트일때만 */
                            if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                                myRoom.ccu.get(i).dos.writeUTF(Multi_winTag + "//");
                                // 수정 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                if(db.Multi_winRecord(myRoom.ccu.get(i).nickname)) {	//전적 업데이트가 성공하면 업데이트 성공을 전송
                                    myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                                } else {										//전적 업데이트가 실패하면 업데이트 실패를 전송
                                    myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                                }
                            }
                        }
                    }
                }  //패배, 기권 및 전적 업데이트 if문
                else if(m[0].equals(logoutTag)) {
                    System.out.println(server.alluser.size());
                    String Client_nick = m[1];
                    int i=0;
                    while(!server.alluser.get(i).nickname.equals(Client_nick)) {
                        i++;
                    }
                    System.out.println("[Server]" + server.alluser.get(i).nickname + "님이 게임을 종료하였습니다.");
                    server.alluser.remove(i);
                    System.out.println(server.alluser.size());
                }
            }  //while문
        } catch(IOException e) {
            System.out.println("[Server] 입출력 오류 > " + e.toString());
        }
    }  //run()
    /* 현재 존재하는 방의 목록을 조회하는 메소드 */
    String roomInfo() {
        String msg = vroomTag + "//";

        for(int i=0; i<room.size(); i++) {
            msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
        }
        return msg;
    }

    /* 클라이언트가 입장한 방의 인원을 조회하는 메소드 */
    String roomUser() {
        String msg = uroomTag + "//";

        for(int i=0; i<myRoom.ccu.size(); i++) {
            msg = msg + myRoom.ccu.get(i).nickname + "@";
        }
        return msg;
    }

    /* 접속한 모든 회원 목록을 조회하는 메소드 */
    String connectedUser() {
        String msg = cuserTag + "//";

        for(int i=0; i<auser.size(); i++) {
            msg = msg + auser.get(i).nickname + "@";
        }
        return msg;
    }

    /* 대기실에 있는 모든 회원에게 메시지 전송하는 메소드 */
    void sendWait(String m) {
        for(int i=0; i<wuser.size(); i++) {
            try {
                wuser.get(i).dos.writeUTF(m);
            } catch(IOException e) {
                wuser.remove(i--);
            }
        }
    }

    /* 방에 입장한 모든 회원에게 메시지 전송하는 메소드 */
    void sendRoom(String m) {
        for(int i=0; i<myRoom.ccu.size(); i++) {
            try {
                myRoom.ccu.get(i).dos.writeUTF(m);
            } catch(IOException e) {
                myRoom.ccu.remove(i--);
            }
        }
    }
}  //CCUser 클래스
