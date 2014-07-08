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

public class DisConnectorGRND extends Group {

	private final double SH_WIDTH = 27.5590551181102;
	private final double SH_HEIGHT = 70.0787401574803;
	private final double SH_L4 = 4;
	
	private Rectangle rect;
	private Line l1;
	private Line l2;
	private Line l3;
	private Line l4;
	private Circle c;
	private Line l5;
	private Line l6;
	private Line l7;
	
	private int signalTU1 = 0;
	private int signalTU2 = 0;
	private int signalTS = 0;
	
	public DisConnectorGRND() {
		rect = new Rectangle();	
		l1 = new Line(SH_WIDTH/2, 0, SH_WIDTH/2, 20);
		l2 = new Line(SH_WIDTH/2, 40, SH_WIDTH/2, Disconnector.SH_HEIGHT);
		l3 = new Line(SH_WIDTH/2, 40, SH_WIDTH, 20);
		l4 = new Line(SH_WIDTH/2 - SH_L4, 20, SH_WIDTH/2 + SH_L4, 20);
	    c = new Circle(SH_WIDTH/2, 40, 2, Color.LIGHTGREY);
	    double h = SH_HEIGHT - Disconnector.SH_HEIGHT;
	    l5 = new Line(0, Disconnector.SH_HEIGHT, SH_WIDTH, Disconnector.SH_HEIGHT);
	    l6 = new Line(h/2, Disconnector.SH_HEIGHT + h/2, SH_WIDTH - h/2, Disconnector.SH_HEIGHT + h/2);
	    l7 = new Line(h, Disconnector.SH_HEIGHT + h, SH_WIDTH - h, Disconnector.SH_HEIGHT + h);
	    
		getChildren().addAll(rect, l1, l2, l3, l4, c, l5, l6, l7);
	}
	
	public DisConnectorGRND(ShapeX sh) {
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
		l5.setStroke(col);
		l5.setStrokeWidth(lineWidth);
		l6.setStroke(col);
		l6.setStrokeWidth(lineWidth);
		l7.setStroke(col);
		l7.setStrokeWidth(lineWidth);
		
	    setLayoutX(x - lineWidth/2);
	    setLayoutY(y - lineWidth/2);
	    setRotate(sh.getAngle());
	    setScaleX(sh.getZoomX());
	    setScaleY(sh.getZoomY());
	    
	    setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				l3.setEndX(l3.getEndX() == SH_WIDTH/2 ? SH_WIDTH : SH_WIDTH/2);
			}
		});
	}
	
	public void changeTS(int ts) {
		l3.setEndX(ts == 0 ? SH_WIDTH : SH_WIDTH/2);
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
