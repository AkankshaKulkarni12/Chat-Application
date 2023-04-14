package chat.application;

/**
 *
 * @author Akanksha Kulkarni
 */

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.text.*;
import java.net.*;

public class Server implements ActionListener{
    
    JTextField textArea;
    JPanel panel2;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    
    Server(){
        
        f.setLayout(null);
        
        // Panel 1 - create green upper panel 
        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(7, 94, 84));
        panel1.setBounds(0, 0, 400, 65);
        panel1.setLayout(null);
        f.add(panel1);
        
        // Back Button
        ImageIcon imageBackButton = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image imageScaledBack  = imageBackButton.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon scaledBackButton = new ImageIcon(imageScaledBack);
        JLabel backImage = new JLabel(scaledBackButton );
        backImage.setBounds(10, 20, 25, 25);
        panel1.add(backImage);
        
        // event for mouse click for Back Button
        backImage.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent me){
                System.exit(0);
            }
        });
        
        // Profile Button
        ImageIcon profileImageButton = new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image imageScaledProfile  = profileImageButton.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT);
        ImageIcon scaledProfileButton = new ImageIcon(imageScaledProfile);
        JLabel profileImage = new JLabel(scaledProfileButton );
        profileImage.setBounds(40, 10, 45, 45);
        panel1.add(profileImage);
        
        // Video Button
        ImageIcon videoImageButton = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image imageScaledVideo  = videoImageButton.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon scaledVideoButton = new ImageIcon(imageScaledVideo);
        JLabel videoImage = new JLabel(scaledVideoButton );
        videoImage.setBounds(280, 20, 30, 30);
        panel1.add(videoImage);
        
        // Telephone Button
        ImageIcon telephoneImageButton = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image imageScaledTelephone  = telephoneImageButton.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon scaledTelephoneButton = new ImageIcon(imageScaledTelephone);
        JLabel telephoneImage = new JLabel(scaledTelephoneButton );
        telephoneImage.setBounds(327, 20, 30, 30);
        panel1.add(telephoneImage);
        
        // More Button
        ImageIcon moreImageButton = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image imageScaledMore  = moreImageButton.getImage().getScaledInstance(10, 18, Image.SCALE_DEFAULT);
        ImageIcon scaledMoreButton = new ImageIcon(imageScaledMore);
        JLabel moreImage = new JLabel(scaledMoreButton );
        moreImage.setBounds(360, 20, 30, 30);
        panel1.add(moreImage);
        
        // Name Label
        JLabel name = new JLabel("Charles");
        name.setBounds(100, 10, 90, 30);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SANS SERIF", Font.BOLD, 18));
        panel1.add(name);
        
        // Status Label
        JLabel status = new JLabel("Online");
        status.setBounds(100, 30, 90, 30);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SANS SERIF", Font.PLAIN, 12 ));
        panel1.add(status);
        
        
        // Panel 2 - Chatting Panel
        panel2 = new JPanel();
        panel2.setBounds(5, 70, 377, 450);
        panel2.setLayout(null);
        f.add(panel2);
        
        // Text Area
        textArea = new JTextField();
        textArea.setBounds(5, 525, 300, 35);
        textArea.setFont(new Font("SANS SERIF", Font.PLAIN, 18));
        f.add(textArea);
        
        // Send Button
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(307, 525, 75, 35);
        sendButton.setBackground(new Color(7, 94, 84));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SANS SERIF", Font.BOLD, 16));
        sendButton.addActionListener(this);
        f.add(sendButton);
        
        
        f.setLocation(150,45);
        f.setSize(400, 600);
//        setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        
        f.setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent e){
        
       try{
           String out = textArea.getText();
       
            JPanel p2 = formatLabel(out); 

            panel2.setLayout(new BorderLayout());

            // msg will be aligned to right of the panel
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);

            // align all msgs in vertical order one below the other
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            // add all the msgs to the main panel
            panel2.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            textArea.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
       } catch(Exception e1){
            e1.printStackTrace();
       }
        
    }
    
    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 14));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50)); 
        
        panel.add(output);
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        
        return panel;
    }
    
    public static void main(String [] args){
        new Server();
        
        try{
            ServerSocket skt = new ServerSocket(6001);
            while(true){
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                
                while(true){
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    
                    vertical.add(left);
                    f.validate();
                    
                }
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        
    }
    
}
