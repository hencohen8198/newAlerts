package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.RateType;
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
import javafx.scene.control.TextArea;
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
 * this class represents the Add page,display the fields for Add a new discount
 * for a rate
 * 
 * @author Yehonatan
 * @version 0.99
 *
 */
public class MarketingManagerDiscountAddController extends AbstractController {
	/**
	 * an instance of the controller
	 */
	public static MarketingManagerDiscountAddController instance;
	// public static boolean fl = false;

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
	private TextField discount_txt;

	@FXML
	private ComboBox<String> RateTypeComboBox;

	@FXML
	private TextArea decription_txt;

	@FXML
	private Button back_btn;

	@FXML
	private Button sendCEO_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	/**
	 * this method is responsible for handling a click in the back button and return
	 * into the discount main
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingManagerDiscountMainForm.fxml",
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
		dia.setContentText(
				"\nHere you choose the new dicount between 0-100\n\n" + "choose the rate type you want the add\n\n"
						+ "Send to CEO button:\nclick on this button send your request to the CEO");

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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerDiscountAddController_logout_clicked",
				quary);
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
	 * this method is for the correct float input
	 * 
	 * @param txt, the text field where we get the text
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
	 * this method is handling with the marketing manager request to add a new
	 * discount on specific rate, its sent to the CEO
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sendCEO_btn_clicked(MouseEvent event) {
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		String comment;
		if (!decription_txt.getText().isEmpty()) {
			comment = decription_txt.getText();
		} else {
			comment = "No Comments Added";
		}
		if (correctInputFloat(discount_txt) && RateTypeComboBox.getSelectionModel().getSelectedItem() != null) {
			// insert to the DB the input form the add page
			String impending_rates_query = "INSERT INTO impending_rates(rateType, userID, newDiscount,comment,date) VALUES ("
					+ RateType.getRateType(RateTypeComboBox.getSelectionModel().getSelectedItem()) + ", \""
					+ MainClientGUI.getUserFirstName() + "\", \"" + discount_txt.getText() + "\", " + "\"" + comment
					+ "\",\"" + timeStamp + "\")";
			Message message_impending_rates = new Message(MessageType.UPDATEINFO,
					"MarketingManagerDiscountAddController_sendCEO", impending_rates_query);
			MainClientGUI.client.handleMessageFromClientUI(message_impending_rates);
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert1 = new Alert(AlertType.INFORMATION);
					alert1.initOwner(MainClientGUI.primaryStage);
					alert1.getButtonTypes().remove(ButtonType.CANCEL);
					alert1.setTitle("Request for approve");
					alert1.setHeaderText(null);
					alert1.setContentText("The discount sent successfully");
					alert1.show();
				}
			});
			switchScenes("/client/boundry/MarketingManagerDiscountMainForm.fxml",
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
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		ObservableList<String> options = FXCollections.observableArrayList("Casual fuel",
				"Routine monthly fuel - single car", "Routine monthly fuel - some cars",
				"Full monthly fuel - single car");
		RateTypeComboBox.setItems(options);
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());

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
	 * @param form the location of the form file
	 * @param css  the location of the CSS file
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

}
