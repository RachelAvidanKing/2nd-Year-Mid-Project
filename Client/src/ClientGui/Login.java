package ClientGui;

import client.ClientUI;
import entities.Client;
import entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This class handles the login page functionality. It allows users to enter
 * their username and password and navigate based on their role.
 */
public class Login {

	/** Helper class instance to handle common functionalities. */
	private helpClass help = new helpClass();

	/** Status of the login operation. */
	public static int status = -1;

	/** Delivery Branch associated with the user, if applicable. */
	public static String branch = "";
	/**discount for user*/
	public static String discount;
	
	/**Text field for username insertion*/
	@FXML
	private TextField UserName;
	
	/**Text field for password insertion*/
	@FXML
	private TextField Password;
	/**Confirm that the username and password insertion was correct*/
	@FXML
	private Button btnConfirm = null;
	@FXML
	private Button btnExit = null;
	/**Message if user doesn't exist or needs to be approved or is already logged in*/
	@FXML
	private Label message;
	/**String that indicates that all details needed for login check in server exist*/
	private static String str;

	/**
	 * Handles the event when the confirm button is clicked. Validates the input
	 * fields, sends a login request, and navigates based on the user role.
	 * 
	 * @param event The action event triggered by pressing the confirm button.
	 * @throws Exception if there is an error during the process.
	 */
	public void btnConfirmClick(ActionEvent event) throws Exception {
		helpClass.order=new Order();
		str="login";
		/**
		 * Checks if the username field is empty or incorrectly formatted.
		 */
		if (UserName.getText().trim().isEmpty()) {
			UserName.setText("Please enter username");
			str = "";
			return;
		} else {
			/**The username should split to first name and last name
			 * If not it is not inputed in correct format*/
			String[] arr = UserName.getText().split("\\s");
			if (arr.length == 1 || arr.length > 2) {
				UserName.setText("please enter username in the correct format");
				str = "";
				return;
			}
		}

		/**
		 * Checks if the password field is empty.
		 */
		if (Password.getText().trim().isEmpty()) {
			Password.setText("Please enter password");
			str = "";
			return;
		}
		

		/**
		 * Sends the login request to the server if both fields are valid.
		 */
		if (str.equals("login")) {
			String user = "login: " + UserName.getText() + " " + Password.getText();
			ClientUI.chat.accept(user);
			/**
			 * Processes the server response and navigates to the appropriate screen based
			 * on user role.
			 */
			switch (status) {
			case 0:
				/** Needs to be approved. */
				message.setText("needs to be approved!");
				break;
			case 1:
				/** User doesn't exist or wrong password. */
				message.setText("user doesn't exist!");
				str="";
				break;
			case 2:
				/** Customer. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "regular");
				helpClass.newClient.setDiscount(discount);
				help.changeScreen("/ClientGui/FirstOrderScreenO.fxml", event);
				break;
			case 3:
				/** Business. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "business");
				helpClass.newClient.setDiscount(discount);
				help.changeScreen("/ClientGui/FirstOrderScreenO.fxml", event);
				break;
			case 4:
				/** Qworker. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "qworker");
				helpClass.newClient.sethomeBranch(branch);
				help.changeScreen("/qworkergui/QWGuiO.fxml", event);
				break;
			case 5:
				/** Supplier. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "supplier");
				help.changeScreen("/supplier/SupplierGui.fxml", event); 
				break;
			case 6:
				/** CEO. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "ceo");
				help.changeScreen("/reportsGUI/CEOMenu.fxml", event);
				break;
			case 8:
				/** Delivery manager - add in chatClient after db updated. */
				helpClass.newClient = new Client(UserName.getText(), Password.getText(), "delivery manager");
				helpClass.newClient.sethomeBranch(branch);
				help.changeScreen("/reportsGUI/BranchManagerPage.fxml", event);
				break;
			case 9:
				/** Already logged in. */
				message.setText("Already logged in!");
				break;
			}
		}

		status = -1;
		branch = "";
		str="";
		return;
	}

	/**
	 * Handles the event when the exit button is clicked. Exits the application.
	 * 
	 * @param event The action event triggered by pressing the exit button.
	 * @throws Exception if there is an error during the process.
	 */
	public void btnExitClick(ActionEvent event) throws Exception {
		help.ExitBtn(event);
	}
}
