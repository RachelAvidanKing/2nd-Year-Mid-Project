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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This class allows a qualified worker to decide if they need to add/delete/edit
 * a dish to the menu. It implements setters and getters and various possible 
 * change to be made to the appropriate menu in the DB. 
 **/
public class QWmenu {
    private helpClass help = new helpClass();
    private ArrayList<Meal> dishesArray = new ArrayList<>();
    
    @FXML
    private Button add = null;

    @FXML
    private Button btnLogOut = null;

    @FXML
    private Button edit = null;
    
    @FXML
    private Button remove = null;

    @FXML
    private TableColumn<Meal, String> name;

    @FXML
    private TableColumn<Meal, String> optionalComponents;

    @FXML
    private TableColumn<Meal, String> price;

    @FXML
    private TableView<Meal> table;
    
    public static ObservableList<Meal> DataList = FXCollections.observableArrayList();

    @FXML
    private void initialize() { 
    	dishesArray = new ArrayList<>();
    	DataList = FXCollections.observableArrayList();
        String toSend = "allMenu " + helpClass.newClient.gethomeBranch();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();
        name.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        optionalComponents.setCellValueFactory(new PropertyValueFactory<Meal, String>("optionalComponents"));
        price.setCellValueFactory(new PropertyValueFactory<Meal, String>("price"));
        table.setItems(DataList);
    }
    
    /**
     * Method to logout if the appropriate button is pressed.
     * @param event
     * @throws Exception
     */
    public void btnLogout(ActionEvent event) throws Exception {
        String toSend = "LoggedOut " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
        ClientUI.chat.accept(toSend);
        helpClass.dishes=null;
        help.changeScreen("/ClientGui/LoginP.fxml", event);
    }    
    
    /**
     * Method to go edit a dish if the appropriate button is pressed.
     * @param event
     * @throws Exception
     */
    public void editDish(ActionEvent event) throws Exception {
    	DataList = FXCollections.observableArrayList();
        help.changeScreen("/qworkergui/editDishGui.fxml", event);
    }
    
    /**
     * Method to go delete a dish if the appropriate button is pressed.
     * @param event
     * @throws Exception
     */
    public void removeDish(ActionEvent event) throws Exception {
    	DataList = FXCollections.observableArrayList();
        help.changeScreen("/qworkergui/deleteDishGui.fxml", event);
    }
    
    /**
     * Method to go add a dish if the appropriate button is pressed.
     * @param event
     * @throws Exception
     */
    public void addDish(ActionEvent event) throws Exception {
    	DataList = FXCollections.observableArrayList();
        help.changeScreen("/qworkergui/addDishGui.fxml", event);
    }

    /**
     * Method to change the array from string array to an array of object Meal
     */
    private void fromStringToMealArray() {
        Meal meal;
        String temp = " ";
        String[] tempArray;
        // array locations: 0-name, 1-type, 2-price, rest: options
        for (String str : helpClass.dishes) {
            temp = str; // get current string in the array
            tempArray = temp.split("\\:"); // split the string
            temp = ""; // restart temp
            for (int i = 2; i < tempArray.length; i++) {
                temp = temp + tempArray[i] + " "; // create a String for optional components
            }
            if (temp.equals("null")) {
                temp = "";
            }
            
            meal = new Meal(tempArray[0], null, tempArray[1], temp); // create object meal from the string
            dishesArray.add(meal); // add current meal to the list
           
        }
        DataList.addAll(dishesArray);
    }
}
