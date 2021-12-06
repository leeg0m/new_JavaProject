package Server;


import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

//������ ������ �������� �޽��� �ۼ����� �����ϴ� Ŭ����.
//�����带 ��ӹ޾� ���� ��û�� ������ ���� ���������� ������ �� �ֵ��� �Ѵ�.
class CCUser extends Thread{
    Server server;
    Socket socket;

    /* �� ��ü�� Vector�� ���� */
    Vector<CCUser> auser;	//����� ��� Ŭ���̾�Ʈ
    Vector<CCUser> wuser;	//���ǿ� �ִ� Ŭ���̾�Ʈ
    Vector<Room> room;		//������ Room

    Database db = new Database();

    /* �޽��� �ۼ����� ���� �ʵ� */
    OutputStream os;
    DataOutputStream dos;
    InputStream is;
    DataInputStream dis;

    String msg;			//���� �޽����� ������ �ʵ�
    String nickname;	//Ŭ���̾�Ʈ�� �г����� ������ �ʵ�
    String mode;        //�̱۸�� ���ӳ��̵��� ������ �ʵ�
    int currenttime;           //�̱۸�� �ð������ ������ �ʵ�
    String findID;          //Ŭ���̾�Ʈ�� ���̵� ������ �ʵ�
    String findPassword;    //Ŭ���̾�Ʈ�� ��й�ȣ�� ������ �ʵ�
    Room myRoom;		//������ �� ��ü�� ������ �ʵ�

    static int auser_count = 0; // auser ���� ī��Ʈ �ʱ�ȭ

    /* �� �޽����� �����ϱ� ���� �±� */
    final String loginTag = "LOGIN";	//�α���
    final String logoutTag = "LOGOUT";  //�α׾ƿ�
    final String signupTag = "SIGNUP";	//ȸ������
    final String overTag = "OVER";		//�ߺ�Ȯ��
    final String viewTag = "VIEW";		//ȸ��������ȸ
    final String changeTag = "CHANGE";	//ȸ����������
    final String rankTag = "RANK";		//������ȸ(��üȸ��)
    final String croomTag = "C_ROOM";	//�����
    final String vroomTag = "V_ROOM";	//����
    final String uroomTag = "U_ROOM";	//������
    final String eroomTag = "E_ROOM";	//������
    final String cuserTag = "C_USER";	//��������
    final String searchTag = "SEARCH";	//������ȸ(�Ѹ�)
    final String pexitTag = "PEXIT";	//���α׷�����
    final String rexitTag = "REXIT";	//������
    final String omokTag = "OMOK";		//����
    final String Multi_winTag = "MULTIWIN";	//�̱۸�� �¸�
    final String Single_winTag = "SINGLEWIN"; //��Ƽ��� �¸�
    final String loseTag = "LOSE";		//�й�
    final String recordTag = "RECORD";	//����������Ʈ
    static String find_id = "FINDID";
    static String find_pw = "FINDPW";


    CCUser(Socket _s, Server _ss) {
        this.socket = _s;
        this.server = _ss;

        auser = server.alluser; // ���� ���� ���� ��� ����
        wuser = server.waituser; // ���� ���ǿ� �ִ� ����
        room = server.room;
    }  //CCUser()
    public void run() {
        try {
            System.out.println("[Server] Ŭ���̾�Ʈ ���� > " + this.socket.toString());

            os = this.socket.getOutputStream();
            dos = new DataOutputStream(os);
            is = this.socket.getInputStream();
            dis = new DataInputStream(is);

            while(true) {
                msg = dis.readUTF();	//�޽��� ������ ��� ����Ѵ�.
                // msg = "LOGIN//admin//admin"
                String[] m = msg.split("//");	//msg�� "//"�� ������ m[] �迭�� ���ʷ� ����ִ´�.
                // m["LOGIN", "admin", "admin"]
                System.out.println(Arrays.toString(m));
                // ���Ź��� ���ڿ����� ù ��° �迭(m[0])�� ��� �±� ����. �� ����� �и��Ѵ�.



                /* �α��� */
                if(m[0].equals(loginTag)) {
                    // loginCheck�� nickname�� ��ȯ m[1]:id, m[2]:pw �� �´� nickname
                    String mm = db.loginCheck(m[1], m[2]);

                    if(!mm.equals("null")) {	//�α��� ����
                        nickname = mm;		//�α����� ������� �г����� �ʵ忡 ����

                        auser.add(this);	//��� ���� �ο��� �߰�
                        wuser.add(this);	//���� ���� �ο��� �߰�

                        System.out.println(auser.get(0).nickname);

                        //System.out.println(auser.get(1));
                        //System.out.println(auser.get(2));

                        dos.writeUTF(loginTag + "//success" + "//" + nickname);

                        sendWait(connectedUser());	//���� ���� ������ ��� ���� �ο��� ����

                        if(room.size() > 0) {	//������ ���� ������ 0 �̻��� ��
                            sendWait(roomInfo());	//���� ���� �ο��� �� ����� ����
                        }

                    }

                    else {	//�α��� ����
                        dos.writeUTF(loginTag + "//fail");
                    }
                }  //�α��� if��

                /* ȸ������ */
                else if(m[0].equals(signupTag)) {
                    if(db.signupCheck(m[1], m[2], m[3], m[4])) {	//ȸ������ ����
                        dos.writeUTF(signupTag + "//success");
                    }

                    /*
                    else {	//ȸ������ ����
                        dos.writeUTF(signupTag + "//fail");
                    }
                    */
                }  //ȸ������ if��

                /* �ߺ�Ȯ�� */
                else if(m[0].equals(overTag)) {
                    if(db.overCheck(m[1], m[2])) {	//��� ����
                        dos.writeUTF(overTag + "//success");
                    }

                    else {	//��� �Ұ���
                        dos.writeUTF(overTag + "//fail");
                    }
                }  //�ߺ�Ȯ�� if��


                //���̵� ��ȸ
                else if(m[0].equals(find_id)){  //
                    if(!db.viewID(m[1]).equals("null")){
                        dos.writeUTF(find_id + "//success//" + db.viewID(m[1]));
                    }
                    else
                        dos.writeUTF(find_id + "//FAIL");
                }
                //��й�ȣ ��ȸ
                else if(m[0].equals(find_pw)){
                    if(!db.viewPW(m[1],m[2]).equals("null")){
                        System.out.println(db.viewPW(m[1], m[2]));
                        dos.writeUTF(find_pw + "//success//" + db.viewPW(m[1],m[2]));
                    }else
                        dos.writeUTF(find_pw + "//FAIL");
                }


                /*
                // ȸ������ ��ȸ
                else if(m[0].equals(viewTag)) {
                    if(!db.viewInfo(nickname).equals("null")) {	//��ȸ ����
                        dos.writeUTF(viewTag + "//" + db.viewInfo(nickname));	//�±׿� ��ȸ�� ������ ���� ����
                    }

                    else {	//��ȸ ����
                        dos.writeUTF(viewTag + "//FAIL");
                    }
                }  //ȸ������ ��ȸ if��

                /*
                 */

                /*
                // ȸ������ ����
                else if(m[0].equals(changeTag)) {
                    if(db.changeInfo(nickname, m[1], m[2])) {	//���� ����
                        dos.writeUTF(changeTag + "//OKAY");
                    }

                    else {	//���� ����
                        dos.writeUTF(changeTag + "//FAIL");
                    }
                }  //ȸ������ ���� if��
                */

                /* ��ü ���� ��ȸ */
                else if(m[0].equals(rankTag)) {
                    if(!db.viewRank().equals("")) {	//��ȸ ����
                        dos.writeUTF(rankTag + "//" + db.viewRank());	//�±׿� ��ȸ�� ������ ���� ����
                    }
                    // db.viewRank() = record//member
                    else {	//��ȸ ����
                        dos.writeUTF(rankTag + "//FAIL");
                    }
                }  //��ü ���� ��ȸ if��

                /* �� ���� */
                else if(m[0].equals(croomTag)) {
                    myRoom = new Room();	//���ο� Room ��ü ���� �� myRoom�� �ʱ�ȭ
                    myRoom.title = m[1];	//�� ������ m[1]�� ����
                    myRoom.count++;			//���� �ο��� �ϳ� �߰�

                    room.add(myRoom);		//room �迭�� myRoom�� �߰�

                    myRoom.ccu.add(this);	//myRoom�� �����ο��� Ŭ���̾�Ʈ �߰�
                    wuser.remove(this);		//���� ���� �ο����� Ŭ���̾�Ʈ ����

                    dos.writeUTF(croomTag + "//OKAY");
                    System.out.println("[Server] "+ nickname + " : �� '" + m[1] + "' ����");

                    sendWait(roomInfo());	//���� ���� �ο��� �� ����� ����
                    sendRoom(roomUser());	//�濡 ������ �ο��� �� �ο� ����� ����
                }  //�� ���� if��
                /* ���� */
                else if(m[0].equals(omokTag)) {
                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�� �ο�����ŭ �ݺ�

                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {	//�� ���� �ο� �� Ŭ���̾�Ʈ�� �ٸ� �г����� Ŭ���̾�Ʈ���Ը� ����
                            myRoom.ccu.get(i).dos.writeUTF(omokTag + "//" + m[1] + "//" + m[2] + "//" + m[3]);
                        }
                    }
                }  //���� if��

                /* �̱۸�� �¸� �� ���� ������Ʈ */
                else if(m[0].equals(Single_winTag)) {
                    System.out.println("[Server] " + nickname + " �¸�");

                    if(db.Single_winRecord(mode, nickname, currenttime)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                        dos.writeUTF(recordTag + "//OKAY");
                    } else {						//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                        dos.writeUTF(recordTag + "//FAIL");
                    }

                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�� �ο�����ŭ �ݺ�

                        /* �� ���� �ο� �� Ŭ���̾�Ʈ�� �ٸ� �г����� Ŭ���̾�Ʈ�϶��� */
                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                            myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");

                            if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                            } else {										//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                            }
                        }
                    }
                }

                /* ��Ƽ��� �¸� �� ���� ������Ʈ */
                else if(m[0].equals(Multi_winTag)) {
                    System.out.println("[Server] " + nickname + " �¸�");

                    if(db.Multi_winRecord(nickname)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                        dos.writeUTF(recordTag + "//OKAY");
                    } else {						//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                        dos.writeUTF(recordTag + "//FAIL");
                    }

                    for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�� �ο�����ŭ �ݺ�

                        /* �� ���� �ο� �� Ŭ���̾�Ʈ�� �ٸ� �г����� Ŭ���̾�Ʈ�϶��� */
                        if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                            myRoom.ccu.get(i).dos.writeUTF(loseTag + "//");

                            if(db.loseRecord(myRoom.ccu.get(i).nickname)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                            } else {										//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                                myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                            }
                        }
                    }
                }  //�¸� �� ���� ������Ʈ if��

                /* �й�, ��� �� ���� ������Ʈ */
                else if(m[0].equals(loseTag)) {
                    if(myRoom.count==1) {	//����� �ߴµ� �� ���� �ο��� 1���� �� ���� �̹ݿ��� ����
                        dos.writeUTF(recordTag + "//NO");
                    }

                    else if(myRoom.count==2) {	//��� �� �й踦 ���� �� �� ���� �ο��� 2���� ��
                        dos.writeUTF(loseTag + "//");

                        if(db.loseRecord(nickname)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                            dos.writeUTF(recordTag + "//OKAY");
                        } else {						//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                            dos.writeUTF(recordTag + "//FAIL");
                        }

                        for(int i=0; i<myRoom.ccu.size(); i++) {	//myRoom�� �ο�����ŭ �ݺ�

                            /* �� ���� �ο� �� Ŭ���̾�Ʈ�� �ٸ� �г����� Ŭ���̾�Ʈ�϶��� */
                            if(!myRoom.ccu.get(i).nickname.equals(nickname)) {
                                myRoom.ccu.get(i).dos.writeUTF(Multi_winTag + "//");
                                // ���� �ʿ�!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                if(db.Multi_winRecord(myRoom.ccu.get(i).nickname)) {	//���� ������Ʈ�� �����ϸ� ������Ʈ ������ ����
                                    myRoom.ccu.get(i).dos.writeUTF(recordTag + "//OKAY");
                                } else {										//���� ������Ʈ�� �����ϸ� ������Ʈ ���и� ����
                                    myRoom.ccu.get(i).dos.writeUTF(recordTag + "//FAIL");
                                }
                            }
                        }
                    }
                }  //�й�, ��� �� ���� ������Ʈ if��
                else if(m[0].equals(logoutTag)) {
                    System.out.println(server.alluser.size());
                    String Client_nick = m[1];
                    int i=0;
                    while(!server.alluser.get(i).nickname.equals(Client_nick)) {
                        i++;
                    }
                    System.out.println("[Server]" + server.alluser.get(i).nickname + "���� ������ �����Ͽ����ϴ�.");
                    server.alluser.remove(i);
                    System.out.println(server.alluser.size());
                }
            }  //while��
        } catch(IOException e) {
            System.out.println("[Server] ����� ���� > " + e.toString());
        }
    }  //run()
    /* ���� �����ϴ� ���� ����� ��ȸ�ϴ� �޼ҵ� */
    String roomInfo() {
        String msg = vroomTag + "//";

        for(int i=0; i<room.size(); i++) {
            msg = msg + room.get(i).title + " : " + room.get(i).count + "@";
        }
        return msg;
    }

    /* Ŭ���̾�Ʈ�� ������ ���� �ο��� ��ȸ�ϴ� �޼ҵ� */
    String roomUser() {
        String msg = uroomTag + "//";

        for(int i=0; i<myRoom.ccu.size(); i++) {
            msg = msg + myRoom.ccu.get(i).nickname + "@";
        }
        return msg;
    }

    /* ������ ��� ȸ�� ����� ��ȸ�ϴ� �޼ҵ� */
    String connectedUser() {
        String msg = cuserTag + "//";

        for(int i=0; i<auser.size(); i++) {
            msg = msg + auser.get(i).nickname + "@";
        }
        return msg;
    }

    /* ���ǿ� �ִ� ��� ȸ������ �޽��� �����ϴ� �޼ҵ� */
    void sendWait(String m) {
        for(int i=0; i<wuser.size(); i++) {
            try {
                wuser.get(i).dos.writeUTF(m);
            } catch(IOException e) {
                wuser.remove(i--);
            }
        }
    }

    /* �濡 ������ ��� ȸ������ �޽��� �����ϴ� �޼ҵ� */
    void sendRoom(String m) {
        for(int i=0; i<myRoom.ccu.size(); i++) {
            try {
                myRoom.ccu.get(i).dos.writeUTF(m);
            } catch(IOException e) {
                myRoom.ccu.remove(i--);
            }
        }
    }
}  //CCUser Ŭ����
