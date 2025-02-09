package ClientGui;

import client.ClientConsole;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class represents the starting point of the client's journey.
 * The client inputs the server's IP address to establish a connection.
 */
public class OrderStartGui {

    /** The IP address entered by the client. */
    public static String clientIp = "";

    /** Helper class instance to handle screen changes. */
    helpClass help = new helpClass();

    @FXML
    private Button btnExit = null;
    
    /**Button for ip insert*/
    @FXML
    private Button btnIP = null;
    /**Text field to check that everything is okay with ip*/
    @FXML
    private TextField txtIp;

    /**
     * Starts the primary stage and loads the initial FXML layout.
     * 
     * @param primaryStage The primary stage of the JavaFX application.
     * @throws Exception if the FXML file cannot be loaded.
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ClientGui/ClientGUIenterIP.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Show Start Frame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles the event when the client enters the IP address and presses the button.
     * Establishes a connection to the server using the provided IP address.
     * 
     * @param event The action event triggered by pressing the button.
     * @throws Exception if there is an error during the screen change.
     */
    public void enterIp(ActionEvent event) throws Exception {
        Object[] toSend = new Object[2];
        String temp = txtIp.getText();
        if (temp.trim().isEmpty()) { /** If no ip was entered*/
            txtIp.setText("Please enter IP!");
        } else {
            ClientUI.chat = new ClientConsole(temp, 5555);
            toSend[0] = event;
            toSend[1] = clientIp;
            clientIp = "";
            help.changeScreen("/ClientGui/LoginP.fxml", event);
        }
    }
}
