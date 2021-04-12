package bsu.rfe.java.group9.lab7.Zakharevich.varC6;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



@SuppressWarnings("serial")
public class MainFrame extends JFrame{
    private static String name=null;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private HashMap<String, JTextArea> map = new HashMap<String, JTextArea>();
    private HashMap<String, String> map2 = new HashMap<String, String>();
    private JTextArea forall;
    private static String msgroup="225.4.5.6";
    private static Integer msport=6788;
    private static Integer srvPort;
    private static Integer id;
    private static boolean fail=false;
    private int sMAX_LIST_W=200;
    private int sTAM_H=70;
    private JButton btnsall;
    private static MainFrame THIS;


    MainFrame(boolean s) throws IOException{
        super("Клиент мгновенных сообщений");
        THIS=this;
        final JFrame frame=this;
        setMinimumSize(new Dimension(570,390));
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2
                , (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
        if(s){
            final JLabel label=new JLabel("Введите имя:");
            final JTextField textName = new JTextField("",10);
            textName.setMaximumSize(textName.getPreferredSize());
            textName.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    if(!nickIsCorrect(textName.getText())){
                        label.setText("Подтвердите");
                    }else{
                        name=textName.getText();
                    }
                }
            });
            final JButton button = new JButton("Подтвердить имя");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    name=textName.getText();
                }
            });
            Box box0 = Box.createHorizontalBox();
            box0.add(label,BorderLayout.CENTER);
            Box box1 = Box.createHorizontalBox();
            box1.add(textName,BorderLayout.CENTER);
            Box box2 = Box.createHorizontalBox();
            box2.add(button,BorderLayout.CENTER);
            Box box = Box.createVerticalBox();
            box.add(Box.createVerticalGlue());
            box.add(box0);
            box.add(Box.createVerticalStrut(10));
            box.add(box1);
            box.add(Box.createVerticalStrut(20));
            box.add(box2);
            box.add(Box.createVerticalGlue());
            getContentPane().add(box,BorderLayout.CENTER);
            return;
        }
        setLayout(null);
        setTitle("Ваше имя: "+name);
        id=(int) (Math.random()*1000000000);
        listModel=new DefaultListModel<String>();
        listModel.addElement("Общий");
        list=new JList<String>(listModel);
        list.setLayoutOrientation(JList.VERTICAL);
        final JScrollPane scrollPaneList=new JScrollPane(list);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                if(list.getSelectedIndex()==0){
                    tabbedPane.setSelectedIndex(0);
                    return;
                }
                if(arg0.getValueIsAdjusting())
                    return;
                int i=0;
                int si=list.getSelectedIndex();
                if(si<0)
                    return;
                String s=listModel.get(si);
                String[] _a=s.split("@");
                s=_a[0];
                while(i<tabbedPane.getTabCount()){
                    if(tabbedPane.getTitleAt(i).equals(s)){
                        tabbedPane.setSelectedIndex(i);
                        return;
                    }
                    i++;
                }
                String[] a = listModel.get(list.getSelectedIndex()).toString().split("@");
                newTab(a[0],a[1]);
            }
        });
        tabbedPane.setLocation(210,3);
        tabbedPane.setSize(350,360);
        scrollPaneList.setLocation(3,3);
        scrollPaneList.setSize(200,360);
        this.add(scrollPaneList);
        this.add(tabbedPane);
        this.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener() {
            public void ancestorResized(HierarchyEvent arg0) {
                scrollPaneList.setSize(sMAX_LIST_W,frame.getHeight()-30);
                tabbedPane.setSize(frame.getWidth()-220,frame.getHeight()-30);
            }
            public void ancestorMoved(HierarchyEvent arg0) {}
        });
        this.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent arg0) {}
            public void windowIconified(WindowEvent arg0) {}
            public void windowDeiconified(WindowEvent arg0) {}
            public void windowDeactivated(WindowEvent arg0) {}
            public void windowClosing(WindowEvent arg0) {
                offline();
                System.exit(0);
            }
            public void windowClosed(WindowEvent arg0) {}
            public void windowActivated(WindowEvent arg0) {}
        });
        MSrecive();
        reciveMSG();
        newTab("Общий",null);
    }

    private void newTab(final String caption, String address){
        String ip = null;
        int port = -1;
        if(address!=null){
            String[] _a=address.split(":");
            ip=_a[0];
            port=Integer.parseInt(_a[1]);
        }

        JPanel panel = new JPanel();
        final JTextArea textAreaHist = new JTextArea(1, 15);
        textAreaHist.setEditable(false);
        textAreaHist.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent arg0) {


            }

            @Override
            public void focusGained(FocusEvent arg0) {


            }
        });
        final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaHist);
        map.put(caption, textAreaHist);
        final JTextArea textAreaMsg = new JTextArea(1, 15);
        final JScrollPane scrollPaneMsg = new JScrollPane(textAreaMsg);
        final String ip2=ip;
        final int port2=port;

        final JButton btn=new JButton("Отправить");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendMessage(caption, ip2, port2, textAreaMsg, textAreaHist);
            }
        });
        if(address==null){
            btnsall=btn;
            btn.setEnabled(false);

        }
        String hist=map2.get(caption);
        if(hist!=null)
            textAreaHist.append(hist);
        tabbedPane.addTab(caption, panel);
        if(address==null)
            forall=textAreaHist;
        panel.setLayout(null);
        panel.add(btn);
        btn.setSize(120,20);
        btn.setLocation(tabbedPane.getWidth()-150,tabbedPane.getHeight()-55);
        scrollPaneOutgoing.setLocation(10,10);
        scrollPaneOutgoing.setSize(tabbedPane.getWidth()-20,tabbedPane.getHeight()-150);
        scrollPaneMsg.setLocation(40,tabbedPane.getHeight()-130);
        scrollPaneMsg.setSize(tabbedPane.getWidth()-50,sTAM_H);
        panel.add(scrollPaneOutgoing);
        panel.add(scrollPaneMsg);
        panel.addHierarchyBoundsListener(new HierarchyBoundsListener() {
            public void ancestorResized(HierarchyEvent e) {
                btn.setLocation(tabbedPane.getWidth()-150,tabbedPane.getHeight()-55);
                scrollPaneOutgoing.setSize(tabbedPane.getWidth()-20,tabbedPane.getHeight()-150);
                scrollPaneMsg.setSize(tabbedPane.getWidth()-50,sTAM_H);
                scrollPaneMsg.setLocation(40,tabbedPane.getHeight()-130);
            }
            public void ancestorMoved(HierarchyEvent e) {}
        });

    }

    private void sendMessage(String nick, String address, int port, JTextArea textAreaMsg, JTextArea textAreaHist) {
        String message = textAreaMsg.getText();
        textAreaMsg.setText("");
        if(message.length()<1 || message.length()>999){
            JOptionPane.showMessageDialog(MainFrame.this, "Сообщение пустое или большое",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(address==null){
            int i=0;
            String ipAddress;
            while(i<listModel.getSize()-1){
                i++;
                ipAddress=(listModel.get(i).split("@"))[1].split(":")[0];
                port=Integer.parseInt(((listModel.get(i).split("@"))[1].split(":")[1]));
                sendData(ipAddress, port, "all", message);
            }
            writeText(forall, name,message);
            return;
        }
        sendData(address, port, "private",message);
        writeText(textAreaHist, name,message);
    }

    private void writeText(JTextArea textarea,String name, String message){
        textarea.append(name+": "+message+"\n");
        textarea.moveCaretPosition(textarea.getText().length());
    }


    private void reciveMSG(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Integer port=findFreePort();
                    srvPort=port.intValue();
                    final ServerSocket serverSocket = new ServerSocket(port);
                    while (!Thread.interrupted() && !fail) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        final String priv = in.readUTF();
                        final String senderPort = in.readUTF();
                        final String message = in.readUTF();
                        final String address =((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
                        socket.close();
                        if(priv.equals("offline")){
                            removeUser(message);
                            continue;
                        }
                        if(priv.equals("fail")){
                            fail=true;
                            continue;
                        }
                        if(priv.equals("hi")){
                            addUser(message, address, senderPort);
                            continue;
                        }
                        procMSG(priv, message, address, senderPort);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this, "Неудалось отправить сообщение", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private void procMSG(String priv, String msg, String addr, String port){
        String nick=listModel.get(findInList(addr+":"+port, 1)).split("@")[0];
        if("all".equals(priv)){
            tabbedPane.setSelectedIndex(0);
            writeText(forall, nick,msg);
            return;
        }
        JTextArea t = map.get(nick);
        if(t!=null){
            tabbedPane.setSelectedIndex(findInTabbed(nick));
        }else{
            String s=findUser2(nick);
            if(s!=null){
                String[] a=s.split("@");
                newTab(a[0], a[1]);
            }
            t = map.get(nick);
        }
        writeText(t,nick,msg);
    }

    public Integer findFreePort() throws IOException {
        ServerSocket server =new ServerSocket(0);
        int port = server.getLocalPort();
        server.close();
        return port;
    }

    private static void MSonline(){
        try{
            MulticastSocket s = new MulticastSocket();
            byte buf[] = ("online@"+srvPort.toString()+"@"+name+"@"+id.toString()).getBytes();
            DatagramPacket pack = new DatagramPacket(buf, buf.length, InetAddress.getByName(msgroup), msport);
            s.send(pack);
            s.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(THIS, "Ошибка", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int findInTabbed(String s){
        int i=1;
        while(i<tabbedPane.getTabCount()){
            if(tabbedPane.getTitleAt(i).equals(s)){
                tabbedPane.setSelectedIndex(i);
                return i;
            }
            i++;
        }
        return 0;
    }

    private int findInList(String str, int o){
        int i=1;
        while(i<listModel.getSize()){
            String _s=listModel.getElementAt(i);
            if(str.equals((_s.split("@"))[o])){
                return i;
            }
            i++;
        }
        return 0;
    }

    private String getNamebyaddr(String ipport){
        int i=1;
        while(i<listModel.getSize()){
            String _s=listModel.getElementAt(i);
            if(ipport.equals((_s.split("@"))[1])){
                return _s.split("@")[0];
            }
            i++;
        }
        return null;
    }

    private String findUser2(String str){
        int i=1;
        while(i<listModel.getSize()){
            String _s=listModel.getElementAt(i);
            if(str.equals((_s.split("@"))[0])){
                return _s;
            }
            i++;
        }
        return null;
    }

    private boolean isMe(String nick, String idd, String port){
        if(nick.equals(name) && idd.equals(id.toString()) && port.equals(srvPort.toString()))
            return true;
        return false;
    }

    private void addUser(String nick, String ip, String port){
        String item = nick+"@"+ip+":"+port;
        listModel.addElement(item);
        btnsall.setEnabled(true);
    }

    private void removeUser(String nick){
        closeTab(nick);
        listModel.remove(findInList(nick,0));
        map2.remove(nick);
        if(listModel.size()<2)
            btnsall.setEnabled(false);
    }

    public void closeTab(String title){
        int i=findInTabbed(title);
        if(i>0)
            tabbedPane.remove(i);
        if(map.get(title)!=null)
            map2.put(title,map.get(title).getText());
        map.remove(title);
        tabbedPane.setSelectedIndex(0);
        list.setSelectedIndex(0);
    }

    private void MSrecive(){
        new Thread(new Runnable() {
            public void run() {
                try{
                    MulticastSocket s = new MulticastSocket(msport);
                    s.joinGroup(InetAddress.getByName(msgroup));
                    while(!Thread.interrupted()){
                        byte buf[] = new byte[1024];
                        DatagramPacket pack = new DatagramPacket(buf, buf.length);
                        s.receive(pack);
                        String rdata=new String(pack.getData()).split("\0")[0];
                        String[] _a=rdata.split("@");
                        String idd=_a[3];
                        String nick=_a[2];
                        String port=_a[1];
                        String msg=_a[0];
                        String ip=pack.getAddress().getHostAddress().toString();
                        if(isMe(nick,idd,port)){
                            continue;
                        }
                        if(name.equals(nick)){
                            sendData(ip, new Integer(port),"fail","name already use");
                            continue;
                        }
                        if("online".equals(msg)){
                            if(findInList(nick,0)==0){

                                addUser(nick, ip, port);
                                sendData(ip,new Integer(port),"hi",name);
                            }else{
                                sendData(ip, new Integer(port),"fail","name already use");
                            }
                        }
                        Thread.sleep(100);
                    }
                    s.leaveGroup(InetAddress.getByName(msgroup));
                    s.close();
                } catch (IOException | InterruptedException e) {
                    JOptionPane.showMessageDialog(THIS, "Ошибка", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private void sendData(String address, Integer port, String type, String data){
        try {
            Socket socket = new Socket(address,port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(type);
            out.writeUTF(srvPort.toString());
            out.writeUTF(data);
            socket.close();
        } catch (IOException e ) {
            removeUser(getNamebyaddr(address+":"+port));
        }
    }

    private static boolean nickIsCorrect(String name){

        if(name!=null && name.length()!=0 && name.length()<10){
            for(int i=0;i<name.length();i++)
                if(name.charAt(i)=='@' || name.charAt(i)==' ')
                    return false;
            return true;
        }else
            return false;
    }

    private void offline(){
        int i=1;
        while(i<listModel.getSize()){
            String _s=listModel.getElementAt(i);
            String s=_s.split("@")[1];
            String[] s_=s.split(":");
            sendData(s_[0],new Integer(s_[1]),"offline",name);
            i++;
        }
    }

    public static void main(String[] args) throws IOException {
        try{
            while(true){
                MainFrame frame=new MainFrame(true);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(true);
                while(!nickIsCorrect(name)){
                    Thread.sleep(100);
                }
                frame.setVisible(false);
                Point location = frame.getLocation();
                Dimension size = frame.getSize();

                frame = new MainFrame(false);
                frame.setLocation(location);
                frame.setSize(size);
                frame.setVisible(true);
                MSonline();
                while(!fail){
                    Thread.sleep(100);
                }
                JOptionPane.showMessageDialog(frame, "Имя "+name+" занято", "Ошибка", JOptionPane.ERROR_MESSAGE);
                frame.setVisible(false);
                frame=null;
                name=null;
                fail=false;
            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}

