package client.common.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import client.common.CharacterizationInfo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CharacterizationController extends AbstractController {

	public static CharacterizationController instance;
	@FXML
	private TableView<CharacterizationInfo> main_table;

	@FXML
	private TableColumn<CharacterizationInfo, Integer>  id_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> dor_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> nrg_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> delek_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> ten_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> paz_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> sonol_clm;

	@FXML
	private TableColumn<CharacterizationInfo, Integer> total_clm;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		instance = this;
		sonol_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("sonol"));
		nrg_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("nrg"));
		paz_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("paz"));
		total_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("total"));
		delek_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("delek"));
		ten_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("ten"));
		id_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("id"));
		dor_clm.setCellValueFactory(new PropertyValueFactory<CharacterizationInfo, Integer>("dor"));
	}
	
	public void add_to_table(ObservableList<CharacterizationInfo> list) {
		main_table.setItems(list);
	}

}
