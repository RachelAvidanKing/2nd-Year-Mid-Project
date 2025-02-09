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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller class for the order report view page. It manages the display of
 * order reports, including bar charts and pie charts, based on the selected
 * report type.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class OrderReportViewPage {
    /**
     * Enum representing different types of reports.
     */
    enum ReportType {
        INCOME_REPORT, ORDER_REPORT, PERFORMANCE_REPORT
    }

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
     * Type of the report to be displayed.
     */
    private static ReportType reportType;
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
     * BarChart for displaying order quantities.
     */
    @FXML
    private BarChart<String, Number> barChart;
    /**
     * X-axis for the BarChart.
     */
    @FXML
    private CategoryAxis xAxis;
    /**
     * Y-axis for the BarChart.
     */
    @FXML
    private NumberAxis yAxis;
    /**
     * PieChart for displaying the proportion of different product types.
     */
    @FXML
    private PieChart pieCategories;
    /**
     * Label displaying the best-selling item.
     */
    @FXML
    private Label lblBestSelling;
    /**
     * Label displaying the worst-selling item.
     */
    @FXML
    private Label lblWorstSelling;

    /**
     * Initializes the controller and sets up the order report based on the report
     * data.
     */
    @FXML
    public void initialize() {
        monthlyReportController = MonthlyReportController.getInstance();
        ArrayList<Map<String, Object>> reportData = new ArrayList<>();
        setupOrderReport(reportData);
    }

    /**
     * Sets up the order report by configuring the bar chart and pie chart based on
     * the provided report data.
     * 
     * @param reportData The report data to be displayed.
     */
    private void setupOrderReport(ArrayList<Map<String, Object>> reportData) {
        lblReportTitle.setText("Order Report");

        Map<String, Report> restaurantOrders = monthlyReportController.produceOrderReport();
        if (restaurantOrders == null) {
            return;
        }

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

        /**
         * Set X-axis label for Order Report
         */
        xAxis.setLabel("Product Type");
        yAxis.setLabel("Quantity");

        /**
         * Populate the BarChart for Order Report
         */
        populateBarChartForOrderReport(reportData);

        /**
         *  Populate PieChart and Labels for Best and Worst Selling
         */
        populatePieChartAndLabels(reportData);
    }

    /**
     * Populates the BarChart with order data for each restaurant.
     * 
     * @param reportData The report data to be displayed.
     */
    private void populateBarChartForOrderReport(ArrayList<Map<String, Object>> reportData) {
        barChart.getData().clear();

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
     * Populates the PieChart with total sales data and updates labels for best and
     * worst selling items.
     * 
     * @param reportData The report data to be displayed.
     */
    private void populatePieChartAndLabels(ArrayList<Map<String, Object>> reportData) {
        pieCategories.getData().clear();

        /**
         * Use a HashMap to allow modifications
         */
        Map<String, Double> totalSales = new HashMap<>();
        totalSales.put("Salads", 0.0);
        totalSales.put("Appetizers", 0.0);
        totalSales.put("Main Dishes", 0.0);
        totalSales.put("Desserts", 0.0);
        totalSales.put("Drinks", 0.0);

        /**
         * Aggregate sales data
         */
        for (Map<String, Object> data : reportData) {
            for (Entry<String, Double> entry : totalSales.entrySet()) {
                String productType = entry.getKey();
                Number value = (Number) data.get(productType);
                totalSales.put(productType, totalSales.get(productType) + value.doubleValue());
            }
        }

        /**
         * Populate PieChart
         */
        for (Entry<String, Double> entry : totalSales.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieCategories.getData().add(slice);
        }

        /**
         *  Find best and worst selling items
         */
        String bestSelling = "";
        String worstSelling = "";
        double maxSales = Double.NEGATIVE_INFINITY;
        double minSales = Double.POSITIVE_INFINITY;

        for (Entry<String, Double> entry : totalSales.entrySet()) {
            double sales = entry.getValue();
            if (sales > maxSales) {
                maxSales = sales;
                bestSelling = entry.getKey();
            }
            if (sales < minSales) {
                minSales = sales;
                worstSelling = entry.getKey();
            }
        }

        lblBestSelling.setText(bestSelling.isEmpty() ? "None" : bestSelling);
        lblWorstSelling.setText(worstSelling.isEmpty() ? "None" : worstSelling);
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
     * Sets the report type to be displayed.
     * 
     * @param reportType The type of the report to be displayed.
     */
    public static void setReportType(ReportType reportType) {
        OrderReportViewPage.reportType = reportType;
    }
}