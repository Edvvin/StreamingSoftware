package server;
import javax.swing.*;
import java.io.*;

public class Logger {

	private JTextArea log;
	public Logger(JTextArea log) { 
		this.log = log;
	}

	public Logger() { 
		log = null;
	}
	
	public void log(String str) {
		if(log == null) {
			System.out.println(str);
		}else {
			log.setText(log.getText() + str + "\n");
		}
	}
}
