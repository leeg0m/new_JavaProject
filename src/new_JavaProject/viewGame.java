package new_JavaProject;

import javax.swing.*;
import java.awt.*;

public class viewGame extends JPanel{
    private static int[] field;
    private Image[] img;// 이미지 파일
    private final int NUM_IMAGES = 13;
    private static int CELL_SIZE = 30;
    private static int N_ROWS = 16;// 세로칸 수
    private static int N_COLS = 16;// 가로칸 수

    private final int COVER_FOR_CELL = 10;// 커버(비활성화 칸)//커버 있는곳은 10만큼 더해짐
    private final int MARK_FOR_CELL = 10;// 깃발 꽃힌 칸//깃발꽂힌곳은 10만큼 더해짐
    private final int EMPTY_CELL = 0;// 빈칸(활성화된칸)
    private final int MINE_CELL = 9;// 지뢰 셀(칸)
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;// 커버+지뢰 = 커버 씌운 지뢰
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;// 깃발 꽂힌 지뢰셀 //깃발+커버지뢰 = 깃발(10) + 지뢰(9+10)

    private final int DRAW_MINE = 9;// 지뢰 이미지?
    //  private final int DRAW_COVER = 1;//NUM_IMAGES와 연관있음. 위의 CELL도 NUM_IMAGES에 연관있음.
//  이미지순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:깃 12:깃 취소
//	셀값 순서 0:눌림 1:1 2:2 3:3 .. 8:8 9:지뢰 10:안눌림 11:커버(1) 12:커버(2) .. 19:커버지뢰 29:깃발꽂힌지뢰커버 //20:빈셀에 꽂힌 깃발 21:1에꽂힌깃발...28:8에꽂깃
    private final int DRAW_COVER = 10;// 커버
    private final int DRAW_MARK = 11;// 깃발마크
    private final int DRAW_WRONG_MARK = 12;// 깃 취소

    viewGame(byte[] ff){
        for(int i=0;i<ff.length;i++){
            field[i]=(int)ff[i];
        }

        img = new Image[NUM_IMAGES];// 이미지 로딩
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
        for (int i = 0; i < N_ROWS; i++) {// 세로

            for (int j = 0; j < N_COLS; j++) {// 가로
                int cell = field[(i * N_COLS) + j];

                if (cell > COVERED_MINE_CELL) {// 커버 지뢰(19) 보다 크면
                    cell = DRAW_MARK;// 깃발
                } else if (cell > MINE_CELL) {// 지뢰보다 크면
                    cell = DRAW_COVER;// 일반커버
                }
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);// 셀 이미지 갱신
            }
        }
    }
}
