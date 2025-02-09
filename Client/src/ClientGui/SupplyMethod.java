package ClientGui;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import client.ClientUI;
import entities.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SupplyMethod {
    helpClass help = new helpClass();
    protected static int pay=0;
    public static String branch="";
    public static ObservableList<String> deliveryMethods = FXCollections.observableArrayList();
    public static ArrayList<String> methodsList = null;
    public static ObservableList<String> cities = FXCollections.observableArrayList();
    public static ArrayList<String> citiesList = null;
    public static ObservableList<String> streets = FXCollections.observableArrayList();
    public static ArrayList<String> streetsList = null;
    
    @FXML
    private Button btnBack = null;

    @FXML
    private Button btnFinishOrder = null;
    @FXML
    private Button btnPay=null;
    @FXML
    private TextArea message;

    @FXML
    private ComboBox<String> comboCity;

    @FXML
    private ComboBox<String> comboMethod;

    @FXML
    private ComboBox<String> comboStreet;

    @FXML
    private TextField num;

    @FXML
    private TextField txtAddreesWorkplace;

    @FXML
    private TextField txtOrderNum;

    @FXML
    private TextField txtPhoneNum;

    @FXML
    private TextField txtRecipientName;
    @FXML
    private RadioButton selectAddress;

    @FXML
    private RadioButton selectWorkplace;
    
    private int check=0;

    @FXML
    private void initialize() { 
    	pay=0;
    	deliveryMethods = FXCollections.observableArrayList();
    	
        ClientUI.chat.accept("methods");
        deliveryMethods.addAll(methodsList);

        
        comboMethod.setItems(deliveryMethods);
     // Disable all controls initially
        setAllControlsDisabled(true);
        
        comboMethod.setOnAction(event -> {
        	
            String selectedMethod = comboMethod.getSelectionModel().getSelectedItem();
            boolean isRegular=selectedMethod.equalsIgnoreCase("Regular");
            if (selectedMethod != null)
            {
            	//enter cities values
                ClientUI.chat.accept("cities");
                cities.addAll(citiesList);
                comboCity.setItems(cities);
            	if(isRegular||selectedMethod.equalsIgnoreCase("joint"))
            	{
            		setAllControlsDisabled(false);
            		
            		if(isRegular)
            		{
            			txtOrderNum.setDisable(true);//block joint delivery
            		}
            		comboCity.setDisable(false);
                    comboStreet.setDisable(false);
            	}
            	else
            	{
            		setAllControlsDisabled(false);
            		txtOrderNum.setDisable(true);//block joint delivery
            		comboCity.setDisable(false);
                    comboStreet.setDisable(true);
                    num.setDisable(true);
                    txtAddreesWorkplace.setDisable(true);
                    selectAddress.setDisable(true);
                    selectWorkplace.setDisable(true);
            	}
            }
        });
        comboCity.setOnAction(event -> {
            if (selectAddress.isSelected()) {  // Check if Address radio button is selected
                String selectCity = comboCity.getSelectionModel().getSelectedItem();
                if (selectCity != null) {
                    comboStreet.setDisable(false); // Enable comboStreet if city is selected
                    ClientUI.chat.accept("streets:" + comboCity.getValue());
                    streets.clear();
                    streets.addAll(streetsList);
                    comboStreet.setItems(streets);
                } else {
                    comboStreet.setDisable(true);
                }
            } else {
                comboStreet.setDisable(true);  // Ensure it's disabled if Address is not selected
            }
        });
        
        // Manually handle radio button selections
        selectAddress.setOnAction(event -> handleRadioButtonSelection(selectAddress));
        selectWorkplace.setOnAction(event -> handleRadioButtonSelection(selectWorkplace));
        
    }
    
    private void setAllControlsDisabled(boolean disabled) {
        comboCity.setDisable(disabled);
        comboStreet.setDisable(disabled);
        num.setDisable(disabled);
        txtAddreesWorkplace.setDisable(disabled);
        txtOrderNum.setDisable(disabled);
        txtPhoneNum.setDisable(disabled);
        txtRecipientName.setDisable(disabled);
        selectAddress.setDisable(disabled);
        selectWorkplace.setDisable(disabled);
        btnFinishOrder.setDisable(disabled);
        btnPay.setDisable(disabled);
    }
    
    private void handleRadioButtonSelection(RadioButton selectedRadioButton) {
        if (selectedRadioButton == selectAddress) {
            selectWorkplace.setSelected(false);
            num.setDisable(false);
            comboStreet.setDisable(false);
            txtAddreesWorkplace.setDisable(true);
        } else if (selectedRadioButton == selectWorkplace) {
            selectAddress.setSelected(false);
            num.setDisable(true);
            comboStreet.setDisable(true);
            txtAddreesWorkplace.setDisable(false);
        }
    }


    public void getBackBtn(ActionEvent event) throws Exception {
        help.generalBtnBack("/ClientGui/MenuGui.fxml", event);
    }


    public void btnFinishOrder(ActionEvent event) throws Exception 
    {
    	if(pay==0)
    	{
    		message.setText("please pay first!");
    		return;
    	}
    	/**send order's details to the server */
    	ArrayList<Object> toSend=new ArrayList<>();
    	toSend.add("addToOrders");
    	toSend.add(helpClass.newClient);
    	toSend.add(helpClass.order);
    	 ClientUI.chat.accept(toSend);
        help.changeScreen("/ClientGui/OrderSummary.fxml", event);
    }
    
    /**
     * Validates the general input fields required for all types of orders.
     * This method checks if the city has been selected, the name is entered correctly,
     * and the phone number is valid.
     *
     * @param name The name of the recipient.
     * @param phoneNumber The phone number of the recipient.
     * @param city The city for the delivery.
     * @return A string containing error messages if validations fail, otherwise an empty string.
     */
    public String checkForEveryone(String name, String phoneNumber, String city) {
        String msg = "";
        if (city == null) {
            msg += "choose city\n";  /** Prompt to choose a city if not selected*/
        }
        if (name.equals("") || !help.checkStringIsOk(name)) {
            msg += "enter name properly\n ";  /** Check if name is non-empty and valid*/
        }
        if (phoneNumber.equals("") || !help.checkNumIsInt(phoneNumber)) {
            msg += "enter phone properly\n";  /** Validate that the phone number is numeric*/
        }
        return msg;
    }

    /**
     * Checks inputs specific to the "Regular" delivery method, including validation based on
     * whether the delivery is to a workplace or a residential address.
     *
     * @param workplace Indicates if the delivery is to a workplace.
     * @param address Indicates if the delivery is to a residential address.
     * @param work The workplace address, if applicable.
     * @param street The street for delivery, if applicable.
     * @param houseNum The house number for delivery, if applicable.
     * @return A string containing error messages if validations fail, otherwise an empty string.
     */
    public String checkRegular(boolean workplace, boolean address, String work, String street, String houseNum) {
        String msg = "";

        if (workplace) {
            if (work.equals("") || !help.checkStringIsOk(work)) {
                msg += "enter workplace properly\n";  /** Validate workplace address input*/
            }
        } else if (address) {
            if (street == null) {
                msg += "choose street\n";  /** Prompt to choose a street if not selected*/
            }
            if (houseNum.equals("") || !help.checkNumIsInt(houseNum)) {
                msg += "enter house number properly\n";  /** Check if house number is numeric*/
            }
        } else {
            msg += "please choose delivery method\n";  
        }
        return msg;
    }
    
    /**
     * Processes the payment step of an order, including validation of inputs and setting delivery details.
     * If all validations pass and payment has not been previously completed, it transitions to the payment screen.
     * It also calculates the final price including delivery and discounts, and sets various order details.
     *
     * @param event The action event triggered by a payment-related action.
     * @throws Exception If an error occurs during processing or navigation.
     */
    
    public void getBackpay(ActionEvent event) throws Exception {
    	message.setText("");
        /** Check if payment has already been made to prevent multiple payments*/
    	if(pay>=1)
    	{
    		message.setText("you already paid");
    		return;
    	}
    	String delivery=comboMethod.getValue(),cityName=comboCity.getValue(),streetName=comboStreet.getValue();
    	String name=txtRecipientName.getText().trim(),houseNum=num.getText().trim(),phone=txtPhoneNum.getText().trim(),
    			work=txtAddreesWorkplace.getText().trim(), people=txtOrderNum.getText().trim();
    	boolean workplace=selectWorkplace.isSelected(),address=selectAddress.isSelected();
    	if(delivery==null)
    	{
    		message.setText("you must choose delivery method");
    		return;
    	}
    	 /** Perform validation checks for all input fields and accumulate error messages if any*/
    	String toPrint=checkForEveryone(name,phone,cityName);
    	/** Handle different delivery methods and perform specific checks for each*/
    	switch(delivery)
    	{
    	case "Regular":
    		helpClass.order.setDeliveryMethod("Regular");
    		toPrint+=checkRegular(workplace,address,work,streetName,houseNum);
    		if(!toPrint.equals(""))
    		{
    			message.setText(toPrint);
    			return;
    		}
    		break;
    	case "Self Pickup":
    		helpClass.order.setDeliveryMethod("Self Pickup");
    		if(!toPrint.equals(""))
    		{
    			message.setText(toPrint);
    			return;
    		}
    		break;
    	case "Joint":
    		helpClass.order.setDeliveryMethod("Joint");
    		toPrint+=checkJoint(people);
    		if(!toPrint.equals(""))
    		{
    			message.setText(toPrint);
    			return;
    		}
    		break;
    	}
    	
    	/**all fields are filled, update flag of payment, move to payment screen and save order details*/
    	changetopay(event);
    	pay++;
        helpClass.order.setCity(cityName);
        if(address)
        {
        	helpClass.order.setStreet(streetName);
        }
        else
        {
        	helpClass.order.setWorkplace(txtAddreesWorkplace.getText());
        }
        helpClass.order.setHomeNum(houseNum);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        helpClass.order.setStartTime(LocalTime.now().format(formatter));
        helpClass.order.setPhoneNum(phone);
        helpClass.order.setNickname(name);
        //to get restaurant branch
        ClientUI.chat.accept("getRestaurantBranch:"+helpClass.order.getRestaurant()+":"+cityName);
        helpClass.order.setBranch(branch);
        double price=helpClass.order.getFinalPrice()-Double.parseDouble(helpClass.newClient.getDiscount());
        if(price<0.0)
        {
        	price=0.0;
        }
        
        price+=calculateDelivery(delivery,people);
        helpClass.order.setFinalPrice(price);
    }
    
    
    /**
     * Calculates the delivery charge based on the selected method and number of people.
     * For "Regular" delivery, a fixed charge is applied.
     * For "Joint" delivery, a discounted rate is applied based on the number of people, up to a maximum of 3.
     * 
     * @param method The delivery method selected by the user ("Regular" or "Joint").
     * @param people The number of people involved in a "Joint" delivery, ignored for "Regular".
     * @return The calculated delivery price.
     */
    private double calculateDelivery(String method, String people) {
        Double price = 0.0; 
        switch (method) {
            case "Regular":
                price = 25.0; /** Fixed price for regular delivery */
                break;
            case "Joint":
                /** Convert the string representation of people to an integer */
                Integer peopleInt = Integer.parseInt(people);
                /** Calculate the number of people for discount, capped at 3*/
                Integer amount = Math.min(peopleInt, 3);
                /** Calculate the discounted price based on the number of people
                 * Maximum discount is 15
                 * For each person who joins between 1-3 there is a discount of 5*/
                price = (25 - (amount - 1) * 5.0) * amount;
                break;
        }
        return price; // Return the computed price
    }
    
    
    
    /**
     * Navigates to the payment screen.
     * @param event the action event that triggered this method
     * @throws Exception if navigation fails
     */
    public void changetopay(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientGui/payscreen.fxml"));
        Parent parent = fxmlLoader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Payment");
        stage.setScene(new Scene(parent));
        stage.showAndWait(); /** This will block until the popup is closed */
    }
    
    /**
     * Checks and validates the joint delivery input.
     * @param people the string representing the number of people
     * @return a message indicating errors or an empty string if valid
     */
    public String checkJoint(String people) {
        String msg = "";
        if (people.equals("") || !help.checkNumIsInt(people)) {
            msg += "enter amount of people properly\n";
        }
        return msg;
    }
}