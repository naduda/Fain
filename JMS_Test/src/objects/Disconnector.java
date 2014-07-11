package objects;

import ui.Main;
import xml.ShapeX;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Disconnector extends AShape {

	private static final double CIRCLE_LINE_WIDTH = 2;
	private static final Paint CIRCLE_FILL_COLOR = Color.LIGHTGRAY;
	
	private final double SH_HEIGHT = rect.getHeight() - lineWidth;
	
	private Line l1 = new Line(ONE_MM, 0, ONE_MM, 5 * ONE_MM);
	private Line l2 = new Line(ONE_MM, 10 * ONE_MM, ONE_MM, SH_HEIGHT);
	private Line l3 = new Line(ONE_MM, 10 * ONE_MM, ONE_MM + 5 * ONE_MM / Math.sqrt(3), 5 * ONE_MM);
	private Line l4 = new Line(0, 5 * ONE_MM, 2 * ONE_MM, 5 * ONE_MM);
	private Circle c = new Circle(ONE_MM, 10 * ONE_MM, CIRCLE_LINE_WIDTH, CIRCLE_FILL_COLOR);
	
	public Disconnector(ShapeX sh) {
		super(sh);

		shapes.getChildren().addAll(l1, l2, l3, l4, c);
		getChildren().add(shapes);
		setStrokeAndColor();
	}
	
	@Override
	public void changeTS(int val) {
		super.changeTS(val);
		l3.setEndX(isON ? l3.getStartX() : l3.getStartX() + 5 * ONE_MM / Math.sqrt(3));
	}

	@Override
	public void setTS(int val) {
		super.setTS(val);
		Main.pdb.setTS(Integer.parseInt(getId()), val, 1);
	}
}
