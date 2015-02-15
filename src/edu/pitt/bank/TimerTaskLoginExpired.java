package edu.pitt.bank;

import java.awt.Window;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import edu.pitt.ui.LoginUI;

/**
 * Provides methods for: 
 * 1. Running a scheduled task to show that the session has expired, close all windows, and restart login process
 */
public class TimerTaskLoginExpired extends TimerTask{
	
	/**
	 * This method runs after a set time period. The method will display a message that the session has expire. 
	 * Also, the method will close all open windows, collect the garbage, and reinitialize the login process. The garbage
	 * collection helps for exiting the program from the re-initialized login screen and closing windows after 
	 * subsequent timer expirations following re-login. 
	 */
	@Override
	public void run() {
		for(Window window: Window.getWindows()){ //dispose of all open frames
		    window.dispose();
		}
		System.gc(); //collect garbage 
		JOptionPane.showMessageDialog(null,  "Your session has expired - please login again!"); //show expiration message
		//System.exit(0); //close the application
		LoginUI.main(null); //call LoginUI main method to bring up login screen
	}

}
