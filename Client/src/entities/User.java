package entities;

import java.io.Serializable;

/**
 * Represents a user with details such as ID, name, phone number, email,
 * user type, and credit card number.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -2637045140761759044L;

    private String firstName;
    private String lastName;
    private int ID;
    private String phoneNum;
    private String email;
    private String userType;
    private String creditCardNumber;

    /**
     * Constructs a new User with specified details.
     * 
     * @param ID               the unique identifier for the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param phoneNum         the phone number of the user
     * @param email            the email address of the user
     * @param userType         the type of user (e.g., admin, regular)
     * @param creditCardNumber the credit card number of the user
     */
    public User(int ID, String firstName, String lastName, String phoneNum, String email, String userType, String creditCardNumber) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userType = userType;
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Constructs a new User with default values.
     */
    public User() {
        // Default constructor
    }

    /**
     * Returns the unique identifier of the user.
     * 
     * @return the ID of the user
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier of the user.
     * 
     * @param ID the new ID of the user
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Returns the first name of the user.
     * 
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * 
     * @param firstName the new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     * 
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * 
     * @param lastName the new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the phone number of the user.
     * 
     * @return the phone number of the user
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Sets the phone number of the user.
     * 
     * @param phoneNum the new phone number of the user
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * Returns the email address of the user.
     * 
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * 
     * @param email the new email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user type.
     * 
     * @return the user type
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the user type.
     * 
     * @param userType the new user type
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Returns the credit card number of the user.
     * 
     * @return the credit card number
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Sets the credit card number of the user.
     * 
     * @param creditCardNumber the new credit card number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                '}';
    }
}