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
import java.io.IOException;
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



    public static void main(String[] args) throws IOException {
        try{
            while(true){
                MainFrame frame=new MainFrame(true);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(true);

                frame.setVisible(false);
                Point location = frame.getLocation();
                Dimension size = frame.getSize();

                frame = new MainFrame(false);
                frame.setLocation(location);
                frame.setSize(size);
                frame.setVisible(true);

                while(!fail){
                    Thread.sleep(100);
                }
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

