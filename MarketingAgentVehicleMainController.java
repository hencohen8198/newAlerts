package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.Vehicle;
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
 * This class is the controller of main vehicle info screen, used by marketing
 * agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentVehicleMainController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentVehicleMainController instance;
//	public static boolean fl = false;
	// observable list for vehicle list to put in table
	ObservableList<Vehicle> vehicle_list = FXCollections.observableArrayList();
	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private TableView<Vehicle> vehicleTable;

	@FXML
	private TableColumn<Vehicle, Integer> vehicleNumberColumn;

	@FXML
	private TableColumn<Vehicle, String> fuelTypeColumn;

	@FXML
	private Button add_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Label customer_name_label;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button main_btn;

	@FXML
	private Button customer_info_btn;

	@FXML
	private Button vehicle_info_btn;

	@FXML
	private Button update_purchase_pattern_btn;

	@FXML
	private Label user_fullname;

	/**
	 * switch the screen to add vehicle form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void add_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentVehicleAddForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * switch the screen to customer info form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customer_info_btn_clicked(MouseEvent event) {
		if (MarketingAgentCustomersMainController.instance.customer_selected == null) {
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
	 * switch the screen to customer info form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * this method delete vehicle from DB, if car is selected else alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		if (vehicleTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose vehicle");
			alert.setHeaderText(null);
			alert.setContentText("Please select vehicle");
			alert.show();
			return;
		}
		// query for delete specific vehicle from DB
		String query1 = "Delete from vehicles where vehicles.userID = "
				+ vehicleTable.getSelectionModel().getSelectedItem().getUserID() + " and vehicles.vehicleNumber = "
				+ vehicleTable.getSelectionModel().getSelectedItem().getVehicleNumber();
		Message message1 = new Message(MessageType.UPDATEINFO, "MarketingAgentVehicleMainController_delete_vehicle",
				query1);
		MainClientGUI.client.handleMessageFromClientUI(message1);
		// successful alert
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete vehicle");
		alert.setHeaderText(null);
		alert.setContentText("The vehicle successfully deleted");
		alert.show();
		// refresh
		switchScenes("/client/boundry/MarketingAgentVehicleMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
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
		dia.setContentText("Main Vehicle Screen\nIn this screen you can see all vehicles of the selected customer\n"
				+ "add/delete by click on specific vehicle first");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentVehicleMainController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method alert the user before move main customer screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
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
	 * switch the screen to notification form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to sales form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to update purchase pattern form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_purchase_pattern_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentCustomerPurchasePatternForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to vehicle info form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void vehicle_info_btn_clicked(MouseEvent event) {
		// switchScenes("/client/boundry/MarketingAgentVehicleMainForm.fxml",
		// "/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * initialize the screen, set the user name at the top of the page, and
	 * initialize the vehicles list at the table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		if (MarketingAgentCustomersMainController.instance.customer_selected != null) {
			customer_name_label
					.setText(MarketingAgentCustomersMainController.instance.customer_selected.getCustomerName());
		}
//		} else {
//			customer_name_label.setText("");
//		}
		// set the vehicles info at the table
		vehicleNumberColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("vehicleNumber"));
		fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("fuelType"));
		// query for get all vehicles of the customer
		String query = "select vehicles.userID, vehicles.vehicleNumber , fuels.fuelName" + " from vehicles, fuels"
				+ " where " + MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ " = vehicles.userID and vehicles.fuelType = fuels.fuelType";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingAgentVehicleMainController_initialize_table",
				query);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * get the vehicles from DB, add to observable list and than set to table
	 * 
	 * @param vehicle_customer, get the vehicle list from DB of the specific
	 *                          customer
	 */
	public void fill_table_vehicles(ArrayList<ArrayList<Object>> vehicle_customer) {
		for (ArrayList<Object> row : vehicle_customer) {
			vehicle_list.add(new Vehicle((Integer) row.get(0), (String) row.get(2), (Integer) row.get(1)));
		}
		vehicleTable.setItems(vehicle_list);
	}

	/**
	 * this method make a query for count the cars amount
	 */
	public void checkAmountOfCars() {
		String query = "select customer_rates.rateType, (select count(vehicles.vehicleNumber) from vehicles where vehicles.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ ") from customer_rates where customer_rates.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
		Message message = new Message(MessageType.REQUESTINFO, "MarketingAgentVehicleMainController_get_vehicle_rate",
				query);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method get the vehicles number and check if the system need to update
	 * the subscription for the customer
	 * 
	 * @param rate_vehicle, parameter for checking if we need to update the rate of
	 *                      the customer
	 */
	public void changeRateVehicle(ArrayList<ArrayList<Object>> rate_vehicle) {
		int rate = (int) rate_vehicle.get(0).get(0);
		Long vehicles = (Long) rate_vehicle.get(0).get(1);

		// if the rate is *some car* and the user have only 1 car if true update the
		// subscription and DB

		if (rate == 3 && vehicles == 1) {
			String query1 = "Update customer_rates Set customer_rates.rateType = 2 where customer_rates.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message1 = new Message(MessageType.UPDATEINFO, "MarketingAgentVehicleMainController_update_rate",
					query1);
			MainClientGUI.client.handleMessageFromClientUI(message1);
		}
	}

}
