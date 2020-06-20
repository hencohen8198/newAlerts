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
 * this class is the controller of the Welcome page screen of the CEO. here will
 * be displayed the Welcome from for the CEO
 *
 * @author Meni
 * @version 0.99
 */
public class CEOMenuWelcomePageController extends AbstractController {
	/**
	 * holding an instance of this controller here
	 */

	public static CEOMenuWelcomePageController instance;
	/**
	 * flag that help us with the pop up menu button
	 */
	public static boolean fl = false;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button reports_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Label user_fullname;

	@FXML
    private Label welcome_name;

	@FXML
	private Label Date_time_txt;
	
    @FXML
    private Label datelogin_welcome;

	/**
	 * this method displayes some guidence for the user about the specific page
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
		dia.setContentText("\nWelcome page :\nHere you will find the menu for the CEO");
		dia.show();
	}

	/**
	 * this method is responsible for handling a click in the logout button it sents
	 * the server a request to logout
	 * 
	 * @param event The event that caused the method to activate
	 * 
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "CEOMenuWelcomePageController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method made the menu button to a pop up button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		if (!fl) {
			menu_parent.getChildren().remove(notification_btn.getParent());
			menu_parent.getChildren().remove(reports_btn.getParent());

			fl = true;
		} else {
			menu_parent.getChildren().add(notification_btn.getParent());
			menu_parent.getChildren().add(reports_btn.getParent());
			fl = false;
		}
	}

	/**
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CEONotificationPageForm.fxml", "/client/boundry/CEOMenu.css");
	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void reports_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CEOReportPageForm.fxml", "/client/boundry/CEOMenu.css");
	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 * @param location  location of the controller
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		instance = this;
		datelogin_welcome.setText(MainClientGUI.getUserloginDate());
		welcome_name.setText(MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		/**
		 * this method is activated when the server has successfully logged out the
		 * user. then the screen is switched to the main login screen
		 */
	}

	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

}
