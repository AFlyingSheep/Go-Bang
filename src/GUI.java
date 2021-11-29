import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class GUI {
    mainFrame mf;
    public GUI(mainFrame mf){
        this.mf=mf;
    }
}

//首页----------------------------------------------------------------
class indexFrame extends JFrame{
    public indexFrame(){

        //窗体初始化设置
        this.setSize(1000,700);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        //加入panel
        indexPanel ip=new indexPanel(this);
        this.add(ip);


        //注释
        JLabel name=new JLabel("<html>开发：<br>大连理工大学计算机科学与技术<br>19级1班 赵阳</html>");
        Font f=new Font("宋体",Font.BOLD,20);
        name.setBounds(50,500,300,200);
        name.setFont(f);
        ip.add(name);
        
        //加入单人游戏按钮
        JButton solo=new JButton("单人游戏");
        solo.setBounds(415,415,160,40);
        solo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AI();
                dispose();
            }
        });

        //加入多人游戏按钮
        JButton morePeople=new JButton("局域网游戏");
        morePeople.setBounds(415,505,160,40);
        morePeople.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectFrame cf=new connectFrame();
                dispose();
            }
        });

        //复盘游戏按钮
        JButton review=new JButton("复盘游戏");
        review.setBounds(415,595,160,40);
        review.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayReview dr=new displayReview();
                dispose();
            }
        });

        ip.add(morePeople);
        ip.add(solo);
        ip.add(review);
        //加入复盘按钮

        //加入背景
        indexBackground ibg=new indexBackground(this);
        this.add(ibg);

        this.repaint();

    }
}

//界面按钮等操作
class indexPanel extends JPanel{
    indexFrame inf;
    indexPanel(indexFrame inf){
        this.inf=inf;
        this.setBounds(0,0,inf.getWidth(),inf.getHeight());
        this.setOpaque(false);
        this.setLayout(null);
    }
}

//界面背景
class indexBackground extends  JPanel{
    indexFrame inf;
    public indexBackground (indexFrame inf){
        this.inf=inf;
        this.setBounds(0,0,inf.getWidth(),inf.getHeight());
    }

    @Override
    public void paintComponent(Graphics g){
        int x=0,y=0;
        ImageIcon icon=new ImageIcon("image//main.gif");
        g.drawImage(icon.getImage(),x,y,getSize().width,getSize().height,this);
    }

}
//网络连接界面----------------------------------------------------------
class connectFrame extends JFrame{
    connectPanel cp;
    public connectFrame(){

        //窗体初始化设置
        this.setSize(650,250);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("多人游戏");

        //添加布局
        cp=new connectPanel(this);
        this.add(cp);

        //文本状态设置
        /*JLabel nowlable=new JLabel();
        cp.add(nowlable);
        nowlable.setBounds(new Rectangle(30,180,300,30));
        if(Global.connectflag==0) nowlable.setText("等待操作");
        else if(Global.connectflag==1) nowlable.setText("连接中……");
        else nowlable.setText("等待加入中……");*/

        //按钮设置
        JButton b_foundIP=new JButton("寻找游戏"),b_waitIP=new JButton("等待加入");
        cp.setLayout(null);
        b_foundIP.setBounds(new Rectangle(30, 80, 290, 80));
        cp.add(b_foundIP);
        b_waitIP.setBounds(new Rectangle(650-30-290, 80, 290, 80));
        cp.add(b_waitIP);
        b_foundIP.addActionListener(new findIPAction(this));
        b_waitIP.addActionListener(new waitIPAction(this));

        //文本IP设置
        JLabel iplable=new JLabel();
        cp.add(iplable);
        iplable.setBounds(new Rectangle(30,30,300,30));
        iplable.setForeground(Color.white);
        try{
            InetAddress ip=InetAddress.getLocalHost();
            Global.localIP=ip.getHostAddress();

            iplable.setText("本机IP："+ip.getHostName()+"--"+Global.localIP);
        }
        catch (Exception e){
            iplable.setText("本机IP获取失败！");
            Global.localIP=null;
        }

        //背景设置
        connectBackground cb=new connectBackground(this);
        cp.add(cb);


    }
}

//网络布局
class connectPanel extends JPanel{
    connectFrame cf;
    public connectPanel(connectFrame cf){
        this.setBounds(0,0,cf.getWidth(),cf.getHeight());
        this.cf=cf;
    }
}

//网络连接背景
class connectBackground extends JPanel{
    connectFrame cf;
    public connectBackground (connectFrame cf){
        this.cf=cf;
        this.setBounds(0,0,cf.getWidth(),cf.getHeight());
    }

    @Override
    public void paintComponent(Graphics g){
        ImageIcon icon=new ImageIcon("image//background3.jpg");
        g.drawImage(icon.getImage(),0,0,getSize().width,getSize().height,this);
    }
}

//对战界面---------------------------------------------------------------------------

//绘制主界面
class mainFrame extends JFrame{
    pictureFrame picF;
    informFrame1 inF1;
    informFrame2 inF2;
    mainBackground mb;
    JTextArea displayMes;
    chatGUI cg;
    pointYou py;
    buttonPanel bp;
    JLabel win=new JLabel();
    int result=0;
    public mainFrame(){

        //窗体初始化设置

        this.setSize(1600,900);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        //加入聊天室
        //聊天室基础框架;

        cg=new chatGUI();
        this.add(cg);
        cg.setLayout(null);

        //添加显示聊天界面---------------------------

        displayMes=new JTextArea();
        displayMes.setEditable(false);
        displayMes.setLineWrap(true);
        displayMes.setBounds(10,10,250,600);

        //添加滚动条
        JScrollPane ScrollPane1 = new JScrollPane(displayMes);
        ScrollPane1.setBounds(10,10,250,600);
        cg.add(ScrollPane1);


        //添加发送聊天界面
        JTextArea sendMes=new JTextArea(7,22);
        sendMes.setLineWrap(true);
        sendMes.setBounds(10,620,250,100);

        //添加滚动条
        JScrollPane ScrollPane2 = new JScrollPane(sendMes);
        ScrollPane2.setBounds(10,620,250,100);
        cg.add(ScrollPane2);

        //添加发送按钮
        JButton go=new JButton("发送！");
        go.setBounds(10,720,250,30);
        cg.add(go);
        go.addActionListener(new sendMessageAction(displayMes,sendMes));

        //添加下拉列表
        JComboBox choiceMessage=new JComboBox();
        choiceMessage.setBounds(10,760,160,30);
        addItem(choiceMessage);
        cg.add(choiceMessage);

        //发送语音按钮
        JButton sendVoice=new JButton("Send!");
        sendVoice.setBounds(180,760,80,30);
        sendVoice.addActionListener(new sendVoiceAction(choiceMessage,displayMes));
        cg.add(sendVoice);


        //添加下排按钮-------------------------------------
        buttonPanel bp=new buttonPanel();
        bp.setLayout(null);
        this.add(bp);
        bp.setBackground(null);
        bp.setOpaque(false);

        //添加重开按钮
        JButton renew =new JButton("重开");
        renew.setBounds(0,0,150,50);
        bp.add(renew);
        /*if(Global.isWin==0){
            renew.setVisible(false);
        }
        else{
            renew.setVisible(true);
        }*/
        renew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog
                        (null, "申请重开？", "重开",JOptionPane.YES_NO_OPTION);
                if(n==0) Global.renewNeedSend=1;
            }
        });


        //添加退出按钮
        JButton exit=new JButton("退出");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog
                        (null, "确定退出？", "退出",JOptionPane.YES_NO_OPTION);
                if(n==0) System.exit(0);
            }
        });
        exit.setBounds(320,0,150,50);
        bp.add(exit);


        //添加信息框---------------------------------
        //加入指示符
        py=new pointYou();
        this.add(py);

        //加入消息框
        inF1=new informFrame1(this);
        this.add(inF1);
        inF2=new informFrame2(this);
        this.add(inF2);
        inF1.setLayout(null);
        inF2.setLayout(null);

        //加入玩家信息
        //输入玩家1信息

        String []player1= new String[] {Global.localName, Global.localIP};
        JLabel player1Inform=new JLabel();
        Font f = new Font("宋体",Font.BOLD,16);
        player1Inform.setFont(f);
        player1Inform.setText("<html>玩家姓名："+player1[0]+"<br><br>"+"玩家IP："+player1[1]+"</html>");
        player1Inform.setBounds(250,0,200,200);
        inF1.add(player1Inform);
        //输入玩家2信息

        String []player2= new String[]{Global.opponentName, Global.opponentIP};
        JLabel player2Inform=new JLabel();
        player2Inform.setFont(f);
        player2Inform.setText("<html>玩家姓名："+player2[0]+"<br><br>"+"玩家IP："+player2[1]+"</html>");
        player2Inform.setBounds(250,0,200,200);
        inF2.add(player2Inform);






        //加入棋盘
        picF=new pictureFrame();
        this.add(picF);

        //加入背景
        mb=new mainBackground(this);
        this.add(mb);

        //加入胜利了吗

        this.add(win);
        win.setFont(new Font("微软雅黑",Font.BOLD,20));
        win.setText("hello！");

        // 给面板添加一个鼠标监听事件
        this.addMouseListener(new mouseCatch(this,win,result));




    }

    //对语音复选框添加选项
    protected void addItem(JComboBox b){
        b.addItem("选择发送语音内容");
        b.addItem("赞扬！");
        b.addItem("问候！");
        b.addItem("催促！");
        b.addItem("后悔！");
        b.addItem("承让！");
    }
}

//下排按钮绘制
class buttonPanel extends JPanel{
    public buttonPanel(){
        this.setBounds(10,750,500,500);
        this.setLayout(null);
    }
}

//绘制棋盘
class pictureFrame extends JPanel {

    public pictureFrame() {
        this.setBounds(500, 50, 800, 800);
        this.setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        //添加背景
        try {
            BufferedImage bi = ImageIO.read(new File("image//background.jpg"));
            g.drawImage(bi, 0, 0, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //画出棋盘
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                g.setColor(Color.black);
                g.drawRect(50 * i + 25, 50 * j + 25, 50, 50);
            }
        //绘制棋子
        rulesMethod.drawChess(this,g);

    }
}

//对战信息框-------------------------------------------
class informFrame1 extends JPanel{
    mainFrame mf;

    //初始化
    public  informFrame1(mainFrame mf){
        this.setBounds(10,50,480,300);
        this.mf=mf;
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        int x = 0, y = 0;
        ImageIcon icon = new ImageIcon("image//background2.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放

        x=50;
        y=50;
        if(Global.localRound==1)
            icon=new ImageIcon("image//white.jpg");
        else{
            icon=new ImageIcon("image//black.jpg");
        }
        g.drawImage(icon.getImage(),x,y,150,150,this);

    }
}

class informFrame2 extends JPanel{
    mainFrame mf;

    //初始化
    public  informFrame2(mainFrame mf){
        this.setBounds(10,400,480,300);
        this.mf=mf;
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {

        //画出边框和背景
        int x = 0, y = 0;
        ImageIcon icon = new ImageIcon("image//background2.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放

        x=50;
        y=50;
        if(Global.localRound==1)
            icon=new ImageIcon("image//black.jpg");
        else{
            icon=new ImageIcon("image//white.jpg");
        }
        g.drawImage(icon.getImage(),x,y,150,150,this);
    }
}

class pointYou extends JPanel{
    //初始化
    public  pointYou(){
        this.setBounds(0,0,1000,1000);
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {

        //画出指示
        int x,y;

        //如果还没有结束
        if (Global.isWin == 0) {

            //指示的位置
            if(Global.currentRound==Global.localRound){
                x = 100;
                y = 250;
            }
            else{
                x = 100;
                y = 600;
            }

            ImageIcon icon = new ImageIcon("image//point.jpg");
            g.drawImage(icon.getImage(), x, y, 75,
                    75, this);// 图片会自动缩放
        }
    }


}

//主背景
class mainBackground extends JPanel{
    mainFrame mf;
    public mainBackground(mainFrame mf){
        this.mf=mf;
        this.setBounds(0,0,mf.getWidth(),mf.getHeight());
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
        int x = 0, y = 0;
        ImageIcon icon = new ImageIcon("image//mainground.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放
    }
}

class chatGUI extends JPanel{
    public chatGUI(){
        this.setBounds(1310,50,270,800);
    }
}




//事件------------------------------------------------------------------------
//下棋事件
class mouseCatch extends MouseAdapter  {
    mainFrame mf;
    int x, y;
    JLabel jb;
    int result;
    public mouseCatch(mainFrame mf,JLabel jb,int result) {
        super();
        this.mf = mf;
        this.jb=jb;
        this.result=result;
    }

    public void mousePressed(MouseEvent e) {
        // 获取鼠标点下的位置的坐标
        Point p = e.getPoint();
        x = (int) (p.getX() - 500) / 50;
        y = (int) (p.getY() - 70) / 50;

        //测试用
        //System.out.println(x);
        //System.out.println(y);

        //修改数组
        //如果下在棋盘里，并且下的位置不是下过的
        if (x >= 0 && x <= 15 && y >= 0 && y <= 15 && Global.dataarr[x][y] == 0 && Global.currentRound == Global.localRound && Global.isWin==0) {
            Global.chessCount++;
            if (Global.dataarr[x][y] == 0) {
                if (Global.localRound == 1) {
                    Global.dataarr[x][y] = 1;

                } else if (Global.localRound == 2) {
                    Global.dataarr[x][y] = 2;

                }
            }

            //判定阶段：注！判断尽量在本地判断，并给对手传输：是否胜利。
            result = rulesMethod.checkVictory(x, y, Global.localRound, Global.chessCount);

            Global.lastX=x;
            Global.lastY=y;
            Global.isWin=result;
            Global.isInput=Global.currentRound;
            Global.gameNeedSend=1;


            if (result == Global.localRound) {

                //进入判定回合
                String Winner=result==1?"白方":"黑方";
                JOptionPane.showMessageDialog(null, "共"+Global.chessCount+"步，"+Winner+"胜！！");
                Global.isWin=result;
                mf.repaint();

            } else if (result == 0) {
                Global.currentRound = (Global.currentRound == 1 ? 2 : 1);
                mf.repaint();
            } else if (result == 3) {
                //返回平局，游戏结束
            }


        }
    }
}
//寻找对手事件
class findIPAction implements ActionListener{
    JFrame jf;
    findIPAction(JFrame jf){
        this.jf=jf;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Global.localName = JOptionPane.showInputDialog("请输入你的名字：");
        String ipInput=JOptionPane.showInputDialog(null,"请输入对方的IP地址：\n","寻找对手",JOptionPane.PLAIN_MESSAGE);
        int findResult=new serverSocket().sentIP(ipInput);
        if(findResult==1){
            //隐藏连接界面
            //开始对战界面
            jf.setVisible(false);
        }
    }
}

//等待连接事件
class waitIPAction implements ActionListener{
    JFrame jf;
    waitIPAction(JFrame jf){
        this.jf=jf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Global.localName = JOptionPane.showInputDialog("请输入你的名字：");
        int waitResult=new serverSocket().receiveIP();
        if(waitResult==1){
            //隐藏连接界面
            //开始对战界面/
            jf.setVisible(false);
        }
    }
}

//发送消息事件
class sendMessageAction implements ActionListener{
    JTextArea receive,send;

    public sendMessageAction(JTextArea receive,JTextArea send){
        this.receive=receive;
        this.send=send;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Global.sendChatMessage=send.getText();
        if(!send.getText().equals(null)){
            send.setText("");
            SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");
            receive.append(Global.getMessageName()+Global.sendChatMessage+"\n\r");
            Global.messageNeedSend=1;
        }
    }
}



//发送语音事件
class sendVoiceAction implements ActionListener{
    JComboBox jcb;
    JTextArea textArea;
    sendVoiceAction(JComboBox jcb,JTextArea textArea){
        this.jcb=jcb;
        this.textArea=textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message;
        switch (jcb.getSelectedIndex()){

            case 1:{
                //播放语音
                message="好棋，好棋啊!";
                textArea.append(Global.getMessageName()+message+"\n\r");
                Global.sendChatMessage=message;
                Global.messageNeedSend=1;
                Global.musicNeedPlay=1;

                break;
            }
            case 2:{
                //播放语音
                message="你好！";
                textArea.append(Global.getMessageName()+message+"\n\r");
                Global.sendChatMessage=message;
                Global.messageNeedSend=1;
                Global.musicNeedPlay=2;
                break;
            }
            case 3:{
                //播放语音
                message="速速决定啊，太阳快下山了！";
                textArea.append(Global.getMessageName()+message+"\n\r");
                Global.sendChatMessage=message;
                Global.messageNeedSend=1;
                Global.musicNeedPlay=3;
                break;
            }
            case 4:{
                //播放语音
                message="哎呀，这棋下错了啊！";
                textArea.append(Global.getMessageName()+message+"\n\r");
                Global.sendChatMessage=message;
                Global.messageNeedSend=1;
                Global.musicNeedPlay=4;
                break;
            }
            case 5:{
                //播放语音
                message="承让，承让啊!";
                textArea.append(Global.getMessageName()+message+"\n\r");
                Global.sendChatMessage=message;
                Global.messageNeedSend=1;
                Global.musicNeedPlay=5;
                break;
            }
            default:{
                break;
            }

        }


    }
}



