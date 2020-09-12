package client;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class Buddy extends HBox {
	private CheckBox cb;
	private Label label;
	private String username;
	public Buddy(String username) {
		cb = new CheckBox();
		this.username = username;
		getChildren().add(cb);
		label = new Label();
		label.setText(username);
		getChildren().add(label);
	}
	
	public boolean isChecked() {
		return cb.isSelected();
	}
	
	public String getUsername() {
		return username;
	}
}
