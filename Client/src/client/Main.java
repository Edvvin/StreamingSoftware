package client;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import my.utils.*;

import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import my.rmi.*;

public class Main extends Application {
	
	static CSRemote csrmi;
	static SSRemote ssrmi;

	static Stage notificationStage;
	static Stage primaryStage;
	private static TextField log_uname;
	private static TextField reg_uname;
	private static TextField log_pass;
	private static TextField reg_pass;
	private static ChoiceBox<String> movieChoice;
	private static TextField hostTF;
	private static TextField portTF;
	private static String host = "localhost";
	private static int port = 1234;

	private Scene createLogRegScene() {
		VBox login = new VBox();
		VBox register = new VBox();
		SplitPane split = new SplitPane(login, register);
		VBox south = new VBox();
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(split);
		mainPane.setBottom(south);
		login.maxWidthProperty().bind(split.widthProperty().multiply(0.5));
		login.setAlignment(Pos.CENTER);
		register.maxWidthProperty().bind(split.widthProperty().multiply(0.5));
		register.setAlignment(Pos.CENTER);
		
		Label log_label = new Label("Login");
		log_label.setFont(new Font(22));
		Label log_uname_label = new Label("Username: ");
		log_uname = new TextField();
		log_uname.setMaxWidth(250);
		Label log_pass_label = new Label("Password: ");
		log_pass = new PasswordField();
		log_pass.setMaxWidth(250);
		login.getChildren().add(log_label);
		login.getChildren().add(log_uname_label);
		login.getChildren().add(log_uname);
		login.getChildren().add(log_pass_label);
		login.getChildren().add(log_pass);
		Label reg_label = new Label("Register");
		reg_label.setFont(new Font(22));
		Label reg_uname_label = new Label("Username: ");
		reg_uname = new TextField();
		reg_uname.setMaxWidth(250);
		Label reg_pass_label = new Label("Password: ");
		reg_pass = new TextField();
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
				host = hostTF.getText();
				port = Integer.parseInt(portTF.getText());
				if (System.getSecurityManager() == null) {
					System.setSecurityManager(new SecurityManager());
				}

				try {
					Registry regCS = LocateRegistry.getRegistry(host, port);

					csrmi = (CSRemote) regCS.lookup("/csrmi");
					String reply = csrmi.login(log_uname.getText(), log_pass.getText());
					if(!reply.equals("INVALID") && !reply.equals("FAILED")) {
						String[] parts = reply.split(":");
						try {
							Registry regSS = LocateRegistry.getRegistry(
									parts[0],
									Integer.parseInt(parts[1]));
							ssrmi = (SSRemote) regSS.lookup("/ssrmi");
							primaryStage.setScene(createChoiceScene());
						}
						catch(RemoteException | NotBoundException err) {
							//TODO complain
							err.printStackTrace();
						}
					}
					else {
						if(reply.equals("INVALID")) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Log In Failed");
							alert.setContentText("Wrong username or password");
							alert.show();
						}
						else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setHeaderText("Log In Failed");
							alert.setContentText("Server error");
							alert.show();
						}
					}
				} catch (RemoteException | NotBoundException err) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Log In Failed");
					alert.setContentText("Server error");
					alert.show();
					System.out.print(err.getMessage());
				}
			}
		});

		reg_btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// REGISTER HANDLE
				
				port = Integer.parseInt(portTF.getText());
				host = hostTF.getText();
				int port = Integer.parseInt(portTF.getText());
				if (System.getSecurityManager() == null) {
					System.setSecurityManager(new SecurityManager());
				}

				try {
					Registry regCS = LocateRegistry.getRegistry(host, port);

					csrmi = (CSRemote) regCS.lookup("/csrmi");
					boolean reply = csrmi.register(reg_uname.getText(), reg_pass.getText());
					if(reply) {
						primaryStage.setScene(createChoiceScene());
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText("Registration Failed");
						alert.setContentText("Server error");
						alert.show();
					}
				} catch (RemoteException | NotBoundException err) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Registration Failed");
					alert.setContentText("Server error");
					alert.show();
					System.out.print(err.getMessage());
				}
			}
		});
		
		Label hostLabel = new Label("Host:");
		Label portLabel = new Label("Port:");
		hostTF = new TextField();
		portTF = new TextField();
		hostTF.setText(host);
		hostTF.setMaxWidth(200);
		portTF.setText(port + "");
		portTF.setMaxWidth(200);
		south.getChildren().addAll(hostLabel, hostTF, portLabel, portTF);
		south.setAlignment(Pos.CENTER);
		return new Scene(mainPane, 800, 600);
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
				FileChooser fc = new FileChooser();
				File movie = fc.showOpenDialog(primaryStage);
				if(movie != null) {
					try{
						FileInputStream in = new FileInputStream(movie);
						int ind = 0;
						Chunk c = new Chunk(ind);
						int mylen = in.read(c.getBytes());
						boolean first = true;
						while(mylen>0){
							ssrmi.upload(movie.getName(), c, first);
							c = new Chunk(++ind);
							mylen = in.read(c.getBytes());
						}
						ssrmi.uploadFinished(movie.getName());
					}catch(RemoteException e1){
						 //TODO
						e1.printStackTrace();
					}
					catch(IOException e2) {
						e2.printStackTrace();
					}
				}
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
	
	private Scene createTextScene(String text) {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		Label label = new Label(text);
		label.setFont(new Font(25));
		box.getChildren().add(label);

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
		movieChoice = new ChoiceBox<>();
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
		//primaryStage.setScene(createChoiceScene());
		primaryStage.setScene(createLogRegScene());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
