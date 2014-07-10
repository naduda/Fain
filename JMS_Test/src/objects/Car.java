package objects;

import javafx.scene.shape.Line;
import xml.ShapeX;

public class Car extends AShape {
	
	private final double SH_WIDTH = rect.getWidth() - lineWidth;
	private final double SH_HEIGHT = rect.getHeight() - lineWidth;

	private final Line l1 = new Line(SH_WIDTH/2, 0, SH_WIDTH/2, 5 * ONE_MM);
	private final Line l2 = new Line(SH_WIDTH/2, 7 * ONE_MM, SH_WIDTH/2, SH_HEIGHT);
	private final Line l3 = new Line(0, 5 * ONE_MM - SH_WIDTH/2, SH_WIDTH/2, 5 * ONE_MM);
	private final Line l4 = new Line(SH_WIDTH, 5 * ONE_MM - SH_WIDTH/2, SH_WIDTH/2, 5 * ONE_MM);
	private final Line l5 = new Line(0, 7 * ONE_MM - SH_WIDTH/2, SH_WIDTH/2, 7 * ONE_MM);
	private final Line l6 = new Line(SH_WIDTH, 7 * ONE_MM - SH_WIDTH/2, SH_WIDTH/2, 7 * ONE_MM);
	
	public Car(ShapeX sh) {
		super(sh);
		
		l1.setStroke(lineColor);
		l1.setStrokeWidth(lineWidth);
		l2.setStroke(lineColor);
		l2.setStrokeWidth(lineWidth);
		l3.setStroke(lineColor);
		l3.setStrokeWidth(lineWidth);
		l4.setStroke(lineColor);
		l4.setStrokeWidth(lineWidth);
		l5.setStroke(lineColor);
		l5.setStrokeWidth(lineWidth);
		l6.setStroke(lineColor);
		l6.setStrokeWidth(lineWidth);
		
		getChildren().addAll(l1, l2, l3, l4, l5, l6);
		setLayoutX(getLayoutX() + lineWidth/2);
		setLayoutY(getLayoutY() + lineWidth/2);
	}
}
