package supplier;

import ClientGui.helpClass;
import client.ClientUI;
import entities.branchOrder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controller for the Supplier Page, which allows the supplier to view and update the status of branch orders.
 */
public class SupplierPage {
    @FXML
    private TextField HourTime;

    @FXML
    private TextField MinTime;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnSave;

    @FXML
    private TableColumn<branchOrder, String> orderPlacedTime;

    @FXML
    private TableColumn<branchOrder, String> orderStatus;

    @FXML
    private ComboBox<String> ordernum;

    @FXML
    private TableColumn<branchOrder, String> ordernumber;

    @FXML
    private TableView<branchOrder> table;
    
    public static ObservableList<branchOrder> branchorders;
    
    ArrayList<String> extractedNumbers = new ArrayList<>();
    
    @FXML
    private TextField status;

    @FXML
    private TextArea message;
    
	/**Delivery method for supplier*/
	public static ArrayList<String> message1=null;

    /** Helper class instance to handle screen changes. */
    helpClass help = new helpClass();
    
    /**
     * Initializes the Supplier Page by setting up the table view, populating the ComboBox with order numbers,
     * and adding a listener to update the status field when an order number is selected.
     */
    @FXML
    void initialize() {
        String toSend = "importBranchOrders " + helpClass.newClient.getUsername();
        ClientUI.chat.accept(toSend);
        ordernumber.setCellValueFactory(new PropertyValueFactory<branchOrder, String>("orderNumber"));
        orderPlacedTime.setCellValueFactory(new PropertyValueFactory<branchOrder, String>("orderPlacedTime"));
        orderStatus.setCellValueFactory(new PropertyValueFactory<branchOrder, String>("orderStatus"));
        extractedNumbers = extractOrderNumber();
        table.setItems(branchorders);
        ordernum.setItems(FXCollections.observableArrayList(extractedNumbers));

        // Add listener to the ComboBox
        ordernum.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateStatusField(newValue);
            }
        });
    }
    
    /**
     * Updates the status field based on the selected order number from the ComboBox.
     * 
     * @param selectedOrderNumber the order number selected from the ComboBox
     */
    private void updateStatusField(String selectedOrderNumber) {
        String currentStatus = "";
        for (branchOrder order : branchorders) {
            if (order.getOrderNumber().equals(selectedOrderNumber)) {
                currentStatus = order.getOrderStatus();
                break;
            }
        }
        
        if (currentStatus.equals("Sent")) {
            status.setText("Approved");
        } else if (currentStatus.equals("Approved")) {
            status.setText("Ready");
        } else {
            status.setText(currentStatus); // Maintain the current status if it's neither "Sent" nor "Approved"
        }
    }
    
    /**
     * Updates the status of the selected order and handles time input for "Ready" status.
     * 
     * @param event the action event triggered by clicking the Save button
     * @throws Exception if an error occurs while updating the status or changing screens
     */
    @FXML
    private void updateStatus(ActionEvent event) throws Exception {
    	int locationInArray=-1;
        message.setText("");
        String value = ordernum.getValue();
        if (value == null && branchorders.isEmpty()) {
            message.setText("The table is empty. No orders to change status.");
            status.setText("");
            return;
        }
        if (value == null && !branchorders.isEmpty()) {
            message.setText("Please choose an order number.");
            return;
        }
        else {
        	// Look for the location of relevant object
        	 for (int i=0;i<branchorders.size();i++) {
                 if (branchorders.get(i).getOrderNumber().equals(value)) {
                	 locationInArray=i;
                	 break;     	 
                 }
        	 }

            String toSend = "";
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            
            String currentStatus = status.getText();

            if (currentStatus.equals("Approved")) {
                // Send the command to change the status to "Approved"
                toSend = "ChangeStatusOfOrder " + value + " " + "Approved" + " " + formattedDateTime;
                
                branchorders.get(locationInArray).setOrderStatus("Approved");
                branchorders.get(locationInArray).setOrderPlacedTime(formattedDateTime);
                helpClass.messageDetails.get(locationInArray).setStatus("Approved");
                
            } else if (currentStatus.equals("Ready")) {
                // Send the command to change the status to "Ready" and remove it from the list
                toSend = "ChangeStatusOfOrder " + value + " " + "Ready" + " " + formattedDateTime;
           
                // Set the status in TableView 
               String hours = HourTime.getText().trim();
     	       String min = MinTime.getText().trim();
     	       boolean valid=checkInput(hours,min);
     	       
     	      if(!helpClass.messageDetails.get(locationInArray).getDelievery_type().equals("Self Pickup") && !valid)
        	  {
        		  message.setText("please enter valid time");
        		  return;
        	  }
        	  helpClass.messageDetails.get(locationInArray).setTime(hours+":"+min);
        	  helpClass.messageDetails.get(locationInArray).setStatus("Ready"); 
              removeOrder(value);
            }
            
            ClientUI.chat.accept(toSend);
            table.refresh();
            message.setText("Status updated successfully");
            // Reset the ComboBox selection to allow for re-selection
            ordernum.getSelectionModel().clearSelection();
            helpClass.orderForPopup=helpClass.messageDetails.get(locationInArray);
            changetopay(event);
        }
    }
    
    /**
     * Removes an order from the table and the ComboBox.
     * 
     * @param orderNumber the order number of the order to remove
     */
    private void removeOrder(String orderNumber) {
        branchOrder orderToRemove = null;
        for (branchOrder order : branchorders) {
            if (order.getOrderNumber().equals(orderNumber)) {
                orderToRemove = order;
                break;
            }
        }
        if (orderToRemove != null) {
            branchorders.remove(orderToRemove);
            ordernum.getItems().remove(orderNumber);
        }
    }
    
    /**
     * Logs out the current user and navigates to the login page.
     * 
     * @param event the action event triggered by clicking the Log Out button
     * @throws Exception if an error occurs while changing screens
     */
    public void btnLogOutClick(ActionEvent event) throws Exception {
        String toSend = "LoggedOut " + helpClass.newClient.getUsername() + " " + helpClass.newClient.getPassword();
        ClientUI.chat.accept(toSend);
        help.changeScreen("/ClientGui/LoginP.fxml", event);
    }
    
    /**
     * Extracts order numbers from a list of strings and returns them as an ArrayList.
     * 
     * @return an ArrayList of extracted order numbers
     */
    public ArrayList<String> extractOrderNumber() {
        // Array list of extracted first elements
        ArrayList<String> extractedNumbers = new ArrayList<>();
        for (String str : helpClass.ordernumber) {
            String firstWord = str.split("/")[0];
            extractedNumbers.add(firstWord);
        }
        return extractedNumbers;
    }
    
    /**
     * Checks if the user input for hour and minute is valid.
     * 
     * @param hour the desired hour as a string
     * @param min the desired minute as a string
     * @return true if the input is valid, false otherwise
     */
    public boolean checkInput(String hour, String min) {
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

        return true;
    }
    
    /**
     * Opens a new modal window to display the simulation page.
     * This method loads the FXML file for the simulation page, creates a new stage,
     * and sets it to be modal. The window will remain open until it is closed by the user.
     * 
     * @param event the ActionEvent that triggered this method; can be used to get more event details if needed
     * @throws Exception if there is an error loading the FXML file or showing the stage
     */
    public void changetopay(ActionEvent event) throws Exception
    {
    	  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/supplier/simulationpageO.fxml"));
    	    Parent parent = fxmlLoader.load();

    	    Stage stage = new Stage();
    	    stage.initModality(Modality.APPLICATION_MODAL);
    	    stage.setTitle("Simulation");
    	    stage.setScene(new Scene(parent));
    	    stage.showAndWait();
    }
}
