package swingclient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;


 public class SwingClient extends JFrame {

    static  PrintWriter out;// 宣告輸出流對像
    String info;
    static String player_name="";//玩家名稱
    static int whereami=1; //目前位子
    static int number=0; //擲出
    static boolean won=false; //玩家是否贏了
    
    static JFrame dice;
    static JFrame ms_info;
    
    static JButton[] player;
    static JButton[] JB;
    static JButton GO;
    static JTextArea Msg=new JTextArea();
    static JScrollPane scrollPane = new JScrollPane(Msg);
    static JButton submit= new JButton("送出") ;     
    static JTextField text= new  JTextField () ;
    
    
    public  void createClientSocket() throws IOException {
        Socket socket = new Socket("localhost", 9876);
        out = new PrintWriter(socket.getOutputStream(), true);
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        GO.addActionListener(new GOEventListener());out.flush(); //骰子按鈕監聽
        submit.addActionListener(new submitEventListener());//送出按鈕監聽
        while (true) {
             info = in.readLine();
            if (info.equals("Player01")) {
                player_name="丁丁";
                dice.setTitle(player_name+"的骰子視窗");ms_info.setTitle(player_name+"的資訊視窗");
                out.println("/say【系統】" + "玩家 " + player_name + " 進入遊戲。" );out.flush();
            } else if (info.equals("Player02")) {
                player_name="小波";
                dice.setTitle(player_name+"的骰子視窗");ms_info.setTitle(player_name+"的資訊視窗");
                out.println("/say【系統】" + "玩家 " + player_name + " 進入遊戲。" );out.flush();
            } else if (info.equals("Player03")) {
                player_name="迪西";
                dice.setTitle(player_name+"的骰子視窗");ms_info.setTitle(player_name+"的資訊視窗");
                out.println("/say【系統】" + "玩家 " + player_name + " 進入遊戲。" );out.flush();
            } else if (info.equals("Player04")) {
                player_name="拉拉";
                dice.setTitle(player_name+"的骰子視窗");ms_info.setTitle(player_name+"的資訊視窗");
                out.println("/say【系統】" + "玩家 " + player_name + " 進入遊戲。" );out.flush();
            } else if (info.startsWith("/say")) {
             Msg.append(info.substring(4)+"\n");
            } else if (info.startsWith("/full")) {//當4人到齊時
                if(player_name.equals("丁丁")) GO.setEnabled(true); //這是第一個玩家則打開骰子
            }else if (info.startsWith("/change")){
                String[] change_data_Array = info.split(" ");// // /change(0)_原圖名(改後)(1)_終位圖名(改後)(2)_原位子(3)_終位子(4)_換誰(5)
                JB[Integer.valueOf(change_data_Array[3])].setIcon(new ImageIcon(change_data_Array[1]+".png"));
                JB[Integer.valueOf(change_data_Array[4])].setIcon(new ImageIcon(change_data_Array[2]+".png"));
                if(player_name.equals(change_data_Array[5])){//換我的話就把骰子打開 
                   if (won==false){//還沒獲得勝利
                    GO.setEnabled(true);
                   }  
                   else{
                     out.println("/won");out.flush();
                   } 
                } 
            }        
        }
         
    }
    

    public static void main(String[] args) throws IOException {
        SwingClient frame = new SwingClient();
       
        JPanel manJPanel = new JPanel();
        manJPanel.setLayout(new BoxLayout(manJPanel, BoxLayout.Y_AXIS));

        Image image=new ImageIcon("snakesandladders3.png").getImage();
        JPanel gridJPanel = new BackgroundPanel(image);//上面的框架
        gridJPanel.setLayout(new GridLayout(10, 10));
        gridJPanel.setPreferredSize( new Dimension(500,500));
        
      
        JB = new JButton[101];
        for (int i = 1 ;i<=100;i++){
         JB[i] = new JButton();
         JB[i].setHorizontalAlignment(JButton.CENTER);//字置中
         JB[i].setContentAreaFilled(false);//設置物件透明
         JB[i].setBorder(null);          //去邊框
        }
        
        for (int i=100;i>=91;i--){gridJPanel.add(JB[i]);}
        for (int i=81;i<=90;i++){gridJPanel.add(JB[i]);}
        for (int i=80;i>=71;i--){gridJPanel.add(JB[i]);}
        for (int i=61;i<=70;i++){gridJPanel.add(JB[i]);}
        for (int i=60;i>=51;i--){gridJPanel.add(JB[i]);}
        for (int i=41;i<=50;i++){gridJPanel.add(JB[i]);}
        for (int i=40;i>=31;i--){gridJPanel.add(JB[i]);}
        for (int i=21;i<=30;i++){gridJPanel.add(JB[i]);}
        for (int i=20;i>=11;i--){gridJPanel.add(JB[i]);}
        for (int i=1;i<=10;i++){gridJPanel.add(JB[i]);}
        
     
        manJPanel.add(gridJPanel);//將按鈕放入上面框架
        

     
        //初始化動作
        GO.setEnabled(false); //將骰子鎖定
        JB[1].setIcon(new ImageIcon("1111.png"));//放點點
       //
        
        
        frame.add(manJPanel);
        frame.setVisible(true);
        frame.createClientSocket();
        
     
    }

    public SwingClient() throws IOException {
        super();
        setResizable(false);  // 視窗不可縮放
        setTitle("Snakes And Ladders");
        setBounds(0, 0, 900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        dice = new JFrame("骰子視窗");//開新視窗JFrame
        dice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dice.setBounds(910, 425, 400, 400);//900 850
        dice.setVisible(true);
        GO= new JButton();
        dice.add(GO);
        GO.setIcon(new ImageIcon("dice0.png"));

        
        ms_info = new JFrame("資訊視窗");//開新視窗JFrame
        ms_info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ms_info.setBounds(910, 0, 450, 425);//900 850
        ms_info.setVisible(true);

        JPanel message = new JPanel();     

        message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));//分成上下兩塊
        
        scrollPane.setPreferredSize(new Dimension(400, 400));
        message.add(scrollPane); //將顯示板放上去
        
        JPanel message_bottom = new JPanel(); //建立如→(    text    )(submit)     
       

        text.setPreferredSize(new Dimension(350, 30)); 

        message_bottom.add(text);
        message_bottom.add(submit);
        
        
        message.add(message_bottom); 

        
        ms_info.add(message);
        
    }

        public static class BackgroundPanel extends JPanel {//參考資料來源:http://rritw.com/a/JAVAbiancheng/SWING/20120902/216378.html
	
	private static final long serialVersionUID = -6352788025440244338L;
	
	private Image image = null;

	public BackgroundPanel(Image image) {
		this.image = image;
	}

	// 固定背景圖片，允許這個JPanel可以在圖片上添加其他組件
        @Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
    }
        
        
        static class GOEventListener implements ActionListener{
             
            @Override
            public  void actionPerformed(ActionEvent e) {  
               Random ran = new Random();number =ran.nextInt(6)+1;
               GO.setIcon(new ImageIcon("dice" +number+".png"));
               out.println("/go " + whereami + " " + number + " " + (whereami+number) );out.flush();
               GO.setEnabled(false); //按完就鎖
               whereami = check(whereami+number);
            }         
        }     
        
        static class submitEventListener implements ActionListener{
            @Override
            public  void actionPerformed(ActionEvent e) { 
               
              if (text.getText().startsWith("/god")  ){//作弊程式
                int cheat=Integer.valueOf(String.valueOf(text.getText().charAt(4)));//先從char 轉 string 再轉 int   
                if (cheat >=1 && cheat <=6 && GO.isEnabled()==true){
                  out.println("/go " + whereami + " " + cheat + " " + (whereami+cheat) );out.flush();  
                  GO.setEnabled(false); //按完就鎖
                  whereami = check(whereami+cheat);  
                }
              }
              else{
               out.println("/say " + player_name + "：" + text.getText() );out.flush();
              } 
              text.setText("");//按完就清除
            }         
        }     
        
     static int check(int x){//判斷是否有事件產生
      int temp=0;
      switch (x){
         case 3:temp=21;break; 
         case 8:temp=30;break; 
         case 17:temp=13;break; 
         case 28:temp=84;break; 
         case 52:temp=29;break; 
         case 57:temp=40;break; 
         case 62:temp=22;break; 
         case 58:temp=77;break; 
         case 75:temp=86;break; 
         case 80:temp=100;won=true;break; 
         case 88:temp=18;break; 
         case 90:temp=91;break;
         case 95:temp=51;break; 
         case 97:temp=79;break;
         case 100:case 101:case 102:case 103:case 104:case 105:temp =100;   won=true;  break;
         default: temp = x;break;    
     }
     return temp;
    }
}