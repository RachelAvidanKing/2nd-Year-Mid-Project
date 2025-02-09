package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * class for connecting between the server and the DB
 * this controller controls actions related to the qualified worker*/
public class QWController {
	/**variable for the connection to the DB*/
	protected Connection conn;
	public QWController(Connection con)
	{
		conn=con;
	}
	
	/**
	 * Retrieves meal data from the database for a specific restaurant and dish type.
	 * <p>
	 * This method queries the database to get the list of meals based on the provided restaurant name and dish type. It returns the meal data
	 * as an ArrayList of strings, where each string contains information about a meal in a specific format.
	 * </p>
	 *
	 * @param type The type of dish (e.g., "Appetizer", "Main Course") for which the meal data is being retrieved.
	 * @param restaurantName The name of the restaurant from which to retrieve the meal data.
	 * @return An ArrayList of strings where each string represents a meal and contains the meal's name, type, price, and options.
	 */
	public ArrayList<String> importMealData(String type, String restaurantName) {
		PreparedStatement pstmt, pstmt2;
		ResultSet rs = null;
		String rest = "", typeNum = "", stringDish = "";
		ArrayList<String> toTry = new ArrayList<>();
		// ArrayList<Meal> data = new ArrayList<>();
		try {
			/** query to get restaurant's id*/
			String query2 = "select Brand_ID from brands where brand=?;";
			pstmt2 = conn.prepareStatement(query2);
			pstmt2.setString(1, restaurantName);
			rs = pstmt2.executeQuery(); // get the restaurant's id
			while (rs.next()) {
				rest = rs.getString(1);
			}

			/** query to get dish type's number*/
			query2 = "select Category_ID from category where Category=?;";
			pstmt2 = conn.prepareStatement(query2);
			pstmt2.setString(1, type);
			rs = pstmt2.executeQuery();
			while (rs.next()) {
				typeNum = rs.getString(1);
			}

			/** query to get the relevant dishes*/
			String query = "SELECT Meal_Name,OptionsChangeMeal,Price FROM updatedmenus WHERE Category = ? and BrandID=?;";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, typeNum);
			pstmt.setString(2, rest);
			rs = pstmt.executeQuery();

			/** enter all dishes into an array, splits them with :*/
			while (rs.next()) {
				stringDish = rs.getString("Meal_Name") + ":" + type + ":" + rs.getString("Price") + ":"
						+ rs.getString("OptionsChangeMeal");
				toTry.add(stringDish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toTry;
	}

	/**
	 * Retrieves the menu for a specific branch of a restaurant.
	 * <p>
	 * This method queries the database to get the list of dishes available in the menu for the given branch of the restaurant. 
	 * It returns the menu data as an ArrayList of strings, where each string contains information about a dish.
	 * </p>
	 *
	 * @param branch The ID of the branch for which to retrieve the menu.
	 * @return An ArrayList of strings where each string represents a dish and contains the dish's name, price, and options.
	 */
	
	public ArrayList<String> importMenu(String branch) {
		PreparedStatement pstmt, pstmt2;
		ResultSet rs = null;
		String rest = "", stringDish = "";
		ArrayList<String> toTry = new ArrayList<>();
		try {
			/** query to get restaurant's id*/
			String query2 = "select Brand_ID from restaurants where Restaurant_ID=?;";
			pstmt2 = conn.prepareStatement(query2);
			pstmt2.setString(1, branch);
			rs = pstmt2.executeQuery(); // get the restaurant's id
			while (rs.next()) {
				rest = rs.getString(1);
			}

			/** query to get the whole menu*/
			String query = "SELECT Meal_Name,OptionsChangeMeal,Price FROM updatedmenus WHERE BrandID=?;";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, rest);
			rs = pstmt.executeQuery();

			/** enter all dishes into an array,split the details of each dish with :*/
			while (rs.next()) {
				stringDish = rs.getString("Meal_Name") + ":" + rs.getString("Price") + ":"
						+ rs.getString("OptionsChangeMeal");
				toTry.add(stringDish);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return toTry;

	}
	
	
	/**
	 * Updates the details of a meal in the menu for a specific branch of a restaurant.
	 * <p>
	 * This method updates the meal name, options, and price for a given meal in both the `Menus` and `updatedmenus` tables
	 * for the specified branch of the restaurant. It first retrieves the restaurant's ID using the branch ID, then performs 
	 * the update operations.
	 * </p>
	 *
	 * @param oldMealName The current name of the meal to be updated.
	 * @param newMealName The new name of the meal.
	 * @param newOptions The new options for the meal.
	 * @param newPrice The new price for the meal.
	 * @param branch The ID of the branch where the meal is located.
	 */
	public void UpdateMenu(String oldMealName, String newMealName, String newOptions, String newPrice, String branch) {
	    PreparedStatement pstmt, pstmt2;
	    ResultSet rs = null;
	    String rest = "";

	    try {
	        // query to get restaurant's id
	        String query2 = "SELECT Brand_ID FROM restaurants WHERE Restaurant_ID=?;";
	        pstmt2 = conn.prepareStatement(query2);
	        pstmt2.setString(1, branch);
	        rs = pstmt2.executeQuery(); // get the restaurant's id
	        if (rs.next()) {
	            rest = rs.getString(1);
	        }
	        rs.close();
	        pstmt2.close();

	        // query to update the fields
	        String updateQuery = "UPDATE Menus SET Meal_Name=?, OptionsChangeMeal=?, Price=? WHERE BrandID=? AND Meal_Name=?";
	        pstmt = conn.prepareStatement(updateQuery);
	        pstmt.setString(1, newMealName);
	        pstmt.setString(2, newOptions);
	        pstmt.setString(3, newPrice);
	        pstmt.setString(4, rest);
	        pstmt.setString(5, oldMealName);
	        
	        updateQuery = "UPDATE updatedmenus SET Meal_Name=?, OptionsChangeMeal=?, Price=? WHERE BrandID=? AND Meal_Name=?";
	        pstmt = conn.prepareStatement(updateQuery);
	        pstmt.setString(1, newMealName);
	        pstmt.setString(2, newOptions);
	        pstmt.setString(3, newPrice);
	        pstmt.setString(4, rest);
	        pstmt.setString(5, oldMealName);
	        pstmt.executeUpdate(); // execute update
	        pstmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Deletes a specific dish from the menu of a restaurant branch.
	 * <p>
	 * This method removes a dish from the `updatedmenus` table based on the dish name and the restaurant branch ID. 
	 * It first retrieves the restaurant's ID using the branch ID, and then performs the deletion of the dish.
	 * </p>
	 *
	 * @param dish The name of the dish to be deleted from the menu.
	 * @param branch The ID of the branch where the dish is located.
	 */
	public void deleteDishFromMenu(String dish,String branch)
	{
		PreparedStatement pstmt, pstmt2;
	    ResultSet rs = null;
	    String rest="";
	    try {
	        // query to get restaurant's id
	        String query2 = "SELECT Brand_ID FROM restaurants WHERE Restaurant_ID=?;";
	        pstmt2 = conn.prepareStatement(query2);
	        pstmt2.setString(1, branch);
	        rs = pstmt2.executeQuery(); // get the restaurant's id
	        if (rs.next()) 
	        {
	            rest = rs.getString(1);
	        }
	        String updateQuery = "Delete from updatedmenus WHERE BrandID=? and Meal_Name=?;";
	        pstmt = conn.prepareStatement(updateQuery);
	        pstmt.setString(1, rest);
	        pstmt.setString(2, dish);
	        pstmt.executeUpdate();
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace();
	    }    
	}
	
	
	
	/**
	 * Adds a new dish to the menu of a specified restaurant branch.
	 * <p>
	 * This method performs the following operations:
	 * <ul>
	 *     <li>Retrieves the `Brand_ID` for the given branch.</li>
	 *     <li>Determines the next available `Meal_ID` for the new dish.</li>
	 *     <li>Retrieves the `Category_ID` for the specified category.</li>
	 *     <li>Checks if a dish with the same name already exists in the menu for the restaurant.</li>
	 *     <li>Adds the new dish to both the `menus` and `updatedmenus` tables if it does not already exist.</li>
	 * </ul>
	 * </p>
	 *
	 * @param category The category of the dish (e.g., appetizer, main course).
	 * @param name The name of the dish to be added.
	 * @param options The options available for the dish (e.g., customization options).
	 * @param price The price of the dish.
	 * @param branch The ID of the branch where the dish will be added.
	 * @return Returns 1 if the dish is successfully added; returns 0 if the dish already exists.
	 */
	public int addToMenu(String category,String name,String options,String price,String branch)
	{
		PreparedStatement pstmt, pstmt2,pstmt3, pstmt4;
	    ResultSet rs = null;
	    String rest="",mealId="",catNum="";
	    int check=0;
	    Integer id;
	    try {
	        // query to get restaurant's id
	        String query2 = "SELECT Brand_ID FROM restaurants WHERE Restaurant_ID=?;";
	        pstmt2 = conn.prepareStatement(query2);
	        pstmt2.setString(1, branch);
	        rs = pstmt2.executeQuery(); // get the restaurant's id
	        if (rs.next()) 
	        {
	            rest = rs.getString(1);
	        }
	        pstmt2.close();
	         //query to get the highest meal id
	        query2="select max(Meal_ID) from menus;";
	        pstmt3=conn.prepareStatement(query2);
	        rs = pstmt3.executeQuery();
	        if (rs.next()) 
	        {
	            mealId = rs.getString(1);
	        }
	        id=Integer.parseInt(mealId) +1;
	        mealId=id.toString();

	        //query to get the category's id
	        query2="select Category_ID from category where Category=?;";
	        pstmt4=conn.prepareStatement(query2);
	        pstmt4.setString(1, category);
	        rs = pstmt4.executeQuery();

	        if (rs.next()) 
	        {
	        	catNum = rs.getString(1);
	        }
	        
	        //query to check if dish already exists
	        query2="select * from updatedmenus where BrandID=? and Meal_Name=?;";
	        pstmt3=conn.prepareStatement(query2);
	        pstmt3.setString(1,rest );
	        pstmt3.setString(2,name );
	        rs = pstmt3.executeQuery();
	        if (rs.next()) 
	        {
	        	return check;
	        }
	        
	        //query to add the new dish to the menu
	        query2="insert into menus(Meal_ID,BrandID,Category,Meal_Name,OptionsChangeMeal,Price) values(?,?,?,?,?,?);";
	        pstmt = conn.prepareStatement(query2);
	        pstmt.setString(1, mealId);
	        pstmt.setString(2, rest);
	        pstmt.setString(3, catNum);
	        pstmt.setString(4, name);
	        pstmt.setString(5, options);
	        pstmt.setString(6, price);
	        pstmt.executeUpdate();  
	        
	        query2="insert into updatedmenus(Meal_ID,BrandID,Category,Meal_Name,OptionsChangeMeal,Price) values(?,?,?,?,?,?);";
	        pstmt = conn.prepareStatement(query2);
	        pstmt.setString(1, mealId);
	        pstmt.setString(2, rest);
	        pstmt.setString(3, catNum);
	        pstmt.setString(4, name);
	        pstmt.setString(5, options);
	        pstmt.setString(6, price);
	        pstmt.executeUpdate(); 
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace();
	    }  
	    return 1;
	}
	
	
	/**
	 * Retrieves a list of all category names from the database.
	 * <p>
	 * This method queries the `category` table to retrieve the names of all categories and 
	 * stores them in an ArrayList.
	 * </p>
	 *
	 * @return An ArrayList of Strings where each String represents a category name.
	 */
	public ArrayList<String> importCategories()
	{
		PreparedStatement pstmt;
		ResultSet rs = null;
		ArrayList<String> categories = new ArrayList<>();
		try {
			// query to get the categories' names
			String query = "SELECT Category from category;";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			// enter all categories into an array
			while (rs.next()) {
				categories.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return categories;

	}
}
