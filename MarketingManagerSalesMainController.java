package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.SalePattern;
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
import javafx.collections.FXCollections;

/**
 * This class is the controller of main sale pattern screen, used by marketing
 * manager, displays all the sale pattern exist
 * 
 * @author yehonatan
 * @version 0.99
 */
public class MarketingManagerSalesMainController extends AbstractController {
	/**
	 * an instance of this controller
	 */
	public static MarketingManagerSalesMainController instance;
//	public static boolean fl = false;
	/**
	 * static sale for some controller by the selected one
	 */
	public static SalePattern sale_selected;
	/**
	 * Observable list for put the details at the table by specific format
	 * (SalePattern)
	 */
	public ObservableList<SalePattern> sale_patterns_list = FXCollections.observableArrayList();
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
	private Button add_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Button activiate_btn;
	@FXML
	private Button deactivate_btn;
	@FXML
	private Button open_btn;
	@FXML
	private TextField search_txt;
	@FXML
	private Button statistics_btn;
	@FXML
	private Button search_btn;

	/**
	 * this method is responsible for handling a activate button
	 *
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void activiate_btn_clicked(MouseEvent event) {
		sale_selected = sales_table.getSelectionModel().getSelectedItem();

		if (sale_selected.getStatus().equals("Un-active")) {
			for (SalePattern salePattern : sales_table.getItems()) {
				if (sale_selected.getGasStationTag().equals(salePattern.getGasStationTag())) {
					if (salePattern.getStatus().equals("active")) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Cant activted");
						alert.setHeaderText(null);
						alert.setContentText("There is a sale already active in this gas station");
						alert.show();
						return;
					}

				}

			}
			// this query update the sale pattern with un-active status in the DB to active
			String query = "UPDATE sale_pattern SET sale_pattern.status=\"active\"  WHERE sale_pattern.salePatternTag="
					+ sale_selected.getSalePatternTag();
			Message message = new Message(MessageType.UPDATEINFO,
					"MarketingManagerSalesMainController_activiate_btn_clicked", query);
			MainClientGUI.client.handleMessageFromClientUI(message);
		}

		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cant ativted");
			alert.setHeaderText(null);
			alert.setContentText("This sale is already active");
			alert.show();
			return;
		}
		switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	@FXML
	void add_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerSaleAddForm.fxml", "/client/boundry/MarketingManagerMain.css");
	}

	/**
	 * this method is responsible for handling a deactivate button
	 * 
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void deactivate_btn_clicked(MouseEvent event) {
		sale_selected = sales_table.getSelectionModel().getSelectedItem();
		if (sale_selected.getStatus().equals("active")) {
			// this query update the sale pattern with active status in the DB to
			// un-active
			String query = "UPDATE sale_pattern SET sale_pattern.status=\"Un-active\"  WHERE sale_pattern.salePatternTag="
					+ sale_selected.getSalePatternTag();
			Message message = new Message(MessageType.UPDATEINFO,
					"MarketingManagerSalesMainController_deactivate_btn_clicked", query);
			MainClientGUI.client.handleMessageFromClientUI(message);
		}

		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cant decativted");
			alert.setHeaderText(null);
			alert.setContentText("This sale is already Un-active");
			alert.show();
			return;
		}
		switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is for search a specific sale pattern by station tag.
	 * 
	 * @param event The event that caused the method to activate
	 * 
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
	 * open the full report of the specific sale by put the sale at static parameter
	 * 
	 * @param event event The event that caused the method to activate
	 */
	@FXML
	void open_btn_clicked(MouseEvent event) {
		sale_selected = sales_table.getSelectionModel().getSelectedItem();
		if (sale_selected != null) {
			switchScenes("/client/boundry/MarketingManagerSalesShowForm.fxml",
					"/client/boundry/MarketingManagerMain.css");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No sale pattern selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select sale pattern");
			alert.initOwner(MainClientGUI.primaryStage);
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
		switchScenes("/client/boundry/MarketingManagerSaleStatisticsInformationForm.fxml",
				"/client/boundry/MarketingManagerMain.css");
	}

	/**
	 * this method delete sale pattern if sale is clicked and it's not "active" at
	 * the DB after appropriate alert
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		sale_selected = sales_table.getSelectionModel().getSelectedItem();
		if (sale_selected != null) {
			if (sale_selected.getStatus().equals("Un-active")) {
				Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to delete?", ButtonType.YES,
						ButtonType.NO);
				alert.setTitle("Back");
				alert.setHeaderText(null);
				alert.showAndWait();
				if (alert.getResult() == ButtonType.YES) {
					String query = "DELETE FROM sale_pattern WHERE sale_pattern.salePatternTag="
							+ sale_selected.getSalePatternTag();
					Message message = new Message(MessageType.UPDATEINFO,
							"MarketingManagerSalesMainController_delete_btn_clicked", query);
					MainClientGUI.client.handleMessageFromClientUI(message);
					Alert alert1 = new Alert(AlertType.CONFIRMATION);
					alert1.setTitle("Sale deleted");
					alert1.setHeaderText(null);
					alert1.setContentText("The sale pattern deleted successfully");
					alert1.show();
					switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml",
							"/client/boundry/MarketingManagerMain.css");
				} else {
					return;
				}
			} else {
				// return;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("The sale is active");
				alert2.setHeaderText(null);
				alert2.setContentText("Active sale cannot be deleted");
				alert2.show();
				return;
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose sale");
			alert.setHeaderText(null);
			alert.setContentText("Please choose sale");
			alert.show();
			return;
		}

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
				"\nHere you will able to watch all of your sales pattern information, like station tag status, name and start hour, end hour.\n\n"
						+ "Activate button:\nHere you be able to change sale pattern status from Un-active to active.\n\n"
						+ "Deactivate button:\nHere you be able to change sale pattern status from active to Un-active\n\n"
						+ "Open button:\nAfter click this button the full details of the selected sale pattern\n\n"
						+ "statistics button:\nWill open statistic information\n\n"
						+ "Delete button:\nHere you will able to delete the selected sale pattern\n\n"
						+ "Add button:\nclick this button to add a new sale pattern");
		dia.show();
	}

	/**
	 * this method is activated when the server has successfully logged out the
	 * user. then the screen is switched to the main login screen
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerSalesMainController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
				"/client/boundry/MarketingManagerMain.css");
	}

	/**
	 * this method is responsible for handling a click in the sales button and it
	 * switch between the forms
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
		switchScenes("/client/boundry/MarketingManagerSalesMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * initialize the screen, set the user name at the top of the page, and
	 * initialize table with the details of the sales
	 * 
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		sale_selected = null;
		nameColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("name"));
		fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("fuelType"));
//		startColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("startHour"));
//		endColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("endHour"));
//		saleIDColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, Integer>("salePatternTag"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("status"));
		stationTagColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, Integer>("gasStationTag"));
		discountColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, Float>("discount"));
//		descriptionColumn.setCellValueFactory(new PropertyValueFactory<SalePattern, String>("description"));
		String query = "SELECT sp.name , f.fuelName, sp.startHour, sp.endHour, sp.discount,salePatternTag, sp.status, sp.stationTag, sp.description"
				+ " FROM sale_pattern sp, fuels f" + " WHERE sp.fuelType = f.fuelType";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingManagerSalesMainController_initialize", query);
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
	 * this method get the sales patterns and add them to the list and put to the
	 * list
	 * 
	 * @param sale_pattern get the sale pattern details from DB for initialize
	 */
	public void fill_table_sale_pattern(ArrayList<ArrayList<Object>> sale_pattern) {
		for (ArrayList<Object> row : sale_pattern) {
			sale_patterns_list.add(new SalePattern((String) row.get(0), (String) row.get(1), (String) row.get(2),
					(String) row.get(3), (Float) row.get(4), (Integer) row.get(5), (String) row.get(6),
					(Integer) row.get(7), (String) row.get(8)));
		}
		sales_table.setItems(sale_patterns_list);
//		TableResizer.autoFitTable(sales_table);
	}

}
