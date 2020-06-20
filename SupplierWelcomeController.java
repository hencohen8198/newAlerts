package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class is the controller of the welcome screen , used by the supplier
 * 
 * @author yarin
 * @version 0.99
 */

public class SupplierWelcomeController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static SupplierWelcomeController instance;
	@FXML
	private Button menu_btn;

	@FXML
	private Button orders_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Label welcome_name;

	@FXML
	private Label Date_time_txt;

	@FXML
	private Label datelogin_welcome;

	@FXML
	protected ImageView orders_image;

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
		dia.setContentText("Welcome Screen\nIn this screen you can see when the last time you login to system\n"
				+ "now the marketing agent can choose which action to do");
		dia.show();

	}

	/**
	 * This method in charge of log out the specific client from the system. Create
	 * appropriate query and sent it to the D.B
	 * 
	 * @param event The event that caused the method to activate.
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "SupplierWelcomeController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

		// changed
		String query = ("SELECT count(status) FROM supplier_orders_list where status='New';");
		Message msg = new Message(MessageType.REQUESTINFO, "SupplierWelcomeControllerController_new_notification",
				query);
		MainClientGUI.client.handleMessageFromClientUI(msg);

	}

	/**
	 * This method in charge of create query to D.B for receiving manager
	 * information Create appropriate query and sent it to the D.B
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {

	}

	@FXML
	void orders_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/SupplierOrdersForm.fxml", "/client/boundry/SupplierWelcomeCss.css");

	}

	/**
	 * This method happen immediately when the relevant FXML open. Set the relevant
	 * name of supplier in the name label. Set the static variable to the this
	 * instance of the class
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello, " + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		welcome_name.setText(MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		datelogin_welcome.setText(MainClientGUI.getUserloginDate());

		// changed
		if (MainClientGUI.new_notifications) {
			Image img = new Image("/purchase-order-red-64.png");
			orders_image.setImage(img);
		} else {
			Image img = new Image("/purchase-order-64.png");
			orders_image.setImage(img);
		}

	}

	/**
	 * This method active when log out succeed from D.B, and than change the
	 * variable of log in to false. Finally open log in screen to reconnect.
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");

	}

	public void return_new_counter(ArrayList<ArrayList<Object>> new_wellcom_counter) {

		if ((Long) new_wellcom_counter.get(0).get(0) > 0) {
			Image img = new Image("/purchase-order-red-64.png");
			orders_image.setImage(img);
			MainClientGUI.new_notifications = true;
		} else {
			Image img = new Image("/purchase-order-64.png");
			orders_image.setImage(img);
			MainClientGUI.new_notifications = false;
		}
	}

}
