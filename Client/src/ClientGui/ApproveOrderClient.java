package ClientGui;

import client.ClientUI;
import entities.orderPlaced;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for the screen 
 */

public class ApproveOrderClient {

    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();

    /** Observable list to hold the orders to be displayed in the table. */
    public static ObservableList<orderPlaced> orders;

    /** Observable list to hold the orders ready for approval. */
    public static ObservableList<String> readyOrders;

    @FXML
    private Button btnBack = null;

    @FXML
    private Button btnapprove = null;
    
    /**Message for the user's choice that indicates that status was updated or no order was chosen*/
    @FXML
    private TextField message;
    
    /**Message which is activated if there was a late delivery and discount is applicable*/
    @FXML
    private TextField message1;

    @FXML
    private ComboBox<String> orderapprove;
    /**Setup for the orders placed by client*/

    @FXML
    private TableColumn<orderPlaced, String> orderPlacedTime;

    @FXML
    private TableColumn<orderPlaced, String> ordernumber;

    @FXML
    private TableColumn<orderPlaced, String> orderStatus;

    @FXML
    private TableView<orderPlaced> table;

    /**
     * Handles the event when the back button is pressed.
     * @param event The ActionEvent triggered by the back button.
     * @throws Exception if the screen transition fails.
     */
    @FXML
    public void btnBack(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/FirstOrderScreenO.fxml", event);
    }

    /**
     * Initializes the view by setting up the table columns and loading the orders.
     */
    @FXML
    void initialize() {
    	/**Get the open orders for the client to approve*/
        String toSend = "importOpenOrders " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
        ClientUI.chat.accept(toSend);
    	/**Set properties for the table*/
        ordernumber.setCellValueFactory(new PropertyValueFactory<orderPlaced, String>("orderNumber"));
        orderPlacedTime.setCellValueFactory(new PropertyValueFactory<orderPlaced, String>("orderPlacedTime"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<orderPlaced, String>("orderStatus"));
        /**Set the orders to approve in the table*/
        table.setItems(orders);
        /**Set the orders to approve in combobox selection of the user*/
        orderapprove.setItems(readyOrders);
    }

    /**
     * Updates the status of the selected order and applies a discount if applicable.
     * @param event The ActionEvent triggered by the approval button.
     * @throws Exception if the update process fails.
     */
    @FXML
    private void updateStatus(ActionEvent event) throws Exception {
        long timeDiff;
        Double flag = 0.0; /** Indicates if the client needs to receive a discount (late delivery) */
        message.setText(" ");

        String num = orderapprove.getValue();
        if (num == null) {
        	/** If not order was chosen */
            message.setText("please choose order!");
        } else {
            String formattedDateTime = getCurrentTime(); /* Approval time **/
            orderPlaced order = getRelevantOrder(num);
            /** Check for early delivery */
            if (order.getOrderType().equals("early")) {
                timeDiff = getTimeDifference(formattedDateTime, order.getOrderExpected());
                /**If this is early delivery and time difference is more than 20 minutes there is a 50% discount */
                if (timeDiff > 20) {
                    flag = Double.parseDouble(order.getOrderPrice()) / 2;
                }
            } else {
            	/**If this is regular delivery and time difference is more than one hour there is a 50% discount */
                timeDiff = getTimeDifference(formattedDateTime, order.getOrderReceivedBySuuplier());
                if (timeDiff > 60) {
                    flag = Double.parseDouble(order.getOrderPrice()) / 2;
                }
            }
            /**Updating the status of the order and discount in DB */
            String toSend = "updateStatus " + num + " Finished " + formattedDateTime + " " + helpClass.newClient.getUsername() +
                            " " + helpClass.newClient.getPassword() + " " + flag;

            ClientUI.chat.accept(toSend);
            toSend = "updated successfully! updated orders list will show when re-entering";
            message.setText(toSend);
            message1.setText(" ");
            /**Message Indicating the the order was late and the discount applied */
            if (flag > 0) {
                toSend = "sorry the order was late, you received " + flag + " discount for next purchase";
                message1.setText(toSend);
            }
        }
    }

    /**
     * Gets the current time formatted as a string.
     * @return The current date and time formatted as "yyyy-MM-dd HH:mm:ss".
     */
    private String getCurrentTime() {
        /** Get the current date and time */
        LocalDateTime currentDateTime = LocalDateTime.now();

        /** Define a formatter */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /** Format the current date and time */
        return currentDateTime.format(formatter);
    }

    /**
     * Retrieves the relevant order based on the order number.
     * @param num The order number as a string.
     * @return The corresponding orderPlaced object or null if not found.
     */
    private orderPlaced getRelevantOrder(String num) {
        for (orderPlaced op : orders) {
            if (op.getOrderNumber().equals(num)) {
                return op;
            }
        }
        return null;
    }

    /**
     * Calculates the time difference in minutes between two date-time strings.
     * @param str1 The first date-time string.
     * @param str2 The second date-time string.
     * @return The absolute time difference in minutes.
     */
    private long getTimeDifference(String str1, String str2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /**Parse the strings into LocalDateTime objects*/
        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);

        /**Calculate the duration between the two datetimes*/
        Duration duration = Duration.between(dateTime1, dateTime2);
        return Math.abs(duration.toMinutes());
    }

}
