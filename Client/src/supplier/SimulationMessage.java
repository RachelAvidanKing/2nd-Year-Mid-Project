package supplier;

import ClientGui.helpClass;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * This class represents a simulation message display for the client, 
 * which behaves like a message from the external system.
 * It fetches the order information from the helpClass and displays it in a TextArea.
 */
public class SimulationMessage {
	
	 /**
     * The TextArea where the simulation message will be displayed.
     */
    @FXML
    private TextArea message;
    
    /**
     * Initializes populating the TextArea with the order information from the helpClass.
     */
    @FXML
    void initialize() {
    	message.setText(helpClass.orderForPopup.toString());
    }

}