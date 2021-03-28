package bsu.rfe.java.group9.lab6.Zakharevich.varC6;

import java.awt.*;
import java.awt.event.*;
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
    private String ai = "0";
    private String gamer= "0";

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
        canvas.setFont(new Font("Serif", Font.BOLD, 36));
// Последовательно запросить прорисовку от всех мячей из списка\
        canvas.drawString(ai, 10, 36);
        canvas.drawString(gamer, 10, (int)getSize().getHeight()-10);
        if(start) {
            balls.paint(canvas);
            racket.paint(canvas);
            racketBot.paint(canvas);
        }
        if(Integer.parseInt(ai)>=10){
            canvas.setColor(Color.RED);
            canvas.drawString("Победитель компьютер",(int)getSize().getWidth()/2-150,(int)getSize().getHeight()/2);
            pause();
        }
        if(Integer.parseInt(gamer)>=10){
            canvas.setColor(Color.RED);
            canvas.drawString("Победитель игрок",(int)getSize().getWidth()/2-150,(int)getSize().getHeight()/2);
            pause();
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
        int i = Integer.parseInt(gamer);
        i++;
        gamer = Integer.toString(i);
    }
    public void Lose(){
        win = false;
        int i = Integer.parseInt(ai);
        i++;
        ai = Integer.toString(i);
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

