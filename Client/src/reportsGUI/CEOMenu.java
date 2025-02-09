package reportsGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ClientGui.helpClass;
import client.ChatClient;
import client.ClientUI;
import entities.DeliveryBranch;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * Controller class for the CEO Menu page. This class handles user interactions
 * with the CEO Menu, including branch selection, report generation, and logging
 * out.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class CEOMenu {
	/**
	 * Helper class for screen management and other utilities.
	 */
	helpClass help = new helpClass();
	/**
	 * The chat client for server communication.
	 */
	public static ChatClient chatClient;
	/**
	 * Controller for handling monthly report operations.
	 */
	private MonthlyReportController reportController;
	/**
	 * A map to associate branch names with their IDs.
	 */
	private Map<String, String> branchStrings;
	/**
	 * ComboBox for selecting a branch.
	 */
	@FXML
	private ComboBox<String> cmbChooseBranch;
	/**
	 * Button for logging out of the application.
	 */
	@FXML
	private Button btnLogOut;
	/**
	 * Button for switching to quarterly report selection page.
	 */
	@FXML
	private Button btnQuarterly;
	/**
	 * Button for switching to monthly report selection page.
	 */
	@FXML
	private Button btnMonthly;
	/**
	 * Label for displaying branch selection error messages.
	 */
	@FXML
	private Label lblBranchError;

	/**
	 * Initializes the controller and populates the branch ComboBox with available
	 * branches. This method is called automatically by the JavaFX framework when
	 * the FXML is loaded.
	 */
	@FXML
	public void initialize() {
		reportController = MonthlyReportController.getInstance();
		reportController.getBranches();
		ArrayList<DeliveryBranch> branches = reportController.branches;
		branchStrings = new HashMap<>();
		for (DeliveryBranch b : branches)
			branchStrings.put(b.getName(), b.getID());
		cmbChooseBranch.setItems(FXCollections.observableArrayList(branchStrings.keySet()));
	}

	/**
	 * Handles the selection of a branch from the ComboBox. Currently a placeholder
	 * for future functionality.
	 */
	@FXML
	public void branchSelected() {
	}

	/**
	 * Handles the click event for the "Quarterly Report" button. Navigates to the
	 * Quarterly Report Selection page after validating the branch selection.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs while changing screens.
	 */
	public void btnQuarterlyReport(ActionEvent event) throws Exception {
		clearErrors();
		if (checkInput()) {
			QuarterlyReportSelectionPage.selectedBranchString = cmbChooseBranch.getValue();
			QuarterlyReportSelectionPage.selectedBranch = branchStrings.get(cmbChooseBranch.getValue());
			QuarterlyReportSelectionPage.branchStrings = branchStrings;
			help.changeScreen("/reportsGUI/QuarterlyReportSelectionPage.fxml", event); // Show the reports from DB
		} else
			lblBranchError.setText("Please select a branch.");

	}

	/**
	 * Handles the click event for the "Monthly Report" button. Navigates to the
	 * Monthly Report Selection page after validating the branch selection.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs while changing screens.
	 */
	public void btnMonthlyReport(ActionEvent event) throws Exception {
		clearErrors();
		if (checkInput()) {
			MonthlyReportSelectionPage.branch = branchStrings.get(cmbChooseBranch.getValue());
			help.changeScreen("/reportsGUI/MonthlyReportSelectionPage.fxml", event); // Show the reports from DB
		} else {
			lblBranchError.setText("Please select a branch.");
		}
	}

	/**
	 * Checks if a branch has been selected in the ComboBox.
	 * 
	 * @return True if a branch is selected, false otherwise.
	 */
	private boolean checkInput() {
		return !(cmbChooseBranch.getValue() == null);
	}

	/**
	 * Clears any error messages from the branch selection label.
	 */
	private void clearErrors() {
		lblBranchError.setText("");
	}

	/**
	 * Handles the click event for the "Log Out" button. Logs out the current user
	 * and navigates to the Login page.
	 * 
	 * @param event The action event triggered by clicking the button.
	 * @throws Exception If an error occurs while logging out or changing screens.
	 */
	public void btnLogOut(ActionEvent event) throws Exception {
		String toSend = "LoggedOut " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
		ClientUI.chat.accept(toSend);
		help.changeScreen("/ClientGui/LoginP.fxml", event);
	}
}