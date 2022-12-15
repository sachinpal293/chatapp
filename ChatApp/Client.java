import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Client extends JFrame {
    Socket socket;

    BufferedReader br;
    PrintWriter out;
    
    // Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField  messagInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    // Constructor
    public Client()
    {
        
        try {
        System.out.println("Sending Request to server.....");  
          socket = new Socket("127.0.01",7777);
        System.out.println("Connection Done.");

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
         
        CreateGUI();
        handleEvents();
        startReading();
        // startWriting();


        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
    /**
     * 
     */
    private void handleEvents()
    {
        messagInput.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key Released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    // System.out.println("You have pressed enter button");
                    String contentToSend = messagInput.getText();
                    messageArea.append("\t"+"Me : "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messagInput.setText("");
                    messagInput.requestFocus();
                }
                
            }

        });
    }
    private void CreateGUI()
    {
        this.setTitle("Client Messanger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messagInput.setFont(font);
        
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        // frame's layout
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        // this.add(messageArea,BorderLayout.CENTER);
        this.add(messagInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }

    // Reading Method
    public void startReading()
    {
         // thread-reader
         Runnable r1 = ()->{
          System.out.println("Reader starting...");
          try{
          while(true)
          {
           
                String msg = br.readLine();
                if(msg.equals("exit"))
                {
                    // System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server Terminated the Chat");
                    messagInput.setEnabled(false);
                    socket.close();
                    break;
                }
                // System.out.println("Server : "+msg);
                messageArea.append("Server : "+msg+"\n");
          }
        }catch(Exception e)
        {
            // e.printStackTrace();
            System.out.println("connection is closed.");
        }
         };

         new Thread(r1).start();
    }


    // Writting Method
    public void startWriting()
    {
         // thread for get data from the user and send to client
         Runnable r2 = ()->{
            System.out.println("Writer started......");
            try{
            while(!socket.isClosed())
            {
                
                    
                     BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                     String content = br1.readLine();

                     out.println(content);
                     out.flush();

                     if(content.equals("exit"))
                    {
                       socket.close();
                       break;
                    }

            }
            // System.out.println("Connection is closed.");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

         };

         new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is client and going to start");

        new Client();
    }
}
