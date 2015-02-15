package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.ErrorLogger;
import edu.pitt.utilities.MySqlUtilities;

/**
 * Provides methods for: 
 * 1. Loading Account Objects onto the Bank's accountList by retrieving data sets from MySQL database 
 * 2. Loading Customer Objects onto the Bank's customerList by retrieving data sets from MySQL database 
 *    and then setting the Customer Object as an Account Object's owner 
 * 3. Finding if a Customer is on the Bank Object's customerList
 * 4. Finding if an Account is on the Bank Object's accountList
 * 5. Getting Bank Object's account and customer lists.
 */
public class Bank {
	private ArrayList<Account> accountList = new ArrayList<Account>(); //list of all bank accounts
	private ArrayList<Customer> customerList = new ArrayList<Customer>(); //list of all bank customers
	
	/**
	 * Default constructor instantiates a Bank object. Loads all accounts
	 * and set the owners.
	 */
	public Bank(){
		loadAccounts(); 
		setAccountOwners();
	}
	
	/**
	 * This method loads the bank accounts by creating a SELECT statement for the 
	 * account table in the database. The method creates a connection to and queries 
	 * the database. The resulting data set is used to create new Account objects
	 * and add them to the Bank Object's accountList.
	 */
	private void loadAccounts() {
		//sql statement to get all account records 
		String sql = "SELECT * FROM account;";
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				Account a = new Account(rs.getString("accountID")); //create a new account object
				accountList.add(a); //add account object to account list
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
	 * This method sets the bank account owners by creating a SELECT statement to retrieve
	 * a list of all accounts and their respective customers. The method creates a connection 
	 * to and queries the database. The resulting data set is used to create new Customer objects
	 * and add them to the Bank Object's customerList (if not already on the list). The method
	 * also finds the account for each accountID in the resulting data set and adds the customer 
	 * to the Account Object's account owner list.  
	 */
	private void setAccountOwners() {
		//sql statement to retrieve a list of all accounts and their corresponding customers from the database
		String sql = "SELECT accountID, type, balance, interestRate, penalty, status, dateOpen,";
		sql += "customerID, lastName, firstName, ssn, streetAddress, city, state,";
		sql += "zip, loginName, pin ";
		sql += "FROM account ";
		sql += "JOIN customer_account ON accountId = fk_accountId ";
		sql += "JOIN customer ON fk_customerId = customerId;";
		//System.out.println(sql);
		DbUtilities db = new MySqlUtilities(); //connect to database
		try { //execute if able to get result set
			ResultSet rs = db.getResultSet(sql); //store the resulting data set from the sql query in rs
			while(rs.next()){ //while there is data in rs
				Customer c = new Customer(rs.getString("customerID")); //create a new customer object using customerID
				if(findCustomer(c.getCustomerID()) == null){ //if not already on customer list
					customerList.add(c); //add customer to list
				}
				Account a = findAccount(rs.getString("accountID")); //find the account of the accountID
				a.addAccountOwner(c); //add the customer to account owners list of the found account
				
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
	 * This method iterates through the Bank Object's customerList to determine if the 
	 * given customerID is on the list. 
	 * @param customerID
	 * @return the found Customer or null if not found
	 */
	public Customer findCustomer(String customerID){
		 for(int i=0;i<customerList.size();i++){ //iterate through customerList
	            if(customerList.get(i).getCustomerID().equals(customerID)){ //if passed customerID is on list
	            	return customerList.get(i); //return found customer
	            };
	     }
		 return null; //return null if not found
	}
	
	/**
	 * This method iterates through the Bank Object's accountList to determine if the 
	 * given accountID is on the list.  
	 * @param accountID
	 * @return the found account or null if not found
	 */
	public Account findAccount(String accountID){
		for(int i=0;i<accountList.size();i++){ //iterate through accountList
			 if(accountList.get(i).getAccountID().equals(accountID)){ //if passed accountID is on list
	            	return accountList.get(i); //return found account
	         };
		}
		return null; //return null if not found
	}

	/**
	 * Getter for accountList
	 * @return the accountList
	 */
	public ArrayList<Account> getAccountList() {
		return accountList;
	}

	/**
	 * Getter for customerList
	 * @return the customerList
	 */
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}
}
