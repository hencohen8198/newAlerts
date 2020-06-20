package client.common.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import client.MainClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class represents the welcome form of Marketing Manager Marketing Manager
 * sign in and this the first form which appeared in the welcome form has the
 * full name of the marketing manager and form this form he can choose to which
 * form he wants to move forward
 * 
 * @author Yehonatan
 * @version 0.99
 */
public class MarketingManagerMenuWelcomeController extends AbstractController {
	/**
	 * An instance of the class
	 * 
	 */
	public static MarketingManagerMenuWelcomeController instance;
	
	// public static boolean fl = false;
	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button discount_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Label welcome_name;

	@FXML
	private Label Date_time_txt;

	@FXML
	private Label datelogin_welcome;

	/**
	 * this method is responsible for handling a click in the reports button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void reports_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerReportsForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is responsible for handling a click in the discount button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void discount_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerDiscountMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method displayes some guidence for the user about the page
	 * 
	 * @param event The event that caused the method to activate
	 */
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
		// Add a custom icon.
		stage.getIcons().add(new Image(this.getClass().getResource("/icons8-help-24.png").toString()));
		dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		dia.setContentText("\nReports button:\nHere you will able all create an new reports.\n\n"
				+ "Notification button:\nHere is all of your messages. You will find here your requests that you sent to the CEO.\n\n"
				+ "sales button:\nIn the sales menu are all of your sales pattern information, like station tag status, name and start hour, end hour\n\n"
				+ "Discount button:\nHere you will be able to watch the rates information");
		dia.show();
	}

	/**
	 * this method is responsible for handling a click in the logout button it sents
	 * the server a request to logout
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerMenuWelcomeController_logout_clicked",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {

	}

	/**
	 * this method is responsible for handling a click in the notification button
	 * and it switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerNotificationMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is responsible for handling a click in the sales button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		datelogin_welcome.setText(MainClientGUI.getUserloginDate());
		welcome_name.setText(MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
	}

	/**
	 * this method is activated when the server has successfully logged out the
	 * user. then the screen is switched to the main login screen
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}
}
