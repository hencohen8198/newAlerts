package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.NotificationCEO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the notification screen of the CEO. here will
 * be displayed notifications of the CEO
 *
 * @author Meni
 * @version 0.99
 */
public class CEONotificationPageController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static CEONotificationPageController instance;
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
	private Button confirm_btn;

	@FXML
	private Button reject_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Label new_rate;

	@FXML
	private TableView<NotificationCEO> rates_col;

	@FXML
	private TableColumn<NotificationCEO, Integer> ratetype_col;

	@FXML
	private TableColumn<NotificationCEO, String> date_col;

	@FXML
	private TableColumn<NotificationCEO, Float> oldrate_col;

	@FXML
	private TableColumn<NotificationCEO, Float> newrate_col;

	@FXML
	private TableColumn<NotificationCEO, String> from_col;

	@FXML
	private TableColumn<NotificationCEO, String> comment_col;

	/**
	 * in this method the CEO reject a discount offer that he get from marketing
	 * manager, after it this method delete the offer from the sql table
	 * (impending_rates ) and than update the marketing manager
	 * (marketing_manager_notification)
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void reject_btn_clicked(MouseEvent event) {

		if (rates_col.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(MainClientGUI.primaryStage);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Delete Request");
			alert.setContentText(String.format("Do you want to delete this change request?"));
			alert.initOwner(MainClientGUI.primaryStage.getOwner());
			Optional<ButtonType> res = alert.showAndWait();

			if (res.isPresent()) {
				if (res.get().equals(ButtonType.YES)) {
					String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					String quary2 = "INSERT INTO `marketing_manager_notification`(`rare_type`, `date`, `description`) VALUES ("
							+ " \"" + rates_col.getSelectionModel().getSelectedItem().getRatetype() + "\"" + ", \""
							+ timeStamp + "\",\"" + "Reject" + "\")";

					Message message2 = new Message(MessageType.UPDATEINFO,
							"CEONotificationPageController_update_marketing_manager", quary2);
					MainClientGUI.client.handleMessageFromClientUI(message2);
					String quary = "DELETE FROM `impending_rates` WHERE impending_rates.ratetype ="
							+ rates_col.getSelectionModel().getSelectedItem().getRatenum();
					Message message = new Message(MessageType.UPDATEINFO, "CEONotificationPageController_rate_delete",
							quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					switchScenes("/client/boundry/CEONotificationPageForm.fxml", "/client/boundry/CEOMenu.css");
					Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
					alert2.initOwner(MainClientGUI.primaryStage);
					alert2.getButtonTypes().remove(ButtonType.CANCEL);
					alert2.setTitle("Request remove");
					alert2.setContentText(String.format("The Request has been removed"));
					alert2.initOwner(MainClientGUI.primaryStage.getOwner());
					alert2.showAndWait();

				} else {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert.initOwner(MainClientGUI.primaryStage);
					alert1.setTitle("Delete Faild ");
					alert1.setHeaderText(null);
					alert1.setContentText("The request has not been deleted!");
					alert1.show();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(MainClientGUI.primaryStage);
			alert.setTitle("Choose Line ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A CHANGE REQUEST!");
			alert.show();
		}
	}

	/**
	 * in this method the CEO accept a discount offer that he get from marketing
	 * manager, and than update the marketing manager
	 * (marketing_manager_notification) after it this method delete the offer from
	 * the sql table (impending_rates )
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void confirm_btn_clicked(MouseEvent event) {
		// check that the you choose a line
		if (rates_col.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initOwner(MainClientGUI.primaryStage);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Change Request");
			alert.setContentText(String.format("Do you want to change this rate"));
			alert.initOwner(MainClientGUI.primaryStage.getOwner());
			Optional<ButtonType> res = alert.showAndWait();

			if (res.isPresent()) {
				if (res.get().equals(ButtonType.YES)) {

					String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					String quary2 = "INSERT INTO `marketing_manager_notification`(`rare_type`, `date`, `description`) VALUES ("
							+ " \"" + rates_col.getSelectionModel().getSelectedItem().getRatetype() + "\"" + ", \""
							+ timeStamp + "\",\"" + "Approve" + "\")";

					Message message2 = new Message(MessageType.UPDATEINFO,
							"CEONotificationPageController_update_marketing_manager", quary2);
					MainClientGUI.client.handleMessageFromClientUI(message2);

					String quary = "UPDATE `rates` SET `discoundPercent`="
							+ rates_col.getSelectionModel().getSelectedItem().getNewrate() + "WHERE rates.rateType ="
							+ rates_col.getSelectionModel().getSelectedItem().getRatenum();
					Message message = new Message(MessageType.UPDATEINFO, "CEONotificationPageController_rate_delete",
							quary);
					MainClientGUI.client.handleMessageFromClientUI(message);

					String quary1 = "DELETE FROM `impending_rates` WHERE impending_rates.ratetype ="
							+ rates_col.getSelectionModel().getSelectedItem().getRatenum();
					Message message1 = new Message(MessageType.UPDATEINFO, "CEONotificationPageController_rate_delete",
							quary1);
					MainClientGUI.client.handleMessageFromClientUI(message1);
					switchScenes("/client/boundry/CEONotificationPageForm.fxml", "/client/boundry/CEOMenu.css");
					Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
					alert2.initOwner(MainClientGUI.primaryStage);
					alert2.getButtonTypes().remove(ButtonType.CANCEL);
					alert2.setTitle("Request approved");
					alert2.setContentText(String.format("The Request has been approved"));
					alert2.initOwner(MainClientGUI.primaryStage.getOwner());
					alert2.showAndWait();


				} else {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.initOwner(MainClientGUI.primaryStage);
					alert1.setTitle("change Faild ");
					alert1.setHeaderText(null);
					alert1.setContentText("The request is not changed!");
					alert1.show();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(MainClientGUI.primaryStage);
			alert.setTitle("Choose Line ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A CHANGE REQUEST!");
			alert.show();
		}

	}

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
		dia.setContentText(
				"\nNotifications Rate :\nHere you will find all of the new rate offer made by your marketing manager.\n "
						+ "Reject button:\nIf you choose to reject, the rate update will be deleted.\n\n"
						+ "Delete button:\nif you choose to delete, the report will be deleted from the database\n"
						+ "Refresh button:\nrefresh the page\n" 
						+ "Open button:\nopen the report in a dialog screen\n" );
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
		Message message = new Message(MessageType.LOGOUT, "CEONotificationPageController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method made the menu button to a pop up button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CEOMenuWelcomePageForm.fxml", "/client/boundry/CEOMenu.css");
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
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());

		date_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, String>("ratedate"));
		comment_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, String>("comment"));
		from_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, String>("from"));
		ratetype_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, Integer>("ratetype"));
		oldrate_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, Float>("oldrate"));
		newrate_col.setCellValueFactory(new PropertyValueFactory<NotificationCEO, Float>("newrate"));

		String quary = "SELECT impending_rates.*,rates.discoundPercent,rates.rateName FROM impending_rates,rates WHERE impending_rates.ratetype=rates.rateType";
		Message msg = new Message(MessageType.REQUESTINFO, new String("CEONotificationsPageController_initialize"),
				quary);
		MainClientGUI.client.handleMessageFromClientUI(msg);

		String quary1 = "SELECT count(status) FROM impending_rates WHERE status=1";
		Message msg1 = new Message(MessageType.REQUESTINFO, new String("CEONotificationsPageController_notifications"),
				quary1);
		MainClientGUI.client.handleMessageFromClientUI(msg1);
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
	 * * this method is responsible for filling up the table of the notification.
	 * its recives the notification from the server
	 * 
	 * @param table the new table waiting for the CEO from the database
	 */
	public void return_CEONotifications(ArrayList<ArrayList<Object>> table) {
		ObservableList<NotificationCEO> rates = FXCollections.observableArrayList();

		for (ArrayList<Object> row : table) {
			rates.add(new NotificationCEO((String) row.get(7), (String) row.get(4), (Float) row.get(6),
					(Float) row.get(2), (String) row.get(1), (String) row.get(3), (Integer) row.get(0)));
		}
		rates_col.setItems(rates);
	}

	/**
	 * In this method we get the quantity of the new notification that we get from
	 * the database and than show it to the user in different color
	 * 
	 * @param table that hold the amount of new messages
	 */

	public void return_CEONotificationsAlret(ArrayList<ArrayList<Object>> table) {

		Platform.runLater(new Runnable() {
			public void run() {
				Long count = (Long) table.get(0).get(0);
				if (count == 0) {
					new_rate.setText("Update Rates");
				} else {
					new_rate.setStyle("-fx-font-weight: bold;");
					new_rate.setTextFill(javafx.scene.paint.Color.RED);
					new_rate.setText("You have " + count + " new notifications");
					String quary1 = "UPDATE `impending_rates` SET `status`= 0 WHERE status=1 ";
					Message msg1 = new Message(MessageType.UPDATEINFO, new String("CEONotificationsPageController"),
							quary1);
					MainClientGUI.client.handleMessageFromClientUI(msg1);
				}

			}
		});

	}

}
