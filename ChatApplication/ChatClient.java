
package ChatApplication;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Lauro Ribeiro
 */
public class ChatClient {
    
    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22,40);
    static JTextField textField = new JTextField(40);
    static JLabel blankLabel = new JLabel("      ");
    static JButton sendButton = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("     ");
    
    
    ChatClient(){
        
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);
        
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475,500);
        chatWindow.setVisible(true);
        
        textField.setEditable(false);
        chatArea.setEditable(false);
        
        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());
    
    }
    
    void startChat() throws Exception{
        
        String ipAddress = JOptionPane.showInputDialog(chatWindow,
                "Enter Ip Address: ",
                "IP Address Required!!", 
                JOptionPane.PLAIN_MESSAGE);
        
        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);
            
        while(true){
            
            String str = in.readLine();
            if(str.equals("NameRequired")){
                String name = JOptionPane.showInputDialog(chatWindow,
                        "Enter a unique name: ",
                        "Name Required!!",
                        JOptionPane.PLAIN_MESSAGE);
                
                out.println(name);
                
            }else if(str.equals("NameAlreadyExists")){
                String name = JOptionPane.showInputDialog(chatWindow,
                        "Enter another name: ",
                        "Name already exists!!",
                        JOptionPane.WARNING_MESSAGE);
                
                out.println(name);
            } else if(str.startsWith("NameAccepted")){
                textField.setEditable(true);
                nameLabel.setText("You are logged in as: "+str.substring(12));
            }
            else{
                chatArea.append(str + "\n");
            }
        }
                
    }
    
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           ChatClient.out.println(ChatClient.textField.getText());
           ChatClient.textField.setText("");
        }
        
    }
    
    public static void main(String[]args) throws Exception{
        ChatClient client = new ChatClient();
        client.startChat();
    }
}
