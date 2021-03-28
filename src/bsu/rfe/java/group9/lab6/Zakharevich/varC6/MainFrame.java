package bsu.rfe.java.group9.lab6.Zakharevich.varC6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
    // Константы, задающие размер окна приложения, если оно
// не распахнуто на весь экран
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;
    private JMenuItem startMenuItem;
    // Поле, по которому прыгают мячи
    private Field field = new Field();

    // Конструктор главного окна приложения
    public MainFrame() {
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
// Установить начальное состояние окна развѐрнутым на весь экран

        setExtendedState(MAXIMIZED_BOTH);

// Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu ballMenu = new JMenu("Игра");
        Action addBallAction = new AbstractAction("Начать") {
            public void actionPerformed(ActionEvent event) {
                field.Start();
                field.addBall();
                field.addRacket();
                field.addRacketBot();
                ballMenu.setEnabled(false);
                if (!pauseMenuItem.isEnabled() &&
                        !resumeMenuItem.isEnabled()) {
// Ни один из пунктов меню не являются
// доступными - сделать доступным "Паузу"
                    pauseMenuItem.setEnabled(true);
                }
            }
        };
        ballMenu.setEnabled(true);
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);
        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);
// Добавить в центр граничной компоновки поле Field
        getContentPane().add(field, BorderLayout.CENTER);
        addKeyListener(new KeyListener()
        {


            @Override
            public void keyTyped(KeyEvent e){}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_LEFT)
                    field.moveRocketLeft();
                if(e.getKeyCode()==KeyEvent.VK_RIGHT)
                    field.moveRocketRight();
            }

            @Override
            public void keyReleased(KeyEvent e){}

        });
    }
    // Главный метод приложения
    public static void main(String[] args) {
// Создать и сделать видимым главное окно приложения
        MainFrame frame = new MainFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}