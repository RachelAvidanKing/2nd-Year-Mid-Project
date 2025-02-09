package reportsGUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * Controller class for the quarterly report page. It manages the display of
 * order and income reports for the selected quarter, year, and branch, including
 * bar charts for visual representation.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class QuarterlyReportPage {
	/**
     * Stage for the current window.
     */
	private Stage window;
	/**
     * Controller instance for accessing monthly report data.
     */
	private MonthlyReportController reportController;
	/**
     * Selected quarter for the report.
     */
	static String selectedQuarter;
	/**
     * Selected year for the report.
     */
	static String selectedYear;
	/**
     * Selected branch for the report.
     */
	static String selectedBranch;
	/**
     * Data for the quarterly report, including income and order data.
     */
	private ArrayList<Map<String, Map<LocalDate, Double>>> quarterlyData;
	/**
     * Label displaying the selected year.
     */
	@FXML
	private Label lblSelectedYear;
	/**
     * Label displaying the selected quarter.
     */
	@FXML
	private Label lblSelectedQuarter;
	/**
     * Label displaying the selected branch.
     */
	@FXML
	private Label lblSelectedBranch;
	/**
     * Label displaying the report title.
     */
	@FXML
	private Label lblReportTitle;
	/**
     * Button for navigating back to the previous page.
     */
	@FXML
	private Button btnBack;
	/**
     * BarChart for displaying order data.
     */
	@FXML
	private BarChart<String, Number> orderChart;
	/**
     * BarChart for displaying income data.
     */
	@FXML
	private BarChart<String, Number> incomeChart;
	/**
     * Initializes the controller, loads the quarterly report data, and populates
     * the charts with the retrieved data.
     */
	@FXML
	public void initialize() {
		reportController = MonthlyReportController.getInstance();
		quarterlyData = reportController.produceQuarterlyReport(selectedQuarter, selectedYear);
		lblSelectedBranch.setText(selectedBranch);
		lblSelectedYear.setText(selectedYear);
		lblSelectedQuarter.setText(selectedQuarter);
		populateOrders();
		populateIncome();
	}
	
	/**
     * Handles the click event for the Back button and closes the current window.
     * 
     * @param event The action event triggered by clicking the Back button.
     * @throws Exception If an error occurs during the operation.
     */
	@FXML
	public void btnBackClick(ActionEvent event) throws Exception {
		QuarterlyReportSelectionPage.removeWindow(window);
		window.close();
	}
	
	/**
     * Gets the current stage (window) of the application.
     * 
     * @return The current stage.
     */
	public Stage getStage() {
		return window;
	}
	/**
     * Sets the stage (window) for the application.
     * 
     * @param stage The stage to be set.
     */
	public void setStage(Stage stage) {
		this.window = stage;
	}
	/**
     * Populates the order chart with data from the quarterly report.
     */
	public void populateOrders() {
		/**
		 * Clear existing data from the chart
		 */
		orderChart.getData().clear();

		/**
		 * Fetch quarterly income data
		 */
		Map<String, Map<LocalDate, Double>> quarterlyIncomeData = quarterlyData.get(1);

		/**
		 *  Iterate over each restaurant to create a series
		 */
		for (Map.Entry<String, Map<LocalDate, Double>> entry : quarterlyIncomeData.entrySet()) {
			String restaurantName = entry.getKey();
			Map<LocalDate, Double> dailyIncome = entry.getValue();

			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(restaurantName); 

			/**
			 * Convert the map entries to a list and sort by date
			 */
			List<Map.Entry<LocalDate, Double>> sortedEntries = new ArrayList<>(dailyIncome.entrySet());
			sortedEntries.sort(Map.Entry.comparingByKey());

			/**
			 * Add sorted data to series
			 */
			for (Map.Entry<LocalDate, Double> incomeEntry : sortedEntries) {
				LocalDate date = incomeEntry.getKey();
				Double income = incomeEntry.getValue();

				/**
				 * Convert LocalDate to a formatted string for the X-axis
				 */
				String formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMM")); // Example format: 1 Aug

				/**
				 * Check if the data is not null
				 */
				if (income != null) {
					series.getData().add(new XYChart.Data<>(formattedDate, income));
				}
			}

			// Add series to the chart
			orderChart.getData().add(series);
		}

		// The legend should be visible by default
		orderChart.setLegendVisible(true);
	}
	/**
     * Populates the income chart with data from the quarterly report.
     */
	public void populateIncome() {
		// Clear existing data from the chart
		incomeChart.getData().clear();

		// Fetch quarterly income data
		Map<String, Map<LocalDate, Double>> quarterlyIncomeData = quarterlyData.get(0);

		// Iterate over each restaurant to create a series
		for (Map.Entry<String, Map<LocalDate, Double>> entry : quarterlyIncomeData.entrySet()) {
			String restaurantName = entry.getKey();
			Map<LocalDate, Double> dailyIncome = entry.getValue();

			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName(restaurantName); // This name will appear in the legend

			// Convert the map entries to a list and sort by date
			List<Map.Entry<LocalDate, Double>> sortedEntries = new ArrayList<>(dailyIncome.entrySet());
			sortedEntries.sort(Map.Entry.comparingByKey());

			// Add sorted data to series
			for (Map.Entry<LocalDate, Double> incomeEntry : sortedEntries) {
				LocalDate date = incomeEntry.getKey();
				Double income = incomeEntry.getValue();

				// Convert LocalDate to a formatted string for the X-axis
				String formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMM")); // Example format: 1 Aug 2024

				// Check if the data is not null
				if (income != null) {
					series.getData().add(new XYChart.Data<>(formattedDate, income));
				}
			}

			// Add series to the chart
			incomeChart.getData().add(series);
		}

		// The legend should be visible by default
		incomeChart.setLegendVisible(true);
	}
}