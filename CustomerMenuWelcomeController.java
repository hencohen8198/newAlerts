package client.common.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import message_info.Message;
import message_info.MessageType;
import java.net.URL;
import java.util.ResourceBundle;
import client.MainClientGUI;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 *This controller Class will use as the menu class in the user intrance 
 *it will present for him the side bar buttons functionality and the last date of his login to the system
 *Morveover the class contains methods to help(explantion on methods), and methods to switch scenes.
 *
 * @author Itay
 * @version 0.99
 *
 */
public class CustomerMenuWelcomeController extends AbstractController {
	
	/**
	 * hold the instance of the object   
	 * */
	public static CustomerMenuWelcomeController instance;
	
	@FXML
	private Label user_fullname;

    @FXML
    private Button orders_btn;

    @FXML
    private Button notifications_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Button help_btn;
    
    @FXML
    private Label welcome_name;
    
    @FXML
    private Label datelogin_welcome;
    
    /**
	 * This method will apply after mouse clicked on help button, and will present for the user
	 * A guide for the specific page, details about the page, and filling forms insturactions.
	 * 
	 * @param event mouse clicked
	 * 
	 * */
    @FXML
    void help_btn_clicked(MouseEvent event) {
    	
    	Dialog<String> dia = new Dialog<>();
		Stage stage = (Stage) dia.getDialogPane().getScene().getWindow();
		DialogPane dialogPane = dia.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/client/boundry/dialog.css").toExternalForm());
		dialogPane.getStyleClass().add("dialog");
		dia.setTitle("Help Dialog");
		dia.setHeaderText("Guide:");
		dia.setGraphic(new ImageView(this.getClass().getResource("/icons8-info-48.png").toString()));
		stage.getIcons().add(new Image(this.getClass().getResource("/icons8-help-24.png").toString()));
		dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		dia.setContentText("\n•Main customer menu:\n"
				+ "\n•You may observe the customer last login to the \n system and the side bar of the system functionality \n for the customer.");
		dia.show();

    }
    /**
	 * This method will apply after mouse clicked on logout button, and will logout the user
	 * From the system.
	 * After the click on this button, the user will return to the main login form.
	 * 
	 * @param event mouse clicked
	 * 
	 * */
    @FXML
    void logout_btn_clicked(MouseEvent event) {
    	String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "CustomerWelcomeController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

    }
    /**
	 * This method will apply after mouse clicked on notifications button, and will present the user
	 * The notifications form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 * */
    @FXML
    void notifications_btn_clicked(MouseEvent event) {
    	switchScenes("/client/boundry/CustomerNotifications.fxml", "/client/boundry/CustomerOrders.css");
    }
    /**
	 * This method will apply after mouse clicked on orders button, and will present the user
	 * The orders form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 * */
    @FXML
    void orders_btn_clicked(MouseEvent event) {
    	switchScenes("/client/boundry/CustomerOrdersMainForm.fxml", "/client/boundry/CustomerOrders.css");
    }
    /**
	 * This method will initlize the user name and his last name
	 * 
	 */	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		welcome_name.setText(MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		datelogin_welcome.setText(MainClientGUI.getUserloginDate());

	}
	/**
	 * This method will apply after logout succsess(the trigger for this method activation was
	 * From the message handler client).
	 * The method will change the form to the main form .
	 * 
	 * */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
		
	}
	
	

}
