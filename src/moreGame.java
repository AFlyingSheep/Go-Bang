import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class moreGame {
    moreGame(){
        //开启进程
        mainFrame mf=new mainFrame();
        GUI gui=new GUI(mf);
        sendMessage send=new sendMessage(gui);
        recieceMessage recieve=new recieceMessage(gui);
        writeFile wf=new writeFile();
        musicPlay mp=new musicPlay();
        backgroundMusic bgm=new backgroundMusic();

        Thread t1=new Thread(send);
        Thread t2=new Thread(recieve);
        Thread t3=new Thread(wf);
        Thread t4=new Thread(mp);
        Thread t5=new Thread(bgm);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

    }
}

class sendMessage implements Runnable{
    GUI gui;
    sendMessage(GUI gui){
        this.gui=gui;
    }
    public void run(){
        try{
            int port=0;
            if(Global.connectFlags==1) port=25563;
            else port=25562;

            while(true){
                TimeUnit.MICROSECONDS.sleep(100);
                if(Global.gameNeedSend==1){
                    Socket socket=new Socket(Global.opponentIP,port);
                    OutputStream out=socket.getOutputStream();
                    out.write(("GAMEMESSAGE,"+Global.lastX+","+Global.lastY+","+Global.isWin).getBytes());
                    Global.gameNeedSend=0;
                    Global.currentRound=(Global.localRound==1?2:1);
                    socket.close();
                }
                else if(Global.messageNeedSend==1){
                    Socket socket=new Socket(Global.opponentIP,port);
                    OutputStream out=socket.getOutputStream();
                    out.write(("MESSAGE,"+Global.sendChatMessage).getBytes());
                    Global.messageNeedSend=0;
                    socket.close();
                }
                else if(Global.renewNeedSend==1){
                    //发送重开信息
                    Socket socket=new Socket(Global.opponentIP,port);
                    OutputStream out=socket.getOutputStream();
                    out.write(("RENEWMESSAGE").getBytes());
                    Global.renewNeedSend=0;
                    socket.close();
                    //接受对方是否同意
                    ServerSocket ss=new ServerSocket(25560);
                    Socket s=ss.accept();
                    InputStream in =s.getInputStream();
                    byte[] buf=new byte[1024];
                    int len=in.read(buf);
                    String str=new String(buf,0,len) ;
                    if(str.equals("ACRENEW")){
                        JOptionPane.showMessageDialog(null, "对方接受重开！");
                        rulesMethod.initial(gui.mf);
                    }
                    else {
                        JOptionPane.showMessageDialog
                                (null, "对方拒绝了！", "消息",JOptionPane.WARNING_MESSAGE);
                    }
                    ss.close();
                    s.close();
                }
            }

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "连接断开！！","ERROR!" ,
                    JOptionPane.ERROR_MESSAGE);
            exit(0);
        }


    }
}

class recieceMessage implements  Runnable{
    GUI gui;
    recieceMessage(GUI gui){
        this.gui=gui;
    }
    public void run(){
        try{
            int port=0;
            if(Global.connectFlags==2) port=25563;
            else port=25562;
            ServerSocket ss=new ServerSocket(port);
            while(true){
                //信息接收
                Socket s=ss.accept();
                InputStream in=s.getInputStream();
                byte[] buf=new byte[1024];
                int len=in.read(buf);
                String[] str=new String(buf,0,len).split(",");

                //如果是游戏消息
                if(str[0].equals("GAMEMESSAGE")){
                    //更新棋盘
                    int x=Integer.valueOf(str[1]);
                    int y=Integer.valueOf(str[2]);
                    int result=Integer.valueOf(str[3]);
                    Global.chessCount++;
                    Global.dataarr[x][y]=Global.currentRound;
                    Global.lastX=x;
                    Global.lastY=y;
                    if(Global.isWin==0)
                        Global.isInput=Global.currentRound;


                    gui.mf.picF.repaint();
                    if(result!=0){
                        String Winner=result==1?"白方":"黑方";
                        JOptionPane.showMessageDialog(null, "共"+Global.chessCount+"步，"+Winner+"胜！！");
                        Global.isWin=result;
                        Global.isInput=result;
                        gui.mf.repaint();
                    }
                    Global.currentRound=Global.localRound;
                    gui.mf.repaint();
                }

                //如果是聊天消息
                else if (str[0].equals("MESSAGE")){
                    //更新聊天框
                    gui.mf.displayMes.append(Global.getMessageOpponentName()+str[1]+"\n\r");
                    if(str[1].equals("好棋，好棋啊!")) Global.musicNeedPlay=1;
                    if(str[1].equals("你好！")) Global.musicNeedPlay=2;
                    if(str[1].equals("速速决定啊，太阳快下山了！")) Global.musicNeedPlay=3;
                    if(str[1].equals("哎呀，这棋下错了啊！")) Global.musicNeedPlay=4;
                    if(str[1].equals("承让，承让啊!")) Global.musicNeedPlay=5;
                }

                //如果是重开消息
                else if(str[0].equals("RENEWMESSAGE")){
                    int n = JOptionPane.showConfirmDialog
                            (null, "对手申请重开，是否接受？", "重开",JOptionPane.YES_NO_OPTION);
                    if(n==0)
                        rulesMethod.initial(gui.mf);
                    Socket socket=new Socket(Global.opponentIP,25560);
                    OutputStream out=socket.getOutputStream();
                    out.write(n==0?"ACRENEW".getBytes():"WRRENEW".getBytes());
                    socket.close();
                }
            }

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "连接断开！！","ERROR!" ,
                    JOptionPane.ERROR_MESSAGE);
            exit(0);
        }
    }
}

class writeFile implements Runnable{

    @Override
    public void run() {
        try{
            while(true){
                TimeUnit.MICROSECONDS.sleep(100);
                while (Global.isWin == 0) {
                    //写文件操作
                    String[] player;
                    if(Global.localRound==1){
                        player =new String[]{Global.localName,Global.opponentName};
                    }
                    else {
                        player =new String[]{Global.opponentName,Global.localName};
                    }



                    String path="data/gameReview/"+"Re-"+Global.getNowTime()+"-"+player[0]+"-"+player[1]+".review";
                    File file = new File(path);
                    if(!file.getParentFile().exists()){
                        file.mkdir();
                    }
                    FileWriter fw=new FileWriter(file);
                    while(Global.isWin==0){
                        if(Global.isInput==1 && Global.isWin==0){
                            fw.write("1,"+Global.lastX+","+Global.lastY+",");
                            fw.flush();
                            Global.isInput=0;
                        }
                        else if(Global.isInput==2 && Global.isWin==0){
                            fw.write("2,"+Global.lastX+","+Global.lastY+",");
                            fw.flush();
                            Global.isInput=0;
                        }
                        TimeUnit.MICROSECONDS.sleep(100);
                    }
                    if(Global.localRound==Global.isWin){
                        fw.write(Global.isWin+","+Global.lastX+","+Global.lastY+",");

                    }
                    fw.write("Winner,"+Global.isWin);
                    fw.flush();
                    fw.close();
                }
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "啥几把玩意","ERROR!" ,
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}

class musicPlay implements Runnable{

    @Override
    public void run() {
        while(true){
            try{
                TimeUnit.MICROSECONDS.sleep(1000);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if(Global.musicNeedPlay!=0){
                startMusic(Global.musicNeedPlay);
                Global.musicNeedPlay=0;
            }
        }
    }

    public static void startMusic(int n){
        try{
            File sound=new File("music//"+n+".wav");
            AudioClip soundclip= Applet.newAudioClip(sound.toURL());
            soundclip.play();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

class backgroundMusic implements Runnable{

    @Override
    public void run() {
        //播放音乐
        String filepath="music//bgm1.wav";
        musicStuff musicStuff=new musicStuff();
        musicStuff.playMusic(filepath);
    }
}