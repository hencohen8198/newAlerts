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
 * this class is the controller of the main screen of the station manager. here
 * will be displayed some info of the user.
 *
 * @author henco
 * @version 0.99
 */
public class StationManagerController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static StationManagerController instance;
	// private static boolean fl = false;

	/**
	 * holds the station number of the station manager
	 */
	public static String station_tag_number;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button main_btn;

	@FXML
	private Button report_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button station_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Label user_position;

	@FXML
	private Label welcome_name;
	
    @FXML
    private Label datelogin_welcome;

	/**
	 * this method is responsible for handling a click in the logout button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "StationManagerController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * Will display all of the relevant information that a screencan do
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
		dia.setContentText(
				"\nReports button:\nHere you will find all of the reports made by you, there you can make updated reports and sent them to the CEO\n\n"
						+ "Notification button:\nHere is all of your messages. You will find here orders made by MyFuel.\n\n"
						+ "Station button:\nIn the station menu are all of your stations information, like reserves status, name and location");
		dia.show();
	}

	/**
	 * this method is responsible for handling a click in the manu button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {

//		if (!fl) {
//			menu_parent.getChildren().remove(report_btn.getParent());
//			menu_parent.getChildren().remove(notification_btn.getParent());
//			menu_parent.getChildren().remove(station_btn.getParent());
//			fl = true;
//		} else {
//			menu_parent.getChildren().add(report_btn.getParent());
//			menu_parent.getChildren().add(notification_btn.getParent());
//			menu_parent.getChildren().add(station_btn.getParent());
//			fl = false;
//		}
	}

	/**
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
				"/client/boundry/StationManagerNotification.css");
	}

	/**
	 * this method is responsible for handling a click in the report button it sents
	 * the server a request to logout
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void report_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerReportsForm.fxml", "/client/boundry/StationManagerReports.css");
	}

	/**
	 * this method is responsible for handling a click in the station button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void station_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerStationForm.fxml", "/client/boundry/StationManagerStation.css");
	}

	/**
	 * this method is activtaed when the server has successfully logged out the
	 * user. then the screen is switched to the main login screen
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
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
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		welcome_name.setText(MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		String quary = "SELECT stations.stationTag FROM stations, employees WHERE employees.userID = "
				+ MainClientGUI.getUserID() + " AND stations.eid = employees.eid";
		Message message = new Message(MessageType.REQUESTINFO, "StationManagerController_initialize", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
		String qString = "SELECT impending_rates.*,rates.discoundPercent,rates.rateName FROM impending_rates,rates WHERE impending_rates.ratetype=rates.rateType";
		Message message2 = new Message(MessageType.REQUESTINFO, "A", qString);
		MainClientGUI.client.handleMessageFromClientUI(message2);
		datelogin_welcome.setText(MainClientGUI.getUserloginDate());
	}

	/**
	 * TODO
	 */
	void hide_manu() {
		menu_parent.getChildren().remove(report_btn.getParent());
		menu_parent.getChildren().remove(notification_btn.getParent());
		menu_parent.getChildren().remove(station_btn.getParent());
	}

	/**
	 * TODO
	 */
	void show_menu() {
		menu_parent.getChildren().add(report_btn.getParent());
		menu_parent.getChildren().add(notification_btn.getParent());
		menu_parent.getChildren().add(station_btn.getParent());
	}

}
