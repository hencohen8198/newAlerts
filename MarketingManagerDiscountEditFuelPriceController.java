package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
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
 * this class represents the price edit of fuel and add new price
 * @author Yehonatan
 * @version 0.99
 *
 */

public class MarketingManagerDiscountEditFuelPriceController extends AbstractController {
	/**
	 * an instance of the controller
	 */
	public static MarketingManagerDiscountEditFuelPriceController instance;
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
    private ComboBox<String> FuelTypeComboBox;

    @FXML
    private Label currentPrice_label;

    @FXML
    private TextField newPrice_txt;

    @FXML
    private Button back_btn;


    @FXML
    private Button update_btn;

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
		exitFromScreenAlert("/client/boundry/MarketingManagerDiscountEditFuelPriceForm.fxml",
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
		dia.setContentText("\nHere you will able to change price of specific fuel.\n\n"
				+ "Back button:\nclick on this button if you want to return the last page.\n\n"+"Update button:\nchange the price the fuel");

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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerDiscountEditFuelPriceController_logout_clicked",
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
	 * this method is resposinble to update the price of the fuel
	 * @param event The event that caused the method to activate
	 */

    @FXML
    void update_btn_clicked(MouseEvent event) {
			//String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
			if (FuelTypeComboBox.getSelectionModel().getSelectedItem() != null&&correctInputFloat(newPrice_txt)) {
				// this query update the sale pattern with un-active status in the DB to active
				String query = "UPDATE fuels SET price=\""+newPrice_txt.getText()+"\"" +" WHERE fuels.fuelName=\""
						+FuelTypeComboBox.getSelectionModel().getSelectedItem()+"\"";
				Message message = new Message(MessageType.UPDATEINFO,
						"MarketingManagerDiscountEditFuelPriceController_update_btn_clicked", query);
				MainClientGUI.client.handleMessageFromClientUI(message);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						Alert alert1 = new Alert(AlertType.INFORMATION);
						alert1.setTitle("Request for approve");
						alert1.setHeaderText(null);
						alert1.setContentText("The price updated successfully");
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
		alert.setTitle("Back");
		alert.setHeaderText(null);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			switchScenes(form, css);
		} else {
			return;
		}
	}
    /* this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 * 
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		String query = "SELECT fuels.fuelName FROM fuels";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingManagerDiscountEditFuelPriceController_initialize", query);
		MainClientGUI.client.handleMessageFromClientUI(message);
		FuelTypeComboBox.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if (FuelTypeComboBox.getSelectionModel().getSelectedItem() != null) {
					/**
					 * this query is for to get the price for the selscted fuel
					 */
					String quary = "SELECT fuels.price from fuels"
							+ " where fuels.fuelName=\""
							+ FuelTypeComboBox.getSelectionModel().getSelectedItem().toString() + "\""
							;
					Message message = new Message(MessageType.REQUESTINFO,
							"MarketingManagerDiscountEditFuelPriceController_initialize2", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
				}
			}
		});
	}
	/**
	 * this method add the current price form the DB to label
	 * @param fuels is the data that recived from DB
	 */
	public void fill_fuelprice_Txt(ArrayList<ArrayList<Object>> fuels) {
		Float price=new Float((float)fuels.get(0).get(0));
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				currentPrice_label.setText(price.toString());				
			}
		});
			

		
	}

	/**
	 * this method gets the fuels name and add it to the list and put them in the
	 * list
	 * 
	 * @param fuels get the fuels names form the DB
	 */
	public void fill_fuelTypes_ComboBox(ArrayList<ArrayList<Object>> fuels) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				ObservableList<String> options = FXCollections.observableArrayList();
				for (ArrayList<Object> row : fuels) {
					options.add((String) row.get(0));				
			}
				FuelTypeComboBox.setItems(options);
				
			}
		});
	}	
}
