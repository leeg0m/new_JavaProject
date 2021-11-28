package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
//Ŭ���̾�Ʈ�� ���� ��û �� ������� ��� �����ϴ� Ŭ����.
public class Server {
    ServerSocket ss = null;

    /* �� ��ü���� Vector�� ���� */
    Vector<CCUser> alluser;		// ����� ��� Ŭ���̾�Ʈ
    Vector<CCUser> waituser;	// ���ǿ� �ִ� Ŭ���̾�Ʈ
    Vector<Room> room;			// ������ Room

    public static void main(String[] args) {
        Server server = new Server();

        server.alluser = new Vector<>();
        server.waituser = new Vector<>();
        server.room = new Vector<>();

        try {
            //���� ���� �غ�
            server.ss = new ServerSocket(7777);
            System.out.println("[Server] ���� ���� �غ� �Ϸ�");

            //Ŭ���̾�Ʈ�� ���� ��û�� ��� ���.
            while(true) {
                Socket socket = server.ss.accept();
                CCUser c = new CCUser(socket, server);	//���ϰ� ������ �Ѱ� CCUser(������ ���� ����)��ü ����

                c.start();	//CCUser ������ ����
            }
        } catch(SocketException e) {	//�� ������ �ַܼ� �˸���.
            System.out.println("[Server] ���� ���� ���� > " + e.toString());
        } catch(IOException e) {
            System.out.println("[Server] ����� ���� > " + e.toString());
        }
    }
}
