package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
	
	@FXML 
	protected void exitButtonAction(ActionEvent event) {
		System.out.println("exit");
		System.exit(0);
	}

}
