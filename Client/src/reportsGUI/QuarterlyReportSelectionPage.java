package reportsGUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ClientGui.helpClass;
import entities.DeliveryBranch;

/**
 * Controller class for the quarterly report selection page. It manages user
 * interactions for selecting a branch, year, and quarter, and opens the
 * corresponding report page.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class QuarterlyReportSelectionPage {
	/**
	 * Helper class for changing screens.
	 */
	helpClass help = new helpClass();
	/**
	 * Mapping of branch names to their IDs.
	 */
	static Map<String, String> branchStrings;
	/**
	 * Controller instance for accessing monthly report data.
	 */

	private MonthlyReportController reportController;
	/**
	 * Selected branch for the report.
	 */
	protected static String selectedBranch;
	/**
	 * Selected branch string for display purposes.
	 */
	static String selectedBranchString;
	/**
	 * Selected year for the report.
	 */
	static String selectedYear;
	/**
	 * List of open quarterly report stages.
	 */
	private static List<Stage> openQuarterlyReports = new ArrayList<>();
	/**
	 * Maximum number of open quarterly report stages allowed.
	 */
	private final int MAX_OPEN_REPORTS = 2;
	/**
	 * Label to display errors related to report selection.
	 */
	@FXML
	private Label lblShowError;
	/**
	 * ComboBox for selecting the branch.
	 */
	@FXML
	private ComboBox<String> cmbChooseBranch;
	/**
	 * ComboBox for selecting the quarter.
	 */
	@FXML
	private ComboBox<String> cmbChooseQuarter;
	/**
	 * Button to show the selected report.
	 */
	@FXML
	private Button btnShow;
	/**
	 * Button to navigate back to the previous screen.
	 */
	@FXML
	private Button btnBack;
	/**
	 * ComboBox for selecting the year.
	 */
	@FXML
	private ComboBox<String> cmbChooseYear;
	/**
	 * Label to display errors related to branch selection.
	 */
	@FXML
	private Label lblBranchError;
	/**
	 * Label to display errors related to year selection.
	 */
	@FXML
	private Label lblYearError;
	/**
	 * Label to display errors related to quarter selection.
	 */
	@FXML
	private Label lblQuarterError;

	/**
	 * Initializes the controller, populates the branch combo box, and sets up
	 * initial states for year and quarter selections.
	 */
	@FXML
	public void initialize() {
		reportController = MonthlyReportController.getInstance();
		reportController.getReportsBranch(selectedBranch);
		ArrayList<DeliveryBranch> branches = reportController.branches;
		branchStrings = new HashMap<>();
		for (DeliveryBranch b : branches)
			branchStrings.put(b.getName(), b.getID());
		cmbChooseBranch.setItems(FXCollections.observableArrayList(branchStrings.keySet()));
		cmbChooseBranch.setValue(selectedBranchString);
		fillYears();
		cmbChooseQuarter.setDisable(true);
	}

	/**
	 * Handles the branch selection event. Updates available years based on the
	 * selected branch and resets the quarter selection.
	 * 
	 * @param event The action event triggered by selecting a branch.
	 */
	@FXML
	public void branchSelected(ActionEvent event) {
		selectedBranch = branchStrings.get(cmbChooseBranch.getValue());
		reportController.getReportsBranch(selectedBranch);
		resetYears();
		fillYears();
		restQuarters();
		cmbChooseQuarter.setDisable(true);
	}

	/**
	 * Handles the year selection event. Updates available quarters based on the
	 * selected year.
	 * 
	 * @param event The action event triggered by selecting a year.
	 */
	@FXML
	public void yearSelected(ActionEvent event) {
		if (cmbChooseYear.getValue() != null) {
			selectedYear = cmbChooseYear.getValue();
			restQuarters();
			fillQuarters();
		}
		cmbChooseQuarter.setDisable(false);
		cmbChooseQuarter.requestLayout();
	}

	/**
	 * Handles the quarter selection event. Currently, no specific actions are
	 * taken.
	 * 
	 * @param event The action event triggered by selecting a quarter.
	 */
	@FXML
	public void quarterSelected(ActionEvent event) {
	}

	/**
	 * Handles the click event for the Show button. Validates user input, displays
	 * errors if necessary, and opens a new quarterly report page if input is valid
	 * and the maximum number of open reports has not been reached.
	 * 
	 * @param event The action event triggered by clicking the Show button.
	 */
	@FXML
	public void btnShowClick(ActionEvent event) {
		clearErrors();
		if (checkInput()) {
			if (cmbChooseBranch.getValue() == null)
				lblBranchError.setText("Please select a branch to display.");
			else
				lblBranchError.setText("");
			if (cmbChooseYear.getValue() == null)
				lblYearError.setText("Please select a year to display.");
			else
				lblYearError.setText("");
			if (cmbChooseQuarter.getValue() == null)
				lblQuarterError.setText("Please select a quarter to display.");
			else
				lblQuarterError.setText("");
		} else if (openQuarterlyReports.size() < MAX_OPEN_REPORTS) {
			try {
				QuarterlyReportPage.selectedBranch = cmbChooseBranch.getValue();
				QuarterlyReportPage.selectedQuarter = cmbChooseQuarter.getValue();
				QuarterlyReportPage.selectedYear = cmbChooseYear.getValue();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/reportsGUI/QuarterlyReportPage.fxml"));
				Parent root = loader.load();

				Stage window = new Stage();
				window.setTitle("BiteMe");

				QuarterlyReportPage quarterlyReportPage = loader.getController();
				quarterlyReportPage.setStage(window);

				openQuarterlyReports.add(window);
				window.setOnCloseRequest(e -> removeWindow(window));
				Scene scene = new Scene(root);
				window.setScene(scene);
				window.show();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			lblShowError.setText("Can only display 2 reports at once!");
	}

	/**
	 * Checks if any input fields (branch, year, or quarter) are empty.
	 * 
	 * @return True if any input field is empty, otherwise false.
	 */
	private boolean checkInput() {
		return (cmbChooseBranch.getValue() == null || cmbChooseYear.getValue() == null
				|| cmbChooseQuarter.getValue() == null);
	}

	/**
	 * Clears all error labels.
	 */
	private void clearErrors() {
		lblBranchError.setText("");
		lblYearError.setText("");
		lblQuarterError.setText("");
		lblShowError.setText("");
	}

	/**
	 * Handles the click event for the Back button, navigating to the CEO menu.
	 * 
	 * @param event The action event triggered by clicking the Back button.
	 * @throws Exception If an error occurs during the operation.
	 */
	@FXML
	public void btnBackClick(ActionEvent event) throws Exception {
		help.changeScreen("/reportsGUI/CEOMenu.fxml", event);
	}

	/**
	 * Resets the year selection by clearing the combo box and setting the prompt
	 * text.
	 */
	private void resetYears() {
		cmbChooseYear.setValue(null);
		cmbChooseYear.getItems().clear();
		cmbChooseYear.setPromptText("Select Year");
	}

	/**
	 * Resets the quarter selection by clearing the combo box and setting the prompt
	 * text.
	 */
	private void restQuarters() {
		cmbChooseQuarter.setValue(null);
		cmbChooseQuarter.getItems().clear();
		cmbChooseQuarter.setPromptText("Select Quarter");
	}

	/**
	 * Fills the year combo box with available years based on the selected branch.
	 */
	private void fillYears() {
		ArrayList<String> years = new ArrayList<>();
		years = reportController.getYears();
		if (!years.isEmpty()) {
			lblShowError.setText("");
			cmbChooseYear.setItems(FXCollections.observableArrayList(years));
			cmbChooseYear.setDisable(false);
			btnShow.setDisable(false);
		} else {
			lblShowError.setText("No reports available for that branch.");
			cmbChooseYear.setDisable(true);
			cmbChooseYear.setValue("No reports available.");
			cmbChooseQuarter.setValue("No reports available.");
			btnShow.setDisable(true);
		}
	}

	/**
	 * Fills the quarter combo box with available quarters based on the selected
	 * year.
	 */
	private void fillQuarters() {
		ArrayList<String> quarters = new ArrayList<>();
		quarters = reportController.getQuarters(selectedYear);
		cmbChooseQuarter.setItems(FXCollections.observableArrayList(quarters));
	}

	/**
	 * Removes the specified window from the list of open quarterly reports.
	 * 
	 * @param window The stage (window) to be removed.
	 */
	public static void removeWindow(Stage window) {
		openQuarterlyReports.remove(window);
	}
}