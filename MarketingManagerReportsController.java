package client.common.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.MainClientGUI;
import client.common.CharacterizationInfo;
import client.common.CommentsInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import message_info.Message;
import message_info.MessageType;

/**
 * this class represents the marketing manager reports\ there are two kind of
 * reports comments report for sale pattern periodic characterization report
 * 
 * @author Yehonatan
 * @version 0.99
 * 
 */
public class MarketingManagerReportsController extends AbstractController {
	/**
	 * an instance of marketing manager reports controller
	 */
	public static MarketingManagerReportsController instance;
	/**
	 * the value if the report created
	 */
	private static boolean report_created = false;
	/**
	 * save the name of the chosen sale pattern
	 */
	public static String sale_PatrrenName;
	/**
	 * create file to save the report for download
	 */
	FileChooser fileChooser = new FileChooser();
	/**
	 * save the text of the report and use to write it in the file
	 */
	private static String content;
	/**
	 * list that will hold rows in the characterization table
	 */
	public ObservableList<CharacterizationInfo> char_report;
	/**
	 * list that will hold rows in the comments table
	 */
	public ObservableList<CommentsInfo> comment_report;
	/**
	 * flag to know what table to open when the user chooses to open a table for a
	 * report
	 */
	private static boolean report = false;

	private Series<String, Number> series;

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
	private Button comments_report_btn;

	@FXML
	private ListView<String> list_view;

	@FXML
	private Button periodic_characterization_report_btn;

	@FXML
	private Button download_report_btn;

	@FXML
	private ComboBox<String> salesPatternComboBox;

	@FXML
	private TextField search_customer_txt;

	@FXML
	private Button search_btn;

	@FXML
	private Button open_btn;

	@FXML
	private Button open_diagram_btn;

	/**
	 * open charactize period report diagram
	 * 
	 * @param event mouse clicked
	 */
	@FXML
	void open_diagram_btn_clicked(MouseEvent event) {

		if (series != null) {

			Dialog<String> dia = new Dialog<>();

			Stage stage = (Stage) dia.getDialogPane().getScene().getWindow();
			DialogPane dialogPane = dia.getDialogPane();
			dia.setTitle("Diagram");
			// defining the axes
			final CategoryAxis xAxis = new CategoryAxis();
			final NumberAxis yAxis = new NumberAxis();
			xAxis.setLabel("Gas stations");
			yAxis.setLabel("Purchases");
			// creating the chart
			final BarChart<String, Number> barchart = new BarChart<String, Number>(xAxis, yAxis);
			barchart.setTitle("Customers Purchases Diagram");
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(MainClientGUI.primaryStage);
			VBox dialogVbox = new VBox(20);
			barchart.getData().add(series);
			Scene dialogScene = new Scene(barchart, 800, 600);
			dialog.setScene(dialogScene);
			dialog.getIcons().add(new Image(this.getClass().getResource("/icons8-bar-chart-64.png").toString()));
			dialog.setTitle("Gas stations diagram");
			dialog.show();
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Diagram error");
			alert.setContentText(String.format("Sorry, there isn't any report"));
			alert.show();
			return;
		}

	}

	/**
	 * this method will set the diagram details to the global attribute, it will
	 * recive their details from the UpdateUIController
	 * 
	 * @param series2 returned value
	 */

	public void setdiagram(Series<String, Number> series2) {
		series = series2;

	}

	/**
	 * method that handles a request to open a table of a report created it will
	 * open a new screen with the report's details
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void open_btn_clicked(MouseEvent event) {
		if ((report_created)) {
			Parent root = null;
			CharacterizationController controller;
			CommentsController commentsController;
			if (report) {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/client/boundry/CharacterizationForm.fxml"));
					root = loader.load();
					Stage stage = new Stage();
					stage.setTitle("Characterization Report Table");
					stage.setScene(new Scene(root, 450, 450));
					stage.show();
					stage.setResizable(false);
					stage.setWidth(685);
					stage.setHeight(654);
					stage.getIcons().add(new Image(this.getClass().getResource("/icons8-table-100.png").toString()));
					CharacterizationController.instance
							.add_to_table(MarketingManagerReportsController.instance.char_report);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/client/boundry/CommentsForm.fxml"));
					root = loader.load();
					Stage stage = new Stage();
					stage.setTitle(
							"Comments Report Table - " + salesPatternComboBox.getSelectionModel().getSelectedItem());
					stage.setScene(new Scene(root, 450, 450));
					stage.show();
					stage.setResizable(false);
					stage.setWidth(685);
					stage.setHeight(654);
					CommentsController.instance.add_to_table(MarketingManagerReportsController.instance.comment_report);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Open Failed");
			alert.setHeaderText(null);
			alert.setContentText("No report bas been created");
			alert.show();
		}
	}

	/**
	 * this method is for create a comment reports
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void comments_report_btn_clicked(MouseEvent event) {
		open_diagram_btn.setVisible(false);
		report = false;
		salesPatternComboBox.setVisible(true);
		salesPatternComboBox.getSelectionModel().clearSelection();
		//fileChooser.setInitialFileName("Comments_Report");
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
	 * this method displays some guidance for the user about the page
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
				"\nIn this page you will be able to create comments report for sale or periodic characterization\nSearch button:\nHere you will able to search specific customer the choosen request that it status is approved or rejected."
						+ "\n\nDownload button:click on this button download the report to your computer\n\n");
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
		Message message = new Message(MessageType.LOGOUT, "MarketingManagerReportsController_logout_clicked", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingManagerMainWelcomeForm.fxml",
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
		switchScenes("/client/boundry/MarketingManagerNotificationMainForm.fxml",
				"/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is for create a periodic characterization report
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void periodic_characterization_report_btn_clicked(MouseEvent event) {
		open_diagram_btn.setVisible(true);
		report = true;
		search_customer_txt.setText("");
		salesPatternComboBox.setVisible(false);
		while (!list_view.getItems().isEmpty())
			list_view.getItems().remove(0);
		fileChooser.setInitialFileName("Periodic_Characterization_Report");
		// this query is for get from DB the customers who bought, from which companies
		// and for each company the amount of purchases of the customer
		String query = "SELECT sales.customerID ,COUNT(sales.customerID),companys.companyName FROM sales,stations,companys"
				+ " WHERE stations.stationTag=sales.stationTag AND companys.companyTag=stations.companyTag"
				+ " GROUP BY sales.customerID,stations.companyTag ORDER BY COUNT(sales.customerID) DESC";
		Message message = new Message(MessageType.REQUESTINFO,
				"MarketingManagerReportsController_periodic_characterization_report_btn_clicked", query);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this method gets the report and add it to the list and put them in the list
	 * 
	 * @param report get the report details form the method
	 *               add_info_to_listview_marketing_manager_periodic_characterization
	 */
	public void periodic_characterization_report_returned(ArrayList<String> report) {
		if (!report.isEmpty()) {
			for (String string : report) {
				list_view.getItems().add(string);
			}
			report_created = true;
			content = convert_ListView_to_String();
			search_customer_txt.setVisible(true);
			search_btn.setVisible(true);
		} else {
			search_customer_txt.setVisible(false);
			search_btn.setVisible(false);
			content = "No Info Found";
			list_view.getItems().add("No Info Found");
		}

	}

	/**
	 * this method is handling the case of a downloading the report
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void download_report_btn_clicked(MouseEvent event) {
		if (report_created) {
			fileChooser.getExtensionFilters().add(new ExtensionFilter("text file", "*.txt"));
			File file = fileChooser.showSaveDialog(MainClientGUI.primaryStage);
			try {
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(content);
				fileWriter.close();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Download Failed");
				alert.setHeaderText(null);
				alert.setContentText("Downloading The Report Has Failed!");
				alert.show();
			}
		}
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
		switchScenes("/client/boundry/MarketingManagerSaleMainForm.fxml", "/client/boundry/MarketingManagerMain.css");

	}

	/**
	 * this method is the first thing that the controller does. we help to start the
	 * controller in here.
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		series = null;
		instance = this;
		report_created = false;
		open_diagram_btn.setVisible(false);
		char_report = null;
		comment_report = null;
		fileChooser.setInitialDirectory(new File("C:\\"));
		fileChooser.setTitle("Open Report");
		salesPatternComboBox.setVisible(false);
		search_customer_txt.setVisible(false);
		search_btn.setVisible(false);
		/**
		 * this query is for to get the sale pattern names which exits in our DB
		 */
		String query = "SELECT sale_pattern.name FROM sale_pattern";
		Message message = new Message(MessageType.REQUESTINFO, "MarketingManagerReportsController_initialize", query);
		MainClientGUI.client.handleMessageFromClientUI(message);
		// ObservableList<String> options = FXCollections.observableArrayList(query);
		// salesPatternComboBox.setItems(options);
		user_fullname.setText("Hello ," + MainClientGUI.getUserFirstName() + " " + MainClientGUI.getUserLastName());
		salesPatternComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (salesPatternComboBox.getSelectionModel().getSelectedItem() != null) {
					while (!list_view.getItems().isEmpty())
						list_view.getItems().remove(0);
					String filename = "Comments_Report_for_ "
							+ salesPatternComboBox.getSelectionModel().getSelectedItem() + " sale pattern";
					filename = filename.replaceAll("\\s+", "_");
					filename = filename.replaceAll("\\/+", "_");
					filename = filename.replaceAll("\\:+", "_");
					filename = filename.replaceAll("\\:+", " ");
					fileChooser.setInitialFileName(filename);
					sale_PatrrenName = salesPatternComboBox.getSelectionModel().getSelectedItem();
					search_customer_txt.setText("");
					/**
					 * this query is for to get the all customers who bought, how much every
					 * customer spents at the sale pattern which the marketing manager chose
					 */
					String quary = "SELECT distinct sales.customerID ,SUM(sales.price) from sale_pattern,sales"
							+ " where sale_pattern.salePatternTag=sales.sale_patternID and sale_pattern.name=\""
							+ salesPatternComboBox.getSelectionModel().getSelectedItem().toString() + "\""
							+ " GROUP BY(sales.customerID)";
					Message message = new Message(MessageType.REQUESTINFO,
							"MarketingManagerReportsController_comments_report_btn_clicked", quary);
					MainClientGUI.client.handleMessageFromClientUI(message);
				}
			}
		});
	}

	/**
	 * this method gets the sales pattern and add it to the list and put them in the
	 * list
	 * 
	 * @param sales, get the sale pattern names form the DB
	 */
	public void fill_sales_ComboBox(ArrayList<ArrayList<Object>> sales) {
		ObservableList<String> options = FXCollections.observableArrayList();
		for (ArrayList<Object> row : sales) {
			options.add((String) row.get(0));

		}
		salesPatternComboBox.setItems(options);

	}

	/**
	 * this method gets the report and add it to the list and put them in the list
	 * 
	 * @param report get the report detalis form the method
	 *               add_info_to_listview_marketing_manager_comments
	 */

	public void comments_report_returned(ArrayList<String> report) {
		if (!report.isEmpty()) {
			for (String string : report) {
				list_view.getItems().add(string);
			}
			report_created = true;
			content = convert_ListView_to_String();
			search_customer_txt.setVisible(true);
			search_btn.setVisible(true);
		} else {
			search_customer_txt.setVisible(false);
			search_btn.setVisible(false);
			content = "No Info Found";
			list_view.getItems().add("No Info Found");
		}

	}

	/**
	 * this method is for download use convert the report form list view to string
	 * 
	 * @return the list view as string
	 */
	public String convert_ListView_to_String() {
		StringBuilder page = new StringBuilder();
		for (String row : list_view.getItems()) {
			page.append(row + "\n");
		}
		return page.toString();
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
	 * this method is for search a specific customer by ID in the report.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void search_btn_clicked(MouseEvent event) {
		list_view.getSelectionModel().clearSelection();
		int count = 0, flag = 0;
		try {
			new Integer(search_customer_txt.getText());
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Input entered is not valid please try again");
			alert.show();
			return;
		}
		for (String row : list_view.getItems()) {
			if (row.contains("ID: " + search_customer_txt.getText())) {
				flag = 1;
				list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				list_view.scrollTo(count);
				list_view.getFocusModel().focus(count);
				list_view.getSelectionModel().select(count);
			}
			count++;

		}
		if (flag == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ID Not Found!");
			alert.setHeaderText(null);
			alert.setContentText("Customer searched not found");
			alert.show();
			return;
		}
	}

}
