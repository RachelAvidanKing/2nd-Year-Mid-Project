package ClientGui;

import java.util.ArrayList;

import client.ClientUI;
import entities.Meal;
import entities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This class handles the menu page functionality.
 * It allows users to view and select different types of dishes and manage their order.
 */
public class MenuPage {

    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();

    /** List to store Meal objects. */
    private ArrayList<Meal> dishesArray = new ArrayList<>();
    
    /** Button to display the appetizer menu. */
    @FXML
    private Button btnAppetizer = null;
    
    /** Button to display the dessert menu. */
    @FXML
    private Button btnDessert = null;

    /** Button to display the drinks menu. */
    @FXML
    private Button btnDrinks = null;
    
    /** Button to display the main course menu. */
    @FXML
    private Button btnMain = null;

    /** Button to display the salad menu. */
    @FXML
    private Button btnSalad = null;
    
    /** Button to navigate to the cart page. */
    @FXML
    private Button btnBasket = null;
    
    /** Button to cancel the current order. */
    @FXML
    private Button btnCancel = null;
    
    /** Button to finish adding items to the order and proceed. */
    @FXML
    private Button btnFinishAdd = null;

    /** Label to display messages to the user. */
    @FXML
    private Label message;

    /**
     * Handles the event when the "Finish Add" button is clicked.
     * Navigates to the "Choose Supply Method" page.
     *
     * @param event The action event triggered by pressing the "Finish Add" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnFinishAddClick(ActionEvent event) throws Exception {
        if (helpClass.order.getDishList().size() != 0) {
            message.setText("");
            help.changeScreen("/ClientGui/ChooseSupplyMethodPage.fxml", event);
        } else {
            message.setText("you must add a dish to continue");
        }
    }

    /**
     * Handles the event when the "Cancel" button is clicked.
     * Cancels the current order and navigates back to the "Order" page.
     *
     * @param event The action event triggered by pressing the "Cancel" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void cancelOrder(ActionEvent event) throws Exception {
        helpClass.order = new Order();
        help.changeScreen("/ClientGui/OrderPage.fxml", event);
    }

    /**
     * Handles the event when the "Basket" button is clicked.
     * Navigates to the cart page where the user can review their order.
     *
     * @param event The action event triggered by pressing the "Basket" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void goToCart(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/cartGui.fxml", event);
    }

    /**
     * Handles the event when the "Salad" button is clicked.
     * Displays the salad menu and navigates to the "Client Show Meal" page.
     *
     * @param event The action event triggered by pressing the "Salad" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void SaladMenuShow(ActionEvent event) throws Exception {
        /** Different types of message to get the specific courses for a certain restaurant */
        helpClass.dishType = "Salad";
        String toSend = "showDish:salad:" + helpClass.order.getRestaurant();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();

        helpClass.dishes = null;
        dishesArray = new ArrayList<>();
        helpClass.dishType = "";
        help.changeScreen("/ClientGui/ClientShowMeal.fxml", event);
    }

    /**
     * Handles the event when the "Appetizer" button is clicked.
     * Displays the appetizer menu and navigates to the "Client Show Meal" page.
     *
     * @param event The action event triggered by pressing the "Appetizer" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void AppetizerMenuShow(ActionEvent event) throws Exception {
        helpClass.dishType = "Appetizers";
        String toSend = "showDish:Appetizers:" + helpClass.order.getRestaurant();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();

        helpClass.dishes = null;
        dishesArray = new ArrayList<>();
        helpClass.dishType = "";
        help.changeScreen("/ClientGui/ClientShowMeal.fxml", event);
    }

    /**
     * Handles the event when the "Main" button is clicked.
     * Displays the main course menu and navigates to the "Client Show Meal" page.
     *
     * @param event The action event triggered by pressing the "Main" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void MainMenuShow(ActionEvent event) throws Exception {
        helpClass.dishType = "Main";
        String toSend = "showDish:Main:" + helpClass.order.getRestaurant();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();

        helpClass.dishes = null;
        dishesArray = new ArrayList<>();
        helpClass.dishType = "";
        help.changeScreen("/ClientGui/ClientShowMeal.fxml", event);
    }

    /**
     * Handles the event when the "Dessert" button is clicked.
     * Displays the dessert menu and navigates to the "Client Show Meal" page.
     *
     * @param event The action event triggered by pressing the "Dessert" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void DessertMenuShow(ActionEvent event) throws Exception {
        helpClass.dishType = "Desserts";
        String toSend = "showDish:Desserts:" + helpClass.order.getRestaurant();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();

        helpClass.dishes = null;
        dishesArray = new ArrayList<>();
        helpClass.dishType = "";
        help.changeScreen("/ClientGui/ClientShowMeal.fxml", event);
    }

    /**
     * Handles the event when the "Drinks" button is clicked.
     * Displays the drinks menu and navigates to the "Client Show Meal" page.
     *
     * @param event The action event triggered by pressing the "Drinks" button.
     * @throws Exception if there is an error during the screen change.
     */
    public void DrinksMenuShow(ActionEvent event) throws Exception {
        helpClass.dishType = "Drinks";
        String toSend = "showDish:Drinks:" + helpClass.order.getRestaurant();
        ClientUI.chat.accept(toSend);
        fromStringToMealArray();

        helpClass.dishes = null;
        dishesArray = new ArrayList<>();
        helpClass.dishType = "";
        help.changeScreen("/ClientGui/ClientShowMeal.fxml", event);
    }
    
    /**
     * Converts a string array to an array of Meal objects and populates the dishes array.
     * Parses the string array from the server and creates Meal objects to add to the dishes array.
     */
    private void fromStringToMealArray() {
        Meal meal;
        String temp = " ";
        String[] tempArray;

        /**
         * Iterates over the string array to convert each string into a Meal object.
         */
        for (String str : helpClass.dishes) {
            temp = str;
            tempArray = temp.split("\\:");
            temp = "";

            /**
             * Creates a string for optional components.
             */
            for (int i = 3; i < tempArray.length; i++) {
                temp = temp + tempArray[i] + " ";
            }

            /**
             * Handles the case where there are no optional components.
             */
            if (temp.equals("null")) {
                temp = "";
            }
            /**Saving the data of the meal*/
            meal = new Meal(tempArray[0], tempArray[1], tempArray[2], temp);
            /**Add the meal to the current dishes array*/
            dishesArray.add(meal);
            /**Add the matching options*/
            ClientShowMenuOptions.namesList.add(tempArray[0]);
        }
        /**Add the dishes to the meal data array*/
        ClientShowMenuOptions.mealDataList.addAll(dishesArray);
    }
}
