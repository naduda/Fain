package ui;

import java.io.IOException;
import java.net.URL;

import ua.pr.common.ToolsPrLib;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainStage extends Stage {

	public static ListView<String> lvTree;
	public static BorderPane bpScheme;
	
	@SuppressWarnings("unchecked")
	public MainStage(String pathXML) {
		try {
			Parent root = FXMLLoader.load(new URL("file:/" + ToolsPrLib.getFullPath(pathXML)));
			Scene scene = new Scene(root);      
			setTitle("PowerSys ARM");
			
			BorderPane bp = (BorderPane) root.lookup("#bp");
			SplitPane vSplitPane = (SplitPane) bp.getCenter();
			SplitPane hSplitPane = (SplitPane) vSplitPane.getItems().get(0);
			ScrollPane spTree = (ScrollPane) hSplitPane.getItems().get(0);
			lvTree = (ListView<String>) spTree.getContent();
			lvTree.setOnMouseReleased(event -> {				
				switch (((MouseEvent)event).getButton()) {
				case SECONDARY:
					System.out.println(event.getSource());
					System.out.println("right");
					break;

				default:
					break;
				}
			});;
			bpScheme = (BorderPane) hSplitPane.getItems().get(1);
			
			bpScheme.setCenter(Main.mainScheme);
			
			setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
