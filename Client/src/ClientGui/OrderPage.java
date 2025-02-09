package ClientGui;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.Order;

/**
 * This class handles the order page functionality.
 * It allows users to select a restaurant, set the desired hour for the order, and proceed to the menu page.
 */
public class OrderPage{

    /** Chat client instance for communication with the server. */
    public static ChatClient chatClient;

    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();

    @FXML
    private Button btnNext;

    @FXML
    private Button btnBack;
    
    /**combo box for restaurant selection by the user*/
    @FXML
    private ComboBox<String> cmbChooseRestaurant;
    
    /**Text fields to insert time of arrival*/
    @FXML
    private TextField Hour;

    @FXML
    private TextField Min;
    
    /**Label for error message if the values in timer inserted incorrectly*/
    @FXML
    private Label labelErrorMessage;

    /** Collection to hold restaurant names retrieved from the database. */
    public static ArrayList<String> restaurants;

    /**
     * Initializes the order page, sets up the restaurant combo box, and initializes the order object.
     */
    @FXML
    private void initialize() {
    	ClientUI.chat.accept("getRestaurants");
        cmbChooseRestaurant.setItems(FXCollections.observableArrayList(restaurants));
    }

    /**
     * Checks the user input for selecting a restaurant and setting the order time.
     *
     * @return true if the input is valid, false otherwise.
     */
    @FXML
    private boolean checkInput() {
        String hours = Hour.getText().trim();
        String min = Min.getText().trim();
        String restaurant = cmbChooseRestaurant.getValue();
        return checkInput(restaurant, hours, min);
    }

    /**
     * Handles the event when the Next button is clicked.
     * Validates the input and navigates to the menu page if the input is valid.
     *
     * @param event The action event triggered by pressing the Next button.
     * @throws Exception if there is an error during the screen change.
     */
    public void btnNext(ActionEvent event) throws Exception {
        if (checkInput()) {
        	Integer hour=Integer.parseInt(Hour.getText().trim());
        	Integer minute=Integer.parseInt(Min.getText().trim());
        	LocalTime toSet=LocalTime.of(hour,minute,0);
            /** Define the desired format */
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            /** Convert the LocalTime object to a String */
            String formattedTime = toSet.format(formatter);
            /**Set the data in order object to save it for later*/
        	helpClass.order.setWantedTime(formattedTime);
        	helpClass.order.setDate(LocalDate.now());
            helpClass.order.setRestaurant(cmbChooseRestaurant.getValue());
            help.changeScreen("/ClientGui/MenuGui.fxml", event);
        } else {
            labelErrorMessage.setText("please enter values correctly!");
        }
    }

    /**
     * Handles the event when the Logout button is clicked.
     * Logs out the user and navigates back to the login page.
     *
     * @param event The action event triggered by pressing the Logout button.
     * @throws Exception if there is an error during the screen change.
     */
    @FXML
    private void btnBack(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/FirstOrderScreenO.fxml", event);
    }

    /**
     * Retrieves the selected restaurant from the combo box.
     *
     * @return the selected restaurant name, or null if no restaurant is selected.
     */
    public String getCmbChooseRestaurant() {
        if (cmbChooseRestaurant != null && cmbChooseRestaurant.getValue() != null) {
            return cmbChooseRestaurant.getValue();
        }
        return null;
    }
    
    /**
     * Checks if the user input for selecting a restaurant and setting the order time is valid.
     *
     * @param restaurant The selected restaurant.
     * @param hour The desired hour as a string.
     * @param min The desired minute as a string.
     * @return true if the input is valid, false otherwise.
     */
    public boolean checkInput(String restaurant, String hour, String min) {
        if (restaurant == null || restaurant.equals("")) {
            return false;
        }
        if (hour == null || !hour.matches("\\d{2}")) {
            return false;
        }
        if (min == null || !min.matches("\\d{2}")) {
            return false;
        }

        int hourInt;
        int minInt;

        try {
            hourInt = Integer.parseInt(hour);
            minInt = Integer.parseInt(min);
        } catch (NumberFormatException e) {
            return false;
        }

        if (hourInt < 0 || hourInt > 23) {
            return false;
        }
        if (minInt < 0 || minInt > 59) {
            return false;
        }

        /**
         * Get the current time
         */
        LocalTime currentTime = LocalTime.now();

        /**
         * Create a LocalTime object from the input
         */
        LocalTime inputTime = LocalTime.of(hourInt, minInt);

        /**
         * Check if the input time has already passed
         */
        if (inputTime.isBefore(currentTime)) {
            return false;
        }

        return true;
    }
}
