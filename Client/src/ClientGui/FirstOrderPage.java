package ClientGui;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class handles the first order page where the user can start a new order, 
 * approve existing orders, or log out of the system.
 */
public class FirstOrderPage {
    
    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();

    /** Button to navigate to the order approval page. */
    @FXML
    private Button btnApproveOrder = null;

    /** Button to log out of the system. */
    @FXML
    private Button btnLogOut = null;

    /** Button to start a new order. */
    @FXML
    private Button btnStartOrder = null;

    /** Label to display messages to the user. */
    @FXML
    private Label message;
    
    /**
     * Handles the event when the "Start Order" button is clicked.
     * Navigates the user to the order creation page.
     *
     * @param event The ActionEvent triggered by pressing the "Start Order" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnStartOrder(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/OrderPage.fxml", event);
    }
    
    /**
     * Handles the event when the "Approve Order" button is clicked.
     * Navigates the user to the approved orders page where they can manage their orders.
     *
     * @param event The ActionEvent triggered by pressing the "Approve Order" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnApproveOrder(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/ApprovedOrdersPage.fxml", event);
    }
    
    /**
     * Handles the event when the "Log Out" button is clicked.
     * Logs the user out of the system and navigates them back to the login page.
     *
     * @param event The ActionEvent triggered by pressing the "Log Out" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnLogOut(ActionEvent event) throws Exception {
        String toSend = "LoggedOut " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
        ClientUI.chat.accept(toSend);
        help.changeScreen("/ClientGui/LoginP.fxml", event);
    }
}
