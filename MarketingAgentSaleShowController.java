package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class is the controller of showing sale pattern screen, used by
 * marketing agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentSaleShowController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentSaleShowController instance;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button back_btn;

	@FXML
	private Label station_tag_lable;

	@FXML
	private Label sale_name_lable;

	@FXML
	private Label sale_tag_lable;

	@FXML
	private Label start_hour_lable;

	@FXML
	private Label end_hour_lable;

	@FXML
	private Label discount_lable;

	@FXML
	private Label fuel_type_lable;

	@FXML
	private Label description_lable;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	/**
	 * switch the screen to main sales form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * switch the screen to main customer form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
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
		dia.setContentText("Show Sale Pattern Screen\nIn this screen marketing agent can see specific sale pattern\n"
				+ "the full information is in this page, click \"Back\" to return");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentSaleShowController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
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
	 * switch the screen to notification form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * switch the screen to sales form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * initialize the screen, set the user name at the top of the page, and
	 * initialize page with the details of the sale
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());

		// receive the row of specific sale by sale tag
		String quary = "SELECT sale_pattern.*, stations.location, stations.stationName, companys.companyName, fuels.fuelName FROM sale_pattern, stations, companys, fuels  where salePatternTag = "
				+ MarketingAgentSalesMainController.sale_selected.getSalePatternTag()
				+ " and sale_pattern.stationTag = stations.stationTag and companys.companyTag = stations.companyTag and fuels.fuelType = sale_pattern.fuelType";
		Message msg = new Message(MessageType.REQUESTINFO, "MarketingAgentSaleShowController_sale_details", quary);
		MainClientGUI.client.handleMessageFromClientUI(msg);

	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");

	}

	/**
	 * set the sale details at the labels with some changes for better look by
	 * StringBuilder
	 * 
	 * @param sale_details, get the sale details from DB for initialize
	 */
	public void return_sale_details(ArrayList<ArrayList<Object>> sale_details) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StringBuilder start = null, end = null, discount = null, stationTag = null, fuelType = null;
				start = new StringBuilder((String) sale_details.get(0).get(2).toString());
				end = new StringBuilder((String) sale_details.get(0).get(3));
				discount = new StringBuilder(((Float) sale_details.get(0).get(4)).toString());
				stationTag = new StringBuilder(((Integer) sale_details.get(0).get(0)).toString());
				fuelType = new StringBuilder((String) sale_details.get(0).get(12));
				start.insert(2, ":");
				end.insert(2, ":");
				discount.append("%");
				stationTag.append(", ");
				stationTag.append((String) sale_details.get(0).get(9));// location
				stationTag.append(", ");
				stationTag.append((String) sale_details.get(0).get(10));// station name
				stationTag.append(", ");
				stationTag.append((String) sale_details.get(0).get(11));// company name
				station_tag_lable.setText(stationTag.toString());
				sale_name_lable.setText((String) sale_details.get(0).get(7));
				sale_tag_lable.setText(((Integer) sale_details.get(0).get(5)).toString());
				start_hour_lable.setText(start.toString());
				end_hour_lable.setText(end.toString());
				discount_lable.setText(discount.toString());
				fuel_type_lable.setText(fuelType.toString());
				description_lable.setText((String) sale_details.get(0).get(8));
			}
		});
	}
}
