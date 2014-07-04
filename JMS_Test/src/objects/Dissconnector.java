package objects;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Dissconnector extends Group {

	private Rectangle rect;
	private Line l1;
	private Line l2;
	private Line l3;
	private Line l4;
	private Circle c;
	
	public Dissconnector() {
		rect = new Rectangle(10, 45);	
		l1 = new Line(0, 0, 0, 15);
		l2 = new Line(0, 30, 0, 45);
		l3 = new Line(0, 30, 10, 15);
		l4 = new Line(-2.5, 15, 2.5, 15);
	    c = new Circle(0, 30, 2, Color.LIGHTGREY);
	    
		getChildren().addAll(rect, l1, l2, l3, l4, c);
		System.out.println("1");
	}
	
	public Dissconnector(double x, double y, double w, String lineColor, double angle, double zoomX, double zoomY) {
		rect = new Rectangle(15 + w + 2.5, 60 + w);
		rect.setFill(Color.LIGHTGRAY);
		rect.setLayoutX(-w/2 - 2.5);
		rect.setLayoutY(-w/2);
		l1 = new Line(0, 0, 0, 20);
		l2 = new Line(0, 40, 0, 60);
		l3 = new Line(0, 40, 15, 20);
		l4 = new Line(-2.5, 20, 2.5, 20);
	    c = new Circle(0, 40, 2, Color.LIGHTGREY);
	    
		getChildren().addAll(rect, l1, l2, l3, l4, c);
				
		l1.setStroke(Color.RED);
		l1.setStrokeWidth(w);
		l2.setStroke(Color.RED);
		l2.setStrokeWidth(w);
		l3.setStroke(Color.RED);
		l3.setStrokeWidth(w);
		l4.setStroke(Color.RED);
		l4.setStrokeWidth(w);
		c.setStroke(Color.RED);
		c.setStrokeWidth(w/2);
		
	    setLayoutX(x + w);
	    setLayoutY(y);
	    setRotate(angle);
	    setScaleX(zoomX);
	    setScaleY(zoomY);
	    
	    setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				l3.setEndX(l3.getEndX() == 15 ? 0 : 15);
				System.out.println();
			}
		});
	}
}
