package client.common.controllers;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.MainClientGUI;
import client.common.ReportsCEO;
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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * this class is the controller of the Report screen of the CEO. here will be
 * displayed rate discount for the CEO
 *
 * @author Meni
 * @version 0.99
 */
public class CEOReportsPageController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static CEOReportsPageController instance;
	/**
	 * flag that help us with the pop up menu button
	 */
	public static boolean fl = false;
	/**
	 * FileChooser that with him we download the report
	 */

	FileChooser fileChooser = new FileChooser();
	/**
	 * the name of the file that we want to download
	 */

	private static String content = "Income Report ";

	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button reports_btn;

	@FXML
	private Button logout_btn;

	@FXML
	private Button help_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private TableView<ReportsCEO> report_table;

	@FXML
	private TableColumn<ReportsCEO, String> report_type_col;

	@FXML
	private TableColumn<ReportsCEO, String> date_col;

	@FXML
	private TableColumn<ReportsCEO, Integer> from_col;

	/*
	 * @FXML private TableColumn<ReportsCEO, String> comment_col;
	 */

	@FXML
	private Button open_btn;

	@FXML
	private Button delete_btn;

	@FXML
	private Button open_dialog;

	@FXML
	private Button refresh_btn;

	/**
	 * In this method we open a new dialog screen that show us the report that we
	 * choose
	 * 
	 * @param event The event that caused the method to activate
	 */

	@FXML
	void open_dialog_btn_clicked(MouseEvent event) {
		if (report_table.getSelectionModel().getSelectedItem() != null) {
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
			dia.setContentText(report_table.getSelectionModel().getSelectedItem().getComment());
			dia.show();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose Report ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A REPORT!");
			alert.initOwner(MainClientGUI.primaryStage);
			alert.show();
		}
	}

	/**
	 * in this method the CEO delete a report from the screen and from the database
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void delete_btn_clicked(MouseEvent event) {
		if (report_table.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().add(ButtonType.CANCEL);
			alert.getButtonTypes().add(ButtonType.YES);
			alert.setTitle("Delete Repore");
			alert.setContentText(String.format("Do you want to delete this report?"));
			alert.initOwner(MainClientGUI.primaryStage);
			Optional<ButtonType> res = alert.showAndWait();

			if (res.isPresent()) {
				if (res.get().equals(ButtonType.YES)) {
					String quary = "DELETE FROM `stations_reports` WHERE `stations_reports`.`reportID` ="
							+ report_table.getSelectionModel().getSelectedItem().getReportid();
					Message message = new Message(MessageType.UPDATEINFO, "CEOReportsPageController_report_delete",
							quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
					refresh_btn_clicked(null);
				} else {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setTitle("Delete Faild ");
					alert1.setHeaderText(null);
					alert1.initOwner(MainClientGUI.primaryStage);
					alert1.setContentText("The report is not deleted!");
					alert1.show();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose Report ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A REPORT!");
			alert.initOwner(MainClientGUI.primaryStage);
			alert.show();
		}
	}

	/**
	 * this method displayes some guidence for the user about the specific page
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
				"\nReports :\nHere you will find all of the reports that has been sent to you from yours station managers. "
						+ "Download button:\nIf you choose to download, the roprt will download automatic.\n\n"
						+ "Confirm button:\nif you choose to confirm, the discount of this rate will be updated");

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
		Message message = new Message(MessageType.LOGOUT, "CEOReportsPageController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method made the menu button to a pop up button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CEOMenuWelcomePageForm.fxml", "/client/boundry/CEOMenu.css");
	}

	/**
	 * this method is responsible for handling a click in the notification button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/CEONotificationPageForm.fxml", "/client/boundry/CEOMenu.css");
	}

	/**
	 * this method download the report and save it where you want with the name and
	 * the date of the report
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void open_btn_clicked(MouseEvent event) {
		if (report_table.getSelectionModel().getSelectedItem() != null) {
			fileChooser.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
			String filename = report_table.getSelectionModel().getSelectedItem().getReportName() + "_"
					+ report_table.getSelectionModel().getSelectedItem().getDate();
			filename = filename.replaceAll("\\s+", "_");
			filename = filename.replaceAll("\\/+", "_");
			filename = filename.replaceAll("\\:+", "_");
			fileChooser.setInitialFileName(filename);
			File file = fileChooser.showSaveDialog(MainClientGUI.primaryStage);
			try {
				FileWriter fileWriter = new FileWriter(file);
				content = report_table.getSelectionModel().getSelectedItem().getComment();
				fileWriter.write(content);
				fileWriter.close();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Download Failed");
				alert.setHeaderText(null);
				alert.setContentText("Downloading The Report Has Failed!");
				alert.initOwner(MainClientGUI.primaryStage);
				alert.show();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Choose Report ");
			alert.setHeaderText(null);
			alert.setContentText("YOU NEED TO CHOOSE A REPORT!");
			alert.initOwner(MainClientGUI.primaryStage);
			alert.show();
		}
	}

	/**
	 * this method is responsible for refreshing the CEO report page switches to the
	 * same screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void refresh_btn_clicked(MouseEvent event) {

		switchScenes("/client/boundry/CEOReportPageForm.fxml", "/client/boundry/CEOMenu.css");

	}

	/**
	 * this method is responsible for handling a click in the report button it
	 * switches to the correct screen
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void reports_btn_clicked(MouseEvent event) {

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
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());

		report_type_col.setCellValueFactory(new PropertyValueFactory<ReportsCEO, String>("reportName"));
		date_col.setCellValueFactory(new PropertyValueFactory<ReportsCEO, String>("date"));
		from_col.setCellValueFactory(new PropertyValueFactory<ReportsCEO, Integer>("stationNumber"));
		// comment_col.setCellValueFactory(new PropertyValueFactory<ReportsCEO,
		// String>("comment"));
		String quary = "SELECT * FROM stations_reports";
		Message msg = new Message(MessageType.REQUESTINFO, new String("CEOReportsPageController_initialize"), quary);
		MainClientGUI.client.handleMessageFromClientUI(msg);
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
	 * * this method is responsible for filling up the table of the reports. its
	 * recives the reports from the server
	 * 
	 * @param table the new table waiting for the CEO from the database
	 */
	public void return_CEOReports(ArrayList<ArrayList<Object>> table) {
		ObservableList<ReportsCEO> reports = FXCollections.observableArrayList();

		for (ArrayList<Object> row : table) {
			reports.add(new ReportsCEO(read_untill((String) row.get(1)), (String) row.get(3), (Integer) row.get(2),
					(String) row.get(1), (Integer) row.get(0)));

		}

		report_table.setItems(reports);

	}

	/**
	 * in this method we get the name of the report from the full report (we take
	 * only the headline)
	 * 
	 * @param reportname-get the full report
	 * @return res res is the name of the report
	 */
	public String read_untill(String reportname) {
		StringBuilder read = new StringBuilder(reportname);
		StringBuilder res = new StringBuilder();
		int i = 0;
		while (true) {
			if (read.charAt(i) == ':') {
				return res.toString();
			} else {
				res.append(read.charAt(i));
			}
			i++;
		}

	}
}
