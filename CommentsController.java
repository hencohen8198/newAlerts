package client.common.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import client.common.CommentsInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommentsController extends AbstractController {

	public static CommentsController instance;

	@FXML
	private TableView<CommentsInfo> main_table;

	@FXML
	private TableColumn<CommentsInfo, Integer> id_clm;

	@FXML
	private TableColumn<CommentsInfo, Double> income_clm;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		id_clm.setCellValueFactory(new PropertyValueFactory<CommentsInfo, Integer>("id"));
		income_clm.setCellValueFactory(new PropertyValueFactory<CommentsInfo, Double>("sum"));
	}

	public void add_to_table(ObservableList<CommentsInfo> list) {
		main_table.setItems(list);
	}
}
