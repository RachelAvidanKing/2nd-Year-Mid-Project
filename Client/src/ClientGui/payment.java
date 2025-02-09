package ClientGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**This is a class that handles fake screen for payment method.
 * The payments happens in an external system.*/
public class payment {
    helpClass help = new helpClass();
	/**Button that allows the user to pay*/
    @FXML
    private Button btnPay2=null;

    /**
     * Handles the event when the user wants to close the popup screen.
     * 
     * @param event The action event triggered by pressing the button.
     * @throws Exception if there is an error during the close of popup screen.
     */
    @FXML
    private void handlePass(ActionEvent event) throws Exception {
        Stage stage = (Stage) btnPay2.getScene().getWindow();
        stage.close();
    }

}
