package entities;

import java.io.Serializable;

/**
 * This class represents a message between the server and the client. It
 * contains the type of the message and its content.
 * 
 * @author Daniel Feldman
 * @version 27-07-24
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Class variables *************************************************
	/**
	 * The message type, it could be to get a list of branches, or send a list of
	 * branches, etc.
	 */
	private MessageType type;

	/**
	 * The content of the message, such as ArrayLists, Order objects, etc.
	 */
	private Serializable content;

	/**
	 * The message type enumeration.
	 */
	public enum MessageType {
		/**
		 * Get a list of restaurants command.
		 */
		GET_RESTAURANTS,
		/**
		 * Send a list of restaurants command.
		 */
		SEND_RESTAURANTS,
		/**
		 * Send an order to the server
		 */
		CLIENT_SEND_ORDER,
		/**
		 * Get a list of all branch reports
		 */
		BRANCHMANAGER_GET_REPORTS,
		/**
		 * Get a list of all branch reports
		 */
		BRANCHMANAGER_SEND_REPORTS,
		/**
		 * Get a list of all branches
		 */
		CEO_GET_BRANCHES,
		/**
		 * Send a list of all branches
		 */
		CEO_SEND_BRANCHES,
		/**
		 * Get a list of all reports by using branch id
		 */
		CEO_GET_REPORTS,
		/**
		 * Send a list of all reports to the CEO
		 */
		CEO_SEND_REPORTS,
		/**
		 * Get a List of all available quarters to view
		 */
		CEO_GET_QUARTERS,
		/**
		 * Send a list of all available quarters.
		 */
		CEO_SEND_QUARTERS,
		/**
		 * Get a list of all unapproved users of given branch
		 */
		BRANCHMANAGER_GET_UNAPPROVED_USERS,
		/**
		 * Send a list of all unapproved users of given branch
		 */
		BRANCHMANAGER_SEND_UNAPPROVED_USERS,
		/**
		 * Approve a user by the id
		 */
		BRANCHMANAGER_APPROVE_USER, BRANCHMANAGER_APPROVE_USER_FAIL, BRANCHMANAGER_APPROVE_USER_SUCCESS

	}

	// Constructors ******************************************************
	/**
	 * Constructs a new Message with the specified type and content.
	 * 
	 * @param type    the type of the message
	 * @param content the content of the message
	 */
	public Message(MessageType type, Serializable content) {
		this.setType(type);
		this.setContent(content);
	}
	// Methods ***********************************************************

	/**
	 * Returns the type of the message.
	 * 
	 * @return the type of the message
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Sets the type of the message.
	 * 
	 * @param type the new type of the message
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * Returns the content of the message.
	 * 
	 * @return the content of the message
	 */
	public Serializable getContent() {
		return content;
	}

	/**
	 * Sets the content of the message.
	 * 
	 * @param content the new content of the message
	 */
	public void setContent(Serializable content) {
		this.content = content;
	}
}