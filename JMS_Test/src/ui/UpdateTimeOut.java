package ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Tsignal;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
							Text t = type_ == 1 ? Main.mainStage.getTextById(tsignal.getIdsignal() + "") : Main.mainStage.getTextById("d_" + tsignal.getIdsignal());
							
							String st = t.getText();
							if (st.lastIndexOf("2014-") != -1) {
								st = st.substring(st.lastIndexOf("   "), st.length()).trim();
								Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
								
								if ((System.currentTimeMillis() - date.getTime()) < sec * 1000) {
									t.setFill(Color.GREEN);
									
								} else {
									t.setFill(Color.RED);
								}
							}
							
						}
					}
				}
				
				Thread.sleep(sec * 100);
			}
		} catch (Exception e) {
			System.err.println("UpdateTimeOut ...");
			e.printStackTrace();
		}
	}

}
