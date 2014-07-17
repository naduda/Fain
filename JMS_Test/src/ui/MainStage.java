package ui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import controllers.Controller;
import ua.pr.common.ToolsPrLib;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private static final String DEFAULT_SCHEME = "ПС-110 кВ 'Блок-4'";
	public static ListView<String> lvTree;
	public static BorderPane bpScheme;
	public static Map<Integer, Scheme> schemes = new HashMap<>();
	public static Controller controller;

	public MainStage(String pathXML) {
		try {	
			FXMLLoader loader = new FXMLLoader(new URL("file:/" + ToolsPrLib.getFullPath("./ui/Main.xml")));
			Parent root = loader.load();
			controller = loader.getController();

			Scene scene = new Scene(root);      
			setTitle("PowerSys ARM");
			
			BorderPane bp = (BorderPane) root.lookup("#bp");
			
			SplitPane vSplitPane = (SplitPane) bp.getCenter();
			SplitPane hSplitPane = (SplitPane) vSplitPane.getItems().get(0);

			bpScheme = (BorderPane) hSplitPane.getItems().get(1);
			
			setScheme(DEFAULT_SCHEME);
			controller.getSpTreeController().expandSchemes();
			controller.getSpTreeController().addContMenu();
			setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setScheme(String schemeName) {
		if (schemeName == null) {
			Main.mainScheme = new Scheme();
		} else {
			Main.mainScheme = new Scheme(ToolsPrLib.getFullPath("./schemes/" + schemeName + ".xml"));

			TreeItem<Scheme> ti = new TreeItem<>(Main.mainScheme);
			controller.getSpTreeController().addScheme(ti);
	        MainStage.schemes.put(Main.mainScheme.getIdScheme(), Main.mainScheme);
		}
		bpScheme.setCenter(Main.mainScheme);
	}
}