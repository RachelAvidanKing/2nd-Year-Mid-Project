package entities;


/**This class is a entity for the branchOrder. It implements setters and getters
 * and a constructor for new branchOrder*/
public class branchOrder {
	private String orderNumber,orderStatus,orderPlacedTime;
	
    /**
     * constructor for a new branch order with the specified details.
     *
     * @param orderNumber     the order number
     * @param orderPlacedTime the time the order was placed
     * @param orderStatus     the current status of the order
     */
	public branchOrder(String orderNumber, String orderPlacedTime, String orderStatus) {
		this.orderNumber = orderNumber;
		this.orderPlacedTime = orderPlacedTime;
		this.orderStatus = orderStatus;
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
	
	public void setOrderStatus(String orderStatus) {
	        this.orderStatus = orderStatus;
	}
	public void setOrderPlacedTime(String orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
}
	public String toString() {
		return "order number: "+orderNumber+"order status: "+orderStatus+"order date: "+orderPlacedTime;
		
	}
}