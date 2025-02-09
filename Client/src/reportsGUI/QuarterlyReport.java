package reportsGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import ClientGui.helpClass;

/**
 * Controller class for the quarterly report page. It manages the selection of
 * restaurants and quarters and provides navigation options for showing reports.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class QuarterlyReport {
	/**
	 * Helper class for changing screens.
	 */
	helpClass help = new helpClass();
	/**
	 * ComboBox for selecting a restaurant.
	 */
	@FXML
	private ComboBox<String> cmbChooseRestaurant;
	/**
	 * ComboBox for selecting a quarterly period.
	 */
	@FXML
	private ComboBox<String> cmbChooseQuarterly;
	/**
	 * Button for showing the selected report.
	 */
	@FXML
	private Button btnShow = null;
	/**
	 * Button for navigating back to the CEO GUI.
	 */
	@FXML
	private Button btnBack = null;

	/**
	 * Handles the click event for the Show button and navigates to the report
	 * display page.
	 * 
	 * @param event The action event triggered by clicking the Show button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void btnShowClick(ActionEvent event) throws Exception {
		help.changeScreen("/ClientGui/ReportShow.fxml", event); // Show the reports from DB
	}

	/**
	 * Handles the click event for the Back button and navigates back to the CEO
	 * GUI.
	 * 
	 * @param event The action event triggered by clicking the Back button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void btnBackClick(ActionEvent event) throws Exception {
		help.changeScreen("/ClientGui/CEOGui.fxml", event);
	}
}
