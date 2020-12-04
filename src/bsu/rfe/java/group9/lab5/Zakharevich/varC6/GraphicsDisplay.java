package bsu.rfe.java.group9.lab5.Zakharevich.varC6;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.geom.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Vector;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel implements MouseMotionListener, MouseListener{
    // Список координат точек для построения графика
    private Double[][] graphicsData;
    private Double[][] NewgraphicsData;
    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;
    // Границы диапазона пространства, подлежащего отображению
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    // Используемый масштаб отображения
    private double scale;
    private double mouseYnew;
    private int mouseX ;
    private int mouseY;
    private int mouseX0;
    private int mouseY0;
    private int numfun;
    private boolean Rotate = false;
    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke newgraphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    // Различные шрифты отображения надписей
    private Font axisFont;
    private Font cursorFont;
    private BasicStroke fieldStroke;
    private boolean flagpoint;
    private boolean flagpointfun;
    private boolean flagpressed = false;
    private boolean flagpressedfun=false;
    private Vector<Double> minXzoom;
    private Vector<Double> maxXzoom;
    private Vector<Double> minYzoom;
    private Vector<Double> maxYzoom;
    public GraphicsDisplay() {
// Цвет заднего фона области отображения - белый
        setBackground(Color.WHITE);
// Сконструировать необходимые объекты, используемые в рисовании
// Перо для рисования графика
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[]{4,1,4,1,4,1,1,1,1,1,1,1}, 0.0f);
        newgraphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
// Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        fieldStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[]{4,2}, 0.0f);

// Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Шрифт для подписей осей координат
        axisFont = new Font("Serif", Font.BOLD, 36);
        cursorFont = new Font("Serif",Font.PLAIN,14);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        minXzoom = new Vector(1);
        minYzoom = new Vector(1);
        maxXzoom = new Vector(1);
        maxYzoom = new Vector(1);
    }

// Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
public void showNewGraphics(Double[][] graphicsData) {
    this.NewgraphicsData = graphicsData;
    repaint();
}
    public int getLength () {
        return this.graphicsData.length;
    }
    public  double getX(int i)
    {
        return this.graphicsData[i][0];
    }
    public  double getY(int i)
    {
        return this.graphicsData[i][1];
    }
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {
// Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
// Запросить перерисовку компонента, т.е. неявно вызвать paintComponent()
        repaint();
    }
    // Методы-модификаторы для изменения параметров отображения графика
// Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {
        /* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
         * Эта функциональность - единственное, что осталось в наследство от
         * paintComponent класса JPanel
         */
        super.paintComponent(g);
// Шаг 2 - Если данные графика не загружены (при показе компонентапри запуске программы) - ничего не делать
        if(NewgraphicsData==null||NewgraphicsData.length==0) {
            if (graphicsData == null || graphicsData.length == 0) return;
// Шаг 3 - Определить минимальное и максимальное значения для координат X и Y
// Это необходимо для определения области пространства, подлежащей отображению
// Еѐ верхний левый угол это (minX, maxY) - правый нижний это(maxX, minY)
            minX = graphicsData[0][0];
            maxX = graphicsData[graphicsData.length - 1][0];
            minY = graphicsData[0][1];
            maxY = minY;
// Найти минимальное и максимальное значение функции
            for (int i = 1; i < graphicsData.length; i++) {
                if (graphicsData[i][1] < minY) {
                    minY = graphicsData[i][1];
                }
                if (graphicsData[i][1] > maxY) {
                    maxY = graphicsData[i][1];
                }
            }
/* Шаг 4 - Определить (исходя из размеров окна) масштабы по осям X
и Y - сколько пикселов
* приходится на единицу длины по X и по Y
*/

        }else{

            if(graphicsData[0][0]<NewgraphicsData[0][0]){
                minX = graphicsData[0][0];
            }else{
                minX = NewgraphicsData[0][0];
            }
            if(graphicsData[graphicsData.length - 1][0]<NewgraphicsData[NewgraphicsData.length - 1][0]){
                maxX = NewgraphicsData[NewgraphicsData.length - 1][0];
            }else{
                maxX = graphicsData[graphicsData.length - 1][0];
            }
            minY = graphicsData[0][1];
            maxY = minY;
// Найти минимальное и максимальное значение функции
            for (int i = 1; i < graphicsData.length; i++) {
                if (graphicsData[i][1] < minY) {
                    minY = graphicsData[i][1];
                }
                if (graphicsData[i][1] > maxY) {
                    maxY = graphicsData[i][1];
                }
            }
                for (int i = 1; i < NewgraphicsData.length; i++){
                    if (NewgraphicsData[i][1] < minY) {
                    minY = NewgraphicsData[i][1];
                }
                if (NewgraphicsData[i][1] > maxY) {
                    maxY = NewgraphicsData[i][1];
                }
            }
        }
        if(minXzoom.size()<1) {
            minXzoom.add(0, minX);
            maxXzoom.add(0, maxX);
            minYzoom.add(0, minY);
            maxYzoom.add(0, maxY);
        }

            double scaleX = getSize().getWidth() / (maxXzoom.elementAt(maxXzoom.size() - 1) - minXzoom.elementAt(minXzoom.size() - 1));
            double scaleY = getSize().getHeight() / (maxYzoom.elementAt(maxYzoom.size() - 1) - minYzoom.elementAt(minYzoom.size() - 1));
// Шаг 5 - Чтобы изображение было неискажѐнным - масштаб должен быть одинаков
// Выбираем за основу минимальный
            scale = Math.min(scaleX, scaleY);
// Шаг 6 - корректировка границ отображаемой области согласно выбранному масштабу
            if (scale == scaleX) {
/* Если за основу был взят масштаб по оси X, значит по оси Y
делений меньше,
* т.е. подлежащий визуализации диапазон по Y будет меньше
высоты окна.
* Значит необходимо добавить делений, сделаем это так:
* 1) Вычислим, сколько делений влезет по Y при выбранном
масштабе - getSize().getHeight()/scale
* 2) Вычтем из этого сколько делений требовалось изначально
* 3) Набросим по половине недостающего расстояния на maxY и
minY
*/
                Double yIncrement = (getSize().getHeight() / scale - (maxYzoom.elementAt(maxYzoom.size() - 1) - minYzoom.elementAt(minYzoom.size() - 1))) / 2;
                maxYzoom.set(maxYzoom.size() - 1,yIncrement+maxYzoom.elementAt(maxYzoom.size() - 1));
                minYzoom.set(minYzoom.size() - 1,-yIncrement+minYzoom.elementAt(minYzoom.size() - 1));;
            }
            if (scale == scaleY) {
// Если за основу был взят масштаб по оси Y, действовать по аналогии
                Double xIncrement = (getSize().getWidth() / scale - (maxXzoom.elementAt(maxXzoom.size() - 1) - minXzoom.elementAt(minXzoom.size() - 1))) / 2;
                maxXzoom.set(maxXzoom.size() - 1,xIncrement+maxXzoom.elementAt(maxXzoom.size() - 1));
                minXzoom.set(minXzoom.size() - 1,-xIncrement+minXzoom.elementAt(minXzoom.size() - 1));;
            }
        maxX=maxXzoom.elementAt(maxXzoom.size() - 1);
        minX=minXzoom.elementAt(minXzoom.size() - 1);
        maxY=maxYzoom.elementAt(maxYzoom.size() - 1);
        minY=minYzoom.elementAt(minYzoom.size() - 1);
// Шаг 7 - Сохранить текущие настройки холста
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (Rotate) {
            AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2, getSize().getWidth()/2, getSize().getHeight()/2);
            at.concatenate(new AffineTransform(getSize().getHeight()/getSize().getWidth(), 0.0, 0.0, getSize().getWidth()/getSize().getHeight(),
                    (getSize().getWidth()-getSize().getHeight())/2, -(getSize().getWidth()-getSize().getHeight())/2));
            canvas.setTransform(at);
        }
// Шаг 8 - В нужном порядке вызвать методы отображения элементов графика
// Порядок вызова методов имеет значение, т.к. предыдущий рисунок будет затираться последующим
// Первыми (если нужно) отрисовываются оси координат.
        if (showAxis) paintAxis(canvas);
// Затем отображается сам график
        paintGraphics(canvas);
        if(!(NewgraphicsData==null||NewgraphicsData.length==0)){
            paintNewGraphics(canvas);
        }

// Затем (если нужно) отображаются маркеры точек, по которым строился график.
        if (showMarkers) paintMarkers(canvas);
// Шаг 9 - Восстановить старые настройки холста
        MyMouse(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);

    }
    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {
// Выбрать линию для рисования графика
        canvas.setStroke(graphicsStroke);
// Выбрать цвет линии
        canvas.setColor(Color.RED);
/* Будем рисовать линию графика как путь, состоящий из множества
сегментов (GeneralPath)
* Начало пути устанавливается в первую точку графика, после чего
прямой соединяется со
* следующими точками
*/
        GeneralPath graphics = new GeneralPath();
        for (int i=0; i<graphicsData.length; i++) {
// Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(graphicsData[i][0],
                    graphicsData[i][1]);
            if (i>0) {
// Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY());
            } else {
// Первая итерация цикла - установить начало пути в точку point
                graphics.moveTo(point.getX(), point.getY());
            }
        }

// Отобразить график
        canvas.draw(graphics);
    }

    protected void paintNewGraphics(Graphics2D canvas) {
// Выбрать линию для рисования графика
        canvas.setStroke(newgraphicsStroke);
// Выбрать цвет линии
        canvas.setColor(Color.BLACK);
/* Будем рисовать линию графика как путь, состоящий из множества
сегментов (GeneralPath)
* Начало пути устанавливается в первую точку графика, после чего
прямой соединяется со
* следующими точками
*/
        GeneralPath graphics = new GeneralPath();
        for (int i=0; i<NewgraphicsData.length; i++) {
// Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(NewgraphicsData[i][0],
                    NewgraphicsData[i][1]);
            if (i>0) {
// Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY());
            } else {
// Первая итерация цикла - установить начало пути в точку point
                graphics.moveTo(point.getX(), point.getY());
            }
        }
// Отобразить график
        canvas.draw(graphics);
    }
    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {
// Шаг 1 - Установить специальное перо для черчения контуров маркеров
        canvas.setStroke(markerStroke);
// Шаг 2 - Организовать цикл по всем точкам графика
        for (Double[] point: graphicsData) {
            boolean temp = false;
            String str = Double.toString(point[1]);
            int sum =0;
            for(int i =0;i<str.length();i++) {
                if(str.charAt(i)=='.'){
                    break;
                }
                if(str.charAt(i)=='-'){
                    continue;
                }
                sum += Character.getNumericValue(str.charAt(i));
            }
            if(sum<10){
                temp = true;
            }
            if(!temp) {
                canvas.setColor(Color.RED);
            }else{
                canvas.setColor(Color.BLUE);
            }



// Инициализировать эллипс как объект для представления маркера
            Ellipse2D.Double marker = new Ellipse2D.Double();
/* Эллипс будет задаваться посредством указания координат
его центра
и угла прямоугольника, в который он вписан */
// Центр - в точке (x,y)
            Point2D.Double center = xyToPoint(point[0], point[1]);
            marker.setFrameFromCenter(center, shiftPoint(center, 5, 5));
            canvas.draw(marker);
            canvas.draw(new Line2D.Double(shiftPoint(center, -5, 0), shiftPoint(center, 5, 0)));
            canvas.draw(new Line2D.Double(shiftPoint(center, 0, -5), shiftPoint(center, 0, 5)));

        }
    }
    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {
// Установить особое начертание для осей
        canvas.setStroke(axisStroke);
// Оси рисуются чѐрным цветом
        canvas.setColor(Color.BLACK);
// Стрелки заливаются чѐрным цветом
        canvas.setPaint(Color.BLACK);
// Подписи к координатным осям делаются специальным шрифтом
        canvas.setFont(axisFont);
// Создать объект контекста отображения текста - для получения характеристик устройства (экрана)
        FontRenderContext context = canvas.getFontRenderContext();
// Определить, должна ли быть видна ось Y на графике
        if (minX<=0.0 && maxX>=0.0) {
// Она должна быть видна, если левая граница показываемой области (minX) <= 0.0,
// а правая (maxX) >= 0.0
// Сама ось - это линия между точками (0, maxY) и (0, minY)
                    canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                            xyToPoint(0, minY)));
// Стрелка оси Y
            GeneralPath arrow = new GeneralPath();
// Установить начальную точку ломаной точно на верхний конец оси Y
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// Вести левый "скат" стрелки в точку с относительными координатами (5,20)
            arrow.lineTo(arrow.getCurrentPoint().getX()+5,
                    arrow.getCurrentPoint().getY()+20);
// Вести нижнюю часть стрелки в точку с относительными координатами (-10, 0)
            arrow.lineTo(arrow.getCurrentPoint().getX()-10,
                    arrow.getCurrentPoint().getY());
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку
// Нарисовать подпись к оси Y
// Определить, сколько места понадобится для надписи "y"
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            Rectangle2D bound = axisFont.getStringBounds("0", context);
            Point2D.Double label0 = xyToPoint(0, 0);
            canvas.drawString("0", (float)label0.getX() + 10,
                    (float)(label0.getY() - bound.getY()));
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y", (float)labelPos.getX() + 10,
                    (float)(labelPos.getY() - bounds.getY()));
        }
// Определить, должна ли быть видна ось X на графике
        if (minY<=0.0 && maxY>=0.0) {
// Она должна быть видна, если верхняя граница показываемой области (maxX) >= 0.0,
// а нижняя (minY) <= 0.0
                    canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                            xyToPoint(maxX, 0)));
// Стрелка оси X
            GeneralPath arrow = new GeneralPath();
// Установить начальную точку ломаной точно на правый конец оси X
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
// Вести верхний "скат" стрелки в точку с относительными координатами (-20,-5)
            arrow.lineTo(arrow.getCurrentPoint().getX()-20,
                    arrow.getCurrentPoint().getY()-5);
// Вести левую часть стрелки в точку с относительными координатами (0, 10)
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY()+10);
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку
// Нарисовать подпись к оси X
// Определить, сколько места понадобится для надписи "x"
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("x", (float)(labelPos.getX() -
                    bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
        }
    }
    /* Метод-помощник, осуществляющий преобразование координат.
    * Оно необходимо, т.к. верхнему левому углу холста с координатами
    * (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где
    * minX - это самое "левое" значение X, а
    * maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y) {
// Вычисляем смещение X от самой левой точки (minX)
        double deltaX = x - minX;
// Вычисляем смещение Y от точки верхней точки (maxY)
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }
    /* Метод-помощник, возвращающий экземпляр класса Point2D.Double
     * смещѐнный по отношению к исходному на deltaX, deltaY
     * К сожалению, стандартного метода, выполняющего такую задачу, нет.
     */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
                                        double deltaY) {
// Инициализировать новый экземпляр точки
        Point2D.Double dest = new Point2D.Double();
// Задать еѐ координаты как координаты существующей точки +заданные смещения
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    public void setRotate(boolean rotate) {
        this.Rotate = rotate;
        repaint();
    }
    public boolean isRotate() {
        return Rotate;
    }
    public void mouseClicked(MouseEvent e) {
        if(e.getButton()==3 && minXzoom.size()>1) {
            minXzoom.remove(minXzoom.size()-1);
            maxXzoom.remove(maxXzoom.size()-1);
            minYzoom.remove(minYzoom.size()-1);
            maxYzoom.remove(maxYzoom.size()-1);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {

        if(!flagpoint && e.getButton()==1){
        flagpressed=true;
        flagpressedfun=false;
            mouseX0=e.getX();
            mouseY0=e.getY();
        }
        if(flagpoint && e.getButton()==1){
        flagpressedfun=true;
        flagpressed=false;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(!flagpressedfun&&!flagpoint && e.getButton()==1){
        flagpressed=false;
        if(minXzoom.size()<2) {
            minXzoom.add(mouseX0 / scale + minXzoom.elementAt(0));
            maxXzoom.add(mouseX / scale + minXzoom.elementAt(0));
            minYzoom.add(maxYzoom.elementAt(0) - mouseY / scale);
            maxYzoom.add(maxYzoom.elementAt(0) - mouseY0 / scale);
            }
        else {
            minXzoom.add(mouseX0 / scale + minXzoom.elementAt(minXzoom.size()-1));
            maxXzoom.add(mouseX / scale + minXzoom.elementAt(minXzoom.size()-1));
            minYzoom.add(maxYzoom.elementAt(maxYzoom.size()-1) - mouseY / scale);
            maxYzoom.add(maxYzoom.elementAt(maxYzoom.size()-1) - mouseY0 / scale);
        }
        }
        if( e.getButton()==1){
            flagpressedfun=false;
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        if(flagpoint){
            mouseX0=e.getX();
            mouseY0=e.getY();
        }
        mouseX=e.getX();
        mouseY=e.getY();
        repaint();
    }
    public void mouseMoved(MouseEvent e) {
        if(!flagpoint){
            mouseX0=e.getX();
            mouseY0=e.getY();
        }
        mouseX=e.getX();
        mouseY=e.getY();
        repaint();
    }
    public void MyMouse(Graphics2D canvas) {

        canvas.setPaint(Color.BLACK);
        canvas.setFont(cursorFont);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(3);
        flagpoint=false;
        int i=0;
        for (Double[] point: graphicsData) {
            Point2D p = xyToPoint(point[0], point[1]);
            if ((mouseY0<=(int)p.getY()+5)&&(mouseY0>=(int)p.getY()-5)&&((mouseX0<=(int)p.getX()+5)&&(mouseX0>=(int)p.getX()-5))) {
               flagpoint=true;
            }
            if ((mouseY<=(int)p.getY()+5)&&(mouseY>=(int)p.getY()-5)&&((mouseX<=(int)p.getX()+5)&&(mouseX>=(int)p.getX()-5))) {
                canvas.drawString("X=" + formatter.format(point[0]) + "; Y=" + formatter.format(point[1]), (float) mouseX, (float) mouseY);
                break;
            }
           i++;
        }
        if(!flagpressedfun) numfun=i;
        canvas.setColor(Color.GRAY);
        canvas.setStroke(fieldStroke);
        if(!flagpoint && flagpressed && !flagpressedfun)
            canvas.drawRect(mouseX0,mouseY0,mouseX-mouseX0,mouseY-mouseY0);
        if( flagpressedfun && numfun<graphicsData.length) graphicsData[numfun][1]=(maxYzoom.elementAt(maxYzoom.size()-1)-mouseY/scale);
    }

}
