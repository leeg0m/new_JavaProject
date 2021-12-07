package new_JavaProject;

import javax.swing.*;
import java.awt.*;

public class viewGame extends JPanel{
    private static int[] field;
    private Image[] img;// �̹��� ����
    private final int NUM_IMAGES = 13;
    private static int CELL_SIZE = 30;
    private static int N_ROWS = 16;// ����ĭ ��
    private static int N_COLS = 16;// ����ĭ ��

    private final int COVER_FOR_CELL = 10;// Ŀ��(��Ȱ��ȭ ĭ)//Ŀ�� �ִ°��� 10��ŭ ������
    private final int MARK_FOR_CELL = 10;// ��� ���� ĭ//��߲������� 10��ŭ ������
    private final int EMPTY_CELL = 0;// ��ĭ(Ȱ��ȭ��ĭ)
    private final int MINE_CELL = 9;// ���� ��(ĭ)
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// Ŀ��+���� = Ŀ�� ���� ����
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// ��� ���� ���ڼ� //���+Ŀ������ = ���(10) + ����(9+10)

    private final int DRAW_MINE = 9;// ���� �̹���?
    //  private final int DRAW_COVER = 1;//NUM_IMAGES�� ��������. ���� CELL�� NUM_IMAGES�� ��������.
//  �̹������� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:�� 12:�� ���
//	���� ���� 0:���� 1:1 2:2 3:3 .. 8:8 9:���� 10:�ȴ��� 11:Ŀ��(1) 12:Ŀ��(2) .. 19:Ŀ������ 29:��߲�������Ŀ�� //20:�󼿿� ���� ��� 21:1���������...28:8���ȱ�
    private final int DRAW_COVER = 10;// Ŀ��
    private final int DRAW_MARK = 11;// ��߸�ũ
    private final int DRAW_WRONG_MARK = 12;// �� ���

    viewGame(byte[] ff){
        for(int i=0;i<ff.length;i++){
            field[i]=(int)ff[i];
        }

        img = new Image[NUM_IMAGES];// �̹��� �ε�
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "./images/inGame/resources/" + i + ".png";
            ImageIcon icon = new ImageIcon(path);
            img[i] = icon.getImage();
            img[i] = img[i].getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);

            ImageIcon changeIcon = new ImageIcon(img[i]);
        }
    }
    public void setField(byte[] ff) {
        for(int i=0;i<ff.length;i++){
            field[i]=(int)ff[i];
        }

        this.field = field;
        repaint();
    }

    byte[] f;
    public byte[] getField() {
        f= new byte[field.length];
        for(int i=0;i<field.length;i++){
            f[i]=(byte)field[i];
        }
        return f;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int i = 0; i < N_ROWS; i++) {// ����

            for (int j = 0; j < N_COLS; j++) {// ����
                int cell = field[(i * N_COLS) + j];

                if (cell > COVERED_MINE_CELL) {// Ŀ�� ����(19) ���� ũ��
                    cell = DRAW_MARK;// ���
                } else if (cell > MINE_CELL) {// ���ں��� ũ��
                    cell = DRAW_COVER;// �Ϲ�Ŀ��
                }
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// �� �̹��� ����
            }
        }
    }
}
