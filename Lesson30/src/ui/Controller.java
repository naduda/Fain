package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class Controller {
	
	@FXML 
	public Text t1;
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		System.out.println("exit");
		System.exit(0);
	}
	
	public String getTextT1() {
		return t1.getText();
	}

}
