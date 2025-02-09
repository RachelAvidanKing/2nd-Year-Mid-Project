package entities;

import java.io.Serializable;

/**This class is a entity for the Meal. It implements setters and getters
 * and a constructor for new Meal*/
public class Meal implements Serializable {
	private static final long serialVersionUID = 1L;
		private String name, type, optionalComponents,price;

	    /**
	     * Constructs a new meal with the specified details.
	     *
	     * @param name               the name of the meal
	     * @param type                the type of the meal (e.g., main, drink, dessert)
	     * @param price               the price of the meal
	     * @param optionalComponents a comma-separated string listing optional components that can be added to the meal (or null if none)
	     */
		public Meal(String name, String type, String price, String optionalComponents) {
			this.name = name;
			this.type = type;
			this.optionalComponents = optionalComponents;
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getOptionalComponents() {
			return optionalComponents;
		}

		public void setOptionalComponents(String optionalComponents) {
			this.optionalComponents = optionalComponents;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}
		@Override
		public String toString() {
			return "dish name = " + name + "\n " + "type = " + type + "\n " + "optionalComponents = " + optionalComponents + "\n " +  "price = " + price + "\n ";
		}
	}