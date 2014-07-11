package ui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ua.pr.common.ToolsPrLib;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private static final String DEFAULT_SCHEME = "ПС-110 кВ 'Блок-4'";
	public static ListView<String> lvTree;
	public static BorderPane bpScheme;
	public static Map<Integer, Scheme> schemes = new HashMap<>();
	public static Label lLastDate;
	
	@SuppressWarnings("unchecked")
	public MainStage(String pathXML) {
		try {
			Parent root = FXMLLoader.load(new URL("file:/" + ToolsPrLib.getFullPath(pathXML)));
			Scene scene = new Scene(root);      
			setTitle("PowerSys ARM");
			
			BorderPane bp = (BorderPane) root.lookup("#bp");
			BorderPane bpTop = (BorderPane) bp.lookup("#bpTop");
			ToolBar toolBar = (ToolBar) bpTop.lookup("#toolBar");
			lLastDate = (Label) ((HBox)toolBar.getItems().get(0)).lookup("#lLastDate");
			System.out.println(lLastDate.getText());
			
			SplitPane vSplitPane = (SplitPane) bp.getCenter();
			SplitPane hSplitPane = (SplitPane) vSplitPane.getItems().get(0);
			ScrollPane spTree = (ScrollPane) hSplitPane.getItems().get(0);
			lvTree = (ListView<String>) spTree.getContent();
			
			spTree.maxWidthProperty().bind(hSplitPane.widthProperty().divide(4));
			lvTree.prefWidthProperty().bind(spTree.widthProperty().subtract(spTree.getPadding().getLeft() + spTree.getPadding().getRight()));
			lvTree.prefHeightProperty().bind(spTree.heightProperty().subtract(spTree.getPadding().getBottom() + spTree.getPadding().getTop()));
			
			lvTree.setOnMouseReleased(event -> {
				Main.mainScheme = MainStage.schemes.get(lvTree.getSelectionModel().getSelectedIndex());
	            MainStage.bpScheme.setCenter(Main.mainScheme);
				switch (((MouseEvent)event).getButton()) {
				case PRIMARY:
					
					break;
				case SECONDARY:
					System.out.println(event.getSource());
					System.out.println("right");
					break;

				default:
					break;
				}
			});
			bpScheme = (BorderPane) hSplitPane.getItems().get(1);
			
			HBox hboxAlarms = (HBox) vSplitPane.getItems().get(1);
			hboxAlarms.maxHeightProperty().bind(vSplitPane.heightProperty().divide(4));
			
			setScheme(DEFAULT_SCHEME);
			
			setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setScheme(String schemeName) {
		Main.mainScheme = new Scheme(ToolsPrLib.getFullPath("./schemes/" + schemeName + ".xml"));
		bpScheme.setCenter(Main.mainScheme);
		MainStage.lvTree.getItems().add(schemeName);
        MainStage.schemes.put(MainStage.lvTree.getItems().indexOf(schemeName), Main.mainScheme);
	}
	
}
