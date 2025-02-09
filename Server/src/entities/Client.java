package entities;

import java.io.Serializable;


/**This class is a entity for the Client. It implements setters and getters
 * and a constructor for new Client*/
public class Client implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String username,password,client_type,homeBranch,discount;
	
    /**
     * Constructs a new client with the specified credentials.
     *
     * @param username   the client's username
     * @param password   the client's password
     * @param client_type the client's type
     */
	public Client (String username,String password,String client_type) {
		this.username=username;
		this.password=password;
		this.client_type=client_type;
	}
	
	public String getDiscount()
	{
		return discount;
	}
	public void setDiscount(String disc)
	{
		discount=disc;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getClient_type() {
		return client_type;
	}
	public String gethomeBranch()
	{
		return homeBranch;
	}
	public void sethomeBranch(String homebranch) {
		this.homeBranch=homebranch;
	}

	@Override
	public String toString() {
		return "Client [username=" + username + ", password=" + password + ", client_type=" + client_type
				+ ", homeBranch=" + homeBranch + ", discount=" + discount + "]";
	}

}