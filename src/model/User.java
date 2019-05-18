package model;

import java.io.Serializable;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: User
 * DESCRIPTION:
 * A User object containing the data fields: username, password, admin status, total time balance
 * remaining in the account in seconds, and the number of songs selected (current date by day).
 **************************************************************************************************/

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private boolean isAdmin;
	private int timeRemaining; //seconds
	private int songsSelected;
	
	/**********************************************************************************************
	 * METHOD: User()
	 * PURPOSE: Constructs a new User with the given username, password, admin status, time
	 *     balance (seconds), and the number of total songs selected.
	 * @param user The String username to be stored.
	 * @param pass The String of this user's password.
	 * @param admin The boolean flag of user's admin status.
	 * @param time The integer (in seconds) of this user's time balance.
	 * @param selected The integer number of song's this user has selected.
	 **********************************************************************************************/
	public User(String user, String pass, boolean admin, int time, int selected) {
		username = user;
		password = pass;
		isAdmin = admin;
		timeRemaining = time;
		songsSelected = selected;
	}//END User() constructor
	
	/**********************************************************************************************
	 * METHOD: getUsername()
	 * PURPOSE: Returns this User's username (used for testing).
	 * @return This user's username.
	 **********************************************************************************************/
	public String getUsername() {
		return username;
	}//END getUsername()
	
	/**********************************************************************************************
	 * METHOD: getPassword()
	 * PURPOSE: Returns this User's password.
	 * @return This user's password.
	 **********************************************************************************************/
	public String getPassword() {
		return password;
	}//END getPassword()
	
	/**********************************************************************************************
	 * METHOD: updateInfo()
	 * PURPOSE: Updates the existing User's password and admin access to the given values.
	 * @param pass The Sting of this user's new password to store.
	 * @param admin The boolean flag of this user's new admin status to store.
	 **********************************************************************************************/
	public void updateInfo(String pass, boolean admin) {
		password = pass;
		isAdmin = admin;
	}//END updateInfo()
	
	/**********************************************************************************************
	 * METHOD: isAdmin()
	 * PURPOSE: Returns the boolean value of whether this User is an admin or not.
	 * @return This user's admin status (true = admin).
	 **********************************************************************************************/
	public boolean isAdmin() {
		return isAdmin;
	}//END isAdmin()
	
	/**********************************************************************************************
	 * METHOD: getTimeRemaining()
	 * PURPOSE: Returns how many seconds this User has remaining in their time balance.
	 * @return This user's total available time selected in seconds.
	 **********************************************************************************************/
	public int getTimeRemaining() {
		return timeRemaining;
	}//end getTimeRemaining()
	
	/**********************************************************************************************
	 * METHOD: getSongCount()
	 * PURPOSE: Returns the count of how many songs this User has selected today.
	 * @return The number of songs this user has selected.
	 **********************************************************************************************/
	public int getSongCount() {
		return songsSelected;
	}//END getSongCount()
	
	/**********************************************************************************************
	 * METHOD: incrementSongCount()
	 * PURPOSE: Updates this User's playable time balace and the number of songs they have selected
	 *     today in total.
	 * @param time The integer song play time in seconds to deduct from this user's time balance.
	 **********************************************************************************************/
	public void selectSong(int time) {
		timeRemaining = timeRemaining - time;
		songsSelected++;
	}//END incrementSongCount()
	
	/**********************************************************************************************
	 * METHOD: resetCount()
	 * PURPOSE: Resets the number of times this song has been selected to 0.
	 **********************************************************************************************/
	public void resetCount() {
		songsSelected = 0;
	}//END resetCount()
	
	/**********************************************************************************************
	 * METHOD: toString()
	 * PURPOSE: Returns a String representing this User for file format.
	 **********************************************************************************************/
	//public String toString() {
	//	return ""+username+"\t"+password+"\t"+isAdmin+"\t"+(timeRemaining/60)+"\t"
	//			+(timeRemaining%60)+"\t"+songsSelected;
	//}//END toString()
	
}//END User class