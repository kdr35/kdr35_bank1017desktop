package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * Provides methods for: 
 * 1. Instantiating Customer Objects by retrieving data sets from MySQL database 
 * 2. Instantiating Customer Objects and inserting object data into MySQL database
 * 3. Finding all accounts associated with a given customer
 * 4. Getting/Setting Customer object properties
 */
public class Customer {
	private String customerID; //unique id for customer object
	private String firstName; //first name of customer
	private String lastName; //last name of customer
	private String ssn; //SSN of customr
	private String streetAddress; //street address of customer	
	private String city; //city customer lives in
	private String state; //state customer lives in
	private int zip; //zip where customer lives
	private String loginName; //login name of customer
	private int pin; //pin for login 
	
	/**
	 * Constructor to instantiate a Customer Object by creating a SELECT 
     * statement for the given customerID. The method creates a connection to
     * and queries the database. The resulting data set is used 
     * to initialize the properties of the Customer Object  
	 * @param customerID identification number(PK) of a record in the customer table
	 */
	public Customer(String customerID){
		//sql statement to get customer record of the passed customerID
		String sql = "SELECT * FROM customer "; 
		sql += "WHERE customerID = '" + customerID + "'";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				//Set instance variables
				this.customerID = rs.getString("customerID");
				this.firstName = rs.getString("firstName");
				this.lastName = rs.getString("lastName");
				this.ssn = rs.getString("ssn");
				this.streetAddress = rs.getString("streetAddress");
				this.city = rs.getString("city");
				this.state = rs.getString("state");
				this.zip = rs.getInt("zip");
				this.loginName = rs.getString("loginName");
				this.pin = rs.getInt("pin");
			}
			db.closeDbConnection(); //close database connection
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLogger.log(e.getMessage()); // Log error
			ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
		}	
	}
	
	/**
	 * Constructor to instantiate a Customer Object. The constructor also uses the passed 
     * parameters to create a new entry in the transaction table of a database by creating 
     * an INSERT statement, connecting to the database, and executing the INSERT statement. 
     * The customerID property is initialized by creating a universally unique identifier. 
     * The other properties in this method are initialized by the passed parameters. 
	 * @param lastName last name of customer
	 * @param firstName first name of customer
	 * @param ssn social security number of customer
	 * @param loginName customer login name
	 * @param pin customer pin
	 * @param streetAddress customer street address
	 * @param city customer city	
	 * @param state customer state
	 * @param zip customer zip
	 */
	public Customer(String lastName, String firstName, String ssn, String loginName, int pin,  String streetAddress, String city, String state, int zip){
		//Set instance variables
		this.customerID = UUID.randomUUID().toString(); //create a universally unique identifier for customerID
		this.lastName = lastName;
		this.firstName = firstName;
		this.ssn = ssn;
		this.loginName = loginName;
		this.pin = pin;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zip = zip;
		//sql statement to insert new record
		String sql = "INSERT INTO customer ";
		sql += "(customerID, lastName, firstName, ssn, loginName, pin, streetAddress, city, state, zip) ";
		sql += " VALUES ";
		sql += "('" + this.customerID + "', ";
		sql += "'" + this.lastName + "', ";
		sql += "'" + this.firstName + "', ";
		sql += "'" + this.ssn + "', ";
		sql += "'" + this.loginName + "', ";
		sql += this.pin + ", ";
		sql += "'" + this.streetAddress + "', ";
		sql += "'" + this.city + "', ";
		sql += "'" + this.state + "', ";
		sql += this.zip + ");";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities(); //connect to database
		db.executeQuery(sql); //execute sql statement on database
		db.closeDbConnection(); //close database connection
	}
	
	/**
	 * This method finds and returns a list of all accounts associated with the given customer.
	 * @param c
	 * @return
	 */
	public ArrayList<String> findCustomerAccounts(Customer c){
		ArrayList<String> accountList = new ArrayList<String>();
		//sql statement to get accounts of the currently logged in customer
		String sql = "SELECT * FROM customer "; 
		sql += "JOIN customer_account ON customerId = fk_customerId ";
		sql += "JOIN account ON fk_accountId = accountId ";
		sql += "WHERE customerId = '" + c.getCustomerID() + "';";
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				accountList.add(rs.getString("accountID")); //add account to accountList
			}
			db.closeDbConnection(); //close database connection
		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLogger.log(e.getMessage()); //Log error
		}
		return accountList; //return list of accounts
	}
	
	/**
	 * Getter for customerID
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}
	
	/**
	 * Getter for firstName
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter for firstName
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for lastName
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter for lastName
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter for social security number
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * Setter for social security number
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * Getter for streetAddress
	 * @return the streetAddress
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * Setter for streetAddress
	 * @param streetAddress the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * Getter for city
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Setter for city
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Getter for state
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Setter for state
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Getter for zip
	 * @return the zip
	 */
	public int getZip() {
		return zip;
	}

	/**
	 * Setter for zip
	 * @param zip the zip to set
	 */
	public void setZip(int zip) {
		this.zip = zip;
	}

	/**
	 * Getter for loginName
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Setter for loginName
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * Getter for pin
	 * @return the pin
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * Setter for pin
	 * @param pin the pin to set
	 */
	public void setPin(int pin) {
		this.pin = pin;
	}

}
