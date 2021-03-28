package bsu.rfe.java.group9.lab6.Zakharevich.varC6;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
public class  BouncingBall implements Runnable {
    //радиус, который может иметь мяч
    private static final int RADIUS = 10;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 15;
    private static final int START_SPEED = 5;
    private Field field;
    private int radius;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private int speed;
    private double speedX;
    private double speedY;
    // Конструктор класса BouncingBall
    public BouncingBall(Field field) {
// Необходимо иметь ссылку на поле, по которому прыгает мяч,
// чтобы отслеживать выход за его пределы
// через getWidth(), getHeight()
        this.field = field;
// Радиус мяча случайного размера
        radius = new Double(RADIUS).intValue() ;
// Абсолютное значение скорости зависит от диаметра мяча,
// чем он больше, тем медленнее
        speed = new Double(Math.round(5*MAX_SPEED / radius)).intValue();
        if (speed>MAX_SPEED) {
            speed = MAX_SPEED;
        }

// Вычисляются горизонтальная и вертикальная компоненты скорости

        speedY = START_SPEED;
// Цвет мяча выбирается случайно
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
        x = field.getSize().getWidth()/2;
        y = field.getSize().getHeight()/2;
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
// Синхронизация потоков на самом объекте поля
// Если движение разрешено - управление будет
// возвращено в метод
// В противном случае - активный поток заснѐт
                Thread.sleep(16-speed);
                field.canMove(this);
                if (x + speedX <= radius) {
// Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
// Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
                    field.Win();
                    field.reset();
                } else
                if (y + speedY <= radius+field.getHeightRocket()&& x>= field.getXRocketBot()&&x<= field.getWidthRocket()+ field.getXRocketBot() ) {

                    speedY = -speedY;
                    speedX = 7*Math.random()-7*Math.random();
                    y = radius + field.getHeightRocket();
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    field.Lose();
                    field.reset();
                }
                if (y + speedY >= field.getYRocket() - radius && x>= field.getXRocket()&&x<= field.getWidthRocket()+ field.getXRocket()) {
                    speedY = -speedY;
                    speedX = 7*Math.random()-7*Math.random();
                    y=new Double(field.getYRocket()-radius).intValue();
                } else {
// Просто смещаемся
                    x += speedX;

                    y += speedY;
                }

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
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
    public Double getX(){
        return x;
    }
    public void reset(){
        if (field.getWin())
        speedY = -START_SPEED;
        else
            speedY=START_SPEED;
        x = field.getSize().getWidth()/2;
        y = field.getSize().getHeight()/2;
        speedX=0;
    }
}

