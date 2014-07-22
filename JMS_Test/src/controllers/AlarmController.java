package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import model.Alarm;
import ui.alarm.AlarmTableItem;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AlarmController implements Initializable{

	private final ObservableList<AlarmTableItem> data = FXCollections.observableArrayList();
	
	@FXML TableView<AlarmTableItem> tvAlarms;
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
		System.out.println("initialize " + tvAlarms + " " + cbPriority.getItems().size());
		
		List<String> list = new ArrayList<>();
		list.add("qwe");
		list.add("asd");
		list.add("zxc");
		ObservableList<String> obList = FXCollections.observableList(list);

		cbPriority.getItems().addAll(obList);
		cbPriority.setValue(cbPriority.getItems().get(0));
		
		tvAlarms.setItems(data);
		ObservableList<TableColumn<AlarmTableItem, ?>> tColumns = tvAlarms.getColumns();
		for (TableColumn<AlarmTableItem, ?> tableColumn : tColumns) {
			tableColumn.setCellValueFactory(p -> Bindings.selectString(p.getValue(), tableColumn.getId()));
		}
	}
	
	public void addAlarm(Alarm a) {
		data.add(new AlarmTableItem(a));
	}
}
