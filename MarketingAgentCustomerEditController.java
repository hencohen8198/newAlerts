package client.common.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.Customer;
import client.common.logic_controllers.UserController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
 * This class is the controller of editing an exists customer of MyFuel system,
 * used by the marketing agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentCustomerEditController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentCustomerEditController instance;
//	public static boolean fl = false;
	/**
	 * flag for switch from view only mode to edit mode false is view only mode
	 */
	public boolean editFlag = false;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private VBox main_vbox;

	@FXML
	private RadioButton private_radio_btn;

	@FXML
	private RadioButton company_radio_btn;

	@FXML
	private Label company_name_label;

	@FXML
	private TextField company_name_txt;

	@FXML
	private Label first_name_label;

	@FXML
	private TextField first_name_txt;

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
	private ComboBox<Integer> month_combo_box;

	@FXML
	private ComboBox<Integer> year_combo_box;

	@FXML
	private TextField cvv_txt;

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
	private Button back_btn;

	@FXML
	private Button update_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button logout_btn1;

	@FXML
	private Label user_fullname;

	@FXML
	private Label title_label;

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
	 * switch the radio button from private to company and switch the label, text
	 * fields
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void company_radio_btn_clicked(ActionEvent event) {
		if (!company_radio_btn.isSelected()) {
			company_radio_btn.setSelected(true);
			private_radio_btn.setSelected(false);
			checkRadioButton();

		}
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customer_info_btn_clicked(MouseEvent event) {

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
		dia.setContentText("Edit Customer Screen\nIn this screen marketing agnet can view the details/ edit them\n"
				+ "if you in \"view only\" mode please click the button \"Edit\" to start editing\n"
				+ "the ID and the customer type cannot be change");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentCustomerEditController_logout_clicked", quary);
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
	 * switch the radio button from company to private and switch the labels, text
	 * fields
	 * 
	 * @param event
	 */
	@FXML
	void private_radio_btn_clicked(ActionEvent event) {
		if (!private_radio_btn.isSelected()) {
			private_radio_btn.setSelected(true);
			company_radio_btn.setSelected(false);
			checkRadioButton();
		}
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
	 * first enter from edit button shows the view only screen, when edit(update
	 * button) clicked the scene is change to edit mode the relevant label, button
	 * is updated and the edit is allowed, than, when the button update is clicked,
	 * the method check if the input is valid
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void update_btn_clicked(MouseEvent event) {
		
		 //switch the scene if the flag is true
		 
		if (editFlag) {
			switchFunctionality(true);
			return;
		}
		
		  //check input for private/ company
		 
		if ((private_radio_btn.isSelected()) && correctInputString(first_name_txt, 20)
				&& correctInputString(last_name_txt, 20) && correctInputString(email_txt, 401)
				&& correctInputNubmers(credit_card_number_txt, 816) && correctInputNubmers(cvv_txt, 3)
				&& !month_combo_box.getSelectionModel().isEmpty() && !year_combo_box.getSelectionModel().isEmpty()
				|| (company_radio_btn.isSelected() && correctInputString(company_name_txt, 30)
						&& correctInputString(email_txt, 401) && correctInputNubmers(credit_card_number_txt, 816)
						&& correctInputNubmers(cvv_txt, 3) && !month_combo_box.getSelectionModel().isEmpty()
						&& !year_combo_box.getSelectionModel().isEmpty())) {
			// make a query for private customer
			if (private_radio_btn.isSelected()) {
				String query_private = "UPDATE `customers` SET `firstName`=\"" + first_name_txt.getText()
						+ "\",`lastName`= \"" + last_name_txt.getText() + "\",`email`=\"" + email_txt.getText()
						+ "\",`credit`=" + credit_card_number_txt.getText() + " WHERE customers.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
				Message message_private = new Message(MessageType.UPDATEINFO,
						"MarketingAgentCustomersMainController_edit_private", query_private);
				MainClientGUI.client.handleMessageFromClientUI(message_private);
				MarketingAgentCustomersMainController.instance.customer_selected = new Customer(
						first_name_txt.getText(), last_name_txt.getText(), new Integer(id_txt.getText()),
						email_txt.getText(), new Long(credit_card_number_txt.getText()),
						MarketingAgentCustomersMainController.instance.customer_selected.getUserID(),
						month_combo_box.getSelectionModel().getSelectedItem(),
						year_combo_box.getSelectionModel().getSelectedItem(), new Integer(cvv_txt.getText()));
			}
			// make a query for private customer
			if (company_radio_btn.isSelected()) {
				String query_company = "UPDATE `customers` SET `companyName`= \"" + company_name_txt.getText()
						+ "\",`email`= \"" + email_txt.getText() + "\",`credit`=" + credit_card_number_txt.getText()
						+ " WHERE customers.userID = "
						+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
				Message message_company = new Message(MessageType.UPDATEINFO,
						"MarketingAgentCustomersMainController_edit_private", query_company);
				MainClientGUI.client.handleMessageFromClientUI(message_company);
				MarketingAgentCustomersMainController.instance.customer_selected = new Customer(
						company_name_label.getText(), new Integer(id_txt.getText()), email_txt.getText(),
						new Long(credit_card_number_txt.getText()),
						MarketingAgentCustomersMainController.instance.customer_selected.getUserID(),
						month_combo_box.getSelectionModel().getSelectedItem(),
						year_combo_box.getSelectionModel().getSelectedItem(), new Integer(cvv_txt.getText()));

			}
			
			  //make an update query for the customer
			 
			String query_credit = "UPDATE `credit_info` SET `credit`= " + credit_card_number_txt.getText()
					+ ",`expMonth`= " + month_combo_box.getSelectionModel().getSelectedItem() + ",`expYear`="
					+ year_combo_box.getSelectionModel().getSelectedItem() + ",`cvv`=" + cvv_txt.getText()
					+ " WHERE credit_info.userID = "
					+ MarketingAgentCustomersMainController.instance.customer_selected.getUserID();
			Message message_credit = new Message(MessageType.UPDATEINFO,
					"MarketingAgentCustomersMainController_edit_private", query_credit);
			MainClientGUI.client.handleMessageFromClientUI(message_credit);
			Alert alert = new Alert(AlertType.INFORMATION, "You successfully update the customer");
			alert.setTitle("Update successful");
			alert.setHeaderText(null);
			alert.show();
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
		exitFromScreenAlert("/client/boundry/MarketingAgentCustomerPurchasePatternForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

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
	 * initialize the screen, set the user name at the top of the page, first the
	 * company's labels is removed until company radio button is clicked, and
	 * initialize the months, years of the credit card info initialize the screen by
	 * the method that called for view only/ edit mode and load the details of the
	 * customer
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		main_vbox.getChildren().remove(company_name_label);
		main_vbox.getChildren().remove(company_name_txt);
		company_radio_btn.setSelected(false);
		for (int i = 1; i < 13; i++) {
			month_combo_box.getItems().add(i);
		}
		for (int i = 20; i < 30; i++) {
			year_combo_box.getItems().add(i);
		}
		editFlag = false;
		if (UserController.getCustomer().getCustomerType().equals("Private")) {
			private_radio_btn_clicked(null);
			first_name_txt.setText(UserController.getCustomer().getFirstName());
			last_name_txt.setText(UserController.getCustomer().getLastName());
			first_name_txt.setEditable(false);
			last_name_txt.setEditable(false);
		} else {
			company_radio_btn_clicked(null);
			company_name_txt.setText(UserController.getCustomer().getCustomerName());
			company_name_txt.setEditable(false);
		}
		
		 // load the details of the customer to the text fields
		 
		id_txt.setText(UserController.getCustomer().getId().toString());
		id_txt.setEditable(false);
		email_txt.setText(UserController.getCustomer().getEmail());
		credit_card_number_txt.setText(UserController.getCustomer().getFullCredit().toString());
		month_combo_box.setValue(UserController.getCustomer().getExpMonth());
		year_combo_box.setValue(UserController.getCustomer().getExpYear());
		cvv_txt.setText(UserController.getCustomer().getCvv().toString());
		
		  //if flag is false, returned to view only mode
		 
		if (!editFlag) {
			email_txt.setEditable(false);
			credit_card_number_txt.setEditable(false);
			month_combo_box.setDisable(true);
			year_combo_box.setDisable(true);
			cvv_txt.setEditable(false);
			update_btn.setText("Edit");
			editFlag = true;
		} else {
			switchFunctionality(false);
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
	 * when private radio button is selected the label and the text field of the
	 * company is removed when company radio button is selected the labels and text
	 * fields of first, last name is removed and company label,text field is back
	 */
	public void checkRadioButton() {
		if (private_radio_btn.isSelected()) {
			main_vbox.getChildren().remove(company_name_label);
			main_vbox.getChildren().remove(company_name_txt);
			main_vbox.getChildren().add(1, first_name_label);
			main_vbox.getChildren().add(2, first_name_txt);
			main_vbox.getChildren().add(3, last_name_label);
			main_vbox.getChildren().add(4, last_name_txt);
		}
		if (company_radio_btn.isSelected()) {
			main_vbox.getChildren().remove(first_name_label);
			main_vbox.getChildren().remove(first_name_txt);
			main_vbox.getChildren().remove(last_name_label);
			main_vbox.getChildren().remove(last_name_txt);
			main_vbox.getChildren().add(1, company_name_label);
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
			
			  //40 digits and '@' sign at email box
			 
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
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 9 chars at id field");
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
			
			 //8-16 digits
			 
			if (label == 816) {
				if (txt.getText().length() < 8 || txt.getText().length() > 16) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter between 6-16 chars at credit field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			
			  //colored the text field with the wrong input
			 
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
			
			 //40 digits and '@' sign at email box
			 
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
					Alert alert = new Alert(AlertType.ERROR, "Please Enter 9 chars at id field");
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
			
			  //8-16 digits
			 
			if (label == 816) {
				if (txt.getText().length() < 6 || txt.getText().length() > 16) {
					Alert alert = new Alert(AlertType.ERROR, "Please Enter between 6-16 chars at credit field");
					alert.setTitle("Back");
					alert.setHeaderText(null);
					alert.show();
					throw new Exception();
				}
			}
			
			 //colored the text field with the wrong input
			 
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
	 * method for changing the scene from view only mode to edit and back
	 * 
	 * @param fl, if false view only mode shows up, if true, edit mode
	 */
	public void switchFunctionality(boolean fl) {
		editFlag = fl;
		if (editFlag == true) {
			title_label.setText("Customer info - Edit");
			update_btn.setText("Update");
			if (private_radio_btn.isSelected()) {
				first_name_txt.setEditable(true);
				last_name_txt.setEditable(true);
			} else {
				company_name_txt.setEditable(true);
			}
			email_txt.setEditable(true);
			credit_card_number_txt.setEditable(true);
			month_combo_box.setDisable(false);
			year_combo_box.setDisable(false);
			cvv_txt.setEditable(true);
		}
		editFlag = false;
	}
}
