package com.tenjava.entries.MarianDCrafter.t2.machines;

import java.util.ArrayList;
import java.util.List;

public class CalculatorStringParser {

    private String input;

    public CalculatorStringParser(String input) {
        this.input = input;
    }

    public double calculate() {
        List<String> previousNumbers = new ArrayList<String>();
        List<Character> operators = new ArrayList<Character>();
        String currentNumber = "";
        for (char character : input.toCharArray()) {
            if (Character.isDigit(character) || character == '.')
                currentNumber += character;
            else if (character == '+' || character == '-' || character == '*' || character == '/') {
                previousNumbers.add(currentNumber);
                operators.add(character);
                currentNumber = "";
            }
        }
        if (!currentNumber.isEmpty())
            previousNumbers.add(currentNumber);

        Double result = null;
        for (int i = 0; i < previousNumbers.size(); i++) {
            double number = Double.parseDouble(previousNumbers.get(i));
            if (result == null)
                result = number;
            else if (operators.size() > i - 1) { // i-1, because we want to get the operator BEFORE the number
                switch (operators.get(i - 1)) {
                    case '+':
                        result += number;
                        break;
                    case '-':
                        result -= number;
                        break;
                    case '*':
                        result *= number;
                        break;
                    case '/':
                        result /= number;
                        break;
                }
            }
        }

        return result;
    }

}
