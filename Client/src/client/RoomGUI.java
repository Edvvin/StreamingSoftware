package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import my.rmi.Room;

public class RoomGUI extends HBox {
	private Button joinBtn;
	private Label label;
	private Room room;
	public RoomGUI(Room room) {
		this.room = room;
		joinBtn = new Button();
		getChildren().add(joinBtn);
		label = new Label();
		label.setText(room.getRoomName());
		getChildren().add(label);
		joinBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Main.download(room.getMovie());
				Main.currentRoom = room;
				if(Main.currentUser.equals(room.getAdmin()))
					Main.primaryStage.setScene(Main.createMediaAdmin());
				else
					Main.primaryStage.setScene(Main.createMediaGuest());
			}
		});
	}
	
	
	
}
