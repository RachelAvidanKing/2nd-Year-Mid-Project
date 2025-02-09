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
 * Controller class for the editDish GUI.
 * Handles operations such as displaying, editing, and saving dish details.
 */
public class editDish {
    private helpClass help = new helpClass();

    @FXML
    private Button btnBack = null;

    @FXML
    private Button btnSave = null;

    @FXML
    private Button btnShowDish = null;

    @FXML
    private TextField categoryField;

    @FXML
    private ComboBox<String> dishesCombo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField optionsField;

    @FXML
    private TextField priceField;

    @FXML
    private Label message;
    
    @FXML
    private Label labelupdated;
    
    

    /**
     * Event handler for the back button click.
     * Changes the screen to the previous GUI.
     * @param event the action event
     * @throws Exception if there is an error during the screen change process
     */
    public void btnBackClick(ActionEvent event) throws Exception {
        help.changeScreen("/qworkergui/QWGuiO.fxml", event);
    }

    /**
     * Initializes the controller class.
     * Called to initialize the dishes combo-box with the dishes names.
     */
    @FXML
    private void initialize() {
        ArrayList<String> names = getNames();
        //dishesCombo.setItems(null);
        dishesCombo.setItems(FXCollections.observableArrayList(names));
    }

    /**
     * Retrieves the names of all dishes.
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

    /**
     * Event handler to display the selected dish details.
     * @param event the action event
     * @throws Exception if there is an error during the process
     */
    public void showDish(ActionEvent event) throws Exception {
        String dish = dishesCombo.getValue();
        if (dish == null) {
            message.setText("Please choose a dish!");
        } else {
            message.setText("");
            String[] dishDetails = getDishDetails(dish);
            nameField.setText(dishDetails[0]);
            optionsField.setText(dishDetails[2]);
            priceField.setText(dishDetails[1]);
        }
    }

    /**
     * Event handler to save the edited dish details.
     * @param event the action event
     * @throws Exception if there is an error during the process
     */
    public void btnSaveData(ActionEvent event) throws Exception {
        String dish = dishesCombo.getValue();
        String name = nameField.getText().trim();
        String options = optionsField.getText().trim();
        String price = priceField.getText().trim();
              
        /**
         * Check if the price is valid by the functions that
         * validates a Double number in help class.
         */
        if (dish == null) {
            message.setText("Please choose a dish!");
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


        String details = "UpdateMenu" + ":" + dish + ":" + name + ":" + options + ":" + price+":"+helpClass.newClient.gethomeBranch();
        ClientUI.chat.accept(details);
        labelupdated.setText("Menu data updated successfully!");
    }

    /**
     * Retrieves the details of a dish given its name.
     * @param name the name of the dish
     * @return an array of dish details
     */
    private String[] getDishDetails(String name) {
        String[] toSplit = null;
        String[] ifNoComponents = new String[3];
        String temp;
        for (String str : helpClass.dishes) {
            temp = str;
            toSplit = temp.split("\\:");
            if (name.equals(toSplit[0])) {
                if (toSplit.length == 2) {
                    ifNoComponents[0] = toSplit[0];
                    ifNoComponents[1] = toSplit[1];
                    ifNoComponents[2] = " ";
                    return ifNoComponents;
                }
                return toSplit;
            }
        }
        return null;
    }
}
