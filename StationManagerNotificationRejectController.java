package client.common.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.MainClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the order reject screen of the station
 * manager. here will be displayed the order rejected before the final reject
 *
 * @author henco
 * @version 0.99
 */
public class StationManagerNotificationRejectController extends AbstractController {

	/**
	 * instance of this controller
	 */
	public static StationManagerNotificationRejectController instance;
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
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Button back_btn;

	@FXML
	private Button reject_btn;

	@FXML
	private TextArea text_input;

	@FXML
	private Label supplier_label;

	@FXML
	private Label fuelType_label;

	@FXML
	private Label amount_label;

	@FXML
	private Label date_label;

	/**
	 * this method returns the user to the previous screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
				"/client/boundry/StationManagerNotification.css");
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
		dia.setContentText(
				"\nHere is the main reject menu.\nBefore you reject an order, please look over it and consider rejecting. You may specify youre reasoning in the description");
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
		Message message = new Message(MessageType.LOGOUT, "StationManagerNotificationRejectController_logout_clicked",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method is activated when the server has successfully logged out the
	 * user. then the screen is switched to the main login screen
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * TODO
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {
		
		switchScenes("/client/boundry/StationManagerForm.fxml", "/client/boundry/StationManager.css");
//		if (!fl) {
//			hide_manu();
//			fl = true;
//		} else {
//			show_menu();
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
	 * this method is responsible for handling a click in the reject button it saves
	 * the resson for rejecting and then deletes from the orders table
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void reject_btn_clicked(MouseEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.CANCEL);
		alert.getButtonTypes().add(ButtonType.YES);
		alert.setTitle("Reject Order");
		alert.setContentText(String.format("Are You Sure You Want To Reject The Order?"));
		alert.initOwner(MainClientGUI.primaryStage.getOwner());
		Optional<ButtonType> res = alert.showAndWait();

		if (res.isPresent()) {
			if (res.get().equals(ButtonType.CANCEL)) {
				event.consume();
			} else {
				if (!text_input.getText().isEmpty()) {
					String quary = "UPDATE supplie_order SET supplie_order.status = \"Rejected:\n"
							+ text_input.getText() + "\" WHERE supplie_order.orderTag = "
							+ StationManagerNotificationController.instance.selected_order.getOrderID();
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerNotificationRejectController_reject_btn_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
				} else {
					String quary = "UPDATE supplie_order SET supplie_order.status = \"Rejected\" WHERE supplie_order.orderTag = "
							+ StationManagerNotificationController.instance.selected_order.getOrderID();
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerNotificationRejectController_reject_btn_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
				}
				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert2.setTitle("Success");
				alert2.setHeaderText(null);
				alert2.setContentText("The Order Has Been Successfully Rejected");
				Optional<ButtonType> res2 = alert2.showAndWait();
				if (res2.isPresent()) {
					switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
							"/client/boundry/StationManagerNotification.css");
				}
			}
		}
	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the corrent screen
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
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 * @param location  location of the controller
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
//		if (fl) {
//			hide_manu();
//		}
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		supplier_label.setText(supplier_label.getText() + " "
				+ StationManagerNotificationController.instance.selected_order.getSupplier());
		fuelType_label.setText(fuelType_label.getText() + " "
				+ StationManagerNotificationController.instance.selected_order.getFuelType());
		date_label.setText(
				date_label.getText() + " " + StationManagerNotificationController.instance.selected_order.getDate());
		amount_label.setText(amount_label.getText() + " "
				+ StationManagerNotificationController.instance.selected_order.getQuantity());
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
