package qworkergui;
import java.util.ArrayList;

import ClientGui.ClientShowMenuOptions;
import ClientGui.helpClass;
import client.ClientUI;
import entities.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class allows a qualified worker to add a dish to the menu.
 * It implements setters and getters and various possible 
 * change to be made to the appropriate menu in the DB. 
 **/
public class addDish {
	private helpClass help = new helpClass();
	public static ArrayList<String> categoriesList;
	public static String addDishStatus;
	 @FXML
	    private Button btnBack=null;

	    @FXML
	    private Button btnSave=null;

	    @FXML
	    private ComboBox<String> categCombo;

	    @FXML
	    private Label labelupdated;

	    @FXML
	    private Label message;

	    @FXML
	    private TextField nameField;

	    @FXML
	    private TextField optionsField;

	    @FXML
	    private TextField priceField;

	    @FXML
	    private void initialize() {
	    	ClientUI.chat.accept("importCategories");
	    	categCombo.setItems(null);
	    	categCombo.setItems(FXCollections.observableArrayList(categoriesList));
	    }
    
	    /**
	     * Method to save the data when the appropriate button was pressed.
	     * @param event
	     * @throws Exception
	     */
	    public void btnSaveData(ActionEvent event) throws Exception {
	        String category = categCombo.getValue();
	        String name = nameField.getText().trim();
	        String options = optionsField.getText().trim();
	        String price = priceField.getText().trim();
	              
	        /**
	         * Check if the price is valid by the functions that
	         * validates a Double number in help class.
	         */
	        if (category == null) {
	            message.setText("Please choose a category!");
	            return;
	        }
	        else
	        {
	        	message.setText("");
	        }
	        if (price.equals("") || !help.checkNumIsDouble(price)) {
	        	message.setText("Invalid price.");
	            return;
	        	
	        } else {
	        	message.setText("");
	        }
	        
	        if (name.equals("") ||!help.checkStringIsOk(name)) {
	        	message.setText("Invalid name.");
	            return;
	        }
	        else {
	        	message.setText("");
	        }

	        if (help.checkStringIsOk(options)) {
	        	message.setText("");
	        }
	        else {
	        	 message.setText("Invalid options.");
	             return;
	        }


	        String details = "addToMenu" + ":" + category + ":" + name + ":" + options + ":" + price+":"+helpClass.newClient.gethomeBranch();
	        ClientUI.chat.accept(details);
	        if(addDishStatus.equals("1"))
	        {
	        	labelupdated.setText("Menu data updated successfully!");
	        }
	        else
	        {
	        	labelupdated.setText("Dish already exists");
	        }
	    }
	    
	    /**
	     * Method to go back when the appropriate button was pressed.
	     * @param event
	     * @throws Exception
	     */
	    public void btnBackClick(ActionEvent event) throws Exception {
	        help.changeScreen("/qworkergui/QWGuiO.fxml", event);
	    }
}
