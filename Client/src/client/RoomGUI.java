package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class RoomGUI extends HBox {
	private Button joinBtn;
	private Label label;
	public RoomGUI(String roomName) {
		joinBtn = new Button();
		joinBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO join room
			}
		});
		getChildren().add(joinBtn);
		label = new Label();
		label.setText(roomName);
		getChildren().add(label);
	}
	
}
