package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.FuelType;
import client.common.logic_controllers.UserController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class is the controller of add vehicle screen, used by marketing agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentVehicleAddController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentVehicleAddController instance;
//	public static boolean fl = false;
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
	private Label customer_name_label;

	@FXML
	private ComboBox<String> fuelType_comboBox;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button update_btn;

	@FXML
	private TextField vehicleNumber_txt;

	@FXML
	private Button back_btn;

	@FXML
	private Button main_btn;

	@FXML
	private Button customers_info_btn;

	@FXML
	private Button vehicle_info_btn;

	@FXML
	private Button update_purchase_pattern_btn;

	@FXML
	private Label user_fullname;

	/**
	 * if back button is clicked, alert is pop up and the user back to the main
	 * vehicle info screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentVehicleMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * if customers button is clicked, alert is pop up and the user move to the main
	 * customers info screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * try to switch the scene to customers info, only if customer is selected
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_info_btn_clicked(MouseEvent event) {
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
				"Add Vehicle Screen\nIn this screen marketing agent can add new vehicle to the selected customer\n"
						+ "first enter fuel type and than enter vehicle number 7-8 numbers");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentVehicleAddController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method alert the user before move main customer screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentMenuWelcomeForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * switch the screen to notification form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to sales form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * this method checks if the input is valid, that check if vehicle exist
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_btn_clicked(MouseEvent event) {
		if (correctInputNubmers(vehicleNumber_txt, 78))
			checkIfVehicleExist(vehicleNumber_txt);
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please Enter between 7-8 numbers");
			alert.setTitle("ERROR");
			alert.setHeaderText(null);
			alert.show();
			return;
		}
	}

	/**
	 * switch the screen to update purchase pattern form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_purchase_pattern_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentCustomerPurchasePatternForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the screen to vehicle info form after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void vehicle_info_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentVehicleMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * initialize the screen, set the user name at the top of the page, and
	 * initialize the fuels type at the combo box
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		if (UserController.getCustomer() != null) {
			customer_name_label.setText(UserController.getCustomer().getCustomerName());
		} else {
			customer_name_label.setText("");
		}
		ObservableList<String> options = FXCollections.observableArrayList("Motor", "Benzin", "Soler");
		fuelType_comboBox.setItems(options);
	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * method for alert before leave page with important information that could be
	 * lost
	 * 
	 * @param form, the location of the form file
	 * @param css,  the location of the CSS file
	 */
	public void exitFromScreenAlert(String form, String css) {
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
		alert.setTitle("Back");
		alert.setHeaderText(null);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			switchScenes(form, css);
		} else {
			return;
		}
	}

	/**
	 * this method is for the correct numbers input
	 * 
	 * @param txt,   the text field where we get the text
	 * @param label, the number of the numbers of digits
	 * @return true if the input is in the valid format, else false
	 */
	public boolean correctInputNubmers(TextField txt, int label) {
		try {
			Integer temp = new Integer(txt.getText());
			if (!txt.getText().isEmpty()) {
				if (temp <= 0)
					throw new Exception();
			}
			// 7-8 digits
			if (label == 78) {
				if (txt.getText().length() < 7 || txt.getText().length() > 8) {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			// colored the text field with the wrong input
			txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			return false;
		}
		txt.setStyle(null);
		return true;
	}

	/**
	 * this method make query to choose if the requested car is already exist
	 * 
	 * @param txt,get the text field of the vehicle number
	 */
	public void checkIfVehicleExist(TextField txt) {
		String vehicle_query = "select vehicles.vehicleNumber from vehicles where vehicles.vehicleNumber ="
				+ txt.getText();
		Message message_vehicle = new Message(MessageType.REQUESTINFO,
				"MarketingAgentVehicleAddController_check_if_vehicle_exist", vehicle_query);
		MainClientGUI.client.handleMessageFromClientUI(message_vehicle);
	}

	/**
	 * this method get the requested car from DB, if customer vehicle is null the
	 * system can add the car
	 * 
	 * @param customer_vehicle, get if the car is already registered before
	 */
	public void getVehicleNumber(ArrayList<ArrayList<Object>> customer_vehicle) {
		if (customer_vehicle == null) {
			if (correctInputNubmers(vehicleNumber_txt, 78)
					&& fuelType_comboBox.getSelectionModel().getSelectedItem() != null) {
				// query for adding the car to vehicles table at DB
				String vehicle_query = "INSERT INTO vehicles(vehicleNumber, fuelType, userID) VALUES ("
						+ vehicleNumber_txt.getText() + ", "
						+ FuelType.getFuelType(fuelType_comboBox.getSelectionModel().getSelectedItem()) + ", "
						+ UserController.getCustomer().getUserID() + ")";
				Message message_vehicle = new Message(MessageType.UPDATEINFO,
						"MarketingAgentVehicleAddController_add_vehicle", vehicle_query);
				MainClientGUI.client.handleMessageFromClientUI(message_vehicle);
				// refresh
				switchScenes("/client/boundry/MarketingAgentVehicleMainForm.fxml",
						"/client/boundry/MarketingAgentMainCustomer.css");
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Wrong Input");
				alert.setHeaderText(null);
				alert.setContentText("Please enter vaild number");
				alert.show();
				return;
			}
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Vehicle already exists");
					alert.setHeaderText(null);
					alert.setContentText("The Vehicle is already a registered");
					alert.show();
					return;
				}
			});

		}
	}

	/**
	 * this method make query to check if the system need to update the subscription
	 * of the customer (single car to some cars)
	 */
	public void checkAmountOfCars() {
		String query = "select customer_rates.rateType, (select count(vehicles.vehicleNumber) from vehicles where vehicles.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ ") from customer_rates where customer_rates.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
		Message message = new Message(MessageType.REQUESTINFO, "MarketingAgentVehicleAddController_get_vehicle_rate",
				query);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * this method can change the subscription if the conditions is true
	 * 
	 * @param rate_add_vehicle hold the subscription type and the amount of cars for that customer
	 */
	public void changeRateVehicle(ArrayList<ArrayList<Object>> rate_add_vehicle) {
		int rate = (int) rate_add_vehicle.get(0).get(0);
		Long vehicles = (Long) rate_add_vehicle.get(0).get(1);
		if (rate == 2 && vehicles > 1) {
			// update the DB with the relevant subscription
			String query1 = "Update customer_rates Set customer_rates.rateType = 3 where customer_rates.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message1 = new Message(MessageType.UPDATEINFO, "MarketingAgentVehicleMainController_update_rate",
					query1);
			MainClientGUI.client.handleMessageFromClientUI(message1);
		}
	}
}
