package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

public class AlarmController implements Initializable{

	@FXML TableView<String> tvAlarms;
	@FXML ChoiceBox<String> cbPriority;
	
	@FXML
	private void kvitOne(ActionEvent event) {
		System.out.println("kvitOne");
	}
	
	@FXML
	private void kvitPS(ActionEvent event) {
		System.out.println("kvitPS");
	}
	
	@FXML
	private void kvitAll(ActionEvent event) {
		System.out.println("kvitAll");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initialize " + cbPriority + " " + cbPriority.getItems().size());
		
		List<String> list = new ArrayList<>();
		list.add("qwe");
		list.add("asd");
		list.add("zxc");
		ObservableList<String> obList = FXCollections.observableList(list);

		cbPriority.getItems().addAll(obList);
		cbPriority.setValue(cbPriority.getItems().get(0));
	}
	
}
