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

		JButton btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Listener listener : listeners) {
					listener.reset();
				}
			}
		});

		// create a panel and add elements to it
		JPanel panel = new JPanel();
		panel.add(display);

		for (JButton button : numberButtons) {
			panel.add(button);
		}
		panel.add(btnAdd);
		panel.add(btnSubtract);
		panel.add(btnMultiply);
		panel.add(btnDivide);
		panel.add(btnReset);

		panel.setBackground(Color.gray);
		frame.add(panel);
		//setSize(200, 220);
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
		public void numberPushed(int value);
		public void operatorPushed(String operator);
		public void reset();
	}
}
