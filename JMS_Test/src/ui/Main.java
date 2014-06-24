package ui;
import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	public static MainStage mainStage;

	public static void main(String[] args) {
		if (args.length > 0) {
			runInNewthread(new ReceiveTopic(args[0]));
		} else {
			runInNewthread(new ReceiveTopic());
		}
		runInNewthread(new UpdateTimeOut(11, 1));
		runInNewthread(new UpdateTimeOut(660, 2));
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = new MainStage("../Main.xml");
		stage = mainStage;
        stage.show();
	}
	
	public static void runInNewthread(Runnable runnable) { 
        Thread brokerThread = new Thread(runnable); 
        brokerThread.setDaemon(false); 
        brokerThread.start(); 
    }

}
