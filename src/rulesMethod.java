import javax.swing.*;
import java.awt.*;

public class rulesMethod {

    public static int checkVictory(int x,int y,int round,int chessCount){
        //每个方向判断最大的偏移值
        int x0=(x-4>=0?x-4:0);
        int x1=(x+4>=16?15:x+4);
        int y0=(y-4>=0?y-4:0);
        int y1=(y+4>=16?15:y+4);
        int z00=(x-x0>=y-y0?y-y0:x-x0);
        int z01=(x1-x>=y1-y?y1-y:x1-x);
        int z10=(x-x0>=y1-y?y1-y:x-x0);
        int z11=(x1-x>=y-y0?y-y0:x1-x);
        int count=0;
        //四个方向各自判断
        //左右
        for(int i=x0;i<=x1;i++){
            if(round==Global.dataarr[i][y]){
                count++;
            }
            else {
                if(count==5)
                    return round;
                else
                count=0;
            }
        }
        //上下
        for(int i=y0;i<=y1;i++){
            if(round==Global.dataarr[x][i]){
                count++;
            }
            else {
                if(count==5)
                    return round;
                else
                    count=0;
            }
        }
        //左上到右下
        for(int i=-1*z00;i<=z01;i++){
            if(round==Global.dataarr[x+i][y+i]){
                count++;
            }
            else {
                if(count==5)
                    return round;
                else
                    count=0;
            }
        }
        //左下到右上
        for(int i=-1*z10;i<=z11;i++){
            if(round==Global.dataarr[x+i][y-i]){
                count++;
            }
            else {
                if(count==5)
                    return round;
                else
                    count=0;
            }
        }
        if(chessCount==16*16) return 3;
        return 0;

    }

    public static void initial(mainFrame mf){
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                Global.dataarr[i][j]=0;
            }
        }

        Global.localRound=(Global.localRound==1?2:1);
        Global.isWin=0;
        Global.chessCount=0;
        Global.currentRound=1;

        mf.repaint();
    }

    //画棋！
    //根据Global数组画棋
    public static void drawChess(pictureFrame pf,Graphics g){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                // 1为白
                if (Global.dataarr[i][j] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillOval(50*i+5,50*j+5,40,40);
                }
                g.setColor(Color.black);
                // 2为黑
                if (Global.dataarr[i][j] == 2) {
                    g.setColor(Color.black);
                    g.fillOval(50*i+5,50*j+5,40,40);
                }
            }
        }
        pf.repaint();
    }
}


