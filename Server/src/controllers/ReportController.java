package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.DeliveryBranch;
import entities.Report;
import entities.User;
/**
 * The ReportController class handles the interaction with the database to
 * generate and retrieve reports related to restaurants and delivery branches.
 * This class includes methods for importing reports, approving users, and 
 * generating monthly report tuples.
 *
 * @author Daniel Feldman
 * @version August 2024
 */
public class ReportController {
	/**
     * The database connection object.
     */
	protected Connection conn;
	/**
     * A flag to indicate the success of an operation.
     */
	public int successFlag;
	/**
     * Constructs a ReportController with the given database connection.
     *
     * @param con The Connection object for interacting with the database.
     */
	public ReportController(Connection con) {
		conn = con;
	}
	/**
     * Returns the current database connection.
     *
     * @return The Connection object used by this controller.
     */
	public Connection getConnection() {
		return conn;
	}
	/**
     * Imports monthly reports for a specific branch based on the restaurant ID.
     *
     * @param branchNumber The restaurant ID to fetch reports for.
     * @return An ArrayList of Report objects containing the monthly reports.
     */
	public ArrayList<Report> importMonthlyReports(String branchNumber) { // Fix the statement to work like the second
		// one
		PreparedStatement pstmt;
		String query,name="", deliveryBranch="";
		ResultSet rs;
		ArrayList<String> restaurants=new ArrayList<>();
		ArrayList<Report> reports = new ArrayList<>();
		try {
//			String query = "SELECT DISTINCT rep.*, b.Brand "
//                    + "FROM reports rep "
//                    + "JOIN restaurants res ON rep.Restaurant_ID = res.Restaurant_ID "
//                    + "JOIN brands b ON res.Brand_ID = b.Brand_ID "
//                    + "JOIN delivery_branches db ON res.DeliveryBranch_ID = db.DeliveryBranches_ID "
//                    + "WHERE db.DeliveryBranches_ID = ("
//                    + "    SELECT DeliveryBranch_ID "
//                    + "    FROM restaurants r "
//                    + "    WHERE r.Restaurant_ID = ?"
//                    + ")";
//			pstmt = conn.prepareStatement(query);
//			pstmt.setString(1, branchNumber);
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				Report r=new Report(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
//						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getString(11));
//				reports.add(r);
////				reports.add(new Report(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
////						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getString(11)));
//			}
			query="select DeliveryBranch_ID from restaurants where Restaurant_ID=?;"; //get the related delivery branch
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, branchNumber);
			rs = pstmt.executeQuery();
			if(rs.next())
			{
				 deliveryBranch=rs.getString(1);
			}
			
			query="select Restaurant_ID,Brand_ID from restaurants where DeliveryBranch_ID=?;"; //get all restaurants of this delivery branch
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,deliveryBranch);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				restaurants.add(rs.getString(1)+":"+rs.getString(2));
			}
			for(int i=0;i<restaurants.size();i++) //go over all the restaurants and find their reports
			{
				String[] arr=restaurants.get(i).split(":"); //seperate restaurant's branch number from restaurant id
				query="select Brand from brands where Brand_ID=?;"; //get the restaurant's name
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1,arr[1]);
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					name=rs.getString(1);
				}
				query="select * from reports where Restaurant_ID=?;";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1,arr[0]);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					reports.add(new Report(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), name));
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reports;
	}
	/**
     * Imports monthly reports for a specific delivery branch.
     *
     * @param branch The delivery branch ID to fetch reports for.
     * @return An ArrayList of Report objects containing the monthly reports.
     */
	public ArrayList<Report> importMonthlyReportsBranchID(String branch) {
		PreparedStatement pstmt;
		ResultSet rs;
		ArrayList<Report> reports = new ArrayList<>();
		try {
			String query = "SELECT rep.*, b.Brand " + "FROM reports rep "
					+ "JOIN restaurants res ON rep.Restaurant_ID = res.Restaurant_ID "
					+ "JOIN brands b ON res.Brand_ID = b.Brand_ID "
					+ "JOIN delivery_branches db ON res.DeliveryBranch_ID = db.DeliveryBranches_ID "
					+ "WHERE res.DeliveryBranch_ID = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, branch);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reports.add(new Report(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getString(11)));
			}
			// System.out.println(reports);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reports;
	}
	/**
     * Imports available quarters for a specific branch and year.
     *
     * @param content An array where the first element is the branch ID and 
     *                the second element is the year.
     * @return An ArrayList of Strings representing available quarters.
     */
	public ArrayList<String> importAvailableQuarters(String[] content) {
		String branch = content[0];
		String year = content[1];
		ArrayList<String> quarters = new ArrayList<>();
		// Check q1
		PreparedStatement pstmt;
		ResultSet rs;
		// Check if there are tuples in which the dates fall between the range of Jan to
		// March
		String query = "SELECT * " + "FROM reports " + "WHERE Date_For_Report BETWEEN '?-01-01' AND '?-03-31";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, year);
			pstmt.setString(2, year);
			rs = pstmt.executeQuery();
			if (rs.next())
				quarters.add("Q1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quarters;
	}
	/**
     * Imports a list of unapproved users for a specific branch.
     *
     * @param branch The branch ID to fetch unapproved users for.
     * @return An ArrayList of User objects representing unapproved users.
     */
	public ArrayList<User> importUnApprovedUsers(String branch) {
		ArrayList<User> unapprovedUsers = new ArrayList<>();
		PreparedStatement pstmt;
		ResultSet rs;
		String query = "SELECT u.ID, u.First_Name, u.Last_Name, u.Phone_Num, u.eMail, u.CustomerType, u.CardNum "
				+ "FROM users u " + "JOIN restaurants r ON u.AssociatedBranch = r.Restaurant_ID "
				+ "WHERE u.Approved = '0' AND r.DeliveryBranch_ID = (SELECT Distinct rest.DeliveryBranch_ID "
				+ "FROM restaurants rest " + "WHERE rest.Restaurant_ID = ?)";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, branch);
			rs = pstmt.executeQuery();
			while (rs.next())
				unapprovedUsers.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unapprovedUsers;
	}
	/**
     * Imports a list of all delivery branches.
     *
     * @return An ArrayList of DeliveryBranch objects representing all branches.
     */
	public ArrayList<DeliveryBranch> importBranches() {
		Statement stmt;
		ResultSet rs;
		ArrayList<DeliveryBranch> branches = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM delivery_branches WHERE DeliveryBranches_ID <> 0");
			while (rs.next()) {
				branches.add(new DeliveryBranch(rs.getString(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	/**
     * Approves a user by updating their status in the database.
     *
     * @param user The User object representing the user to be approved.
     * @return true if the user was successfully approved; false otherwise.
     */
	public boolean approveUser(User user) {
		PreparedStatement preparedStatement;
		String update = "UPDATE users "
				+ "SET ID = ?, Phone_Num = ?, eMail = ?, CustomerType = ?, CardNum = ?, Approved = 1 "
				+ "WHERE First_Name = ? AND Last_Name = ?";
		try {
			preparedStatement = conn.prepareStatement(update);
			preparedStatement.setInt(1, user.getID());
			preparedStatement.setString(2, user.getPhoneNum());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getUserType());
			preparedStatement.setString(5, user.getCreditCardNumber());
			preparedStatement.setString(6, user.getFirstName());
			preparedStatement.setString(7, user.getLastName());
			int affected = preparedStatement.executeUpdate();
			if (affected > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Produces and stores monthly report tuples for all restaurants in the database.
	 * This method calculates various metrics for each restaurant, such as the number
	 * of total orders, late orders, and revenue, based on the previous month's data.
	 *
	 * @param month The month for which to generate the report.
	 * @param year  The year for which to generate the report.
	 */
	public void produceMonthlyReportTuples(int month, int year) {
		PreparedStatement preparedStatement;

		// For every unique restaurant id, we'll insert a tuple with todays date
		// LocalDate today = LocalDate.now().withMonth(month).withYear(year);
		// Stub date
		LocalDate today = LocalDate.now().withDayOfMonth(1).withMonth(month).withYear(year);
		Date todaySQL = Date.valueOf(today);
		ResultSet rs;

		String query = "SELECT " + "r.Restaurant_ID, " + "COUNT(o.Order_number) AS TotalOrders, "
				+ "COUNT(CASE WHEN TIMESTAMPDIFF(SECOND, o.Order_Expected_TS, o.Order_Delivered_TS) > 0 THEN 1 END) AS LateOrders, "
				+ "SUM(o.Total_price) AS TotalRevenue, "
				+ "SUM(CASE WHEN c.category = 'Salad' THEN 1 ELSE 0 END) AS SaladOrders, "
				+ "SUM(CASE WHEN c.category = 'Appetizers' THEN 1 ELSE 0 END) AS AppetizerOrders, "
				+ "SUM(CASE WHEN c.category = 'Main' THEN 1 ELSE 0 END) AS MainOrders, "
				+ "SUM(CASE WHEN c.category = 'Desserts' THEN 1 ELSE 0 END) AS DessertOrders, "
				+ "SUM(CASE WHEN c.category = 'Drinks' THEN 1 ELSE 0 END) AS DrinkOrders " + "FROM restaurants r "
				+ "JOIN orders o ON r.Restaurant_ID = o.Restaurant_ID "
				+ "JOIN order_contents oc ON o.Order_number = oc.Order_Num " + "JOIN menus m ON oc.Dish_ID = m.Meal_ID "
				+ "JOIN category c ON m.Category = c.Category_ID "
				+ "WHERE month(STR_TO_DATE(o.Order_TS, '%Y-%m-%d')) = month(?) AND year(STR_TO_DATE(o.Order_TS, '%Y-%m-%d')) = year(?) "
				+ "GROUP BY r.Restaurant_ID;";

		String update = "INSERT INTO reports (Restaurant_ID, Date_For_Report, "
				+ "Total_Num_Orders, Num_Orders_Late, Restaurant_Revenue, "
				+ "Num_Of_Salads, Num_Of_Appetizers, Num_Of_Main, Num_Of_Desserts, Num_Of_Drinks) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? ,?) ";
		// execute query
		try {
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setDate(1, todaySQL);
			preparedStatement.setDate(2, todaySQL);
			rs = preparedStatement.executeQuery();
			// for each tuple in the resultset execute update
			while (rs.next()) {
				preparedStatement = conn.prepareStatement(update);
				// restaurant id
				preparedStatement.setString(1, rs.getString(1));
				// date
				preparedStatement.setDate(2, todaySQL);
				// total orders
				preparedStatement.setInt(3, rs.getInt(2));
				// late orders
				preparedStatement.setInt(4, rs.getInt(3));
				// revenue
				preparedStatement.setDouble(5, rs.getDouble(4));
				// salads
				preparedStatement.setInt(6, rs.getInt(5));
				// appetizers
				preparedStatement.setInt(7, rs.getInt(6));
				// mains
				preparedStatement.setInt(8, rs.getInt(7));
				// desserts
				preparedStatement.setInt(9, rs.getInt(8));
				// drinks
				preparedStatement.setInt(10, rs.getInt(9));
				// execute update
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}