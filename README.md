![제목 없음](https://user-images.githubusercontent.com/57351834/72527866-0dc91100-38ad-11ea-938f-b952baec1db7.png)




package chat;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
public class ChatServer implements Runnable{
       ServerSocket ss;
       Service service;
       ArrayList<Service> list = new ArrayList();
       
       public ChatServer() {
              try {
                     ss = new ServerSocket(7777);
                     System.out.println("채팅 서버 서비스 중...");
                     new Thread(this).start();
              } catch (Exception e) {
                     System.out.println("ChatServer err : " + e);
              }
       }
       
       @Override
       public void run() {  //ChatServer의 run
              while(true) {
                     try {
                           Socket socket = ss.accept();
                           service = new Service(socket);
                           service.start();
                           service.chat_name = service.in.readLine();  //접속자의 이름 읽기
                           //System.out.println(service.chat_name);
                           
                           service.messageSend("/c" + service.chat_name);
                           for(Service ser : list) {
                                  ser.messageSend("/c" + service.chat_name);
                                  service.messageSend("/c" + service.chat_name);
                           }
                           
                           list.add(service);
                     } catch (Exception e) {
                           System.out.println("ChatServer run err :" + e);
                     }
              }
       }
       
       //client 별 처리를 위한 내부 클래스
       class Service extends Thread {
              String chat_name;
              BufferedReader in;
              OutputStream out;
              Socket socket;
              public Service(Socket socket) {
                     try {
                           this.socket = socket;
                           
                           in = new BufferedReader(
                                         new InputStreamReader(socket.getInputStream(), "euc-kr"));
                           out = socket.getOutputStream();
                     } catch (Exception e) {
                           System.out.println("Service err : " + e);
                     }
              }
              
              @Override
              public void run() {
                     while(true) {
                           try {
                                  String msg = in.readLine();
                                  JLabel label = new JLabel();
                                  label.setText(msg);
                                  label.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
                                  if(msg == null || msg.equals("")) continue;
                                  
                                  if(msg.charAt(0) == '/') {
                                         if(msg.charAt(1) == 'r') { //대화명 변경
                                                messageAll("/r" + chat_name + "-" + msg.substring(2));
                                                chat_name = msg.substring(2);
                                         }else if(msg.charAt(1) == 'q') { //퇴장
                                                try {
                                                       messageAll("/q" + chat_name);
                                                       list.remove(this);
                                                       in.close();
                                                       out.close();
                                                       socket.close();
                                                } catch (Exception e) {
                                                       // TODO: handle exception
                                                }
                                                break;
                                         }else if(msg.charAt(1) == 's') { //귓속말   /s이름-메세지
                                                String name = msg.substring(2, msg.indexOf('-')).trim();
                                                for(Service ser : list) {
                                                       if(name.equals(ser.chat_name)) {
                                                              ser.messageSend((chat_name + ">(secret)" +
                                                                            msg.substring(msg.indexOf('-') + 1)));
                                                       }
                                                }
                                         }
                                  }else {  //일반 메세지
                                         messageAll(chat_name + ">" + label);
                                  }
                           } catch (Exception e) {
                                  break;
                           }
                     }
              }
              
              public void messageAll(String msg) {
                     try {
                           for (int i = 0; i < list.size(); i++) {
                                  Service ser = list.get(i);
                                  ser.messageSend(msg);
                           }
                     } catch (Exception e) {
                           System.out.println("messageAll err : " + e);
                     }
              }
              
              public void messageSend(String msg) {
                     try {
                           out.write((msg + "\n").getBytes("euc-kr"));
                     } catch (Exception e) {
                           System.out.println("messageSend err : " + e);
                     }
              }
       }
       
       public static void main(String[] args) {
              new ChatServer();
       }
}


package chat;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.io.*;
import java.net.*;
public class ChatClient extends JFrame implements ActionListener, Runnable {
       private JLabel jLabel1 = new JLabel();
       private JLabel jLabel4 = new JLabel();
       private JTextField txtname1 = new JTextField();
       private JTextField txtname2 = new JTextField();
       private JButton btnconn = new JButton();
       private JTextArea txtarea = new JTextArea();
       private JScrollPane jScrollPane1 = new JScrollPane();
       private JTextField txtsend = new JTextField();
       private JButton btnok = new JButton();
       private JLabel jLabel2 = new JLabel();
       private JLabel jLabel3 = new JLabel();
       private JLabel lblinwon = new JLabel();
       private JRadioButton radio1 = new JRadioButton();
       private JRadioButton radio2 = new JRadioButton();
       private JButton btnclose = new JButton();
       private List list = new List();
       private JButton btnchange = new JButton();
       private BufferedReader in;
       private OutputStream out;
       private Socket soc;
       int count = 0; // 접속 인원수
       public ChatClient() {
              try {
                     jbInit();
                     addListener();
              } catch (Exception e) {
                     e.printStackTrace();
              }
       }
       private void jbInit() throws Exception {
              this.getContentPane().setLayout(null);
              this.setSize(new Dimension(1280, 800));
              this.setTitle("캐치마인드");
              this.setBackground(new Color(198, 214, 255));
              jLabel1.setText("닉네임:");
              jLabel1.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
              jLabel1.setBounds(new Rectangle(15, 100, 140, 40));
              txtname1.setBounds(new Rectangle(150, 100, 140, 40));
              jLabel4.setText("스코어:");
              jLabel4.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
              jLabel4.setBounds(new Rectangle(15, 140, 140, 40));
              txtname2.setBounds(new Rectangle(150, 140, 140, 40));
              btnconn.setText("접속");
              btnconn.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,30));
              btnconn.setBounds(new Rectangle(15, 180, 130, 40));
              btnclose.setText("나가기");
              btnclose.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,30));
              btnclose.setBounds(new Rectangle(150, 180, 140, 40));
              lblinwon.setText("0");
              lblinwon.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,30));
              lblinwon.setBounds(new Rectangle(203, 230, 88, 42));
              lblinwon.setBackground(new Color(198, 198, 200));
              lblinwon.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
              lblinwon.setHorizontalAlignment(SwingConstants.CENTER);
              lblinwon.setHorizontalTextPosition(SwingConstants.CENTER);
              jLabel3.setText("참여 인원:");
              jLabel3.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
              jLabel3.setBounds(new Rectangle(15, 230, 210, 40));
              jLabel2.setText("참여자 명단");
              jLabel2.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
              jLabel2.setBounds(new Rectangle(15, 280, 250, 40));
              list.setBounds(new Rectangle(15, 330, 280, 230)); //참여 인원 리스트 위치
              jScrollPane1.setBounds(new Rectangle(350, 570, 910, 155)); // 채팅창 위치
              txtsend.setBounds(new Rectangle(350, 730, 850, 25)); //채팅 입력 위치
              txtsend.setFont(new Font("나눔고딕 ExtraBold",Font.BOLD,40));
              btnok.setText("확인");
              btnok.setBounds(new Rectangle(1200, 730, 60, 25));
              radio1.setText("귓속말");
              radio1.setBounds(new Rectangle(345, 10, 70, 25));
              radio2.setText("귓속말해제");
              radio2.setBounds(new Rectangle(420, 10, 90, 25));
              btnchange.setText("대화명 변경");
              btnchange.setBounds(new Rectangle(230, 10, 110, 25));
              ButtonGroup group = new ButtonGroup();
              group.add(radio1);
              group.add(radio2);
              this.getContentPane().add(btnchange, null);
              this.getContentPane().add(list, null);
              this.getContentPane().add(btnclose, null);
              this.getContentPane().add(radio1, null);
              this.getContentPane().add(radio2, null);
              this.getContentPane().add(lblinwon, null);
              this.getContentPane().add(jLabel3, null);
              this.getContentPane().add(jLabel2, null);
              this.getContentPane().add(btnok, null);
              this.getContentPane().add(txtsend, null);
              jScrollPane1.getViewport().add(txtarea, null);
              this.getContentPane().add(jScrollPane1, null);
              this.getContentPane().add(btnconn, null);
              this.getContentPane().add(txtname2, null);
              this.getContentPane().add(txtname1, null);
              this.getContentPane().add(jLabel4, null);
              this.getContentPane().add(jLabel1, null);
       }
       public void addListener() {
              txtname1.addActionListener(this);
              txtname2.addActionListener(this);
              txtsend.addActionListener(this);
              btnok.addActionListener(this);
              btnconn.addActionListener(this);
              btnclose.addActionListener(this);
              btnchange.addActionListener(this);
       }
       public void actionPerformed(ActionEvent e) {
              if(e.getSource() == txtname1 || e.getSource() == btnconn) {
                     //대화명 입력 후 접속
                     if(txtname1.getText().equals("")) {
                           JOptionPane.showMessageDialog(this, "대화명 입력");
                           txtname1.requestFocus();
                           return;
                     }
                     
                     try {
                           soc = new Socket("192.168.0.15", 7777);
                           in = new BufferedReader(
                                         new InputStreamReader(soc.getInputStream(), "euc-kr"));
                           out = soc.getOutputStream();
                           out.write((txtname1.getText() + "\n").getBytes("euc-kr"));
                           new Thread(this).start();  //run()을 호출
                     } catch (Exception e2) {
                           System.out.println("접속 오류:" + e2);
                     }
              }else if(e.getSource() == txtsend || e.getSource() == btnok) {
                     //메세지 전송
                     try {
                           if(radio1.isSelected()) {  //귓속말 메세지
                                  String name = list.getSelectedItem();
                                  out.write(("/s" + name + "-" +
                                                       txtsend.getText() + "\n").getBytes("euc-kr"));
                                  txtarea.append(name + "님에게 귓속말이 전달되었습니다.\n");
                           }else {  //일반 메세지
                                  out.write((txtsend.getText() + "\n").getBytes("euc-kr"));
                           }
                           
                           txtsend.setText("");
                           txtsend.requestFocus();
                     } catch (Exception e2) {
                           System.out.println("메세지 전송 오류:" + e2);
                     }
              }else if(e.getSource() == btnchange) {
                     //대화명 변경
                     if(btnchange.getText().equals("대화명 변경")) {
                           btnchange.setText("변경확인");
                           txtname1.setEditable(true);
                           txtname1.requestFocus();
                     }else {
                           btnchange.setText("대화명 변경");
                           txtname1.setEditable(false);
                           try {
                                  out.write(("/r" + txtname1.getText() + "\n").getBytes("euc-kr"));
                           } catch (Exception e2) {
                                  System.out.println("대화명 변경 오류:" + e2);
                           }
                     }
              }else if(e.getSource() == btnclose) {
                     //나가기
                     try {
                           out.write(("/q\n").getBytes());
                           in.close();
                           out.close();
                           soc.close();
                     } catch (Exception e2) {
                           System.out.println("나가기 오류:" + e2);
                     } finally {
                           System.exit(0);
                     }
              }
       }
       
       @Override
       public void run() {
              while (true) {
                     try {
                           String msg = in.readLine();  //서버로부터 메세지 수신
                           
                           if(msg == null || msg.equals("")) return;
                           
                           if(msg.charAt(0) == '/') {
                                  if(msg.charAt(1) == 'c') { //대화명(입장)
                                         //  /c홍길동
                                         list.add(msg.substring(2), count);
                                         count++;
                                         lblinwon.setText(String.valueOf(count));
                                         txtarea.append("**" + msg.substring(2) + "님이 입장했습니다.\n");
                                         txtname1.setEditable(false);  //대화명 입력 불가
                                         btnconn.setEnabled(false);
                                  }else if(msg.charAt(1) == 'q') { //퇴장
                                         txtarea.append("^^" + msg.substring(2) + "님이 퇴장했습니다.\n");
                                         String cname = msg.substring(2);
                                         
                                         for (int i = 0; i < count; i++) {
                                                if(cname.equals(list.getItem(i))) {
                                                       list.remove(i);
                                                       count--;
                                                       lblinwon.setText(String.valueOf(count));
                                                       break;
                                                }
                                         }
                                  }else if(msg.charAt(1) == 'r') { //대화명 변경
                                         //   /roldName-newName
                                         String oldName = msg.substring(2, msg.indexOf('-'));
                                         String newName = msg.substring(msg.indexOf('-') + 1);
                                         txtarea.append("*" + oldName + "님의 대화명이 " +
                                                       newName + "으로 변경됐습니다\n");
                                         
                                         for (int i = 0; i < count; i++) {
                                                if(oldName.equals(list.getItem(i))) {
                                                       list.replaceItem(newName, i);
                                                       break;
                                                }
                                         }
                                  }
                           }else { //일반 메세지
                                  txtarea.append(msg + "\n");
                           }
                     } catch (Exception e) {
                           System.out.println("run err : " + e);
                     }                    
              }
       }
       
       public static void main(String args[]) {
              ChatClient fr = new ChatClient();
              fr.getPreferredSize();
              fr.setLocation(200, 200);
              fr.setVisible(true);
              fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       }
}
