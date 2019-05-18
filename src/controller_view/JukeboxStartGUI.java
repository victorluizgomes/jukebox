package controller_view;

import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import views.JukeboxView;
import views.LoginView;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: JukeboxStartGUI
 * DESCRIPTION: This program is a functional spike to determine the interactions are actually
 * working. It is an event-driven program with a graphical user interface to affirm the
 * functionality all Iteration 1 tasks have been completed and are working correctly. This program
 * will be used to test your code for the first 100 points of the JukeBox project.
 **************************************************************************************************/

public class JukeboxStartGUI extends Application {
	
	private static BorderPane window;
	private static LoginView loginView;
	private static JukeboxView jukeboxView;
	private static Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}// END main()
	
	/**********************************************************************************************
	 * METHOD: start()
	 * PURPOSE: Sets up the Jukebox GUI initialized to the login screne at startup.
	 * @param primaryStage The Stage of the GUI display.
	 **********************************************************************************************/
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Jukebox");
		window = new BorderPane();
		
		// Set up views (default vs. last known state)
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Select 'OK' to read last known state.\nSelect 'Cancel' to start at default.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) { // Set up views
			loginView = new LoginView(true);
			jukeboxView = new JukeboxView(true);
		} else { // Set up views and reset
			loginView = new LoginView(false);
			jukeboxView = new JukeboxView(false);
		}
		
		window.setCenter(loginView);
		window.setId("window");
		window.setStyle("-fx-background-color: rgb(147, 222, 237);");
		Scene scene = new Scene(window, 300, 250);
		primaryStage.setScene(scene);
		stage = primaryStage;
		stage.show();
		stage.setOnCloseRequest(new WindowCloseHandler());
	}// END start()
	
	/**********************************************************************************************
	 * METHOD: setViewTo()
	 * PURPOSE: Sets the GUI to either the "login" or "jukebox" view.
	 * @param view The String view name to be set to stage.
	 **********************************************************************************************/
	public static void setViewTo(String view) {
		if (view.equals("login")) {
			loginView.resetFields();
			window.setCenter(null);
			stage.setWidth(300);
			stage.setHeight(250);
			window.setCenter(loginView);
		}
		if (view.equals("jukebox")) {
			window.setCenter(null);
			jukeboxView.setViewer(loginView.getCurrentUser());
			stage.setWidth(450);
			stage.setHeight(550);
			window.setCenter(jukeboxView);
		}
	}// END setViewTo()
	
	/**********************************************************************************************
	 * CLASS: WindowCloseHandler()
	 * PURPOSE: Handles when the user tries to close the window and asks if the user wants to save
	 *     current data.
	 **********************************************************************************************/
	private class WindowCloseHandler implements EventHandler<WindowEvent> {
		@Override
		public void handle(WindowEvent event) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Select 'OK' to save current data.\nSelect 'Cancel' to not save.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				loginView.writeFiles();
				jukeboxView.writeFiles();
			}
		}//END handle()
	}//END WindowCloseHandler class
	
}// END JukeboxStartGUI class