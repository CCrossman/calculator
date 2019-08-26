package com.hbm;

import java.util.function.Function;

import static com.hbm.Assertions.shouldEqual;

public final class Calculator {
	private final String[] results = new String[10];
	private int numPreviousResults = 0;

	private String left;
	private String right;
	private char operator;
	private String result;

	public Calculator(String left, String right, char operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public void setOperator(char operator) {
		this.operator = operator;
	}

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

	public void calculate() {
		pushPreviousResult(result);

		// parse the first term
		Either<String,Integer> leftTerm = parseTerm(left);

		// parse the second term
		Either<String,Integer> rightTerm = parseTerm(right);

		// calculate using these terms
		this.result = leftTerm.flatMap(op1 -> rightTerm.flatMap(op2 -> calculateWith(op1,op2,operator))).fold(Function.identity(), Object::toString);
	}

	private String calculateAndGetResult() {
		calculate();
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

	private static Either<String, Integer> calculateWith(Integer a, Integer b, Character op) {
		switch (op) {
			case '+':
				// TODO: arithmetic overflow/underflow
				return Either.right(a + b);
			case '-':
				// TODO: arithmetic overflow/underflow
				return Either.right(a - b);
			case '*':
				// TODO: arithmetic overflow/underflow
				return Either.right(a * b);
			case '/':
				if (b == 0) {
					return Either.left("cannot divide by zero");
				}
				return Either.right(a / b);
			default:
				return Either.left("invalid operator '" + op + "'");
		}
	}

	private static Either<String, Integer> parseTerm(String str) {
		try {
			int i = Integer.parseInt(str);
			return Either.right(i);
		} catch (Exception ex) {
			return Either.left("invalid term '" + str + "'");
		}
	}

	public static void main(String[] args) {
		shouldEqual("invalid term ''", new Calculator("", "1", '+').calculateAndGetResult());
		shouldEqual("invalid term ''", new Calculator("9", "", '+').calculateAndGetResult());
		shouldEqual("invalid operator '\0'", new Calculator("9", "1", '\0').calculateAndGetResult());
		shouldEqual("cannot divide by zero", new Calculator("9", "0", '/').calculateAndGetResult());
		shouldEqual("10", new Calculator("9", "1", '+').calculateAndGetResult());
		shouldEqual("-6", new Calculator("1", "7", '-').calculateAndGetResult());
		shouldEqual("64", new Calculator("8", "8", '*').calculateAndGetResult());
		shouldEqual("4", new Calculator("12", "3", '/').calculateAndGetResult());
		shouldEqual("cannot divide by zero", new Calculator("256", "0", '/').calculateAndGetResult());

		Calculator calculator = new Calculator("5", "1", '+');
		shouldEqual("6", calculator.calculateAndGetResult());

		calculator.setLeft("3");
		calculator.setRight("9");
		calculator.setOperator('*');
		shouldEqual("27", calculator.calculateAndGetResult());
		shouldEqual("6", calculator.getPreviousResult(1));

		calculator.setLeft("5");
		shouldEqual("45", calculator.calculateAndGetResult());
		shouldEqual("27", calculator.getPreviousResult(1));
		shouldEqual("6", calculator.getPreviousResult(2));
	}
}
