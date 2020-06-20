package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.SalePattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class is the controller of main sale pattern screen, used by marketing
 * agent
 * 
 * @author Yogev
 * @version 0.99
 */
public class MarketingAgentSalesMainController extends AbstractController {
	/**
	 * instance of this controller
	 */
	public static MarketingAgentSalesMainController instance;
//	public static boolean fl = false;
	/**
	 * static sale for used at some controllers by the selected one
	 */
	public static SalePattern sale_selected;

	// Observable list for put the details at the table by specific format
	// (SalePattern)

	private ObservableList<SalePattern> sale_patterns_list = FXCollections.observableArrayList();
	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private TableView<SalePattern> sales_table;

	@FXML
	private TableColumn<SalePattern, String> nameColumn;

	@FXML
	private TableColumn<SalePattern, String> fuelTypeColumn;

	@FXML
	private TableColumn<SalePattern, String> statusColumn;

	@FXML
	private TableColumn<SalePattern, Integer> stationTagColumn;

	@FXML
	private TableColumn<SalePattern, Float> discountColumn;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button add_btn;

	@FXML
	private Button open_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Button refresh_btn;

	@FXML
	private Button statistics_btn;

	@FXML
	private TextField search_txt;

	@FXML
	private Button search_btn;

	/**
	 * switch the screen to add sales form
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void add_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesAddForm.fxml",
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
	 * this method delete sale pattern if sale is clicked and it's not "active" at
	 * the DB after appropriate alert
	 * 
	 * @param event
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		if (sales_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle("ERROR");
			alert3.setHeaderText(null);
			alert3.setContentText("Please select a sale to delete");
			alert3.show();
			return;
		}
		if (sales_table.getSelectionModel().getSelectedItem().getStatus().toLowerCase().equals("active")) {
			Alert alert2 = new Alert(AlertType.ERROR);
			alert2.setTitle("The sale is active");
			alert2.setHeaderText(null);
			alert2.setContentText("Active sale cannot be deleted");
			alert2.show();
			return;
		}
		Alert alert = new Alert(AlertType.WARNING, "Are you sure you delete the sale pattern?", ButtonType.YES,
				ButtonType.NO);
		alert.setTitle("Back");
		alert.setHeaderText(null);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			if (sales_table.getSelectionModel().getSelectedItem().getStatus().equals("Un-active")) {
				// query for delete from DB
				String query1 = "Delete from sale_pattern where salePatternTag = "
						+ sales_table.getSelectionModel().getSelectedItem().getSalePatternTag().toString();
				Message message1 = new Message(MessageType.UPDATEINFO, "MarketingAgentSalesMainController_delete",
						query1);
				MainClientGUI.client.handleMessageFromClientUI(message1);
				// successful alert
				Alert alert1 = new Alert(AlertType.CONFIRMATION);
				alert1.setTitle("Sale deleted");
				alert1.setHeaderText(null);
				alert1.setContentText("The sale pattern deleted successfully");
				alert1.show();
				switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
						"/client/boundry/MarketingAgentMainCustomer.css");
			}
		} else {
			return;
		}

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
		dia.setContentText(
				"Main Sale Pattern Screen\nIn this screen you can choose sale from table,add sale, delete and open\n"
						+ "for full information, click on specific sale and choose an action, in addition you can click"
						+ "on \"Statistics\" for statistic inforamtion");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingAgentSalesMainController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * not have implementation for now
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {

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
	 * switch the scene to the same page for refreshing
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void refresh_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	@FXML
	void sales_btn_clicked(MouseEvent event) {

	}

	/**
	 * this method searches specific sale and put them at local list to show on
	 * table
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void search_btn_clicked(MouseEvent event) {
		ObservableList<SalePattern> found_sales = FXCollections.observableArrayList();
		try {
			if (!search_txt.getText().isEmpty()) {
				for (SalePattern sale : sale_patterns_list) {
					Integer search = new Integer(search_txt.getText());
					if (search <= 0) {
						throw new Exception();
					}
					if (sale.getGasStationTag().toString().startsWith(search.toString())) {
						found_sales.add(sale);
					}
				}
				sales_table.setItems(FXCollections.observableArrayList());
				if (found_sales != null) {
					sales_table.getItems().addAll(found_sales);
				}
			} else {
				sales_table.setItems(sale_patterns_list);
			}
			search_txt.setStyle(null);
		} catch (Exception e) {
			search_txt.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Input entered is not valid please try again");
			alert.show();
			return;
		}
	}

	/**
	 * open statistic data for make sale pattern by the analytic system
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void statistics_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/SaleStatisticsInformationForm.fxml", "/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * initialize the screen, set the user name at the top of the page, and
	 * initialize table with the details of the sales
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		// set at the table
		nameColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("name"));
		fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("fuelType"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("status"));
		stationTagColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, Integer>("gasStationTag"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, Float>("discount"));
		// this query is for get from DB the details of the sales
		String query = "SELECT sp.name , f.fuelName, sp.startHour, sp.endHour, sp.discount,salePatternTag, sp.status, sp.stationTag, sp.description"
				+ " FROM sale_pattern sp, fuels f" + " WHERE sp.fuelType = f.fuelType";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingAgentSalesMainController_initialize_table",
				query);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * open the full report of the specific sale by put the sale at static parameter
	 * 
	 * @param event
	 */
	@FXML
	void open_btn_clicked(MouseEvent event) {
		sale_selected = sales_table.getSelectionModel().getSelectedItem();
		if (sale_selected != null) {
			switchScenes("/client/boundry/MarketingAgentSaleShowForm.fxml",
					"/client/boundry/MarketingAgentMainCustomer.css");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No sale pattern selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select sale pattern");
			alert.show();
			return;
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
	 * this method get the sale's details from DB and put them at list, , than set
	 * them at the table
	 * 
	 * @param sale_pattern all of the sales available
	 */
	public void fill_table_sale_pattern(ArrayList<ArrayList<Object>> sale_pattern) {
		for (ArrayList<Object> row : sale_pattern) {
			sale_patterns_list.add(new SalePattern((String) row.get(0), (String) row.get(1), (String) row.get(2),
					(String) row.get(3), (Float) row.get(4), (Integer) row.get(5), (String) row.get(6),
					(Integer) row.get(7), (String) row.get(8)));
		}
		sales_table.setItems(sale_patterns_list);

	}

}
