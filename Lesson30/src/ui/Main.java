package ui;
import topic.ReceiveTopic;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	public static MainStage mainStage;

	public static void main(String[] args) {
		if (args.length > 0) {
			ReceiveTopic.runInNewthread(new ReceiveTopic(args[0]));
		} else {
			ReceiveTopic.runInNewthread(new ReceiveTopic());
		}
		ReceiveTopic.runInNewthread(new UpdateTimeOut(11, 1));
		ReceiveTopic.runInNewthread(new UpdateTimeOut(660, 2));
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		mainStage = new MainStage("../Main.xml");
		stage = mainStage;
        stage.show();
	}


}
