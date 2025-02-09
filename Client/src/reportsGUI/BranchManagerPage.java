package reportsGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ClientGui.helpClass;
import client.ClientUI;
/**
 * Controller class for the Branch Manager page.
 * This class handles user interactions with the Branch Manager page, including
 * navigating to other screens and logging out.
 * @author Daniel Feldman
 * @version August 2024
 */
public class BranchManagerPage {
	/**
     * Helper class for screen management and other utilities.
     */
	private helpClass help = new helpClass();
	/**
     * Button for navigating to the Approve Users page.
     */
	@FXML
	private Button btnApproveUser=null;
	/**
     * Button for navigating to the Reports page.
     */
	@FXML
	private Button btnReports=null;
	/**
     * Button for logging out of the application.
     */
	@FXML
	private Button btnLogOut=null;
	
	  /**
     * Handles the click event for the "Approve User" button.
     * Navigates to the Approve Users page.
     * 
     * @param event The action event triggered by clicking the button.
     * @throws Exception If an error occurs while changing screens.
     */
	public void btnApproveClick(ActionEvent event) throws Exception {
		help.changeScreen("/managerGUI/ApproveUsersPage.fxml", event);
	}
	
	
	/**
     * Handles the click event for the "Reports" button.
     * Navigates to the Monthly Report Selection page.
     * 
     * @param event The action event triggered by clicking the button.
     * @throws Exception If an error occurs while changing screens.
     */
	public void btnReportsClick(ActionEvent event) throws Exception {
		help.changeScreen("/reportsGUI/MonthlyReportSelectionPage.fxml", event);
	}
	
	/**
     * Handles the click event for the "Log Out" button.
     * Updates the logged-in status in the database and navigates to the Login page.
     * 
     * @param event The action event triggered by clicking the button.
     * @throws Exception If an error occurs while changing screens or sending data.
     */
	public void btnLogOutClick(ActionEvent event) throws Exception {
		String toSend = "LoggedOut " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
		ClientUI.chat.accept(toSend);
		help.changeScreen("/ClientGui/LoginP.fxml", event);
	}
}
