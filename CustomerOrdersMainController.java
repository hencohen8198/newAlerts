package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.OrderHomeFuel;
import client.common.SalePattern;

/**
 * This class takes care about the customer home fuel orderes status. It
 * contains methods for search and reomve home fuel orderes from the home fuel
 * orderes table. As well, this controller class won't allow the customer to
 * delete an open order, means the customer won't can to delete order with the
 * undeliverd status If the user doesn't chose any order to serach, the class
 * will inform him. If the class did't find the order search the table will be
 * empty. To present the ordres table again, the user will clean the search text
 * field and press again on the search button. In invalid input,the order search
 * text filed will color in red.
 * 
 * @author Itay
 * @version 0.99
 */
public class CustomerOrdersMainController extends AbstractController {

	public ObservableList<OrderHomeFuel> orders_list = FXCollections.observableArrayList();
	/**
	 * hold the instance of the object
	 */
	public static CustomerOrdersMainController instance;

	@FXML
	private TableView<OrderHomeFuel> orders_table;

	@FXML
	private TableColumn<OrderHomeFuel, Integer> ordernum_col;

	@FXML
	private TableColumn<OrderHomeFuel, String> orderdate_col;

	@FXML
	private TableColumn<OrderHomeFuel, String> supplydate_col;

	@FXML
	private TableColumn<OrderHomeFuel, Float> quantity_col;

	@FXML
	private TableColumn<OrderHomeFuel, String> urgent_col;

	@FXML
	private TableColumn<OrderHomeFuel, Float> price_col;

	@FXML
	private TableColumn<OrderHomeFuel, String> status_col;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notifications_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Button homefuelorder_btn;

	@FXML
	private TextField searchfororder_text;

	@FXML
	private Button search_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	/**
	 * This method will apply after mouse clicked on remove button in the customer
	 * home fuel orderes form. If the user didn't chose any home fuel order to
	 * remove, the system will inform him. If the user chose an home fuel order to
	 * remove in an undeleviered status, the system will inform him(we didn't
	 * allowed the user to remove undeleviered orderes) the method makes a query to
	 * remove the notification from the db by set the message status to 0(the home
	 * fuel orders used for reports) and remove it from the notification table as
	 * well.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {

		if (orders_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Orders table");
			alert.setContentText(String.format("Sorry, please choose an order"));
			alert.showAndWait();
		} else {
			if (!orders_table.getSelectionModel().getSelectedItem().getStatus().equals("Delivered")) {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Orders table");
				alert.setContentText(String.format("Sorry, can not delete an open order"));
				alert.showAndWait();
			} else {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Orders table");
				alert.setContentText(String.format("Remove Order?"));
				Optional<ButtonType> res = alert.showAndWait();
				if (res.get() == ButtonType.CANCEL)
					return;
				OrderHomeFuel ordertoDelete = orders_table.getSelectionModel().getSelectedItem();
				orders_list.removeAll(orders_list);
				Message message = new Message(MessageType.UPDATEINFO, "CustomerOrdersMainController_table",
						"UPDATE order_home_fuel SET order_home_fuel.display=0 WHERE order_home_fuel.homeOrderTag="
								+ ordertoDelete.getOrdernum());
				MainClientGUI.client.handleMessageFromClientUI(message);
				Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
				alert2.getButtonTypes().remove(ButtonType.CANCEL);
				alert2.setTitle("Order remove");
				alert2.setContentText(String.format("The Order has been removed"));
				alert2.initOwner(MainClientGUI.primaryStage.getOwner());
				alert2.showAndWait();
				orders_table.refresh();
			}
		}

	}

	/**
	 * This method will activate after query in the initalize method to make the
	 * whole orders status to deleivered if our day is the same day like the
	 * requsted supply date Moreover, the method will make a query to receive the
	 * home fuel orders from the db Father: initialize,
	 * CustomerOrdersMainController_table after returned updated info sucsseful
	 */
	public void update_orders() {
		Message message = new Message(MessageType.REQUESTINFO, "CustomerOrdersMainController_table",
				"SELECT ohf.homeOrderTag, ohf.userID, ohf.orderDate, ohf.supplyDate, ohf.price, ohf.status, ohf.urgent,ohf.address,ohf.quantity"
						+ " FROM order_home_fuel ohf  WHERE ohf.userID=" + MainClientGUI.getUserID()
						+ " AND ohf.display=1");
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * This method will apply after mouse clicked on help button, and will present
	 * for the user A guide for the specific page, details about the page, and
	 * filling forms insturactions.
	 * 
	 * @param event mouse clicked
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
		dia.setContentText("\nCustomer homefuel table orders:\n"
				+ "\n•In this form you can observe the whole home fuel orders that you have been placed\n"
				+ "Moreover, each urgent order simulated to delivered in the same day. \n•Every un-urgent order will deliver in the exact supply order date"
				+ ".\n•As well, remove notification will apply just if the order has been supplied.\n•For order search, enter the order number and search.");
		dia.show();

	}

	/**
	 * This method will apply after mouse clicked on homefuelorder button, and will
	 * present the user The homefuelorder form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void homefuelorder_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerOrdersHomefuelorderForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will apply after mouse clicked on logout button, and will logout
	 * the user From the system. After the click on this button, the user will
	 * return to the main login form.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "CustomerOrdersMainController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will apply after logout succsess(the trigger for this method
	 * activation was From the message handler client). The method will change the
	 * form to the main form .
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * This method will apply after mouse clicked on homefuelorder button, and will
	 * present the user The homefuelorder form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerMainForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will apply after mouse clicked on notifications button, and will
	 * present the user The notifications form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void notifications_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerNotifications.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will search an order by the order number Moreover, this method
	 * checks the input as well, if the input is unvalid, like a letter in integere
	 * field, It will mark the text field in red. If the class did't find the order
	 * search the table will be empty. To present the ordres table again, the user
	 * will clean the search text field and press again on the search button.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void search_btn_clicked(MouseEvent event) {

		ObservableList<OrderHomeFuel> found_sales = FXCollections.observableArrayList();
		try {
			if (!searchfororder_text.getText().isEmpty()) {
				for (OrderHomeFuel sale : orders_list) {
					Integer search = new Integer(searchfororder_text.getText());
					if (search <= 0) {
						throw new Exception();
					}
					if (sale.getOrdernum().toString().startsWith(search.toString())) {
						found_sales.add(sale);
					}
				}
				orders_table.setItems(FXCollections.observableArrayList());
				if (found_sales != null) {
					orders_table.getItems().addAll(found_sales);
				}
			} else {
				orders_table.setItems(orders_list);
			}
			searchfororder_text.setStyle(null);
		} catch (Exception e) {
			searchfororder_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Input entered is not valid please try again");
			alert.show();
			return;
		}
//		if(!searchfororder_text.getText().isEmpty()) {
//			OrderHomeFuel found_order = null;
//			try {
//				if (!searchfororder_text.getText().isEmpty()) {
//					for (OrderHomeFuel order : tempOrders) {
//						Integer search = new Integer(searchfororder_text.getText());
//						if (search <= 0||searchfororder_text.getText().length()>11) {
//							throw new Exception();
//						}
//						if (order.getOrdernum().equals(search))
//							found_order = order;
//	
//					}
//					orders_table.setItems(FXCollections.observableArrayList());
//					searchfororder_text.setStyle(null);
//					if (found_order != null) {
//						orders_table.getItems().add(found_order);
//					}
//			
//				}
//			}catch (Exception e) {
//				searchfororder_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
//				return;
//			}
//		}
//		else {
//			orders_table.setItems(tempOrders);
//			searchfororder_text.setStyle(null);
//		}

	}

	/**
	 * This method will initlize the values, make a query to load home fuel orders
	 * table. Moreover,the method create a query to update oreders status to
	 * deleivered when the supply day has passed
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		ordernum_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, Integer>("ordernum"));
		orderdate_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, String>("orderDate"));
		supplydate_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, String>("supplyDate"));
		quantity_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, Float>("quantity"));
		urgent_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, String>("urgent"));
		price_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, Float>("price"));
		status_col.setCellValueFactory(new PropertyValueFactory<OrderHomeFuel, String>("status"));
		Message message2 = new Message(MessageType.UPDATEINFO, "CustomerOrdersMainController_table_updatetime",
				"UPDATE order_home_fuel SET order_home_fuel.status=\"Delivered\" WHERE order_home_fuel.supplyDate<="
						+ "\"" + new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "\"");
		MainClientGUI.client.handleMessageFromClientUI(message2);

		Message message = new Message(MessageType.REQUESTINFO, "CustomerOrdersMainController_table",
				"SELECT ohf.homeOrderTag, ohf.userID, ohf.orderDate, ohf.supplyDate, ohf.price, ohf.status, ohf.urgent,ohf.address,ohf.quantity"
						+ " FROM order_home_fuel ohf  WHERE ohf.userID=" + MainClientGUI.getUserID()
						+ " AND ohf.display=1");
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will load the home fuel orders from the db to the table (those
	 * oreders with the 1 display)
	 * 
	 * @param table returned info from the db. specifically the 1 statuses orders
	 *              from db
	 */
	public void tabledetails(ArrayList<ArrayList<Object>> table) {

		if (table != null) {
			for (ArrayList<Object> row : table)
				orders_list.add(0,
						new OrderHomeFuel((Integer) row.get(0), (Integer) row.get(1), (Float) row.get(4),
								(String) row.get(2), (String) row.get(3), (String) row.get(5), (String) row.get(6),
								(String) row.get(7), (Float) row.get(8)));
		}
		orders_table.setItems(orders_list);
	}

}
