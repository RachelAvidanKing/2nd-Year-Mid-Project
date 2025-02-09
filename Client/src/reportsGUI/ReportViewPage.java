package reportsGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ClientGui.helpClass;
import entities.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller class for Income reports. It displays
 * an income report in a BarChart.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class ReportViewPage {
    /**
     * Enumeration of the different report types.
     */
    enum ReportType {
        INCOME_REPORT, ORDER_REPORT, PERFORMANCE_REPORT
    }

    /**
     * Helper class for changing screens.
     */
    private helpClass help = new helpClass();

    /**
     * Controller instance for accessing monthly report data.
     */
    private MonthlyReportController monthlyReportController;

    /**
     * Type of the report to be displayed.
     */
    private static ReportType reportType;

    /**
     * Label for displaying the title of the report.
     */
    @FXML
    private Label lblReportTitle;

    /**
     * Button to navigate back to the previous screen.
     */
    @FXML
    private Button btnBack;

    /**
     * Bar chart for displaying report data.
     */
    @FXML
    private BarChart<String, Number> barChart;

    /**
     * X-axis of the bar chart.
     */
    @FXML
    private CategoryAxis xAxis;

    /**
     * Y-axis of the bar chart.
     */
    @FXML
    private NumberAxis yAxis;

    /**
     * Initializes the controller and sets up the chart based on the report type.
     */
    @FXML
    public void initialize() {
        monthlyReportController = MonthlyReportController.getInstance();
        ArrayList<Map<String, Object>> reportData = new ArrayList<>();

        switch (reportType) {
            case INCOME_REPORT:
                setupIncomeReport(reportData);
                break;
            case ORDER_REPORT:
                setupOrderReport(reportData);
                break;
            default:
                break;
        }
    }

    /**
     * Sets up the bar chart for displaying the income report.
     * 
     * @param reportData The data to be displayed in the report.
     */
    private void setupIncomeReport(ArrayList<Map<String, Object>> reportData) {
        lblReportTitle.setText("Income Report");

        Map<String, Double> restaurantRevenue = monthlyReportController.produceIncomeReport();
        for (String restaurant : restaurantRevenue.keySet()) {
            Map<String, Object> row = new HashMap<>();
            row.put("Restaurant Name", restaurant);
            row.put("Revenue", restaurantRevenue.get(restaurant));
            reportData.add(row);
        }

        // Set X-axis label for Income Report
        xAxis.setLabel("Restaurant Name");
        yAxis.setLabel("Revenue");

        // Populate the BarChart
        populateBarChart(reportData);
    }

    /**
     * Sets up the bar chart for displaying the order report.
     * 
     * @param reportData The data to be displayed in the report.
     */
    private void setupOrderReport(ArrayList<Map<String, Object>> reportData) {
        lblReportTitle.setText("Order Report");

        Map<String, Report> restaurantOrders = monthlyReportController.produceOrderReport();
        for (String restaurant : restaurantOrders.keySet()) {
            Report report = restaurantOrders.get(restaurant);
            Map<String, Object> row = new HashMap<>();
            row.put("Restaurant Name", restaurant);
            row.put("Salads", report.getNum_Of_Salads());
            row.put("Appetizers", report.getNum_Of_Appetizers());
            row.put("Main Dishes", report.getNum_Of_Main());
            row.put("Desserts", report.getNum_Of_Desserts());
            row.put("Drinks", report.getNum_Of_Drinks());
            reportData.add(row);
        }

        // Set X-axis label for Order Report
        xAxis.setLabel("Product Type");
        yAxis.setLabel("Quantity");

        // Populate the BarChart for Order Report
        populateBarChartForOrderReport(reportData);
    }

    /**
     * Populates the bar chart with data for the income report.
     * 
     * @param reportData The data to be displayed in the bar chart.
     */
    private void populateBarChart(ArrayList<Map<String, Object>> reportData) {
        barChart.getData().clear();

        if (!reportData.isEmpty()) {
            Set<String> keys = new HashSet<>(reportData.get(0).keySet());

            for (String key : keys) {
                if (!key.equals("Restaurant Name")) {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(key);

                    for (Map<String, Object> data : reportData) {
                        String restaurantName = (String) data.get("Restaurant Name");
                        Number value = (Number) data.get(key);

                        series.getData().add(new XYChart.Data<>(restaurantName, value));
                    }

                    barChart.getData().add(series);
                }
            }
        }
    }

    /**
     * Populates the bar chart with data for the order report.
     * 
     * @param reportData The data to be displayed in the bar chart.
     */
    private void populateBarChartForOrderReport(ArrayList<Map<String, Object>> reportData) {
        barChart.getData().clear();

        // The X-axis categories should be the product types
        Set<String> productTypes = new HashSet<>();
        productTypes.add("Salads");
        productTypes.add("Appetizers");
        productTypes.add("Main Dishes");
        productTypes.add("Desserts");
        productTypes.add("Drinks");

        for (Map<String, Object> data : reportData) {
            String restaurantName = (String) data.get("Restaurant Name");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(restaurantName);

            for (String productType : productTypes) {
                Number value = (Number) data.get(productType);
                series.getData().add(new XYChart.Data<>(productType, value));
            }

            barChart.getData().add(series);
        }
    }

    /**
     * Handles the click event for the Back button, navigating to the previous screen.
     * 
     * @param event The action event triggered by clicking the Back button.
     * @throws Exception If an error occurs during navigation.
     */
    @FXML
    public void btnBackClick(ActionEvent event) throws Exception {
        help.changeScreenClose("/reportsGUI/MonthlyReportSelectionPage.fxml", event);
    }

    /**
     * Gets the current report type.
     * 
     * @return The current report type.
     */
    public static ReportType getReportType() {
        return reportType;
    }

    /**
     * Sets the type of the report to be displayed.
     * 
     * @param reportType The type of the report.
     */
    public static void setReportType(ReportType reportType) {
        ReportViewPage.reportType = reportType;
    }
}