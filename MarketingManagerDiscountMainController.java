package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.MarketingManagerDiscount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * this class represents the Discount page, displays the rates and their
 * discount
 * 
 * @author Yehonatan
 * @version 0.99
 */
public class MarketingManagerDiscountMainController extends AbstractController {
	/**
	 * instance of the controller
	 */
	public static MarketingManagerDiscountMainController instance;
	// public static boolean fl = false;
	// Observable list for the list of the notifications

	public ObservableList<MarketingManagerDiscount> rates_list = FXCollections.observableArrayList();
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
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private TableView<MarketingManagerDiscount> discount_table;

	@FXML
	private TableColumn<MarketingManagerDiscount, Integer> RateTypeColumn;

	@FXML
	private TableColumn<MarketingManagerDiscount, Float> dicount_column;

	@FXML
	private TableColumn<MarketingManagerDiscount, String> rateName_column;
	@FXML
	private Button add_btn;
	@FXML
	private Button Edit_fuels_price_btn;

	@FXML
	void Edit_fuels_price_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerDiscounEditFuelPriceForm.fxml",
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
		switchScenes("/client/boundry/MarketingManagerDiscountMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is responsible for handling a click in the add button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void add_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerDiscounAddForm.fxml", "/client/boundry/MarketingManagerMain.css");

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
		dia.setContentText("\nHere you will able  to watch the rates information.\n\n"
				+ "Add button:\nclick on this button if you want to add new discount.\n\n"+"Edit fuels price button:\nclick on this button to give a new price for fuel");

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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerDiscountMainController_logout_clicked",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
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
		switchScenes("/client/boundry/MarketingManagerNotificationMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is responsible for handling a click in the reports button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void reports_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerReportsForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is responsible for handling a click in the sales button and it
	 * switch between the forms
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		RateTypeColumn.setCellValueFactory(new PropertyValueFactory<MarketingManagerDiscount, Integer>("rateType"));
		dicount_column.setCellValueFactory(new PropertyValueFactory<MarketingManagerDiscount, Float>("discount"));
		rateName_column.setCellValueFactory(new PropertyValueFactory<MarketingManagerDiscount, String>("rateName"));
//		descriptionColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("description"));
		// this query is for get from DB the rates.
		String quary = "SELECT * FROM rates";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingManagerDiscountMainController_initialize",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

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
	 * this method get rate an their discount add it to the list and put them in the
	 * list
	 * 
	 * @param rates,get the rates details which has in the DB
	 */
	public void fill_table_dicount(ArrayList<ArrayList<Object>> rates) {
		for (ArrayList<Object> row : rates) {
			rates_list.add(new MarketingManagerDiscount((Integer) row.get(0), (Float) row.get(1), (String) row.get(2)));
		}
		discount_table.setItems(rates_list);

	}
}
