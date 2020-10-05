package bsu.rfe.java.group9.lab1.Zakharevich.varC6;

import java.lang.reflect.Constructor;
import java.util.*;

public class main
{

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception
    {
        Food[] breakfast = new Food[20];
        int i = 0;
        boolean var1, var2;         // случаи для спец параметров, начинающихся с дефиса
        var1 = var2 = false;




        for (String arg : args)         // проходим по передаваемым параметрам
        {
            String[] parts = arg.split(("/")); //раздлеяем параметры ком строки
            // split - превращает строку в массив, разбив ее по разделителю
            try {
                Class myClass = Class.forName("bsu.rfe.java.group9.lab1.Zakharevich.varC6." + parts[0]);
                if (parts.length == 1) {                               //если передается 1 параметр (имя класса)
                    Constructor constructor = myClass.getConstructor();
                    breakfast[i] = (Food) constructor.newInstance();
                    i++;
                } else if (parts.length == 2) {                      //если передается 2 парметра(имя класса + поле)
                    Constructor constructor = myClass.getConstructor(String.class);
                    breakfast[i] = (Food) constructor.newInstance(parts[1]);
                    i++;
                } else if (parts.length == 3) {                           //если передается 3 параметра(имя класса + 2 поля)
                    Constructor constructor = myClass.getConstructor(String.class, String.class);
                    breakfast[i] = (Food) constructor.newInstance(parts[1], parts[2]);
                    i++;
                }
            } catch (ClassNotFoundException e)     // исключение, если введенный класс не найден
            {
                switch (parts[0])
                {
                    case "-sort":            // если параметр -sort, значит нужно будет отсортировать продукты завтрака
                        var1 = true;
                        break;
                    case "-calories":       // если параметр -calories, значит нужно будет посчитать калрийность завтрака
                        var2 = true;
                        break;
                    default:
                        System.out.println("Класс " + parts[0] + " не найден.");
                        break;
                }

            }
            catch (NoSuchMethodException e) // исключение, если метод класса не найден (к примеру конструктор)
            {
                System.out.println("Метод класса " + parts[0] + " не был найден.");
            }
        }

        System.out.println("Завтрак: "); //выводим завтрак таким,каким он был первоначально
        for (Food item : breakfast) {
            if (item != null)
            {
                if (item.calculateCalories()==0.0)  // если ввели продукт, который не предусмотрен в программе
                {
                    System.out.print("Такой продукт не предусмотрен (" + item.name);
                    if(item.par1!=null)
                        System.out.print(", " + item.par1);
                    if(item.par2!=null)
                        System.out.print(", " + item.par2);
                    System.out.println(")");
                    continue;
                }
                item.consume();
                System.out.println(" " + item.calculateCalories());
            } else {
                break;
            }
        }

        if (var1)
        {                //случай "ClassNotFoundException", когда мы задаем параметр -sort
            Arrays.sort(breakfast, new Comparator() {
                public int compare(Object o1, Object o2)
                {
                    if (o1 == null || ((Food)o1).par > ((Food)o2).par)
                        return 1;
                    if (o2 == null || ((Food)o1).par < ((Food)o2).par)
                        return -1;
                    else return 0;
                }
            });

            System.out.println("Завтрак (отсортированный вариант):");
            for (Food item : breakfast)
            {
                if (item != null)
                {
                    if (item.calculateCalories()==0.0)
                        continue;
                    item.consume();
                    System.out.println(" " + item.calculateCalories());
                }
                else
                    break;
            }
        }
        if (var2)
        {                            //случай "ClassNotFoundException", когда мы задаем парметр -calories
            double CaloriesCounter = 0.0;
            for (Food item : breakfast)
            {
                if (item != null)
                    CaloriesCounter += item.calculateCalories();
                else
                    break;
            }
            System.out.println("Общее количество калорий: " + CaloriesCounter);

        }



        int eatten_a1, eatten_a2, eatten_a3, eatten_c, eatten_s1, eatten_s2, eatten_s3;
        eatten_a1 = eatten_a2 = eatten_a3 = eatten_c = eatten_s1 = eatten_s2 = eatten_s3 = 0;
        for(Food item: breakfast)                            // считаем, сколько чего было съедено на завтрак
        {
            if(item == null)
                break;
            if(item.name.equals("Яблоко"))
            {
                if(item.par1.equals("маленькое"))
                    eatten_a1++;
                else if(item.par1.equals("среднее"))
                    eatten_a2++;
                else if(item.par1.equals("большое"))
                    eatten_a3++;
            }
            if(item.name.equals("Сыр"))
                eatten_c++;
            if(item.name.equals("Десерт"))
            {
                if(item.par1.equals("шоколад") || item.par2.equals("шоколад"))
                {
                    if(item.par1.equals("мороженое") || item.par2.equals("мороженое"))
                        eatten_s1++;
                    if(item.par1.equals("клубника") || item.par2.equals("клубника"))
                        eatten_s2++;
                }
                if(item.par1.equals("мороженое") || item.par2.equals("мороженое"))
                {
                    if(item.par1.equals("клубника") || item.par2.equals("клубника"))
                        eatten_s3++;
                }
            }
        }
        System.out.println("На завтрак съедено:");
        System.out.println(" маленьких яблок - " + eatten_a1 + ", средних яблок - " + eatten_a2 + ", больших яблок - " + eatten_a3);
        System.out.println(" кусочков сыра - " + eatten_c);
        System.out.println(" Десертов с мороженым и шоколадом - " + eatten_s1);
        System.out.println(" Десертов с клубникой и шоколадом - " + eatten_s2);
        System.out.println(" Десертов с мороженым и клубникой - " + eatten_s3);
    }
}