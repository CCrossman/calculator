package com.hbm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class View {
	private final Collection<Listener> listeners = new ArrayList<>();

	private final JFrame frame;
	private final JTextField display;
	private final JButton[] recallButtons;

	public View() {
		this.frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create display for the numbers
		display = new JTextField(18);
		display.setEditable(false);
		display.setBackground(Color.white);

		// create number buttons
		JButton[] numberButtons = new JButton[10];
		for (int i = 0; i < 10; ++i) {
			final int j = i;
			numberButtons[i] = newButton(String.valueOf(i), listener -> listener.numberPushed(j));
		}
		// create operator buttons
		JButton btnAdd = newButton("+", listener -> listener.operatorPushed("+"));
		JButton btnSubtract = newButton("-", listener -> listener.operatorPushed("-"));
		JButton btnMultiply = newButton("*", listener -> listener.operatorPushed("*"));
		JButton btnDivide = newButton("/", listener -> listener.operatorPushed("/"));
		JButton btnEqual = newButton("=", Listener::evaluate);
		JButton btnReset = newButton("RESET", Listener::reset);
		JButton btnPoint = newButton(".", Listener::pointPushed);

		// create a panel and add elements to it
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(Box.createVerticalBox());

		JPanel row1 = new JPanel();
		row1.add(numberButtons[1]);
		row1.add(numberButtons[2]);
		row1.add(numberButtons[3]);
		row1.add(Box.createHorizontalStrut(20));
		row1.add(btnAdd);
		centerPanel.add(row1);

		JPanel row2 = new JPanel();
		row2.add(numberButtons[4]);
		row2.add(numberButtons[5]);
		row2.add(numberButtons[6]);
		row2.add(Box.createHorizontalStrut(20));
		row2.add(btnSubtract);
		centerPanel.add(row2);

		JPanel row3 = new JPanel();
		row3.add(numberButtons[7]);
		row3.add(numberButtons[8]);
		row3.add(numberButtons[9]);
		row3.add(Box.createHorizontalStrut(20));
		row3.add(btnMultiply);
		centerPanel.add(row3);

		JPanel row4 = new JPanel();
		row4.add(btnPoint);
		row4.add(numberButtons[0]);
		row4.add(btnEqual);
		row4.add(Box.createHorizontalStrut(20));
		row4.add(btnDivide);
		centerPanel.add(row4);

		JPanel southPanel = new JPanel(new GridLayout(4, 3));

		this.recallButtons = new JButton[10];
		for (int i = 0; i < 10; ++i) {
			final int j = i + 1;
			JButton btn = newButton("", listener -> listener.recall(j));
			recallButtons[i] = btn;
			southPanel.add(btn);
			unmarkRecall(i);
		}
		southPanel.add(btnReset);

		panel.add(display, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);

		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
	}

	private JButton newButton(String text, Consumer<Listener> onClick) {
		JButton button = new JButton(text);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					onClick.accept(listener);
				}
			}
		});
		return button;
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void displayValue(String message) {
		display.setText(message);
	}

	public void show() {
		frame.setVisible(true);
	}

	public void markRecall(int index, String result) {
		recallButtons[index].setText(result);
		recallButtons[index].setEnabled(true);
	}

	public void unmarkRecall(int index) {
		recallButtons[index].setText("RECALL #" + (index+1));
		recallButtons[index].setEnabled(false);
	}

	public static interface Listener {
		public void pointPushed();
		public void numberPushed(int value);
		public void operatorPushed(String operator);
		public void reset();
		public void evaluate();
		public void recall(int index);
	}
}
