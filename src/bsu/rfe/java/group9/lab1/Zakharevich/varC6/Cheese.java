package bsu.rfe.java.group9.lab1.Zakharevich.varC6;

public class Cheese extends Food {
    public Cheese()      // конструктор
{
    super("Сыр");
    par = 0;// вызывает конструктор базового класса
}

    public Double calculateCalories()       // реализация метода подсчета калорий
    {
        calories = 30.0;
        return calories;
    }

    public void consume()           // реализация метода consume (что произошло с объектом)
    {
        System.out.print(this + " съеден");
    }

}
