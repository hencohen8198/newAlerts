package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.MainClientGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class takes care about the customer home fuel orders. It contains
 * methods to receive inputs from the user, and method to calculte the price
 * after discount considiration Moreover,after the user select any choice of
 * urgent or not, if the order was urgent, the method will close the option to
 * supply date and set the supply date in the same day As well the class will
 * present the customer the oreder details on the right side of the form. After
 * the purchase has been placed, a receipt will send to the customer in the
 * notification form by the db The user should fill the all neccesray fileds to
 * complete his purchase
 * 
 * @author Itay
 * @version 0.99
 *
 */
public class CustomerOrdersHomefuelorderController extends AbstractController {

	private Integer customerID;
	private Float total;
	private float homefuelprice;
	/**
	 * hold the instance of the object
	 */
	public static CustomerOrdersHomefuelorderController instance;

	@FXML
	private Label priceforliter_text;

	@FXML
	private Label user_fullname;

	@FXML
	private Button menu_btn;

	@FXML
	private TextField quantity_text;

	@FXML
	private Button orders_btn;

	@FXML
	private Button notifications_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Button openOrdersStatus_btn;

	@FXML
	private RadioButton yes_radiobtn;

	@FXML
	private RadioButton no_radiobtn;

	@FXML
	private DatePicker supplyDate_datepickerbtn;

	@FXML
	private Button placeAnOrder_btn;

	@FXML
	private RadioButton credit_radiobtn;

	@FXML
	private RadioButton cash_radiobtn;

	@FXML
	private Button back_btn;

	@FXML
	private Label quantityDetail_text;

	@FXML
	private Label urgentDetail_text;

	@FXML
	private Label supplydateDetail_text;

	@FXML
	private Label discountDetail_text;

	@FXML
	private Label payementmethodDetail_text;

	@FXML
	private Label totalDetail_text;

	@FXML
	private TextField address_text;

	@FXML
	private Label addressDetails_text;

	/**
	 * This method in charge to lock the selection on cash radio button and not
	 * remove the last selection on this button As well, this method will also
	 * present the payment method detail in the right side of the screen
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void cash_radiobtn_clicked(MouseEvent event) {

		if (cash_radiobtn.isSelected()) {
			payementmethodDetail_text.setText("Cash");
			credit_radiobtn.setSelected(false);
		} else {
			cash_radiobtn.setSelected(true);
			payementmethodDetail_text.setText("Cash");
			credit_radiobtn.setSelected(false);
		}

	}

	/**
	 * This method in charge to lock the selection on credit radio button and not
	 * remove the last selection on this button As well, this method will also
	 * present the payment method detail in the right side of the screen
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void credit_radiobtn_clicked(MouseEvent event) {

		if (credit_radiobtn.isSelected()) {
			payementmethodDetail_text.setText("Credit");
			cash_radiobtn.setSelected(false);
		} else {
			credit_radiobtn.setSelected(true);
			payementmethodDetail_text.setText("Credit");
			cash_radiobtn.setSelected(false);
		}
	}

	/**
	 * This method will apply after mouse clicked on help button, and will present
	 * for the user A guide for the specific page, details about the page, and
	 * filling forms insturactions.
	 * 
	 * @param event mouse clicked
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
		dia.setContentText("\nCustomer homefuel orders:\n"
				+ "\n•In this form you can order home fuel by yur request.\n•Fill the neccesary fileds to complete your purchase.\n"
				+ "•Moreover, each urgent order simulated to delivered in the same day. \n•Every un-urgent order will deliver in the exact supply order date"
				+ ".\n•As well, after your purchase you will recive the recipet in notifications and the order status will be set in the orders table.");
		dia.show();
	}

	/**
	 * /** This method will apply after mouse clicked on logout button, and will
	 * logout the user From the system. After the click on this button, the user
	 * will return to the main login form.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "CustomerOrdersHomefuelorderController_logout_clicked",
				quary);
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will apply after logout succsess(the trigger for this method
	 * activation was From the message handler client). The method will change the
	 * form to the main form .
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * This method will apply after mouse clicked on menu button, and will present
	 * the user The menu form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerMainForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method in charge to lock the selection on NO radio button and not remove
	 * the last selection on this button As well, this method will also present the
	 * urgent detail in the right side of the screen
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void no_radiobtn_clicked(MouseEvent event) {

		if (no_radiobtn.isSelected()) {
			urgentDetail_text.setText("NO");
			yes_radiobtn.setSelected(false);
			supplydateDetail_text.setText("");
		} else {
			no_radiobtn.setSelected(true);
			urgentDetail_text.setText("NO");
			yes_radiobtn.setSelected(false);
		}
		priceforliter_text.setText(String.format("%.02f", homefuelprice));
		supplyDate_datepickerbtn.setDisable(false);
		supplyDate_datepickerbtn.setPromptText("");
		calculate_price();
		if (supplyDate_datepickerbtn.getValue() != null)
			supplydateDetail_text
					.setText(supplyDate_datepickerbtn.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

	}

	/**
	 * This method in charge to lock the selection on Yes radio button and not
	 * remove the last selection on this button As well, this method will also
	 * present the urgent detail in the right side of the screen
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void yes_radiobtn_clicked(MouseEvent event) {

		if (yes_radiobtn.isSelected()) {
			urgentDetail_text.setText("YES");
			no_radiobtn.setSelected(false);
		} else {
			yes_radiobtn.setSelected(true);
			urgentDetail_text.setText("YES");
			no_radiobtn.setSelected(false);

		}
		priceforliter_text.setText(String.format("%.02f", homefuelprice * 1.02));
		discountDetail_text.setText("");
		supplyDate_datepickerbtn.setValue(null);
		supplyDate_datepickerbtn
				.setPromptText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		supplyDate_datepickerbtn.setDisable(true);
		supplydateDetail_text.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		calculate_price();

	}

	/**
	 * This method will apply after mouse clicked on notifications button, and will
	 * present the user The notifications form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void notifications_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerNotifications.fxml", "/client/boundry/CustomerOrders.css");

	}

	/**
	 * This method will apply after mouse clicked on open orders status button, and
	 * will present the user The home fuel orders form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void openOrdersStatus_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerOrdersMainForm.fxml", "/client/boundry/CustomerOrders.css");
	}

	/**
	 * This method will apply after mouse clicked on orders button, and will present
	 * the user The orders form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void orders_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CustomerOrdersMainForm.fxml", "/client/boundry/CustomerOrders.css");

	}

	/**
	 * This method will check at first if the whole fields were completed As well,
	 * the method will insert into the db new homefuel order after the user confirm
	 * his order with today's order date Moreover, the method will make the recipet
	 * with the require home fuel order and insert it to db.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void placeAnOrder_btn_clicked(MouseEvent event) {
		if (payementmethodDetail_text.getText().isEmpty() || totalDetail_text.getText().isEmpty()
				|| address_text.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Orders table");
			alert.setContentText(String.format("Sorry, missing field"));
			alert.show();
			return;
		}
		Message message = new Message(MessageType.UPDATEINFO, "CustomerOrdersHomefuelorderController_addneworder",
				"INSERT INTO order_home_fuel(userID,orderDate,supplyDate,price,status,urgent,address,quantity,display) VALUES ("
						+ MainClientGUI.getUserID() + ",\""
						+ new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "\"" + ",\""
						+ reverseDate(supplydateDetail_text.getText()) + "\"," + total + ",\"Not delivered\",\""
						+ urgentDetail_text.getText() + "\",\"" + address_text.getText() + "\","
						+ quantity_text.getText() + ",1)");

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Orders table");
		alert.setContentText(String.format("Place an Order?"));
		Optional<ButtonType> res = alert.showAndWait();
		if (res.get() == ButtonType.CANCEL)
			return;
		else {
			MainClientGUI.client.handleMessageFromClientUI(message);
			Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
			alert2.getButtonTypes().remove(ButtonType.CANCEL);
			alert2.setTitle("Fuel Order");
			alert2.setContentText(String.format("The order has been placed"));
			alert2.initOwner(MainClientGUI.primaryStage.getOwner());
			alert2.showAndWait();
			switchScenes("/client/boundry/CustomerOrdersMainForm.fxml", "/client/boundry/CustomerOrders.css");
			StringBuilder description = new StringBuilder();
			description.append("The order has been placed on: ");
			description.append(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
			description.append("\n\n");
			description.append("The Supply date: ");
			description.append(supplydateDetail_text.getText());
			description.append("\n\n");
			description.append("The quantity is: ");
			description.append(quantityDetail_text.getText());
			description.append("\n\n");
			description.append("The Supply price is: ");
			description.append(total + "ILS");
			description.append("\n\n");
			description.append("Urgent: ");
			description.append(urgentDetail_text.getText());
			description.append("\n\n");
			if (!discountDetail_text.getText().equals("Not available")) { // Itay-fixed-add a sale disount if happend in
																			// the hoe fuel purchase
				description.append("The Sale discount: ");
				description.append(discountDetail_text.getText());
				description.append("\n\n");
			}
			description.append("Address: ");
			description.append(addressDetails_text.getText());
			Message message3 = new Message(MessageType.UPDATEINFO,
					"CustomerOrdersHomefuelorderController_addnewnotification",
					"INSERT INTO customer_notifications(customerID,topic,date,description,statusNew) VALUES ("
							+ customerID + ",\"HomeFuel receipt\"," + "\""
							+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + "\"," + "\""
							+ description.toString() + "\",1)");
			MainClientGUI.client.handleMessageFromClientUI(message3);
		}
	}

	/**
	 * This method reverse date from format of yyyy/mm/dd to dd/mm/yyyy
	 * 
	 * @param msg String to reverse
	 * @return reversed String
	 */
	public String reverseDate(String msg) {
		StringBuilder temp = new StringBuilder(msg);
		StringBuilder date = new StringBuilder();
		date.append(temp.substring(6, 10));
		date.append("/");
		date.append(temp.substring(3, 5));
		date.append("/");
		date.append(temp.substring(0, 2));
		return date.toString();
	}

	/**
	 * This method in charge to receive the address from the customer
	 * 
	 * @param event key typed
	 */
	@FXML
	void addressInQutityDetails(KeyEvent event) {
		try {
			if (!address_text.getText().isEmpty()) {
				addressDetails_text.setText(address_text.getText());
			} else {
				addressDetails_text.setText("");
				throw new Exception();
			}
		}

		catch (Exception e) {
			address_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			addressDetails_text.setText("");
			return;
		}
		address_text.setStyle(null);
	}

	/**
	 * This method in charge to receive the quantity from the customer As well, this
	 * method will also present the urgent detail in the right side of the screen
	 * with two digits after point Moreover it will check the input (that the input
	 * will be float, will colored the text field in red) and after it the method
	 * will try to calculte via calculate method.
	 * 
	 * @param event key typed
	 */
	@FXML
	void quantityInOrderSetails(KeyEvent event) {
		try {
			Float temp = new Float(quantity_text.getText());
			if (!quantity_text.getText().isEmpty()) {
				if (temp <= 0)
					throw new Exception();
				StringBuilder sb = new StringBuilder(quantity_text.getText());
				if (sb.charAt(0) == '.')
					sb.insert(0, '0');
				else {
					while (sb.charAt(0) == '0' || sb.charAt(0) == ' ') {
						if (sb.charAt(1) == '.')
							break;
						if (sb.charAt(0) != '0' && sb.charAt(0) != ' ')
							break;
						sb.deleteCharAt(0);
					}

				}
				quantityDetail_text.setText(sb.toString() + "L");

			} else {
				quantityDetail_text.setText("");
			}
			calculate_price();
		} catch (Exception e) {
			quantity_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			quantityDetail_text.setText("");
			calculate_price();
			return;
		}
		quantity_text.setStyle(null);

	}

	/**
	 * this method will calculte the price if the all fields are completed And set
	 * the calculted price into the orderes detail As well, it will calculate the
	 * price considering the quantity threshold discount.
	 */
	public void calculate_price() {
		if (!check()) {
			totalDetail_text.setText("");
			return;
		}
		Float quantity;
		try {
			quantity = new Float(quantity_text.getText());
		} catch (Exception e) {
			return;
		}

		float discount = 1;
		float addi = 1;
		String discountString = "";
		if (yes_radiobtn.isSelected()) {
			addi += 0.02;
			priceforliter_text.setText(String.format("%.02f", homefuelprice * addi) + " ILS");
		}
		if ((quantity >= 600 && quantity < 800)) {
			discount -= 0.03;
			discountString = "3%";
		}
		if (quantity >= 800) {
			discount -= 0.04;
			discountString = "4%";
		}
		if (discount == 1) {
			discountDetail_text.setText("");
		} else {
			discountDetail_text.setText(discountString);
		}
		total = quantity * homefuelprice * addi * discount;
		totalDetail_text.setText(total.toString() + "ILS");

	}

	/**
	 * This method will check the inputs and return true if the calculte price
	 * method could work, else false
	 * 
	 * @return boolen true or false
	 */
	public boolean check() {

		Float quantity;
		try {
			quantity = new Float(quantity_text.getText());
		} catch (Exception e) {
			return false;
		}
		if (supplyDate_datepickerbtn.getValue() == null && !yes_radiobtn.isSelected())
			return false;
		if (!yes_radiobtn.isSelected() && !no_radiobtn.isSelected())
			return false;
		return true;

	}

	/**
	 * This method will initlize the username, make a query to load home fuel price
	 * and a query customer id. Moreover, the method will close to chose the last
	 * previous days until today
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		supplyDate_datepickerbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (supplyDate_datepickerbtn.getValue() != null) {
					supplydateDetail_text.setText(
							supplyDate_datepickerbtn.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				}
				calculate_price();
			}
		});
		supplyDate_datepickerbtn.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();

				setDisable(empty || date.compareTo(today) < 0);

			}
		});
		Message message2 = new Message(MessageType.REQUESTINFO,
				"CustomerOrdersHomefuelorderController_recivecustomerID",
				"SELECT customers.ID FROM customers WHERE customers.userID=" + MainClientGUI.getUserID());
		MainClientGUI.client.handleMessageFromClientUI(message2);

		Message message = new Message(MessageType.REQUESTINFO, "CustomerOrdersHomefuelorderController_gethomefuelprice",
				"SELECT fuels.price FROM fuels WHERE fuels.fuelType=4");
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will update the homefuel price from the returned db value
	 * 
	 * @param fuelprice returned homefuel price
	 */
	public void homefuelorder(ArrayList<ArrayList<Object>> fuelprice) {
		homefuelprice = (Float) fuelprice.get(0).get(0);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				priceforliter_text.setText(String.format("%.02f", homefuelprice));
			}
		});
	}

	/**
	 * This method will update the customerid from the returned db value
	 * 
	 * @param customerid returned customerid price
	 */
	public void recivecustomer(ArrayList<ArrayList<Object>> customerid) {

		customerID = (Integer) customerid.get(0).get(0);
	}

}
