package objects;

import javafx.scene.shape.Line;
import xml.ShapeX;

public class DisConnectorGRND extends AShape {

	private final double SH_WIDTH = rect.getWidth();
	private final double SH_HEIGHT = rect.getHeight();
	private final double H = SH_HEIGHT * 0.15;
	
	private final Line l5 = new Line(0, SH_HEIGHT - H, SH_WIDTH, SH_HEIGHT - H);
	private final Line l6 = new Line(H/2, SH_HEIGHT - H/2, SH_WIDTH - H/2, SH_HEIGHT - H/2);
	private final Line l7 = new Line(H, SH_HEIGHT, SH_WIDTH - H, SH_HEIGHT);
	
	private Disconnector dc;
	
	public DisConnectorGRND(ShapeX sh) {
		super(sh);
		ShapeX shD = sh;
		shD.setHeight(SH_HEIGHT * 0.85);
		shD.setAngle(0);
		shD.setX(0);
		shD.setY(0);
		shD.setZoomX(1);
		shD.setZoomY(1);
		
		dc = new Disconnector(shD);
		dc.setLayoutX(SH_WIDTH/2 - ONE_MM);
		
		l5.setStroke(lineColor);
		l5.setStrokeWidth(lineWidth);
		l6.setStroke(lineColor);
		l6.setStrokeWidth(lineWidth);
		l7.setStroke(lineColor);
		l7.setStrokeWidth(lineWidth);
		
		getChildren().addAll(dc, l5, l6, l7);
		System.out.println(Math.abs(sh.getAngle()));
		if (Math.abs(sh.getAngle()) == 90 || Math.abs(sh.getAngle()) == 270) {
			System.out.println(Math.abs(sh.getAngle()));
			setLayoutX(getLayoutX() - lineWidth);
			setLayoutY(getLayoutY() - 1.5*lineWidth);
		}
	}

	@Override
	public void changeTS(int val) {
		super.changeTS(val);
		dc.changeTS(val);
	}
	
}
