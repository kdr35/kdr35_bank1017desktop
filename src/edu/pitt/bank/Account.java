package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * Provides methods for: 
 * 1. Instantiating Account Objects by retrieving data sets from MySQL database 
 * 2. Instantiating Account Objects and inserting object data into MySQL database
 * 3. Withdrawing an amount from the account
 * 4. Depositing an amount into the account
 * 5. Updating the account balance associated with the Account Object's accountID in the database
 * 6. Creating transaction against the account and adding them to the transactionList  
 * 7. Getting/Setting Account appropriate object properties
 */
public class Account {
	private String accountID; //unique id for account object
	private String type; //type of account
	private double balance; //balance of account
	private double interestRate; //interest rate of account
	private double penalty; //penalty amount of account
	private String status; //staus of account
	private Date dateOpen; //date account was opened
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>(); //list of transactions for account object
	private ArrayList<Customer> accountOwners = new ArrayList<Customer>(); //list of account owners for account object
	
	/**
	 * Constructor to instantiate an Account Object by creating a SELECT 
     * statement for the given accountID. The method creates a connection to
     * and queries the database. The resulting data set is used 
     * to initialize the properties of the Account Object. The method also
     * populates the transactionList property of the Account Object by creating a SELECT 
     * statement for the given accountID in the transactions table. The method creates a connection to
     * and queries the database. The resulting data set is used to create 
     * transaction objects and add them to the transactionList for the Account Object.
	 * @param accountID identification number(PK) of a record in the account table
	 */
	public Account(String accountID){
		//sql statement to get account record of the passed accountID
		String sql = "SELECT * FROM account "; 
		sql += "WHERE accountID = '" + accountID + "'";
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				//Set instance variables
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.balance = rs.getDouble("balance");
				this.interestRate = rs.getDouble("interestRate");
				this.penalty = rs.getDouble("penalty");
				this.status = rs.getString("status");
				//this.dateOpen = new Date();
				String dateTime = rs.getString("dateOpen"); //get date in the database field format
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //format required for database transactionDate field
				try { //execute if able to parse dateTime
					this.dateOpen = dateFormat.parse(dateTime); //parse into java date format
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ErrorLogger.log(e.getMessage()); // Log error
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ErrorLogger.log(e.getMessage()); // Log error
			ErrorLogger.log(sql); // Log INSERT, UPDATE, DELETE query
		}
		//sql statement to get transaction records of the passed accountID
		sql = "SELECT * FROM transaction "; 
		sql += "WHERE accountID = '" + accountID + "'";
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				createTransaction(rs.getString("transactionID")); //create a transaction for each transactionID
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
	 * Constructor to instantiate an Account Object. The constructor also uses the passed 
     * parameters to create a new entry in the account table of a database by creating 
     * an INSERT statement, connecting to the database, and executing the INSERT statement. 
     * The accountID property is initialized by creating a universally unique identifier. 
     * The other properties in this method are initialized by the passed parameters,
     * setting them equal to static values, and through the creation of a Date() object.
	 * @param accountType the type of account
	 * @param initialBalance the initial balance when account is instantiated
	 */
	public Account(String accountType, double initialBalance){
		this.accountID = UUID.randomUUID().toString(); //create a universally unique identifier for accountID
		this.type = accountType;
		this.balance = initialBalance;
		this.interestRate = 0;
		this.penalty = 0;
		this.status = "active";
		this.dateOpen = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //format required for database transactionDate field
		//sql statement to insert new record
		String sql = "INSERT INTO account ";
		sql += "(accountID,type,balance,interestRate,penalty,status,dateOpen) ";
		sql += " VALUES ";
		sql += "('" + this.accountID + "', ";
		sql += "'" + this.type + "', ";
		sql += this.balance + ", ";
		sql += this.interestRate + ", ";
		sql += this.penalty + ", ";
		sql += "'" + this.status + "', ";
		sql += "'" + dateFormat.format(this.dateOpen)+ "');"; //format object dateOpen into required database dateOpen format
		//sql += "CURDATE());";
		DbUtilities db = new MySqlUtilities(); //connect to database
		db.executeQuery(sql); //execute sql statement on database
		db.closeDbConnection(); //close database
	}
	
	/**
	 * Method to withdraw the given amount from the Account Object's balance. The method
	 * also calls the createTransaction method to record the withdrawal transaction and 
	 * add it to the transactionList of the Account Object. The method also calls the 
	 * updateDatabaseAccountBalance() method to update the balance associated with the 
	 * Account Object's accountID in the database
	 * @param amount the amount to withdrawal from the account
	 */
	public void withdraw(double amount){
		this.balance -= amount; //subtract amount from account balance
		//createTransaction(this.accountID, this.type, amount, this.balance);
		createTransaction(this.accountID, "withdraw", amount, this.balance); //create a transaction
		updateDatabaseAccountBalance(); //update account balance in database
	}
	
	/**
	 * Method to deposit the given amount to the Account Object's balance. The method
	 * also calls the createTransaction method to record the deposit transaction and 
	 * add it to the transactionList of the Account Object. The method also calls the 
	 * updateDatabaseAccountBalance() method to update the balance associated with the 
	 * Account Object's accountID in the database
	 * @param amount the amount to withdrawal from the account
	 */
	public void deposit(double amount){
		this.balance += amount; //add amount to account balance
		//createTransaction(this.accountID, this.type, amount, this.balance);
		createTransaction(this.accountID, "deposit", amount, this.balance); //create a transaction 
		updateDatabaseAccountBalance(); //update account balance in database
	}
	
	/**
	 * This method updates the account balance associated with the Account Object's accountID 
	 * in the database by creating an UPDATE statement, connecting to the database, and 
	 * executing the UPDATE statement.
	 */
	private void updateDatabaseAccountBalance(){
		//sql statement to update balance in database from current object balance
		String sql = "UPDATE account SET balance = " + this.balance + " ";
		sql += "WHERE accountID = '" + this.accountID + "';";
		DbUtilities db = new MySqlUtilities(); //connect to database
		db.executeQuery(sql); //execute sql statement on database
		db.closeDbConnection(); //close database
	}
	
	/**
	 * This method creates a new transaction object by calling the Transaction class
	 * constructor with the given transactionID. The method then adds the new transaction
	 * to the Account Object's transactionList before returning the new Transaction Object.
	 * @param transactionID
	 * @return the created Transaction Object
	 */
	private Transaction createTransaction(String transactionID){
		Transaction t = new Transaction(transactionID); //instantiate a new transaction with passed transactionID
		transactionList.add(t); //add newly instantiated transaction to account transaction list
		return t; //return transaction
	}
	
	/**
	 * This method creates a new transaction object by calling the Transaction class
	 * constructor with the given accountID, transaction type, transaction amount, and 
	 * resulting balance. The method then adds the new transaction to the Account Object's 
	 * transactionList before returning the new Transaction Object. 
	 * @param accountID the identifier of the account the transaction takes place on 
	 * @param type the type of transaction
	 * @param amount the amount of the transaction
	 * @param balance the resulting balance
	 * @return the created Transaction Object
	 */
	private Transaction createTransaction(String accountID, String type, double amount, double balance){
		Transaction t = new Transaction(accountID, type, amount, balance); //instantiate a new transaction with passed parameters
		transactionList.add(t); //add newly instantiated transaction to account transaction list
		return t; //return transaction
	}
	
	/**
	 * This method adds the given Customer Object to the Account Object's accountOwners list.
	 * @param accountOwner the owner to add to the account
	 */
	public void addAccountOwner(Customer accountOwner){
		accountOwners.add(accountOwner); //add the passed customer to the account owners list	
	}
	
	/**
	 * Getter for accountID
	 * @return the accountID
	 */
	public String getAccountID(){
		return this.accountID;
	}
	
	/**
	 * Getter for account balance
	 * @return the balance
	 */
	public double getBalance(){
		return this.balance;
	}

	/**
	 * Getter for accountOwners list
	 * @return the accountOwners
	 */
	public ArrayList<Customer> getAccountOwners() {
		return accountOwners;
	}

	/**
	 * Getter for account type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter for account type
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter for interestRate
	 * @return the interestRate
	 */
	public double getInterestRate() {
		return interestRate;
	}

	/**
	 * Setter for interestRate
	 * @param interestRate the interestRate to set
	 */
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * Getter for penalty
	 * @return the penalty
	 */
	public double getPenalty() {
		return penalty;
	}

	/**
	 * Setter for penalty
	 * @param penalty the penalty to set
	 */
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	/**
	 * Getter for transactionList
	 * @return the transactionList
	 */
	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}

	/**
	 * Getter for account status
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Setter for account status
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * Getter for dateOpen
	 * @return the dateOpen
	 */
	public Date getDateOpen() {
		return dateOpen;
	}

}
