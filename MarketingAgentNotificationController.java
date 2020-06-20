package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.NotificationMakretingAgent;
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
 * This class is the controller of the notification screen, shows relevant
 * notification to marketing agent like analytic system got new information
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentNotificationController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentNotificationController instance;
//	public static boolean fl = false;
	
	  //Observable list for the list of the notification
	 
	private ObservableList<NotificationMakretingAgent> notification_list = FXCollections.observableArrayList();

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private TableView<NotificationMakretingAgent> notification_table;

	@FXML
	private TableColumn<NotificationMakretingAgent, String> titleColumn;

	@FXML
	private TableColumn<NotificationMakretingAgent, String> dateColumn;

	@FXML
	private TableColumn<NotificationMakretingAgent, String> descriptionColumn;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button delete_btn;

	/**
	 * switch the screen to main customer form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * this method delete the selected notification from the table and list, after
	 * accept at the alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		if (notification_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle("ERROR");
			alert3.setHeaderText(null);
			alert3.setContentText("Please select a notification to delete");
			alert3.show();
			return;
		}
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you delete the notification?", ButtonType.YES,
				ButtonType.NO);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText(null);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			String query1 = "Delete from M_A_Notification where M_A_Notification.notificationID = "
					+ notification_table.getSelectionModel().getSelectedItem().getNotificationID();
			Message message1 = new Message(MessageType.UPDATEINFO,
					"MarketingAgentNotificationController_delete_notification", query1);
			MainClientGUI.client.handleMessageFromClientUI(message1);
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle("Notification customer");
			alert1.setHeaderText(null);
			alert1.setContentText("Your notification successfully deleted");
			alert1.show();
			switchScenes("/client/boundry/MarketingAgentNotificationMainForm.fxml",
					"/client/boundry/MarketingAgentMainCustomer.css");
		} else {
			return;
		}
	}

	/**
	 * specific help button for marketing agent with general information
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
		dia.setContentText("Notification Screen\nIn this screen you can see the marketing agent's notifcation\n"
				+ "for example: update by the analytic system");
		dia.show();
	}

	/**
	 * when logout button clicked, the system send a query to logout and switch to
	 * login scene
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentNotificationController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMenuWelcomeForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {

	}

	/**
	 * switch the screen to main sales form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * initialize the screen, set the user name at the top of the page, and load the
	 * table with the last notifications
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		titleColumn.setCellValueFactory(new PropertyValueFactory<NotificationMakretingAgent, String>("name"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<NotificationMakretingAgent, String>("date"));
		descriptionColumn
				.setCellValueFactory(new PropertyValueFactory<NotificationMakretingAgent, String>("description"));
		// query for get the notifications from DB
		String query = "SELECT m_a_n.*" + " FROM M_A_Notification m_a_n WHERE m_a_n.userID = "
				+ MainClientGUI.getUserID();
		Message message = new Message(MessageType.REQUESTINFO, "MarketingAgentNotificationController_initialize_table",
				query);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * get the notification list from DB and set them at observable list and to the
	 * table
	 * 
	 * @param notificationList, get the list from DB and set them at the table
	 */
	public void getNotificationList(ArrayList<ArrayList<Object>> notificationList) {
		for (ArrayList<Object> row : notificationList) {
			notification_list.add(new NotificationMakretingAgent((String) row.get(0), (String) row.get(1),
					(Integer) row.get(2), (String) row.get(3), (Integer) row.get(4)));
		}
		notification_table.setItems(notification_list);
	}

}
