package com.hbm;

public class Launcher implements View.Listener {
	private Calculator2 calculator;
	private View view;

	private String expression = "";

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
	public void numberPushed(int value) {
		if ("".equals(expression)) {
			expression = String.valueOf(value);
			view.displayValue(expression);
		} else {
			calculator.calculate(expression + " " + value);
			expression = calculator.getResult();
			view.displayValue(expression);
		}
	}

	@Override
	public void operatorPushed(String operator) {
		if ("".equals(expression)) {
			expression = operator;
		} else {
			expression = expression + " " + operator;
		}
	}

	@Override
	public void reset() {
		expression = "";
		view.displayValue("");
	}
}
