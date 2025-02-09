package managerGUI;

import java.util.ArrayList;

import entities.Message.MessageType;
import entities.User;
import reportsGUI.ServerCommunicationController;

/**
 * Controller class for handling the approval of users in the manager GUI. This
 * class manages communication with the server and interacts with the
 * ApproveUsersPage to update the UI.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class ApproveUsersController {
	/**
	 * The singleton instance of ApproveUsersController.
	 */
	private static ApproveUsersController instance;
	/**
	 * A list of users who are not yet approved.
	 */
	public static ArrayList<User> unapprovedUsers = new ArrayList<>();
	/**
	 * The controller responsible for handling communication with the server.
	 */
	public static ServerCommunicationController scc;
	/**
	 * The page that displays the UI for approving users.
	 */
	static ApproveUsersPage approveUsersPage;

	/**
	 * Private constructor to enforce singleton pattern.
	 */
	private ApproveUsersController() {
	}

	/**
	 * Returns the singleton instance of ApproveUsersController. If the instance
	 * does not exist, it creates one.
	 * 
	 * @return The singleton instance of ApproveUsersController.
	 */
	public static ApproveUsersController getInstance() {
		if (instance == null)
			instance = new ApproveUsersController();
		return instance;
	}

	/**
	 * Fetches the list of unapproved users for a given branch from the server.
	 * Clears the current list and sends a request to the server to retrieve the
	 * latest data.
	 * 
	 * @param branch The branch ID for which to fetch unapproved users.
	 */
	public void fetchUnApprovedUsers(String branch) {
		unapprovedUsers.clear();
		scc.SendMessageToServer(MessageType.BRANCHMANAGER_GET_UNAPPROVED_USERS, branch);
	}

	/**
	 * Sends a request to the server to approve the specified user.
	 * 
	 * @param user The user to be approved.
	 */
	public void approveUser(User user) {
		scc.SendMessageToServer(MessageType.BRANCHMANAGER_APPROVE_USER, user);
	}

	/**
	 * Displays a success message in the UI when a user is successfully approved.
	 * 
	 * @param message The success message to be displayed.
	 */
	public void setApprovedMessageSuccess(String message) {
		approveUsersPage.setApproveMessageSuccess();
	}

	/**
	 * Displays a failure message in the UI if a user cannot be approved.
	 * 
	 * @param message The failure message to be displayed.
	 */
	public void setApprovedMessageFail(String message) {
		approveUsersPage.setApproveMessageFail();
	}
}
