package server;

// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controllers.OrderController;
import controllers.QWController;
import controllers.ReportController;
import controllers.SupplierController;
import entities.Client;
import entities.DeliveryBranch;
import entities.Message;
import entities.Message.MessageType;
import entities.Order;
import entities.Report;
import entities.User;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import serverGUI.ServerPortFrame;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static Integer DEFAULT_PORT = 5555;
	/**connection to all the controllers*/
	public static OrderController oc;
	public static QWController qw;
	public static SupplierController sup;
	public static ReportController reportController;
	public static ServerPortFrame spf;
	public static String branch = "";
	/**map for all the clients connected*/
	public Map<ConnectionToClient, String> clientsMap = new HashMap<>();
	public static String serverIp;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		try {
			serverIp = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------
	// Report Maker code
	// ----------------------------------------------------------------
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void scheduleMonthlyTask() {
		scheduleNextMonthlyRun();
	}

	private void scheduleNextMonthlyRun() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextRun = now.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		long initialDelay = ChronoUnit.MILLIS.between(now, nextRun);
		scheduler.schedule(() -> {
			runMonthlyTask();
			scheduleNextMonthlyRun();
		}, initialDelay, TimeUnit.MILLISECONDS);
		System.out.println("Monthly run scheduled for " + nextRun);
	}

	private void runMonthlyTask() {
		// Since it happens at midnight, we would like it to set all reports of the day
		// before
		LocalDateTime today = LocalDateTime.now().minusDays(1);
		reportController.produceMonthlyReportTuples(today.getMonthValue(), today.getYear());
		System.out.println("Monthly run performed at " + LocalTime.now());
	}
	// ---------------------------------------------------
	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Message received: " + msg + " from" + client);
		try {
			// Check if the message is in byte array form
			if (msg instanceof byte[]) {
				// Deserialize byte array to Message object
				byte[] messageBytes = (byte[]) msg;
				ByteArrayInputStream byteStream = new ByteArrayInputStream(messageBytes);
				ObjectInputStream objectStream = new ObjectInputStream(byteStream);
				msg = objectStream.readObject();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (msg instanceof Message) {
			Message message = (Message) msg;
			Message ret;
			switch (message.getType()) {
			case GET_RESTAURANTS:
				ArrayList<String> restaurants = oc.importRestaurants();
				ret = new Message(MessageType.SEND_RESTAURANTS, restaurants);
				try {
					client.sendToClient(ret);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case CLIENT_SEND_ORDER:
				System.out.println("ORDER RECIEVED");
				System.out.println(message.getContent());
				try {
					client.sendToClient("Order Recieved");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case BRANCHMANAGER_GET_REPORTS:
				ArrayList<Report> reports = oc.importMonthlyReports((String) message.getContent());
				//System.out.println(reports);
				ret = new Message(MessageType.BRANCHMANAGER_SEND_REPORTS, reports);
				try {
					client.sendToClient(serialize(ret));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case BRANCHMANAGER_GET_UNAPPROVED_USERS:
				ArrayList<User> unapprovedUsers = reportController.importUnApprovedUsers((String) message.getContent());
				ret = new Message(MessageType.BRANCHMANAGER_SEND_UNAPPROVED_USERS, unapprovedUsers);
				try {
					client.sendToClient(serialize(ret));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case BRANCHMANAGER_APPROVE_USER:
				if (reportController.approveUser((User) message.getContent()))
					ret = new Message(MessageType.BRANCHMANAGER_APPROVE_USER_SUCCESS, "User approved successfully.");
				else
					ret = new Message(MessageType.BRANCHMANAGER_APPROVE_USER_FAIL, "Failed to approve user.");
				try {
					client.sendToClient(serialize(ret));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case CEO_GET_BRANCHES:
				ArrayList<DeliveryBranch> branches = reportController.importBranches();
				ret = new Message(MessageType.CEO_SEND_BRANCHES, branches);
				try {
					client.sendToClient(serialize(ret));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case CEO_GET_REPORTS:
				ArrayList<Report> reportsID = reportController
						.importMonthlyReportsBranchID((String) message.getContent());
				System.out.println(reportsID);
				ret = new Message(MessageType.BRANCHMANAGER_SEND_REPORTS, reportsID);
				try {
					client.sendToClient(serialize(ret));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;

			}
		}

		if (msg instanceof String) {
			ArrayList<String> data = new ArrayList<>();
			String message = (String) msg;
			String[] arr;

			// cartain messages are split with :
			if (message.startsWith("UpdateMenu:") || message.startsWith("addToMenu") || message.startsWith("Remove:")
					|| message.startsWith("streets:") || message.startsWith("showDish")
					|| message.startsWith("getRestaurantBranch")) {
				arr = message.split(":"); // Split by ":"
			} else { //other messages are split by space
				arr = message.split("\\s"); // Split by spaces for all other cases
			}
			try {
				switch (arr[0]) {
				case "ClientDisconnect":
					disconnect(client);
					break;
				case "login:":
					String branch_type = oc.checkLogin(arr[1], arr[2], arr[3]);
					client.sendToClient("login: " + branch_type);
					break;
				case "LoggedOut":
					oc.changeLoginStatus(arr[1], arr[2], arr[3], "0");
					break;
				case "showDish":
					data.add("dishes");
					data.addAll(qw.importMealData(arr[1], arr[2]));
					client.sendToClient(data);
					break;
				case "allMenu":
					data.add("dishes");
					data.addAll(qw.importMenu(arr[1]));
					client.sendToClient(data);
					break;
				case "UpdateMenu":
					qw.UpdateMenu(arr[1], arr[2], arr[3], arr[4], arr[5]);
					client.sendToClient(null);
					break;
				case "Remove":
					qw.deleteDishFromMenu(arr[1], arr[2]);
					client.sendToClient(null);
					break;
				case "addToMenu":
					int check = qw.addToMenu(arr[1], arr[2], arr[3], arr[4], arr[5]);
					String toSend = "addToMenu " + check;
					client.sendToClient(toSend);
					break;
				case "importCategories":
					data.add("categories");
					data.addAll(qw.importCategories());
					client.sendToClient(data);
					break;
				case "methods":
					data.add("methods");
					data.addAll(oc.importmethods());
					client.sendToClient(data);
					break;
				case "cities":
					data.add("cities");
					data.addAll(oc.importCities());
					client.sendToClient(data);
					break;
				case "streets":
					data.add("streets");
					data.addAll(oc.importStreets(arr[1]));
					client.sendToClient(data);
					break;
				case "importOpenOrders":
					data.add("openOrders");
					data.addAll(oc.importClientsOpenOrders(arr[1], arr[2], arr[3]));
					client.sendToClient(data);
					break;
				case "updateStatus":
					oc.updateStatus(arr[1], arr[2], arr[3], arr[4]);
					oc.updateDiscount(arr[5], arr[6], arr[7], arr[8]);
					client.sendToClient(null);
					break;
				case "getRestaurantBranch":
					String branch = "branch " + oc.getRestaurantBranch(arr[1], arr[2]);
					client.sendToClient(branch);
					break;
				case "importBranchOrders":
					data.add("importBranchOrder");
					data.addAll(sup.importBranchOrders(arr[1], arr[2]));
					client.sendToClient(data);
					break;
				case "ChangeStatusOfOrder":
					sup.UpdateStatusOfOrder(arr[1], arr[2], arr[3], arr[4]);
					client.sendToClient(null);
					break;
				case "getRestaurants":
					data.add("restaurantsList");
					data.addAll(oc.importRestaurants());
					client.sendToClient(data);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (msg instanceof ArrayList<?>) {
			ArrayList<Object> message = ((ArrayList<Object>) msg);

			try {
				oc.updateOrders((Client) message.get(1), (Order) message.get(2));
				client.sendToClient(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) // change main name to db??
	{
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	private byte[] serialize(Message msg) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteStream);
			out.writeObject(msg);
			out.flush();
			byte[] messageBytes = byteStream.toByteArray();
			return messageBytes;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		String clientIP = client.getInetAddress().getHostAddress();
		String clientHostName = client.getInetAddress().getHostName();
		String connectionStatus = "ClientIP: " + clientIP + " Client Host Name: " + clientHostName
				+ " status: connected";

		int ipExists = 0;
		ConnectionToClient existingClient = null;

		// Check if the IP address already exists in the map
		for (Map.Entry<ConnectionToClient, String> entry : clientsMap.entrySet()) {
			if (entry.getValue().contains(clientIP)) {
				ipExists = 1;
				existingClient = entry.getKey();
				break;
			}
		}

		if (ipExists == 0) {
			// If the IP address does not exist, add the new client
			clientsMap.put(client, connectionStatus);
		} else {
			// If the IP address exists, update the existing client's status
			clientsMap.put(existingClient, connectionStatus);
		}
		spf.printConnection(clientsMap);
	}

	protected void disconnect(ConnectionToClient client) {
		String clientIP = client.getInetAddress().getHostAddress();
		String clientHostName = client.getInetAddress().getHostName();
		String disconnectionStatus = "ClientIP: " + clientIP + " Client Host Name: " + clientHostName
				+ " status: disconnected";

		int ipExists = 0;
		ConnectionToClient existingClient = null;

		// Check if the IP address exists in the map
		for (Map.Entry<ConnectionToClient, String> entry : clientsMap.entrySet()) {
			if (entry.getValue().contains(clientIP)) {
				ipExists = 1;
				existingClient = entry.getKey();
				break;
			}
		}

		if (ipExists == 1) {
			// If the IP address exists, update the existing client's status to disconnected
			clientsMap.put(existingClient, disconnectionStatus);
		}

		spf.printConnection(clientsMap);
	}

}
