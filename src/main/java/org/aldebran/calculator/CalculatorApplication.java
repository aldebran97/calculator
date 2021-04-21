package org.aldebran.calculator;

public class CalculatorApplication {

    public static void main(String[] args) {
        System.out.println(Calculator.calc("2*sin(1+2*(3+4))+3/4"));
        System.out.println(Calculator.calc("100/50+2/cos(1+2*(3+4))"));
        System.out.println(2 * Math.sin(15) + 3.0 / 4.0);
        System.out.println(100 / 50.0 + 2 / Math.cos(1 + 2 * (3 + 4)));
    }

}
