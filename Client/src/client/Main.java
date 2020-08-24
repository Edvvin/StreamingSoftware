package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = createChoiceScene();
		Stage secst = createNotificationStage();
		secst.show();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
