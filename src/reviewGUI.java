import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class reviewGUI {
}

//显示所有文件界面
class displayReview extends JFrame {
    displayReview() {
        //窗体初始化设置
        this.setSize(400, 100);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setTitle("选择场次");

        //显示所有文件，返回数组
        String[] name=method.displayFile();

        //创建复选框
        JComboBox choiceMessage=new JComboBox();
        choiceMessage.setBounds(30,20,200,30);
        //清除初始信息
        choiceMessage.removeAllItems();
        //遍历每个文件，修改为可观看文件
        for(int i=0;i<name.length;i++){
            String [] arr;
            arr=name[i].split("-");
            arr[5]=arr[5].replaceAll(".review","");
            String label=arr[1]+"时"+arr[2]+"分"+arr[3]+"秒"+"-"+arr[4]+"对战"+arr[5];
            choiceMessage.addItem(label);
        }
        //加入到panel中
        displayPanel dp=new displayPanel(this);
        this.add(dp);
        dp.add(choiceMessage);

        //加入选中Button
        JButton choose=new JButton("选择");
        choose.setBounds(300,10,80,50);
        //选中
        choose.addActionListener(new chooseAction(choiceMessage,this));
        //修改名字

        dp.add(choose);

        displayBackground db=new displayBackground(this);
        this.add(db);

        this.repaint();
    }
}

//添加框件
class displayPanel extends JPanel{
    displayReview dr;
    String fileChoose;
    public displayPanel(displayReview dr){
        this.dr=dr;
        this.setBounds(0,0,dr.getWidth(),dr.getHeight());
        this.setVisible(true);
        this.setLayout(null);
    }
}

//添加背景
class displayBackground extends JPanel{
    displayReview dr;
    public displayBackground(displayReview dr){
        this.dr=dr;
        this.setBounds(0,0,dr.getWidth(),dr.getHeight());
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
        int x = 0, y = 0;
        ImageIcon icon = new ImageIcon("image//background5.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放
    }
}

//浏览对局界面-------------------------------------------------------------------------------------

class reviewFrame extends JFrame{
    pictureFrame pf2;
    reviewPanel rp=new reviewPanel(this);
    reviewFrame(pictureFrame pf2){
        //窗体初始化设置
        this.pf2=pf2;
        this.setSize(1000,1000);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);

        //修改步骤的两个按钮
        JButton pre=new JButton("上一步");
        JButton next=new JButton("下一步");
        pre.setBounds(200,910,200,50);
        next.setBounds(600,910,200,50);

        //添加返回按钮
        JButton back=new JButton("退出");
        back.setBounds(890,910,100,50);

        this.add(back);

        //添加关闭事件
        back.addActionListener(new ActionListener() {
            reviewFrame rf;

            @Override
            public void actionPerformed(ActionEvent e) {
                new indexFrame();
                rf.dispose();
            }

            public ActionListener accept(reviewFrame rf){
                this.rf=rf;
                return this;
            }
        }.accept(this));

        //按钮加入
        this.add(pre);
        this.add(next);

        //加入棋盘
        pf2.setBounds(100,100,800,800);
        this.add(pf2);

        //加入标题
        String t=Global.player[0]+"(执白)--VS--"+Global.player[1]+"（执黑）";
        JLabel test=new JLabel(t);
        test.setBounds(20,10,1000,100);
        Font f=new Font("楷体",Font.PLAIN,30);

        test.setFont(f);

        this.add(test);

        //加入背景
        reviewBackground rb=new reviewBackground(this);
        this.add(rb);


        //为按钮添加事件
        pre.addActionListener(new ActionListener() {
            pictureFrame pf;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(Global.nowPoint==0){
                    JOptionPane.showMessageDialog(null, "已经是第一步！","ERROR!" ,
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    Global.nowPoint-=3;
                    Global.dataarr[Integer.parseInt(Global.reviewData[Global.nowPoint+1])]
                            [Integer.parseInt(Global.reviewData[Global.nowPoint+2])]=0;
                    pf.repaint();
                }
            }

            public ActionListener accept(pictureFrame pf){
                this.pf=pf;
                return this;
            }
        }.accept(pf2));

        next.addActionListener(new ActionListener() {
            pictureFrame pf;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(Global.nowPoint==Global.reviewData.length-2){
                    JOptionPane.showMessageDialog(null,(Global.reviewData[Global.nowPoint+1].equals("1")?"黑棋胜！":"白棋胜！"));

                }
                else{
                    try{
                        Global.dataarr[Integer.parseInt(Global.reviewData[Global.nowPoint+1])]
                                [Integer.parseInt(Global.reviewData[Global.nowPoint+2])]
                                =Integer.parseInt(Global.reviewData[Global.nowPoint]);
                        Global.nowPoint+=3;
                        pf.repaint();
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
            }

            public ActionListener accept(pictureFrame pf){
                this.pf=pf;
                return this;
            }
        }.accept(pf2));



    }
}

//主背景
class reviewBackground extends JPanel{
    reviewFrame rf;
    public reviewBackground(reviewFrame rf){
        this.rf=rf;
        this.setBounds(0,0,rf.getWidth(),rf.getHeight());
        this.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g){
        int x = 0, y = 0;
        ImageIcon icon = new ImageIcon("image//background4.jpg");
        g.drawImage(icon.getImage(), x, y, getSize().width,
                getSize().height, this);// 图片会自动缩放
    }
}

//控件
class reviewPanel extends JPanel{
    reviewFrame rf;
    public reviewPanel(reviewFrame rf){
        this.rf=rf;
        this.setBounds(0,0,rf.getWidth(),rf.getHeight());
        this.setLayout(null);
    }
}

//方法-----------------------------------------------------------------
class method {

    //显示文件
    public static String[] displayFile() {
        //路径
        File desktop = new File("data//gameReview");

        String[] arr = desktop.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                //把dir 和name都封装到一个文件对象里
                File file = new File(dir, name);
                return file.isFile() && file.getName().endsWith(".review");
            }
        });

        //返回名字数组
        return arr;
    }

    //读取文件
    public static String[] gamedataRead(String path) {
        try {
            File file = new File(path);//定义一个file对象，用来初始化FileReader
            FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            bReader.close();
            return sb.toString().split(",");
        } catch (Exception e) {
            System.out.println(e);

        }
        return null;
    }
}

class chooseAction implements ActionListener{
    JComboBox jb;
    displayReview dr;
    chooseAction(JComboBox jb,displayReview dr){
        this.jb=jb;
        this.dr=dr;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Global.fileChoose=jb.getSelectedIndex();
        String[] name=method.displayFile();
        Global.filePath=name[Global.fileChoose];
        Global.nowPoint=0;
        //读取数据
        if(Global.filePath!=null){
            Global.reviewData=method.gamedataRead("data//gameReview//"+Global.filePath);
            String[] str=Global.filePath.split("-");
            Global.player=new String[]{str[4],str[5].replaceAll(".review","")};
            dr.dispose();
            for(int i=0;i<16;i++){
                for(int j=0;j<16;j++){
                    Global.dataarr[i][j]=0;
                    Global.aiarr[i][j]=0;
                }
            }
            new reviewFrame(new pictureFrame());
        }
    }
}