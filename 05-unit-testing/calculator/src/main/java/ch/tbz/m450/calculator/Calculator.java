package ch.tbz.m450.calculator;

public class Calculator {
    public double add(double summand1, double summand2) {
        double result = summand1 + summand2;
        return result;
    }

    public double subtract(double minuend, double subtrahend) {
        double result = minuend - subtrahend;
        return result;
    }

    public double multiply(double factor1, double factor2) {
        double result = factor1 * factor2;
        return result;
    }

    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }

        double result = dividend / divisor;
        return result;
    }
}
