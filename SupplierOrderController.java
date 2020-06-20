package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.AcceptedOrders;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * 
 * This class is in charge of supplier screen. In the screen displayed table of
 * all the supplier's orders. The supplier can delete order from table, confirm
 * order and receive manager information.
 * 
 * @author yarin
 * @version 0.99
 * 
 * 
 */
public class SupplierOrderController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static SupplierOrderController instance;

	public ObservableList<AcceptedOrders> order_sup = FXCollections.observableArrayList();

	@FXML
	private Button menu_btn;

	@FXML
	private Button orders_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Button select_btn;

	@FXML
	private Button remove_btn;

	@FXML
	private Button manager_btn;

	@FXML
	private Button update_btn;

	@FXML
	private TableView<AcceptedOrders> order_table;

	@FXML
	private TableColumn<AcceptedOrders, String> date_column;

	@FXML
	private TableColumn<AcceptedOrders, Integer> order_tag_column;

	@FXML
	private TableColumn<AcceptedOrders, Integer> gas_station_column;

	@FXML
	private TableColumn<AcceptedOrders, Float> quantity_column;

	@FXML
	private TableColumn<AcceptedOrders, String> fuelType_column;

	@FXML
	private TableColumn<AcceptedOrders, String> status_column;

	@FXML
	private Label notify_label;

	@FXML
	private Label full_name_lable;

	@FXML
	private TextField search_lable;

	/**
	 * this function handles the event of muse clicked on help button. When this
	 * button clicked,dialog display on the screen with help information.
	 * 
	 * @param event The event that caused the method to activate.
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
		dia.setContentText("Supplier Orders Screen:\nIn this screen supplier will be able to watch all of his orders.\n"
				+ "In addition supplier can delete orders with status = complete.\n\n" + "Button's:\n\n"
				+ "Delete: remove order from table.\n\n"
				+ "Confirm: change staus of order from new/waiting to complete.\n\n"
				+ "Manamger Information: displat manager personal information.");

		dia.show();
	}

	/**
	 * This function handles the event of muse clicked on search button. The method
	 * will filter the orders presented
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void search_btn_clicked(MouseEvent event) {

		ObservableList<AcceptedOrders> found_sales = FXCollections.observableArrayList();
		try {
			if (!search_lable.getText().isEmpty()) {
				for (AcceptedOrders order : order_sup) {
					Integer search = new Integer(search_lable.getText());
					if (search <= 0) {
						throw new Exception();
					}
					if (order.getOrderTag().toString().startsWith(search.toString())) {
						found_sales.add(order);
					}
				}
				order_table.setItems(FXCollections.observableArrayList());
				if (found_sales != null) {
					order_table.getItems().addAll(found_sales);
				}
			} else {
				order_table.setItems(order_sup);
			}
			search_lable.setStyle(null);
		} catch (Exception e) {
			search_lable.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Input entered is not valid please try again");
			alert.show();
			return;
		}
	}

	/**
	 * This method in charge of log out the specific client from the system. Create
	 * appropriate query and sent it to the D.B
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String query = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "SupplierOrderController_logout_clicked", query);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * This method in charge of create query to D.B for receiving manager
	 * information Create appropriate query and sent it to the D.B
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void manager_btn_clicked(MouseEvent event) {

		// if supplier select item in table
		if (order_table.getSelectionModel().getSelectedItem() != null) {
			String query = "SELECT emp.firstName, emp.lastName, emp.email FROM stations st, employees emp WHERE st.eid = emp.eid AND st.stationTag = "
					+ order_table.getSelectionModel().getSelectedItem().getGasStation();
			Message message = new Message(MessageType.REQUESTINFO, "SupplierOrderController_manager_btn_clicked",
					query);
			MainClientGUI.client.handleMessageFromClientUI(message);
		}

		// supplier did not select any row
		else {
			Alert alert = new Alert(AlertType.ERROR);
			// alert.setTitle("Error Dialog");
			alert.setHeaderText("Selection Error");
			alert.setContentText("Sorry, no order detected");
			alert.showAndWait();
		}
	}

	/**
	 * This method in charge of return to menu screen.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/SupplierWelcomeForm.fxml", "/client/boundry/SupplierWelcomeCss.css");
	}

	/**
	 * This method active when orders button is clicked
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void orders_btn_clicked(MouseEvent event) {

	}

	/**
	 * This method active when remove button is clicked. In case of one order
	 * selected and than pressed remove the row deleted from the table and from D.B
	 * .
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void remove_btn_clicked(MouseEvent event) {

		// if supplier select item in table
		if (order_table.getSelectionModel().getSelectedItem() != null) {

			// if the order status is complete create query for delete
			if (order_table.getSelectionModel().getSelectedItem().getStatus().toString().equals("Complete")) {

				// show alert before execute deleting
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete");
				alert.setHeaderText("If you click ok the order will delte");
				alert.setContentText("Are you sure?");

				Optional<ButtonType> result = alert.showAndWait();

				// delete executing
				if (result.get() == ButtonType.OK) {
					String quary = "DELETE FROM supplier_orders_list WHERE orderTag = "
							+ order_table.getSelectionModel().getSelectedItem().getOrderTag();
					Message message = new Message(MessageType.UPDATEINFO, "SupplierOrderController_remove_btn_clicked",
							quary);
					MainClientGUI.client.handleMessageFromClientUI(message);

					// refresh for table after delete
					switchScenes("/client/boundry/SupplierOrdersForm.fxml", "/client/boundry/SupplierWelcomeCss.css");
				}

				// supplier regret delete and press cancel
				else {
					return;
				}
			}

			// order is not complete, can not delete
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Check status");
				alert.setContentText("Sorry, unable to delete incomplete order");
				alert.showAndWait();
			}

		}

		// supplier did not select any row
		else {
			Alert alert = new Alert(AlertType.ERROR);
			// alert.setTitle("Error Dialog");
			alert.setHeaderText("Selection Error");
			alert.setContentText("Sorry, no order detected");
			alert.showAndWait();
		}
	}

	/**
	 * This method active when select button is clicked
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void select_btn_clicked(MouseEvent event) {

	}

	/**
	 * This method active when confirm button is clicked. When row in table selected
	 * and confirm pressed, her status is changed from new/waiting to complete.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_btn_clicked(MouseEvent event) {

		// if supplier select item in table
		if (order_table.getSelectionModel().getSelectedItem() != null) {

			// if the order status is Waiting than possible to change to Complete
			if (order_table.getSelectionModel().getSelectedItem().getStatus().toString().equals("Waiting")
					|| order_table.getSelectionModel().getSelectedItem().getStatus().toString().equals("New")) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Status");
				alert.setContentText("Are you sure want to change status to complete?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					String query = "UPDATE supplier_orders_list SET status = 'Complete' WHERE orderTag = "
							+ order_table.getSelectionModel().getSelectedItem().getOrderTag();
					Message message = new Message(MessageType.UPDATEINFO, "update_btn_clicked", query);
					MainClientGUI.client.handleMessageFromClientUI(message);

					// ******update fuel reserves******//

					// receive the fuel number by the name of the fuel

					String query2 = "SELECT fuels.fuelType FROM fuels where fuels.fuelName =\""
							+ order_table.getSelectionModel().getSelectedItem().getFuelType() + "\";";
					Message message2 = new Message(MessageType.REQUESTINFO, "SupplierOrderController_receive_fuel_type",
							query2);
					MainClientGUI.client.handleMessageFromClientUI(message2);

					// refresh for table after update

				} else {
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setHeaderText("Confirmation Error");
					alert2.setContentText("Updating the order has failed");
					alert2.showAndWait();
				}

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				// alert.setTitle("Error Dialog");
				alert.setHeaderText("Status Error");
				alert.setContentText("Sorry, unable to complete this order");
				alert.showAndWait();
			}
		}
		// supplier does not choose any row
		else {
			Alert alert = new Alert(AlertType.ERROR);
			// alert.setTitle("Error Dialog");
			alert.setHeaderText("Selection Error");
			alert.setContentText("Sorry, no order detected");
			alert.showAndWait();
		}

	}

	/**
	 * This method happen immediately when the relevant FXML open. Initialize all
	 * the table column. Set supplier name create query for total counter of orders
	 * create query for total counter of new orders
	 * 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		instance = this;
		MainClientGUI.new_notifications = false;
		date_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, String>("date"));
		order_tag_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, Integer>("orderTag"));
		gas_station_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, Integer>("gasStation"));
		quantity_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, Float>("quantity"));
		fuelType_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, String>("fuelType"));
		status_column.setCellValueFactory(new PropertyValueFactory<AcceptedOrders, String>("status"));
		full_name_lable.setText("Hello, " + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());

		String query = ("SELECT supplier_orders_list.* FROM supplier_orders_list,employees WHERE employees.userID = "
				+ MainClientGUI.getUserID() + " AND supplier_orders_list.eid = employees.eid;");
		Message msg = new Message(MessageType.REQUESTINFO, "SupplierOrderController_initialize", query);
		MainClientGUI.client.handleMessageFromClientUI(msg);

		// check for new orders

		String query2 = "SELECT count(supplier_orders_list.status) FROM supplier_orders_list,employees where status='New' AND employees.userID = "
				+ MainClientGUI.getUserID() + " AND supplier_orders_list.eid = employees.eid";
		Message msg2 = new Message(MessageType.REQUESTINFO, "SupplierOrderController_initialize_New_counter", query2);
		MainClientGUI.client.handleMessageFromClientUI(msg2);

	}

	/**
	 * This method active when log out succeed from D.B, and than change the
	 * variable of log in to false. Finally open log in screen to reconnect.
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/SupplierWelcomeCss.css");
	}

	/**
	 * This method activated when the result from query return. The result is all
	 * the orders information. After organized the data in matrix, display the
	 * information in table.
	 * 
	 * @param supplier_orders is a matrix that holds all the order information
	 */
	public void return_order(ArrayList<ArrayList<Object>> supplier_orders) {

		Integer eid = 0;
		for (ArrayList<Object> row : supplier_orders) {
			eid = (Integer) row.get(6);
			order_sup.add(new AcceptedOrders((String) row.get(0), (Integer) row.get(1), (Integer) row.get(2),
					(Float) row.get(3), (String) row.get(5), (String) row.get(4)));
		}
		order_table.setItems(order_sup);

		if (eid != 0) {
			String query3 = "update supplier_orders_list set status='Waiting' where status='New' AND eid = " + eid;
			Message msg3 = new Message(MessageType.UPDATEINFO, "SupplierOrderController_initialize_New_counter",
					query3);
			MainClientGUI.client.handleMessageFromClientUI(msg3);
		}
	}

	/**
	 * This method activated when the result from query return. The result is all
	 * the manager information. Insert the data into alert note.
	 * 
	 * @param manager_info holds the manager information in matrix
	 */
	public void return_manager_info(ArrayList<ArrayList<Object>> manager_info) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				// manager details from return sql
				String first = (String) manager_info.get(0).get(0).toString();
				String last = (String) manager_info.get(0).get(1).toString();
				String email = (String) manager_info.get(0).get(2).toString();

				Dialog<String> dia = new Dialog<>();
				Stage stage = (Stage) dia.getDialogPane().getScene().getWindow();
				DialogPane dialogPane = dia.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("/client/boundry/dialog.css").toExternalForm());
				dialogPane.getStyleClass().add("dialog");
				dia.setTitle("Station Manager Info");
				dia.setHeaderText("Guide:");
				dia.setGraphic(new ImageView(this.getClass().getResource("/icons8-business-report-48.png").toString()));
				// Add a custom icon.
				stage.getIcons()
						.add(new Image(this.getClass().getResource("/icons8-business-report-24.png").toString()));
				dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
				dia.setContentText(
						"Manager First Name: " + first + "\n\nManager Last Name: " + last + "\n\nEmail: " + email);

				dia.show();

			}
		});

	}

	/**
	 * * This method activated when the result from query return. The result is
	 * counter of new orders. Display the red text with the counter of new orders.
	 * in case there is no New order display static text instead.
	 * 
	 * @param new_order holds the counter of orders with status= NEW
	 */
	public void return_new_order(ArrayList<ArrayList<Object>> new_order) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				String count = (String) new_order.get(0).get(0).toString();
				if (count.equals("0")) {
					notify_label.setText("Supplier orders list");
				} else {
					notify_label.setStyle("-fx-font-weight: bold;");
					notify_label.setTextFill(Color.RED);
					notify_label.setText("You have " + count + " new orders");
				}
			}
		});
	}

	/**
	 * This method activated when the result from query return. The result is the
	 * fuel type by his number. After receive fuel type and quantity create
	 * appropriate query for update fuel inventory.
	 * 
	 * @param fuel_type is the number that Describes the fuel type
	 */
	public void return_fuel_type(ArrayList<ArrayList<Object>> fuel_type) {

		Integer fuel_num = (Integer) fuel_type.get(0).get(0); // fuel number that receive from server
		Integer gas_station = order_table.getSelectionModel().getSelectedItem().getGasStation(); // get gas station
																									// number

		// update fuel_reserves
		String query = "update fuel_reserves set fuel_reserves.fuelInventory = fuel_reserves.fuelInventory +"
				+ order_table.getSelectionModel().getSelectedItem().getQuantity() + " where fuel_reserves.fuelType ="
				+ fuel_num + " and stationTag =" + gas_station;

		Message msg = new Message(MessageType.UPDATEINFO, "SupplierOrderController_update_fuel_reserves", query);
		MainClientGUI.client.handleMessageFromClientUI(msg);
		switchScenes("/client/boundry/SupplierOrdersForm.fxml", "/client/boundry/SupplierWelcomeCss.css");

	}

	/**
	 * This method activated when the result from query return. The result is the
	 * order that was searched. after organized the order information, display the
	 * only order in the table.
	 * 
	 * @param specific_order is the the specific order that searched by order ID
	 */
	public void return_specific_order(ArrayList<ArrayList<Object>> specific_order) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (specific_order == null) {
					search_lable.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
					return;
				} else {
					for (ArrayList<Object> row : specific_order) {
						order_sup.add(new AcceptedOrders((String) row.get(0), (Integer) row.get(1),
								(Integer) row.get(2), (Float) row.get(3), (String) row.get(5), (String) row.get(4)));
					}
					order_table.setItems(order_sup);

				}

			}
		});
	}

}
