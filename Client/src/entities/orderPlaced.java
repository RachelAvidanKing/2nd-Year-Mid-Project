package entities;

/**
 * This class is an entities for the orders placed.  It implements setters and getters
 * and various possible change to be made to the orders placed in the DB. 
 **/
public class orderPlaced {
	private String orderPrice,orderNumber,orderStatus,orderPlacedTime,orderReceivedBySuuplier,orderExpected,orderType;

	/**
	 *  Full constructor
	 * @param orderNumber
	 * @param orderStatus
	 * @param orderPlacedTime
	 * @param orderReceivedBySuuplier
	 * @param orderExpected
	 * @param orderType
	 * @param price
	 */
	public orderPlaced(String orderNumber, String orderStatus, String orderPlacedTime, String orderReceivedBySuuplier,
			String orderExpected, String orderType,String price) {
		super();
		this.orderNumber = orderNumber;
		this.orderStatus = orderStatus;
		this.orderPlacedTime = orderPlacedTime;
		this.orderReceivedBySuuplier = orderReceivedBySuuplier;
		this.orderExpected = orderExpected;
		this.orderType = orderType;
		orderPrice=price;
	}

	public String getOrderPrice() {
		return orderPrice;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderPlacedTime() {
		return orderPlacedTime;
	}

	public String getOrderReceivedBySuuplier() {
		return orderReceivedBySuuplier;
	}

	public String getOrderExpected() {
		return orderExpected;
	}

	public String getOrderType() {
		return orderType;
	}
	@Override
	public String toString() {
		return "orderNumber=" + orderNumber + ", orderStatus=" + orderStatus + ", orderPlacedTime="
				+ orderPlacedTime + ", orderReceivedBySuuplier=" + orderReceivedBySuuplier + ", orderExpected="
				+ orderExpected + ", orderType=" + orderType;
	}
}
