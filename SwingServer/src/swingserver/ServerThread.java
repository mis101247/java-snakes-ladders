package swingserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays; 

public class ServerThread extends Thread {

    Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    String info;
    static int user = 0;
    static String user_name[]={"丁丁","小波","迪西","拉拉"};
    static int who = 0;//第一個開始
    static String s="";//判斷 
    static Vector players = new Vector(4);
    static String image_name[] =new String[101]; //地圖各button內的圖名
    

    public ServerThread(Socket socket) throws IOException {
        synchronized (players) {
            players.addElement(this);
        }
       
        user++;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() {
        Arrays.fill(image_name,"0000");//"100個button圖為0000"
        image_name[1]="1111";//目前第一格為全玩家在
        if (user == 1) {
            out.println("Player01");
            out.flush();
        } else if(user ==2){
            out.println("Player02");
            out.flush();
        } else if(user ==3){
            out.println("Player03");
            out.flush();
        } else if(user==4){
            out.println("Player04");
            out.flush();
            talk_all("/full");           
        }
        while (true) {
            try {
                info = in.readLine();// 讀取資訊
                System.out.println(info);
                if(info.startsWith("/say")){
                    talk_all(info);
                }
                else if (info.startsWith("/go")){
                   String[] go_data_Array = info.split(" ");// "/go"(0)  "目前位子"(1) "擲出點數"(2) "原目的"(3)
                   
                   int origin_seat = Integer.valueOf(go_data_Array[1]); //原位子
                   int final_seat = check(go_data_Array[3]); // 最終位子
                   int oi =Integer.valueOf(String.valueOf("1248".charAt(who)));//oi為使用者處理過後編號
                   
                   change_img( origin_seat,final_seat, oi);    //a為原本 b為終位子 oi為使用者處理過後編號
                   
                   int who2 =who+1; who2=who2%4; //預備下一位
                   talk_all("/change "+ image_name[origin_seat] + " "+ image_name[final_seat] + " " + origin_seat + " "+ final_seat + " " + user_name[who2]);
                            // /change_原圖名(改後)_終位圖名(改後)_原位子_終位子_換誰
                  // System.out.println("/change "+ image_name[origin_seat] + " "+ image_name[final_seat] + " " + origin_seat + " "+ final_seat + " " + user_name[who2]);
                   talk_all("/say【系統】"+user_name[who]+"擲出"+go_data_Array[2] +"點，從位置 " + origin_seat + " 移動到 " + final_seat + " 位置上" +s);
                   if (final_seat==100){talk_all("/say【系統】恭喜玩家 " + user_name[who] + " 走到終點獲得勝利。");}
                   who =who2; //換下一位使用者
                }else if (info.startsWith("/won")){
                   who++;who=who%4; 
                   talk_all("/change "+ image_name[100] + " "+ image_name[100] + " " + "100" + " "+ "100" + " " + user_name[who]);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void talk_all(String tt){
    for (int i = 0; i < players.size(); i++) {   
                synchronized (players) {
                    ServerThread self = (ServerThread) players.elementAt(i);     
                    self.out.println(tt);
                    self.out.flush();
                }
        }
    }
    
    int check(String x){//判斷是否有事件產生
      int temp=0;
      int y = Integer.parseInt(x); // String 轉換 int
      switch (y){
         case 3:temp=21;s="(因為爬上樓梯)";break; 
         case 8:temp=30;s="(因為爬上樓梯)";break; 
         case 17:temp=13;s="(因為被蛇吃掉)";break; 
         case 28:temp=84;s="(因為爬上樓梯)";break; 
         case 52:temp=29;s="(因為被蛇吃掉)";break; 
         case 57:temp=40;s="(因為被蛇吃掉)";break; 
         case 62:temp=22;s="(因為被蛇吃掉)";break; 
         case 58:temp=77;s="(因為爬上樓梯)";break; 
         case 75:temp=86;s="(因為爬上樓梯)";break; 
         case 80:temp=100;s="(因為爬上樓梯)";break; 
         case 88:temp=18;s="(因為被蛇吃掉)";break; 
         case 90:temp=91;s="(因為爬上樓梯)";break;
         case 95:temp=51;s="(因為被蛇吃掉)";break; 
         case 97:temp=79;s="(因為被蛇吃掉)";break;
         case 101:case 102:case 103:case 104:case 105:temp =100;break;
         default: temp = y;s="";break;    
     }
     return temp;
    }
    
    void change_img(int a,int b,int oi){ // a為原本 b為終位子 oi為使用者處理過後編號
     int tempa =Integer.parseInt(image_name[a],2); //把2進位轉10進位
     tempa = tempa ^ oi;  // XOR 為^
     image_name[a] =Integer.toBinaryString(tempa);
     image_name[a] = "0000".substring(0, 4 - image_name[a].length())+image_name[a];
     
     int tempb =Integer.parseInt(image_name[b],2); //把2進位轉10進位
     tempb = tempb | oi;  // OR 為|
     image_name[b] =Integer.toBinaryString(tempb);
     image_name[b] = "0000".substring(0, 4 - image_name[b].length())+image_name[b];
     
        
    }
}
