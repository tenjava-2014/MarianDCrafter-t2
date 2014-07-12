package com.tenjava.entries.MarianDCrafter.t2.machines.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses an input string and calculates the result.
 */
public class CalculatorStringParser {

    private String input;

    /**
     * Initializes the CalculatorStringParser with the input string.
     * @param input the input string.
     */
    public CalculatorStringParser(String input) {
        this.input = input;
    }

    /**
     * Parses the input string and calculates the result.
     * @return the result
     * @throws CalculatorStringParserException if there is an error with parsing the string.
     */
    public double calculate() throws CalculatorStringParserException {
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
            double number;
            try {
                number = Double.parseDouble(previousNumbers.get(i));
            } catch(NumberFormatException e) {
                throw new CalculatorStringParserException("Could not parse a number.");
            }
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

        if(result == null)
            throw new CalculatorStringParserException("Could not parse the input.");

        return result;
    }

}
