package ui;

import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import jdbc.PostgresDB;


public class Main extends Application {

	private static final int TIMEOUT_TI_SEC = 30;
	private static final int TIMEOUT_TS_SEC = 630;
	public static final PostgresDB pdb = new PostgresDB("10.1.3.17", "3700", "dimitrovEU");
	
	public static MainStage mainStage;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = new MainStage("../Main.xml");
		stage = mainStage;
        stage.show();
        
        final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new ReceiveTopic();
				return null;
			}
        	
        };
        new Thread(task).start();
        
        final Task<Void> taskTI = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new UpdateTimeOut(TIMEOUT_TI_SEC, 1);
				return null;
			}
        	
        };
        new Thread(taskTI).start();
        
        final Task<Void> taskTS = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new UpdateTimeOut(TIMEOUT_TS_SEC, 2);
				return null;
			}
        	
        };
        new Thread(taskTS).start();
	}

}
