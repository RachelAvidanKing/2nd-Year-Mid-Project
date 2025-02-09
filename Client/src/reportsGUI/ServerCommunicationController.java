package reportsGUI;

import java.io.Serializable;
import java.util.ArrayList;

import client.ChatClient;
import entities.DeliveryBranch;
import entities.Message;
import entities.Report;
import entities.User;
import managerGUI.ApproveUsersController;

/**
 * Singleton class responsible for handling communication between the client and
 * server. It manages sending and receiving messages and updating various
 * controllers with received data.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class ServerCommunicationController {
	/**
	 * Singleton instance of the ServerCommunicationController.
	 */
	private static ServerCommunicationController instance;
	/**
	 * ChatClient instance used for communication with the server.
	 */
	public static ChatClient dbController;
	/**
	 * MonthlyReportController instance for managing monthly reports.
	 */
	public static MonthlyReportController monthlyReportController;
	/**
	 * ApproveUsersController instance for managing user approval processes.
	 */
	public static ApproveUsersController approveUsersController;
	/**
	 * List of reports received from the server.
	 */
	private ArrayList<Report> reports;
	/**
	 * List of delivery branches received from the server.
	 */
	private ArrayList<DeliveryBranch> branches;
	/**
	 * List of unapproved users received from the server.
	 */
	private ArrayList<User> unapprovedUsers;

	/**
	 * Private constructor to enforce singleton pattern.
	 */
	private ServerCommunicationController() {
		monthlyReportController = MonthlyReportController.getInstance();
		MonthlyReportController.scc = this;
		approveUsersController = ApproveUsersController.getInstance();
		ApproveUsersController.scc = this;
	}

	/**
	 * Gets the singleton instance of ServerCommunicationController.
	 * 
	 * @return The singleton instance of ServerCommunicationController.
	 */
	public static ServerCommunicationController getInstance() {
		if (instance == null)
			instance = new ServerCommunicationController();
		return instance;
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param type    The type of the message.
	 * @param content The content of the message.
	 */
	public void SendMessageToServer(Message.MessageType type, Serializable content) {
		Message message = new Message(type, content);
		dbController.handleMessageFromClientUI(message);
	}

	/**
	 * Sets the reports list received from the server.
	 * 
	 * @param content The serialized content containing the reports.
	 */
	@SuppressWarnings("unchecked")
	public void setReports(Serializable content) {
		if (content instanceof ArrayList) {
			reports = (ArrayList<Report>) content;
			MonthlyReportController.reports.addAll(reports);
		} else
			System.out.println("Wrong type received by the server.");
	}

	/**
	 * Sets the branches list received from the server.
	 * 
	 * @param content The serialized content containing the branches.
	 */
	@SuppressWarnings("unchecked")
	public void setBranches(Serializable content) {
		if (content instanceof ArrayList<?>) {
			branches = (ArrayList<DeliveryBranch>) content;
			MonthlyReportController.branches.addAll(branches);
		} else
			System.out.println("Wrong type received by the server.");
	}

	/**
	 * Sets the unapproved users list received from the server.
	 * 
	 * @param content The serialized content containing the unapproved users.
	 */
	@SuppressWarnings("unchecked")
	public void setUnApprovedUsers(Serializable content) {
		if (content instanceof ArrayList<?>) {
			unapprovedUsers = (ArrayList<User>) content;
			System.out.println(unapprovedUsers);
			ApproveUsersController.unapprovedUsers.addAll(unapprovedUsers);
		}
	}

	/**
	 * Updates the ApproveUsersController with a success message for user approval.
	 * 
	 * @param content The serialized success message.
	 */
	public void setApprovedMessageSuccess(Serializable content) {
		if (content instanceof String)
			approveUsersController.setApprovedMessageSuccess((String) content);
		else
			System.out.println("Wrong type received by the server.");
	}

	/**
	 * Updates the ApproveUsersController with a failure message for user approval.
	 * 
	 * @param content The serialized failure message.
	 */
	public void setApprovedMessageFail(Serializable content) {
		if (content instanceof String)
			approveUsersController.setApprovedMessageFail((String) content);
		else
			System.out.println("Wrong type received by the server.");
	}
}
