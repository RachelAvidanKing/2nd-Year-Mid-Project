package client;

import ocsf.client.*;
import qworkergui.addDish;
import reportsGUI.ServerCommunicationController;
import supplier.SupplierPage;
import common.*;
import entities.Message;
import entities.MessageFinishOrder;
import entities.branchOrder;
import entities.orderPlaced;
import javafx.collections.FXCollections;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import ClientGui.ApproveOrderClient;
import ClientGui.Login;
import ClientGui.OrderPage;
import ClientGui.SupplyMethod;
import ClientGui.helpClass;

/**
 * The ChatClient class extends the AbstractClient class and provides 
 * additional functionality for a client in a client-server architecture. 
 * It handles communication between the client and the server, processes 
 * messages from the server, and sends messages to the server.
 * 
 * <p>This class is designed to work with the OCSF framework, allowing for 
 * the implementation of a client that can connect to a server, send and 
 * receive messages, and interact with a user interface.</p>
 * 
 * @see ocsf.client.AbstractClient
 * 
 * @author Orian Haziza
 * @author Keren Kazatchinski
 * @author Daniel Feldman
 * @version August 2024
 */
public class ChatClient extends AbstractClient {
	// Instance variables ****************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
	ChatIF clientUI;
	/**
     * A flag indicating whether the client is currently waiting for a response
     * from the server.
     */
	public static boolean awaitResponse = false;
	/**
     * A reference to the ServerCommunicationController instance used for 
     * communication with the server.
     */
	public static ServerCommunicationController scc;

	// Constructors ******************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @throws IOException
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		scc = ServerCommunicationController.getInstance();
		ServerCommunicationController.dbController = this;
		openConnection();
	}
	// Instance methods ****************

    /**
     * This method handles all data that comes in from the server. It processes 
     * messages based on their type and updates the relevant data structures or 
     * UI components.
     *
     * @param msg The message from the server. Can be of various types including 
     *            Message, ArrayList<String>, or String.
     */
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof byte[])
			msg = deserialize(msg);
		awaitResponse = false;
		if (msg instanceof Message) {
			Message message = (Message) msg;
			switch (message.getType()) {
			case BRANCHMANAGER_SEND_REPORTS:
				scc.setReports(message.getContent());
				break;
			case BRANCHMANAGER_SEND_UNAPPROVED_USERS:
				scc.setUnApprovedUsers(message.getContent());
				break;
			case CEO_SEND_BRANCHES:
				scc.setBranches(message.getContent());
				break;
			case BRANCHMANAGER_APPROVE_USER_SUCCESS:
				scc.setApprovedMessageSuccess(message.getContent());
				break;
			case BRANCHMANAGER_APPROVE_USER_FAIL:
				scc.setApprovedMessageFail(message.getContent());
				break;
			default:
				break;
			}
		}

		if (msg instanceof ArrayList<?>) {
			ArrayList<String> message = ((ArrayList<String>) msg);
			String str=message.get(0);
			int len=message.size();
			switch(str)
			{
			case "restaurantsList":
				OrderPage.restaurants=new ArrayList<>();
				for(int i=1; i<len;i++)
				{
					OrderPage.restaurants.add(message.get(i));
				}
				break;
			case "dishes":
				helpClass.dishes=new ArrayList<String>();
				for(int i=1; i<len;i++)
				{
					helpClass.dishes.add(message.get(i));
				}
				break;
			case "categories":
				addDish.categoriesList=new ArrayList<String>();
				for(int i=1; i<len;i++)
				{
					addDish.categoriesList.add(message.get(i));
				}
				break;
			case "methods":
				SupplyMethod.methodsList=new ArrayList<String>();
				for(int i=1; i<len;i++)
				{
					if (!helpClass.newClient.getClient_type().equalsIgnoreCase("Regular")||!message.get(i).equalsIgnoreCase("joint")){
						SupplyMethod.methodsList.add(message.get(i));
					}
				}
				break;
			case "cities":
				SupplyMethod.citiesList=new ArrayList<String>();
				SupplyMethod.cities = FXCollections.observableArrayList();
				for(int i=1; i<len;i++)
				{
					SupplyMethod.citiesList.add(message.get(i));
				}
				break;
			case "streets":
				SupplyMethod.streetsList=new ArrayList<String>();
				SupplyMethod.streets = FXCollections.observableArrayList();
				for(int i=1; i<len;i++)
				{
					SupplyMethod.streetsList.add(message.get(i));
				}
				break;
			case "openOrders":
				ApproveOrderClient.orders=FXCollections.observableArrayList();
				ApproveOrderClient.readyOrders=FXCollections.observableArrayList();
				String[] arr;
				orderPlaced order;
				for(int i=1; i<len;i++)
				{
					arr= message.get(i).split("\\/");
					order=new orderPlaced(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]);
					if(arr[1].equals("Ready"))
					{
						ApproveOrderClient.readyOrders.add(arr[0]);
					}
					ApproveOrderClient.orders.add(order);
				}
				break;
			case "importBranchOrder":
				helpClass.ordernumber=new ArrayList<String>();
				helpClass.messageDetails=new ArrayList<>();
				SupplierPage.branchorders=FXCollections.observableArrayList();
				String[] arr1;
				branchOrder brancho;
				MessageFinishOrder finish;
				for(int i=1; i<len;i++)
				{
					arr1= message.get(i).split("\\/");
					brancho=new branchOrder(arr1[0],arr1[1],arr1[2]);
					finish=new MessageFinishOrder(arr1[3],arr1[4],arr1[5],arr1[6],arr1[0],arr1[2]);
					helpClass.ordernumber.add(message.get(i));
					helpClass.messageDetails.add(finish);
					SupplierPage.branchorders.add(brancho);
				}
				
				break;
			}
				
		}
		if (msg instanceof String) {
			String str = (String) msg; // turn object to string
			String[] arr = str.split("\\s");
			switch (arr[0]) {
			case "branch":
				SupplyMethod.branch=arr[1];
				break;
			case "login:":
				switch (arr[1]) 
				{
				case "NotApproved":
					Login.status = 0;
					break;
				case "Regular":
					Login.status = 2;
					Login.discount=arr[3];
					break;
				case "None":
					Login.status = 1;
					break;
				case "Buisness":
					Login.status = 3;
					Login.discount=arr[3];
					break;
				case "Qworker":
					Login.status = 4;
					Login.branch = arr[2];
					break;
				case "Supplier":
					Login.status = 5;
					break;
				case "CEO":
					Login.status = 6;
					break;
				case "WBranchManager":
					Login.status = 8;
					Login.branch = arr[2];
					break;
				case "AlreadyLoggedIn":
					Login.status = 9;
					break;
				}
			case "addToMenu":
				addDish.addDishStatus=arr[1];
				
			}
		}
	}

	/**
     * This method handles all data coming from the UI and sends it to the server.
     *
     * @param myMsg The message from the UI. Can be either a Message object or a String.
     */
	public void handleMessageFromClientUI(Object myMsg) {
		try {
			awaitResponse = true;

			if (myMsg instanceof Message) {
				// Serialize the Message object into a byte array
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(byteStream);
				out.writeObject(myMsg);
				out.flush();
				byte[] messageBytes = byteStream.toByteArray();
				// Send the byte array to the server
				sendToServer(messageBytes);
			} else {
				sendToServer(myMsg);
				
			}

			// wait for response
			if (myMsg instanceof String) {
				if (myMsg.equals("ClientDisconnect"))
					return;
				if (((String) myMsg).startsWith("LoggedOut")) {
					awaitResponse = false;
				}

			}
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server. Terminating client.");
			e.printStackTrace();
			quit();
		}
	}

	private Object deserialize(Object msg) {
		try {
			// Deserialize byte array to Message object
			byte[] messageBytes = (byte[]) msg;
			ByteArrayInputStream byteStream = new ByteArrayInputStream(messageBytes);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
			msg = objectStream.readObject();
			return msg;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class