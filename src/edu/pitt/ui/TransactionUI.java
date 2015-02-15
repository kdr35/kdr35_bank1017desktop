package edu.pitt.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import edu.pitt.bank.Transaction;

import java.awt.Component;

import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

import java.awt.SystemColor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ListSelectionModel;

/**
 * Provides methods for: 
 * 1. Creating the application
 * 2. Initializing the contents of the frame
 */
public class TransactionUI {

	private JFrame frame;
	private JTable tblTransaction;
	private ArrayList<Transaction> accountTransactions;
	private JScrollPane transactionPane;
	
	/**
	 * Create the application. Set accountTransactions to the passed list of transactions
	 * for the selected account.
	 * @param transactions the list of transactions for selected account
	 */
	public TransactionUI(ArrayList<Transaction> transactions) {
		accountTransactions = transactions; //set to the passed list of transactions for selected account
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame. Frame includes a list of transactions
	 * for the selected account from the AccountDetailsUI and a button to close the 
	 * frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Bank 1017 Account Transactions");
		frame.setBounds(200, 200, 750, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		transactionPane = new JScrollPane();
		transactionPane.setBounds(51, 23, 631, 450);
		frame.getContentPane().add(transactionPane);
		
		DefaultTableModel transactionList = new DefaultTableModel();
		//create column headers
		transactionList.setColumnIdentifiers(new String[] { "Type","Transaction Date", "Amount", "New Balance" });
		//set row count based on the number of transaction on accountTransactions list
		transactionList.setRowCount(accountTransactions.size());
		int index=0; //counter
		for(Transaction transaction: accountTransactions){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //format to display
			//populate transactionList
			transactionList.setValueAt("  " +transaction.getType().toString(), index, 0);
			transactionList.setValueAt("  " + dateFormat.format(transaction.getTransactionDate()), index, 1);
			transactionList.setValueAt("  $" + transaction.getAmount(), index, 2);
			transactionList.setValueAt("  $" + transaction.getBalance(), index, 3);
			index++; //increment counter
        }
		tblTransaction = new JTable(transactionList); //create table from transactionList
		tblTransaction.setFillsViewportHeight(true);
		tblTransaction.setSelectionBackground(SystemColor.inactiveCaption);
		tblTransaction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//set column widths
		tblTransaction.getColumnModel().getColumn(0).setPreferredWidth(125);
		tblTransaction.getColumnModel().getColumn(1).setPreferredWidth(250);
		tblTransaction.getColumnModel().getColumn(2).setPreferredWidth(125);
		tblTransaction.getColumnModel().getColumn(3).setPreferredWidth(125);
		tblTransaction.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tblTransaction.setRowHeight(25);
		tblTransaction.setAutoCreateRowSorter(true); 
		tblTransaction.getTableHeader().setReorderingAllowed(false);
		tblTransaction.getRowSorter().toggleSortOrder(1); //sort list based on TransactionDate column
		//align headers
		DefaultTableCellRenderer header = new DefaultTableCellRenderer();
		header.setHorizontalAlignment(DefaultTableCellRenderer.LEFT); 
		header.setVerticalAlignment(DefaultTableCellRenderer.CENTER);
		header.setOpaque(false);
		for(int i=0; i<tblTransaction.getColumnCount();i++){
			tblTransaction.getColumnModel().getColumn(i).setHeaderRenderer(header);
		}
		//alternate row colors
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		if (defaults.get("Table.alternateRowColor") == null)
		    defaults.put("Table.alternateRowColor", new Color(230,230,250));
		transactionPane.setViewportView(tblTransaction);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				frame.dispose(); //dispose of frame if close button hit
			}
		});
		
		btnClose.setBounds(599, 505, 70, 29);
		frame.getContentPane().add(btnClose);
	}
}
