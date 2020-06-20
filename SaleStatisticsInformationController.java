package client.common.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.MainClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import message_info.Message;
import message_info.MessageType;

/**
 * This class implements the analyzing of data from the analytic system
 * 
 * @author yarin
 * @version 0.99
 */
public class SaleStatisticsInformationController extends AbstractController {

	/**
	 * holding an instance of this controller here
	 */
	public static SaleStatisticsInformationController instance;

	@FXML
	private VBox menu_parent;

	@FXML
	private Button menu_btn;

	@FXML
	private Button notification_btn;

	@FXML
	private Button customers_btn;

	@FXML
	private Button sales_btn;

	@FXML
	private Button back_btn;

	@FXML
	private ComboBox<String> fuel_type_comboBox;

	@FXML
	private ComboBox<String> fuel_timing_comboBox;

	@FXML
	private ComboBox<String> fuel_quantity_comboBox;

	@FXML
	private ComboBox<String> customer_rate_comboBox;

	@FXML
	private Button find_btn;

	@FXML
	private PieChart pie_chart;

	@FXML
	private Label result_lable;

	@FXML
	private Button logout_btn;

	@FXML
	private Label user_fullname;

	@FXML
	private Button help_btn;

	@FXML
	private Label total_number_lable;

	private Long count;

	private boolean all_type = false;

	private boolean all_timing = false;

	private boolean all_quantity = false;

	private boolean all_rate = false;

	/**
	 * this function handles the event of muse clicked on back button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void back_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");

	}

	/**
	 * this function handles the event of muse clicked on customers button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void customers_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMainCustomerForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * this function handles the event of muse clicked on help button
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
		dia.setContentText("Sale Statistics Information Screen\nIn this screen marketing agent can see statistics from the analytic system\n"
				+ "This screen is used for help to create a new sale pattern by filtering all the customer information");
		dia.show();
	}

	/**
	 * this function handles the event of muse clicked on customers button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void logout_btn_clicked(MouseEvent event) {
		String quary = "UPDATE users SET connection_status = 0 WHERE userID = " + MainClientGUI.getUserID();
		Message message = new Message(MessageType.LOGOUT, "SaleStatisticsInformationController_logout_clicked_agent", quary);
		MainClientGUI.client.handleMessageFromClientUI(message);
	}

	/**
	 * this function handles the event of muse clicked on menu button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void menu_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentMenuWelcomeForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * this function handles the event of muse clicked on notification button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void notification_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentNotificationMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * this function handles the event of muse clicked on sales button
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void sales_btn_clicked(MouseEvent event) {
		switchScenes("/client/boundry/MarketingAgentSalesMainForm.fxml",
				"/client/boundry/MarketingAgentMainCustomer.css");
	}

	/**
	 * This method handles the event of muse clicked on find button. In FXML there
	 * is 4 comboBox: fuel type, fuel timing, fuel amount, customer rate. Each
	 * comboBox have different filtering options. After fill all the relevant fields
	 * in comboBox and press find A pie Chart will be displayed with the data after
	 * filtering. The data will be taken from the analytic system.
	 * 
	 * @param event The event that caused the method to activate
	 */
	@FXML
	void find_btn_clicked(MouseEvent event) {

		all_type = false;
		all_timing = false;
		all_quantity = false;
		all_rate = false;

		int All_flag = 0;
		boolean choose_flag = false;

		StringBuilder all_query = new StringBuilder();

		// check counter All_flag for chosen "All" in combBox, and choose_flag if there
		// is at list 1 specific

		if (!(fuel_quantity_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(fuel_quantity_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {
			if (fuel_quantity_comboBox.getSelectionModel().getSelectedItem().equals("All")) {
				All_flag++;
				all_quantity = true;
				all_query.append("SELECT fuelAmount FROM KPLsaa2Lyx.analytic_info");
			} else
				choose_flag = true;

		}

		if (!(fuel_timing_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(fuel_timing_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {
			if (fuel_timing_comboBox.getSelectionModel().getSelectedItem().equals("All")) {
				All_flag++;
				all_timing = true;
				all_query.append("SELECT fuelingHours FROM KPLsaa2Lyx.analytic_info");
			} else
				choose_flag = true;
		}

		if (!(fuel_type_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(fuel_type_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {
			if (fuel_type_comboBox.getSelectionModel().getSelectedItem().equals("All")) {
				All_flag++;
				all_type = true;
				all_query.append("SELECT motorCustomer, benzinCustomer, solerCustomer FROM KPLsaa2Lyx.analytic_info");
			} else
				choose_flag = true;
		}

		if (!(customer_rate_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(customer_rate_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {
			if (customer_rate_comboBox.getSelectionModel().getSelectedItem().equals("All")) {
				All_flag++;
				all_rate = true;
				all_query.append("SELECT customerType FROM KPLsaa2Lyx.analytic_info");
			} else
				choose_flag = true;
		}

		// there is specific choice, no need change query
		if (choose_flag == true) {

		}

		else {
			if (All_flag > 1) {
				// error notice
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error ");
				alert.setContentText("You can choose All only 1 time !");

				alert.showAndWait();
				return;
			}

			if (All_flag == 0 && choose_flag == false) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error ");
				alert.setContentText("You must choose an option !");

				alert.showAndWait();
				return;
			}

			if (All_flag == 1) {
				Message msg = new Message(MessageType.REQUESTINFO, "SaleStatisticsInformationController_all_comboBox_agent",
						all_query.toString());
				MainClientGUI.client.handleMessageFromClientUI(msg);
				return;
			}
		}

		StringBuilder query = new StringBuilder();
		query.append("select count(customerID) from analytic_info where");
		String choosen_box;
		choosen_box = fuel_quantity_comboBox.getSelectionModel().getSelectedItem();

		int sql_flag = 0;

		if (!(fuel_quantity_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(fuel_quantity_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {

			if (all_quantity == false) { //// customer choose all, no need add to query because check all the quantity
											//// data
				sql_flag++;
				if (sql_flag > 1)
					query.append(" and ");

				query.append(" analytic_info.fuelAmount ");

				switch (choosen_box) {

				case "0-500 L":
					query.append("<500");
					break;

				case "500-1000 L":
					query.append(">= 500 and analytic_info.fuelAmount < 1000");
					break;

				case "1000L +":
					query.append(">= 1000");
					break;
				default:
					break;

				}

			}

		}

		if (!(fuel_timing_comboBox.getSelectionModel().getSelectedItem() == null)) {
			if (!(fuel_timing_comboBox.getSelectionModel().getSelectedItem().isEmpty())) {

				if (all_timing == false) { //// customer choose all, no need add to query because check all the hours
											//// data
					sql_flag++;

					if (sql_flag > 1) {
						query.append(" AND ");
					}

					switch (fuel_timing_comboBox.getSelectionModel().getSelectedItem()) {
					case "06:00-10:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%06%' OR analytic_info.fuelingHours LIKE '%07%' OR analytic_info.fuelingHours LIKE '%08%' OR analytic_info.fuelingHours LIKE '%09%' )");

						break;
					case "10:00-14:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%10%' OR analytic_info.fuelingHours LIKE '%11%' OR analytic_info.fuelingHours LIKE '%12%' OR analytic_info.fuelingHours LIKE '%13%' )");
						break;
					case "14:00-18:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%14%' OR analytic_info.fuelingHours LIKE '%15%' OR analytic_info.fuelingHours LIKE '%16%' OR analytic_info.fuelingHours LIKE '%17%' )");

						break;
					case "18:00-22:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%18%' OR analytic_info.fuelingHours LIKE '%19%' OR analytic_info.fuelingHours LIKE '%20%' OR analytic_info.fuelingHours LIKE '%21%' )");

						break;
					case "22:00-02:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%22%' OR analytic_info.fuelingHours LIKE '%23%' OR analytic_info.fuelingHours LIKE '%00%' OR analytic_info.fuelingHours LIKE '%01%' )");

						break;
					case "02:00-06:00":
						query.append(
								"(analytic_info.fuelingHours LIKE '%02' OR analytic_info.fuelingHours LIKE '%03%' OR analytic_info.fuelingHours LIKE '%04%' OR analytic_info.fuelingHours LIKE '%05%' )");
						break;

					case "All":
						break;

					default:
						break;
					}
				}

			}
		}

		if (!(fuel_type_comboBox.getSelectionModel().getSelectedItem() == null)) {
			if (!(fuel_type_comboBox.getSelectionModel().getSelectedItem().isEmpty())) {

				if (all_type == false) { //// customer choose all, no need add to query because check all the type data

					sql_flag++;

					if (sql_flag > 1) {
						query.append(" AND ");
					}

					switch (fuel_type_comboBox.getSelectionModel().getSelectedItem()) {

					case "Motor":
						query.append(" analytic_info.motorCustomer > 0 ");
						break;
					case "Benzin":
						query.append(" analytic_info.benzinCustomer > 0 ");
						break;
					case "Soler":
						query.append(" analytic_info.solerCustomer > 0 ");
						break;
					default:
						break;
					}
				}

			}
		}

		if (!(customer_rate_comboBox.getSelectionModel().getSelectedItem() == null)
				&& (!(customer_rate_comboBox.getSelectionModel().getSelectedItem().isEmpty()))) {

			if (all_rate == false) { // customer choose all, no need add to query because check all the rate data
				sql_flag++;
				if (sql_flag > 1)
					query.append(" and ");

				query.append(" analytic_info.customerType ='");
				query.append(customer_rate_comboBox.getSelectionModel().getSelectedItem());
				query.append("';");
			}
		}

		// case there was no options set
		if (query.toString().equals("select count(customerID) from analytic_info where")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error ");
			alert.setContentText("You must choose an option !");

			alert.showAndWait();
			return;
		}

		Message msg = new Message(MessageType.REQUESTINFO, "SaleStatisticsInformationController_count_choosen_box_agent",
				query.toString());
		MainClientGUI.client.handleMessageFromClientUI(msg);

	}

	/**
	 * This method happen immediately when the relevant FXML open. Here we define
	 * all the fields in the comboBox. Set the static variable to the this instance
	 * of the class
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;

		ObservableList<String> fueltype = FXCollections.observableArrayList("All", "", "Motor", "Benzin", "Soler");
		fuel_type_comboBox.setItems(fueltype);

		ObservableList<String> ratenumber = FXCollections.observableArrayList("All", "", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "10");
		customer_rate_comboBox.setItems(ratenumber);

		ObservableList<String> fuelhour = FXCollections.observableArrayList("All", "", "06:00-10:00", "10:00-14:00",
				"14:00-18:00", "18:00-22:00", "22:00-02:00", "02:00-06:00");
		fuel_timing_comboBox.setItems(fuelhour);

		ObservableList<String> fuelquantity = FXCollections.observableArrayList("All", "", "0-500 L", "500-1000 L",
				"1000L +");
		fuel_quantity_comboBox.setItems(fuelquantity);

	}

	/**
	 * returned method after the user logout
	 */
	public void return_logout_success() {
		MainClientGUI.loggedIn = false;
		switchScenes("/client/boundry/LoginForm.fxml", "/client/boundry/MainCss.css");

	}

	/**
	 * Return method that receive the counter of all the client after filtering, and
	 * save it in global variable count. After define the counter, the method send
	 * query to receive the general counter of client without filter.
	 * 
	 * @param conut_for_comboBox is a matrix that hold the result of the query:
	 *                           counter of clients after filter.
	 */
	public void return_counter_comboBox(ArrayList<ArrayList<Object>> conut_for_comboBox) {

		count = (Long) conut_for_comboBox.get(0).get(0);

		String query = ("select count(customerID) from analytic_info ;");
		Message msg = new Message(MessageType.REQUESTINFO, "SaleStatisticsInformationController_general_counter_agent",
				query);
		MainClientGUI.client.handleMessageFromClientUI(msg);

	}

	/**
	 * This method in charge of displaying the pie chart with the details that
	 * return from the query. In addition set the appropriate counters and percents
	 * in the labels.
	 * 
	 * @param general_counter is a matrix that hold the result of the query: total
	 *                        clients number.
	 */
	public void return_general_counter(ArrayList<ArrayList<Object>> general_counter) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				float help_precent;
				Long total_num = (Long) general_counter.get(0).get(0);

				result_lable.setText("Requested: " + count);
				total_number_lable.setText("Total number of clients: " + total_num);

				help_precent = ((float) count / total_num) * 100;

				ObservableList<PieChart.Data> PieChartDate = FXCollections.observableArrayList(
						new PieChart.Data("Requested " + (int) help_precent + "%", help_precent),
						new PieChart.Data("Total " + (int) (100 - help_precent) + "%", 100 - help_precent));
				pie_chart.setData(PieChartDate);

			}
		});

	}

	/**
	 * This method recognized which one of the "All" button was pressed. After
	 * detect it the method calculate the data and insert the data to the pie chart.
	 * 
	 * @param all_comboBox is a matrix that holds the relevant columns from the
	 *                     query.
	 */
	public void return_comboBox(ArrayList<ArrayList<Object>> all_comboBox) {

		if (all_rate == true) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					int count_rows = 0;
					int sum_for_avg = 0;

					int[] arr = new int[10];
					double[] precent_arr = new double[10];
					ArrayList<PieChart.Data> pie = new ArrayList<PieChart.Data>(10);

					for (ArrayList<Object> row : all_comboBox) {

						arr[(int) row.get(0) - 1]++;
						sum_for_avg += (int) row.get(0);
						count_rows++;
					}

					for (int i = 0; i < 10; i++) {

						precent_arr[i] = ((double) arr[i] / count_rows) * 100;

						String temp = String.format("%.02f", precent_arr[i]);

						pie.add(new PieChart.Data((i + 1) + "- " + temp + "%", precent_arr[i]));

					}

					total_number_lable.setText("Total Number Of Clients: " + count_rows);
					result_lable.setText("Avarage Rate: " + sum_for_avg / count_rows);
					ObservableList<PieChart.Data> PieChartDate = FXCollections.observableArrayList(pie);
					pie_chart.setData(PieChartDate);
				}
			});

		}

		if (all_quantity == true) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					int first_counter = 0; // sum of customer with amount 0-500
					int second_counter = 0; // sum of customer with amount 500-1000
					int third_counter = 0; // sum of customer with amount 1000+
					float total_amount = 0;
					String total_str;

					float[] arr = new float[3]; // counter array for all 3 amounts
					int count_rows = 0;
					double[] precent_arr = new double[3];

					ArrayList<PieChart.Data> pie = new ArrayList<PieChart.Data>(3);

					for (ArrayList<Object> row : all_comboBox) {

						if ((float) row.get(0) < 500) {
							arr[0] += (float) row.get(0);
							first_counter++;
							count_rows++;
						}

						if ((float) row.get(0) > 500 && (float) row.get(0) < 1000) {
							arr[1] += (float) row.get(0);
							second_counter++;
							count_rows++;
						}

						if ((float) row.get(0) >= 1000) {
							arr[2] += (float) row.get(0);
							third_counter++;
							count_rows++;
						}
					}

					total_amount = arr[0] + arr[1] + arr[2];

					precent_arr[0] = (((double) first_counter / count_rows) * 100);
					String temp = String.format("%.02f", precent_arr[0]);
					pie.add(new PieChart.Data("0-500L \n" + temp + "%", (double) precent_arr[0]));

					precent_arr[1] = (((double) second_counter / count_rows) * 100);
					temp = String.format("%.02f", precent_arr[1]);
					pie.add(new PieChart.Data("500-1000 L \n" + temp + "%", (double) precent_arr[1]));

					precent_arr[2] = (((double) third_counter / count_rows) * 100);
					temp = String.format("%.02f", precent_arr[2]);
					pie.add(new PieChart.Data("1000L+ \n" + temp + "%", (double) precent_arr[2]));

					total_str = String.format("%.02f", total_amount);

					total_number_lable.setText("Total Quantity Sold: " + total_str + "L");
					String avg = String.format("%.02f", total_amount / count_rows);
					result_lable.setText("Avarage Purchase Quantity : " + avg);
					ObservableList<PieChart.Data> PieChartDate = FXCollections.observableArrayList(pie);
					pie_chart.setData(PieChartDate);
				}
			});

		}

		if (all_type == true) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					int motor_counter = 0;
					int benzin_counter = 0;
					int soler_counter = 0;

					/// float[] arr = new float[3]; // counter array for all 3 types
					int count_rows = 0;
					double[] precent_arr = new double[3];

					ArrayList<PieChart.Data> pie = new ArrayList<PieChart.Data>(3);

					for (ArrayList<Object> row : all_comboBox) {

						motor_counter += (int) row.get(0);

						benzin_counter += (int) row.get(1);

						soler_counter += (int) row.get(2);

					}

					count_rows = soler_counter + benzin_counter + motor_counter;
					precent_arr[0] = (((double) motor_counter / count_rows) * 100);
					String temp = String.format("%.02f", precent_arr[0]);
					pie.add(new PieChart.Data("Motor \n" + temp + "%", (double) precent_arr[0]));

					precent_arr[1] = (((double) benzin_counter / count_rows) * 100);
					temp = String.format("%.02f", precent_arr[1]);
					pie.add(new PieChart.Data("Benzin \n" + temp + "%", (double) precent_arr[1]));

					precent_arr[2] = (((double) soler_counter / count_rows) * 100);
					temp = String.format("%.02f", precent_arr[2]);
					pie.add(new PieChart.Data("Soler \n" + temp + "%", (double) precent_arr[2]));

					total_number_lable.setText("Total Number Of Purchases: " + count_rows);
					result_lable.setText("Avarage Purchases For Client: " + (float) count_rows / all_comboBox.size());
					ObservableList<PieChart.Data> PieChartDate = FXCollections.observableArrayList(pie);
					pie_chart.setData(PieChartDate);
				}
			});

		}

		if (all_timing == true) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					int[] arr = new int[6];
					double[] precent_arr = new double[6];
					int total_purchase_hour;
					StringBuilder rush_hour = new StringBuilder();
					// StringBuilder specific_hour = new StringBuilder();
					int max;

					ArrayList<PieChart.Data> pie = new ArrayList<PieChart.Data>(6);

					for (ArrayList<Object> row : all_comboBox) {

						if (((((String) row.get(0)).contains("06")) || (((String) row.get(0)).contains("07"))
								|| (((String) row.get(0)).contains("08")) || (((String) row.get(0)).contains("09"))))
							arr[0]++;

						if (((((String) row.get(0)).contains("10")) || (((String) row.get(0)).contains("11"))
								|| (((String) row.get(0)).contains("12")) || (((String) row.get(0)).contains("13"))))
							arr[1]++;

						if (((((String) row.get(0)).contains("14")) || (((String) row.get(0)).contains("15"))
								|| (((String) row.get(0)).contains("16")) || (((String) row.get(0)).contains("17"))))
							arr[2]++;

						if (((((String) row.get(0)).contains("18")) || (((String) row.get(0)).contains("19"))
								|| (((String) row.get(0)).contains("20")) || (((String) row.get(0)).contains("21"))))
							arr[3]++;

						if (((((String) row.get(0)).contains("22")) || (((String) row.get(0)).contains("23"))
								|| (((String) row.get(0)).contains("00")) || (((String) row.get(0)).contains("01"))))
							arr[4]++;

						if (((((String) row.get(0)).contains("02")) || (((String) row.get(0)).contains("03"))
								|| (((String) row.get(0)).contains("04")) || (((String) row.get(0)).contains("05"))))
							arr[5]++;

					}

					max = arr[0];

					for (int i = 1; i < 6; i++) {
						if (arr[i] > max)
							max = arr[i];
					}

					// sum of all purchases from all the hours
					total_purchase_hour = arr[0] + arr[1] + arr[2] + arr[3] + arr[4] + arr[5];

					precent_arr[0] = (((double) arr[0] / total_purchase_hour) * 100);
					String temp = String.format("%.02f", precent_arr[0]);
					pie.add(new PieChart.Data("6-10 " + temp + "%", (double) precent_arr[0]));

					precent_arr[1] = (((double) arr[1] / total_purchase_hour) * 100);
					temp = String.format("%.02f", precent_arr[1]);
					pie.add(new PieChart.Data("10-14 " + temp + "%", (double) precent_arr[1]));

					precent_arr[2] = (((double) arr[2] / total_purchase_hour) * 100);
					temp = String.format("%.02f", precent_arr[2]);
					pie.add(new PieChart.Data("14-18 " + temp + "%", (double) precent_arr[2]));

					precent_arr[3] = (((double) arr[3] / total_purchase_hour) * 100);
					temp = String.format("%.02f", precent_arr[3]);
					pie.add(new PieChart.Data("18-22 " + temp + "%", (double) precent_arr[3]));

					precent_arr[4] = (((double) arr[4] / total_purchase_hour) * 100);
					temp = String.format("%.02f", precent_arr[4]);
					pie.add(new PieChart.Data("22-02 " + temp + "%", (double) precent_arr[4]));

					precent_arr[5] = (((double) arr[5] / total_purchase_hour) * 100);
					temp = String.format("%.02f", precent_arr[5]);
					pie.add(new PieChart.Data("2-6 " + temp + "%", (double) precent_arr[5]));

					total_number_lable.setText("Total Number Of Clients: " + all_comboBox.size());

					result_lable.setText("Rush Hour : ");

					for (int i = 0; i < 6; i++) {

						if (arr[i] == max) {

							switch (i) {

							case 0:
								rush_hour.append("06:00-10:00, ");
								break;
							case 1:
								rush_hour.append("10:00-14:00, ");

								break;
							case 2:
								rush_hour.append("14:00-18:00, ");

								break;
							case 3:
								rush_hour.append("18:00-22:00, ");

								break;
							case 4:
								rush_hour.append("22:00-02:00, ");

								break;
							case 5:
								rush_hour.append("02:00-06:00, ");

								break;
							default:
								break;
							}

						}

					}

					String str = rush_hour.substring(0, rush_hour.length() - 2);

					result_lable.setText(result_lable.getText() + str);

					ObservableList<PieChart.Data> PieChartDate = FXCollections.observableArrayList(pie);
					pie_chart.setData(PieChartDate);
				}
			});
		}

	}
}
