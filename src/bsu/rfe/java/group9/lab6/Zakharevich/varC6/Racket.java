package bsu.rfe.java.group9.lab6.Zakharevich.varC6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Racket implements Runnable {
    private static final int MAX_SPEED = 20;
    private static final int HEIGHT = 15;
    private static final int WIDTH = 100;
    private Field field;
    private int width,height;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private int speed;
    // Конструктор класса BouncingBall
    public Racket(Field field) {

// чтобы отслеживать выход за его пределы
// через getWidth(), getHeight()
        this.field = field;

        width =new Double(WIDTH).intValue();
        height=new Double(HEIGHT).intValue();
// чем он больше, тем медленнее
        speed = new Double(5).intValue();

// Цвет мяча выбирается случайно
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
        x = field.getSize().getWidth()/2;
        y = field.getSize().getHeight()-HEIGHT;
// Создаѐм новый экземпляр потока, передавая аргументом
// ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
// Запускаем поток
        thisThread.start();
    }
    // Метод run() исполняется внутри потока. Когда он завершает работу,
// то завершается и поток
    public void run() {
        try {
// Крутим бесконечный цикл, т.е. пока нас не прервут,
// мы не намерены завершаться
            while(true) {
                Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex) {
// Если нас прервали, то ничего не делаем
// и просто выходим (завершаемся)
        }
    }
    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Rectangle2D.Double ball = new Rectangle2D.Double(x, y,
                width, height);
        canvas.draw(ball);
        canvas.fill(ball);
    }
    public Double getX(){
        return x;
    }
    public Double getY(){
        return y;
    }
    public Double getWidth(){
        return new Double(width);
    }
    public void moveLeft(){
        if(x>0){
            x=x-MAX_SPEED;
        }
    }
    public void moveRight() {
        if (x< field.getSize().getWidth()-width) {
            x = x + MAX_SPEED;
        }
    }
}