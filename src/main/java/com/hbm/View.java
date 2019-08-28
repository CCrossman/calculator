package com.hbm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

public class View {
	private final Collection<Listener> listeners = new ArrayList<>();

	private final JFrame frame;
	private final JTextField display;

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
			numberButtons[i] = new JButton(String.valueOf(i));
			numberButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (Listener listener : listeners) {
						listener.numberPushed(j);
					}
				}
			});
		}
		// create operator buttons
		JButton btnAdd = new JButton("+");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.operatorPushed("+");
				}
			}
		});

		JButton btnSubtract = new JButton("-");
		btnSubtract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.operatorPushed("-");
				}
			}
		});

		JButton btnMultiply = new JButton("*");
		btnMultiply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.operatorPushed("*");
				}
			}
		});

		JButton btnDivide = new JButton("/");
		btnDivide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.operatorPushed("/");
				}
			}
		});

		JButton btnEqual = new JButton("=");
		btnEqual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.evaluate();
				}
			}
		});

		JButton btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.reset();
				}
			}
		});

		JButton btnPoint = new JButton(".");
		btnPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.pointPushed();
				}
			}
		});

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
		centerPanel.add(row1);

		JPanel row2 = new JPanel();
		row2.add(numberButtons[4]);
		row2.add(numberButtons[5]);
		row2.add(numberButtons[6]);
		centerPanel.add(row2);

		JPanel row3 = new JPanel();
		row3.add(numberButtons[7]);
		row3.add(numberButtons[8]);
		row3.add(numberButtons[9]);
		centerPanel.add(row3);

		JPanel row4 = new JPanel();
		row4.add(btnPoint);
		row4.add(numberButtons[0]);
		row4.add(btnEqual);
		centerPanel.add(row4);

		JPanel southPanel = new JPanel();
		southPanel.add(btnAdd);
		southPanel.add(btnSubtract);
		southPanel.add(btnMultiply);
		southPanel.add(btnDivide);
		southPanel.add(btnReset);

		panel.add(display, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);

		frame.add(panel);
		frame.pack();
		frame.setResizable(false);
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

	public static interface Listener {
		public void pointPushed();
		public void numberPushed(int value);
		public void operatorPushed(String operator);
		public void reset();
		public void evaluate();
	}
}
