package com.hbm;

import java.util.*;
import java.util.regex.Pattern;

import static com.hbm.Assertions.shouldEqual;

public final class Calculator2 {
	private final String[] results = new String[10];
	private int numPreviousResults = 0;
	private String result;

	public String getResult() {
		return result;
	}

	public String getPreviousResult(int index) {
		if (numPreviousResults == 0) {
			return null;
		}
		if (index < 1 || index > 10) {
			return "invalid index: " + index;
		}
		return results[numPreviousResults - index];
	}

	private static final Pattern ptnOperator = Pattern.compile("[\\+\\-\\*\\/]");

	public void calculate(String expression) {
		pushPreviousResult(result);

		try (Scanner scanner = new Scanner(expression)) {
			double value = scanner.nextDouble();

			while (scanner.hasNext()) {
				String operator = scanner.next(ptnOperator);
				double operand = scanner.nextDouble();

				switch (operator) {
					case "+":
						value = value + operand;
						break;
					case "-":
						value = value - operand;
						break;
					case "*":
						value = value * operand;
						break;
					case "/":
						if (operand == 0) {
							this.result = "cannot divide by zero";
							return;
						}
						value = value / operand;
						break;
				}
			}
			this.result = String.valueOf(value);
		} catch (InputMismatchException ime) {
			//ime.printStackTrace();
			this.result = "invalid expression: " + expression;
		} catch (NoSuchElementException nsee) {
			//nsee.printStackTrace();
			this.result = "invalid expression: " + expression;
		}
	}

	private String calculateAndGetResult(String expression) {
		calculate(expression);
		return getResult();
	}

	private void pushPreviousResult(String result) {
		if (result != null) {
			if (numPreviousResults == 10) {
				System.arraycopy(results, 1, results, 0, 9);
				numPreviousResults--;
			}
			results[numPreviousResults++] = result;
		}
	}

	private void clear() {
		numPreviousResults = 0;
		Arrays.fill(results,null);
	}

	public static void main(String[] args) {
		final Calculator2 calculator2 = new Calculator2();
		shouldEqual("invalid expression: + 1", calculator2.calculateAndGetResult("+ 1"));
		shouldEqual("invalid expression: / 1", calculator2.calculateAndGetResult("/ 1"));
		shouldEqual("invalid expression: 9 +", calculator2.calculateAndGetResult("9 +"));
		shouldEqual("cannot divide by zero", calculator2.calculateAndGetResult("9 / 0"));
		shouldEqual("10.0", calculator2.calculateAndGetResult("9 + 1"));
		shouldEqual("-6.0", calculator2.calculateAndGetResult("1 - 7"));
		shouldEqual("64.0", calculator2.calculateAndGetResult("8 * 8"));
		shouldEqual("4.0", calculator2.calculateAndGetResult("12 / 3"));

		calculator2.clear();
		shouldEqual("2.0", calculator2.calculateAndGetResult("5 + 1 * 3 / 6 - 1"));
		shouldEqual("5.0", calculator2.calculateAndGetResult("1 + 2 + 3 - 1"));
		shouldEqual("2.0", calculator2.getPreviousResult(1));

		shouldEqual("3.75", calculator2.calculateAndGetResult("15 / 4"));
	}
}
