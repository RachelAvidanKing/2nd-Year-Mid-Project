package entities;

import java.io.Serializable;


/**
 * This class represents a delivery branch with an ID and name.It implements setters and getters
 * and a constructor for new deliveryBranch
 */

 
public class DeliveryBranch implements Serializable {

	private static final long serialVersionUID = 2690985245000831035L;
	private String ID;
	private String name;

    /**
     * Constructs a new delivery branch with the specified ID and name.
     *
     * @param ID   the unique identifier of the delivery branch
     * @param name the name of the delivery branch
     */
	public DeliveryBranch(String ID, String name) {
		this.setID(ID);
		this.setName(name);
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}