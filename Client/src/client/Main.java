package client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	static Stage notificationStage;
	static Stage primaryStage;

	private Scene createLogRegScene() {
		VBox login = new VBox();
		VBox register = new VBox();
		SplitPane split = new SplitPane(login, register);
		login.maxWidthProperty().bind(split.widthProperty().multiply(0.5));
		login.setAlignment(Pos.CENTER);
		register.maxWidthProperty().bind(split.widthProperty().multiply(0.5));
		register.setAlignment(Pos.CENTER);
		
		Label log_label = new Label("Login");
		log_label.setFont(new Font(22));
		Label log_uname_label = new Label("Username: ");
		TextField log_uname = new TextField();
		log_uname.setMaxWidth(250);
		Label log_pass_label = new Label("Password: ");
		PasswordField log_pass = new PasswordField();
		log_pass.setMaxWidth(250);
		login.getChildren().add(log_label);
		login.getChildren().add(log_uname_label);
		login.getChildren().add(log_uname);
		login.getChildren().add(log_pass_label);
		login.getChildren().add(log_pass);
		Label reg_label = new Label("Register");
		reg_label.setFont(new Font(22));
		Label reg_uname_label = new Label("Username: ");
		TextField reg_uname = new TextField();
		reg_uname.setMaxWidth(250);
		Label reg_pass_label = new Label("Password: ");
		TextField reg_pass = new TextField();
		reg_pass.setMaxWidth(250);
		register.getChildren().add(reg_label);
		register.getChildren().add(reg_uname_label);
		register.getChildren().add(reg_uname);
		register.getChildren().add(reg_pass_label);
		register.getChildren().add(reg_pass);
		Button log_btn = new Button("Log in");
		Button reg_btn = new Button("Register");
		login.getChildren().add(log_btn);
		register.getChildren().add(reg_btn);
		log_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// LOGIN HANDLE
				primaryStage.setScene(createChoiceScene());
			}
		});

		reg_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// REGISTER HANDLE
			}
		});
		
		return new Scene(split, 800, 600);
	}
	
	private Scene createChoiceScene() {
		HBox choices = new HBox();
		choices.setSpacing(20);
		choices.setAlignment(Pos.CENTER);
		Button upload = new Button("Upload");
		Button watch = new Button("Watch");
		Button logout = new Button("Logout");
		upload.setPadding(new Insets(20));
		watch.setPadding(new Insets(20));
		logout.setPadding(new Insets(20));
		upload.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// UPLOAD HANDLE
			}
		});
		watch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// WATCH HANDLE
				primaryStage.setScene(createRoomCreateScene());
			}
		});
		logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// LOGOUT HANDLE
				primaryStage.setScene(createLogRegScene());
			}
		});
		choices.getChildren().add(upload);
		choices.getChildren().add(watch);
		choices.getChildren().add(logout);
		return new Scene(choices,800,600);
	}
	
	private Scene createLoadingScene() {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		Label text = new Label("Please Wait...");
		text.setFont(new Font(25));
		box.getChildren().add(text);

		return new Scene(box,800,600);
	}
	
	private Stage createNotificationStage() {
		ScrollPane pane = new ScrollPane();
		TextArea text = new TextArea();
		text.maxWidthProperty().bind(pane.widthProperty());
		text.minWidthProperty().bind(pane.widthProperty());
		text.maxHeightProperty().bind(pane.heightProperty());
		text.minHeightProperty().bind(pane.heightProperty());
		text.setWrapText(true);
		text.setEditable(false);
		text.setFont(new Font(15));
		pane.setHbarPolicy(ScrollBarPolicy.NEVER);
		pane.setPadding(new Insets(0,5,0,0));
		pane.setContent(text);
		Scene scene = new Scene(pane, 300, 600);
		Stage stage = new Stage();
		stage.setScene(scene);
		return stage;
	}
	
	private Scene createRoomCreateScene() {
		BorderPane border = new BorderPane();
		VBox left = new VBox();
		left.setAlignment(Pos.CENTER);
		VBox right = new VBox();
		right.setAlignment(Pos.CENTER);
		SplitPane center = new SplitPane(left, right);

		center.setDividerPosition(0, 0.5);
		left.maxWidthProperty().bind(center.widthProperty().multiply(0.5));
		right.maxWidthProperty().bind(center.widthProperty().multiply(0.5));
		HBox bottom = new HBox();
		bottom.setAlignment(Pos.CENTER);
		border.setCenter(center);
		border.setBottom(bottom);
		
		Button back = new Button("Back");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// BACK HANDLE
				primaryStage.setScene(createChoiceScene());
			}
		});
		Button createRoom = new Button("Create Room");
		createRoom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// ROOM HANDLE
			}
		});
		Button watchAlone = new Button("Watch Alone");
		watchAlone.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// ALONE HANDLE
			}
		});
		bottom.getChildren().addAll(back, watchAlone, createRoom);
		
		Label movieLabel = new Label("Choose Movie: ");
		movieLabel.setFont(new Font(18));
		ChoiceBox<String> movieChoice = new ChoiceBox<>();
		left.getChildren().add(movieLabel);
		left.getChildren().add(movieChoice);
		Label roomLabel = new Label("Choose your buddies: ");
		roomLabel.setFont(new Font(18));
        ObservableList<Buddy> items = FXCollections.observableArrayList(new Buddy("Edo"), new Buddy("Emi"));
        ListView<Buddy> buddyList = new ListView<>(items);

		right.getChildren().add(roomLabel);
		right.getChildren().add(buddyList);
		
		return new Scene(border, 800, 600);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		primaryStage.setScene(createLogRegScene());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
