package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import client.MainClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the main login screen,as well this controller
 * is responsible for sending request to the server in order to login the
 * system. here when the server grants permission this controller routs the user
 * to its corrent main screen after login.
 * 
 * @author henco
 * @version 0.99
 */
public class LoginController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static LoginController instance;

	@FXML
	private ImageView logo;

	@FXML
	private TextField username_text;

	@FXML
	private TextField password_text;

	@FXML
	private Button login_btn;

	@FXML
	private Button fast_fueling_btn;

	/**
	 * this method will switch the scene to the fueling simulation
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void fast_fueling_btn_clicked(MouseEvent event) {

		switchScenes("/client/boundry/CustomerOrdersVehicleRefuelForm.fxml", "");
	}

	/**
	 * this method is responsible for handling the "login" button getting pressed
	 * the method sends to the server the information of the loggin.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void login_clicked(MouseEvent event) {
		String user, pass;
		user = username_text.getText();
		pass = password_text.getText();
		Message message = new Message(MessageType.LOGIN_REQUEST, "LoginController_login_clicked",
				"SELECT * FROM users WHERE users.username = \"" + user + "\" AND users.password = \"" + pass + "\"");
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	public void return_login_failed(String errorMsg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("LOGIN FAIELD");
				alert.setHeaderText(null);
				alert.setContentText(errorMsg);
				alert.show();

			}
		});
	}

	/**
	 * this method is activated when the clientMessageHandler recives the
	 * information of the user (if he is a customer or an employee), then the method
	 * sends the currect user to its appropriate screen (considering his position)
	 * 
	 * @param user contains information regarding the user
	 */
	public void return_login_success(ArrayList<ArrayList<Object>> user) {
		MainClientGUI.setUserID(((int) user.get(0).get(2)));
		MainClientGUI.loggedIn = true;
		MainClientGUI.setUserFirstName((String) user.get(0).get(5));
		MainClientGUI.setUserLastName((String) user.get(0).get(6));
		MainClientGUI.setUserloginDate((String) user.get(0).get(7));
		Message message = new Message(MessageType.UPDATEINFO, "LoginController_update_last_login",
				"UPDATE users SET lastLogin = \"" + (new Date()).toString() + "\" WHERE userID = "
						+ MainClientGUI.getUserID());
		MainClientGUI.client.handleMessageFromClientUI(message);

		switch ((int) user.get(0).get(3)) {
		case 1:
			switchScenes("/client/boundry/StationManagerForm.fxml", "/client/boundry/StationManager.css");
			break;
		case 2:
			switchScenes("/client/boundry/MarketingAgentMenuWelcomeForm.fxml",
					"/client/boundry/MarketingAgentMainCustomer.css");
			break;

		case 3:
			switchScenes("/client/boundry/CustomerMainForm.fxml", "/client/boundry/CustomerOrders.css");
			break;

		case 4:
			switchScenes("/client/boundry/CEOMenuWelcomePageForm.fxml", "/client/boundry/CEOMenu.css");
			break;

		case 5:
			switchScenes("/client/boundry/SupplierWelcomeForm.fxml", "/client/boundry/SupplierWelcomeCss.css");
			String query5 = "SELECT count(supplier_orders_list.status) FROM supplier_orders_list,employees where status='New' AND employees.userID = "
					+ MainClientGUI.getUserID() + " AND supplier_orders_list.eid = employees.eid";
			Message msg5 = new Message(MessageType.REQUESTINFO, "SupplierWelcomeController_new_notificatios", query5);
			MainClientGUI.client.handleMessageFromClientUI(msg5);
			break;
		case 6:
			switchScenes("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
					"/client/boundry/MarketingManagerMain.css");
			break;
		}
	}

	/**
	 * after the user is confirmed by the system as a registered user, the system
	 * checks if the user is a customer or a employee
	 * 
	 * @param user the info of the user logged in
	 */
	public void get_user_firstName_AND_lastName(ArrayList<ArrayList<Object>> user) {
		String usern, pass;
		usern = username_text.getText();
		pass = password_text.getText();
		if ((int) user.get(0).get(3) == 3) {
			Message message = new Message(MessageType.LOGIN, "get_user_firstName_AND_lastName",
					"SELECT users.username,users.password,users.userID,users.type,users.connection_status,customers.firstName,customers.lastName,users.lastLogin FROM users,customers WHERE users.userID=customers.userID AND users.username = \""
							+ usern + "\" AND users.password = \"" + pass + "\"");
			MainClientGUI.client.handleMessageFromClientUI(message);
		} else {
			Message message = new Message(MessageType.LOGIN, "get_user_firstName_AND_lastName",
					"SELECT users.username,users.password,users.userID,users.type,users.connection_status,employees.firstName,employees.lastName,users.lastLogin FROM users,employees WHERE users.userID=employees.userID AND users.username = \""
							+ usern + "\" AND users.password = \"" + pass + "\"");
			MainClientGUI.client.handleMessageFromClientUI(message);
		}
	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here. first thing that we do is to run the client in order to
	 * connect to the server
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		MainClientGUI.run_Client();
		MainClientGUI.new_notifications = false;

	}

}