package ui;

import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import jdbc.PostgresDB;


public class Main extends Application {

	private static final int TIMEOUT_TI_SEC = 30;
	private static final int TIMEOUT_TS_SEC = 630;
//	public static final PostgresDB pdb = new PostgresDB("10.1.3.17", "3700", "dimitrovEU");
	public static final PostgresDB pdb = new PostgresDB("193.254.232.107", "5451", "dimitrovoEU", "postgres", "askue");
	
	public static MainStage mainStage;
	public static Scheme mainStage2;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = new MainStage("../Main.xml");
		mainStage2 = new Scheme("d:/export.xml");
		stage = mainStage2;
        stage.show();
        new Scheme("d:/export.xml");
        
        final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new ReceiveTopic();
				return null;
			}
        	
        };
        new Thread(task, "ReceiveTopic").start();
        
        
        
        
        final Task<Void> taskTI = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new UpdateTimeOut(TIMEOUT_TI_SEC, 1);
				return null;
			}
        	
        };
        new Thread(taskTI, "UpdateTimeOut_TI").start();
        
        final Task<Void> taskTS = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new UpdateTimeOut(TIMEOUT_TS_SEC, 2);
				return null;
			}
        	
        };
        new Thread(taskTS, "UpdateTimeOut_TS").start();
	}

}
