package ClientGui;

import java.util.Collection;

import client.ChatClient;
import entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
/**
 * This class represents the Order Summary of all data
 * inserted by the user.
 */
public class OrderSummary {
	helpClass help = new helpClass();
    /**Button to exit and start the order process from the begining*/
    @FXML
    private Button btnLogOutOrderSummary=null;
    
    /**Textarea for order details*/
    @FXML
    private TextArea ordersummary;
    
    /**Initialize the data from the order in the screen*/
    @FXML
    private void initialize() {
    	ordersummary.setText(helpClass.order.toString());
    }
    
    /**
     * Handles the event when the user wants to get back to main screen
     * 
     * @param event The action event triggered by pressing the button.
     * @throws Exception if there is an error during the screen change.
     */
    
    public void backToMain(ActionEvent event) throws Exception
    {
    	 helpClass.order = new Order();
    	help.changeScreen("/ClientGui/FirstOrderScreenO.fxml", event);

    }
}
