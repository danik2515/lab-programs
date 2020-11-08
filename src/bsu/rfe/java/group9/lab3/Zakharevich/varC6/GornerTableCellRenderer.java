package bsu.rfe.java.group9.lab3.Zakharevich.varC6;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
// (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
// стога сена - таблица
    private String needle = null;
    private Boolean Palindromes = false;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    public GornerTableCellRenderer() {
// Показывать только 10 знаков после запятой
        formatter.setMaximumFractionDigits(10);
// Не использовать группировку (т.е. не отделять тысячи
// ни запятыми, ни пробелами), т.е. показывать число как "1000",
// а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
// Установить в качестве разделителя дробной части точку, а не
// запятую. По умолчанию, в региональных настройках
// Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// Разместить надпись внутри панели
        panel.add(label);
// Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
// Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
// Установить текст надписи равным строковому представлению числа

        if (col==1 && needle!=null && needle.equals(formattedDouble)) {
// Номер столбца = 1 (т.е. второй столбец) + иголка не null
// (значит что-то ищем) +
// значение иголки совпадает со значением ячейки таблицы -
// то флажок
            JCheckBox Check = new JCheckBox();
            Check.setBackground(Color.WHITE);
            Check.doClick();
            return Check;
        }
        else if(Palindromes){
            String word = "";
            Boolean TruePalindrom = true;
            for(int i = 0 ; i<formattedDouble.length();i++){
                if(formattedDouble.charAt(i)=='.'){

                }
                else{
                    word+=formattedDouble.charAt(i);
                }

            }
            for(int i = 0 ; i<word.length();i++){
                if(word.charAt(i)!=word.charAt(word.length()-1-i)){
                    TruePalindrom = false;
                    break;
                }
            }
            if(TruePalindrom){
                panel.setBackground(Color.RED);
            }
            else{
                panel.setBackground(Color.WHITE);
            }
        }
        else {
// Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        label.setText(formattedDouble);
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }
    public void setPalindromes(Boolean Palindromes) {
        this.Palindromes = Palindromes;
    }
}
