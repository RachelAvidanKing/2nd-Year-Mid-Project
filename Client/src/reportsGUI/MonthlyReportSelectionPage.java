package reportsGUI;

import java.time.LocalDate;
import java.util.ArrayList;

import ClientGui.helpClass;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import reportsGUI.ReportViewPage.ReportType;

/**
 * Controller class for the monthly report selection page. It manages user input
 * and interactions for selecting and viewing different types of monthly
 * reports.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class MonthlyReportSelectionPage {
	/**
	 * Helper class for changing screens.
	 */
	private helpClass help = new helpClass();
	/**
	 * Instance of the MonthlyReportController used for interacting with the report
	 * data.
	 */
	private MonthlyReportController mrc = MonthlyReportController.getInstance();
	/**
	 * Type of the client who entered the page (e.g., "ceo" or "branch manager").
	 */
	private String clientType;
	/**
	 * Branch identifier for the reports.
	 */
	protected static String branch;
	/**
	 * ComboBox for selecting the type of report (Income Report, Orders Report, or
	 * Performance Report).
	 */
	@FXML
	private ComboBox<String> cmbChooseType;
	/**
	 * ComboBox for selecting the month of the report.
	 */
	@FXML
	private ComboBox<String> cmbChooseMonth;
	/**
	 * ComboBox for selecting the year of the report.
	 */
	@FXML
	private ComboBox<String> cmbChooseYear;
	/**
	 * TextField for displaying or entering the date (though it’s not used in this
	 * implementation).
	 */
	@FXML
	private TextField Date;
	/**
	 * Button to show the selected report.
	 */
	@FXML
	private Button btnShow;
	/**
	 * Button to navigate back to the previous menu.
	 */
	@FXML
	private Button btnBack;
	/**
	 * Label for displaying error messages.
	 */
	@FXML
	private Label lblError;

	/**
	 * Initializes the controller by setting up combo boxes and loading report data
	 * based on client type.
	 */
	@FXML
	public void initialize() {
		clientType = helpClass.newClient.getClient_type();
		switch (clientType) {
		case "ceo":
			mrc.getReportsBranch(branch); // Get reports is using the home branch
			break;
		default:
			if (MonthlyReportController.reports.isEmpty())
				{
				mrc.getReports(helpClass.newClient.gethomeBranch());}
			lblError.setTextFill(Color.RED);
		}
		ArrayList<String> types = new ArrayList<>();
		types.add("Income Report");
		types.add("Orders Report");
		types.add("Performance Report");
		cmbChooseType.setItems(FXCollections.observableArrayList(types));

		// Get possible Strings from DB and fill up the combo boxes.
		cmbChooseMonth.setDisable(true);
		ArrayList<String> years = new ArrayList<>();
		years = mrc.getYears();

		cmbChooseYear.setItems(FXCollections.observableArrayList(years));
		if (cmbChooseYear.getItems().isEmpty()) {
			lblError.setText("No reports available for this branch.");
			cmbChooseType.setPromptText("No Reports Available!");
			cmbChooseType.setDisable(true);
			cmbChooseYear.setPromptText("No Reports Available!");
			cmbChooseYear.setDisable(true);
			cmbChooseMonth.setPromptText("No Reports Available!");
			btnShow.setDisable(true);
		} else {
			lblError.setText("");
			btnShow.setDisable(false);
		}

	}

	/**
	 * Updates the month combo box based on the selected year.
	 * 
	 * @param event The action event triggered by changing the year.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void YearChanged(ActionEvent event) throws Exception {
		String year = cmbChooseYear.getValue();
		ArrayList<String> months = mrc.getMonths(year);
		cmbChooseMonth.setItems(FXCollections.observableArrayList(months));
		cmbChooseMonth.setDisable(false);
	}

	/**
	 * Handles the click event for the Show button. Validates the user selections
	 * and loads the appropriate report page.
	 * 
	 * @param event The action event triggered by clicking the Show button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void btnShowClick(ActionEvent event) throws Exception {
		// Here there are going to be 3 options, we'll load the correct page
		// accordingly.

		// Check Selection
		String reportType = cmbChooseType.getValue();
		String reportYear = cmbChooseYear.getValue();
		String reportMonth = cmbChooseMonth.getValue();
		if (reportType == null) {
			// set a label saying please select type
			lblError.setText("Please select the report type you would like to view.");
			return;
		}
		if (reportYear == null) {
			// set the label to say please select year
			lblError.setText("Please select the year you would like to view.");
			return;
		}
		if (reportMonth == null) {
			// set the label to say please select month
			lblError.setText("Please select the month you would like to view.");
			return;
		}
		// Set the selection somewhere static.
		MonthlyReportController.reportDate = LocalDate.of(Integer.valueOf(reportYear), Integer.valueOf(reportMonth), 1);

		// Load Income Report
		switch (reportType) {
		case "Income Report":
			ReportViewPage.setReportType(ReportType.INCOME_REPORT);
			help.changeScreen("/reportsGUI/ReportViewPage.fxml", event);
			break;
		case "Orders Report":
			ReportViewPage.setReportType(ReportType.ORDER_REPORT);
			help.changeScreen("/reportsGUI/OrderReportViewPage.fxml", event);
			break;
		case "Performance Report":
			help.changeScreen("/reportsGUI/PerformanceReportPage.fxml", event);
			break;
		}
	}

	/**
	 * Handles the click event for the Back button. Navigates to the appropriate
	 * menu based on the client type.
	 * 
	 * @param event The action event triggered by clicking the Back button.
	 * @throws Exception If an error occurs during the operation.
	 */
	public void btnBackClick(ActionEvent event) throws Exception {
		switch (clientType) {
		case "ceo":
			help.changeScreen("/reportsGUI/CEOMenu.fxml", event);
			break;
		default:
			help.changeScreen("/reportsGUI/BranchManagerPage.fxml", event);
			break;
		}
	}
}
