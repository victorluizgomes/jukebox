package views;

import java.time.LocalDate;
import java.util.ArrayList;
import controller_view.JukeboxStartGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Jukebox;
import model.Song;
import model.User;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: JukeboxView
 * DESCRIPTION:
 * Builds the GUI JukeboxView and provides all interaction implementation for buttons and text
 * boxes. The view can be set to show different options based on the user (admin vs. user).
 **************************************************************************************************/

public class JukeboxView extends BorderPane {
	
	//Jukebox data fields
	private static Jukebox jukebox;
	private ObservableList<Song> allSongs;
	private static ObservableList<String> songQueue;
	private User currentUser;
	
	//Admin grid view fields
	private GridPane adminJukeboxGrid;
	private TextField adminUsernameField;
	private TextField adminPasswordField;
	private Button addAdminButton;
	private Button addUserButton;
	private Button removeUserButton;
	private Button adminAddSongButton;
	private Button adminLogoutButton;
	private static ListView<String> songQueueView;
	private JukeboxViewer adminJukeboxViewer = new JukeboxViewer();
	
	//User grid view fields
	private GridPane userJukeboxGrid;
	private Button userAddSongButton;
	private Button userLogoutButton;
	private JukeboxViewer jukeboxViewer = new JukeboxViewer();
	
	/**********************************************************************************************
	 * METHOD: JukeboxView()
	 * PURPOSE: Initializes the Jukebox object and builds the admin and user interaction grids.
	 * @param doReadFiles The boolean flag to determine whether to read files or not.
	 **********************************************************************************************/
	public JukeboxView(boolean doReadFiles) {
		jukebox = new Jukebox(doReadFiles);
		allSongs = jukebox.getAllSongInfo();
		songQueue = FXCollections.observableArrayList();
		ArrayList<String> jukeboxSongQueue = jukebox.getSongQueue();
		for(int i = 0; i < jukeboxSongQueue.size(); i++) {
			songQueue.add(jukeboxSongQueue.get(i));
		}

		ButtonListener buttonListener = new ButtonListener();
		setUpAdminJukeboxGrid(buttonListener);
		setUpUserJukeboxGrid(buttonListener);
	}//END JukeboxView constructor
	
	/**********************************************************************************************
	 * METHOD: setViewer()
	 * PURPOSE: Sets the JukeboxView to either "admin" or "user" setting.
	 * @param viewer The User currently logged into the Jukebox.
	 **********************************************************************************************/
	public void setViewer(User viewer) {
		//Store current User information
		currentUser = viewer;
		
		if(viewer.isAdmin()) {
			this.setCenter(null);
			this.setCenter(adminJukeboxGrid);
		} else {
			this.setCenter(null);
			this.setCenter(userJukeboxGrid);
		}
	}//END setViewTo()
	
	/**********************************************************************************************
	 * METHOD: setUpAdminJukeboxGrid()
	 * PURPOSE: Builds the admin view containing the admin, user, and song (jukebox) grids.
	 *     Admin Grid - provide interaction for adding and removing users (admin/normal)
	 *     User Grid - provide search bar, song selection button and logout button interactions
	 *     Song Grid - display the list of all songs (or subset of songs in search)
	 * @param buttonListener The ButtonListener handler class when a button is pressed.
	 **********************************************************************************************/
	private void setUpAdminJukeboxGrid(ButtonListener buttonListener) {
		//Admin interaction fields / variables
		Label adminMessage = new Label("Account Management:");
		adminUsernameField = new TextField();
		adminPasswordField = new TextField();
		addAdminButton = new Button("Add Admin");
		addUserButton = new Button("Add User");
		removeUserButton = new Button("Remove");
		addAdminButton.setOnAction(buttonListener);
		addUserButton.setOnAction(buttonListener);
		removeUserButton.setOnAction(buttonListener);
		
		//User interaction fields / variables
		adminAddSongButton = new Button("Add Song");
		adminLogoutButton = new Button("Log Out");
		adminAddSongButton.setOnAction(buttonListener);
		adminLogoutButton.setOnAction(buttonListener);
		
		//SETUP Admin interactions grid
		GridPane adminGrid = new GridPane();
		adminGrid.setPrefWidth(100);
		adminGrid.setAlignment(Pos.CENTER_LEFT);
		adminGrid.setHgap(5); adminGrid.setVgap(5);
		
		adminGrid.add(adminMessage, 0, 0);
		adminGrid.add(new Label("Username:"), 0, 1);
		adminGrid.add(new Label("Password:"), 1, 1);
		adminUsernameField.setMaxWidth(120);
		adminGrid.add(adminUsernameField, 0, 2);
		adminPasswordField.setMaxWidth(120);
		adminGrid.add(adminPasswordField, 1, 2);
		adminGrid.add(addAdminButton, 3, 0);
		adminGrid.add(addUserButton, 3, 1);
		adminGrid.add(removeUserButton, 3, 2);
		
		//SETUP User interactions grid
		GridPane userGrid = new GridPane();
		userGrid.setAlignment(Pos.CENTER_LEFT);
		userGrid.setHgap(5); userGrid.setVgap(5);
		
		userGrid.add(adminAddSongButton, 0, 0);
		userGrid.add(new Label(" "), 1, 0);
		userGrid.add(adminLogoutButton, 2, 0);
		
		// Sets up the Song Queue List View
		songQueueView = new ListView<String>();
		songQueueView.setItems(songQueue);
		songQueueView.setPrefSize(100, 100);
		
		//SETUP SongView grid
		GridPane songGrid = new GridPane();
		songGrid.setAlignment(Pos.CENTER);
		songGrid.setHgap(5); songGrid.setVgap(5);;
		
		adminJukeboxViewer.setItems(allSongs);
		songGrid.add(adminJukeboxViewer, 0, 0);
		songGrid.add(new Label("Song Queue:"), 0, 1);
		songGrid.add(songQueueView, 0, 2);
		
		//SETUP final Jukebox views
		adminJukeboxGrid = new GridPane();
		adminJukeboxGrid.setAlignment(Pos.CENTER);
		adminJukeboxGrid.setVgap(5);
		adminJukeboxGrid.setHgap(5);
		
		adminJukeboxGrid.add(adminGrid, 0, 0);
		adminJukeboxGrid.add(userGrid, 0, 2);
		adminJukeboxGrid.add(songGrid, 0, 4);
	}//END setUpAdminJukeboxGrid()
	
	/**********************************************************************************************
	 * METHOD: setUpUserJukeboxGrid()
	 * PURPOSE: Builds the user view of the Jukebox containing the user and song (jukebox) grids.
	 *     User Grid - provide search bar, song selection button and logout button interactions
	 *     Song Grid - display the list of all songs (or subset of songs in search)
	 * @param buttonListener The ButtonListener handler class when a button is pressed.
	 **********************************************************************************************/
	private void setUpUserJukeboxGrid(ButtonListener buttonListener) {
		//User interaction fields / variables
		userAddSongButton = new Button("Add Song");
		userLogoutButton = new Button("Log Out");
		userAddSongButton.setOnAction(buttonListener);
		userLogoutButton.setOnAction(buttonListener);
		
		//SETUP User interactions grid
		GridPane userGrid = new GridPane();
		userGrid.setAlignment(Pos.CENTER_LEFT);
		userGrid.setHgap(5); userGrid.setVgap(5);
		
		userGrid.add(userAddSongButton, 0, 0);
		userGrid.add(new Label(" "), 1, 0);
		userGrid.add(userLogoutButton, 2, 0);
		
		// Sets up the Song Queue List View
		songQueueView = new ListView<String>();
		songQueueView.setItems(songQueue);
		songQueueView.setPrefSize(100, 100);
		
		//SETUP SongView grid
		GridPane songGrid = new GridPane();
		songGrid.setAlignment(Pos.CENTER);
		songGrid.setHgap(5); songGrid.setVgap(5);;
		
		jukeboxViewer.setItems(allSongs);
		songGrid.add(jukeboxViewer, 0, 0); 
		songGrid.add(new Label("Song Queue:"), 0, 1);
		songGrid.add(songQueueView, 0, 2);
		
		//SETUP final User Jukebox view
		userJukeboxGrid = new GridPane();
		userJukeboxGrid.setAlignment(Pos.CENTER);
		userJukeboxGrid.setVgap(5);
		userJukeboxGrid.setHgap(5);
		
		userJukeboxGrid.add(userGrid, 0, 0);
		userJukeboxGrid.add(songGrid, 0, 3);
	}//END setUpUserJukeboxGrid()
	
	/**********************************************************************************************
	 * METHOD: refreshQueueView()
	 * PURPOSE: refresh the Queue View of Songs on the Queue
	 **********************************************************************************************/
	public static void refreshQueueView() {
		songQueue.clear();
		ArrayList<String> jukeboxSongQueue = jukebox.getSongQueue();
		for(int i = 0; i < jukeboxSongQueue.size(); i++) {
			songQueue.add(jukeboxSongQueue.get(i));
		}
		songQueueView.setItems(songQueue);
		songQueueView.refresh();
	}//END refreshQueueView()
	
	/**********************************************************************************************
	 * METHOD: writeFiles()
	 * PURPOSE: Sends messages to any objects that need to store information for persistence and
	 *     write out the necessary files.
	 **********************************************************************************************/
	public void writeFiles() {
		jukebox.writeQueueToFile();
		jukebox.writeSongsToFile();
	}//END writeFiles()
	
	/**********************************************************************************************
	 * CLASS: JukeboxViewer()
	 * PURPOSE: Handles a TableView that has the times played today, title, artist and length
	 **********************************************************************************************/
	private class JukeboxViewer extends TableView<Song> {
		
		@SuppressWarnings("unchecked")
		public JukeboxViewer() {
			// Add the columns to the Table
			TableColumn<Song, Integer> playsCol = new TableColumn<>("Plays");
			TableColumn<Song, String> titleCol = new TableColumn<>("Title");
			TableColumn<Song, String> artistCol = new TableColumn<>("Artist");
			TableColumn<Song, String> timeCol = new TableColumn<>("Time");
			
			// Add the cell values with the names of the instance variables
			playsCol.setCellValueFactory(new PropertyValueFactory<Song, Integer>("timesSelected"));
			titleCol.setCellValueFactory(new PropertyValueFactory<Song, String>("songTitle"));
			artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("songArtist"));
			timeCol.setCellValueFactory(new PropertyValueFactory<Song, String>("songTime"));
			
			this.getColumns().addAll(playsCol, titleCol, artistCol, timeCol);
			playsCol.setPrefWidth(50);
			titleCol.setPrefWidth(130);
			artistCol.setPrefWidth(100);
			timeCol.setPrefWidth(50);
			this.setMaxWidth(334);
			this.setMaxHeight(194);
		}//END JukeboxViewer()
		
	}//END JukeboxViewer class
	
	/**********************************************************************************************
	 * CLASS: ButtonListener()
	 * PURPOSE: Handles all button interactions in the JukeboxView.
	 **********************************************************************************************/
	public class ButtonListener implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			
			//Handle admin account buttons
			//Add an admin account
			if(event.getSource().equals(addAdminButton)) {
				String user = adminUsernameField.getText();
				String pass = adminPasswordField.getText();
				String message = LoginView.updateAccounts("account", user, pass, true);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(message);
				alert.showAndWait();
			}//Add a normal user account
			if(event.getSource().equals(addUserButton)) {
				String user = adminUsernameField.getText();
				String pass = adminPasswordField.getText();
				String message = LoginView.updateAccounts("account", user, pass, false);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(message);
				alert.showAndWait();
			}//Remove account
			if(event.getSource().equals(removeUserButton)) {
				String user = adminUsernameField.getText();
				String pass = adminPasswordField.getText();
				String message = LoginView.updateAccounts("remove", user, pass, false);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(message);
				alert.showAndWait();
			}
			
			//Handle song selection by admin
			if(event.getSource().equals(adminAddSongButton)) {
				Song songLine = adminJukeboxViewer.getSelectionModel().getSelectedItem();
				String songSelected = songLine.getSongTitle();
				String message = jukebox.checkSongAvailability(currentUser, songSelected, LocalDate.now());
				
				if(message.equals("Confirm")) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("The song '" + songSelected + "' has been added to the queue!");
					alert.showAndWait();
					adminJukeboxViewer.refresh(); // refresh the views
					refreshQueueView();
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(message);
					alert.showAndWait();
				}
			}
			
			//Handle song selection by normal user
			if(event.getSource().equals(userAddSongButton)) {
				Song songLine = jukeboxViewer.getSelectionModel().getSelectedItem();
				String songSelected = songLine.getSongTitle();
				String message = jukebox.checkSongAvailability(currentUser, songSelected, LocalDate.now());
				
				if(message.equals("Confirm")) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("The song '" + songSelected + "' has been added to the queue!");
					alert.showAndWait();
					jukeboxViewer.refresh(); // refresh the views
					refreshQueueView();
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(message);
					alert.showAndWait();
				}
			}
			
			//Handle logouts
			if(event.getSource().equals(adminLogoutButton) || event.getSource().equals(userLogoutButton)) {
				adminUsernameField.setText("");
				adminPasswordField.setText("");
				JukeboxStartGUI.setViewTo("login");
			}
		}//END handle()
		
	}//END ButtonListener class
	
}//END JukeboxView class