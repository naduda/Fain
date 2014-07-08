package objects;

import ui.Scheme;
import xml.ShapeX;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Disconnector extends Group {

	public final static double SH_HEIGHT = 59.0551181102362;
	private final double SH_WIDTH = 14.2793060032;
	private final double SH_L4 = 4;
	
	private Rectangle rect;
	private Line l1;
	private Line l2;
	private Line l3;
	private Line l4;
	private Circle c;
	
	private int signalTU1 = 0;
	private int signalTU2 = 0;
	private int signalTS = 0;
	
	public Disconnector() {
		rect = new Rectangle();	
		l1 = new Line(SH_L4, 0, SH_L4, 20);
		l2 = new Line(SH_L4, 40, SH_L4, SH_HEIGHT);
		l3 = new Line(SH_L4, 40, SH_WIDTH, 20);
		l4 = new Line(0, 20, SH_L4 * 2, 20);
	    c = new Circle(SH_L4, 40, 2, Color.LIGHTGREY);
	    
		getChildren().addAll(rect, l1, l2, l3, l4, c);
	}
	
	public Disconnector(ShapeX sh) {
		this();
		setSignals(sh);
		setId("" + signalTS);
		double x = sh.getX();
		double y = sh.getY();
		double lineWidth = sh.getLineWeight();
		rect.setWidth(SH_WIDTH + lineWidth);
		rect.setHeight(SH_HEIGHT + lineWidth);
		rect.setFill(Color.TRANSPARENT);
		rect.setLayoutX(-lineWidth/2);
		rect.setLayoutY(-lineWidth/2);
				
		Color col = Scheme.getColor(sh.getLineColor());
		l1.setStroke(col);
		l1.setStrokeWidth(lineWidth);
		l2.setStroke(col);
		l2.setStrokeWidth(lineWidth);
		l3.setStroke(col);
		l3.setStrokeWidth(lineWidth);
		l4.setStroke(col);
		l4.setStrokeWidth(lineWidth);
		c.setStroke(col);
		c.setStrokeWidth(lineWidth);
		
	    setLayoutX(x - lineWidth/2);
	    setLayoutY(y - lineWidth/2);
	    setRotate(sh.getAngle());
	    setScaleX(sh.getZoomX());
	    setScaleY(sh.getZoomY());
	    
	    setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				l3.setEndX(l3.getEndX() == SH_WIDTH ? SH_L4 : SH_WIDTH);
			}
		});
	}
	
	public void changeTS(int ts) {
		l3.setEndX(ts == 1 ? SH_L4 : SH_WIDTH);
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
}
