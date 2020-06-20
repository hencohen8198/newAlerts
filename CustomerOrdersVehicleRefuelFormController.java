package client.common.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.MainClientGUI;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class takes care about the customer vehicle refuel order. It contains
 * methods to receive inputs from the user, and method to calculte the price
 * after discount considiration Of the customer purchase pattern, the gas
 * station sale and the customer rate discount. The class will calculte the
 * price via the discounts and present it to the order details Moreover, the
 * class will present to customer in subscription of 4 the summrize of sales
 * price till now to the specific current month The class will simulate a
 * vehicle refuel with a 9 liter per second simulation in progress bar till the
 * refuel will end, after it a message of confirmation will send to the customer
 * As well, a receipt will send to the customer when the refuel will end, with
 * the refuel description
 * 
 * @author Itay
 * @version 0.99
 *
 */
public class CustomerOrdersVehicleRefuelFormController extends AbstractController {

	private Timer timer = null;
	private Double sumofsalesOf4sub;
	private Integer salePatternTag, fuelType, stationTag, customerid, rateTypeCustomer;;
	private String saleName, saledescription, fuelname, stationname;
	private boolean flagCheck, returnedSales, returnedRates, returnedPurchasePattern, subscriptionsaleDetailFlag,
			customer4sub;
	private Float fuelprice, discount, total, discountFromRate, discountFromCompanyCount, quantity;
	/**
	 * hold the instance of the object
	 */
	public static CustomerOrdersVehicleRefuelFormController instance;

	@FXML
	private Label vehiclenNumber_label;

	@FXML
	private Label stationtag_label;

	@FXML
	private Label vehiclenNumberredstar_label;

	@FXML
	private Label stationtagredstar_label;

	@FXML
	private Button startrefuelsimulate_btn;

	@FXML
	private Button back_btn;

	@FXML
	private Button clear_btn;

	@FXML
	private Button placeanorder_btn;

	@FXML
	private Label summonth_label;

	@FXML
	private Label sum_label;

	@FXML
	private Label sumAfterPurchase_label;

	@FXML
	private Label summonth_text;

	@FXML
	private Label sum_text;

	@FXML
	private Label sumAfterPurchase_text;

	@FXML
	private Label quantityDetail_text;

	@FXML
	private Label priceperliterDetail_text;

	@FXML
	private Label fueltypeDetail_text;

	@FXML
	private Label saleDetail_text;

	@FXML
	private Label payementmethodDetail_text;

	@FXML
	private Label stationtagDetailtext_text;

	@FXML
	private Label totalDetail_text;

	@FXML
	private Label subscriptionsaleDetail_text;

	@FXML
	private Label exclusivenessDetail_text;

	@FXML
	private TextField vehiclenNumber_text;

	@FXML
	private TextField stationtag_text;

	@FXML
	private TextField quantity_text;

	@FXML
	private Button scan_btn;

	@FXML
	private Label quantityredstar_label;

	@FXML
	private Label quantity_label;

	@FXML
	private Button help_btn;

	@FXML
	private RadioButton cash_radiobutton;

	@FXML
	private RadioButton credit_radiobutton;

	@FXML
	private ProgressBar progressrefule_bar;

	@FXML
	private Label fueltype_label;

	@FXML
	private Label fueltyperedstar_label;

	@FXML
	private ComboBox<String> fuel_choice;

	/**
	 * This method in charge to receive the vehicle number from the user Moreover it
	 * will check the input (that the input will be float will colored the text
	 * field in red).
	 * 
	 * @param event key typed
	 */
	@FXML
	void vehiclenaumber_onkey(KeyEvent event) {
		try {
			Integer temp = new Integer(vehiclenNumber_text.getText());
			if (!vehiclenNumber_text.getText().isEmpty()) {
				if (temp <= 0 || vehiclenNumber_text.getText().length() > 8)
					throw new Exception();
				flagCheck = false;
			}
		} catch (Exception e) {
			vehiclenNumber_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			scan_btn.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			flagCheck = true;
			return;
		}
		vehiclenNumber_text.setStyle(null);
	}

	/**
	 * This method will check at first if the vehicle number and station tag fields
	 * were completed As well, the method will make a query to receive the fuel type
	 * of the requsted vehicle
	 *
	 * @param event mouse clicked
	 */
	@FXML
	void scan_btn_clicked(MouseEvent event) {

		if (vehiclenNumber_text.getText().isEmpty() || flagCheck == true) {
			quantity_label.setDisable(false);
			quantity_text.setDisable(false);
			Alert alert = new Alert(AlertType.ERROR, "NFC Scan Fail");
			alert.setTitle("Failed to scan vehicle details");
			alert.setHeaderText(null);
			alert.show();
			return;
		}
		if (stationtag_text.getText().isEmpty()) {
			Alert alert2 = new Alert(AlertType.ERROR, "Station Location Fail");
			alert2.setTitle("Failed to get find station");
			alert2.setHeaderText(null);
			alert2.show();
			return;
		}
		if (fuel_choice.getSelectionModel().getSelectedItem() == null) {
			Alert alert2 = new Alert(AlertType.ERROR, "Fuel Pump Fail");
			alert2.setTitle("failed to find pump chosen");
			alert2.setHeaderText(null);
			alert2.show();
			return;
		}
		stationtag_text.setDisable(true);
		Message message = new Message(MessageType.REQUESTINFO, "CustomerOrdersVehicleRefuelFormController_getfuel",
				"SELECT fuels.fuelName,fuels.price,fuels.fuelType FROM vehicles,fuels WHERE fuels.fuelType = vehicles.fuelType AND vehicles.vehicleNumber ="
						+ vehiclenNumber_text.getText());
		MainClientGUI.client.handleMessageFromClientUI(message);

	}

	/**
	 * This method will check the customer sales from purchase pattern, rates of the
	 * customer(analayze the discount from it), and the gas station sale by check if
	 * the sale is active and with the same fuel type and in the exact hours time
	 * Father: receivestationtag
	 */
	public void loadsale() {

		Message message2 = new Message(MessageType.REQUESTINFO, "CustomerOrdersVehicleRefuelFormController_getSale",
				"SELECT sale_pattern.discount,sale_pattern.name,sale_pattern.salePatternTag,sale_pattern.startHour,sale_pattern.endHour,sale_pattern.description FROM sale_pattern WHERE sale_pattern.stationTag ="
						+ stationtag_text.getText() + " AND sale_pattern.status=\"active\""
						+ " AND sale_pattern.fuelType=" + fuelType.toString());
		MainClientGUI.client.handleMessageFromClientUI(message2);

		Message message3 = new Message(MessageType.REQUESTINFO,
				"CustomerOrdersVehicleRefuelFormController_getRatesForCustomer",
				"SELECT rates.rateType FROM rates,customer_rates,vehicles WHERE customer_rates.userID=vehicles.userID AND vehicles.vehicleNumber="
						+ vehiclenNumber_text.getText() + " AND customer_rates.rateType=rates.rateType");
		MainClientGUI.client.handleMessageFromClientUI(message3);

		String query = new String(
				"SELECT distinct company_count_discount.discount,purchase_patten.exclusive FROM stations,purchase_patten,company_count_discount,vehicles "
						+ "WHERE (stations.stationTag=" + stationtag_text.getText()
						+ " AND (stations.companyTag= purchase_patten.firstCompanyTag OR"
						+ " stations.companyTag= purchase_patten.secondCompanyTag OR stations.companyTag= purchase_patten.thirdCompanyTag) AND purchase_patten.userID=vehicles.userID"
						+ " AND purchase_patten.companyCount=company_count_discount.companyCount AND vehicles.vehicleNumber ="
						+ vehiclenNumber_text.getText()
						+ ") OR (purchase_patten.exclusive=0 AND purchase_patten.userID=vehicles.userID AND vehicles.vehicleNumber ="
						+ vehiclenNumber_text.getText() + " AND company_count_discount.discount=0);");
		Message message4 = new Message(MessageType.REQUESTINFO,
				"CustomerOrdersVehicleRefuelFormController_getCompanySaleForCustomer", query);
		MainClientGUI.client.handleMessageFromClientUI(message4);
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
		dia.setContentText("\nCustomer simulate vehicle refule:\n"
				+ "\n•In this form you can order refule your vehicle by nfc sensor.\n•Fill the neccesary fileds to complete your purchase.\n"
				+ "•Moreover, select your vehicle number and and the station tag for start refuel.\n•the refuel will start accordingaly to your purchase pattern."
				+ ".\n•As well, after your purchase you will receive the recipet in notifications.");
		dia.show();

	}

	/**
	 * This method will check at first if the whole fields were completed As well,
	 * the method will insert into the db new vehicle refuel order after the user
	 * confirm his order with today's order date Moreover, the method will make the
	 * triger to create a receipt with the require refuel order and insert it to db.
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void placeanorder_btn_clicked(MouseEvent event) {
		if (quantityDetail_text.getText().isEmpty() || totalDetail_text.getText().isEmpty()
				|| payementmethodDetail_text.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Orders table");
			alert.setContentText(String.format("Sorry, missing field"));
			alert.show();
			return;
		}

		Message message = new Message(MessageType.REQUESTINFO,
				"CustomerOrdersVehicleRefuelFormController_reciveCustomerID",
				"SELECT customers.ID FROM customers,vehicles WHERE customers.userID=vehicles.userID AND vehicles.vehicleNumber ="
						+ vehiclenNumber_text.getText());
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * This method will initlize and make the buttons disable and set value to the
	 * progress bar
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		progressrefule_bar.setStyle("-fx-accent: orange");
		fuel_choice.setVisible(false);
		clear_btn.setVisible(false);
		scan_btn.setVisible(false);
		vehiclenNumber_label.setVisible(false);
		vehiclenNumber_text.setVisible(false);
		vehiclenNumberredstar_label.setVisible(false);
		stationtag_label.setVisible(false);
		stationtag_text.setVisible(false);
		stationtagredstar_label.setVisible(false);
		quantity_text.setVisible(false);
		quantity_label.setVisible(false);
		quantityredstar_label.setVisible(false);
		cash_radiobutton.setVisible(false);
		credit_radiobutton.setVisible(false);
		placeanorder_btn.setVisible(false);
		sum_label.setVisible(false);
		sum_text.setVisible(false);
		sumAfterPurchase_label.setVisible(false);
		sumAfterPurchase_text.setVisible(false);
		summonth_label.setVisible(false);
		summonth_text.setVisible(false);
		returnedSales = false;
		returnedRates = false;
		returnedPurchasePattern = false;
		fueltype_label.setVisible(false);
		fueltyperedstar_label.setVisible(false);
		customer4sub = false;
		ObservableList<String> options = FXCollections.observableArrayList("Motor", "Benzin", "Soler");
		fuel_choice.setItems(options);
	}

	/**
	 * This method will set the vehicle fuel type details The method make a query to
	 * receive the station tag As well, if the vehicle didn't in the db, the method
	 * will inform the user of it Father: scan_btn_clicked method
	 * 
	 * @param vehiclefueltype returned vehicle fuel type details
	 */
	public void receivefuel(ArrayList<ArrayList<Object>> vehiclefueltype) {

		if (vehiclefueltype != null) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					fuelname = (String) vehiclefueltype.get(0).get(0);
					fuelprice = (Float) vehiclefueltype.get(0).get(1);
					fuelType = (Integer) vehiclefueltype.get(0).get(2);
					if (!fuelname.equals(fuel_choice.getSelectionModel().getSelectedItem())) {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("Fuel Type Error");
						alert.setContentText(String.format(
								"Sorry, the vehicle's fuel type does not match the pump, please choose the currect pump"));
						alert.initOwner(MainClientGUI.primaryStage.getOwner());
						stationtag_text.setDisable(false);
						vehiclenNumber_text.setDisable(false);
						alert.show();
						return;
					}
					fuel_choice.setDisable(true);
					fueltypeDetail_text.setText(fuelname.toString());
					priceperliterDetail_text.setText(fuelprice.toString());
					Message message = new Message(MessageType.REQUESTINFO,
							"CustomerOrdersVehicleRefuelFormController_getstationtag",
							"SELECT stations.stationTag FROM stations WHERE stations.stationTag ="
									+ stationtag_text.getText());
					MainClientGUI.client.handleMessageFromClientUI(message);

				}
			});
		} else {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Vehicle Number");
					alert.setContentText(String.format("Sorry, the Vehicle dosen't exist"));
					alert.initOwner(MainClientGUI.primaryStage.getOwner());
					alert.show();
					stationtag_text.setDisable(false);
					return;
				}
			});

		}
	}

	/**
	 * This method in charge to receive the stationtag number from the user Moreover
	 * it will check the input (that the input will be Integer will colored the text
	 * field in red).
	 * 
	 * @param event key typed
	 */
	@FXML
	void stationtag_onkey(KeyEvent event) {
		try {
			Integer temp = new Integer(stationtag_text.getText());
			if (!stationtag_text.getText().isEmpty()) {
				if (temp <= 0 || stationtag_text.getText().length() > 11)
					throw new Exception();
				stationtagDetailtext_text.setText(stationtag_text.getText());
			} else
				stationtagDetailtext_text.setText("");

		} catch (Exception e) {
			stationtag_text.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			stationtagDetailtext_text.setText("");
			return;
		}
		stationtag_text.setStyle(null);
	}

	/**
	 * This method in charge to receive the quantity from the user Moreover it will
	 * check the input (that the input will be Float will colored the text field in
	 * red).
	 * 
	 * @param event key typed
	 */
	@FXML
	void quantity_onkey(KeyEvent event) {
		try {
			Float temp = new Float(quantity_text.getText());
			if (!quantity_text.getText().isEmpty()) {
				if (temp <= 0)
					throw new Exception();
				quantityDetail_text.setText(quantity_text.getText() + "L");
			} else
				quantityDetail_text.setText("");
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
	 * This method in charge to lock the selection on cash radio button and not
	 * remove the last selection on this button As well, this method will also
	 * present the payment method detail in the right side of the screen
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void cash_clicked(MouseEvent event) {

		if (cash_radiobutton.isSelected()) {
			payementmethodDetail_text.setText("Cash");
			credit_radiobutton.setSelected(false);
		} else {
			cash_radiobutton.setSelected(true);
			payementmethodDetail_text.setText("Cash");
			credit_radiobutton.setSelected(false);
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
	void credit_clicked(MouseEvent event) {

		if (credit_radiobutton.isSelected()) {
			payementmethodDetail_text.setText("Credit");
			cash_radiobutton.setSelected(false);
		} else {
			credit_radiobutton.setSelected(true);
			payementmethodDetail_text.setText("Credit");
			cash_radiobutton.setSelected(false);
		}

	}

	/**
	 * This method will check the inputs and return true if the calculte price
	 * method could work, else false
	 * 
	 * @return boolen true or false
	 */
	public boolean check() {

		Float quantity2;
		try {
			quantity2 = new Float(quantity_text.getText());
		} catch (Exception e) {
			return false;
		}
		try {
			quantity2 = new Float(stationtag_text.getText());
		} catch (Exception e) {
			return false;
		}
		if (flagCheck == true)
			return false;
		return true;

	}

	/**
	 * this method will calculte the price if the all fields are completed And set
	 * the calculted price into the orderes detail As well, it will calculate the
	 * price considering the whole user sales options.
	 */
	public void calculate_price() {

		Float temp;
		if (returnedSales && returnedRates && returnedPurchasePattern) {
			if (!check()) {
				totalDetail_text.setText("");
				if (sumofsalesOf4sub != null) {
					String after = String.format("%.02f", sumofsalesOf4sub);
					sumAfterPurchase_text.setText(after + "ILS");
				}
				return;
			}

			try {
				quantity = new Float(quantity_text.getText());
			} catch (Exception e) {
				//
				return;
			}
			total = fuelprice * quantity - ((discount / 100) * fuelprice * quantity)
					- ((discountFromRate / 100) * fuelprice * quantity)
					- ((discountFromCompanyCount / 100) * fuelprice * quantity);
			temp = total;
			if (customer4sub) {
				temp += sumofsalesOf4sub.floatValue();
				String after = String.format("%.02f", temp);
				sumAfterPurchase_text.setText(after + "ILS");
			}
			String temp2 = String.format("%.02f", total.floatValue());
			totalDetail_text.setText(temp2 + "ILS");
		}
	}

	/**
	 * This method will receive a gas station sale from the db via the user's
	 * vehicle details and sale details Moreover, the method will present the user
	 * the sale discount perecentage and if there isn't it will set not available
	 * text to the order's details father: loadsale()
	 * 
	 * @param salefuel returned sales from the db
	 */
	public void receivefuelsale(ArrayList<ArrayList<Object>> salefuel) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (salefuel != null) {
					String Today = new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
					Integer date, starthour, endhour;
					date = new Integer(Today);
					starthour = new Integer((String) salefuel.get(0).get(3));
					endhour = new Integer((String) salefuel.get(0).get(4));
					if (endhour < starthour) {
						endhour += 2400;
						if (date < starthour)
							date += 2400;
					}
					if (date >= starthour && date <= endhour) {
						discount = (Float) salefuel.get(0).get(0);
						saleName = (String) salefuel.get(0).get(1);
						salePatternTag = (Integer) salefuel.get(0).get(2);
						saledescription = (String) salefuel.get(0).get(5);
						saleDetail_text.setText(discount.toString() + "%");
					} else {
						saleDetail_text.setText("Not Available");
						discount = (float) 0;
						salePatternTag = 0;
						saleName = "Not Available";
						saledescription = "Not Available";
					}
				} else {
					saleDetail_text.setText("Not Available");
					discount = (float) 0;
					salePatternTag = 0;
					saleName = "Not Available";
					saledescription = "Not Available";
				}
				quantity_text.setVisible(true);
				quantity_label.setVisible(true);
				quantityredstar_label.setVisible(true);
				returnedSales = true;
				calculate_price();
			}
		});

	}

	/**
	 * This method will receive a rate discount from the db via the user details
	 * Moreover, the method will present the user the rate sale discount perecentage
	 * and if there isn't it will set not available text to the order's details
	 * father: loadsale()
	 * 
	 * @param ratefuel returned rate discount from the db
	 */
	public void receiveratesale(ArrayList<ArrayList<Object>> ratefuel) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				Message msg = new Message(MessageType.REQUESTINFO,
						"CustomerOrdersVehicleRefuelFormController_getratestable",
						"SELECT rateType,discoundPercent FROM rates");
				MainClientGUI.client.handleMessageFromClientUI(msg);

				if (ratefuel == null) {

					subscriptionsaleDetail_text.setText("Not Available");
					rateTypeCustomer = 0;
					return;
				} else {
					rateTypeCustomer = (Integer) ratefuel.get(0).get(0);
				}

			}
		});

	}

	/**
	 * This method will receive a purchase pattern discount from the db via the user
	 * details Moreover, the method will present the user the purchase pattern sale
	 * discount perecentage and if there isn't it will set not available text to the
	 * order's details If the station doesn't fit to the user purchase pattern the
	 * method will inform it father: loadsale()
	 * 
	 * @param companysalefuel returned purchase pattern discount from the db
	 */
	public void receivecompanyfuelsale(ArrayList<ArrayList<Object>> companysalefuel) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				subscriptionsaleDetailFlag = false;
				if (companysalefuel != null) {
					if ((Integer) companysalefuel.get(0).get(1) == 1) {
						discountFromCompanyCount = (Float) companysalefuel.get(0).get(0);
						// subscriptionsaleDetail_text.setText(discountFromCompanyCount.toString() +
						// "%");
						exclusivenessDetail_text.setText(discountFromCompanyCount.toString() + "%");
					} else {
						discountFromCompanyCount = (float) 0;
						// subscriptionsaleDetail_text.setText("Not Available");
						exclusivenessDetail_text.setText("Not Available");

					}
				} else {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Vehicle refule");
					alert.setContentText(String.format(
							"Sorry, You can not refuel in a station that doesn't fit your exclusive subscription"));
					alert.initOwner(MainClientGUI.primaryStage.getOwner());
					alert.show();
					subscriptionsaleDetailFlag = true;
					quantity_text.setVisible(false);
					quantity_label.setVisible(false);
					quantityredstar_label.setVisible(false);
					cash_radiobutton.setVisible(false);
					credit_radiobutton.setVisible(false);
					placeanorder_btn.setVisible(false);
					fueltypeDetail_text.setText("");
					payementmethodDetail_text.setText("");
					priceperliterDetail_text.setText("");
					quantityDetail_text.setText("");
					saleDetail_text.setText("");
					stationtagDetailtext_text.setText("");
					subscriptionsaleDetail_text.setText("");
					exclusivenessDetail_text.setText("");
					totalDetail_text.setText("Can't give service!");
					vehiclenNumber_text.clear();
					stationtag_text.clear();
					vehiclenNumber_text.setDisable(false);
					stationtag_text.setDisable(false);
					return;
				}
				returnedPurchasePattern = true;
				calculate_price();
			}

		});

	}

	/**
	 * This method will recive the station tag and will expose to the user the
	 * quantity and placeNorder buttons If the station doesn't exist, it will inform
	 * the user and not let him countiune in the refuel process father: receivefuel
	 * 
	 * @param stationtag returned stationtag number from the db
	 */
	public void receivestationtag(ArrayList<ArrayList<Object>> stationtag) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (stationtag != null) {
					stationTag = (Integer) stationtag.get(0).get(0);
					vehiclenNumber_text.setDisable(true);
					cash_radiobutton.setVisible(true);
					credit_radiobutton.setVisible(true);
					placeanorder_btn.setVisible(true);
					loadsale();

				}

				else {
					stationtag_text.setDisable(false);
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Station Tag");
					alert.setContentText(String.format("Sorry, the Station dosen't exist"));
					alert.initOwner(MainClientGUI.primaryStage.getOwner());
					alert.show();
					return;
				}

			}
		});

	}

	/**
	 * This method will receive the customerID and update it to the global variable
	 * As well, the method will make a query to recive that the input of the quntity
	 * isn't higher than the max quantity reserve in the station father:
	 * placeanorder_btn_clicked
	 * 
	 * @param customerID returned customerid number from the db
	 */
	public void receivecustomerID(ArrayList<ArrayList<Object>> customerID) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				customerid = (Integer) customerID.get(0).get(0);
				Message message1 = new Message(MessageType.REQUESTINFO,
						"CustomerOrdersVehicleRefuelFormController_checkfuelreserveMaxquantity",
						"SELECT fuel_reserves.fuelInventory FROM fuel_reserves WHERE fuel_reserves.fuelType="
								+ fuelType.toString() + " AND fuel_reserves.stationTag=" + stationTag.toString()
								+ " AND fuel_reserves.maxQuantity>" + quantity.toString());
				MainClientGUI.client.handleMessageFromClientUI(message1);
			}
		});

	}

	/**
	 * This method will receive the fuel reserve Inventory If the amount of quantity
	 * is higher than the fuel reserve Inventory in the station, the method will
	 * inform the user and won't let him countinue the refuel process As well, the
	 * method will make a query to set the order after the user has confirmed it
	 * father: receivecustomerID
	 * 
	 * @param checkfuelreserveInventory returned customerid number from the db
	 */
	public void receivefuelreserveInventory(ArrayList<ArrayList<Object>> checkfuelreserveInventory) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				int credOrcash;
				if (checkfuelreserveInventory == null) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Fuel Resereve");
					alert.setContentText(String.format("Sorry, there is not enough fuel"));
					alert.initOwner(MainClientGUI.primaryStage.getOwner());
					alert.show();
					return;

				} else {
					if (credit_radiobutton.isSelected())
						credOrcash = 1;
					else
						credOrcash = 0;
					Message message = new Message(MessageType.UPDATEINFO,
							"CustomerOrdersVehicleRefuelFormController_addneworder",
							"INSERT INTO sales(date,fuelType,quantity,price,stationTag,customerID,paymentMethod,sale_patternID) VALUES ("
									+ "\""
									+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(
											Calendar.getInstance().getTime())
									+ "\"," + fuelType.toString() + "," + quantity.toString() + "," + total.toString()
									+ "," + stationTag.toString() + "," + customerid.toString() + "," + credOrcash + ","
									+ salePatternTag.toString() + ")");

					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Vehicle orders table");
					alert.setContentText(String.format("Place an order?"));
					Optional<ButtonType> res = alert.showAndWait();
					if (res.get() == ButtonType.CANCEL)
						return;
					else
						MainClientGUI.client.handleMessageFromClientUI(message);

					startfueling(quantity);
				}

			}
		});

	}

	/**
	 * This method will set a timer to start and schedule with the start fueling
	 * procces As well, the method will make a query to sub the requsted quantity
	 * from the specific station
	 * 
	 * @param quantity2 The quantity of the vehicle refule
	 */
	protected void startfueling(Float quantity2) {

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				if (progressrefule_bar.getProgress() < 1)
					progressFuel(quantity2);
				else {
					Message message2 = new Message(MessageType.UPDATEINFO,
							"CustomerOrdersVehicleRefuelFormController_subfuelreserve",
							"UPDATE fuel_reserves SET fuel_reserves.fuelInventory=fuel_reserves.fuelInventory-"
									+ quantity.toString() + " WHERE fuel_reserves.fuelType=" + fuelType.toString()
									+ " AND fuel_reserves.stationTag=" + stationTag.toString());
					MainClientGUI.client.handleMessageFromClientUI(message2);
					timer.cancel();
				}

			}
		};
		timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, 100);

	}

	/**
	 * This method will receive the fuel reserve Max quantity. If the amount of
	 * quantity is higher than the max quantity in the station, the method will
	 * inform the user and won't let him countinue the refuel process As well, the
	 * method will make a query to recive that the input of the requsted quantity
	 * isn't higher than the fuel reserve in the station father: receivecustomerID
	 * 
	 * @param checkfuelreserveMaxquantity returned fuel reserve max quantity of the
	 *                                    station number from the db
	 */
	public void receivefuelreserveMaxquantity(ArrayList<ArrayList<Object>> checkfuelreserveMaxquantity) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Message message;
				if (checkfuelreserveMaxquantity == null) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Fuel Resereve");
					alert.setContentText(String.format("Sorry, you requsted more than the Max quantity"));
					alert.initOwner(MainClientGUI.primaryStage.getOwner());
					alert.show();
					return;

				}
				message = new Message(MessageType.REQUESTINFO,
						"CustomerOrdersVehicleRefuelFormController_checkfuelreserveInventory",
						"SELECT fuel_reserves.fuelInventory FROM fuel_reserves WHERE fuel_reserves.fuelType="
								+ fuelType.toString() + " AND fuel_reserves.stationTag=" + stationTag.toString()
								+ " AND fuel_reserves.fuelInventory>" + quantity.toString());
				MainClientGUI.client.handleMessageFromClientUI(message);
			}
		});

	}

	/**
	 * This method will set the message for the user that the refuel has finished
	 * after the 9 liter per second time As well, the method will make a query to
	 * recive details about the station to make the receipt for the customer father:
	 * startfueling
	 */
	public void updateOrder() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.getButtonTypes().remove(ButtonType.CANCEL);
				alert.setTitle("Fuel Order");
				alert.setContentText(String.format("The order has been placed"));
				alert.initOwner(MainClientGUI.primaryStage.getOwner());
				alert.show();

				Message msg = new Message(MessageType.REQUESTINFO,
						"CustomerOrdersVehicleRefuelFormController_recivestationname",
						"SELECT stations.location,stations.stationName FROM stations WHERE stations.stationTag="
								+ stationTag);
				MainClientGUI.client.handleMessageFromClientUI(msg);

			}
		});

	}

	/**
	 * This method recive the station details from the db and arrange it to receipt
	 * As well, the method make a query to insert the receipt to the notifications
	 * table and clear use clear method when done to clear the refuel simulate
	 * fields. father: updateOrder
	 * 
	 * @param stationname2 the data from the DB
	 */
	public void recivestationname(ArrayList<ArrayList<Object>> stationname2) {

		stationname = (String) stationname2.get(0).get(0);
		stationname += ", " + (String) stationname2.get(0).get(1);
		StringBuilder description = new StringBuilder();
		description.append("The order has been placed on: ");
		description.append(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		description.append("\n\n");
		description.append("The quantity is: ");
		description.append(quantityDetail_text.getText());
		description.append("\n\n");
		description.append("The Fuel Type: ");
		description.append(fuelname);
		description.append("\n\n");
		description.append("The Vehicle number: ");
		description.append(vehiclenNumber_text.getText());
		description.append("\n\n");
		description.append("The ReFuel price is: ");
		description.append(total);
		description.append("\n\n");
		description.append("StationTag: ");
		description.append(stationtagDetailtext_text.getText());
		description.append("\n\n");
		description.append("The Station details: ");
		description.append(stationname.toString());
		description.append("\n\n");
		description.append("The Gas Station Sale percent: ");
		if (saleDetail_text.getText().equals("Not Available")) {
			description.append("Not Available");
		} else {
			description.append(saleDetail_text.getText());
			description.append("\n\n");
			description.append("The Sale name: ");
			description.append(saleName);
			description.append("\n\n");
			description.append("The Sale description: ");
			description.append(saledescription);
		}
		description.append("\n\n");
		Message message3 = new Message(MessageType.UPDATEINFO,
				"CustomerOrdersVehicleRefuelFormController__addnewnotification",
				"INSERT INTO customer_notifications(customerID,topic,date,description,statusNew) VALUES (" + customerid
						+ ",\"Vehicle refule receipt\"," + "\""
						+ new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + "\"," + "\""
						+ description.toString() + "\",1)");
		MainClientGUI.client.handleMessageFromClientUI(message3);
		clear_clicked(null);

	}

	/**
	 * This method will receive the rates for the customer and set the discounts
	 * from it by the customer rates As well, the method will make a query to recive
	 * the sum of 4 type subscription custmer of the current month, and present it
	 * to the screen. father: receiveratesale
	 * 
	 * @param ratestable returned customer rate from db
	 */
	public void reciveratestable(ArrayList<ArrayList<Object>> ratestable) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				switch (rateTypeCustomer) {

				case 1:
					discountFromRate = (Float) ratestable.get(0).get(1);
					customer4sub = false;
					break;
				case 2:
					discountFromRate = (Float) ratestable.get(1).get(1) + (Float) ratestable.get(0).get(1);
					customer4sub = false;
					break;
				case 3:
					discountFromRate = (Float) ratestable.get(2).get(1) + (Float) ratestable.get(0).get(1)
							+ (Float) ratestable.get(1).get(1);
					customer4sub = false;
					break;
				case 4:
					discountFromRate = (Float) ratestable.get(3).get(1) + (Float) ratestable.get(2).get(1)
							+ (Float) ratestable.get(0).get(1) + (Float) ratestable.get(1).get(1);
					if (!subscriptionsaleDetailFlag) {
						sum_label.setVisible(true);
						sum_text.setVisible(true);
						sumAfterPurchase_label.setVisible(true);
						sumAfterPurchase_text.setVisible(true);
						summonth_label.setVisible(true);
						summonth_text.setVisible(true);
						customer4sub = true;
						summonth_text.setText(new SimpleDateFormat("MM/yyyy").format(Calendar.getInstance().getTime()));
						credit_radiobutton.setSelected(true);
						payementmethodDetail_text.setText("Credit");
						cash_radiobutton.setDisable(true);
						credit_radiobutton.setDisable(true);
						Message message = new Message(MessageType.REQUESTINFO,
								"CustomerOrdersVehicleRefuelFormController_receiveSumOfSalesOf4sub",
								"SELECT sum(sales.price) FROM sales,customers,vehicles WHERE sales.customerID=customers.ID AND customers.userID= vehicles.userID AND vehicles.vehicleNumber ="
										+ vehiclenNumber_text.getText() + " AND sales.date LIKE \""
										+ new SimpleDateFormat("yyyy/MM").format(Calendar.getInstance().getTime())
										+ "%\"");
						MainClientGUI.client.handleMessageFromClientUI(message);

					}
					break;
				}
				if (!subscriptionsaleDetailFlag)
					subscriptionsaleDetail_text.setText(discountFromRate.toString() + "%");
				returnedRates = true;
				calculate_price();

			}
		});

	}

	/**
	 * This method will present the current sales sum of the customer from rate 4 to
	 * the screen, if none so it will present 0
	 * 
	 * @param receivesumofsalesOf4sub returned customer rate4 current sales sum from
	 *                                db
	 */
	public void receivesumofsalesOf4sub(ArrayList<ArrayList<Object>> receivesumofsalesOf4sub) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if (receivesumofsalesOf4sub == null || receivesumofsalesOf4sub.get(0).get(0) == null) {
					sum_text.setText("0");
					sumAfterPurchase_text.setText("0");
					sumofsalesOf4sub = 0.0;
				} else {
					String before = String.format("%.02f", (Double) receivesumofsalesOf4sub.get(0).get(0));
					sumofsalesOf4sub = (Double) receivesumofsalesOf4sub.get(0).get(0);
					sum_text.setText(before + "ILS");
					sumAfterPurchase_text.setText(before + "ILS");
					customer4sub = true;

				}

			}
		});

	}

	/**
	 * This method will apply after the user has pressed on the cutton star simulate
	 * refule The method will expose the all missing fields to complete the refuel
	 * process
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void startrefuelsimulate_btn_clicked(MouseEvent event) {

		clear_btn.setVisible(true);
		scan_btn.setVisible(true);
		fuel_choice.setVisible(true);
		vehiclenNumberredstar_label.setVisible(true);
		vehiclenNumber_label.setVisible(true);
		vehiclenNumber_text.setVisible(true);
		stationtagredstar_label.setVisible(true);
		stationtag_label.setVisible(true);
		stationtag_text.setVisible(true);
		fueltype_label.setVisible(true);
		fueltyperedstar_label.setVisible(true);

	}

	/**
	 * This method will apply after mouse clicked on back button, and will present
	 * the user The login Form form in the system.
	 *
	 * @param event mouse clicked
	 * 
	 */
	@FXML
	void back_clicked(MouseEvent event) {
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");
	}

	/**
	 * This method will clear the whole fileds after user pressed on clear button
	 * Moreover, the method will hide the place an order button and quantity labels
	 * and text fileds
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void clear_clicked(MouseEvent event) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				vehiclenNumber_text.clear();
				stationtag_text.clear();
				quantity_text.clear();
				sum_label.setVisible(false);
				sum_text.setVisible(false);
				sumAfterPurchase_label.setVisible(false);
				sumAfterPurchase_text.setVisible(false);
				summonth_label.setVisible(false);
				summonth_text.setVisible(false);
				vehiclenNumber_text.setDisable(false);
				stationtag_text.setDisable(false);
				fueltypeDetail_text.setText("");
				payementmethodDetail_text.setText("");
				priceperliterDetail_text.setText("");
				quantityDetail_text.setText("");
				saleDetail_text.setText("");
				stationtagDetailtext_text.setText("");
				subscriptionsaleDetail_text.setText("");
				exclusivenessDetail_text.setText("");
				totalDetail_text.setText("");
				quantity_text.setVisible(false);
				quantity_label.setVisible(false);
				quantityredstar_label.setVisible(false);
				cash_radiobutton.setVisible(false);
				credit_radiobutton.setVisible(false);
				placeanorder_btn.setVisible(false);
				progressrefule_bar.setProgress(0);
				fuel_choice.setValue(null);
				fuel_choice.setDisable(false);

			}
		});

	}

	/**
	 * This method will calculate and will updat the progress bar that simulate the
	 * vehicle refule(9 liter per second)
	 * 
	 * @param quantity the refuel quantity
	 */
	public void progressFuel(Float quantity) {
		progressrefule_bar.setProgress(progressrefule_bar.getProgress() + (Double) ((1.0 / quantity) * (9.0 / 10.0)));
	}

}
