package reportsGUI;

import java.util.Map;

import ClientGui.helpClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
/**
 * Controller class for the performance report page. It manages the display of
 * performance reports, including a pie chart and delivery statistics.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class PerformanceReportPage {
	/**
     * Helper class for changing screens.
     */
	private helpClass help = new helpClass();
	/**
     * Instance of the MonthlyReportController used for interacting with the report
     * data.
     */
	private MonthlyReportController monthlyReportController;
	/**
     * Label displaying the total number of deliveries.
     */
	@FXML
	private Label lblDeliveriesTotal;
	 /**
     * Label displaying the number of on-time deliveries.
     */
	@FXML
	private Label lblDeliveriesOnTime;
	/**
     * Label displaying the number of late deliveries.
     */
	@FXML
	private Label lblDeliveriesLate;
	/**
     * PieChart for displaying the proportion of on-time and late deliveries.
     */
	@FXML
	private PieChart pieOrders;
	/**
     * Button for navigating back to the previous page.
     */
	@FXML
	private Button btnBack;
	/**
     * Initializes the controller and sets up the performance report based on the
     * report data.
     */
	@FXML
	public void initialize() {
		monthlyReportController = MonthlyReportController.getInstance();
		Map<String, Integer> performanceData = monthlyReportController.producePerformanceReport();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("On Time", performanceData.get("On Time Deliveries")),
				new PieChart.Data("Late", performanceData.get("Late Deliveries")));
		pieOrders.setData(pieChartData);
		lblDeliveriesTotal.setText(performanceData.get("Total Deliveries").toString());
		lblDeliveriesOnTime.setText(performanceData.get("On Time Deliveries").toString());
		lblDeliveriesLate.setText(performanceData.get("Late Deliveries").toString());
	}
	
	/**
     * Handles the click event for the Back button and navigates back to the
     * MonthlyReportSelectionPage.
     * 
     * @param event The action event triggered by clicking the Back button.
     * @throws Exception If an error occurs during the operation.
     */
	@FXML
	public void btnBackClick(ActionEvent event) throws Exception {
		help.changeScreen("/reportsGUI/MonthlyReportSelectionPage.fxml", event);
	}
}