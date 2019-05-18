package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**************************************************************************************************
 * @author Ashley Mains and Victor Gomes
 * 
 * CLASS: AllAccounts
 * DESCRIPTION:
 * AllAccounts keeps track of all valid user information in a map of all User obejcts accessed by
 * the String username.
 **************************************************************************************************/

public class AllAccounts {
	
	private ArrayList<User> userData;
	private Map<String, User> allUsers;
	
	/**********************************************************************************************
	 * METHOD: AllAccounts()
	 * PURPOSE: Constructs and AllAccounts object that keeps track of valid user information.
	 * @param doReadFiles The boolean flag to determine whether to read files or not.
	 **********************************************************************************************/
	@SuppressWarnings("unchecked")
	public AllAccounts(boolean doReadFiles) {
		userData = new ArrayList<User>(0);
		allUsers = new HashMap<String, User>();
		
		if(doReadFiles) {
			try { //Read file of all current valid users
				FileInputStream fileStream = new FileInputStream("AccountList");
				ObjectInputStream objectStream = new ObjectInputStream(fileStream);
				userData = (ArrayList<User>) objectStream.readObject();
				objectStream.close();
				
				for(int i=0; i<userData.size(); i++) {
					allUsers.put(userData.get(i).getUsername(), userData.get(i));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			resetToDefault();
		}
	}//END AllAccounts() constructor
	
	/**********************************************************************************************
	 * METHOD: getUser()
	 * PURPOSE: Returns the User object and corresponding information under the given username.
	 * @param username The String username to look up.
	 * @return The User under the given 'username'.
	 **********************************************************************************************/
	public User getUser(String username) {
		return allUsers.get(username);
	}//END getUser()
	
	/**********************************************************************************************
	 * METHOD: verifyAccount()
	 * PURPOSE: Checks the given a user's login information (username, password) if they are a
	 *     valid User in this system (account is on file).
	 * @param username The String username of the account to verify.
	 * @param password The String password of the matching account.
	 * @return True is the account has been verified; false if not valid.
	 **********************************************************************************************/
	public boolean verifyAccount(String username, String password) {
		User user = allUsers.get(username);
		if(user != null) {
			if(user.getPassword() != null && password.equals(user.getPassword())) {
				return true;
			}
		}
		return false;
	}//END verifyAccount()
	
	/**********************************************************************************************
	 * METHOD: isAdmin()
	 * PURPOSE: Checks if the User under the given username is an admin or not.
	 * @param username The String username of the account to verify.
	 * @return True if the account is an admin, otherwise false.
	 **********************************************************************************************/
	public boolean isAdmin(String username) {
		return allUsers.get(username).isAdmin();
	}//END isAdmin()
	
	/**********************************************************************************************
	 * METHOD: addAccount()
	 * PURPOSE: Adds a new user account to the map of all accounts.
	 * @param username The String username of the account to add.
	 * @param password The String password to the matching account.
	 * @param isAdmin The boolean flag whether the account is an admin or not.
	 **********************************************************************************************/
	public void addAccount(String username, String password, boolean isAdmin) {
		User newUser = new User(username, password, isAdmin, 60*1500, 0);
		userData.add(newUser);
		allUsers.put(username, newUser);
	}//END addAccount()
	
	/**********************************************************************************************
	 * METHOD: removeAccount()
	 * PURPOSE: Removes any valid user from the maps of users stored. The system admin "Merlin" is
	 *     not allowed to be deleted.
	 * @param username The String username of the account to remove.
	 * @return True if the account can be / has been removed, false if the account does not exist.
	 **********************************************************************************************/
	public boolean removeAccount(String username) {
		if(username.equals("Merlin") == false) {
			User u1 = allUsers.remove(username);
			return (u1 != null);
		}
		return false;
	}//END removeAccount()
	
	/**********************************************************************************************
	 * METHOD: resetCounts()
	 * PURPOSE: On a new day, reset all User's song selection counts to 0.
	 **********************************************************************************************/
	public void resetCounts() {
		Set<String> keySet = allUsers.keySet();
		for(String key : keySet) { allUsers.get(key).resetCount(); }
	}//END resetCounts()
	
	/**********************************************************************************************
	 * METHOD: resetToDefault()
	 * PURPOSE: Completely resets to system to starting default values.
	 **********************************************************************************************/
	private void resetToDefault() {
		allUsers.clear();
		User chris = new User("Chris", "1", false, 60*1500, 0);
		User devon = new User("Devon", "22", false, 60*1500, 0);
		User river = new User("River", "333", false, 60*1500, 0);
		User ryan = new User("Ryan", "4444", false, 60*1500, 0);
		User merlin = new User("Merlin", "7777777", true, 60*1500, 0);
		userData.add(chris); userData.add(devon); userData.add(river); userData.add(ryan); userData.add(merlin);
		allUsers.put(chris.getUsername(), chris);
		allUsers.put(devon.getUsername(), devon);
		allUsers.put(river.getUsername(), river);
		allUsers.put(ryan.getUsername(), ryan);
		allUsers.put(merlin.getUsername(), merlin);
	}//END resetToDefault()
	
	/**********************************************************************************************
	 * METHOD: writeAccountsToFile()
	 * PURPOSE: Writes out the current list of users to the file "AccountList".
	 **********************************************************************************************/
	public void writeAccountsToFile() {
		try {
			FileOutputStream fileOutput = new FileOutputStream("AccountList");
			ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
			objectOutput.writeObject(userData);
			objectOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//END writeQueueToFile()
	
}//END AllAccounts class