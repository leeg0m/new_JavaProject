package Server;

import java.util.Vector;

//��Ƽ���� �����ϱ� ���� ����� ������ Ŭ����.
//�� Room���� ��ü�� ����. Room ��ü�� ����, �ο���, Ŭ���̾�Ʈ ��ü �迭�� �ʵ�� ������ �̵��� �����Ѵ�.
public class Room {
    Vector<CCUser> ccu;
    String title;
    int count = 0;

    Room() {	//Room ��ü ���� �� ����(����)�� Ŭ���̾�Ʈ ��ü�� ���� ������ Room�� �����Ѵ�.
        ccu = new Vector<>();
    }
}
