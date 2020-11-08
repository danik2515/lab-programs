package bsu.rfe.java.group9.lab3.Zakharevich.varC6;

import javax.swing.table.AbstractTableModel;
@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Float[] coeff;
    private Double from;
    private Double to;
    private Double step;
    private Double result1;
    private Float result2;
    private Double result3;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients, Float[] coeff) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
        this.coeff = coeff;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
// В данной модели 4 столбца
        return 4;
    }
    public int getRowCount() {
// Вычислить количество точек между началом и концом отрезка
// исходя из шага табулирования
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col) {
// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step*row;
        switch (col){
            case 0:
                return x;
            case 1:
            {
                result1 = coefficients[coefficients.length-1];
                for(int i = coefficients.length-1; i > 0; i--){
                    result1 = result1*x+coefficients[i-1];
                }
                return result1;
            }
            case 2:
            {
                result2 = coeff[coeff.length-1];
                for(int i = coeff.length-1; i > 0; i--){
                    result2 = result2*(float)x+coeff[i-1];
                }
                return result2;
            }
            default:
                return result3 = result2 - result1;
        }

    }
    public String getColumnName(int col) {
        switch (col){
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            case 2:
                return "Через float";
            default:
                return "Разница";
        }
    }
    public Class<?> getColumnClass(int col) {

        if(col==2) {
            return Float.class;
        }
        else {
            return Double.class;
        }
    }
}
