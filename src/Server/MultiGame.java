package Server;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MultiGame {

//    private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources �̹���
//    private static int CELL_SIZE = 30;
//
//    private final int COVER_FOR_CELL = 10;// Ŀ��(��Ȱ��ȭ ĭ)//Ŀ�� �ִ°��� 10��ŭ ������
//    private final int MARK_FOR_CELL = 10;// ��� ���� ĭ//��߲������� 10��ŭ ������
//    private final int EMPTY_CELL = 0;// ��ĭ(Ȱ��ȭ��ĭ)
//    private final int MINE_CELL = 9;// ���� ��(ĭ)
//    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// Ŀ��+���� = Ŀ�� ���� ����
//    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// ��� ���� ���ڼ� //���+Ŀ������ = ���(10) + ����(9+10)
//
//    private final int DRAW_MINE = 9;// ���� �̹���?
//    private final int DRAW_COVER = 10;// Ŀ��
//    private final int DRAW_MARK = 11;// ��߸�ũ
//    private final int DRAW_WRONG_MARK = 12;// �� ���
//
//    private static int N_MINES = 40;// ���� ����
//    private static int N_ROWS = 16;// ����ĭ ��
//    private static int N_COLS = 16;// ����ĭ ��
//
//    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// ������ ����
//    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
//
    static int[] field;// ��� ��(ĭ) �ѹ���
//    boolean inGame;// false:game lost
//    boolean win;
//    int minesLeft;// �����ϴ��� ǥ�õǴ� ���� ���� ���ڰ����� ī��Ʈ�ϴ� ����
//    private Image[] img;// �̹��� ����
//
//    private static int allCells;// N_ROWS * N_COLS;// = field ����
//    private final JLabel statusbar = new JLabel("testMessage");// �����ϴ��� �۾�. ex) ���ڰ���, �¸��޽���,�й�޽��� ��
//    private boolean mousePressedCheck = false;
//    List<Integer> pressedCellCover = new ArrayList<>();
//    List<Integer> scanCellCover = new ArrayList<>();
//
//    long beforeTime;// �ð� ����
//    long afterTime;
//    long secDiffTime;
//    Timer m_timer;
//    TimerTask m_task;
//    JLabel testLabel;
//
//
//    // Ŭ���� ������
//    public MultiGame(int n_cols, int n_rows, int n_mines, int cell_size) {
////        this.N_COLS = n_cols;
////        this.N_ROWS = n_rows;
////        this.N_MINES = n_mines;
////        this.CELL_SIZE = cell_size;
//        N_COLS = 16;
//        N_ROWS = 30;
//        N_MINES = 99;
//
//
//        initBoard();
//    }

    public static void setField(int[] field) {
        MultiGame.field = field;
    }

    public static int[] getField() {
        return field;
    }


    //ó�� ������ �����
//    private void initBoard() {
//        gameBoard();
//    }
//
//    void gameBoard() {
//
//        minesLeft = N_MINES;// ���� ������ �� ������ �ʱ�ȭ
//
//        allCells = N_ROWS * N_COLS;
//        field = new int[allCells];
//
//        for (int i = 0; i < allCells; i++) {
//
//            field[i] = COVER_FOR_CELL;// ��� ĭ�� Ŀ��(��Ȱ��ȭ ��ĭ)�� ����
//        }
//
//    }
}



