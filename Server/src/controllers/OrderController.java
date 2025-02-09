package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Client;
import entities.Meal;
import entities.Order;
import entities.Report;

/**
 * class for connecting between the server and the DB
 * this controller controls actions related to the ordering proccess
 * */
public class OrderController {
	/**variable for the connection to the DB*/
	protected Connection conn;
	/**variable to represent a successful/failed connection*/
	public int successFlag;

	/**
	 * constructor for the controller
	 * @param dbname the name of the DB
	 * @param pass the password for the DB
	 */
	public OrderController(String dbname, String pass) {
		String connectPath = "jdbc:mysql://localhost/" + dbname + "?serverTimezone=IST";
		connectToDB(connectPath, pass);
	}

	public Connection getConnection()
	{
		return conn;
	}
	
	/**
	 * a method to create a connection to the DB
	 * @param path the connection path
	 * @param pass the password for the DB
	 */
	public void connectToDB(String path, String pass) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection(path, "root", pass);
			System.out.println("SQL connection succeed");
			successFlag = 1;
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			successFlag = 2;
		}
	}
	
	/**
	 * Checks the login credentials for a user.
	 * This method verifies the login credentials by querying the database to check if the user exists,
	 * if their account is approved, and if they are already logged in. It also updates the login status
	 * of the user if their credentials are valid and they are not currently logged in.
	 *
	 * @param name      The first name of the user.
	 * @param lastName  The last name of the user.
	 * @param password  The password of the user.
	 * @return A string indicating the login status:
	 *         <ul>
	 *           <li>"None" - if no matching user is found.</li>
	 *           <li>"NotApproved" - if the user is not approved.</li>
	 *           <li>"AlreadyLoggedIn" - if the user is already logged in.</li>
	 *           <li>"fail" - if there was an exception.</li>
	 *           <li>Concatenated string with CustomerType, AssociatedBranch, and Credit if login is successful.</li>
	 *         </ul>
	 *        
	 * @throws SQLException if there is an error executing the SQL query.
	 */

	public String checkLogin(String name, String lastName, String password) {
		String str = "None";
		String qry = "SELECT CustomerType, AssociatedBranch, Approved, IsLoggedIn, Credit FROM users WHERE First_Name = ? AND Last_Name = ? AND Pswd = ?;";
		int checkIfApproved = -1;
		/**get the user's relevant details*/
		try (PreparedStatement stmt = conn.prepareStatement(qry)) {
			stmt.setString(1, name);
			stmt.setString(2, lastName);
			stmt.setString(3, password);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					if (rs.getString("Approved").equals("0")) {
						str = "NotApproved";
						checkIfApproved = 0;
					} else if (rs.getString("IsLoggedIn").equals("1")) {
						str = "AlreadyLoggedIn";
					} else {
						str = rs.getString("CustomerType") + " " + rs.getString("AssociatedBranch")+" "+rs.getString("Credit");
					}
				}
			}
		} catch (SQLException e) {
			// Handle exception (optional: log the error)
			System.out.println("error! exception");
			str = "fail";
		}
		/**if the user exists and approved, change the login status*/
		if (!str.equals("") && !(checkIfApproved == 0)) {
			changeLoginStatus(name, lastName, password, "1");
		}
		return str;
	}

	/**
	 * Updates the login status of a user in the database.
	 * This method sets the `IsLoggedIn` status of a user to the specified value based on their first name,
	 * last name, and password. This is typically used to mark a user as logged in or out.
	 *
	 * @param name      The first name of the user whose login status is being updated.
	 * @param lastName  The last name of the user whose login status is being updated.
	 * @param password  The password of the user to ensure the correct record is updated.
	 * @param status    The new login status to be set. Typically "1" for logged in and "0" for logged out.
	 * @throws SQLException if there is an error executing the SQL update query.
	 */
	public void changeLoginStatus(String name, String lastName, String password, String status) {
		String qry = "update users set IsLoggedIn=? WHERE First_Name = ? AND Last_Name = ? AND Pswd = ?";

		try (PreparedStatement stmt = conn.prepareStatement(qry)) {
			stmt.setString(1, status);
			stmt.setString(2, name);
			stmt.setString(3, lastName);
			stmt.setString(4, password);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieves a list of restaurant brands from the database, excluding the brand "Wheels".
	 * This method queries the `brands` table to select all unique brand names except for "Wheels",
	 * and returns them as an `ArrayList` of strings.
	 *
	 * @return An `ArrayList` of `String` containing the names of restaurants. Each entry is a brand name
	 *         from the `brands` table except "Wheels". If an error occurs during the database query, 
	 *         the returned list may be empty.
	 * @throws SQLException if there is an error executing the SQL query.
	 */

	public ArrayList<String> importRestaurants() {
		Statement stmt;
		ResultSet rs = null;
		ArrayList<String> restaurants = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Brand FROM brands where Brand!='Wheels';");
			while (rs.next())
				restaurants.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return restaurants;
	}
	
	/**
	 * Retrieves a list of monthly reports for a specific delivery branch from the database.
	 * This method performs two queries to gather detailed report information and associated brand names
	 * for a given branch. It first retrieves all reports associated with the specified branch number, 
	 * and then updates each report with the corresponding restaurant brand name.
	 *
	 * @param branchNumber The identifier of the delivery branch for which reports are to be retrieved.
	 * @return An `ArrayList` of `Report` objects containing the monthly reports for the specified branch.
	 *         Each `Report` object is populated with data from the `reports` table and the corresponding 
	 *         restaurant brand name. If an error occurs during the database queries, the list may be incomplete.
	 * @throws SQLException if there is an error executing the SQL queries or processing the result sets.
	 */

	public ArrayList<Report> importMonthlyReports(String branchNumber) {
		PreparedStatement pstmt;
		ResultSet rs;
		ArrayList<Report> reports = new ArrayList<>();
		try {
			String query = "SELECT rep.* " + "FROM reports rep "
					+ "INNER JOIN restaurants rest ON rep.Restaurant_ID = rest.Restaurant_ID "
					+ "WHERE rest.DeliveryBranch_ID = (SELECT DeliveryBranch_ID " 
													+ "FROM restaurants r "
													+ "WHERE r.Restaurant_ID = ?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, branchNumber);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reports.add(new Report(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
						rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10)));
			}
			String query2 = "SELECT br.Brand " + "FROM reports rep "
					+ "JOIN restaurants rest ON rep.Restaurant_ID = rest.Restaurant_ID "
					+ "JOIN brands br ON rest.Brand_ID = br.Brand_ID "
					+ "WHERE rest.DeliveryBranch_ID = (SELECT DeliveryBranch_ID " 
													+ "FROM restaurants r "
													+ "WHERE r.Restaurant_ID = ?)";
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, branchNumber);
			rs = pstmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				reports.get(cnt++).setRestaurant_Name(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(reports);
		return reports;
	}
	
	/**
	 * Retrieves a list of delivery methods from the database.
	 * This method executes a SQL query to fetch all delivery methods from the `deliverymethods` table.
	 * The retrieved methods are stored in an `ArrayList` and returned to the caller.
	 * 
	 * @return An `ArrayList` of `String` values representing the delivery methods retrieved from the database.
	 *         If there is an error executing the SQL query, the list may be incomplete or empty.
	 * @throws SQLException if there is an error executing the SQL query or processing the result set.
	 */
	
	public ArrayList<String> importmethods() {
		Statement stmt;
		ResultSet rs = null;
		ArrayList<String> methods = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT method FROM deliverymethods;");
			while (rs.next())
				methods.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return methods;
	}
	
	
	
	/**
	 * Retrieves a list of unique cities from the `address` table in the database.
	 * This method executes a SQL query to fetch distinct city names from the `address` table.
	 * The retrieved cities are stored in an `ArrayList` and returned to the caller.
	 *
	 * @return An `ArrayList` of `String` values representing unique city names retrieved from the database.
	 *         If there is an error executing the SQL query, the list may be incomplete or empty.
	 * @throws SQLException if there is an error executing the SQL query or processing the result set.
	 */
	public ArrayList<String> importCities()
	{
		PreparedStatement pstmt;
		ResultSet rs;
		ArrayList<String> cities = new ArrayList<>();
		try
		{
			pstmt = conn.prepareStatement("select distinct cities from address;");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				cities.add(rs.getString(1));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return cities;	
	}
	
	/**
	 * Retrieves a list of streets from the `address` table in the database for a given city.
	 * 
	 * This method executes a SQL query to fetch street names associated with a specified city from the `address` table.
	 * The retrieved streets are stored in an `ArrayList` and returned to the caller.
	 * 
	 *
	 * @param city The name of the city for which streets are to be retrieved.
	 *             This parameter is used to filter the results from the database.
	 * @return An `ArrayList` of `String` values representing the streets associated with the specified city.
	 *         If there is an error executing the SQL query, the list may be incomplete or empty.
	 * @throws SQLException if there is an error executing the SQL query or processing the result set.
	 */
	public ArrayList<String> importStreets(String city)
	{
		PreparedStatement pstmt;
		ResultSet rs;
		ArrayList<String> streets = new ArrayList<>();
		try
		{
			pstmt = conn.prepareStatement("select streets from address where cities=?;");
			pstmt.setString(1, city);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				streets.add(rs.getString(1));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return streets;
	}
	
	/**
	 * Retrieves a list of open orders for a client from the `orders` table in the database.
	 * 
	 * This method executes a SQL query to fetch details of orders that are either "Ready" or "Approved" and are not of the type "Self Pickup".
	 * The orders are filtered based on the client's name, last name, and password. The details of each order are formatted as a single string and added to the list.
	 *
	 * @param name The first name of the client whose orders are to be retrieved.
	 * @param lastName The last name of the client whose orders are to be retrieved.
	 * @param password The password of the client, used to authenticate the order request.
	 * @return An `ArrayList` of `String` values, each representing an order. Each order is formatted as "Order_number/Order_Status/Order_TS/Order_Approved_By_Supplier/Order_Expected_TS/OrderType/Total_price".
	 *         If there is an error executing the SQL query, the list may be incomplete or empty.
	 * @throws SQLException if there is an error executing the SQL query or processing the result set.
	 */
	public ArrayList<String> importClientsOpenOrders(String name,String lastName,String password)
	{
		PreparedStatement pstmt;
		ResultSet rs;
		ArrayList<String> orders = new ArrayList<>();
		try
		{
			pstmt = conn.prepareStatement("select Order_number,Order_Status,Order_TS,Order_Approved_By_Supplier,Order_Expected_TS,OrderType,Total_price from orders where Receiver_Name=? and Receiver_LastName=? and Receiver_Password=? and (Order_Status='Ready' or Order_Status='Approved') and Delivery_Type!='Self Pickup';");
			pstmt.setString(1, name);
			pstmt.setString(2, lastName);
			pstmt.setString(3, password);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				/**add the retreived details to the array, split them with / */
				orders.add(rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getString(3)+"/"+rs.getString(4)+"/"+rs.getString(5)+"/"+rs.getString(6)+"/"+rs.getString(7));
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	/**
	 * Updates the status and delivery timestamp of an order in the `orders` table.
	 * 
	 * This method executes two SQL queries:
	 * <ol>
	 *     <li>Updates the `Order_Status` of the order identified by `orderNumber`.</li>
	 *     <li>Updates the `Order_Delivered_TS` of the same order with a timestamp constructed from the provided date and time.</li>
	 * </ol>
	 *
	 * @param orderNumber The unique identifier of the order to be updated.
	 * @param status The new status to set for the order. It should be a valid status according to the application's business rules.
	 * @param date The date of delivery, formatted as a string (e.g., "YYYY-MM-DD").
	 * @param time The time of delivery, formatted as a string (e.g., "HH:MM:SS").
	 * @throws SQLException if there is an error executing the SQL queries or processing the result set.
	 */
	public void updateStatus(String orderNumber,String status,String date,String time)
	{
		PreparedStatement pstmt;
		String str=date+" "+time;
		try
		{
			pstmt =conn.prepareStatement("update orders set Order_Status=? WHERE Order_number = ?;");
			pstmt.setString(2, orderNumber);
			pstmt.setString(1, status);
			pstmt.executeUpdate();
			pstmt =conn.prepareStatement("update orders set Order_Delivered_TS=? WHERE Order_number = ?;");
			pstmt.setString(2, orderNumber);
			pstmt.setString(1, str);
			pstmt.executeUpdate();
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the credit balance (discount) for a user in the `users` table.
	 *
	 * This method updates the `Credit` field for the user specified by their first name, last name, and password.
	 * 
	 *
	 * @param name The first name of the user whose credit balance is to be updated.
	 * @param lastName The last name of the user whose credit balance is to be updated.
	 * @param password The password of the user whose credit balance is to be updated.
	 * @param discount The new credit balance (discount) to set for the user. This should be a valid string representation of the discount value.
	 * @throws SQLException if there is an error executing the SQL query or processing the result set.
	 */
	public void updateDiscount(String name, String lastName,String password,String discount)
	{
		PreparedStatement pstmt;
		try
		{
			pstmt =conn.prepareStatement("update users set Credit=? WHERE First_Name=? and Last_Name=? and Pswd=? ;");
			pstmt.setString(1,discount);
			pstmt.setString(2,name);
			pstmt.setString(3,lastName);
			pstmt.setString(4,password);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the branch ID of a restaurant located in a specified city.
	 * 
	 * This method first obtains the `Brand_ID` for the given restaurant name, then retrieves the delivery branch 
	 * for the specified city, and finally gets the `Restaurant_ID` that matches both the `Brand_ID` and delivery branch.
	 * 
	 *
	 * @param restaurant The name of the restaurant whose branch ID is to be retrieved.
	 * @param city The city where the restaurant is located.
	 * @return The ID of the restaurant branch if found, otherwise an empty string.
	 */
	public String getRestaurantBranch(String restaurant,String city)
	{
		PreparedStatement pstmt2;
		ResultSet rs = null;
		String rest = "", deliveryBranch="",branch="";
		try {
			/** query to get restaurant's id*/
			pstmt2 = conn.prepareStatement("select Brand_ID from brands where brand=?;");
			pstmt2.setString(1, restaurant);
			rs = pstmt2.executeQuery(); // get the restaurant's id
			while (rs.next()) {
				rest = rs.getString(1);
			}

			/**query to get the delivery branch for the city*/
			pstmt2 = conn.prepareStatement("select branch from address where cities=?;");
			pstmt2.setString(1, city);
			rs = pstmt2.executeQuery(); // get the delivery's id
			while (rs.next()) {
				deliveryBranch = rs.getString(1);
			}

			/**query to get restaurant's branch*/
			pstmt2 = conn.prepareStatement("select Restaurant_ID from Restaurants where Brand_ID=? and DeliveryBranch_ID=?;");
			pstmt2.setString(1,rest);
			pstmt2.setString(2,deliveryBranch);
			rs = pstmt2.executeQuery();
			while (rs.next()) {
				branch = rs.getString(1);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	
	/**
	 * Updates the order information in the database with the details from a given `Order` object and associated `Client`.
	 * 
	 * This method first obtains the list of meals from the provided `Order` object. It then updates the order details 
	 * using the `helpUpdateOrders` method and adds the content of the order using the `helpAddOrderContent` method.
	 * 
	 *
	 * @param c The `Client` object associated with the order. This client’s information to be used for further processing.
	 * @param o The `Order` object containing the details of the order to be updated, including the list of meals and restaurant information.
	 */
	public void updateOrders(Client c,Order o)
	{
		//for easier use
		ArrayList<Meal> mealsInOrder=o.getDishList(); 
		String orderNum=helpUpdateOrders(c,o);
		helpAddOrderContent(mealsInOrder,orderNum,o.getRestaurant());
	}
	
	
	/**
	 * Updates the orders database with a new order entry and returns the generated order number.
	 * <p>
	 * This method inserts a new order into the `orders` table. It first determines the next order number by 
	 * querying the maximum existing order number and then increments it. It constructs an SQL query to insert 
	 * the order details into the table. The method handles optional fields like workplace and street based on 
	 * the provided `Order` object.
	 * </p>
	 *
	 * @param c The `Client` object placing the order. This includes the username and password of the client.
	 * @param o The `Order` object containing details of the order to be placed, such as delivery method, address, 
	 *          and other order-related information.
	 * @return A `String` representing the newly generated order number.
	 */
	public String helpUpdateOrders(Client c,Order o)
	{
		PreparedStatement pstmt,pstmt2;
		Integer id;
		Integer num=-1;
		ResultSet rs = null;
		String query,orderNum="",values;
		//for easier use
		String[] arr = c.getUsername().split("\\s");
		if(!o.getHomeNum().equals(""))
		{
			num=Integer.parseInt(o.getHomeNum());
		}
		
		try {
			/**query to get the highest order id*/
	        pstmt=conn.prepareStatement("select max(Order_number) from orders;");
	        rs = pstmt.executeQuery();
	        if (rs.next()) 
	        {
	        	orderNum = rs.getString(1);
	        }
	        id=Integer.parseInt(orderNum) +1;
	        orderNum=id.toString();
	        /**query to add row the order in orders table*/
	        query="insert into orders (Order_number,Delivery_Type,city,";
	        /**gets what needs to be added according to the order- address or workplace*/
	        if(o.getWorkplace()!=null)
	        {
	        	query+="workplace";
	        }
	        else
	        {
	        	query+="street";
	        }
	        query+=",address_num, nickname,Receiver_Name,Receiver_LastName,Receiver_Password,Phone_num,Total_price,Order_Status,Restaurant_ID,Order_TS,Order_Expected_TS)";
	        values=" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	        pstmt2=conn.prepareStatement(query+values);
	        pstmt2.setString(1,orderNum);
	        pstmt2.setString(2,o.getDeliveryMethod());
	        pstmt2.setString(3,o.getCity());
	        if(o.getWorkplace()!=null)
	        {
	        	pstmt2.setString(4,o.getWorkplace());
	        }
	        else
	        {
	        	pstmt2.setString(4,o.getStreet());
	        }
	        
	        pstmt2.setInt(5,num);
	        pstmt2.setString(6,o.getNickname());
	        pstmt2.setString(7,arr[0]);
	        pstmt2.setString(8,arr[1]);
	        pstmt2.setString(9,c.getPassword());
	        pstmt2.setString(10,o.getPhoneNum());
	        pstmt2.setString(11,o.getFinalPrice().toString());
	        pstmt2.setString(12,"Sent");
	        pstmt2.setString(13,o.getBranch());
	        pstmt2.setString(14,o.getDate()+" "+o.getStartTime());
	        pstmt2.setString(15,o.getDate()+" "+o.getWantedTime());
	        
	        pstmt2.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return orderNum;
	}
	
	
	/**
	 * Inserts the contents of an order into the `order_contents` table in the database.
	 * <p>
	 * This method iterates through a list of meals associated with an order and adds each meal to the order_contents table.
	 * It first retrieves the restaurant's ID based on the provided restaurant name, then retrieves each meal's ID from 
	 * the menu based on its name and the restaurant's ID. Finally, it inserts each meal's details into the order_contents table.
	 * </p>
	 *
	 * @param meals A list of `Meal` objects representing the items in the order. Each meal includes details like the dish name.
	 * @param orderNum The order number for which the contents are being added.
	 * @param restaurant The name of the restaurant where the order is placed. Used to fetch the restaurant's ID.
	 */
	public void helpAddOrderContent(ArrayList<Meal> meals, String orderNum,String restaurant)
	{
		PreparedStatement pstmt2;
		Integer orderList=1;
		ResultSet rs = null;
		String rest="",dishName,mealId="";
		
		try
		{
			/** query to get restaurant's id*/
			pstmt2 = conn.prepareStatement("select Brand_ID from brands where brand=?;");
			pstmt2.setString(1, restaurant);
			rs = pstmt2.executeQuery(); // get the restaurant's id
			while (rs.next()) {
				rest = rs.getString(1);
			}
			/**go over the array*/
			for(int i=0;i<meals.size();i++)
			{
				dishName=meals.get(i).getName(); //for easier use
				pstmt2 = conn.prepareStatement("select Meal_ID from updatedmenus where Meal_Name=? and BrandID=?;"); //return dish id
				pstmt2.setString(1,dishName);
				pstmt2.setString(2,rest);
				rs = pstmt2.executeQuery();
				while (rs.next()) {
					mealId = rs.getString(1);
				}
				
				
				pstmt2=conn.prepareStatement("insert into order_contents(Order_Num,Order_List,Dish_ID) values(?,?,?);");
				pstmt2.setString(1,orderNum);
				pstmt2.setString(2,orderList.toString());
				pstmt2.setString(3,mealId);
				pstmt2.executeUpdate();
				orderList++;
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		

		

	}
}
