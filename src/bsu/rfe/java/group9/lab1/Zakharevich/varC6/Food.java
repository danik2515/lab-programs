package bsu.rfe.java.group9.lab1.Zakharevich.varC6;

public abstract class Food implements Consumable, Nutritous {
    String name = null;
    Double calories = null;
    String par1 = null;
    String par2 = null;
    int par = 0;

    public Food(String name)   // конструктор инициализации
    {
        this.name = name;
    }


    public boolean equals(Object arg0)         // перегружен метод сравнения
    {
        if (!(arg0 instanceof Food)) return false;
        if (name == null || ((Food) arg0).name == null) return false;
        return name.equals(((Food) arg0).name);
    }

    public String toString()        // перегружен метод преобразования в строку
    {
        return name;
    }

    public String getName()        // возвращает имя
    {
        return name;
    }

    public void setName(String name)        // для изменения имени
    {
        this.name = name;
    }
}
