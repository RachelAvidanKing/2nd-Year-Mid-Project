package entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This class is an entities for the orders.  It implements setters and getters
 * and various possible change to be made to the orders in the DB. 
 **/
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private String restaurant;
	private String branch;
	private String city,street,homeNum;
	private String deliveryMethod;
	private String phoneNum;
	private String workplace;
	private Double finalPrice=0.0;
	private String nickname;
	private LocalDate date;
	private String startTime, wantedTime;
	private ArrayList<Meal> meals = new ArrayList<>();
	
	public String getCity() {
		return city;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHomeNum() {
		return homeNum;
	}

	public void setHomeNum(String homeNum) {
		this.homeNum = homeNum;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getStartTime() {
		return startTime;
	}


	public String getWantedTime() {
		return wantedTime;
	}


	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public void setWantedTime(String wantedTime) {
		this.wantedTime = wantedTime;
	}


	public String toString() {
		return "Restaurant: " + restaurant+"\nDate: " + date + "\nStart Time: " + startTime +"\nWanted Time: " + wantedTime
				+"\ndishes ordered:\n"+meals.toString()+ "\nFinal Price: $" + finalPrice;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}
	
	/**
	 * Method to add a dish to the order's list of dishes and add it's price to the
	 * order's final price.
	 * @param meal to be added to array
	 */
	public void addToDishList(Meal meal)
	{
		meals.add(meal);
		finalPrice+=Double.parseDouble(meal.getPrice());
	}
	
	public ArrayList<Meal> getDishList()
	{
		return meals;
	}
}
