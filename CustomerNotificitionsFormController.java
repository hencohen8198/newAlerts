package client.common.controllers;

import javafx.scene.control.Dialog;

import javafx.scene.control.DialogPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import message_info.Message;
import message_info.MessageType;
import client.MainClientGUI;
import client.common.CustomerNotifications;

/**
 * This class takes care about the Customer Notifications. It contains methods
 * for insert details to the notificatoins table, and methods to remove them.
 * Moreover, there are methods for inserting data to the notifications table. As
 * well, this controller class will suggest the customer system sales by his
 * purchse pattern: if he doesn't has any purchase pattern he will recieve any
 * sales options by his car details(fuel type),from each station. the second
 * option,by his purchase pattern, he will recive sales options by his company
 * registeration
 * 
 * @author Itay
 * @version 0.99
 */

public class CustomerNotificitionsFormController extends AbstractController {

	public ObservableList<CustomerNotifications> notification_list;
	private Integer customerid, cntNotifications;
	/**
	 * hold the instance of the object
	 */
	public static CustomerNotificitionsFormController instance;

	@FXML
	private TableView<CustomerNotifications> orders_table;

	@FXML
	private TableColumn<CustomerNotifications, Integer> notifynumber_col;

	@FXML
	private TableColumn<CustomerNotifications, String> topic_col;

	@FXML
	private TableColumn<CustomerNotifications, String> date_col;

	@FXML
	private TableColumn<CustomerNotifications, String> status_col;

	@FXML
	private Button menu_btn;

	@FXML
	private Button orders_btn;

	@FXML
	private Button remove_btn;

	@FXML
	private Label notify_label;

	@FXML
	private Button open_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Label notify_notifications_label;

	@FXML
	private Label notify_sales_label;

	/**
	 * This method will apply after mouse clicked on help button, and will present
	 * for the user A guide for the specific page, details about the page, and
	 * filling forms insturactions.
	 * 
	 * @param event mouse clicked
	 * 
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
		stage.getIcons().add(new Image(this.getClass().getResource("/icons8-help-24.png").toString()));
		dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		dia.setContentText("\n•Notifications customer form:\n"
				+ "\n•In this form you can watch your last receipts(after any purchase that you made).\n"
				+ "•Moreover, you will recieve sales options from the system by your purchase pattern type.");
		dia.show();

	}

	/**
	 * This method will apply after mouse clicked on logout button, and will logout
	 * the user From the system. After the click on this button, the user will
	 * return to the main login form.
	 * 
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {

		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "CustomerNotificitionsFormController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will apply after logout succsess(the trigger for this method
	 * activation was From the message handler client). The method will change the
	 * form to the main form .
	 * 
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * This method will apply after mouse clicked on menu button, and will present
	 * the user The menu form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerMainForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will apply after mouse clicked on orders button, and will present
	 * the user The orders form in the system.
	 *
	 * @param event
	 * 
	 */
	@FXML
	void orders_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerOrdersMainForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will apply after mouse clicked on remove button in the
	 * notifications table form. If the user didn't chose any notificatoins to
	 * remove, the system will inform him. If the user chose a system sale to
	 * remove, the system will inform him(we didn't allowed the user to remove sales
	 * options) the method makes a query to remove the notification from the db, and
	 * remove it from the notification table as well.
	 * 
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void remove_btn_clicked(MouseEvent event) {
		StringBuilder description = new StringBuilder();
		if (orders_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Notifications table");
			alert.setContentText(String.format("Sorry, please choose notification"));
			alert.initOwner(MainClientGUI.primaryStage);
			alert.show();
			return;
		} else {
			if (orders_table.getSelectionModel().getSelectedItem().getNotifynumber().equals("Sale Tag")) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Orders table");
				alert.setContentText(String.format("Sorry, you can not delete System sale"));
				alert.initOwner(MainClientGUI.primaryStage);
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Notifications table");
				alert.setContentText(String.format("Remove notification?"));
				Optional<ButtonType> res = alert.showAndWait();
				if (res.get() == ButtonType.CANCEL)
					return;
				if (orders_table.getSelectionModel().getSelectedItem().getStatusNew().equals("New "))// Itay-fixed line
																										// to not sub
																										// the
																										// notifications
																										// cnt if the
																										// notiftation
																										// was not new
					cntNotifications--;
				if (cntNotifications == 0) {
					description.append("You don't have any new notifications");
					notify_notifications_label.setTextFill(Color.BLACK);
					notify_notifications_label.setText(description.toString());
				} else {
					description.append("You have ");
					description.append(cntNotifications);
					description.append(" new notifications");
					notify_notifications_label.setText(description.toString());

				}
				CustomerNotifications customernotification = orders_table.getSelectionModel().getSelectedItem();
				notification_list.remove(orders_table.getSelectionModel().getSelectedItem());
				Message message = new Message(MessageType.UPDATEINFO,
						"CustomerNotificitionsFormController_deleteRowinTable",
						"DELETE customer_notifications FROM customer_notifications WHERE customer_notifications.notifynumber="
								+ customernotification.getNotifynumber());
				MainClientGUI.client.handleMessageFromClientUI(message);

				Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
				alert2.getButtonTypes().remove(ButtonType.CANCEL);
				alert2.setTitle("Notificatoin remove");
				alert2.setContentText(String.format("The notification has been removed"));
				alert2.initOwner(MainClientGUI.primaryStage.getOwner());
				alert2.show();
				orders_table.refresh();

			}
		}

	}

	/**
	 * This method will apply after mouse clicked on open button in the
	 * notifications table form. If the user didn't chose any notificatoin to open,
	 * the system will inform him. The method makes a query to update the
	 * notification to not new in the db and updateing it in the notification table
	 * as opened Notification. Moreover,the mathod will update the new notifications
	 * label by the exact new notifications count. If, the new notifications count
	 * worth to 0, the method will change the label to: you don't have any new
	 * messages.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void open_btn_clicked(MouseEvent event) {

		StringBuilder description = new StringBuilder();
		Dialog<ButtonType> dia = new Dialog<>();
		Stage stage = (Stage) dia.getDialogPane().getScene().getWindow();
		DialogPane dialogPane = dia.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/client/boundry/dialog.css").toExternalForm());
		dialogPane.getStyleClass().add("dialog");
		dia.setGraphic(new ImageView(this.getClass().getResource("/icons8-sale-64.png").toString()));
		stage.getIcons().add(new Image(this.getClass().getResource("/icons8-notification-64.png").toString()));
		dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		if (orders_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Notifications table");
			alert.setContentText(String.format("Sorry, please choose notification to open"));
			alert.showAndWait();
		} else {
			if (!orders_table.getSelectionModel().getSelectedItem().getNotifynumber().equals("Sale Tag")) {

				Message message2 = new Message(MessageType.UPDATEINFO,
						"CustomerNotificitionsFormController_updateNewOrder",
						"UPDATE customer_notifications SET statusNew=0" + " WHERE customer_notifications.customerID="
								+ customerid + " AND customer_notifications.notifynumber="
								+ orders_table.getSelectionModel().getSelectedItem().getNotifynumber());
				MainClientGUI.client.handleMessageFromClientUI(message2);
				if (cntNotifications == 0) {
					description.append("You don't have any new notifications");
					notify_notifications_label.setTextFill(Color.BLACK);
				} else {
					if (orders_table.getSelectionModel().getSelectedItem().getStatusNew().equals("New "))
						cntNotifications--;
					if (cntNotifications == 0) {
						description.append("You don't have any new notifications");
						notify_notifications_label.setTextFill(Color.BLACK);
					} else {
						description.append("You have ");
						description.append(cntNotifications);
						description.append(" new notifications");

					}

				}
				notify_notifications_label.setText(description.toString());
				dia.setGraphic(
						new ImageView(this.getClass().getResource("/icons8-transaction-list-64.png").toString()));
			}
			dia.setHeaderText(orders_table.getSelectionModel().getSelectedItem().getTopic());
			dia.setTitle(orders_table.getSelectionModel().getSelectedItem().getTopic());
			dia.setContentText(orders_table.getSelectionModel().getSelectedItem().getDescription());
			Optional<ButtonType> res = dia.showAndWait();
			if (res.isPresent())
				if (res.get().equals(ButtonType.OK))
					if (!orders_table.getSelectionModel().getSelectedItem().getNotifynumber().equals("Sale Tag")) {
						orders_table.getSelectionModel().getSelectedItem().setStatusNew(0);
						orders_table.refresh();
					}

		}

	}//

	/**
	 * This method will initlize the values, notifications table, and make a query
	 * to receive the customer id
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		notification_list = FXCollections.observableArrayList();
		notifynumber_col.setCellValueFactory(new PropertyValueFactory<CustomerNotifications, Integer>("notifynumber"));
		topic_col.setCellValueFactory(new PropertyValueFactory<CustomerNotifications, String>("topic"));
		date_col.setCellValueFactory(new PropertyValueFactory<CustomerNotifications, String>("date"));
		status_col.setCellValueFactory(new PropertyValueFactory<CustomerNotifications, String>("statusNew"));
		Message message = new Message(MessageType.REQUESTINFO, "CustomerNotificitionsFormController_reciveCustomerID",
				"SELECT customers.ID FROM customers WHERE customers.userID=" + MainClientGUI.getUserID());
		MainClientGUI.client.handleMessageFromClientUI(message);
		loadSales();

	}

	/**
	 * This method will load sales for the customer by his vehicle details and his
	 * purchase pattern Moreover this method will make a query and the returned
	 * receivecustomersales() method Will check the customer exclusiveiness.
	 */
	public void loadSales() {
		String query = new String(
				"SELECT vehicles.vehicleNumber,sale_pattern.startHour,sale_pattern.endHour,sale_pattern.discount,stations.location,stations.stationName,sale_pattern.name,sale_pattern.description,purchase_patten.exclusive"
						+ " FROM sale_pattern,vehicles,stations,purchase_patten WHERE vehicles.userID="
						+ MainClientGUI.getUserID()
						+ " AND vehicles.fuelType=sale_pattern.fuelType AND sale_pattern.status=\"active\" AND "
						+ "sale_pattern.stationTag= stations.stationTag AND purchase_patten.userID="
						+ MainClientGUI.getUserID());
		Message message = new Message(MessageType.REQUESTINFO,
				"CustomerNotificitionsFormController_reciveSalesForCustomer", query);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method receive the customerid and updating the customerid global
	 * variable. Father: initalize
	 * 
	 * @param customerid2 returned info from db
	 */
	public void recivecustomerid(ArrayList<ArrayList<Object>> customerid2) {
		customerid = (Integer) customerid2.get(0).get(0);

	}

	/**
	 * This method will insert new sales for customers in customers notifications
	 * table The method will check if the customer is exclusive. if not, The system
	 * will notify all sales from the all stations by his vehicle fueltype and
	 * active sales. In the sale notification the customer will see details about
	 * the sale, such as, hours, discount, specific vehicle number etc. If the
	 * customer is an exclusive customer by his purchase pattern,the method will
	 * establish a query to notify the customer by appropraite gas stations If the
	 * customer doesn't has any sales, the method will change the label to: you
	 * don't have any new sales The method will inform the customer by his new sales
	 * via new sales counter, each sale that was added to the table, the counter
	 * will updating Father: loadSales()
	 * 
	 * @param recivesalesforcustomer returned info about the all activated sales in
	 *                               every stations
	 */
	public void recivecustomersales(ArrayList<ArrayList<Object>> recivesalesforcustomer) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Integer cntSales = 0;
				StringBuilder description = new StringBuilder();
				StringBuilder time = new StringBuilder();
				if (recivesalesforcustomer != null) {
					if (((Integer) recivesalesforcustomer.get(0).get(8)) == 0) {
						for (ArrayList<Object> row : recivesalesforcustomer) {
							description.append("New ");
							description.append((String) row.get(6) + " Sale!! at ");
							description.append((String) row.get(5) + ", " + (String) row.get(4));
							description.append("\n\n");
							description.append("You will have a ");
							description.append((Float) row.get(3) + "% discount for your ");
							description.append((Integer) row.get(0) + " Vehicle!!");
							description.append("\n\n");
							description.append("Sale Hours: ");
							time.append((String) row.get(1));
							time.insert(2, ':');
							description.append(time.toString() + " to ");
							time.delete(0, time.length());
							time.append((String) row.get(2));
							time.insert(2, ':');
							description.append(time.toString());
							description.append("\n\n");
							description.append("More details: ");
							description.append((String) row.get(7));

							CustomerNotifications row2 = new CustomerNotifications(customerid, (-1),
									"Sale advertisement No." + (cntSales + 1),
									new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()),
									description.toString(), 1);
							notification_list.add(0, row2);
							description.delete(0, description.length());
							time.delete(0, time.length());
							cntSales += 1;
						}

						description.append("You have ");
						description.append(cntSales);
						description.append(" sales advertisementes");
						notify_sales_label.setTextFill(Color.RED);
					} else {
						String query = new String(
								"SELECT vehicles.vehicleNumber,sale_pattern.startHour,sale_pattern.endHour,sale_pattern.discount,stations.location,stations.stationName,sale_pattern.name,sale_pattern.description"
										+ " FROM sale_pattern,vehicles,stations,purchase_patten WHERE vehicles.userID="
										+ MainClientGUI.getUserID()
										+ " AND vehicles.fuelType=sale_pattern.fuelType AND sale_pattern.status=\"active\" AND "
										+ "sale_pattern.stationTag=stations.stationTag AND (stations.companyTag=purchase_patten.firstCompanyTag OR "
										+ "stations.companyTag=purchase_patten.secondCompanyTag OR stations.companyTag= purchase_patten.thirdCompanyTag) AND purchase_patten.userID="
										+ MainClientGUI.getUserID());
						Message message = new Message(MessageType.REQUESTINFO,
								"CustomerNotificitionsFormController_reciveAppropiateSalesForCustomer", query);
						MainClientGUI.client.handleMessageFromClientUI(message);

					}
				} else
					description.append("You don't have any sales options");

				notify_sales_label.setText(description.toString());

				Message message2 = new Message(MessageType.REQUESTINFO, "CustomerNotificitionsFormController_table",
						"SELECT cn.customerID,cn.topic,cn.date,cn.description,cn.notifynumber,cn.statusNew"
								+ " FROM customer_notifications cn  WHERE cn.customerID=" + customerid);
				MainClientGUI.client.handleMessageFromClientUI(message2);

			}
		});

	}

	/**
	 * This method will insert new notifications for customers in customers
	 * notifications table The method will count each notification that was added to
	 * the table, And inform it to the customer as a new notifications(depend on the
	 * notification status) If there are not any new notifications(by the status)
	 * the method will notify the customer by label: you don't have any new messages
	 * Father: recivecustomersales()
	 * 
	 * @param notificationtable returned info about the all customer notifications
	 */
	public void recivenotificationtable(ArrayList<ArrayList<Object>> notificationtable) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				StringBuilder description = new StringBuilder();
				cntNotifications = 0;
				if (notificationtable != null) {
					for (ArrayList<Object> row : notificationtable) {
						notification_list.add(0, new CustomerNotifications((Integer) row.get(0), (Integer) row.get(4),
								(String) row.get(1), (String) row.get(2), (String) row.get(3), (Integer) row.get(5)));
						if ((Integer) row.get(5) == 1)
							cntNotifications += 1;
					}
					if (cntNotifications == 0)
						description.append("You don't have any new notifications");
					else {
						description.append("You have ");
						description.append(cntNotifications);
						description.append(" new notifications");
						notify_notifications_label.setTextFill(Color.RED);

					}

				} else
					description.append("You don't have any new notifications");

				orders_table.setItems(notification_list);
				notify_notifications_label.setText(description.toString());

			}
		});

	}

	/**
	 * This method will insert new sales for customers in customers notifications
	 * table The method will count each sales that was added to the table, And
	 * inform it to the customer as a new sales(by new sales count) The method will
	 * insert the sales that appropraite specificaliy to the customer by his vehicle
	 * fuel type and his purchase pattern If there are not any sales(returned null)
	 * the method will notify the customer by label: you don't have any new sales
	 * Father: recivecustomersales()
	 * 
	 * @param reciveappropiatesalesforCustomer returned info from db by appropiate
	 *                                         customer's data
	 */
	public void reciveAppropiateSalesForCustomer(ArrayList<ArrayList<Object>> reciveappropiatesalesforCustomer) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Integer cntSales = 0;
				StringBuilder description = new StringBuilder();
				StringBuilder time = new StringBuilder();
				if (reciveappropiatesalesforCustomer != null) {
					for (ArrayList<Object> row : reciveappropiatesalesforCustomer) {
						description.append("New ");
						description.append((String) row.get(6) + " Sale!! at ");
						description.append((String) row.get(5) + ", " + (String) row.get(4));
						description.append("\n\n");
						description.append("You will have a ");
						description.append((Float) row.get(3) + "% discount for your ");
						description.append((Integer) row.get(0) + " Vehicle!!");
						description.append("\n\n");
						description.append("Sale Hours: ");
						time.append((String) row.get(1));
						time.insert(2, ':');
						description.append(time.toString() + " to ");
						time.delete(0, time.length());
						time.append((String) row.get(2));
						time.insert(2, ':');
						description.append(time.toString());
						description.append("\n\n");
						description.append("More details: ");
						description.append((String) row.get(7));

						CustomerNotifications row2 = new CustomerNotifications(customerid, (-1),
								"Sale advertisement No." + (cntSales + 1),
								new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()),
								description.toString(), 1);
						notification_list.add(0, row2);
						description.delete(0, description.length());
						time.delete(0, time.length());
						cntSales += 1;
					}

					description.append("You have ");
					description.append(cntSales);
					description.append(" sales advertisementes");
					notify_sales_label.setTextFill(Color.RED);

				} else
					description.append("You don't have any sales options");

				notify_sales_label.setText(description.toString());

			}
		});

	}

}
