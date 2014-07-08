package objects;

import java.util.Date;

import ui.Scheme;
import xml.ShapeX;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Breaker extends Group {

	private final double SH_WIDTH = 31.496062992126;
	private final double SH_HEIGHT = 70.8661417322834;
	
	private Rectangle rect;
	private Rectangle rectB;
	private Line l1;
	private Line l2;
	private int signalTU1 = 0;
	private int signalTU2 = 0;
	private int signalTS = 0;
	
	private Date lastDataDate;
	
	public Breaker() {
		rect = new Rectangle();	
		rectB = new Rectangle();	
		l1 = new Line(SH_WIDTH/2, 0, SH_WIDTH/2, 20);
		l2 = new Line(SH_WIDTH/2, 20 + SH_WIDTH, SH_WIDTH/2, SH_HEIGHT);
		
		getChildren().addAll(rect, l1, l2, rectB);
	}
	
	public Breaker(ShapeX sh) {
		this();
		setSignals(sh);
		setId("" + signalTS);
		double x = sh.getX();
		double y = sh.getY();
		double lineWidth = sh.getLineWeight();

		rect.setWidth(SH_WIDTH + lineWidth);
		rect.setHeight(SH_HEIGHT + lineWidth);
		rect.setFill(Color.TRANSPARENT);
		rect.setStrokeWidth(lineWidth);
		rect.setStroke(Color.TRANSPARENT);
		rect.setLayoutX(-lineWidth/2);
		rect.setLayoutY(-lineWidth/2);
		
		Color col = Scheme.getColor(sh.getLineColor());
		rectB.setWidth(SH_WIDTH);
		rectB.setHeight(SH_WIDTH);
		rectB.setFill(Color.GREEN);
		rectB.setStrokeWidth(lineWidth);
		rectB.setStroke(col);
		rectB.setLayoutX(0);
		rectB.setLayoutY(20);
				
		l1.setStroke(col);
		l1.setStrokeWidth(lineWidth);
		l2.setStroke(col);
		l2.setStrokeWidth(lineWidth);
		
		setLayoutX(x - lineWidth/2);
	    setLayoutY(y - lineWidth/2);
	    setRotate(sh.getAngle());
	    setScaleX(sh.getZoomX());
	    setScaleY(sh.getZoomY());
	    
	    setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				rectB.setFill(rectB.getFill() == Color.RED ? Color.GREEN : Color.RED);
			}
		});
	}
	
	public void changeTS(int ts) {
		rectB.setFill(ts == 0 ? Color.GREEN : Color.RED);
	}
	
	public void updateTS(int sec) {
		if (lastDataDate == null) return;
		if ((System.currentTimeMillis() - lastDataDate.getTime()) < sec * 1000) {
			rect.setStroke(Color.TRANSPARENT);	
		} else {
			rect.setStroke(Color.WHITE);
		}
	}
	
	private void setSignals(ShapeX sh) {
		String[] sInf = sh.getSignalInfo().split("\\|");
		String[] sSign = sh.getSignal().split("\\|");
		for (int i = 0; i < sInf.length; i++) {
			String st = sInf[i];
			int ind = st.indexOf("typeSignal");
			st = st.substring(ind);
			ind = Integer.parseInt(st.substring(st.indexOf(":") + 1, st.indexOf(";")));
			switch (ind) {
			case 2:
				signalTS = Integer.parseInt(sSign[i]);
				break;
			case 3:
				if (signalTU1 == 0) {
					signalTU1 = Integer.parseInt(sSign[i]);
				} else {
					signalTU2 = Integer.parseInt(sSign[i]);
				}
				break;
			default:
				break;
			}
		}
	}

	public int getSignalTU1() {
		return signalTU1;
	}

	public void setSignalTU1(int signalTU1) {
		this.signalTU1 = signalTU1;
	}

	public int getSignalTU2() {
		return signalTU2;
	}

	public void setSignalTU2(int signalTU2) {
		this.signalTU2 = signalTU2;
	}

	public int getSignalTS() {
		return signalTS;
	}

	public void setSignalTS(int signalTS) {
		this.signalTS = signalTS;
	}
	public Date getLastDataDate() {
		return lastDataDate;
	}

	public void setLastDataDate(Date lastDataDate) {
		this.lastDataDate = lastDataDate;
	}
}
