package ui;

import java.net.URL;
import java.util.List;

import model.Tsignal;
import ua.pr.common.ToolsPrLib;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainStage extends Stage {
	
	private GridPane grid;
	private List<Tsignal> signals;
	private Label statusLabel;
	
	public MainStage(String pathXML) {
		try {
			Parent root = FXMLLoader.load(new URL("file:/" + ToolsPrLib.getFullPath(pathXML)));
			Scene scene = new Scene(root);
			this.setTitle("Data monitoring ...");
			this.setScene(scene);
			this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {
			        Controller.exitProgram();
			    }
			});

			ScrollPane sp = (ScrollPane) root.lookup("#sp");
			setGrid((GridPane) sp.getContent().lookup("#grid"));
			statusLabel = (Label) ((ToolBar) root.lookup("#status")).getItems().get(0);
			
			signals = Main.pdb.getSignals();
			if (signals == null) {
				System.out.println("Can't get signals ...");
				System.exit(0);
			}
			int i = 0;
			int j = 0;
			for (Tsignal tsignal : signals) {
				if (tsignal.getTypesignalref() == 1) {
					grid.add(new Label((i +1) + " - " + tsignal.getNamesignal()), 0, i);
					Text tt = new Text();
					tt.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event e) {
							statusLabel.setText(((Text)e.getSource()).getId());
						}
					});
					tt.setId("" + tsignal.getIdsignal());
					tt.setUserData(tsignal);
					grid.add(tt, 1, i);
					i++;
				} else if (tsignal.getTypesignalref() == 2) {
					Button btn = new Button();
					btn.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event e) {
							statusLabel.setText(((Button)e.getSource()).getId());
						}
					});
					btn.setPrefSize(20, 20);
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
		Text tt = null;
		try {
			tt = (Text) grid.lookup("#" + id);
		} catch (Exception e) {
			System.err.println("getTextById ...");
		}
		return tt;
	}
	
	public Label getLabelById(String id) {
		Label tt = null;
		try {
			tt = (Label) grid.lookup("#" + id);
		} catch (Exception e) {
			System.err.println("getLabelById ...");
		}
		return tt;
	}
	
	public Button getButtontById(String id) {
		Button tt = null;
		try {
			tt = (Button) grid.lookup("#" + id);
		} catch (Exception e) {
			System.err.println("getButtontById ...");
		}
		return tt;
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
