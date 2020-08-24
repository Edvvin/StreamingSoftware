package client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = createLogRegScene();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
