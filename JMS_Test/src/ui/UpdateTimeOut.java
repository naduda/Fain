package ui;

import model.Tsignal;
import javafx.application.Platform;

public class UpdateTimeOut {

	private boolean isRun = true;
	private int sec;
	private int type_;
	
	public UpdateTimeOut(int sec, int t) {
		this.sec = sec;
		this.type_ = t;
		run();
	}

	public void run() {
		try {
			while (isRun) {
				if (Main.mainStage != null) {
					for (Tsignal tsignal : Main.mainStage.getSignals()) {
						if (tsignal.getTypesignalref() == type_) {
							new Thread(new Runnable() {
			    	            @Override public void run() {
			    	                Platform.runLater(new Runnable() {
			    	                    @Override public void run() {
			    	                    	Controller.updateSignal(tsignal, type_, sec);
			    	                    }
			    	                });
			    	            }
			    	        }, "Update signal").start();							
						}
					}
				}
				
				Thread.sleep(sec * 100);
			}
		} catch (Exception e) {
			System.err.println("UpdateTimeOut ...");
		}
	}

}
