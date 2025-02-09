package managerGUI;

import java.util.ArrayList;

import ClientGui.helpClass;
import entities.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import reportsGUI.MonthlyReportController;

/**
 * Controller class for the Approve Users Page in the manager GUI. Handles the
 * display and approval of users awaiting approval.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class ApproveUsersPage {
	helpClass help = new helpClass();
	ApproveUsersController approveUsersController;
	MonthlyReportController monthlyReportController;
	User selectedUser;

	@FXML
	private ImageView imgProfilePicture;
	@FXML
	private Label lblUserName;
	@FXML
	private Label lblUserRole;
	@FXML
	private Button btnBack;
	@FXML
	private TableView<User> usersTable;
	@FXML
	private Button btnApprove;
	@FXML
	private Label lblApproveMessage;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private TextField txtID;
	@FXML
	private TextField txtPhoneNumber;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<String> cmbAccountType;
	@FXML
	private TextField txtCreditCard;

	/**
	 * Initializes the Approve Users Page. Sets up the table, event listeners,
	 * and loads the necessary data.
	 */
	@FXML
	public void initialize() {

		// Initialize any necessary logic, event handlers, or data bindings
		ApproveUsersController.approveUsersPage = this;
		monthlyReportController = MonthlyReportController.getInstance();
		lblUserName.setText(helpClass.newClient.getUsername());
		lblUserRole.setText(helpClass.newClient.getClient_type());
		approveUsersController = ApproveUsersController.getInstance();
		approveUsersController.fetchUnApprovedUsers(helpClass.newClient.gethomeBranch()); // get the branch id
		// Load all customers into the table where their approved status is 0
		// Load Tableview
		populateTable();

		usersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				if (newValue != null) {
					// Call your custom function here
					handleSelectionChange(newValue);
				}
			}
		});
		txtFirstName.setDisable(true);
		txtLastName.setDisable(true);
		ArrayList<String> accountTypes = new ArrayList<>();
		accountTypes.add("Buisness");
		accountTypes.add("Regular");
		cmbAccountType.setItems(FXCollections.observableArrayList(accountTypes));
	}
	/**
	 * Handles the event when the back button is clicked.
	 * Navigates back to the previous screen.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If there is an error during navigation.
	 */
	@FXML
	private void btnBackClick(ActionEvent event) throws Exception {
		help.generalBtnBack("/reportsGUI/BranchManagerPage.fxml", event);
		// Logic to navigate back to the previous screen
	}
	/**
	 * Handles the event when the approve button is clicked.
	 * Validates the input fields and approves the selected user.
	 * 
	 * @param event The action event triggered by clicking the button.
	 */
	@FXML
	private void btnApproveClick(ActionEvent event) {
		selectedUser = usersTable.getSelectionModel().getSelectedItem();

		// approveUsersController.approveUser(String.valueOf(selectedUser.getID()));
		// Check input
		if (selectedUser == null) {
			lblApproveMessage.setText("Please select a user.");
			lblApproveMessage.setTextFill(Color.RED);
			// set a message to please select a user
		} else if (checkInput()) {
			selectedUser.setID(Integer.valueOf(txtID.getText()));
			selectedUser.setPhoneNum(txtPhoneNumber.getText());
			selectedUser.setEmail(txtEmail.getText());
			selectedUser.setUserType(cmbAccountType.getValue());
			selectedUser.setCreditCardNumber(txtCreditCard.getText());
			approveUsersController.approveUser(selectedUser);
		}
		// Send user to database
		// Logic to approve the selected user from the table
	}
	/**
	 * Handles the event when the account type is selected.
	 * Enables or disables the credit card field based on the account type.
	 * 
	 * @param event The action event triggered by selecting an account type.
	 */
	@FXML
	private void selectedAccountType(ActionEvent event) {
		String accountType = cmbAccountType.getValue();
		if (accountType.equals("Buisness")) {
			txtCreditCard.setDisable(true);
			txtCreditCard.setText("");
		} else
			txtCreditCard.setDisable(false);
	}
	/**
	 * Populates the users table with data of unapproved users.
	 */
	private void populateTable() {
		usersTable.getColumns().clear();
		// Create Columns
		TableColumn<User, Integer> colID = new TableColumn<>("User ID");
		TableColumn<User, String> colFirstName = new TableColumn<>("First Name");
		TableColumn<User, String> colLastName = new TableColumn<>("Last Name");
		TableColumn<User, String> colPhoneNum = new TableColumn<>("Phone Number");
		TableColumn<User, String> colEmail = new TableColumn<>("E-Mail");
		TableColumn<User, String> colUserType = new TableColumn<>("User Type");
		TableColumn<User, String> colCreditCard = new TableColumn<>("Credit Card");

		// Set cell value factories
		colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colUserType.setCellValueFactory(new PropertyValueFactory<>("userType"));
		colCreditCard.setCellValueFactory(new PropertyValueFactory<>("creditCardNumber"));

		// Add columns to TableView
		usersTable.getColumns().addAll(colID, colFirstName, colLastName, colPhoneNum, colEmail, colUserType,
				colCreditCard);

		// Get data (assuming unapprovedUsers is a static list or fetched elsewhere)
		ArrayList<User> unapprovedUsers = ApproveUsersController.unapprovedUsers;

		// Convert ArrayList to ObservableList
		ObservableList<User> data = FXCollections.observableArrayList(unapprovedUsers);
		usersTable.setItems(data);
	}
	/**
	 * Displays a success message when a user is successfully approved.
	 * Updates the table to reflect the approval.
	 */
	void setApproveMessageSuccess() {
		Platform.runLater(() -> {
			lblApproveMessage.setTextFill(Color.GREEN);
			lblApproveMessage
					.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName() + " was approved!");
			refreshTable();
		});
	}
	/**
	 * Displays a failure message if a user cannot be approved.
	 * Updates the table to reflect the failure.
	 */
	void setApproveMessageFail() {
		Platform.runLater(() -> {
			lblApproveMessage.setTextFill(Color.RED);
			lblApproveMessage.setText("Error: Couldn't approve user...");
			refreshTable();
		});
	}
	/**
	 * Refreshes the users table by fetching the latest data from the database.
	 */
	private void refreshTable() {
		approveUsersController.fetchUnApprovedUsers(helpClass.newClient.gethomeBranch()); // Get info from DB
		populateTable(); // Insert info into table
	}
	/**
	 * Handles the event when a user is selected in the table.
	 * Displays the selected user's details in the appropriate fields.
	 * 
	 * @param selectedUser The user that was selected in the table.
	 */
	private void handleSelectionChange(User selectedUser) {
		// Replace with your desired functionality
		System.out.println("Selected: " + selectedUser.getFirstName());
		txtFirstName.setText(selectedUser.getFirstName());
		txtLastName.setText(selectedUser.getLastName());
		txtID.setText(String.valueOf(selectedUser.getID()));
		txtPhoneNumber.setText(selectedUser.getPhoneNum());
		txtEmail.setText(selectedUser.getEmail());
		String accountType = selectedUser.getUserType();
		txtCreditCard.setText(selectedUser.getCreditCardNumber());
		cmbAccountType.setValue(selectedUser.getUserType());

	}
	/**
	 * Validates the input fields for the user approval process.
	 * Ensures that all required fields are correctly formatted.
	 * 
	 * @return true if all inputs are valid, false otherwise.
	 */
	private boolean checkInput() {
		String tmp;
		lblApproveMessage.setText("");

		// Validate ID (9 digits)
		tmp = txtID.getText();
		if (tmp == null || tmp.isEmpty() || !tmp.matches("\\d{9}")) {
			lblApproveMessage.setText("Please enter a valid ID (9 digits).");
			lblApproveMessage.setTextFill(Color.RED);
			return false;
		}

		// Validate Phone Number (Format: 05X-XXXXXXX)
		tmp = txtPhoneNumber.getText();
		if (tmp == null || !tmp.matches("05\\d{1}-\\d{7}")) {
			lblApproveMessage.setText("Please enter a valid Phone Number (Format: 05X-XXXXXXX).");
			lblApproveMessage.setTextFill(Color.RED);
			return false;
		}

		// Validate Email (Basic email validation)
		tmp = txtEmail.getText();
		if (tmp == null || !tmp.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$")) {
			lblApproveMessage.setText("Please enter a valid Email Address (Format: example@mail.com");
			lblApproveMessage.setTextFill(Color.RED);
			return false;
		}

		// Validate Credit Card if Account Type is Regular (Format: XXXX-XXXX-XXXX-XXXX)
		if ("Regular".equals(cmbAccountType.getValue())) {
			tmp = txtCreditCard.getText();
			if (tmp == null || !tmp.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
				lblApproveMessage.setText("Please enter a valid Credit Card Number (Format: XXXX-XXXX-XXXX-XXXX).");
				lblApproveMessage.setTextFill(Color.RED);
				return false;
			}
		}

		// If all checks pass
		return true;
	}

}