package ui;

import java.io.IOException;
import java.net.URL;

import ua.pr.common.ToolsPrLib;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainStage extends Stage {
	
	private Text t1;
	private Text t2;
	
	public MainStage(String pathXML) {
		try {
			Parent root = FXMLLoader.load(new URL("file:/" + ToolsPrLib.getFullPath(pathXML)));
			Scene scene = new Scene(root);
			this.setTitle("Modbus device's configurator");
			this.setScene(scene);
			setT1((Text) root.lookup("#t1"));
			setT2((Text) root.lookup("#t2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Text getT1() {
		return t1;
	}

	public void setT1(Text t1) {
		this.t1 = t1;
	}

	public Text getT2() {
		return t2;
	}

	public void setT2(Text t2) {
		this.t2 = t2;
	}
	
}
