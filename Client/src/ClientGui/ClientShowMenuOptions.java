package ClientGui;

import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Meal;

/**
 * This class handles the GUI for displaying menu options to the client.
 * It shows a table of meals with their name, optional components, and price.
 */
public class ClientShowMenuOptions {

    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();
    
    @FXML
    private Button btnBack = null;
    
    /** Button to add the item to the cart */
    @FXML
    private Button btnaddcart = null;
    
    /** Button to show optional components after choosing a meal from the menu */
    @FXML
    private Button show = null;
    
    /** 
     * Message that indicates whether the dish with optional components was added successfully
     * or there was no dish selected to add to the cart.
     */
    @FXML
    private Label message;
    
    /** Setup for all the meals in the menu with their optional components and price */
    @FXML
    private TableView<Meal> tableView;
    
    @FXML
    private TableColumn<Meal, String> name;
    @FXML
    private TableColumn<Meal, String> optionalComponents;
    @FXML
    private TableColumn<Meal, String> price;
    
    /** ComboBox for meal options */
    @FXML
    private ComboBox<String> comboOptions;
    
    /** ComboBox for meal names */
    @FXML
    private ComboBox<String> comboName;
    
    /** Observable list to hold the meal data to be displayed in the table. */
    public static ObservableList<Meal> mealDataList = FXCollections.observableArrayList();
    public static ObservableList<String> namesList = FXCollections.observableArrayList();
    public static ObservableList<String> optionsList = FXCollections.observableArrayList();
    
    /**
     * Initializes the table view by setting up the cell value factories for each column
     * and populating the table with the meal data.
     */
    @FXML
    private void initialize() { 
        /** Set properties for the table columns. */
        name.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        optionalComponents.setCellValueFactory(new PropertyValueFactory<Meal, String>("optionalComponents"));
        price.setCellValueFactory(new PropertyValueFactory<Meal, String>("price"));
        
        /** Setup for menu in table */
        tableView.setItems(mealDataList);
        
        /** Setup for meal names in the ComboBox */
        comboName.setItems(namesList);
    }
    
    /**
     * Handles the optional components which are inserted in combo box.
     * 
     * @param event The ActionEvent triggered by the show button click.
     * @throws Exception if the insertion fails.
     */
    @FXML
    private void getOptionsforCombo(ActionEvent event) throws Exception {
        /** Initialization of the options list */
        optionsList = FXCollections.observableArrayList();
        
        /** Calling the method which gets the meal options according to meal name */
        getOptionsForDish(comboName.getValue());
        
        /** Setting the matching options in the ComboBox */
        comboOptions.setItems(optionsList);
    }
    
    /**
     * Handles the event when the back button is clicked.
     * Clears the meal data list and navigates back to the menu screen.
     * 
     * @param event The action event triggered by pressing the back button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnBackClick(ActionEvent event) throws Exception {
        mealDataList = FXCollections.observableArrayList();
        namesList = FXCollections.observableArrayList();
        help.changeScreen("/ClientGui/MenuGui.fxml", event);
    }
    
    /**
     * Handles the event when the "Add to Cart" button is clicked.
     * Validates the selected dish and its optional components, adds the meal to the order, 
     * and displays a success message.
     * 
     * @param event The action event triggered by pressing the "Add to Cart" button.
     * @throws Exception if there is an error during the process.
     */
    public void btnAddClick(ActionEvent event) throws Exception {
        /** Validate that a dish has been selected from the ComboBox */
        if (comboName.getValue() == null) {
            message.setText("please choose dish");
            return;
        }
        
        /** Clear any previous messages */
        message.setText("");
        
        Meal mealChosen = null;
        
        /** Find the selected meal in the meal data list */
        for (Meal m : mealDataList) {
            if (m.getName().equals(comboName.getValue())) {
                mealChosen = m;
                break;
            }
        }
        
        /** Set the selected optional components for the chosen meal */
        mealChosen.setOptionalComponents(comboOptions.getValue());
        
        /** Add the chosen meal with its optional components to the order */
        helpClass.order.addToDishList(mealChosen);
        
        /** Display a success message */
        message.setText("added successfully");
        
        /** Clear the ComboBox for optional components */
        comboOptions.setItems(null);
    }
    
    /**
     * Retrieves the optional components for the selected dish and populates the options list.
     * 
     * @param name The name of the dish for which the optional components are being retrieved.
     */
    private void getOptionsForDish(String name) {
        Meal meal = null;
        
        /** Find the meal in the meal data list */
        for (Meal m : mealDataList) {
            if (m.getName().equals(name)) {
                meal = m;
                break;
            }
        }
        
        /** Split the optional components and add them to the options list */
        String[] options = meal.getOptionalComponents().split("\\/");
        for (int i = 0; i < options.length; i++) {
            optionsList.add(options[i]);
        }
    }
}
