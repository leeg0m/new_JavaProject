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

//    private final int NUM_IMAGES = 13;// \Java-Minesweeper-Game-master\Java-Minesweeper-Game-master\src\resources 이미지
//    private static int CELL_SIZE = 30;
//
//    private final int COVER_FOR_CELL = 10;// 커버(비활성화 칸)//커버 있는곳은 10만큼 더해짐
//    private final int MARK_FOR_CELL = 10;// 깃발 꽃힌 칸//깃발꽂힌곳은 10만큼 더해짐
//    private final int EMPTY_CELL = 0;// 빈칸(활성화된칸)
//    private final int MINE_CELL = 9;// 지뢰 셀(칸)
//    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// 커버+지뢰 = 커버 씌운 지뢰
//    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// 깃발 꽂힌 지뢰셀 //깃발+커버지뢰 = 깃발(10) + 지뢰(9+10)
//
//    private final int DRAW_MINE = 9;// 지뢰 이미지?
//    private final int DRAW_COVER = 10;// 커버
//    private final int DRAW_MARK = 11;// 깃발마크
//    private final int DRAW_WRONG_MARK = 12;// 깃 취소
//
//    private static int N_MINES = 40;// 지뢰 개수
//    private static int N_ROWS = 16;// 세로칸 수
//    private static int N_COLS = 16;// 가로칸 수
//
//    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;// 프레임 넓이
//    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;//
//
    static int[] field;// 모든 셀(칸) 넘버링
//    boolean inGame;// false:game lost
//    boolean win;
//    int minesLeft;// 좌측하단의 표시되는 현재 남은 지뢰개수를 카운트하는 변수
//    private Image[] img;// 이미지 파일
//
//    private static int allCells;// N_ROWS * N_COLS;// = field 개수
//    private final JLabel statusbar = new JLabel("testMessage");// 좌측하단의 글씨. ex) 지뢰개수, 승리메시지,패배메시지 등
//    private boolean mousePressedCheck = false;
//    List<Integer> pressedCellCover = new ArrayList<>();
//    List<Integer> scanCellCover = new ArrayList<>();
//
//    long beforeTime;// 시간 측정
//    long afterTime;
//    long secDiffTime;
//    Timer m_timer;
//    TimerTask m_task;
//    JLabel testLabel;
//
//
//    // 클래스 생성자
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


    //처음 게임판 만들기
//    private void initBoard() {
//        gameBoard();
//    }
//
//    void gameBoard() {
//
//        minesLeft = N_MINES;// 남은 개수를 총 개수로 초기화
//
//        allCells = N_ROWS * N_COLS;
//        field = new int[allCells];
//
//        for (int i = 0; i < allCells; i++) {
//
//            field[i] = COVER_FOR_CELL;// 모든 칸에 커버(비활성화 빈칸)를 씌움
//        }
//
//    }
}



