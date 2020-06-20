package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.Customer;
import client.common.SalePattern;
import client.common.logic_controllers.UserController;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;
import server.common.controllers.MainServerController;

/**
 * This class is the controller of the actions on customers, the customers shows
 * by list in table, used by marketing agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentCustomersMainController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentCustomersMainController instance;
//	public static boolean fl = false;

	public Customer customer_selected;

	// Observable list for the list of the customers

	ObservableList<Customer> customers_list = FXCollections.observableArrayList();
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
	private Button help_btn;

	@FXML
	private TableView<Customer> main_table;

	@FXML
	private TableColumn<Customer, String> customerTypeColumn;

	@FXML
	private TableColumn<Customer, String> fullNameCulomn;

	@FXML
	private TableColumn<Customer, Integer> IDColumn;

	@FXML
	private TableColumn<Customer, String> emailColumn;

	@FXML
	private TableColumn<Customer, Integer> CC4DigitsColumn;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button main_add_btn;

	@FXML
	private Button main_delete_btn;

	@FXML
	private Button main_refresh_btn;

	@FXML
	private Button edit_btn;

	@FXML
	private Button main_btn;

	@FXML
	private Button customer_info_btn;

	@FXML
	private Button vehicle_info_btn;

	@FXML
	private Button update_purchase_pattern_btn;

	@FXML
	private Button search_btn;

	@FXML
	private TextField search_txt;

	@FXML
	private Label customer_name_label;

	@FXML
	private Label user_fullname;

	/**
	 * this method is for switch scene to customer info screen but only if customer
	 * is selected, else show alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customer_info_btn_clicked(MouseEvent event) {

		if (UserController.getCustomer() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose customer");
			alert.setHeaderText(null);
			alert.setContentText("Please select customer");
			alert.show();
			return;
		}

		switchScenes("/client/boundry/MarketingAgentCustomerEditForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {

	}

	/**
	 * if customer is selected, switch scene to edit form else, alert the user
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void edit_btn_clicked(MouseEvent event) {
		if (UserController.getCustomer() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose customer");
			alert.setHeaderText(null);
			alert.setContentText("Please select customer");
			alert.show();
			return;
		}
		switchScenes("/client/boundry/MarketingAgentCustomerEditForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
		Platform.runLater(new Runnable() {

			// send true for edit flag

			@Override
			public void run() {
				MarketingAgentCustomerEditController.instance.switchFunctionality(true);
			}
		});
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
		dia.setContentText(
				"Main customer Screen\nIn this screen marketing agent can choose customer from table by click,"
						+ " and make action on him like: edit, delete, and view only by click on customer info\n"
						+ "to add vehicle/ update purchase pattern please choose customer first");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentCustomerMainController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * switch the scene to customer add form when "Add" is clicked
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_add_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentCustomerAddForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {
		
	}

	/**
	 * if customer is selected, alert the user "Are you sure" and than delete the
	 * customer else, alert "customer is not selected"
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_delete_btn_clicked(MouseEvent event) {
		if (UserController.getCustomer() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose customer");
			alert.setHeaderText(null);
			alert.setContentText("Please select customer");
			alert.show();
			return;
		}
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to delete the customer?", ButtonType.YES,
				ButtonType.NO);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText(null);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			// query for delete from customers table at DB
			String query1 = "Delete from customers where customers.userID = "
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from users table at DB
			String query2 = "Delete from users where users.userID = "
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from credit info table at DB
			String query3 = "Delete from credit_info where credit_info.userID = "
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from vehicles table at DB
			String query4 = "Delete from vehicles where vehicles.userID ="
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from analytic info table at DB
			String query5 = "Delete from analytic_info where analytic_info.customerID ="
					+ main_table.getSelectionModel().getSelectedItem().getId();
			// query for delete from purchase pattern table at DB
			String query6 = "Delete from purchase_patten where purchase_patten.userID ="
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from users table at DB
			String query7 = "Delete from customer_rates where customer_rates.userID ="
					+ main_table.getSelectionModel().getSelectedItem().getUserID();
			// query for delete from notification table at DB
			String query8 = "Delete from customer_notifications where customer_notifications.customerID ="
					+ main_table.getSelectionModel().getSelectedItem().getId();
			// make update message for each query and parent
			Message message1 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query1);
			Message message2 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query2);
			Message message3 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query3);
			Message message4 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query4);
			Message message5 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query5);
			Message message6 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query6);
			Message message7 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query7);
			Message message8 = new Message(MessageType.UPDATEINFO, "MarketingAgentCustomersMainController_delete",
					query8);
			// send all the message
			MainClientGUI.client.handleMessageFromClientUI(message1);
			MainClientGUI.client.handleMessageFromClientUI(message2);
			MainClientGUI.client.handleMessageFromClientUI(message3);
			MainClientGUI.client.handleMessageFromClientUI(message4);
			MainClientGUI.client.handleMessageFromClientUI(message5);
			MainClientGUI.client.handleMessageFromClientUI(message6);
			MainClientGUI.client.handleMessageFromClientUI(message7);
			MainClientGUI.client.handleMessageFromClientUI(message8);

			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Delete customer");
			alert2.setHeaderText(null);
			alert2.setContentText("Your customer successfully deleted");
			alert2.show();
			switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
					"/client/boundry/MarketingAgentMainCustomer.css");
		} else {
			return;
		}
	}

	/**
	 * refresh the screen by switch the screen to the same page
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_refresh_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	@FXML
	void main_select_btn_clicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			customer_selected = main_table.getSelectionModel().getSelectedItem();
			set_selected_customer(customer_selected);
		}

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
	 * switch the screen to notification screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to sales screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * method for search at the table
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void search_btn_clicked(MouseEvent event) {
		customer_name_label.setText("");
		ObservableList<Customer> found_customer = FXCollections.observableArrayList();
		try {
			if (!search_txt.getText().isEmpty()) {
				for (Customer customer : customers_list) {
					Integer search = new Integer(search_txt.getText());
					if (search <= 0) {
						throw new Exception();
					}
					if (customer.getId().toString().startsWith(search.toString())) {
						found_customer.add(customer);
					}
				}
				main_table.setItems(FXCollections.observableArrayList());
				if (found_customer != null) {
					main_table.getItems().addAll(found_customer);
				}
			} else {
				main_table.setItems(customers_list);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Input entered is not valid please try again");
			alert.show();
			return;
		}
	}

	/**
	 * alert the user to choose a customer first before switch to purchase pattern
	 * screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_purchase_pattern_btn_clicked(MouseEvent event) {
		if (UserController.getCustomer() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose customer");
			alert.setHeaderText(null);
			alert.setContentText("Please select customer");
			alert.show();
			return;
		}
		switchScenes("/client/boundry/MarketingAgentCustomerPurchasePatternForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to vehicle info screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void vehicle_info_btn_clicked(MouseEvent event) {
		if (UserController.getCustomer() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose customer");
			alert.setHeaderText(null);
			alert.setContentText("Please select customer");
			alert.show();
			return;
		}
		switchScenes("/client/boundry/MarketingAgentVehicleMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;

		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * initialize the screen, set the user name at the top of the page, than load
	 * the details to table of all customers
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		customer_selected = null;
		set_selected_customer(customer_selected);
		// set in table
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		customerTypeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerType"));
		fullNameCulomn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
		IDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
		CC4DigitsColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("credit"));
		// query for get the private customers
		String query1 = "select customers.firstName, customers.lastName,customers.ID, customers.email, customers.credit,customers.userID, credit_info.expMonth , credit_info.expYear, credit_info.cvv"
				+ " from customers , credit_info"
				+ " where customers.userID = credit_info.userID and customers.customerType=1";
		Message message1 = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomersMainController_initialize_private", query1);
		MainClientGUI.client.handleMessageFromClientUI(message1);

	}

	/**
	 * get the private customers and add to list and send query for the company
	 * customers
	 * 
	 * @param private_customers, get from DB the private customers
	 */
	public void fill_table_private(ArrayList<ArrayList<Object>> private_customers) {
		if (private_customers != null) {
			for (ArrayList<Object> row : private_customers) {
				customers_list.add(new Customer((String) row.get(0), (String) row.get(1), (Integer) row.get(2),
						(String) row.get(3), (Long) row.get(4), (Integer) row.get(5), (Integer) row.get(6),
						(Integer) row.get(7), (Integer) row.get(8)));
			}
		}
		// make query to get company customers
		String query2 = "select customers.companyName,customers.ID, customers.email, customers.credit,customers.userID, credit_info.expMonth , credit_info.expYear, credit_info.cvv"
				+ " from customers, credit_info"
				+ " where customers.userID = credit_info.userID and customers.customerType=0";
		Message message2 = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomersMainController_initialize_company", query2);
		MainClientGUI.client.handleMessageFromClientUI(message2);
//		main_table.setItems(private_customers_list);
	}

	/**
	 * get the company customers and add to the list, than set to table
	 * 
	 * @param company_customers, get from DB the company customers
	 */
	public void fill_table_company(ArrayList<ArrayList<Object>> company_customers) {
		if (company_customers != null) {
			for (ArrayList<Object> row : company_customers) {
				customers_list.add(new Customer((String) row.get(0), (Integer) row.get(1), (String) row.get(2),
						(Long) row.get(3), (Integer) row.get(4), (Integer) row.get(5), (Integer) row.get(6),
						(Integer) row.get(7)));
			}
		}
		main_table.setItems(customers_list);
	}

	/**
	 * this method is set the chosen customer to do action
	 * 
	 * @param customer, get the chosen customer
	 */
	public void set_selected_customer(Customer customer) {
		UserController.setCustomer(customer);
		if (customer != null) {
			customer_name_label.setText(customer.getCustomerName());
			customer_name_label.setVisible(true);
		}
	}
}
