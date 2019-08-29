package com.hbm;

import java.util.Optional;

public class Launcher implements View.Listener {
	private Calculator2 calculator;
	private View view;

	private String expression = "";
	private String operator = "";
	private String operand = "";

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
		if ("".equals(operator)) {
			if ("".equals(expression)) {
				expression = "0.";
			} else {
				expression = expression + ".";
			}
			view.displayValue(expression);
		} else {
			if ("".equals(operand)) {
				operand = operand + "0.";
			} else {
				operand = operand + ".";
			}
			view.displayValue(operand);
		}
		refresh();
	}

	@Override
	public void numberPushed(int value) {
		if ("".equals(operator)) {
			expression = expression + value;
			view.displayValue(expression);
		} else {
			operand = operand + value;
			view.displayValue(operand);
		}
		refresh();
	}

	@Override
	public void operatorPushed(String operator) {
		if ("".equals(this.operator)) {
			this.operator = operator;
		} else {
			evaluate();
			this.operator = operator;
		}
		refresh();
	}

	@Override
	public void reset() {
		expression = "";
		operator = "";
		operand = "";
		view.displayValue("");
		refresh();
	}

	@Override
	public void evaluate() {
		calculator.calculate(expression + " " + operator + " " + operand);
		expression = calculator.getResult();
		view.displayValue(expression);
		operator = "";
		operand = "";
		calculator.rememberCurrentResult();
		refresh();
	}

	@Override
	public void recall(int index) {
		if ("".equals(operator)) {
			expression = Optional.ofNullable(calculator.getPreviousResult(index)).orElse("");
			view.displayValue(expression);
		} else {
			operand = Optional.ofNullable(calculator.getPreviousResult(index)).orElse("");
			view.displayValue(operand);
		}
	}

	private void refresh() {
		for (int i = 1; i <= calculator.getNumPreviousResults(); ++i) {
			view.markRecall(i - 1, calculator.getPreviousResult(i));
		}
		for (int i = 1 + calculator.getNumPreviousResults(); i <= 10; ++i) {
			view.unmarkRecall(i - 1);
		}
	}
}
