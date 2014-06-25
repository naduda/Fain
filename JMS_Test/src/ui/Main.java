package ui;
import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	private static final int TIMEOUT_TI_SEC = 30;
	private static final int TIMEOUT_TS_SEC = 630;
	
	public static MainStage mainStage;

	public static void main(String[] args) {
		if (args.length > 0) {
			runInNewthread(new ReceiveTopic(args[0]), "ReceiveDataThread");
		} else {
			runInNewthread(new ReceiveTopic(), "ReceiveDataThread");
		}
		runInNewthread(new UpdateTimeOut(TIMEOUT_TI_SEC, 1), "UpdateTimeOutTiThread");
		runInNewthread(new UpdateTimeOut(TIMEOUT_TS_SEC, 2), "UpdateTimeOutTsThread");
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = new MainStage("../Main.xml");
		stage = mainStage;
        stage.show();
	}
	
	public static void runInNewthread(Runnable runnable, String name) { 
        Thread thread = new Thread(runnable, name);
        thread.start(); 
    }

}
