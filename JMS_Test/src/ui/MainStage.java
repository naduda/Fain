package ui;

import java.net.URL;
import java.sql.Connection;
import java.util.List;

import model.Tsignal;
import ua.pr.common.ToolsPrLib;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdbc.PostgresDB;

public class MainStage extends Stage {
	private GridPane grid;
	private List<Tsignal> signals;
	
	public MainStage(String pathXML) {
		try {
			Parent root = FXMLLoader.load(new URL("file:/" + ToolsPrLib.getFullPath(pathXML)));
			Scene scene = new Scene(root);
			this.setTitle("Modbus device's configurator");
			this.setScene(scene);

			ScrollPane sp = (ScrollPane) root.lookup("#sp");
			setGrid((GridPane) sp.getContent().lookup("#grid"));
			PostgresDB pdb = new PostgresDB();
			Connection conn = pdb.getConnection("193.254.232.107:5451", "dimitrovoEU", "postgres", "askue");
			signals = pdb.getAllSignals(conn);
			int i = 0;
			int j = 0;
			for (Tsignal tsignal : signals) {
				if (tsignal.getTypesignalref() == 1) {
					grid.add(new Label((i +1) + " - " + tsignal.getNamesignal()), 0, i);
					Text tt = new Text();
					tt.setId("" + tsignal.getIdsignal());
					tt.setUserData(tsignal);
					grid.add(tt, 1, i);
					i++;
				} else if (tsignal.getTypesignalref() == 2) {
					Button btn = new Button();
					btn.setPrefWidth(btn.getHeight());
					btn.setId("" + tsignal.getIdsignal());
					grid.add(btn, 2, j);
					grid.add(new Label(tsignal.getNamesignal()), 3, j);
					Text tt = new Text();
					tt.setId("d_" + tsignal.getIdsignal());
					grid.add(tt, 4, j);
					j++;
				}
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Text getTextById(String id) {
		return (Text) grid.lookup("#" + id);
	}
	
	public Button getButtontById(String id) {
		return (Button) grid.lookup("#" + id);
	}
	
	public GridPane getGrid() {
		return grid;
	}

	public void setGrid(GridPane grid) {
		this.grid = grid;
	}

	public List<Tsignal> getSignals() {
		return signals;
	}
}
