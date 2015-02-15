package edu.pitt.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.pitt.utilities.MySqlUtilities;
import edu.pitt.utilities.ErrorLogger;

public class Tester {

	public static void main(String[] args) {
		//Account account = new Account("checking", 1500);
		
		//Transaction t = new Transaction("1c54c5dc-b0a1-11e4-a38e-005056881e07");
		//System.out.println(t.getTransactionDate());
		
		//Customer c = new Customer("Harrison", "James", "123-45-6789", "jharrison", 1234,  "123 Penn Ave", "Pittsburgh", "PA", 15222);
		//Customer c = new Customer("01b9f986-5d41-11e3-94ef-97beef767f1d");
		//Security s = new Security();
		//ArrayList<String> groupList = s.listUserGroups(c.getCustomerID());
		//System.out.println("Groups:"); 
        //System.out.println("----------------");
        //for(int i=0;i<groupList.size();i++){
        //    System.out.println(groupList.get(i));
        //}
		//Account account = new Account("00ae9c2a-5d43-11e3-94ef-97beef767f1d");
		//Customer c = new Customer("15bce8aa-f9e9-479a-9ba5-a893bec24052");
		//Customer d = new Customer("01b9f986-5d41-11e3-94ef-97beef767f1d");
		//account.addAccountOwner(c);
		//account.addAccountOwner(d);
		//System.out.println(account.getBalance());
		//account.withdraw(5);
		//System.out.println(account.getBalance());
		
		Bank b = new Bank();
		ArrayList<Customer> a = b.getCustomerList();
		System.out.println("Customers:"); 
        System.out.println("----------------");
        for(int i=0;i<a.size();i++){
            System.out.println(a.get(i).getCustomerID());
        }
	}

}