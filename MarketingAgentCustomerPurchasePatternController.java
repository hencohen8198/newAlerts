package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.logic_controllers.UserController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class is the controller of changing the subscription of the customer and
 * choose exclusive mode and the relevant gas stations used by the marketing
 * agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentCustomerPurchasePatternController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentCustomerPurchasePatternController instance;
//	public static boolean fl = false;

	// hold the stations numbers for update

	public int company1, company2, company3;

	// count up to 3 companies

	public int count;
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
	private CheckBox sonol_checkBox;

	@FXML
	private CheckBox pas_checkBox;

	@FXML
	private CheckBox dorAlon_checkBox;

	@FXML
	private CheckBox delek_checkBox;

	@FXML
	private CheckBox ten_checkBox;

	@FXML
	private CheckBox nrg_checkBox;

	@FXML
	private ComboBox<String> subscription_comboBox;

	@FXML
	private VBox menu_parent;

	@FXML
	private RadioButton exclusive_radioBtn;

	@FXML
	private Button update_btn;

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
	 * this method is for switch scene to customer info screen but only if customer
	 * is selected, else show alert
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
	 * alert before leave this page to main customer screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentMainCustomerForm.fxml",
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
				"Update Purchase Pattern Screen\nIn this screen you can choose specific subscription and companies to fuel\n"
						+ "please select exclsive or not, if yes, choose up to 3 companies");
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
		Message message = new Message(MessageType.LOGOUT,
				"MarketingAgentCustomerPurchasePatternController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * alert before leave this page to main customer screen
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
	 * alert before leave this page to notification screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * alert before leave this page to sales screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * method for update the purchase pattern, first check if exclusive is clicked,
	 * if clicked the user can choose the specific gas stations, else not exclusive
	 * mode and can fuel at each of the stations. at the second part the user can
	 * choose for subscription for the customer by the combo box with the options
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_btn_clicked(MouseEvent event) {
		if (exclusive_radioBtn.isSelected() && count == 0) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Exclusive mode");
					alert.setHeaderText(null);
					alert.setContentText("Please select 1-3 stations when exclusive mode is selected");
					alert.showAndWait();

				}
			});
			return;
		}

		// make query for updating the exclusive field at the DB

		if (exclusive_radioBtn.isSelected()) {
			String query1 = "UPDATE purchase_patten SET purchase_patten.exclusive=1 WHERE purchase_patten.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message_subscription = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerPurchasePatternController_exclusive", query1);
			MainClientGUI.client.handleMessageFromClientUI(message_subscription);

			// relevant query for the amount of the companies the user choosed, by the
			// parameter count

			String query_companies = "";
			if (count == 0) {
				query_companies = "UPDATE purchase_patten SET purchase_patten.firstCompanyTag= 0"
						+ ",purchase_patten.secondCompanyTag = 0, purchase_patten.thirdCompanyTag = 0"
						+ ", purchase_patten.companyCount= " + count + " WHERE purchase_patten.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			}
			if (count == 1) {
				query_companies = "UPDATE purchase_patten SET purchase_patten.firstCompanyTag= " + company1
						+ ",purchase_patten.secondCompanyTag = 0, purchase_patten.thirdCompanyTag = 0"
						+ ", purchase_patten.companyCount= " + count + " WHERE purchase_patten.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			}
			if (count == 2) {
				query_companies = "UPDATE purchase_patten SET purchase_patten.firstCompanyTag= " + company1
						+ ",purchase_patten.secondCompanyTag = " + company2
						+ ",purchase_patten.thirdCompanyTag = 0, purchase_patten.companyCount = " + count
						+ " WHERE purchase_patten.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			}
			if (count == 3) {
				query_companies = "UPDATE purchase_patten SET purchase_patten.firstCompanyTag= " + company1
						+ ",purchase_patten.secondCompanyTag = " + company2 + ", purchase_patten.thirdCompanyTag = "
						+ company3 + ",purchase_patten.companyCount= " + count + " WHERE purchase_patten.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			}

			// update the query for the DB

			Message message_company = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerPurchasePatternController_add_companies", query_companies);
			MainClientGUI.client.handleMessageFromClientUI(message_company);

			// get rate type of subscription
			String query = "select rates.rateType, (select count(vehicles.vehicleNumber) from vehicles where vehicles.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
					+ ") from rates where rates.rateName = \""
					+ subscription_comboBox.getSelectionModel().getSelectedItem().toString() + "\"";
			Message message = new Message(MessageType.REQUESTINFO,
					"MarketingAgentCustomerPurchasePatternController_get_subscription", query);
			MainClientGUI.client.handleMessageFromClientUI(message);
		} else {
			String query1 = "UPDATE purchase_patten SET purchase_patten.exclusive=0 WHERE purchase_patten.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message_subscription = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerPurchasePatternController_exclusive", query1);
			MainClientGUI.client.handleMessageFromClientUI(message_subscription);

			// get rate type of subscription
			String query = "select rates.rateType, (select count(vehicles.vehicleNumber) from vehicles where vehicles.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
					+ ") from rates where rates.rateName = \""
					+ subscription_comboBox.getSelectionModel().getSelectedItem().toString() + "\"";
			Message message = new Message(MessageType.REQUESTINFO,
					"MarketingAgentCustomerPurchasePatternController_get_subscription", query);
			MainClientGUI.client.handleMessageFromClientUI(message);
		}
	}

	@FXML
	void update_purchase_pattern_btn_clicked(MouseEvent event) {
	}

	/**
	 * alert the user to choose a customer first before switch to vehicle info
	 * screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void vehicle_info_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentVehicleMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * initialize the screen, set the user name at the top of the page, first checks
	 * if customer is selected, else update the relevant subscription and gas
	 * stations
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		if (MarketingAgentCustomersMainController.instance.customer_selected != null) {
			customer_name_label
					.setText(MarketingAgentCustomersMainController.instance.customer_selected.getCustomerName());
		} else {
			customer_name_label.setText("");
		}

		// initialize the combo box of the subscriptions types

		ObservableList<String> options = FXCollections.observableArrayList("Casual fuel",
				"Routine monthly fuel - single car", "Routine monthly fuel - some cars",
				"Full monthly fuel - single car");
		subscription_comboBox.setItems(options);

		// initialize the rate of the specific customer

		String query = "select rates.rateName from rates, customer_rates where "
				+ UserController.getCustomer().getUserID()
				+ " = customer_rates.userID and rates.rateType = customer_rates.rateType";
		Message message = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomerPurchasePatternController_initialize_rate", query);
		MainClientGUI.client.handleMessageFromClientUI(message);

		// event handler for count up to 3 methods

		EventHandler<ActionEvent> action = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CheckBox source;
				if (!gasStationCheck()) {
					source = (CheckBox) event.getSource();
					source.setSelected(false);
				}
			}
		};

		// the relevant gas companies

		sonol_checkBox.setOnAction(action);
		pas_checkBox.setOnAction(action);
		dorAlon_checkBox.setOnAction(action);
		delek_checkBox.setOnAction(action);
		ten_checkBox.setOnAction(action);
		nrg_checkBox.setOnAction(action);

		// event handler for click on exclusive radio button

		exclusive_radioBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (exclusive_radioBtn.isSelected()) {
					sonol_checkBox.setSelected(false);
					sonol_checkBox.setDisable(false);
					pas_checkBox.setSelected(false);
					pas_checkBox.setDisable(false);
					dorAlon_checkBox.setSelected(false);
					dorAlon_checkBox.setDisable(false);
					delek_checkBox.setSelected(false);
					delek_checkBox.setDisable(false);
					ten_checkBox.setSelected(false);
					ten_checkBox.setDisable(false);
					nrg_checkBox.setSelected(false);
					nrg_checkBox.setDisable(false);
				} else {
					sonol_checkBox.setSelected(true);
					sonol_checkBox.setDisable(true);
					pas_checkBox.setSelected(true);
					pas_checkBox.setDisable(true);
					dorAlon_checkBox.setSelected(true);
					dorAlon_checkBox.setDisable(true);
					delek_checkBox.setSelected(true);
					delek_checkBox.setDisable(true);
					ten_checkBox.setSelected(true);
					ten_checkBox.setDisable(true);
					nrg_checkBox.setSelected(true);
					nrg_checkBox.setDisable(true);
				}
			}

		});

		// query for get the subscription for initialize

		String query_get_selected = "select rates.rateName from rates, customer_rates where customer_rates.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ " and rates.rateType = customer_rates.rateType";
		Message message_get_selected = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomerPurchasePatternController_get_subs", query_get_selected);
		MainClientGUI.client.handleMessageFromClientUI(message_get_selected);
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
	 * get the subscription of the customer and set it at the combo box
	 * 
	 * @param customer_subscription, returned from the DB the subscription of the
	 *                               customer
	 */
	public void getCustomerSubscription(ArrayList<ArrayList<Object>> customer_subscription) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				subscription_comboBox.setValue((String) customer_subscription.get(0).get(0));
			}
		});
	}

	/**
	 * method for check which gas stations is needed to be select
	 * 
	 * @return false if count is more than 3 gas station, else true
	 */
	public boolean gasStationCheck() {
		company1 = 0;
		company2 = 0;
		company3 = 0;
		count = 0;
		boolean fl = true;
		if (sonol_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 1;
			} else if (company2 == 0) {
				company2 = 1;
			} else {
				company3 = 1;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (pas_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 4;
			} else if (company2 == 0) {
				company2 = 4;
			} else {
				company3 = 4;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (dorAlon_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 2;
			} else if (company2 == 0) {
				company2 = 2;
			} else {
				company3 = 2;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (delek_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 5;
			} else if (company2 == 0) {
				company2 = 5;
			} else {
				company3 = 5;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (ten_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 3;
			} else if (company2 == 0) {
				company2 = 3;
			} else {
				company3 = 3;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (nrg_checkBox.isSelected()) {
			count++;
			if (company1 == 0) {
				company1 = 6;
			} else if (company2 == 0) {
				company2 = 6;
			} else {
				company3 = 6;
			}
			if (count > 3) {
				fl = false;
			}
		}
		if (!fl) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Too many station");
			alert.setHeaderText(null);
			alert.setContentText("Please select 0-3 stations");
			alert.showAndWait();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * method for valid input by the number of cars and the type of the subscription
	 * 
	 * @param subscription_number, get the type of the subscription from DB for
	 *                             valid input
	 */
	public void return_subscription(ArrayList<ArrayList<Object>> subscription_number) {
		int rate = (int) subscription_number.get(0).get(0);
		Long count_vehicle = (Long) subscription_number.get(0).get(1);

		// check for single car subscription and more the 1 car

		if ((rate == 2 || rate == 4) && count_vehicle > 1) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText(null);
					alert.setContentText("The customer has more than 1 vehicle");
					alert.showAndWait();
					return;
				}
			});
		} else if (count_vehicle == 0) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText(null);
					alert.setContentText("The customer doesn't has vehicle");
					alert.showAndWait();

				}

			});
			return;

			// check for some cars subscription and only 1 car

		} else if (rate == 3 && count_vehicle == 1) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText(null);
					alert.setContentText("The customer has 1 vehicle only");
					alert.showAndWait();
					return;
				}
			});
		} else

		{

			// query for add the selected companies

			String query1 = "UPDATE customer_rates SET customer_rates.rateType= " + subscription_number.get(0).get(0)
					+ " WHERE customer_rates.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message_subscription = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerPurchasePatternController_add_companies", query1);
			MainClientGUI.client.handleMessageFromClientUI(message_subscription);

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("UPDATE");
					alert.setHeaderText(null);
					alert.setContentText("The purchase pattern successfully updated");
					alert.showAndWait();
					return;

				}
			});
		}
	}

	/**
	 * set the subscription
	 * 
	 * @param subscription, get the relevant subscription
	 */
	public void return_subs(ArrayList<ArrayList<Object>> subscription) {
		String sub = new String(subscription.get(0).get(0).toString());
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				subscription_comboBox.setValue(sub);
			}
		});

		String query_get_stations = "select DISTINCT companys.companyName, purchase_patten.exclusive "
				+ "from companys, purchase_patten "
				+ "where ((companys.companyTag = purchase_patten.firstCompanyTag or companys.companyTag = purchase_patten.secondCompanyTag or companys.companyTag = purchase_patten.thirdCompanyTag) and purchase_patten.userID = "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ ") or (purchase_patten.exclusive = 0 and "
				+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID()
				+ " = purchase_patten.userID)";
		Message message_get_stations = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomerPurchasePatternController_get_staiotns", query_get_stations);
		MainClientGUI.client.handleMessageFromClientUI(message_get_stations);
	}

	/**
	 * update the companies the user chose, if exclusive = 0 so the user can fuel at
	 * each station without discount
	 * 
	 * @param stations, get the company name and the exclusive
	 */
	public void return_stations(ArrayList<ArrayList<Object>> stations) {
		String station1 = "";
		String station2 = "";
		String station3 = "";
		if (((Integer) stations.get(0).get(1)).equals(0)) {
			sonol_checkBox.setSelected(true);
			sonol_checkBox.setDisable(true);
			pas_checkBox.setSelected(true);
			pas_checkBox.setDisable(true);
			dorAlon_checkBox.setSelected(true);
			dorAlon_checkBox.setDisable(true);
			delek_checkBox.setSelected(true);
			delek_checkBox.setDisable(true);
			ten_checkBox.setSelected(true);
			ten_checkBox.setDisable(true);
			nrg_checkBox.setSelected(true);
			nrg_checkBox.setDisable(true);
			return;
		} else {
			exclusive_radioBtn.setSelected(true);
		}

		// fill the stations

		switch (stations.size()) {
		case 0:
			break;
		case 1:
			station1 = new String(stations.get(0).get(0).toString());
			break;
		case 2:
			station1 = new String(stations.get(0).get(0).toString());
			station2 = new String(stations.get(1).get(0).toString());
			break;
		case 3:
			station1 = new String(stations.get(0).get(0).toString());
			station2 = new String(stations.get(1).get(0).toString());
			station3 = new String(stations.get(2).get(0).toString());
			break;
		}
		if ("Sonol".equals(station1) || "Sonol".equals(station2) || "Sonol".equals(station3)) {
			sonol_checkBox.setSelected(true);

			if (count == 0) {
				company1 = 1;
			} else if (count == 1) {
				company2 = 1;
			} else {
				company3 = 1;
			}
			count++;
		}
		if ("Dor Alon".equals(station1) || "Dor Alon".equals(station2) || "Dor Alon".equals(station3))

		{
			dorAlon_checkBox.setSelected(true);
			if (count == 0) {
				company1 = 2;
			} else if (count == 1) {
				company2 = 2;
			} else {
				company3 = 2;
			}
			count++;
		}
		if ("Ten".equals(station1) || "Ten".equals(station2) || "Ten".equals(station3)) {
			ten_checkBox.setSelected(true);
			if (count == 0) {
				company1 = 3;
			} else if (count == 1) {
				company2 = 3;
			} else {
				company3 = 3;
			}
			count++;
		}
		if ("Paz".equals(station1) || "Paz".equals(station2) || "Paz".equals(station3)) {
			pas_checkBox.setSelected(true);
			if (count == 0) {
				company1 = 4;
			} else if (count == 1) {
				company2 = 4;
			} else {
				company3 = 4;
			}
			count++;
		}
		if ("Delek".equals(station1) || "Delek".equals(station2) || "Delek".equals(station3)) {
			delek_checkBox.setSelected(true);
			if (count == 0) {
				company1 = 5;
			} else if (count == 1) {
				company2 = 5;
			} else {
				company3 = 5;
			}
			count++;
		}
		if ("N.R.G".equals(station1) || "N.R.G".equals(station2) || "N.R.G".equals(station3)) {
			nrg_checkBox.setSelected(true);
			if (count == 0) {
				company1 = 6;
			} else if (count == 1) {
				company2 = 6;
			} else {
				company3 = 6;
			}
			count++;
		}
	}

}
