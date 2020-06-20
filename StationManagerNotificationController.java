package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.FuelType;
import client.common.SupplieOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
 * this class is the controller of the notification screen of the station
 * manager. here will be displayed messages of the station manager
 *
 * @author henco
 * @version 0.99
 */
public class StationManagerNotificationController extends AbstractController {

	/**
	 * instance of this controller
	 */
	public static StationManagerNotificationController instance;
	/**
	 * The order that has been selected from the notification table
	 */
	public SupplieOrder selected_order;
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
	private Button reject_btn;

	@FXML
	private Button accept_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private TableView<SupplieOrder> notifications_table;

	@FXML
	private TableColumn<SupplieOrder, String> topic_clm;

	@FXML
	private TableColumn<SupplieOrder, String> status_clm;

	@FXML
	private TableColumn<SupplieOrder, String> message_clm;

	@FXML
	private TableColumn<SupplieOrder, String> date_clm;

	/**
	 * this method is responsible for accepting an order. its job is to update all
	 * of the relevent tables in the sql database, and to remove the row from the
	 * table.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void accept_btn_clicked(ActionEvent event) {

		selected_order = notifications_table.getSelectionModel().getSelectedItem();
		if (selected_order != null) {
			if (!selected_order.getStatus().equals("Waiting")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: CANT ACCEPT THIS NOTIFICATION");
				alert.setHeaderText(null);
				alert.setContentText("The Notifiaction Selected Cant Be Accepted");
				alert.show();
			} else {
				Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
				alert2.getButtonTypes().remove(ButtonType.OK);
				alert2.getButtonTypes().add(ButtonType.CANCEL);
				alert2.getButtonTypes().add(ButtonType.YES);
				alert2.setTitle("Accept Order");
				alert2.setContentText(String.format("Are You Sure You Want To Accept The Order?"));
				alert2.initOwner(MainClientGUI.primaryStage.getOwner());
				Optional<ButtonType> res2 = alert2.showAndWait();

				if (res2.isPresent()) {
					if (res2.get().equals(ButtonType.CANCEL)) {
						event.consume();
					} else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Accepted Successfully");
						alert.setHeaderText(null);
						alert.setContentText("The Notifiaction Has Been Accepted");

						String quary = "UPDATE supplie_order SET supplie_order.status = \"Accepted\" WHERE supplie_order.orderTag = "
								+ selected_order.getOrderID();
						Message message = new Message(MessageType.UPDATEINFO,
								"StationManagerNotificationRejectController_accept_btn_clicked", quary);
						MainClientGUI.client.handleMessageFromClientUI(message);

						quary = "DELETE FROM supplie_order WHERE supplie_order.status = \"Waiting\" AND supplie_order.stationTag = "
								+ station_tag_number + " AND supplie_order.fuelType = "
								+ FuelType.getFuelType(selected_order.getFuelType());
						Message message3 = new Message(MessageType.UPDATEINFO,
								"StationManagerNotificationRejectController_accept_btn_clicked_delete_other_orders",
								quary);
						MainClientGUI.client.handleMessageFromClientUI(message3);

						String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss")
								.format(Calendar.getInstance().getTime());
						quary = "INSERT INTO supplier_orders_list(date,gasStation,quantity,status,fuelType,eid) VALUES (\""
								+ timeStamp + "\"," + station_tag_number + "," + selected_order.getQuantity()
								+ ",\"New\",\"" + selected_order.getFuelType() + "\"," + selected_order.getSupplierEID()
								+ ")";
						Message message2 = new Message(MessageType.UPDATEINFO,
								"StationManagerNotificationRejectController_accept_btn_clicked", quary);
						MainClientGUI.client.handleMessageFromClientUI(message2);

						Optional<ButtonType> res = alert.showAndWait();
						if (res.isPresent()) {
							switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
									"/client/boundry/StationManagerNotification.css");
						}
					}
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SELECT RELEVENT NOTIFICATION");
			alert.setHeaderText(null);
			alert.setContentText("No Notification Has Been Selected\nPlease Select A Notification Before Accepting");
			alert.show();
		}

	}

	/**
	 * this method is handling the case of an order being deleted
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		selected_order = notifications_table.getSelectionModel().getSelectedItem();
		if (selected_order != null) {
			if (selected_order.getStatus().equals("Waiting")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: CANT DELETE THIS NOTIFICATION");
				alert.setHeaderText(null);
				alert.setContentText("The Notifiaction Selected Cant Be Deleted Before Being Rejected");
				alert.show();
			} else {
				Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
				alert2.getButtonTypes().remove(ButtonType.OK);
				alert2.getButtonTypes().add(ButtonType.CANCEL);
				alert2.getButtonTypes().add(ButtonType.YES);
				alert2.setTitle("Delete Notification");
				alert2.setContentText(String.format("Are You Sure You Want To Delete This Message?"));
				alert2.initOwner(MainClientGUI.primaryStage.getOwner());
				Optional<ButtonType> res2 = alert2.showAndWait();

				if (res2.isPresent()) {
					if (res2.get().equals(ButtonType.CANCEL)) {
						event.consume();
					} else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Deleted Successfully");
						alert.setHeaderText(null);
						alert.setContentText("The Notifiaction Has Been Deleted");
						String quary = "Delete FROM supplie_order WHERE supplie_order.orderTag = "
								+ selected_order.getOrderID();
						Message message = new Message(MessageType.UPDATEINFO,
								"StationManagerNotificationRejectController_reject_btn_clicked", quary);
						MainClientGUI.client.handleMessageFromClientUI(message);
						Optional<ButtonType> res = alert.showAndWait();
						if (res.isPresent()) {
							switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
									"/client/boundry/StationManagerNotification.css");
						}
					}
				}
			}
			// UPDATE supplie_order SET display_notification = 1 WHERE
			// supplie_order.orderTag = 1; }
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SELECT RELEVENT NOTIFICATION");
			alert.setHeaderText(null);
			alert.setContentText("No Notification Has Been Selected\nPlease Select A Notification Before Deleting");
			alert.show();
		}
	}

	/**
	 * this method is handling the case of an order being rejected it checks that
	 * the order can be rejected, and then switches to the reject screen with more
	 * info
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void reject_btn_clicked(MouseEvent event) {
		selected_order = notifications_table.getSelectionModel().getSelectedItem();
		if (selected_order != null) {
			if (selected_order.getStatus().equals("Waiting")) {
				switchScenes("/client/boundry/StationManagerNotificationRejectForm.fxml",
						"/client/boundry/StationManagerNotification.css");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR: CANT REJECT THIS NOTIFICATION");
				alert.setHeaderText(null);
				alert.setContentText("The Notifiaction Selected Cant Be Rejected");
				alert.show();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SELECT RELEVENT NOTIFICATION");
			alert.setHeaderText(null);
			alert.setContentText("No Notification Has Been Selected\nPlease Select A Notification Before Rejecting");
			alert.show();
		}
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
		dia.setContentText("\nHere is the main notification menu.\nYou can approve "
				+ "requests the refill low reserves by the suppliers suggested, "
				+ "or you can reject them all, and then new orders will be made");
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
		Message message = new Message(MessageType.LOGOUT, "StationManagerNotificationController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method is responsible for handling a click in the manu button
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

	/**
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {

	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the correct screen
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
	 * this method is activated when the server has successfully logged out the
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
		selected_order = null;
//		if (fl) {
//			hide_manu();
//		}
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		topic_clm.setCellValueFactory(new PropertyValueFactory<SupplieOrder, String>("topic"));
		message_clm.setCellValueFactory(new PropertyValueFactory<SupplieOrder, String>("comment"));
		status_clm.setCellValueFactory(new PropertyValueFactory<SupplieOrder, String>("status"));
		date_clm.setCellValueFactory(new PropertyValueFactory<SupplieOrder, String>("date"));
		String quary = "SELECT employees.firstName,employees.lastName,fuels.fuelName,supplie_order.status,supplie_order.quantity,supplie_order.date,supplie_order.orderTag,supplie_order.supplier_id FROM employees,supplie_order,fuels WHERE supplie_order.stationTag = "
				+ StationManagerNotificationController.station_tag_number
				+ " AND employees.eid = supplie_order.supplier_id AND fuels.fuelType = supplie_order.fuelType AND supplie_order.display_notification = 1";
		Message message = new Message(MessageType.REQUESTINFO, "StationManagerNotificationController_initialize",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method is responsible for filling up the table of the orders. its
	 * resives the orders from the server
	 * 
	 * @param orders orders waiting for the station manager from the database
	 *
	 */
	public void return_SupplierOrders(ArrayList<ArrayList<Object>> orders) {
		ObservableList<SupplieOrder> ordersList = FXCollections.observableArrayList();

		for (ArrayList<Object> row : orders) {
			ordersList.add(new SupplieOrder((String) row.get(2), (String) row.get(5),
					((String) row.get(0)) + " " + ((String) row.get(1)), (String) row.get(3), (Float) row.get(4),
					(Integer) row.get(6),(Integer) row.get(7)));

		}
		notifications_table.setItems(ordersList);
		// TableResizer.autoFitTable(notifications_table);
	}

}
