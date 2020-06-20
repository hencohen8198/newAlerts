package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;
import server.common.MessageHandlerServer;
import server.common.controllers.MainServerController;

/**
 * This class is the controller of adding a new customer to the MyFuel system,
 * used by the marketing agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentCustomerAddController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentCustomerAddController instance;
	// public static boolean fl = false;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private VBox main_vbox;

	@FXML
	private Button sales_btn;

	@FXML
	private RadioButton private_radio_btn;

	@FXML
	private RadioButton company_radio_btn;

	@FXML
	private Label company_name_label;

	@FXML
	private HBox company_hbox;

	@FXML
	private TextField company_name_txt;

	@FXML
	private HBox first_name_hbox;

	@FXML
	private Label first_name_label;

	@FXML
	private TextField first_name_txt;

	@FXML
	private HBox last_name_hbox;

	@FXML
	private Label last_name_label;

	@FXML
	private TextField last_name_txt;

	@FXML
	private TextField id_txt;

	@FXML
	private TextField email_txt;

	@FXML
	private TextField credit_card_number_txt;

	@FXML
	private VBox menu_parent;

	@FXML
	private ComboBox<Integer> month_comboBox;

	@FXML
	private ComboBox<Integer> year_comboBox;

	@FXML
	private TextField cvv_txt;

	@FXML
	private Button main_btn;

	@FXML
	private Button customer_info_btn;

	@FXML
	private Button vehicle_info_btn;

	@FXML
	private Button update_purchase_pattern_btn;

	@FXML
	private Button back_btn;

	@FXML
	private Button update_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	/**
	 * if back button is clicked, alert is pop up and the user back to the main
	 * screen where the table with the users shown
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		exitFromScreenAlert("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customer_info_btn_clicked(MouseEvent event) {
//		if (UserController.getCustomer()==null) {
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Choose customer");
//			alert.setHeaderText(null);
//			alert.setContentText("Please select customer");
//			alert.show();
//			return;
//		}
//		switchScenes("/client/boundry/MarketingAgentCustomerEditForm.fxml",
//				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * switch the radio button from private to company and switch the label, text
	 * fields
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void company_radio_btn_clicked(ActionEvent event) {
		if (!company_radio_btn.isSelected()) {
			company_radio_btn.setSelected(true);
			return;
		}
		private_radio_btn.setSelected(false);
		checkRadioButton();
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
		dia.setContentText("Add customer Button:\nHere you can add new customer to MyFuel system\n"
				+ "Please select \"Private\" / \"Compnay\" first\n"
				+ "Correct details:\nID with 9 digits, credit card with 8-16 digits, cvv with 3 digits");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentCustomerAddController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * switch the radio button from company to private and switch the labels, text
	 * fields
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void private_radio_btn_clicked(ActionEvent event) {
		if (!private_radio_btn.isSelected()) {
			private_radio_btn.setSelected(true);
			return;
		}
		company_radio_btn.setSelected(false);
		checkRadioButton();
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
	 * when the button update is clicked, the method check if the input is valid ,
	 * than check if the user is already exist , else show relevant alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_btn_clicked(MouseEvent event) {
		if ((correctInputNubmers(id_txt, 9))) {
			checkIfUserExist(id_txt);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Please enter valid input");
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
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		alert.setContentText("Please return to the \"Main\" screen, \nChoose customer to update the purchase pattern");
		alert.show();
	}

	/**
	 * alert the user to choose a customer first before switch to vehicle info
	 * screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void vehicle_info_btn_clicked(MouseEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		alert.setContentText("Please return to the \"Main\" screen, \nChoose customer to watch the vehicle info");
		alert.show();
	}

	/**
	 * initialize the screen, set the user name at the top of the page, first the
	 * company's labels is removed until company radio button is clicked, and
	 * initialize the months, years of the credit card info
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		main_vbox.getChildren().remove(company_hbox);
		main_vbox.getChildren().remove(company_name_txt);
		company_radio_btn.setSelected(false);
		for (int i = 1; i < 13; i++) {
			month_comboBox.getItems().add(i);
		}
		for (int i = 20; i < 30; i++) {
			year_comboBox.getItems().add(i);
		}
	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * this method switch the name of the company/private customer by the radio
	 * button
	 */
	public void checkRadioButton() {

		if (private_radio_btn.isSelected()) {
			main_vbox.getChildren().remove(company_hbox);
			main_vbox.getChildren().remove(company_name_txt);
			main_vbox.getChildren().add(1, first_name_hbox);
			main_vbox.getChildren().add(2, first_name_txt);
			main_vbox.getChildren().add(3, last_name_hbox);
			main_vbox.getChildren().add(4, last_name_txt);
		}
		if (company_radio_btn.isSelected()) {
			main_vbox.getChildren().remove(first_name_hbox);
			main_vbox.getChildren().remove(first_name_txt);
			main_vbox.getChildren().remove(last_name_hbox);
			main_vbox.getChildren().remove(last_name_txt);
			main_vbox.getChildren().add(1, company_hbox);
			main_vbox.getChildren().add(2, company_name_txt);
		}
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
	 * returned from the DB with the id of the new user
	 * 
	 * @param userID, row with the id of the new user for the new rows
	 */
	public void getUserID(ArrayList<ArrayList<Object>> userID) {
		// make the relevant query for private customer
		if (private_radio_btn.isSelected()) {
			String query_private = "Insert into customers (firstName,lastName,ID,email,credit,userID,customerType) VALUES (\""
					+ first_name_txt.getText() + "\", \"" + last_name_txt.getText() + "\", \"" + id_txt.getText()
					+ "\", \"" + email_txt.getText() + "\", " + credit_card_number_txt.getText() + ", "
					+ (Integer) userID.get(0).get(0) + ", 1)";
			Message message_private = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerAddController_add_private_customer", query_private);
			MainClientGUI.client.handleMessageFromClientUI(message_private);
		}

		// make the relevant query for company customer

		if (company_radio_btn.isSelected()) {
			String query_company = "Insert into customers (companyName,ID,email,credit,userID,customerType) VALUES (\""
					+ company_name_txt.getText() + "\", \"" + id_txt.getText() + "\", \"" + email_txt.getText() + "\", "
					+ credit_card_number_txt.getText() + ", " + (Integer) userID.get(0).get(0) + ", 0)";
			Message message_company = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomerAddController_add_company_customer", query_company);
			MainClientGUI.client.handleMessageFromClientUI(message_company);
		}

		// make a relevant query for the credit card details

		String query_credit = "Insert into credit_info (credit,expMonth,expYear,cvv,userID) VALUES ("
				+ credit_card_number_txt.getText() + ", " + month_comboBox.getSelectionModel().getSelectedItem() + ", "
				+ year_comboBox.getSelectionModel().getSelectedItem() + ", " + cvv_txt.getText() + ", "
				+ (Integer) userID.get(0).get(0) + ")";
		Message message_credit = new Message(MessageType.UPDATEINFO,
				"MarketingAgentCustomerAddController_add_credit_customer", query_credit);
		MainClientGUI.client.handleMessageFromClientUI(message_credit);

		// make a default query for the customer rates details

		String rates_query = "INSERT INTO customer_rates(userID) VALUES (" + (Integer) userID.get(0).get(0) + ")";
		Message message_rates = new Message(MessageType.UPDATEINFO,
				"MarketingAgentCustomerAddController_add_rates_customer", rates_query);
		MainClientGUI.client.handleMessageFromClientUI(message_rates);

		// make a default query for the purchase patterns details

		String purchase_pattern_query = "INSERT INTO purchase_patten(userID) VALUES (" + (Integer) userID.get(0).get(0)
				+ ")";
		Message message_purchase_pattern = new Message(MessageType.UPDATEINFO,
				"MarketingAgentCustomerAddController_add_purchase_patten_customer", purchase_pattern_query);
		MainClientGUI.client.handleMessageFromClientUI(message_purchase_pattern);

		// make a default query for the analytic system details

		String analytic_info_query = "INSERT INTO analytic_info(customerID) VALUES (" + id_txt.getText() + ")";
		Message message_analytic_info = new Message(MessageType.UPDATEINFO,
				"MarketingAgentCustomerAddController_add_analityc_info", analytic_info_query);
		MainClientGUI.client.handleMessageFromClientUI(message_analytic_info);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Customer Added");
				alert.setHeaderText(null);
				alert.setContentText("The customer successfully added");
				alert.show();
			}
		});

		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * this method is for the correct numbers input
	 * 
	 * @param txt,   the text field where we take the text
	 * @param label, an integer with a sign for the size of the input we want to
	 *               accept
	 * @return true if the input is in the valid format, else false
	 */
	public boolean correctInputNubmers(TextField txt, int label) {
		try {
			if (label == 20) {
				if (txt.getText().length() > 20) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter less than 21 chars");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 30) {
				if (txt.getText().length() > 30) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter less than 31 chars");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// 40 digits and '@' sign at email box

			if (label == 401) {
				if (txt.getText().length() > 40 || !txt.getText().contains("@")) {
					Alert alert = new Alert(AlertType.ERROR,
							"Please Enter less than 41 chars at email field\nOr email not valid");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 9) {
				if (txt.getText().length() != 9) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 9 numbers at id field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 3) {
				if (txt.getText().length() != 3) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 3 chars at cvv field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// 8-16 digits

			if (label == 816) {
				if (txt.getText().length() < 8 || txt.getText().length() > 16) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter between 8-16 chars at credit field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// colored the text field with the wrong input

		} catch (Exception e) {
			txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			return false;
		}
		txt.setStyle(null);
		return true;
	}

	/**
	 * this method is for the correct string input
	 * 
	 * @param txt,   the text field where we take the text
	 * @param label, an integer with a sign for the size of the input we want to
	 *               accept
	 * @return true if the input is in the valid format, else false
	 */
	public boolean correctInputString(TextField txt, int label) {
		try {
			if (label == 20) {
				if (txt.getText().length() > 20) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter less than 21 chars");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 30) {
				if (txt.getText().length() > 30) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter less than 31 chars");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// 40 digits and '@' sign at email box

			if (label == 401) {
				if (txt.getText().length() > 40 || !txt.getText().contains("@")) {
					Alert alert = new Alert(AlertType.ERROR,
							"Please Enter less than 41 chars at email field\nOr email not valid");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 9) {
				if (txt.getText().length() != 9) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 9 numbers at id field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			if (label == 3) {
				if (txt.getText().length() != 3) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 3 chars at cvv field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// 8-16 digits

			if (label == 816) {
				if (txt.getText().length() < 8 || txt.getText().length() > 16) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter between 8-16 chars at credit field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}

			// colored the text field with the wrong input

		} catch (Exception e) {
			txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			return false;
		}
		if (!(txt.getText().isEmpty())) {
			txt.setStyle(null);
			return true;
		} else {
			txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			Alert alert = new Alert(AlertType.ERROR, "Field is empty");
			alert.setTitle("ERROR");
			alert.setHeaderText(null);
			alert.show();
			return false;
		}
	}

	/**
	 * this method if for checking if the user exist by his ID, the username is the
	 * ID of the user
	 * 
	 * @param txt, get the ID from the text field for the query
	 */
	public void checkIfUserExist(TextField txt) {
		String id_query = "select users.username from users where users.username =" + txt.getText();
		Message message_id = new Message(MessageType.REQUESTINFO,
				"MarketingAgentCustomerAddController_check_if_customer_exist", id_query);
		MainClientGUI.client.handleMessageFromClientUI(message_id);
	}

	/**
	 * if the customer id is null we can add the customer
	 * 
	 * @param customer_id, if the parameter is null so we can add the user
	 */
	public void checkIfCreditCardExist(ArrayList<ArrayList<Object>> customer_id) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (customer_id == null) {
					String id_query = "select credit_info.expMonth,credit_info.expYear,credit_info.cvv from credit_info where credit_info.credit ="
							+ credit_card_number_txt.getText();
					Message message_id = new Message(MessageType.REQUESTINFO,
							"MarketingAgentCustomerAddController_check_if_credit_card_exist", id_query);
					MainClientGUI.client.handleMessageFromClientUI(message_id);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("User already exists");
					alert.setHeaderText(null);
					alert.setContentText("The user is already a registered");
					alert.show();
					return;
				}
			}
		});

	}

	/**
	 * this method checks if someone add a credit card number that in our system,
	 * but the month/year/CVV is not the same
	 * 
	 * @param customer_credit_card, parameter with the information of credit card
	 *                              already exist in the system
	 */
	public void getCustomerID(ArrayList<ArrayList<Object>> customer_credit_card) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Integer cvv = null, month = null, year = null;
				if (customer_credit_card != null) {
					month = (Integer) customer_credit_card.get(0).get(0);
					year = (Integer) customer_credit_card.get(0).get(1);
					cvv = (Integer) customer_credit_card.get(0).get(2);
				}
				if (customer_credit_card == null || (month_comboBox.getSelectionModel().getSelectedItem().equals(month)
						&& year_comboBox.getSelectionModel().getSelectedItem().equals(year)
						&& cvv_txt.getText().equals(cvv.toString()))) {
					if ((private_radio_btn.isSelected() && correctInputString(first_name_txt, 20)
							&& correctInputString(last_name_txt, 20) && correctInputString(email_txt, 401)
							&& correctInputNubmers(id_txt, 9) && correctInputNubmers(credit_card_number_txt, 816)
							&& correctInputNubmers(cvv_txt, 3) && !month_comboBox.getSelectionModel().isEmpty()
							&& !year_comboBox.getSelectionModel().isEmpty() && private_radio_btn.isSelected())
							|| company_radio_btn.isSelected() && (correctInputString(company_name_txt, 30)
									&& correctInputString(email_txt, 401) && correctInputNubmers(id_txt, 9)
									&& correctInputNubmers(credit_card_number_txt, 816)
									&& correctInputNubmers(cvv_txt, 3) && !month_comboBox.getSelectionModel().isEmpty()
									&& !year_comboBox.getSelectionModel().isEmpty()
									&& company_radio_btn.isSelected())) {
						String query1 = "Insert into users (username,password,type,connection_status) VALUES(\""
								+ id_txt.getText() + "\", \"" + id_txt.getText() + "\", 3 , 0)";
						Message message1 = new Message(MessageType.UPDATEINFO,
								"MarketingAgentCustomerAddController_add_user", query1);
						MainClientGUI.client.handleMessageFromClientUI(message1);
						String userID = "select u.userID from users u where \"" + id_txt.getText() + "\"= u.username";
						Message message2 = new Message(MessageType.REQUESTINFO,
								"MarketingAgentCustomerAddController_get_userID", userID);
						MainClientGUI.client.handleMessageFromClientUI(message2);
					} else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Wrong Input");
						alert.setHeaderText(null);
						alert.setContentText("Please enter vaild input");
						alert.show();
						return;
					}
				} else {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Credit already exists");
					alert.setHeaderText(null);
					alert.setContentText("The credit is already a registered with diffrent values");
					alert.show();
					return;
				}
			}
		});

	}
}
