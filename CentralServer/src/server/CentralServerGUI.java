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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;

import my.rmi.CSRemote;
import my.utils.Consts;

public class CentralServerGUI extends JFrame {
	
	private JTextArea log;
	private JButton button;
	
	public CentralServerGUI() {
		super("CentralServer");
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
				System.exit(0);
			}
		});
		add(button, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
	public JTextArea getTextArea() {
		return this.log;
	}
	
}
