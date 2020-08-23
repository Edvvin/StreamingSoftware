package server;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class SubServerGUI extends JFrame {
	
	private JTextArea log;
	private JButton button;
	
	public SubServerGUI() {
		super("SubServer");
		setLayout(new BorderLayout());

		log = new JTextArea();
		log.setEditable(false);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		JScrollPane sp = new JScrollPane(log);
		add(sp, BorderLayout.CENTER);
		button = new JButton();
		button.setText("Close");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		add(button, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
			}
		});
		
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
	public JTextArea getTextArea() {
		return this.log;
	}
	
}
