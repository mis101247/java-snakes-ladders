

package swingserver;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class SwingServer extends JFrame{

    private ServerSocket server; 
    private Socket socket; 
    private JTextArea msgTextArea;
    public void createSocket() {
        try {
            server = new ServerSocket(9876);
            while (true) {
                msgTextArea.append("等待新客戶連接......\n");
                socket = server.accept();       
                msgTextArea.append("客戶端連接成功。" + socket + "\n");
                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public SwingServer(){
        super();
        setResizable(false);  // 視窗不可縮放
        setTitle("伺服器");
        setBounds(100, 100, 385, 266);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        final JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);       
        msgTextArea = new JTextArea();
        scrollPane.setViewportView(msgTextArea);
    }
    
    public static void main(String[] args) {
        SwingServer frame = new SwingServer();
        frame.setVisible(true);
        frame.createSocket();
    }
    
}
