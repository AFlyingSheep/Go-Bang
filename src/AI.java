import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AI {
    AI(){
        initial();
        AIPlay ap=new AIPlay();
        Thread t=new Thread(new AIPlay());
        t.start();
        AIJframe ajf=new AIJframe();
    }

    public static void initial(){
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                Global.dataarr[i][j]=0;
                Global.aiarr[i][j]=0;

            }
        }

        Global.localRound=(Global.localRound==1?2:1);
        Global.isWin=0;
        Global.chessCount=0;
        Global.currentRound=1;

    }
}



//AI进程
class AIPlay implements Runnable{

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MICROSECONDS.sleep(1000);  //延迟1000ms
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            while(Global.isWin==0){ //如果游戏没有结束
                try{
                    TimeUnit.MICROSECONDS.sleep(1000);  //延迟1000ms
                    //如果当前回合是电脑回合，且游戏没有结束
                    if(Global.currentRound!=Global.localRound&& Global.isWin==0){

                        //赋值电脑的回合标志
                        Computer.computerColor=Global.localRound%2+1;
                        //计算下一步的最优位置
                        int [] a=Computer.getNext(Global.localRound%2+1);
                        Global.dataarr[a[0]][a[1]]=Global.localRound%2+1;
                        Global.aiarr[a[0]][a[1]]=Global.localRound%2+1;
                        int result=rulesMethod.checkVictory(a[0],a[1],Global.localRound%2+1,Global.chessCount);

                        //计算游戏是否结束
                        if(result!=0){
                            //结束弹出对话框
                            String Winner=result==1?"白方":"黑方";
                            JOptionPane.showMessageDialog(null, "共"+Global.chessCount+"步，"+Winner+"胜！！");
                            Global.isWin=Global.localRound%2+1;
                        }
                        else
                            //没有结束，更改当前回合
                            Global.currentRound=Global.localRound;
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}

//界面-------------------------------------------------------------
class AIJframe extends JFrame{
    AIJframe(){
        //窗体初始化设置
        this.setSize(1200,1000);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        JButton jb;
        JButton exit;

        //添加棋盘
        pictureFrame pf=new pictureFrame();
        pf.setBounds(50,100,800,800);
        this.add(pf);

        //添加信息框
        informJP ij=new informJP();
        this.add(ij);
        ij.setOpaque(true);

        //添加说明框
        JLabel you=new JLabel("你");
        JLabel ai=new JLabel("<html>旗鼓相当的<br>对手<html>");

        Font f = new Font("楷体",Font.BOLD,20);
        Font f1 = new Font("楷体",Font.BOLD,36);
        you.setFont(f1);
        ai.setFont(f);

        you.setBounds(200,80,100,100);
        ai.setBounds(170,220,400,200);

        ij.add(you);
        ij.add(ai);

        //添加按钮
        jb=new JButton("重新开始！");
        jb.setBounds(10,700,130,80);
        ij.add(jb);
        jb.addActionListener(new ActionListener() {
            AIJframe af;

            @Override
            public void actionPerformed(ActionEvent e) {
                AI.initial();
                af.repaint();
            }

            public ActionListener accept(AIJframe af){
                this.af=af;
                return this;
            }
        }.accept(this));

        exit=new JButton("不玩了！");
        exit.setBounds(150,700,130,80);
        ij.add(exit);
        exit.addActionListener(new ActionListener() {
            AIJframe af;

            @Override
            public void actionPerformed(ActionEvent e) {
                af.dispose();
                new indexFrame();
            }
            public ActionListener accept(AIJframe af){
                this.af=af;
                return this;
            }
        }.accept(this));


        //添加背景
        this.add(new aiBackGround(this));



        pf.addMouseListener(new aimouseCatch());
    }


}

//主背景
class aiBackGround extends JPanel{
    AIJframe aj;
    public aiBackGround(AIJframe aj){
        this.aj=aj;
        this.setBounds(0,0,aj.getWidth(),aj.getHeight());
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
       int x = 0, y = 0;

        ImageIcon icon = new ImageIcon("image//background6.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放
    }
}

class informJP extends JPanel{
    informJP(){
        this.setBounds(860,100,300,800);
        this.setLayout(null);
        this.setBackground(Color.white);
    }

    public void paintComponent(Graphics g){

        ImageIcon icon = new ImageIcon("image//background2.jpg");
        g.drawImage(icon.getImage(),0, 0, getSize().width,
                getSize().height, this);// 图片会自动缩放

        ImageIcon iconw=new ImageIcon("image//white.jpg");
        ImageIcon iconb=new ImageIcon("image//black.jpg");

        if(Global.localRound==1){
            g.drawImage(iconw.getImage(),20,60,150,150,this);
            g.drawImage(iconb.getImage(),20,240,150,150,this);
        }
        else{
            g.drawImage(iconb.getImage(),20,60,150,150,this);
            g.drawImage(iconw.getImage(),20,240,150,150,this);
        }

    }

}

class aimouseCatch extends MouseAdapter {


    public aimouseCatch() {
        super();

    }

    public void mousePressed(MouseEvent e) {
        // 获取鼠标点下的位置的坐标
        Point p = e.getPoint();
        int x,y;
        x = (int) (p.getX()) / 50;
        y = (int) (p.getY()) / 50;

        //修改数组
        //如果下在棋盘里，并且下的位置不是下过的
        if (x >= 0 && x <= 15 && y >= 0 && y <= 15 && Global.dataarr[x][y] == 0 && Global.currentRound == Global.localRound && Global.isWin == 0) {
            Global.chessCount++;
            if (Global.dataarr[x][y] == 0) {
                if (Global.localRound == 1) {
                    Global.dataarr[x][y] = 1;
                    Global.aiarr[x][y] = 1;

                } else if (Global.localRound == 2) {
                    Global.dataarr[x][y] = 2;
                    Global.aiarr[x][y] = 2;

                }
            }
            if(aimethod.isEndNow(x,y,Global.localRound)!=0){
                String Winner=Global.localRound==1?"白方":"黑方";
                JOptionPane.showMessageDialog(null, "共"+Global.chessCount+"步，"+Winner+"胜！！");
                Global.isWin=Global.localRound;
            }
            else{
                Global.currentRound=Global.localRound%2+1;
            }
        }
    }
}


//AI算法---------------------------------------------------------------------
class aimethod{
    public static int isEndNow(int x,int y,int color) {
        int dx[] = {1,0,1,1};
        int dy[] = {0,1,1,-1};
        for (int i=0;i<4;i++) {
            int sum = 1;
            int tx =x + dx[i];
            int ty =y + dy[i];
            while (tx>=0 && tx<16 && ty>=0 && ty<16 && Global.aiarr[tx][ty] ==color) { //当前点去遍历
                tx +=dx[i];
                ty +=dy[i];
                ++sum;
            }
            tx =x - dx[i];
            ty =y - dy[i];
            while (tx>=0 && tx<16 && ty>=0 && ty<16 && Global.aiarr[tx][ty] ==color) { //返回来遍历
                tx -=dx[i];
                ty -=dy[i];
                ++sum;
            }
            if(sum >= 5)
                return color;
        }
        return 0;
    }

    public static int reckon(int color) {//评估函数
        int dx[] = {1,0,1,1};//右，下，右下，右上
        int dy[] = {0,1,1,-1};
        int ans = 0;
        for(int x=0;x<16;x++) {
            for (int y=1;y<16;y++) {
                if (Global.aiarr[x][y]!=color)
                    continue;
                int num[][] =new int[2][10];//计数
                for (int i=0;i<4;i++){
                    int sum = 1;
                    //falg1表示一头死，falg2两头活
                    int flag1 = 0,flag2 = 0;
                    int tx =x + dx[i];
                    int ty =y + dy[i];
                    while (tx >= 0&& tx <16 && ty>=0 && ty<16&&Global.aiarr[tx][ty]==color) {
                        tx +=dx[i];
                        ty +=dy[i];
                        ++sum;
                    }
                    if(tx >=0 && tx<16 && ty>=0 && ty<16 && Global.aiarr[tx][ty] ==0)
                        flag1 = 1;
                    tx =x - dx[i];
                    ty =y - dy[i];
                    while (tx >= 0 && tx <16 && ty>=0 && ty <16 && Global.aiarr[tx][ty] ==color) { //回找
                        tx-=dx[i];
                        ty-=dy[i];
                        ++sum;
                    }
                    if(tx>=0 && tx<16 && ty>=0 && ty<16 && Global.aiarr[tx][ty] ==0)
                        flag2 = 1;
                    if(flag1 +flag2 > 0)
                        ++num[flag1 +flag2- 1][sum];
                }

                    //成5
                if(num[0][5]>0 ||num[1][5]> 0) //num[0][5]+num[1][5]>0
                    ans = Math.max(ans, 100000);
                    //活4 |双死四 |死4活3
                else if(num[1][4] > 0||num[0][4]> 1||(num[0][4]> 0 &&num[1][3] > 0))
                    ans = Math.max(ans, 10000);
                    //双活3
                else if(num[1][3] > 1)
                    ans = Math.max(ans, 50000);
                    //死4
                else if(num[0][4] > 0)
                    ans = Math.max(ans, 1000);
                    //死3活3
                else if(num[1][3] > 0 &&num[0][3] > 0)
                    ans = Math.max(ans, 500);
                    //单活3
                else if(num[1][3] > 0)
                    ans = Math.max(ans, 200);
                    //双活2
                else if(num[1][2] > 1)
                    ans = Math.max(ans, 100);
                    //死3
                else if(num[0][3] > 0)
                    ans = Math.max(ans, 50);
                    //单活2
                else if(num[1][2] > 0)
                    ans = Math.max(ans, 10);
                    //死2
                else if(num[0][2] > 0)
                    ans = Math.max(ans, 5);
                else if(num[1][1]>0)
                    ans=Math.max(ans,1);
            }
        }
        return ans;
    }
}


class Computer {
    static int depth=1;
    static int computerColor=Global.localRound%2+1;
    //alpha_beta剪枝搜索,寻找着点

    public static int alpha_betaFind(int depth,int alpha,int beta,int color,int x,int y){
        if(depth>Computer.depth||aimethod.isEndNow(x,y,color%2+1)!=0){
        int ans =aimethod.reckon(computerColor)-aimethod.reckon(computerColor%2+1);
        if(depth%2==0)
        ans=-ans;
        return ans;
        }
        for(int i=0;i<16;i++){
            for(int j=0;j<16;j++){
                if(!(Global.aiarr[i][j]==0))
                    continue;

                Global.aiarr[i][j]=color;
                int val=-alpha_betaFind(depth+1,-beta ,-alpha,color%2+1,i,j);//ans的值给val
                Global.aiarr[i][j]=0;

                if(val>=beta)
                    return beta;//返回比beta好的值val
                if(val>alpha)
                    alpha=-val;//返回比alpha更坏的值val
            }
        }
        return alpha;
    }

    public static int[] getNext(int color){
        int rel[]=new int[2];
        int ans=-100000000;
        Random random=new Random(47);

        //初始电脑定位
        if(Global.chessCount==0||(Global.chessCount==1 && Global.localRound==1)){
            if(Global.aiarr[7][7]!=computerColor){
                if(Global.aiarr[7][7]==0){//中点
                    rel[0]=7;
                    rel[1]=7;
                }
                else{
                    rel[0]=8;//向右占位
                    rel[1]=7;
                }
             }
        }
        else{
            for(int x=0;x<16;x++){
                for(int y=0;y<16;y++){
                    if(Global.aiarr[x][y]!=0)
                        continue;
                    Global.aiarr[x][y]=color;
                    int val=-alpha_betaFind(0,-100000000,100000000,color%2+1,x,y);//判断白棋局面
                    int ran=random.nextInt(100);//100是不包含在内的，只产生0~100之间的数
                    if(val>ans||val==ans&&ran>50){//val(-递归返回值)<-ans=====val>ans ||ans一直被刷新
                        ans=val;
                        rel[0]=x;
                        rel[1]=y;
                    }
                Global.aiarr[x][y]=0;
                }
            }
        }
        return rel;
    }

}