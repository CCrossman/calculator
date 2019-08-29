package com.hbm;

import java.util.Optional;

public class Launcher implements View.Listener {
	private Calculator2 calculator;
	private View view;

	private String expression = "";
	private String operator = "";
	private String operand = "";
	private boolean isErrorState = false;

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.setModel(new Calculator2());
		launcher.setView(new View());
		launcher.run();
	}

	private void run() {
		view.addListener(this);
		view.show();
	}

	private void setModel(Calculator2 calculator) {
		this.calculator = calculator;
	}

	private void setView(View view) {
		this.view = view;
	}

	@Override
	public void pointPushed() {
		clearErrorState();
		if ("".equals(operator)) {
			if ("".equals(expression)) {
				expression = "0.";
			} else {
				expression = expression + ".";
			}
		} else {
			if ("".equals(operand)) {
				operand = operand + "0.";
			} else {
				operand = operand + ".";
			}
		}
		updateView();
	}

	@Override
	public void numberPushed(int value) {
		clearErrorState();
		if ("".equals(operator)) {
			expression = expression + value;
		} else {
			operand = operand + value;
		}
		updateView();
	}

	@Override
	public void operatorPushed(String operator) {
		clearErrorState();
		if ("".equals(this.operator) || "".equals(operand)) {
			this.operator = operator;
		} else {
			evaluate();
			this.operator = operator;
		}
		updateView();
	}

	@Override
	public void clear() {
		expression = "";
		operator = "";
		operand = "";
		updateView();
	}

	@Override
	public void evaluate() {
		clearErrorState();
		if ("".equals(expression)) {
			return;
		}
		if (!"".equals(operator) && "".equals(operand)) {
			return;
		}
		calculator.calculate(expression + " " + operator + " " + operand);
		expression = calculator.getResult();
		operator = "";
		operand = "";
		if (isNumeric(expression)) {
			calculator.rememberCurrentResult();
		} else {
			isErrorState = true;
		}
		updateView();
	}

	@Override
	public void recall(int index) {
		clearErrorState();
		if ("".equals(operator)) {
			expression = Optional.ofNullable(calculator.getPreviousResult(index)).orElse("");
			if (!isNumeric(expression)) {
				isErrorState = true;
			}
		} else {
			operand = Optional.ofNullable(calculator.getPreviousResult(index)).orElse("");
		}
		updateView();
	}

	@Override
	public void reset() {
		expression = "";
		operator = "";
		operand = "";
		isErrorState = false;
		calculator.forgetPreviousResults();
		updateView();
	}

	@Override
	public void instantSquare() {
		clearErrorState();
		if ("".equals(expression)) {
			expression = "0";
		}
		operator = "*";
		operand = expression;
		evaluate();
	}

	@Override
	public void instantNegate() {
		clearErrorState();
		if ("".equals(expression)) {
			expression = "0";
		} else {
			operand = expression;
			expression = "0";
			operator = "-";
		}
		evaluate();
	}

	@Override
	public void instantInvert() {
		clearErrorState();
		operand = expression;
		expression = "1";
		operator = "/";
		evaluate();
	}

	@Override
	public void backspace() {
		clearErrorState();
		if (!"".equals(operand)) {
			operand = operand.substring(0, operand.length() - 1);
		} else if (!"".equals(operator)) {
			operator = "";
		} else if (!"".equals(expression)) {
			expression = expression.substring(0, expression.length() - 1);
		}
		updateView();
	}

	private void clearErrorState() {
		if (isErrorState) {
			clear();
			isErrorState = false;
		}
	}

	private static boolean isNumeric(String str) {
		if (str.startsWith("-")) {
			return isNumeric(str.substring(1));
		}
		for (char ch : str.toCharArray()) {
			if (ch != '.' && !Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	private void updateView() {
		view.displayValue(expression + " " + operator + " " + operand);
		for (int i = 1; i <= calculator.getNumPreviousResults(); ++i) {
			view.markRecall(i - 1, calculator.getPreviousResult(i));
		}
		for (int i = 1 + calculator.getNumPreviousResults(); i <= 10; ++i) {
			view.unmarkRecall(i - 1);
		}
	}
}
