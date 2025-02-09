package qworkergui;

import java.util.ArrayList;
import java.util.Collection;
import ClientGui.helpClass;
import client.ClientUI;
import entities.Order;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 * This class represents the controller for the "Delete Dish" scene in the QWorker GUI.
 * It allows the user to select a dish from a combo box and send a message to the server to remove it.
 */
public class DeleteDish {
	 private helpClass help = new helpClass();
	 
	  @FXML
	    private Button btnBack=null;

	    @FXML
	    private Button btnSave=null;

	    @FXML
	    private ComboBox<String> dishesCombo;

	    /**
	     * Label to display success messages.
	     */
	    @FXML
	    private TextField successLabel;
	    
	    /**
	     * Initializes function retrieves dish names from the help class.
	     * Populates the combo box with dish names.
	     */
	    @FXML
	    private void initialize() {
	        ArrayList<String> names = getNames();
	        dishesCombo.setItems(null);
	        dishesCombo.setItems(FXCollections.observableArrayList(names));
	    }
	    /**
	     * Handles the "Back" button click.
	     * Uses the help class to change the scene to the QWGuiO.fxml.
	     *
	     * @param event the ActionEvent triggered by the button click
	     * @throws Exception if there's an error changing the scene
	     */
	    @FXML
	    void btnBackClick(ActionEvent event) throws Exception
	    {
	    	 help.changeScreen("/qworkergui/QWGuiO.fxml", event);
	    }

	    
	    /**
	     * Handles the "Save" button click.
	     * Gets the selected dish from the combo box.
	     * Validates if a dish is selected (prevents null message).
	     * Sends the message using the ClientUI's chat functionality.
	     * Displays a success message.
	     *
	     * @param event the ActionEvent triggered by the button click
	     */
	    @FXML
	    void btnSaveData(ActionEvent event) 
	    {
	    	String dish = dishesCombo.getValue();        
	        /**
	         * Check if the price is valid by the functions that
	         * validates a Double number in help class.
	         */
	        if (dish == null) {
	        	successLabel.setText("Please choose a dish!");
	            return;
	        }
	        else
	        {
	        	successLabel.setText("");
	        }
	        String msg="Remove:"+dish+":"+helpClass.newClient.gethomeBranch();
	        ClientUI.chat.accept(msg);
	        successLabel.setText("Dish removed successfully!");
	        
	    }
	    
	    /**
	     * Retrieves the names of all dishes.
	     * 
	     * @return an ArrayList of dish names
	     */
	    private ArrayList<String> getNames() {
	        String[] toSplit;
	        ArrayList<String> names = new ArrayList<>();
	        String temp;
	        for (String str : helpClass.dishes) {
	            temp = str;
	            toSplit = temp.split("\\:");
	            names.add(toSplit[0]);
	        }
	        return names;
	    }
}