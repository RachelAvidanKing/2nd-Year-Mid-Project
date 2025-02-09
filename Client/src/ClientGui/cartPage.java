package ClientGui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/** 
 * Class which shows the user's current cart and price of each meal.
 * It provides functionality to view the cart, navigate back to the menu, and proceed to choose the supply method.
 */
public class cartPage {

    /** Helper class instance to handle common functionalities. */
    private helpClass help = new helpClass();

    /** Observable list to hold the meal data to be displayed in the table. */
    public static ObservableList<Meal> dishes;

    @FXML
    private Button btnBack = null;

    @FXML
    private Button btnFinishAdd = null;
    
    /** Message which indicates whether there was something added to the cart. */
    @FXML
    private Label message;
    
    /** TableView to display the meals added to the cart, their price and optional components. */
    @FXML
    private TableView<Meal> table;
    
    @FXML
    private TableColumn<Meal, String> name;

    @FXML
    private TableColumn<Meal, String> optionalComponents;

    @FXML
    private TableColumn<Meal, String> price;
    
    /**
     * Initializes the view by setting up the table columns and loading the current user's cart.
     * This method is automatically called after the FXML layout has been loaded.
     */
    @FXML
    void initialize() {
        dishes = FXCollections.observableArrayList();
        
        /** Get the dishes from helpClass and add them to the observable list. */
        dishes.addAll(helpClass.order.getDishList());

        /** Set properties for the table columns. */
        name.setCellValueFactory(new PropertyValueFactory<Meal, String>("name"));
        optionalComponents.setCellValueFactory(new PropertyValueFactory<Meal, String>("optionalComponents"));
        price.setCellValueFactory(new PropertyValueFactory<Meal, String>("price"));
        
        /** Set the observable list of dishes in the table to be displayed. */
        table.setItems(dishes);
    }
    
    /**
     * Handles the event when the back button is clicked.
     * Navigates the user back to the main menu screen.
     * @param event The ActionEvent triggered by the back button click.
     * @throws Exception if the screen transition fails.
     */
    public void btnBackClick(ActionEvent event) throws Exception {
        help.changeScreen("/ClientGui/MenuGui.fxml", event);
    }
    
    /**
     * Handles the event when the user attempts to move to the supply method selection.
     * If the cart is empty, displays a message prompting the user to add a dish.
     * If the cart is not empty, navigates to the Choose Supply Method page.
     * @param event The ActionEvent triggered by the button click to proceed.
     * @throws Exception if the screen transition fails.
     */
    public void btnMoveToSupply(ActionEvent event) throws Exception {
        if (helpClass.order.getDishList().size() != 0) {
            message.setText("");
            help.changeScreen("/ClientGui/ChooseSupplyMethodPage.fxml", event);
        } else {
            message.setText("you must add a dish to continue");
        }
    }
}
