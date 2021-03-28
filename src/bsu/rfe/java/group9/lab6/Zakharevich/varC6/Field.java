package bsu.rfe.java.group9.lab6.Zakharevich.varC6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Field extends JPanel {
    // Флаг приостановленности движения
    private boolean paused;
    private boolean start=false;
    // Динамический список скачущих мячей
    private BouncingBall balls;
    private Racket racket;
    private Racket racketBot;
    private boolean win;

    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
// При создании его экземпляра используется анонимный класс,
// реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
// Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });

    // Конструктор класса BouncingBall
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        repaintTimer.start();

    }



    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка\
        if(start) {
            balls.paint(canvas);
            racket.paint(canvas);
            racketBot.paint(canvas);
        }
    }
    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        balls= new BouncingBall(this);
    }
    public void Start(){
        start = true;
    }
    public void addRacket() {
        racket=new Racket(this);
    }
    public void addRacketBot(){
        racketBot=new Racket(this);
        racketBot.setBot();
    }
    public Double getXRocket(){
        return racket.getX();
    }
    public Double getYRocket(){
        return racket.getY();
    }
    public Double getWidthRocket(){
        return racket.getWidth();
    }
    public Double getXRocketBot(){
        return racketBot.getX();
    }
    public Double getHeightRocket(){
        return racket.getHeight();
    }
    public Double getXBall(){
        return balls.getX();
    }

    public void moveRocketLeft(){
        if(!paused)
        racket.moveLeft();
    }
    public void moveRocketRight(){
        if(!paused)
        racket.moveRight();
    }
    public void reset(){
        balls.reset();
        racket.reset();
        racketBot.reset();
    }
    public boolean getWin(){
        return win;
    }
    public void Win(){
        win = true;
    }
    public void Lose(){
        win = false;
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
// Будим все ожидающие продолжения потоки
        notifyAll();
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
            wait();
        }
    }
    public synchronized void canMove(Racket racket) throws
            InterruptedException {
        if (paused) {
            wait();
        }
    }
}

