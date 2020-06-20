package client.common.controllers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.Reports;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the reports screen of the station manager.
 * here will be displayed the reports created, and the option to create new
 * reports. in addition the manager can download,send to the ceo,delete and open
 * the selected report.
 *
 * @author henco
 * @version 0.99
 */
public class StationManagerReportsController extends AbstractController {

	/**
	 * instance of this controller
	 */
	public static StationManagerReportsController instance;
	/**
	 * holds the station number of the station manager
	 */
	public static String station_tag_number;
	/**
	 * an instance of a fileChooser
	 */
	FileChooser fileChooser = new FileChooser();
	/**
	 * hold the string of a report selected
	 */
	private static String content;
	public ArrayList<String> report_array;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button main_btn;

	@FXML
	private Button report_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button station_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Button income_report_btn;

	@FXML
	private Button reserves_report_btn;

	@FXML
	private Button purchases_report_btn;

	@FXML
	private TableView<Reports> report_view;

	@FXML
	private TableColumn<Reports, String> report_type_col;

	@FXML
	private TableColumn<Reports, String> report_date_col;

	@FXML
	private Button delete_report;

	@FXML
	private Button send_btn;

	@FXML
	private Button download_btn;

	@FXML
	private Button open_report;

	/**
	 * this method is responsible for when the download button is clicked. it opens
	 * a filechooser for the user in order to select the download location.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void download_btn_clicked(MouseEvent event) {
		if (report_view.getSelectionModel().getSelectedItem() != null) {
			fileChooser.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
			switch (report_view.getSelectionModel().getSelectedItem().getReportType()) {
			case "Reserves Report":
				fileChooser.setInitialFileName("Reserves-Report");
				break;
			case "Purchases Report":
				fileChooser.setInitialFileName("Purchases-Report");
				break;
			case "Income Report":
				fileChooser.setInitialFileName("Income-Report");
				break;
			}
			File file = fileChooser.showSaveDialog(MainClientGUI.primaryStage);
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(report_view.getSelectionModel().getSelectedItem().getDescription());
				fileWriter.close();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Download Failed");
				alert.setHeaderText(null);
				alert.setContentText("Downloading The Report Has Failed!");
				alert.show();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: DOWNLOADING REPORT FAILED");
			alert.setHeaderText(null);
			alert.setContentText("No Report Has Been Selected");
			alert.show();
		}
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
				"\nHere is the main reports menu.\nYou can produce 3 types of reports:\n\nIncome report - Produce a report with all of the current quarter incomes in your station"
						+ "\nReserves report - Produce an updated report of your station's reservres status."
						+ "\nProduct report - Produce a report with all of your station's fuel consumption\n\n"
						+ "The you may open a report, delete it, download it and sent it to the CEO");
		dia.show();
	}

	/**
	 * this method sends the server a request to create an income report for the
	 * station manager's station.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void income_report_btn_clicked(MouseEvent event) {
		String timeStampMonth = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
		String timeStampYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
		// fileChooser.setInitialFileName("Income-Report");
		Integer currentMonth = new Integer(timeStampMonth.toString());
		Integer currentYear = new Integer(timeStampYear.toString());
		if (currentMonth % 3 != 0) {
			currentMonth = (currentMonth / 3) * 3;
			currentMonth++;
		} else {
			currentMonth = ((currentMonth / 3) - 1) * 3;
			currentMonth++;
		}
		if (currentMonth < 10) {
			String quary = "SELECT fuels.fuelName, SUM(sales.price), COUNT(sales.fuelType), sales.stationTag, stations.stationName FROM fuels, employees, stations, sales WHERE fuels.fuelType = sales.fuelType AND employees.userID = "
					+ MainClientGUI.getUserID()
					+ " AND employees.eid = stations.eid AND stations.stationTag = sales.stationTag AND "
					+ "(sales.date LIKE \"" + currentYear + "/0" + currentMonth + "%\" OR sales.date LIKE \""
					+ currentYear + "/0" + (currentMonth + 1) + "%\" OR " + "sales.date LIKE \"" + currentYear + "/0"
					+ (currentMonth + 2) + "%\") GROUP BY fuelName,sales.stationTag";
			Message message = new Message(MessageType.REQUESTINFO,
					"StationManagerReportsController_income_report_btn_clicked", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
		} else {
			String quary = "SELECT fuels.fuelName , SUM(sales.price), COUNT(sales.fuelType), sales.stationTag, stations.stationName FROM fuels, employees, stations, sales WHERE fuels.fuelType = sales.fuelType AND employees.userID = "
					+ MainClientGUI.getUserID()
					+ " AND employees.eid = stations.eid AND stations.stationTag = sales.stationTag AND "
					+ "(sales.date LIKE \"" + currentYear + "/" + currentMonth + "%\" OR sales.date LIKE \""
					+ currentYear + "/" + (currentMonth + 1) + "%\" OR " + "sales.date LIKE \"" + currentYear + "/"
					+ (currentMonth + 2) + "%\") GROUP BY fuelName,sales.stationTag";
			Message message = new Message(MessageType.REQUESTINFO,
					"StationManagerReportsController_income_report_btn_clicked", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
		}

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
		Message message = new Message(MessageType.LOGOUT, "StationManagerReportsController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * TODO
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void main_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerForm.fxml", "/client/boundry/StationManager.css");
	}

	/**
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerNotificationForm.fxml",
				"/client/boundry/StationManagerNotification.css");
	}

	/**
	 * this method sends the server a request to create an purchases report for the
	 * station manager's station.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void purchases_report_btn_clicked(MouseEvent event) {
		// fileChooser.setInitialFileName("Purchases-Report");
		String quary = "SELECT fuels.fuelName,SUM(sales.price),SUM(sales.quantity),COUNT(sales.saleTag), stations.stationName FROM sales, fuels, stations, employees "
				+ "WHERE employees.userID = " + MainClientGUI.getUserID()
				+ " AND stations.eid = employees.eid AND sales.stationTag = stations.stationTag AND sales.fuelType = fuels.fuelType GROUP BY stations.stationName,fuels.fuelName";
		Message message = new Message(MessageType.REQUESTINFO,
				"StationManagerReportsController_purchases_report_btn_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
		// report_view.getItems().add("Purchases Report:");
	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the corrent screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void report_btn_clicked(MouseEvent event) {

	}

	/**
	 * this method sends the server a request to create an reserves report for the
	 * station manager's station.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void reserves_report_btn_clicked(MouseEvent event) {
		// fileChooser.setInitialFileName("Reserves-Report");
		String quary = "SELECT fuel_reserves.fuelType, fuels.price , fuel_reserves.fuelInventory, fuel_reserves.treshold, fuel_reserves.maxQuantity, fuel_reserves.stationTag, fuel_reserves.reserveTag,fuels.fuelName, stations.stationName FROM fuels,fuel_reserves,employees,stations WHERE employees.userID = "
				+ MainClientGUI.getUserID()
				+ " And employees.eid=stations.eid AND stations.stationTag = fuel_reserves.stationTag AND fuels.fuelType=fuel_reserves.fuelType;";
		Message message = new Message(MessageType.REQUESTINFO,
				"StationManagerReportsController_reserves_report_btn_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method deletes a report from the manager's archive.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_report(MouseEvent event) {
		if (report_view.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Quit application");
			alert.setContentText(String.format("Are You Sure You Want To Delete?"));
			alert.initOwner(MainClientGUI.primaryStage);
			Optional<ButtonType> res = alert.showAndWait();
			if (res.isPresent()) {
				if (res.get().equals(ButtonType.YES)) {
					String quary = "DELETE FROM reports_saved WHERE reportTag = "
							+ report_view.getSelectionModel().getSelectedItem().getReportID();
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerReportsController_delete_report", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					refresh();
				} else
					event.consume();

			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SENDING REPORT FAILED");
			alert.setHeaderText(null);
			alert.setContentText("No Report Has Been Selected");
			alert.show();
		}
	}

	/**
	 * this method opens the report selected from the report table in a dialog
	 * screen.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void open_report(MouseEvent event) {
		if (report_view.getSelectionModel().getSelectedItem() != null) {
			Dialog<String> dia = new Dialog<>();
			Stage stage = (Stage) dia.getDialogPane().getScene().getWindow();
			DialogPane dialogPane = dia.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource("/client/boundry/dialog.css").toExternalForm());
			dialogPane.getStyleClass().add("dialog");
			dia.setTitle("Reports");
			dia.setHeaderText("Station Manager Report");
			dia.setGraphic(new ImageView(this.getClass().getResource("/icons8-business-report-48.png").toString()));
			// Add a custom icon.
			stage.getIcons().add(new Image(this.getClass().getResource("/icons8-business-report-24.png").toString()));
			dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
			dia.setContentText(report_view.getSelectionModel().getSelectedItem().getDescription());
			dia.show();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SENDING REPORT FAILED");
			alert.setHeaderText(null);
			alert.setContentText("No Report Has Been Selected");
			alert.show();
		}
	}

	/**
	 * this method sends to the CEO the report selected
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void send_btn_clicked(MouseEvent event) {
		if (report_view.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Sending Report");
			alert.setContentText(String.format("Are You Sure You Want To Send The Report?"));
			alert.initOwner(MainClientGUI.primaryStage.getOwner());
			Optional<ButtonType> res = alert.showAndWait();

			if (res.isPresent()) {
				if (res.get().equals(ButtonType.CANCEL)) {
					event.consume();
				} else {
					Reports report_selected = report_view.getSelectionModel().getSelectedItem();
					String quary = "INSERT INTO stations_reports (report_file, stationTag, date) VALUES (\""
							+ report_selected.getDescription() + "\",\"" + station_tag_number + "\",\""
							+ report_selected.getDate() + "\");";
					Message message = new Message(MessageType.UPDATEINFO,
							"StationManagerReportsController_send_btn_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.setTitle("Report Sent");
					alert2.setHeaderText(null);
					alert2.setContentText("The Report Has Been Sent To The CEO");
					alert2.show();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR: SENDING REPORT FAILED");
			alert.setHeaderText(null);
			alert.setContentText("No Report Has Been Selected");
			alert.show();
		}
	}

	/**
	 * this method is responsible for handling a click in the station button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void station_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/StationManagerStationForm.fxml", "/client/boundry/StationManagerStation.css");
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
	 * this method converts a global variable of arrayList that holds rows of a
	 * report into a single string.
	 * 
	 * @return a string representation of the report selected
	 */
	private String convert_ListView_to_String() {
		StringBuilder page = new StringBuilder();
		for (String row : report_array) {
			page.append(row + "\n");
		}
		return page.toString();
	}

	/**
	 * TODO
	 */
	void hide_manu() {
		menu_parent.getChildren().remove(report_btn.getParent());
		menu_parent.getChildren().remove(notification_btn.getParent());
		menu_parent.getChildren().remove(station_btn.getParent());
	}

	/**
	 * TODO
	 */
	void show_menu() {
		menu_parent.getChildren().add(report_btn.getParent());
		menu_parent.getChildren().add(notification_btn.getParent());
		menu_parent.getChildren().add(station_btn.getParent());
	}

	/**
	 * this method is activated whenever the quary returns and transformed into and
	 * arraylist. this method is responsible for saving the report to the database
	 * 
	 * @param report arraylist representation of the report
	 */
	public void reserves_report_returned(ArrayList<String> report) {
		report_array = report;
		content = convert_ListView_to_String();
		String quary = "INSERT INTO reports_saved(reportType, stationTag, date, description) VALUES (\"Reserves Report\","
				+ station_tag_number + ",\""
				+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()) + "\",\""
				+ content + "\")";
		Message message = new Message(MessageType.UPDATEINFO,
				"StationManagerReportsController_reserves_report_returned", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * * this method is activated whenever the quary returns and transformed into
	 * and arraylist. this method is responsible for saving the report to the
	 * database
	 * 
	 * @param report     arraylist representation of the report
	 * @param info_found if report returned information
	 */
	public void income_report_returned(ArrayList<String> report, boolean info_found) {
		if (info_found) {
			report_array = report;
			content = convert_ListView_to_String();
			String quary = "INSERT INTO reports_saved(reportType, stationTag, date, description) VALUES (\"Income Report\","
					+ station_tag_number + ",\""
					+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()) + "\",\""
					+ content + "\")";
			Message message = new Message(MessageType.UPDATEINFO,
					"StationManagerReportsController_income_report_returned", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
		} else {
			report_array = report;
			content = convert_ListView_to_String();
			String quary = "INSERT INTO reports_saved(reportType, stationTag, date, description) VALUES (\"Income Report\","
					+ station_tag_number + ",\""
					+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()) + "\",\""
					+ content + "\")";
			Message message = new Message(MessageType.UPDATEINFO,
					"StationManagerReportsController_income_report_returned", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
			Alert alert2 = new Alert(AlertType.WARNING);
			alert2.setTitle("No Info Found");
			alert2.setHeaderText(null);
			alert2.setContentText("No Info Found For This Report");
			alert2.show();
		}

	}

	/**
	 * * this method is activated whenever the quary returns and transformed into
	 * and arraylist. this method is responsible for saving the report to the
	 * database
	 * 
	 * @param report     arraylist representation of the report
	 * @param info_found if report returned information
	 */
	public void purchases_report_returned(ArrayList<String> report, boolean info_found) {
		if (info_found) {
			report_array = report;
			content = convert_ListView_to_String();
			String quary = "INSERT INTO reports_saved(reportType, stationTag, date, description) VALUES (\"Purchases Report\","
					+ station_tag_number + ",\""
					+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()) + "\",\""
					+ content + "\")";
			Message message = new Message(MessageType.UPDATEINFO,
					"StationManagerReportsController_purchases_report_returned", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
		} else {
			report_array = report;
			content = convert_ListView_to_String();
			String quary = "INSERT INTO reports_saved(reportType, stationTag, date, description) VALUES (\"Purchases Report\","
					+ station_tag_number + ",\""
					+ new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()) + "\",\""
					+ content + "\")";
			Message message = new Message(MessageType.UPDATEINFO,
					"StationManagerReportsController_purchases_report_returned", quary);
			MainClientGUI.client.handleMessageFromClientUI(message);
			Alert alert2 = new Alert(AlertType.WARNING);
			alert2.setTitle("No Info Found");
			alert2.setHeaderText(null);
			alert2.setContentText("No Info Found For This Report");
			alert2.show();
		}
	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 * @param location  location of the controller
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
//		if (fl) {
//			hide_manu();
//		}
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		fileChooser.setInitialDirectory(new File("C:\\"));
		fileChooser.setTitle("Open Report");
		report_type_col.setCellValueFactory(new PropertyValueFactory<Reports, String>("reportType"));
		report_date_col.setCellValueFactory(new PropertyValueFactory<Reports, String>("date"));
		String quary = "SELECT * FROM reports_saved WHERE stationTag = " + station_tag_number;
		Message message = new Message(MessageType.REQUESTINFO, "StationManagerReportsController_initialize", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method is responsible for filling up the table with the station
	 * manager's reports that he saved.
	 * 
	 * @param reports from the database of the station manager
	 */
	public void returned_reports(ArrayList<ArrayList<Object>> reports) {
		ObservableList<Reports> reports_list = FXCollections.observableArrayList();

		for (ArrayList<Object> row : reports) {
			reports_list.add(new Reports((String) row.get(0), (String) row.get(2), (Integer) row.get(1),
					(String) row.get(3), (Integer) row.get(4)));
		}
		report_view.setItems(reports_list);
		//TableResizer.autoFitTable(report_view);
	}

	/**
	 * this method reloads the screen.
	 */
	public void refresh() {
		switchScenes("/client/boundry/StationManagerReportsForm.fxml", "/client/boundry/StationManagerReports.css");
	}
}
