package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.NotificationMarketingManager;
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
 * This class represents the notifications of Marketing Manager, display the
 * requests that Marketing Managers sent to the CEO
 * 
 * @author Yehonatan
 * @version 0.99
 *
 */
public class MarketingManagerNotificationController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingManagerNotificationController instance;
	// public static boolean fl = false;
	// Observable list for the list of the notifications
	private ObservableList<NotificationMarketingManager> notofications = FXCollections.observableArrayList();
	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button reports_btn;

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
	private Label new_notification;

	@FXML
	private TableView<NotificationMarketingManager> notification_table;

	@FXML
	private TableColumn<NotificationMarketingManager, String> titleColumn;

	@FXML
	private TableColumn<NotificationMarketingManager, String> dateColumn;

	@FXML
	private TableColumn<NotificationMarketingManager, String> descriptionColumn;

	@FXML
	private Button delete_btn;

	/**
	 * this method is handling the case of an order being deleted
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		if (notification_table.getSelectionModel().getSelectedItem() != null) {
			if (!notification_table.getSelectionModel().getSelectedItem().getDescription().equals("Waiting")) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.getButtonTypes().remove(ButtonType.OK);
				alert.getButtonTypes().add(ButtonType.CANCEL);
				alert.getButtonTypes().add(ButtonType.YES);
				alert.setTitle("Delete Notification");
				alert.setContentText(String.format("Do you want to delete this notification?"));
				alert.initOwner(MainClientGUI.primaryStage.getOwner());
				Optional<ButtonType> res = alert.showAndWait();

				if (res.isPresent()) {
					if (res.get().equals(ButtonType.YES)) {
						String quary = "DELETE FROM `marketing_manager_notification` WHERE marketing_manager_notification.date="
								+ " \"" + notification_table.getSelectionModel().getSelectedItem().getRatedate() + "\"";
						Message message = new Message(MessageType.UPDATEINFO,
								"CEONotificationPageController_update_marketing_manager", quary);
						MainClientGUI.client.handleMessageFromClientUI(message);
						Alert alert1 = new Alert(AlertType.CONFIRMATION);
						alert1.setTitle("Message deleted");
						alert1.setHeaderText(null);
						alert1.setContentText("The message was deleted successfully");
						alert1.show();
						switchScenes("/client/boundry/MarketingManagerNotificationMainForm.fxml",
								"/client/boundry/MarketingManagerMain.css");
					}

				} else {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Delete Faild ");
					alert2.setHeaderText(null);
					alert2.setContentText("The notifications has not been deleted!");
					alert2.show();
				}
			} else {
				Alert alert3 = new Alert(AlertType.ERROR);
				alert3.setTitle("Delete Faild ");
				alert3.setHeaderText(null);
				alert3.setContentText("You cant delete notifications with Waiting status !");
				alert3.show();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose Line ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A CHANGE REQUEST!");
			alert.show();
		}

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
		dia.setContentText(
				"\nIn this page you will see your requests to update the rate and you can see the status of your requests and other details\nDelete button:\nHere you will able to delete the choosen request that it status is approved or rejected.");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerNotificationController_logout_clicked",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
				"/client/boundry/MarketingManagerMain.css");
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
	 * 
	 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		titleColumn.setCellValueFactory(new PropertyValueFactory<NotificationMarketingManager, String>("ratetype"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<NotificationMarketingManager, String>("ratedate"));
		descriptionColumn
				.setCellValueFactory(new PropertyValueFactory<NotificationMarketingManager, String>("description"));
		// this query is for get from DB the requests that waiting for approve or reject
		String quary2 = "SELECT rates.rateName,impending_rates.date from impending_rates,rates WHERE impending_rates.rateType=rates.rateType";
		Message messageNotification = new Message(MessageType.REQUESTINFO,
				"MarketingManagerNotificationController_notification_tableImpending", quary2);
		MainClientGUI.client.handleMessageFromClientUI(messageNotification);
		// this query is for get from DB the requests that approved or rejected
		String quary = "SELECT * FROM `marketing_manager_notification`";
		Message message = new Message(MessageType.REQUESTINFO,
				"MarketingManagerNotificationController_notification_table", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
		// this query is for get from DB the how much new notification has 
		String quary1 = "SELECT count(status) FROM marketing_manager_notification WHERE status=1";
		Message msg1 = new Message(MessageType.REQUESTINFO,
				new String("MarketingManagerNotificationController_new_notification"), quary1);
		MainClientGUI.client.handleMessageFromClientUI(msg1);
	}
/**
 * this method get the requests that waiting for approve or reject
 * and put them in the list
 * @param table get the the request that are waiting status in DB
 */
	public void return_notificationImpendenig_table(ArrayList<ArrayList<Object>> table) {
		// ObservableList<NotificationMarketingManager> notofications =
		// FXCollections.observableArrayList();

		for (ArrayList<Object> row : table) {
			notofications.add(0, new NotificationMarketingManager((String) row.get(0), (String) row.get(1), "Waiting"));
		}
		notification_table.setItems(notofications);
	}
	/**
	 * this method get the requests that approved or rejected
	 * and put them in the list
	 * @param table get the request with apporved or rejected status in in DB
	 */
	public void return_notification_table(ArrayList<ArrayList<Object>> table) {
		// ObservableList<NotificationMarketingManager> notofications =
		// FXCollections.observableArrayList();

		for (ArrayList<Object> row : table) {
			notofications.add(0,
					new NotificationMarketingManager((String) row.get(0), (String) row.get(1), (String) row.get(2)));
		}
		notification_table.setItems(notofications);
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
	 * this method get how much new notification has
	 * and put them in the list
	 * @param table get how much there new notifications in the DB
	 */
	public void return_new_notification(ArrayList<ArrayList<Object>> table) {

		Platform.runLater(new Runnable() {
			public void run() {
				Long count = (Long) table.get(0).get(0);
				if (count == 0) {
					new_notification.setText("Notifications:");
				} else {
					new_notification.setStyle("-fx-font-weight: bold;");
					new_notification.setTextFill(javafx.scene.paint.Color.RED);
					new_notification.setText("You have " + count + " new notifications");
					String quary1 = "UPDATE `marketing_manager_notification` SET `status`= 0 WHERE status=1 ";
					Message msg1 = new Message(MessageType.UPDATEINFO,
							new String("MarketingManagerNotificationController"), quary1);
					MainClientGUI.client.handleMessageFromClientUI(msg1);
				}

			}
		});

	}

}
