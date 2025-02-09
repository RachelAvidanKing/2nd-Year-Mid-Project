package reportsGUI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entities.DeliveryBranch;
import entities.Message;
import entities.Report;

/**
 * Singleton controller class responsible for managing and providing
 * functionalities related to monthly and quarterly reports, including loading
 * combo boxes and generating various types of reports.
 * 
 * @author Daniel Feldman
 * @version August 2024
 */
public class MonthlyReportController {
	/**
	 * The single instance of the class.
	 */
	private static MonthlyReportController instance;
	/**
	 * The server communication controller instance used for server communication.
	 */
	public static ServerCommunicationController scc;
	/**
	 * List of reports retrieved from the server.
	 */
	protected static ArrayList<Report> reports = new ArrayList<>();
	/**
	 * Date used for generating reports.
	 */
	protected static LocalDate reportDate;
	/**
	 * List of delivery branches retrieved from the server.
	 */
	protected static ArrayList<DeliveryBranch> branches = new ArrayList<>();
	/**
	 * List of quarters for a given year.
	 */
	protected static ArrayList<String> quarters = new ArrayList<>();

	/**
	 * Private constructor to ensure only one instance of the class exists.
	 */
	private MonthlyReportController() {
	}

	/**
	 * Retrieves the single instance of the class, creating a new one if it doesn't
	 * exist yet.
	 * 
	 * @return The single instance of the class.
	 */
	public static MonthlyReportController getInstance() {
		if (instance == null)
			instance = new MonthlyReportController();
		return instance;
	}

	/**
	 * Fetches the reports for the specified branch from the server.
	 * 
	 * @param branch The name of the branch to fetch reports for.
	 */
	public void getReports(String branch) {
		reports.clear();
		scc.SendMessageToServer(Message.MessageType.BRANCHMANAGER_GET_REPORTS, branch);
	}

	/**
	 * Fetches the list of delivery branches from the server.
	 */
	public void getBranches() {
		branches.clear();
		scc.SendMessageToServer(Message.MessageType.CEO_GET_BRANCHES, null);
	}

	/**
	 * Retrieves a list of months for the specified year from the reports.
	 * 
	 * @param yearString The year to filter the reports by.
	 * @return A list of months in which reports are available for the given year.
	 */
	public ArrayList<String> getMonths(String yearString) {
		Integer year = Integer.valueOf(yearString);
		System.out.println("reports in getMonths: " + reports);
		ArrayList<String> months = new ArrayList<>();
		Integer month;
		if (reports != null) {
			for (Report r : reports) {
				if (r.getDate_For_Report().toLocalDate().getYear() == year) {
					month = r.getDate_For_Report().toLocalDate().getMonthValue();
					if (!months.contains(month.toString())) {
						months.add(month.toString());
					}
				}
			}
		}
		return months;
	}

	/**
	 * Retrieves a list of years for which reports are available.
	 * 
	 * @return A list of years for which reports are available.
	 */
	public ArrayList<String> getYears() {
		ArrayList<String> years = new ArrayList<>();
		Integer year;
		if (reports != null) {
			for (Report r : reports) {
				year = r.getDate_For_Report().toLocalDate().getYear();
				if (!years.contains(year.toString())) {
					years.add(year.toString());
				}
			}
		}
		return years;
	}

	/**
	 * Produces an income report by summing the revenues of all restaurants for the
	 * specified month and year.
	 * 
	 * @return A map where the key is the restaurant name and the value is the total
	 *         revenue for that restaurant.
	 */
	public Map<String, Double> produceIncomeReport() {
		Map<String, Double> restaurantRevenue = new HashMap<>();
		Double revenue = 0.0;
		LocalDate tmpDate;
		/**
		 * Iterate through all reports
		 */
		for (Report r : reports) {
			/**
			 * Search for matching dates of reports
			 */
			tmpDate = r.getDate_For_Report().toLocalDate();
			if (tmpDate.getMonthValue() == reportDate.getMonthValue() && tmpDate.getYear() == reportDate.getYear()) {
				/**
				 * Identify unique Restaurant names by adding them to a hashmap
				 */
				if (!restaurantRevenue.containsKey(r.getRestaurant_Name())) {
					restaurantRevenue.put(r.getRestaurant_Name(), r.getRestaurant_Revenue());
				}

				/**Summing up all of a restaurant's revenue by iterating over all incomes and
				 * summing them in the map entry.
				 */
				else {
					revenue = restaurantRevenue.get(r.getRestaurant_Name());
					revenue += r.getRestaurant_Revenue();
					restaurantRevenue.replace(r.getRestaurant_Name(), revenue);
				}
			}

		}
		System.out.println(restaurantRevenue);
		return restaurantRevenue;
	}

	/**
	 * Produces an order report by summing the quantities of different items ordered
	 * at each restaurant for the specified month and year.
	 * 
	 * @return A map where the key is the restaurant name and the value is a Report
	 *         object containing the total quantities of each type of item ordered.
	 */
	public Map<String, Report> produceOrderReport() {
		Map<String, Report> restaurantOrder = new HashMap<>();
		Report restaurantReport;
		String restaurantName;
		LocalDate tmpDate;
		// Iterate through all reports
		for (Report r : reports) {
			// Search for matching dates of reports
			tmpDate = r.getDate_For_Report().toLocalDate();
			if (tmpDate.getMonthValue() == reportDate.getMonthValue() && tmpDate.getYear() == reportDate.getYear()) {
				// Identify unique Restaurant names using a Hash Map
				if (!restaurantOrder.containsKey(r.getRestaurant_Name()))
					restaurantOrder.put(r.getRestaurant_Name(),
							new Report(r.getNum_Of_Salads(), r.getNum_Of_Appetizers(), r.getNum_Of_Main(),
									r.getNum_Of_Desserts(), r.getNum_Of_Drinks()));
				// Sum up each restaurant's amount of salads, appetizer, mains, desserts and
				// drinks according to each daily report
				else {
					restaurantName = r.getRestaurant_Name();
					restaurantReport = restaurantOrder.get(restaurantName);
					restaurantReport.addNum_Of_Salads(r.getNum_Of_Salads());
					restaurantReport.addNum_Of_Appetizers(r.getNum_Of_Appetizers());
					restaurantReport.addNum_Of_Main(r.getNum_Of_Main());
					restaurantReport.addNum_Of_Desserts(r.getNum_Of_Desserts());
					restaurantReport.addNum_Of_Drinks(r.getNum_Of_Drinks());
				}
			}
		}
		return restaurantOrder;
	}

	/**
	 * Produces a performance report showing the total number of deliveries, late
	 * deliveries, and on-time deliveries for the specified month and year.
	 * 
	 * @return A map containing the total number of deliveries, late deliveries, and
	 *         on-time deliveries.
	 */
	public Map<String, Integer> producePerformanceReport() {
		int totalDeliveries = 0;
		int lateDeliveries = 0;
		int onTimeDeliveries = 0;
		LocalDate tmpDate;
		// Iterate through all reports
		for (Report r : reports) {
			// Search for matching dates of reports
			tmpDate = r.getDate_For_Report().toLocalDate();
			if (tmpDate.getMonthValue() == reportDate.getMonthValue() && tmpDate.getYear() == reportDate.getYear()) {
				totalDeliveries += r.getTotal_Num_Orders();
				lateDeliveries += r.getNum_Order_Late();
			}
		}
		onTimeDeliveries = totalDeliveries - lateDeliveries;
		Map<String, Integer> performanceData = new HashMap<>();
		performanceData.put("Total Deliveries", totalDeliveries);
		performanceData.put("Late Deliveries", lateDeliveries);
		performanceData.put("On Time Deliveries", onTimeDeliveries);
		return performanceData;
	}

	/**
	 * Fetches the reports for the specified branch from the server.
	 * 
	 * @param branch The name of the branch to fetch reports for.
	 */
	public void getReportsBranch(String branch) {
		reports.clear();
		scc.SendMessageToServer(Message.MessageType.CEO_GET_REPORTS, branch);
	}

	/**
	 * Retrieves a list of quarters for the specified year from the reports.
	 * 
	 * @param year The year to filter the reports by.
	 * @return A list of quarters (Q1, Q2, Q3, Q4) for the given year.
	 */
	public ArrayList<String> getQuarters(String year) {
		quarters.clear();
		ArrayList<String> quarters = new ArrayList<>();
		LocalDate reportDate;
		int month;
		for (Report r : reports) {
			reportDate = r.getDate_For_Report().toLocalDate();
			if (reportDate.getYear() == Integer.valueOf(year)) {
				month = reportDate.getMonthValue();
				if (1 <= month && month <= 3 && !quarters.contains("Q1"))
					quarters.add("Q1");
				else if (4 <= month && month <= 6 && !quarters.contains("Q2"))
					quarters.add("Q2");
				else if (7 <= month && month <= 9 && !quarters.contains("Q3"))
					quarters.add("Q3");
				else if (10 <= month && month <= 12 && !quarters.contains("Q4"))
					quarters.add("Q4");
			}
		}
		return quarters;
	}

	/**
	 * Produces a quarterly report containing income and order data for the
	 * specified quarter and year.
	 * 
	 * @param quarter The quarter to generate the report for (e.g., "Q1", "Q2",
	 *                "Q3", "Q4").
	 * @param year    The year to generate the report for.
	 * @return A list containing two maps: one for income data and one for order
	 *         data.
	 */
	public ArrayList<Map<String, Map<LocalDate, Double>>> produceQuarterlyReport(String quarter, String year) {
		ArrayList<Map<String, Map<LocalDate, Double>>> quarterlyData = new ArrayList<>();

		// Initialize data structures for income and orders
		Map<String, Map<LocalDate, Double>> quarterlyIncomeData = new HashMap<>();
		Map<String, Map<LocalDate, Double>> quarterlyOrderData = new HashMap<>(); // Changed to Double for consistency

		// Add the data maps to the list
		quarterlyData.add(quarterlyIncomeData);
		quarterlyData.add(quarterlyOrderData);

		int startMonth, endMonth;

		// Determine start and end months based on the quarter
		switch (quarter) {
		case "Q1":
			startMonth = 1;
			endMonth = 3;
			break;
		case "Q2":
			startMonth = 4;
			endMonth = 6;
			break;
		case "Q3":
			startMonth = 7;
			endMonth = 9;
			break;
		case "Q4":
			startMonth = 10;
			endMonth = 12;
			break;
		default:
			throw new IllegalArgumentException("Invalid quarter: " + quarter);
		}

		// Iterate through all reports
		for (Report r : reports) {
			LocalDate reportDate = r.getDate_For_Report().toLocalDate();
			if (reportDate.getYear() == Integer.parseInt(year) && reportDate.getMonthValue() >= startMonth
					&& reportDate.getMonthValue() <= endMonth) {

				String restaurantName = r.getRestaurant_Name();
				LocalDate reportDay = LocalDate.of(reportDate.getYear(), reportDate.getMonth(),
						reportDate.getDayOfMonth());

				// Initialize data structures if necessary
				quarterlyIncomeData.putIfAbsent(restaurantName, new HashMap<>());
				quarterlyOrderData.putIfAbsent(restaurantName, new HashMap<>());

				Map<LocalDate, Double> dailyIncome = quarterlyIncomeData.get(restaurantName);
				Map<LocalDate, Double> dailyOrders = quarterlyOrderData.get(restaurantName); // Consistent type

				// Update income data
				dailyIncome.put(reportDay, dailyIncome.getOrDefault(reportDay, 0.0) + r.getRestaurant_Revenue());

				// Update orders data (use Double to match the type of the map)
				dailyOrders.put(reportDay, dailyOrders.getOrDefault(reportDay, 0.0) + r.getTotal_Num_Orders());
			}
		}

		return quarterlyData;
	}
}
