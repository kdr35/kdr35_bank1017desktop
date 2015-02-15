package edu.pitt.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.pitt.bank.Account;
import edu.pitt.bank.Customer;
import edu.pitt.bank.Security;
import edu.pitt.bank.TimerTaskLoginExpired;
import edu.pitt.bank.Transaction;
import edu.pitt.utilities.DbUtilities;
import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

import java.awt.SystemColor;
import java.awt.Font;

/**
 * Provides methods for: 
 * 1. Creating the application
 * 2. Initializing the contents of the frame
 */
public class AccountDetailsUI {

	private JFrame frame;
	private Customer accountOwner; //currently logged in customer
	private Account currentAccount; //current account selected in combo box - defaults to first account on list
	private ArrayList<String> listOfGroups; //list of groups the customer belongs to
	private ArrayList<String> listOfAccounts;
	JLabel lblAccounts;
	JComboBox cboAccounts; //combo box of customer's accounts
	JTextArea txtIntro;
	JTextArea txtType;
	JTextArea txtBalance;
	JTextArea txtInterestRate;
	JTextArea txtPenalty;
	JLabel lblAmount;
	private JTextField txtAmount;
	final int EXPIRED_LOGIN = 60000; //set login to expire after 60 seconds

	/**
	 * Create the application. Set accountOwner to the currently logged on customer.
	 * @param c the currently logged on customer
	 */
	public AccountDetailsUI(Customer c) {
		accountOwner = c; //set accountOwner to currently logged in customer 
		initialize();
		frame.setVisible(true);
		//start timer after frame initializes
		TimerTask timerTask = new TimerTaskLoginExpired(); //create new timer task
		Timer timer = new Timer(); //create new timer
		timer.schedule(timerTask, EXPIRED_LOGIN); //schedule timer to execute after 1 minute
	}

	/**
	 * Initialize the contents of the frame. Frame includes a greeting to the customer, a drop-down
	 * combo-box list of their accounts that they can choose, data associated with the chosen account, and the
	 * ability to withdraw or deposit and entered amount. The frame also includes a button to 
	 * show the transactions of the selected account from the combo-box or to exit the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Bank 1017 Account Details");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Intro at top of interface
		String intro = accountOwner.getFirstName() + " " + accountOwner.getLastName() + ", welcome to 1017 bank. You have the following permissions in this system: ";
		Security s = new Security(); //new security object
		listOfGroups = s.listUserGroups(accountOwner.getCustomerID()); //get the list of groups of the currently logged in customer
		int counter=1; //counter
		for(String group:listOfGroups){ //iterate through list of groups
			if(counter==1){ //if first iteration
				intro += group; //append group to intro string
			}
			else{ //if not first iteration
				intro += ", " + group; //append comma and group to intro string
			}
			counter++; //increment counter
		}
		txtIntro = new JTextArea(intro);
		txtIntro.setBackground(SystemColor.control);
		txtIntro.setWrapStyleWord(true);
		txtIntro.setLineWrap(true);
		txtIntro.setBounds(10, 3, 414, 36);
		frame.getContentPane().add(txtIntro);
		
		lblAccounts = new JLabel("Your Accounts:");
		lblAccounts.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lblAccounts.setBounds(10, 52, 105, 27);
		frame.getContentPane().add(lblAccounts);
		
		cboAccounts = new JComboBox();
		listOfAccounts = accountOwner.findCustomerAccounts(accountOwner);
		for(String account: listOfAccounts){
			cboAccounts.addItem(account);
		}
		//default current account is first account in combo box
		currentAccount = new Account(cboAccounts.getItemAt(0).toString());
		
		cboAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String accountNumber = cboAccounts.getSelectedItem().toString(); //store selected accountID in accountNumber
				currentAccount = new Account(accountNumber); //current account is selected accountID
				//update UI information based on selected accountID
				txtType.setText("Account Type: " + currentAccount.getType());
				txtBalance.setText("Balance: $" + currentAccount.getBalance());
				txtInterestRate.setText("Interest Rate: " + currentAccount.getInterestRate()*100 + "%");
				txtPenalty.setText("Penalty: $" + currentAccount.getPenalty());
				txtAmount.setText("0.00"); //reinitialize amount to 0
			}	
		});
		
		cboAccounts.setBounds(125, 52, 275, 27);
		frame.getContentPane().add(cboAccounts);
		
		txtType = new JTextArea("Account Type: " + currentAccount.getType());
		txtType.setBackground(SystemColor.control);
		txtType.setWrapStyleWord(true);
		txtType.setLineWrap(true);
		txtType.setBounds(10, 95, 186, 20);
		frame.getContentPane().add(txtType);
		
		txtBalance = new JTextArea("Balance: $" + currentAccount.getBalance());
		txtBalance.setBackground(SystemColor.control);
		txtBalance.setWrapStyleWord(true);
		txtBalance.setLineWrap(true);
		txtBalance.setBounds(10, 115, 186, 20);
		frame.getContentPane().add(txtBalance);
		
		txtInterestRate = new JTextArea("Interest Rate: " + currentAccount.getInterestRate()*100 + "%");
		txtInterestRate.setBackground(SystemColor.control);
		txtInterestRate.setWrapStyleWord(true);
		txtInterestRate.setLineWrap(true);
		txtInterestRate.setBounds(10, 135, 186, 20);
		frame.getContentPane().add(txtInterestRate);
		
		txtPenalty = new JTextArea("Penalty: $" + currentAccount.getPenalty());
		txtPenalty.setBackground(SystemColor.control);
		txtPenalty.setWrapStyleWord(true);
		txtPenalty.setLineWrap(true);
		txtPenalty.setBounds(10, 155, 186, 20);
		frame.getContentPane().add(txtPenalty);
		
		lblAmount = new JLabel("Amount:");
		lblAmount.setFont(new Font("Monospaced", Font.PLAIN, 12));
		lblAmount.setBounds(206, 95, 61, 28);
		frame.getContentPane().add(lblAmount);
		
		txtAmount = new JTextField("0.00");
		txtAmount.setBounds(277, 95, 123, 27);
		frame.getContentPane().add(txtAmount);
		txtAmount.setColumns(10);
		
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try { //execute if amount entered can parse to double
					double deposit = Double.parseDouble(txtAmount.getText()); //parse inputed string to Double datatype
					currentAccount.deposit(deposit); //deposit the amount
					txtBalance.setText("Balance: $" + currentAccount.getBalance()); //update displayed balance after deposit
					txtAmount.setText("0.00"); //set amount back to 0
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,  "Invalid Amount"); //if can't be parsed to Double, display message
					ErrorLogger.log(nfe.getMessage()); // Log error
				}
			}
		});
		
		btnDeposit.setBounds(216, 135, 90, 29);
		frame.getContentPane().add(btnDeposit);
		
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try { //execute if amount entered can parse to double
					double withdraw = Double.parseDouble(txtAmount.getText()); //parse inputed string to Double datatype
					currentAccount.withdraw(withdraw); //withdraw the amount
					txtBalance.setText("Balance: $" + currentAccount.getBalance()); //update displayed balance after withdraw
					txtAmount.setText("0.00"); //set amount back to 0
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,  "Invalid Amount"); //if can't be parsed to Double, display message
					ErrorLogger.log(nfe.getMessage()); // Log error
				}
			}
		});
		
		btnWithdraw.setBounds(310, 135, 90, 29);
		frame.getContentPane().add(btnWithdraw);
		
		JButton btnTransactions = new JButton("Show Transactions");
		btnTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//get a list of transactions from the current account
				ArrayList<Transaction> transactions = new ArrayList<Transaction>(currentAccount.getTransactionList());
				TransactionUI t = new TransactionUI(transactions); //create an interface of the user's selected account transactions
			}
		});
		
		btnTransactions.setBounds(160, 222, 160, 29);
		frame.getContentPane().add(btnTransactions);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				frame.dispose(); //dispose of account details frame if exit button is clicked
			}
		});
		
		btnExit.setBounds(330, 222, 70, 29);
		frame.getContentPane().add(btnExit);
		
	}

}
