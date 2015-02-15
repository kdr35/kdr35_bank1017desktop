package edu.pitt.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.pitt.bank.Customer;
import edu.pitt.bank.Security;
import edu.pitt.utilities.ErrorLogger;

/**
 * Provides methods for: 
 * 1. Launching the application 
 * 2. Creating the application
 * 3. Initializing the contents of the frame
 */
public class LoginUI {

	private JFrame frmBankLogin;
	JLabel lblLoginName;
	private JTextField txtLogin;
	JLabel lblPassword;
	private JTextField txtPassword;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frmBankLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					ErrorLogger.log(e.getMessage()); // Log error
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame. Frame includes text fields to 
	 * enter a login name and password and buttons to either submit or exit
	 * the application.
	 */
	private void initialize() {
		frmBankLogin = new JFrame();
		frmBankLogin.setTitle("Bank 1017 Login");
		frmBankLogin.setBounds(100, 100, 450, 300);
		frmBankLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankLogin.getContentPane().setLayout(null);
		
		lblLoginName = new JLabel("Login Name:");
		lblLoginName.setBounds(25, 27, 95, 16);
		frmBankLogin.getContentPane().add(lblLoginName);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(25, 71, 82, 16);
		frmBankLogin.getContentPane().add(lblPassword);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(121, 21, 296, 28);
		frmBankLogin.getContentPane().add(txtLogin);
		txtLogin.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(121, 65, 296, 28);
		frmBankLogin.getContentPane().add(txtPassword);
		txtLogin.setColumns(10);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String loginName = txtLogin.getText(); //store entered login text into loginName
				try { //execute if entered password text is of integer values
					int pin = Integer.parseInt(txtPassword.getText()); //store integer values into pin
					Security s = new Security(); //create new security object
					//validate login name and pin and store returned customer if valid or null if not valid in c
					Customer c = s.validateLogin(loginName, pin);  
					if(c != null){ //if login is valid
						AccountDetailsUI ad = new AccountDetailsUI(c); //create an interface with the user's account details
						//frmBankLogin.setVisible(false);
						frmBankLogin.dispose(); //dispose of the login frame
					}
					else{ //if login is not valid
						JOptionPane.showMessageDialog(null,  "Invalid Login"); //show invalid message
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null,  "Invalid Login"); //show invalid message if pin is not valid
					ErrorLogger.log(nfe.getMessage()); // Log error
				}
			}
		});
		
		btnLogin.setBounds(250, 105, 70, 29);
		frmBankLogin.getContentPane().add(btnLogin);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				frmBankLogin.dispose(); //dispose of login frame
			}
		});
		
		btnExit.setBounds(330, 105, 70, 29);
		frmBankLogin.getContentPane().add(btnExit);
		
	}

}
