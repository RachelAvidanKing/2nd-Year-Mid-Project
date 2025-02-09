package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * class for connecting between the server and the DB
 * this controller controls actions related to the supplier*/
public class SupplierController {
	/**variable for the connection to the DB*/
	protected Connection conn;
	/**variable to represent a successful/failed connection*/
	public int successFlag;

	public SupplierController(Connection con) {
		conn=con;
	}

	public Connection getConnection() {
		return conn;
	}


	/**
	 * Retrieves orders associated with a restaurant's branch based on the user's name.
	 * <p>
	 * This method first determines the associated branch of the restaurant for the given user's first and last names.
	 * It then retrieves all orders from that branch that are either approved or sent, and adds the details of these orders to a list.
	 * </p>
	 *
	 * @param firstname The first name of the user. Used to identify the associated branch.
	 * @param lastname The last name of the user. Used to identify the associated branch.
	 * @return An `ArrayList` of `String` objects, where each string represents an order's details in a formatted way.
	 *         The format is: Order_number/Order_TS/Order_Status/Receiver_Name/Receiver_LastName/Phone_num/Delivery_Type.
	 */
	public ArrayList<String> importBranchOrders(String firstname, String lastname) {
		PreparedStatement pstmt, pstmt2;
		ResultSet rs = null;
		String rest = "";
		ArrayList<String> branchorders = new ArrayList<>();
		try {
			/** query to get restaurant's Associated Branch*/
			String query2 = "select AssociatedBranch from users where First_Name=? and Last_Name=?;";
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, firstname);
			pstmt.setString(2, lastname);
			rs = pstmt.executeQuery(); // get the restaurant's Associated Branch
			while (rs.next()) {
				rest = rs.getString(1);
			}

			
			/** query to get orders according to associated branch*/
			String query1 = "select Order_number, Order_TS, Order_Status, Receiver_Name, Receiver_LastName, Phone_num, Delivery_Type from orders where Restaurant_ID=? and (Order_Status='Approved' or Order_Status='Sent');";
			pstmt2 = conn.prepareStatement(query1);
			pstmt2.setString(1, rest);
			rs = pstmt2.executeQuery();
			while (rs.next()) {
				
				branchorders.add(rs.getString(1)+"/"+rs.getString(2)+"/"+rs.getString(3)+"/"+rs.getString(4)+"/"+rs.getString(5)+"/"+rs.getString(6)+"/"+rs.getString(7));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return branchorders;
	}
	
	
	/**
	 * Updates the status of an order in the database.
	 * <p>
	 * This method updates various fields in the `orders` table based on the provided order number. If the order status is updated to "Sent",
	 * it calculates the difference between the current time and the expected delivery time to determine if the order is "early" or "regular".
	 * Additionally, it updates the approval timestamp if provided.
	 * </p>
	 *
	 * @param orderNum The unique identifier of the order to be updated.
	 * @param status The new status of the order (e.g., "Sent", "Delivered", etc.).
	 * @param date The date when the status is updated.
	 * @param time The time when the status is updated.
	 */
	public void UpdateStatusOfOrder (String orderNum, String status, String date, String time) {
		PreparedStatement pstmt;
		ResultSet rs=null;
		String str=date+" "+time;
		String expectedTime="",type="regular";
		try
		{
			/**get the order's expected time*/
			if(status.equals("Sent"))
			{
				pstmt =conn.prepareStatement("select Order_Expected_TS from orders where Order_number=?;");
				pstmt.setString(1,orderNum);
				rs=pstmt.executeQuery();
				while(rs.next())
				{
					expectedTime=rs.getString(1);
				}
				 // Define the date format
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            
	            // Parse the strings into Date objects
	            Date date1 = format.parse(str);
	            Date date2 = format.parse(expectedTime);
	            
	            // Calculate the difference in milliseconds 
	            long differenceInMillis = Math.abs(date2.getTime() - date1.getTime());
	            
	            // Convert milliseconds to minutes (or other units as needed)
	            long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis);
	            if(differenceInMinutes>=120)
	            {
	            	type="early";
	            }
	            pstmt =conn.prepareStatement("update orders set OrderType=? WHERE Order_number=?;");
				pstmt.setString(1,type);
				pstmt.setString(2,orderNum);
				pstmt.executeUpdate();
			}
			
			pstmt =conn.prepareStatement("update orders set Order_Status=? WHERE Order_number=?;");
			pstmt.setString(1,status);
			pstmt.setString(2,orderNum);
			pstmt.executeUpdate();
			
			if (!str.equals("nothing nothing")) {
				pstmt =conn.prepareStatement("update orders set Order_Approved_By_Supplier=? WHERE Order_number=?;");
				pstmt.setString(1,str);
				pstmt.setString(2,orderNum);
				pstmt.executeUpdate();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
