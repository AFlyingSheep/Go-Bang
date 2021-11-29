import java.text.SimpleDateFormat;
import java.util.Date;

public class Global {

    //存储游戏回合信息

    public static int dataarr[][]=new int [16][16];
    public static int aiarr[][]=new int [16][16];
    public static int localRound=2;
    public static int chessCount=0;
    public static int currentRound=1;

    //存储自己与对手的信息

    public static String opponentIP;
    public static String opponentName;
    public static String localName;
    public static String localIP;

    //连接状态，1为发出方等待对方回应，0为正常状态

    public static int connectflag=0;
    public static int connectFlags;
    public static int lastX;
    public static int lastY;
    public static int gameNeedSend=0;
    public static int messageNeedSend=0;
    public static int renewNeedSend=0;
    public static int isInput=0;
    public static int isWin=0;
    public static String sendChatMessage;
    public static int fileChoose;
    public static int nowPoint=0;
    public static String[] reviewData;
    public static String filePath;
    public static String[] player;
    public static int musicNeedPlay=0;
    public static String getNowTime(){
        SimpleDateFormat df=new SimpleDateFormat("HH-mm-ss");
        return df.format(new Date());
    }
    public static String getMessageName(){
        return getNowTime()+"--"+Global.localName+":";
    }
    public static String getMessageOpponentName(){
        return getNowTime()+"--"+Global.opponentName+":";
    }
}
