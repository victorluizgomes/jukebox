package views;

import controller_view.JukeboxStartGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.AllAccounts;
import model.User;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: LoginView
 * DESCRIPTION:
 * Builds the GUI LoginView and provides all interaction implementation for buttons and text boxes.
 **************************************************************************************************/

public class LoginView extends BorderPane {
	
	private static AllAccounts accountList;
	private String currentUser;
	
	private GridPane loginGrid;
	private Label loginMessage;
	private Button loginButton;
	private TextField usernameField;
	private PasswordField passwordField;
	
	/**********************************************************************************************
	 * METHOD: LoginView()
	 * PURPOSE: Initializes the user accounts object and sets up the interaction grid.
	 * @param doReadFiles The boolean flag to determine whether to read files or not.
	 **********************************************************************************************/
	public LoginView(boolean doReadFiles) {
		accountList = new AllAccounts(doReadFiles);
		currentUser = "";
		ButtonListener buttonListener = new ButtonListener();
		setUpLoginGrid(buttonListener);
		this.setCenter(loginGrid);
	}//END LoginView() constructor
	
	/**********************************************************************************************
	 * METHOD: setUpLoginGrid()
	 * PURPOSE: Builds the user interaction grid for the login screen.
	 * @param buttonListener The ButtonListener handler class when a button is pressed.
	 **********************************************************************************************/
	private void setUpLoginGrid(ButtonListener buttonListener) {
		loginMessage = new Label("Please log in.");
		
		Label usernameLabel = new Label("Username: ");
		Label passwordLabel = new Label("Password: ");
		usernameField = new TextField();
		passwordField = new PasswordField();
		usernameField.setMaxSize(150, 20);
		passwordField.setMaxSize(150, 20);
		loginButton = new Button("Login");
		loginButton.setOnAction(buttonListener);
		
		loginGrid = new GridPane();
		loginGrid.setAlignment(Pos.CENTER);
		loginGrid.setVgap(5);
		loginGrid.setHgap(5);
		
		loginGrid.add(loginMessage, 1, 0);
		loginGrid.add(usernameLabel, 0, 2);
		loginGrid.add(passwordLabel, 0, 3);
		loginGrid.add(usernameField, 1, 2);
		loginGrid.add(passwordField, 1, 3);
		loginGrid.add(loginButton, 1, 4);
	}//END setUpLoginGrid()
	
	/**********************************************************************************************
	 * METHOD: resetFields()
	 * PURPOSE: Resets all of the input text fiels in the LoginView.
	 **********************************************************************************************/
	public void resetFields() {
		loginMessage.setText("Please log in.");
		usernameField.setText("");
		passwordField.setText("");
	}//END resetFields()
	
	/**********************************************************************************************
	 * CLASS: ButtonListener()
	 * PURPOSE: Handles when the User clicks the "Login" button in LoginView.
	 **********************************************************************************************/
	private class ButtonListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//Handle user login
			if(event.getSource().equals(loginButton)) {
				String user = usernameField.getText();
				String pass = passwordField.getText();
				currentUser = user;
				if(accountList.verifyAccount(user, pass)) {
					JukeboxStartGUI.setViewTo("jukebox");
				} else {
					loginMessage.setText("Invalid login.");
				}
			}
		}//END handle()
	}//END ButtonListener class
	
	/**********************************************************************************************
	 * METHOD: getCurrentUser()
	 * PURPOSE: Returns the current User that has logged in to the Jukebox.
	 * @return The current User.
	 **********************************************************************************************/
	public User getCurrentUser() {
		return accountList.getUser(currentUser);
	}//END getCurrentUser()
	
	/**********************************************************************************************
	 * METHOD: setCurrentUser()
	 * PURPOSE: Sets the username of the current User for testing purposes.
	 * @param user The String username of the user to be set.
	 **********************************************************************************************/
	public void setCurrentUser(String user) {
		currentUser = user;
	}//END serCurrentUser()
	
	/**********************************************************************************************
	 * METHOD: updateAccounts()
	 * PURPOSE: Handles when an admin adds / removes an account. If the account already exists,
	 *     the user's information is updated.
	 * @param op The String operation to be performed: "account" added or updated, and "remove" account
	 * @param user The String username of the user to be added or updated.
	 * @param pass The String of the (new) matching password for this user.
	 * @param admin The boolean flag of this user's (new) admin status.
	 * @return A String message after an account is updated.
	 **********************************************************************************************/
	public static String updateAccounts(String op, String user, String pass, boolean admin) {
		//Handle addition / update of an account
		if(op.equals("account")) {
			User account = accountList.getUser(user);
			if(account == null) { //Username does not exist
				accountList.addAccount(user, pass, admin);
				return "New account for '"+user+"' has been added.";
			} else { //Username exists -> update user information
				account.updateInfo(pass, admin);
				return "Account information for '"+user+"' has been updated.";
			}
		}
		
		//Remove an account
		if(op.equals("remove")) {
			boolean removed = accountList.removeAccount(user);
			if(removed) {
				return "Account for '"+user+"' has been removed.";
			} else {
				return "Account for '"+user+"' does not exist or cannot be removed.";
			}
		}
		
		return "No account has been updated."; //Fake return
	}//END updateAccounts()
	
	/**********************************************************************************************
	 * METHOD: resetCounts()
	 * PURPOSE: On a new day, reset all User's song selection counts to 0.
	 **********************************************************************************************/
	public static void resetCounts() {
		accountList.resetCounts();
	}//END resetCounts()
	
	/**********************************************************************************************
	 * METHOD: writeFiles()
	 * PURPOSE: Sends messages to any objects that need to store information for persistence and
	 *     write out the necessary files.
	 **********************************************************************************************/
	public void writeFiles() {
		accountList.writeAccountsToFile();
	}//END writeFiles()
	
}//END LoginView class