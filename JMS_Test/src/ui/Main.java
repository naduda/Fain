package ui;
import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;


public class Main extends Application {

	private static final int TIMEOUT_TI_SEC = 30;
	private static final int TIMEOUT_TS_SEC = 630;
	
	public static MainStage mainStage;

	public static void main(String[] args) {
//		if (args.length > 0) {
//			runInNewthread(new ReceiveTopic(args[0]), "ReceiveDataThread");
//		} else {
//			runInNewthread(new ReceiveTopic(), "ReceiveDataThread");
//		}
//		runInNewthread(new UpdateTimeOut(TIMEOUT_TI_SEC, 1), "UpdateTimeOutTiThread");
//		runInNewthread(new UpdateTimeOut(TIMEOUT_TS_SEC, 2), "UpdateTimeOutTsThread");
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
