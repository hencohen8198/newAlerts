package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.FuelType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;
/**
 * This class is the controller of the add sale pattern screen, used by
 * marketing manager
 * 
 * @author Yehonatan
 * @version 0.99
 */
public class MarketingManagerSalesAddController extends AbstractController {
	/**
	 * An instance of the class
	 * 
	 */
	public static MarketingManagerSalesAddController instance;
	public static boolean fl = false;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button reports_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button discount_btn;

	@FXML
	private TextField name_txt;

	@FXML
	private TextField discount_txt;

	@FXML
	private ComboBox<Integer> stationTagComboBox;
	@FXML
	private Label station_name_label;

	@FXML
	private ComboBox<String> fuelTypeComboBox;

	@FXML
	private TextField start_hour_txt;

	@FXML
	private TextField start_min_txt;

	@FXML
	private TextField end_hour_txt;

	@FXML
	private TextField end_min_txt;

	@FXML
	private TextArea description_txt;

	@FXML
	private Button back_btn;

	@FXML
	private Button add_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;
	/**
	 * this method is responsible for handling a click in the back button
	 * and return into the discount main
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerSaleMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}
	/**
	 * this method is responsible for handling a click in the discount button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void discount_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerDiscountMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

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
		dia.setContentText("Add Sale Pattern Screen\nIn this screen you can add new sale to specific station\n"
				+ "the discount is between 0-100% click\n" + "choose a fuel type form the option you have\n"
				+ "you must give start hour and end hour\n" + " \"Add\" to confirm");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerSalesAddController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
				"/client/boundry/MarketingManagerMain.css");
	}
	/**
	 * this method is responsible for handling a click in the notification button
	 * and it switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerNotificationMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}
	/**
	 * go to getSation tag with the station that chose and add the sale pattern
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void add_btn_clicked(MouseEvent event) {
		getStationTag(stationTagComboBox);
		Alert alert1 = new Alert(AlertType.CONFIRMATION);
		alert1.initOwner(MainClientGUI.primaryStage);
		alert1.getButtonTypes().remove(ButtonType.CANCEL);
		alert1.setTitle("Add sale");
		alert1.setHeaderText(null);
		alert1.setContentText("The sale pattern has been successfully added");
		alert1.show();

	}
	/**
	 * this method get the combo box with the chosen option and check if the station
	 * exist
	 * 
	 * @param box get the stations tag in DB
	 */
	public void getStationTag(ComboBox<Integer> box) {
		String station_id_query = "select stations.stationTag from stations where stations.stationTag ="
				+ box.getSelectionModel().getSelectedItem();
		Message message_station_id = new Message(MessageType.REQUESTINFO,
				"MarketingManagerSalesAddController_check_if_station_exist", station_id_query);
		MainClientGUI.client.handleMessageFromClientUI(message_station_id);
	}
	/**
	 * this method is responsible for handling a click in the reports button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void reports_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerReportsForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}
	/**
	 * this method is responsible for handling a click in the sales button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerSaleMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is for the correct string input
	 * 
	 * @param txt  the text field where we take the text
	 * @param str an integer with a sign for the size of the input we want to
	 *               accept
	 * @return true if the input is in the valid format, else false
	 */
	public boolean correctInputString(TextField txt, String str) {
		if (str.contentEquals("hour")) {
			Integer temp = new Integer(start_hour_txt.getText());
			if (temp < 1 || temp > 24) {
				start_hour_txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
				return false;
			} else {
				start_hour_txt.setStyle(null);
				return true;
			}
		}
		if (str.contentEquals("min")) {
			Integer temp = new Integer(start_hour_txt.getText());
			if (temp < 0 || temp > 59) {
				start_hour_txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
				return false;
			} else {
				start_hour_txt.setStyle(null);
				return true;
			}
		}
		return !(txt.getText().isEmpty());

	}
	/**
	 * this method is for the correct float input
	 * 
	 * @param txt the text field where we get the text
	 * @return true if the input is in the valid format, else false
	 */
	public boolean correctInputFloat(TextField txt) {
		try {
			Float temp = new Float(txt.getText());
			if (!txt.getText().isEmpty()) {
				if (temp <= 0 || temp > 100)
					throw new Exception();
			}
		} catch (Exception e) {
			txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			return false;
		}
		txt.setStyle(null);
		return true;
	}
	/**
	 * this method gets the station id and check the input for each field by his
	 * parameters
	 * 
	 * @param station_id get if the station ID is exist in DB
	 */
	public void getStationID(ArrayList<ArrayList<Object>> station_id) {

		if (correctInputString(name_txt, "name") && correctInputFloat(discount_txt)
				&& fuelTypeComboBox.getSelectionModel().getSelectedItem() != null
				&& correctInputString(start_hour_txt, "hour") && correctInputString(start_min_txt, "min")
				&& correctInputString(end_hour_txt, "hour") && correctInputString(end_min_txt, "min")) {
			/**
			 * make query for all of the fields and add to DB
			 */
			String sale_query = "INSERT INTO sale_pattern(stationTag, fuelType, startHour,endHour,discount,status,name,description) VALUES (\""
					+ stationTagComboBox.getSelectionModel().getSelectedItem().toString() + "\", "
					+ FuelType.getFuelType(fuelTypeComboBox.getSelectionModel().getSelectedItem()) + ", \""
					+ start_hour_txt.getText() + start_min_txt.getText() + "\", \"" + end_hour_txt.getText()
					+ end_min_txt.getText() + "\", " + discount_txt.getText() + ", \"Un-active\" , " + "\""
					+ name_txt.getText() + "\",\"" + description_txt.getText() + "\")";
			Message message_sale = new Message(MessageType.UPDATEINFO,
					"MarketingManagerSalesAddController_add_sale_pattern", sale_query);
			MainClientGUI.client.handleMessageFromClientUI(message_sale);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert1 = new Alert(AlertType.INFORMATION);
					alert1.initOwner(MainClientGUI.primaryStage);
					alert1.setTitle("Sale added");
					alert1.setHeaderText(null);
					alert1.setContentText("The sale pattern added successfully");
					alert1.show();
				}
			});
			switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml",
					"/client/boundry/MarketingManagerMain.css");
		} else {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(MainClientGUI.primaryStage);
					alert.setTitle("Wrong Input");
					alert.setHeaderText(null);
					alert.setContentText("Please enter vaild input");
					alert.show();
					return;
				}

			});
		}
	}
	/**
	 * method for initialize the combo box with the list of the stations and set
	 * them at the table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		ObservableList<String> options = FXCollections.observableArrayList("Motor", "Benzin", "Soler");
		fuelTypeComboBox.setItems(options);
		String query = "SELECT stations.stationTag from stations";
		Message stations_message = new Message(MessageType.REQUESTINFO,
				"MarketingManagerSalesAddController_get_stations", query);
		MainClientGUI.client.handleMessageFromClientUI(stations_message);
		// shows the company that the stations tag is belongs to
		stationTagComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String query = "SELECT companys.companyName from stations, companys where stations.stationTag ="
						+ stationTagComboBox.getSelectionModel().getSelectedItem().toString()
						+ " and companys.companyTag = stations.companyTag";
				Message stations_message = new Message(MessageType.REQUESTINFO,
						"MarketingManagerSalesAddController_get_station_handler", query);
				MainClientGUI.client.handleMessageFromClientUI(stations_message);
			}
		});
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
	 * method for alert before leave page with important information that could be
	 * lost
	 * 
	 * @param form, the location of the form file
	 * @param css,  the location of the CSS file
	 */
	public void exitFromScreenAlert(String form, String css) {
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
		alert.initOwner(MainClientGUI.primaryStage);
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
	 * method for initialize the combo box with the list of the stations and set
	 * them at the table
	 * 
	 * @param statoinList get from DB the station list for set them at the
	 * combo box
	 */
	public void getStationList(ArrayList<ArrayList<Object>> statoinList) {
		ObservableList<Integer> options = FXCollections.observableArrayList();
		options = FXCollections.observableArrayList();
		for (ArrayList<Object> temp : statoinList) {
			options.add((Integer) temp.get(0));
		}
		stationTagComboBox.setItems(options);
	}
	/**
	 * this method is for show the company name when user chose specific station
	 * number from combo box
	 * @param station_handler get the company name when station chosen
	 */
	public void getStationHandler(ArrayList<ArrayList<Object>> station_handler) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				station_name_label.setText((String) station_handler.get(0).get(0).toString());
			}
		});
	}
}
/**/
