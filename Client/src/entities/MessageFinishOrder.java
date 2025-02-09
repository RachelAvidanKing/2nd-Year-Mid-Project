package entities;


/**
 * This class represents a message containing information about a completed order.
 * This message can be used for notifications for sent to the Client.
 */
public class MessageFinishOrder {
    String name, lastname, phonenumber, Delievery_type, orderNum,time,status;

    /**
     * Constructs a new MessageFinishOrder object with the specified details.
     *
     * @param name            the customer's first name
     * @param lastname         the customer's last name
     * @param phonenumber      the customer's phone number
     * @param Delievery_type   the type of delivery (e.g., Home Delivery)
     * @param orderNum         the unique identifier for the order
     * @param status           the current status of the order (e.g., Ready, Approved)
     */
	public MessageFinishOrder(String name, String lastname, String phonenumber, String Delievery_type,String num,String status) {
        this.name = name;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.Delievery_type = Delievery_type;
        this.status=status;
        orderNum=num;
    }


    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

    public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getDelievery_type() {
        return Delievery_type;
    }

    public void setDelievery_type(String delievery_type) {
        Delievery_type = delievery_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
    	String toSend="order update: receiver's name: "+name+"\n"+lastname+" phone number: "+phonenumber+"\norder number: "+orderNum+"\n status updated to: "+status;
    	if(status.equals("Ready")&& !Delievery_type.equals("Self Pickup"))
    	{
    		toSend+="\norder will arrive at "+time;
    	}
    	return toSend;
    	
    }
}