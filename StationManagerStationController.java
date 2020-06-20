package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the station screen of the station manager.
 * here the station manager will find all of the station information. like the
 * reserves status, and station location/name.
 *
 * @author henco
 * @version 0.99
 */
public class StationManagerStationController extends AbstractController {

	/**
	 * instance of this controller
	 */
	public static StationManagerStationController instance;
	/**
	 * flag that help to make the threshold field disappear.
	 */
	private static boolean motor_wait = false;
	/**
	 * flag that help to make the threshold field disappear.
	 */
	private static boolean diesel_wait = false;
	/**
	 * flag that help to make the threshold field disappear.
	 */
	private static boolean soler_wait = false;
	/**
	 * holds the station number of the station manager
	 */
	public static String station_tag_number;

	@FXML
	private Label station_name;

	@FXML
	private Text treshold_motor_text;

	@FXML
	private Text treshold_diesel_text;

	@FXML
	private Text treshold_soler_text;

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
	private Label user_fullname;

	@FXML
	private Label motor_price;

	@FXML
	private Label motor_max;

	@FXML
	private Label motor_corrent;

	@FXML
	private ProgressBar motor_progress;

	@FXML
	private Button motor_set_thres;

	@FXML
	private Label diesel_price;

	@FXML
	private Label diesel_max;

	@FXML
	private Label diesel_corrent;

	@FXML
	private ProgressBar diesel_progress;

	@FXML
	private Button diesel_set_thres;

	@FXML
	private Label soler_price;

	@FXML
	private Label soler_max;

	@FXML
	private Label soler_corrent;

	@FXML
	private ProgressBar soler_progress;

	@FXML
	private Button soler_set_thres;

	@FXML
	private Label company_name;

	@FXML
	private Label station_tag;

	@FXML
	private Button refresh;

	@FXML
	private TextField input_for_motor;

	@FXML
	private TextField input_for_diesel;

	@FXML
	private TextField input_for_soler;

	@FXML
	private VBox threshold_motor_vbox;

	@FXML
	private VBox threshold_diesel_vbox;

	@FXML
	private VBox threshold_soler_vbox;

	/**
	 * this method request data of the fuel reserves in the database from the server
	 */
	private void setUpFuel_request() {
		String quary = "SELECT fuel_reserves.fuelType,fuels.price,fuel_reserves.fuelInventory,fuel_reserves.treshold,fuel_reserves.maxQuantity,fuel_reserves.stationTag ,companys.companyName,stations.stationName FROM fuel_reserves,employees,stations,companys,fuels WHERE employees.userID = "
				+ MainClientGUI.getUserID()
				+ " And employees.eid=stations.eid AND stations.stationTag = fuel_reserves.stationTag AND companys.companyTag = stations.companyTag AND fuels.fuelType = fuel_reserves.fuelType;";
		Message message = new Message(MessageType.REQUESTINFO, "StationManagerStationController_setUpFuel_request",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method is responsible in handleing the information recieved from the
	 * database
	 * 
	 * @param info holds matrix of fuel reserves information.
	 */
	public void setUpFuel_return(ArrayList<ArrayList<Object>> info) {

		/*
		 * display station data
		 */
		station_tag_number = ((Integer) info.get(0).get(5)).toString();
		station_tag.setText("Station Tag: " + ((Integer) info.get(0).get(5)).toString());
		company_name.setText("Company: " + (String) info.get(0).get(6));
		station_name.setText("Station Name: " + (String) info.get(0).get(7));
		/*
		 * display motor fuel data
		 */
		motor_price.setText("Price: " + ((Float) info.get(0).get(1)).toString());
		motor_corrent.setText("Inventory: " + ((Float) info.get(0).get(2)).toString());
		motor_max.setText("Max Amount: " + ((Float) info.get(0).get(4)).toString());
		motor_progress.setProgress(((Float) info.get(0).get(2)) / ((Float) info.get(0).get(4)));
		treshold_motor_text.setText("Min Amount: " + ((Float) info.get(0).get(3)).toString());
		/*
		 * display diesel fuel data
		 */
		diesel_price.setText("Price: " + ((Float) info.get(1).get(1)).toString());
		diesel_corrent.setText("Inventory: " + ((Float) info.get(1).get(2)).toString());
		diesel_max.setText("Max Amount: " + ((Float) info.get(1).get(4)).toString());
		diesel_progress.setProgress(((Float) info.get(1).get(2)) / ((Float) info.get(1).get(4)));
		treshold_diesel_text.setText("Min Amount: " + ((Float) info.get(1).get(3)).toString());
		/*
		 * display soler fuel data
		 */
		soler_price.setText("Price: " + ((Float) info.get(2).get(1)).toString());
		soler_corrent.setText("Inventory: " + ((Float) info.get(2).get(2)).toString());
		soler_max.setText("Max Amount: " + ((Float) info.get(2).get(4)).toString());
		soler_progress.setProgress(((Float) info.get(2).get(2)) / ((Float) info.get(2).get(4)));
		treshold_soler_text.setText("Min Amount: " + ((Float) info.get(2).get(3)).toString());
	}

	/**
	 * remove text fields of threshold
	 */
	void setUpthresholdButtons() {
		threshold_motor_vbox.getChildren().remove(input_for_motor);
		threshold_diesel_vbox.getChildren().remove(input_for_diesel);
		threshold_soler_vbox.getChildren().remove(input_for_soler);
	}

	/**
	 * responsible for the click of the motor threshold button. it saves the new
	 * threshold of the fuel reserve or opens the relevent text field for input
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void motor_set_thres_clicked(MouseEvent event) {
		if (!motor_wait) {
			threshold_motor_vbox.getChildren().remove(treshold_motor_text);
			threshold_motor_vbox.getChildren().add(input_for_motor);
			motor_wait = true;
		} else {
			if (!input_for_motor.getText().isEmpty()) {
				if (checkValidity(input_for_motor)) {
					treshold_motor_text.setText("Min Amount: " + input_for_motor.getText());
					String quary = "UPDATE fuel_reserves SET fuel_reserves.treshold = " + input_for_motor.getText()
							+ " WHERE fuel_reserves.stationTag = " + station_tag_number + " AND fuelType = 1";
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerStationController_motor_set_thres_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Threshold Updated!");
					alert.setHeaderText(null);
					alert.setContentText("The New Threshold Has Been Updated");
					alert.initOwner(MainClientGUI.primaryStage);
					alert.showAndWait();
				}
			}
			threshold_motor_vbox.getChildren().remove(input_for_motor);
			threshold_motor_vbox.getChildren().add(treshold_motor_text);
			motor_wait = false;
			refresh_clicked(null);
		}
	}

	/**
	 * responsible for the click of the diesel threshold button. it saves the new
	 * threshold of the fuel reserve or opens the relevent text field for input
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	private void diesel_set_thres_clicked(MouseEvent event) {
		if (!diesel_wait) {
			threshold_diesel_vbox.getChildren().remove(treshold_diesel_text);
			threshold_diesel_vbox.getChildren().add(input_for_diesel);
			diesel_wait = true;
		} else {
			if (!input_for_diesel.getText().isEmpty()) {
				if (checkValidity(input_for_diesel)) {
					treshold_diesel_text.setText("Min Amount: " + input_for_diesel.getText());
					String quary = "UPDATE fuel_reserves SET fuel_reserves.treshold = " + input_for_diesel.getText()
							+ " WHERE fuel_reserves.stationTag = " + station_tag_number + " AND fuelType = 2";
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerStationController_motor_set_thres_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Threshold Updated!");
					alert.setHeaderText(null);
					alert.setContentText("The New Threshold Has Been Updated");
					alert.initOwner(MainClientGUI.primaryStage);
					alert.showAndWait();
				}
			}
			threshold_diesel_vbox.getChildren().remove(input_for_diesel);
			threshold_diesel_vbox.getChildren().add(treshold_diesel_text);
			diesel_wait = false;
			refresh_clicked(null);
		}
	}

	/**
	 * responsible for the click of the soler threshold button. it saves the new
	 * threshold of the fuel reserve or opens the relevent text field for input
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void soler_set_thres_clicked(MouseEvent event) {
		if (!soler_wait) {
			threshold_soler_vbox.getChildren().remove(treshold_soler_text);
			threshold_soler_vbox.getChildren().add(input_for_soler);
			soler_wait = true;
		} else {
			if (!input_for_soler.getText().isEmpty()) {
				if (checkValidity(input_for_soler)) {
					treshold_soler_text.setText("Min Amount: " + input_for_soler.getText());
					String quary = "UPDATE fuel_reserves SET fuel_reserves.treshold = " + input_for_soler.getText()
							+ " WHERE fuel_reserves.stationTag = " + station_tag_number + " AND fuelType = 3";
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerStationController_motor_set_thres_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Threshold Updated!");
					alert.setHeaderText(null);
					alert.setContentText("The New Threshold Has Been Updated");
					alert.initOwner(MainClientGUI.primaryStage);
					alert.showAndWait();
				}
			}
			threshold_soler_vbox.getChildren().remove(input_for_soler);
			threshold_soler_vbox.getChildren().add(treshold_soler_text);
			soler_wait = false;
			refresh_clicked(null);
		}
	}

	/**
	 * this method help to detect if an input isnt valid.
	 * 
	 * @param input textfield that we want to check.
	 * @return true if the input is valid, false other.
	 */
	boolean checkValidity(TextField input) {
		try {
			Float check = new Float(input.getText());
			if (check <= 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("INVALID INPUT");
				alert.setHeaderText(null);
				alert.setContentText("Please Enter A Vaild Input");
				alert.initOwner(MainClientGUI.primaryStage);
				alert.show();
				input.clear();
				return false;
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("INVALID INPUT");
			alert.setHeaderText(null);
			alert.setContentText("Please Enter A Vaild Input");
			alert.initOwner(MainClientGUI.primaryStage);
			alert.show();
			input.clear();
			return false;
		}
		return true;
	}

	/**
	 * this method update the current display of the fuel reserves
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void refresh_clicked(MouseEvent event) {
		setUpFuel_request();
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
		dia.setContentText("\nHere is the main station menu."
				+ "\nYou may observe all of the station's reserves status. "
				+ "Like its current fuel amount, this max amount, fuel type & price, as well as its minimum threshold. "
				+ "If you want, you can edit its threshold to your desire");
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
		Message message = new Message(MessageType.LOGOUT, "StationManagerStationController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * TODO
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
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
				"/client/boundry/StationManagerNotification.css");
	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the corrent screen
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
//		if (fl) {
//			hide_manu();
//		}
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		setUpthresholdButtons();
		setUpFuel_request();
		input_for_motor.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					motor_set_thres_clicked(null);
				}
			}
		});
		input_for_diesel.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					diesel_set_thres_clicked(null);
				}
			}
		});
		input_for_soler.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					soler_set_thres_clicked(null);
				}
			}
		});
	}
}
