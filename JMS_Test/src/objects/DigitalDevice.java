package objects;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import ui.Scheme;
import xml.ShapeX;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DigitalDevice extends Text {
	private DecimalFormat decimalFormat;
	private Date lastDataDate;
	
	public DigitalDevice(ShapeX sh) {
		super(sh.getX(), sh.getY() + sh.getHeight()/2 + sh.getFontSize()/4, sh.getText());
		
		setFont(new Font("Arial", sh.getFontSize()));
		setWrappingWidth(sh.getWidth());
		setTextAlignment(TextAlignment.CENTER);
		setFill(Scheme.getColor(sh.getLineColor()));
		int sign = Integer.parseInt(sh.getSignal());
		setId("" + sign);
		setText("*******");
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat = new DecimalFormat(sh.getPrecision(), dfs);
	}
	
	public void updateTI(int sec) {
		if (lastDataDate == null) return;
		if ((System.currentTimeMillis() - lastDataDate.getTime()) < sec * 1000) {
			setFill(Color.GREEN);	
		} else {
			setFill(Color.RED);
		}
	}

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public Date getLastDataDate() {
		return lastDataDate;
	}

	public void setLastDataDate(Date lastDataDate) {
		this.lastDataDate = lastDataDate;
	}
}
