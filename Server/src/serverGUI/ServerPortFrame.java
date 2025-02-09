package serverGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import controllers.OrderController;
import controllers.QWController;
import controllers.ReportController;
import controllers.SupplierController;
import server.ServerUI;

/**
 * class for the server's gui
 * 
 *
 */
public class ServerPortFrame extends Application {
	public static String str = "";

	@FXML
	private Button btnConnect = null;
	@FXML
	private Button btnExit = null;
	@FXML
	private TextField textMessage;
	@FXML
	private TextField dbname;
	@FXML
	private TextField password;
	@FXML
	private TextField serverip;
	@FXML
	private TextArea txtClientConnection;


	ServerPortFrame controller;

	
	/**
	 * Establishes a connection to the database and initializes various controllers.
	 * <p>
	 * This method is triggered by an action event (e.g., a button click). It performs the following steps:
	 * <ul>
	 *   <li>Checks if there is an existing error state and clears it if necessary.</li>
	 *   <li>Validates user input for the database schema name and password.</li>
	 *   <li>Attempts to establish a connection using the provided schema name and password.</li>
	 *   <li>If successful, initializes various controllers and starts the server.</li>
	 *   <li>Displays appropriate messages based on the connection status.</li>
	 * </ul>
	 * </p>
	 *
	 * @param event The action event that triggered this method. This parameter is typically 
	 *              provided by the UI framework and contains information about the source of the event.
	 * @throws Exception If an error occurs during the connection process.
	 */
	public void Connect(ActionEvent event) throws Exception {
		textMessage.setText("");
		if (str.equals("error")) {
			textMessage.setText("Couldn't connected!");
			
			str = "";
			return;
		}
		str += "connect";
		if (dbname.getText().trim().equals("")) {
			textMessage.setText("Please enter scheme name.");

			str = "";
			return;
		}

		if (password.getText().isEmpty()) {
			textMessage.setText("Please enter a password.");

			str = "";
			return;
		}
		if (str.equals("connect")) {
			EchoServer.oc = new OrderController(dbname.getText(), password.getText());

			if (EchoServer.oc.successFlag == 1) {
				EchoServer.qw = new QWController(EchoServer.oc.getConnection());
				EchoServer.reportController = new ReportController(EchoServer.oc.getConnection());
				EchoServer.sup = new SupplierController(EchoServer.oc.getConnection());
				ServerUI.runServer(EchoServer.DEFAULT_PORT.toString());
				serverip.setText(EchoServer.serverIp);
				textMessage.setText("Connected!");

				importUsers(EchoServer.oc.getConnection());
			} else {
				textMessage.setText("conection failed!");
			}
		}
		str = "";
	}

	/**
	 * methos to start the gui
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/serverGUI/ServerGUI.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		controller = loader.getController();
		EchoServer.spf = this;
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the exit action for the application.
	 * <p>
	 * This method is triggered by an action event (e.g., a button click). It performs the following steps:
	 * <ul>
	 *   <li>Logs a message indicating the server exit.</li>
	 *   <li>If the `OrderController` instance (`EchoServer.oc`) is not null, it performs a cleanup operation in the DB by calling `undoImportUsers`.</li>
	 *   <li>Exits the application with status code 0, indicating a normal termination.</li>
	 * </ul>
	 * </p>
	 *
	 * @param event The action event that triggered this method. This parameter is typically 
	 *              provided by the UI framework and contains information about the source of the event.
	 * @throws Exception If an error occurs during the cleanup process.
	 */
	
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Server");
		if (EchoServer.oc != null)
			undoImportUsers(EchoServer.oc.getConnection());
		System.exit(0);
	}


	/**
	 * Prints the current state of client connections to the console and updates the UI to display this information.
	 * <p>
	 * This method performs the following actions:
	 * <ul>
	 *   <li>Logs the entire `clientsMap` to the console.</li>
	 *   <li>Updates the `txtClientConnection` text area in the UI to show the list of connected clients.</li>
	 * </ul>
	 * <p>
	 * The UI update is performed on the JavaFX Application Thread using {@code Platform.runLater()} to ensure thread safety.
	 * </p>
	 *
	 * @param clientsMap A map where keys are `ConnectionToClient` instances representing connected clients,
	 *                   and values are `String` representations of these clients (e.g., client identifiers or details).
	 */
	public void printConnection(Map<ConnectionToClient, String> clientsMap) {
		System.out.println(clientsMap);
		Platform.runLater(() -> {
			// Accessing txtClientConnection from the controller instance
			String toPrint = "";
			Collection<String> values = clientsMap.values(); // all values in the map
			for (String value : values) {
				toPrint = toPrint + value + "\n"; // add all the values to a string to print
			}
			controller.txtClientConnection.setText(toPrint); // print the clients

		});
	}

	/**
	 * Imports user data from an external source into the `users` table in the database.
	 * <p>
	 * This method performs the following steps:
	 * <ul>
	 *   <li>Executes an SQL statement to copy data from the `usersExternal` table into the `users` table.</li>
	 * </ul>
	 * <p>
	 * In case of an exception, the error stack trace is printed to the console.
	 * </p>
	 *
	 * @param con The database connection to use for executing the SQL statement.
	 */
	public void importUsers(Connection con) {
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(
					"insert into users(ID,First_Name,Last_Name,Phone_Num,eMail,CustomerType,CardNum,Pswd,IsLoggedIn,Approved,AssociatedBranch,Credit)"
							+ " select * from usersExternal");
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Removes all records from the `users` table in the database.
	 * <p>
	 * This method executes a SQL `DELETE FROM users` statement to delete all rows from the `users` table.
	 * </p>
	 * <p>
	 * If an exception occurs while executing the SQL statement, the error stack trace is printed to the console.
	 * </p>
	 *
	 * @param con The database connection to use for executing the SQL statement. This should be a valid and open connection to the database.
	 */
	public void undoImportUsers(Connection con) {
		if (con != null) {
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("delete from users;");
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

}
