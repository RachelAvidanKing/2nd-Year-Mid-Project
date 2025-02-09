package entities;

import java.io.Serializable;
import java.sql.Date;


/**
 * This class is an entities for the reports.  It implements setters and getters
 * and various possible change to be made to the reports in the DB. 
 **/
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String Restaurant_ID;
    private String Restaurant_Name;
    private Date Date_For_Report;
    private int Total_Num_Orders;
    private int Num_Order_Late;
    private double Restaurant_Revenue;
    private int Num_Of_Salads;
    private int Num_Of_Appetizers;
    private int Num_Of_Main;
    private int Num_Of_Desserts;
    private int Num_Of_Drinks;

    /**
     *  Default constructor
     */
    public Report() {
    }

    /**
     *  Full constructor
     * @param restaurant_ID
     * @param date_For_Report
     * @param total_Num_Orders
     * @param num_Order_Late
     * @param restaurant_Revenue
     * @param num_Of_Salads
     * @param num_Of_Appetizers
     * @param num_Of_Main
     * @param num_Of_Desserts
     * @param num_Of_Drinks
     */
    public Report(String restaurant_ID, Date date_For_Report, int total_Num_Orders, int num_Order_Late,
            double restaurant_Revenue, int num_Of_Salads, int num_Of_Appetizers, int num_Of_Main, 
            int num_Of_Desserts, int num_Of_Drinks) {
  this.Restaurant_ID = restaurant_ID;
  this.Date_For_Report = date_For_Report;
  this.Total_Num_Orders = total_Num_Orders;
  this.Num_Order_Late = num_Order_Late;
  this.Restaurant_Revenue = restaurant_Revenue;
  this.Num_Of_Salads = num_Of_Salads;
  this.Num_Of_Appetizers = num_Of_Appetizers;
  this.Num_Of_Main = num_Of_Main;
  this.Num_Of_Desserts = num_Of_Desserts;
  this.Num_Of_Drinks = num_Of_Drinks;
}

    
 // Constructor
 	public Report(String restaurant_ID, Date date_For_Report, int total_Num_Orders, int num_Order_Late,
 			double restaurant_Revenue, int num_Of_Salads, int num_Of_Appetizers, int num_Of_Main, int num_Of_Desserts,
 			int num_Of_Drinks, String restaurant_name) {
 		this.Restaurant_ID = restaurant_ID;
 		this.Date_For_Report = date_For_Report;
 		this.Total_Num_Orders = total_Num_Orders;
 		this.Num_Order_Late = num_Order_Late;
 		this.Restaurant_Revenue = restaurant_Revenue;
 		this.Num_Of_Salads = num_Of_Salads;
 		this.Num_Of_Appetizers = num_Of_Appetizers;
 		this.Num_Of_Main = num_Of_Main;
 		this.Num_Of_Desserts = num_Of_Desserts;
 		this.Num_Of_Drinks = num_Of_Drinks;
 		this.Restaurant_Name = restaurant_name;
 	}
    /**
     * Constructor for dynamic columns
     * @param restaurant_Name
     * @param restaurant_Revenue
     */
    public Report(String restaurant_Name, Double restaurant_Revenue) {
        this.Restaurant_Name = restaurant_Name;
        this.Restaurant_Revenue = restaurant_Revenue;
    }

    /**
     *  Constructor for adding methods
     * @param num_Of_Salads
     * @param num_Of_Appetizers
     * @param num_Of_Main
     * @param num_Of_Desserts
     * @param num_Of_Drinks
     */
    public Report(int num_Of_Salads, int num_Of_Appetizers, int num_Of_Main, int num_Of_Desserts, int num_Of_Drinks) {
        this.Num_Of_Salads = num_Of_Salads;
        this.Num_Of_Appetizers = num_Of_Appetizers;
        this.Num_Of_Main = num_Of_Main;
        this.Num_Of_Desserts = num_Of_Desserts;
        this.Num_Of_Drinks = num_Of_Drinks;
    }


    public String getRestaurant_ID() {
        return Restaurant_ID;
    }

    public void setRestaurant_ID(String restaurant_ID) {
        this.Restaurant_ID = restaurant_ID;
    }

    public String getRestaurant_Name() {
        return Restaurant_Name;
    }

    public void setRestaurant_Name(String restaurant_Name) {
        this.Restaurant_Name = restaurant_Name;
    }

    public Date getDate_For_Report() {
        return Date_For_Report;
    }

    public void setDate_For_Report(Date date_For_Report) {
        this.Date_For_Report = date_For_Report;
    }

    public int getTotal_Num_Orders() {
        return Total_Num_Orders;
    }

    public void setTotal_Num_Orders(int total_Num_Orders) {
        this.Total_Num_Orders = total_Num_Orders;
    }
    
    /**
     * This method adds to the total number of orders of the report.
     * @param additionalOrders the amount of orders to add.
     */
    public void addTotal_Num_Orders(int additionalOrders) {
        this.Total_Num_Orders += additionalOrders;
    }

    public int getNum_Order_Late() {
        return Num_Order_Late;
    }

    public void setNum_Order_Late(int num_Order_Late) {
        this.Num_Order_Late = num_Order_Late;
    }

    /**
     * This method adds to the number of late orders of the report.
     * @param additionalLateOrders the amount of orders to add.
     */
    public void addNum_Order_Late(int additionalLateOrders) {
        this.Num_Order_Late += additionalLateOrders;
    }

    public double getRestaurant_Revenue() {
        return Restaurant_Revenue;
    }

    public void setRestaurant_Revenue(double restaurant_Revenue) {
        this.Restaurant_Revenue = restaurant_Revenue;
    }

    /**
     * This method adds to the the total revenue of the report.
     * @param additionalRevenue the amount of revenue to add.
     */
    public void addRestaurant_Revenue(double additionalRevenue) {
        this.Restaurant_Revenue += additionalRevenue;
    }

    public int getNum_Of_Salads() {
        return Num_Of_Salads;
    }

    public void setNum_Of_Salads(int num_Of_Salads) {
        this.Num_Of_Salads = num_Of_Salads;
    }

    /**
     * This method adds to the number of salads in the report.
     * @param additionalSalads the amount of salads to add.
     */
    public void addNum_Of_Salads(int additionalSalads) {
        this.Num_Of_Salads += additionalSalads;
    }

    public int getNum_Of_Appetizers() {
        return Num_Of_Appetizers;
    }

    public void setNum_Of_Appetizers(int num_Of_Appetizers) {
        this.Num_Of_Appetizers = num_Of_Appetizers;
    }

    /**
     * This method adds to the number of appetizers in the report.
     * @param additionalAppetizers the amount of salads to add.
     */
    public void addNum_Of_Appetizers(int additionalAppetizers) {
        this.Num_Of_Appetizers += additionalAppetizers;
    }

    public int getNum_Of_Main() {
        return Num_Of_Main;
    }

    public void setNum_Of_Main(int num_Of_Main) {
        this.Num_Of_Main = num_Of_Main;
    }

    /**
     * This method adds to the number of main meals in the report.
     * @param additionalSalads the amount of main meals to add.
     */
    public void addNum_Of_Main(int additionalMainCourses) {
        this.Num_Of_Main += additionalMainCourses;
    }

    public int getNum_Of_Desserts() {
        return Num_Of_Desserts;
    }

    public void setNum_Of_Desserts(int num_Of_Desserts) {
        this.Num_Of_Desserts = num_Of_Desserts;
    }

    /**
     * This method adds to the number of desserts in the report.
     * @param additionalSalads the amount of desserts to add.
     */
    public void addNum_Of_Desserts(int additionalDesserts) {
        this.Num_Of_Desserts += additionalDesserts;
    }

    public int getNum_Of_Drinks() {
        return Num_Of_Drinks;
    }

    public void setNum_Of_Drinks(int num_Of_Drinks) {
        this.Num_Of_Drinks = num_Of_Drinks;
    }

    /**
     * This method adds to the number of drinks in the report.
     * @param additionalSalads the amount of drinks to add.
     */
    public void addNum_Of_Drinks(int additionalDrinks) {
        this.Num_Of_Drinks += additionalDrinks;
    }

    @Override
    public String toString() {
        return "Report{" +
               "Restaurant_ID='" + Restaurant_ID + '\'' +
               ", Restaurant_Name='" + Restaurant_Name + '\'' +
               ", Date_For_Report=" + Date_For_Report +
               ", Total_Num_Orders=" + Total_Num_Orders +
               ", Num_Order_Late=" + Num_Order_Late +
               ", Restaurant_Revenue=" + Restaurant_Revenue +
               ", Num_Of_Salads=" + Num_Of_Salads +
               ", Num_Of_Appetizers=" + Num_Of_Appetizers +
               ", Num_Of_Main=" + Num_Of_Main +
               ", Num_Of_Desserts=" + Num_Of_Desserts +
               ", Num_Of_Drinks=" + Num_Of_Drinks +
               '}';
    }
}


//---------------------------------------------------------------------------------------------------------------

