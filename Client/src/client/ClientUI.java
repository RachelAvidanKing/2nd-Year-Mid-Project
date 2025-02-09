package client;

import ClientGui.OrderStartGui;
import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The ClientUI class is the entry point of the JavaFX application. It extends
 * the {@link javafx.application.Application} class and initializes the GUI
 * for the client application.
 * 
 * This class is responsible for launching the application and starting the
 * main window of the client application using the {@link OrderStartGui} class.
 * 
 * It contains a static field {@code chat} that represents the single instance
 * of the client console used for communication with the server.
 * 
 * @see javafx.application.Application
 * @see OrderStartGui
 * 
 * @author Daniel Feldman
 * @author Orian Haziza
 * @author Keren Kazetchinski
 * @version August 2024
 */
public class ClientUI extends Application {
	/**
     * The single instance of {@link ClientConsole} used for communication with
     * the server. This is intended to ensure that only one instance of the
     * client console exists throughout the application's lifecycle.
     */
	public static ClientConsole chat; //only one instance
	/**
     * The main method, which serves as the entry point for the JavaFX application.
     * It launches the JavaFX application by calling the {@link #start(Stage)}
     * method.
     *
     * @param args Command-line arguments passed to the application.
     * @throws Exception If an error occurs during the launch of the application.
     */
	public static void main(String[] args) throws Exception
	{
		launch(args);
	}
	/**
     * Initializes and starts the main GUI of the client application. This method
     * is called by the JavaFX runtime to set up the primary stage and show the
     * initial user interface.
     *
     * @param primaryStage The primary stage for the application, onto which the
     *                     application scene is set.
     * @throws Exception If an error occurs while initializing the GUI.
     */
	@Override
	public void start(Stage primaryStage) throws Exception 
	{	 
		 OrderStartGui aFrame = new OrderStartGui(); 
		aFrame.start(primaryStage);
	}
}