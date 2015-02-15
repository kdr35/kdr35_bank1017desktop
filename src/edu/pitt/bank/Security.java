package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * Provides methods for: 
 * 1. Validating entered loginName and pin values from the LoginUI  
 * 2. Finding the groups a Customer belongs to
 */
public class Security {
	private String userID; 
	
	/**
	 * This method validates a login by creating a SELECT statement for the given 
	 * loginName and pin. The method creates a connection to and queries the database.
	 * If the resulting data set contains a customer record associated with the loginName
	 * and pin, a new Customer object is created based on the found customerID before
	 * being returned. If there is not a record associated with the login/name and pin,
	 * null is returned.
	 * @param loginName the entered login name 
	 * @param pin the entered pin
	 * @return the valid Customer Object or null if not valid
	 */
	public Customer validateLogin(String loginName, int pin){
		//sql statement to get the customer record where the passed loginName and pin match
		String sql = "SELECT * FROM customer ";
		sql += "WHERE loginName ='" + loginName + "' AND pin=" + pin + ";";
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				Customer c = new Customer(rs.getString("customerID")); //create customer object with found customerID
				return c; //return valid Customer
			}
			db.closeDbConnection(); //close database connection
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLogger.log(e.getMessage()); // Log error
			ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
		}
		return null; //return null if not valid
	}
	
	/**
	 * This method finds a Customer's groups by creating a SELECT statement for the given 
	 * userID. The method creates a connection to and queries the database. The resulting data 
	 * set is used to add the Customer's group names to the ArrayList groupList. The groupList is 
	 * then returned by the method. 
	 * @param userID the Customer identifier
	 * @return the groupList the list of groups the user belongs to
	 */
	public ArrayList<String> listUserGroups(String userID){
		ArrayList<String> groupList = new ArrayList<String>(); //create ArrayList to hold groups 
		//sql statement to get groups of passed userID
		String sql = "SELECT * FROM user_permissions ";
		sql += "JOIN groups ON user_permissions.groupID = groups.groupID ";
		sql += "WHERE groupOrUserID ='" + userID + "';";
		DbUtilities db = new MySqlUtilities(); //connect to database
		try {//execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				groupList.add(rs.getString("groupName")); //add group name to list
			}
			db.closeDbConnection(); // close database connection
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLogger.log(e.getMessage()); // Log error
			ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
		}
		return groupList; //return user's list of groups
	}
}
