package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * Provides methods for: 
 * 1. Instantiating Transaction Objects by retrieving data sets from MySQL database 
 * 2. Instantiating Transaction Objects and inserting object data into MySQL database
 * 3. Getting Transaction object properties
 */
public class Transaction {
	private String transactionID; //unique id for transaction object
	private String accountID; //identifier for account object
	private String type; //type of transaction
	private double amount; //amount used in transaction
	private double balance; //balance after transaction
	private Date transactionDate; //date of transaction
	
	
	/**
	 * Constructor to instantiate a Transaction Object by creating a SELECT 
     * statement for the given transactionID. The method creates a connection to
     * and queries the database. The resulting data set is used 
     * to initialize the properties of the Transaction Object 
	 * @param transactionID identification number(PK) of a record in the transaction table
	 */
	public Transaction(String transactionID){
		//sql statement to get transaction record of the passed transactionID
		String sql = "SELECT * FROM transaction "; 
		sql += "WHERE transactionID = '" + transactionID + "'";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				//Set instance variables
				this.transactionID = rs.getString("transactionID");
				this.accountID = rs.getString("accountID");
				this.type = rs.getString("type");
				this.amount = rs.getDouble("amount");
				this.balance = rs.getDouble("balance");
				//this.transactionDate = new Date();
				String dateTime = rs.getString("transactionDate"); //get date in the database field format
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //format required for database transactionDate field
				try { //execute if able to parse dateTime
					this.transactionDate = dateFormat.parse(dateTime); //parse into java date format
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ErrorLogger.log(e.getMessage()); // Log error
				}
				
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
     * Constructor to instantiate a Transaction Object. The constructor also uses the passed 
     * parameters to create a new entry in the transaction table of a database by creating 
     * an INSERT statement, connecting to the database, and executing the INSERT statement. 
     * The transactionID property is initialized by creating a universally unique identifier. 
     * The other properties in this method are initialized by the passed parameters and through
     * the creation of a Date() object.
	 * @param accountID identification number(PK) of a record in the transaction table
	 * @param type type of transaction
	 * @param amount amount of transaction
	 * @param balance balance after transaction
	 */
	public Transaction(String accountID, String type, double amount, double balance){
		//Set instance variables
		this.transactionID = UUID.randomUUID().toString(); //create a universally unique identifier for transactionID
		this.type = type;
		this.amount = amount;
		this.accountID = accountID;
		this.balance = balance;
		this.transactionDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //format required for database transactionDate field
		//sql statement to insert new record
		String sql = "INSERT INTO transaction ";
		sql += "(transactionID, accountID, amount, transactionDate, type, balance) ";
		sql += " VALUES ";
		sql += "('" + this.transactionID + "', ";
		sql += "'" + this.accountID + "', ";
		sql += this.amount + ", ";
		sql += "'" + dateFormat.format(this.transactionDate)+ "', "; //format object transactionDate into required database transactionDate format
		sql += "'" + this.type + "', ";
		sql += this.balance + ");";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities(); //connect to database
		db.executeQuery(sql); //execute sql statement on database
		db.closeDbConnection(); //close database connection
	}
	
	/**
	 * Getter for transactionID
	 * @return the transactionID
	 */
	public String getTransactionID() {
		return transactionID;
	}

	/**
	 * Getter for amount
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Getter for transactionDate
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Getter for transaction type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter for balance
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Getter for accountID
	 * @return the accountID
	 */
	public String getAccountID() {
		return accountID;
	}

	
}
