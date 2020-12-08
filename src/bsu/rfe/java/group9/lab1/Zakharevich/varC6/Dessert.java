package bsu.rfe.java.group9.lab1.Zakharevich.varC6;

public class Dessert extends Food
{
    /* extends - значит класс является наследником
           другого класса */


    public Dessert(String component1, String component2)                 // конструктор инициализации
    {
        super("Десерт");
        par1 = component1;
        par2 = component2;
        par = 2;
    }
    public boolean equals(Object arg0)  // переопределние метода сравнения
    {
        if (super.equals(arg0))
        {
            if (!(arg0 instanceof Dessert)) return false;
            if (!(par1.equals(((Dessert)arg0).par1))) return false;
            return par2.equals(((Dessert)arg0).par2);
        } else
            return false;
    }
    public Double calculateCalories()       // реализация метода подсчета калорий
    {
        calories=0.0;
        if(!par1.equals("мороженое") && !par1.equals("шоколад") && !par1.equals("клубника"))
            return calories;
        if(!par2.equals("мороженое") && !par2.equals("шоколад") && !par2.equals("клубника"))
            return calories;
        if(par1.equals("мороженое") || par2.equals("мороженое"))
        {
            calories += 200.0;
        }
        if(par1.equals("шоколад") || par2.equals("шоколад"))
        {
            calories += 500.0;
        }
        if(par1.equals("клубника") || par2.equals("клубника"))
        {
            calories += 30.0;
        }
        return calories;
    }
    public String getComponent1()         // возвращает первую начинку
    {
        return par1;
    }
    public String getComponent2()           // возвращает вторую начинку
    {
        return par2;
    }
    public String toString()       // переопределение метода преобразования в строку
    {
        return super.toString() + " состоящий из " + par1 + " и " + par2;
    }
    public void consume()            // реализация метода consume (что произошло с объектом)
    {
        System.out.print(this + " съеден");
    }


}
