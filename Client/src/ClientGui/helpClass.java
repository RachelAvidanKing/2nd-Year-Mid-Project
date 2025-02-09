package ClientGui;

import java.util.ArrayList;

import client.ClientUI;
import entities.Client;
import entities.MessageFinishOrder;
import entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class provides helper methods for various functionalities, such as
 * navigating between screens, exiting the application, and validating input
 * strings.
 */
public class helpClass {

    /** The client object representing the new client. */
    public static Client newClient;

    /** The current order object. */
    public static Order order = new Order();

    /** The type of dish selected. */
    public static String dishType = "";
    
    /** The list of dishes available. */
    public static ArrayList<String> dishes = null;
    
    /** The list of order branch numbers for the supplier. */
    public static ArrayList<String> ordernumber = null;
    
    /** The list of order details for supplier including message details. */
    public static ArrayList<MessageFinishOrder> messageDetails = null;

    /** For the simulation to know what to print. */
    public static MessageFinishOrder orderForPopup = null;

    /**
     * Handles the back button functionality, navigating to the specified source.
     *
     * @param source The FXML file path to navigate to.
     * @param event  The action event triggered by pressing the back button.
     * @throws Exception if there is an error during the screen change.
     */
    public void generalBtnBack(String source, ActionEvent event) throws Exception {
        changeScreen(source, event);
    }

    /**
     * Exits the application.
     *
     * @param event The action event triggered by pressing the exit button.
     * @throws Exception if there is an error during the exit process.
     */
    public void ExitBtn(ActionEvent event) throws Exception {
        ClientUI.chat.accept("ClientDisconnect");
        System.exit(0);
    }

    /**
     * Changes the current screen to the specified source.
     *
     * @param source The FXML file path to navigate to.
     * @param event  The action event triggered by pressing a button.
     * @throws Exception if there is an error during the screen change.
     */
    public void changeScreen(String source, ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            ((Node) event.getSource()).getScene().getWindow().hide(); // Hiding the primary window
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Pane root = loader.load(getClass().getResource(source).openStream());
            Scene scene = new Scene(root);
            primaryStage.setTitle("Start Frame");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the current screen to the specified source and closes the current window.
     *
     * @param source The FXML file path to navigate to.
     * @param event  The action event triggered by pressing a button.
     * @throws Exception if there is an error during the screen change.
     */
    public void changeScreenClose(String source, ActionEvent event) throws Exception {
        try {
            /** Close the current window and terminate the application thread*/
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.close(); // Close the current window

            /** Load the new scene */
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Pane root = loader.load();
            Scene scene = new Scene(root);

            /** Set up the new scene in a new stage */
            Stage newStage = new Stage();
            newStage.setTitle("Start Frame");
            newStage.setScene(scene);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a string contains only numbers and at most one dot, used for
     * validating price fields.
     *
     * @param num The string to be checked.
     * @return true if the string is a valid number with at most one dot, false
     *         otherwise.
     */
    public Boolean checkNumIsDouble(String num) {
        int countDots = 0; /** Count how many dots are in the string */
        for (char c : num.toCharArray()) {
            if (!(Character.isDigit(c)) && (Character.compare(c, '.')) != 0) {
                return false; /** If the current char isn't a digit or a dot */
            }
            if (Character.compare(c, '.') == 0) { /** Current char is a dot */
                if (countDots == 1) { /** There is more than one dot in the string */
                    return false;
                }
                countDots++;
            }
        }
        return true;
    }

    /**
     * Checks if a string is an integer, used for validating order number fields.
     *
     * @param num The string to be checked.
     * @return true if the string is a valid integer, false otherwise.
     */
    public Boolean checkNumIsInt(String num) {
        for (char c : num.toCharArray()) { /** Convert to an array of characters*/
            if (!(Character.isDigit(c))) {
                return false; /** If the current char is a digit */
            }
        }
        return true;
    }
    
    /**
     * Checks if a string is valid by ensuring it does not contain any digits.
     *
     * @param str The string to be checked.
     * @return true if the string does not contain digits, false otherwise.
     */
    public Boolean checkStringIsOk(String str) {
        for (char c : str.toCharArray()) { /** Convert to an array of characters*/
            if (Character.isDigit(c)) { 
                return false; /** If the current char is a digit */
            }
        }
        return true;
    }
}
