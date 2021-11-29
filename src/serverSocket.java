import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.TimeoutException;

public class serverSocket{
    //等待收取请求
    int receiveIP(){
        try{
            //JOptionPane.showMessageDialog(null, "等待中，请等待……");
            //建立服务端socket并接听
            ServerSocket server=new ServerSocket(25565);
            server.setReuseAddress(true);

            //server.setSoTimeout(3000);
            //阻塞方法，接听
            Socket socket=server.accept();
            //接收信息并将信息分割
            InputStream in=socket.getInputStream();
            byte[]buf=new byte[1024];
            int len=in.read(buf);
            String message[]=new String(buf,0,len).split(",");
            if(message[0].equals("CONNECT")){
                //创建是否确认接受请求的对话框
                int res=JOptionPane
                        .showConfirmDialog(null, "对方IP:"+message[1]+" " +
                                "对方姓名"+message[2]+",是否同意？", "收到对战请求！", JOptionPane.YES_NO_OPTION);
                //若同意，回复信息并函数返回1
                if(res==JOptionPane.YES_OPTION){
                    Global.opponentIP=message[1];
                    Socket send=new Socket();
                    send.setReuseAddress(true);
                    send.connect(new InetSocketAddress(message[1],25564));
                    Global.connectFlags=1;
                    Global.opponentName=message[2];
                    OutputStream out=send.getOutputStream();
                    out.write(("CONNECTBACK,AC,"+Global.localName).getBytes());

                    Global.localRound=1;

                    moreGame mg=new moreGame();

                    //关闭连接
                    socket.shutdownInput();
                    send.shutdownOutput();
                    socket.close();
                    send.close();
                    server.close();
                    return 1;
                }
                //若拒绝，回复信息并返回2
                else{
                    Socket send=new Socket();
                    send.setReuseAddress(true);
                    send.connect(new InetSocketAddress(message[1],25564));
                    OutputStream out=send.getOutputStream();
                    out.write("CONNECTBACK,NA".getBytes());

                    //关闭连接
                    socket.shutdownInput();
                    send.shutdownOutput();
                    socket.close();
                    send.close();
                    server.close();
                    return 0;
                }
           }



        }
        //连接失败或过程失败返回
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "建立连接失败！","ERROR!" ,
                    JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        return 0;
    }

    //向指定的IP地址发出发联机的请求并监听对方是否同意
    int sentIP(String ipAddress){
        try{
            if(ipAddress==null)
                throw new Exception();
            //如果要连接的地址不为空
            //连接指定IP

            Socket s = new Socket();
            //JOptionPane.showMessageDialog(null, "连接中，请等待……");
            Global.connectflag=1;
            s.setReuseAddress(true);
            s.connect(new InetSocketAddress(ipAddress,25565),30000);
            Global.connectflag=0;

            //创建输出流
            OutputStream out = s.getOutputStream();
            //发送连接信息
            out.write(("CONNECT," + Global.localIP + "," + Global.localName).getBytes());
            s.shutdownOutput();
            s.close();
            //JOptionPane.showMessageDialog(null, "连接成功，等待对手回复……");
            //创建服务端监听端口，等待回复
            ServerSocket receive=new ServerSocket();
            receive.setReuseAddress(true);
            receive.bind(new InetSocketAddress(25564));

            //状态改为等待接收……

            Socket s2=receive.accept();

            //接收成功
            Global.connectflag=0;

            InputStream in=s2.getInputStream();
            byte[] buf=new byte[1024];
            int len=in.read(buf);
            String[] result=new String(buf,0,len).split(",");
            //查看结果并返回不同的值
            if(result[0].equals("CONNECTBACK")){
                if(result[1].equals("AC")){
                    JOptionPane.showMessageDialog(null, "对方同意了请求，准备开始！");
                    Global.opponentIP=s.getInetAddress().getHostAddress();
                    Global.connectFlags=2;
                    Global.localRound=2;
                    Global.opponentName=result[2];
                    //关闭连接
                    s2.shutdownInput();
                    s2.close();
                    receive.close();

                    moreGame mg=new moreGame();


                    return 1;
                }
                else{
                    JOptionPane.showMessageDialog(null, "对方拒绝了！", "ERROR!",JOptionPane.ERROR_MESSAGE);

                    //关闭连接
                    s2.shutdownInput();
                    s2.close();
                    receive.close();
                    return 0;
                }
            }
        }
        //连接失败或过程失败返回
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "连接失败！","ERROR!" ,
                    JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        return 0;
    }

}