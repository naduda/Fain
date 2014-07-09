package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import objects.AShape;
import objects.Breaker;
import objects.DigitalDevice;
import objects.DisConnectorGRND;
import objects.Disconnector;
import xml.Document;
import xml.ShapeX;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Scheme extends Stage {
	
	private boolean ctrlPressed;
	private boolean shiftPressed;
	
	private final Group root = new Group();
	private List<Integer> signalsTI;
	private List<Integer> signalsTS;
	public static AShape selectedShape;
	
	public Scheme(String fileName) {		
		setOnCloseRequest(event -> { Controller.exitProgram(); });
				
		Object result = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Document.class);
			Unmarshaller u = jc.createUnmarshaller();
			result = u.unmarshal(new FileInputStream(new File(fileName)));
		} catch (JAXBException e) {
			System.err.println("Error in EntityFromXML.getObject(...). JAXBException: " + e);
		} catch (FileNotFoundException ex) {
			System.err.println("Error in EntityFromXML.getObject(...). FileNotFoundException: " + ex);
		}
		
		signalsTI = new ArrayList<>();
		signalsTS = new ArrayList<>();
		
		Document doc = (Document) result;
		setTitle(doc.getPage().getName());
		List<ShapeX> shapes = doc.getPage().getShapes();
		for (ShapeX shapeX : shapes) {
			if (shapeX.getType() != null) {
				if (shapeX.getType().toLowerCase().equals("group")) {
					paintGroup(root, shapeX);
				} else {
					paintShape(root, shapeX);
				}
			}
		}
		
		ScrollPane sp = new ScrollPane(root);
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		Scene scene = new Scene(sp, visualBounds.getWidth(), visualBounds.getHeight());

		String bgColor = String.format("-fx-background: %s;", doc.getPage().getFillColor());
		sp.setStyle(bgColor);
		
		scene.setOnKeyPressed(event -> {
			switch (((KeyEvent)event).getCode()) {
			case CONTROL:
				ctrlPressed = true;
				break;
			case SHIFT:
				shiftPressed = true;
				break;
			default:
				break;
			}
		});
		scene.setOnKeyReleased(event -> {
			switch (((KeyEvent)event).getCode()) {
			case CONTROL:
				ctrlPressed = false;
				break;
			case SHIFT:
				shiftPressed = false;
				break;
			default:
				break;
			}
		});

		sp.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
			if (ctrlPressed) {
				double zoomFactor = 1.1;
                if (deltaY < 0) {
                  zoomFactor = 2.0 - zoomFactor;
                }

                root.setScaleX(root.getScaleX() * zoomFactor);
                root.setScaleY(root.getScaleY() * zoomFactor);
                event.consume();
            } else if (shiftPressed) {
            	if (deltaY < 0) {
            		sp.setHvalue(sp.getHvalue() - 0.04);
	            } else {
	            	sp.setHvalue(sp.getHvalue() + 0.04);
	            }
            }
	    });
		
		setScene(scene);
	}
	
	private void paintGroup(Group root, ShapeX shGr) {
		Group gr = new Group();
		for (ShapeX sh : shGr.getShapes()) {
			if (sh.getType().toLowerCase().equals("group")) {
				paintGroup(gr, sh);
			} else {
				paintShape(gr, sh);
			}
		}
		gr.setRotate(shGr.getAngle());
		if (shGr.getFlipY() != 0) {
			gr.setScaleY(-shGr.getFlipY());
		}
		if (shGr.getFlipX() != 0) {
			gr.setScaleX(-shGr.getFlipX());
		}
		root.getChildren().add(gr);
	}
	
	private void paintShape(Group gr, ShapeX sh) {
		Shape shFX = null;
				
		if ("text".equals(sh.getType().toLowerCase())) {
			if (sh.getId() != null && sh.getId().toLowerCase().startsWith("digitaldevice")) {
				DigitalDevice dd = new DigitalDevice(sh);
				signalsTI.add(Integer.parseInt(dd.getId()));
				gr.getChildren().add(dd);
				return;
			}
			
			if (sh.isFilled()) {
				shFX = new Rectangle(0, 0, sh.getWidth(), sh.getHeight());
				shapeTransform(gr, shFX, sh);
			}

			Text text = new Text(sh.getX(), sh.getY() + sh.getHeight()/2 + sh.getFontSize()/4, sh.getText());
			text.setFont(new Font("Arial", sh.getFontSize()));
			text.setWrappingWidth(sh.getWidth());
			text.setTextAlignment(TextAlignment.CENTER);
			text.setFill(getColor(sh.getLineColor()));

			gr.getChildren().add(text);
			return;
		}
		if ("gdisconnector".equals(sh.getType().toLowerCase())) {
			Disconnector dd = new Disconnector(sh);
			signalsTS.add(Integer.parseInt(dd.getId()));
			gr.getChildren().add(dd);
			return;
		}
		if ("gdisconnectorgrnd".equals(sh.getType().toLowerCase())) {
			DisConnectorGRND dd = new DisConnectorGRND(sh);
			signalsTS.add(Integer.parseInt(dd.getId()));
			gr.getChildren().add(dd);
			return;
		}
		if ("gbreaker".equals(sh.getType().toLowerCase())) {
			Breaker dd = new Breaker(sh);
			signalsTS.add(Integer.parseInt(dd.getId()));
			gr.getChildren().add(dd);
			return;
		}
		
		if ("line".equals(sh.getType().toLowerCase())) {			
			shFX = new Line(0, 0, sh.getWidth(), sh.getHeight());
		}
		if ("rect".equals(sh.getType().toLowerCase())) {
			shFX = new Rectangle(0, 0, sh.getWidth(), sh.getHeight());
		}
		if ("circle".equals(sh.getType().toLowerCase())) {
			shFX = new Circle(sh.getWidth()/2, sh.getHeight()/2, sh.getWidth()/2);
		}
		if ("arc".equals(sh.getType().toLowerCase())) {
			shFX = new Arc(sh.getWidth()/2, sh.getHeight()/2, sh.getWidth()/2, sh.getHeight()/2, -sh.getStartAng(), -sh.getLenAng());
		}		
		
		shapeTransform(gr, shFX, sh);
	}
	
	private void shapeTransform(Group gr, Shape shFX, ShapeX sh) {
		double w = sh.getLineWeight();
		if (shFX != null) {
			shFX.setTranslateX(sh.getX());
			shFX.setTranslateY(sh.getY());
			shFX.setStrokeWidth(w);
			shFX.setRotate(sh.getAngle());			
			if (sh.isFilled()) {
				shFX.setFill(getColor(sh.getFillColor()));
			} else {
				shFX.setFill(Color.TRANSPARENT);
			}
			shFX.setStroke(getColor(sh.getLineColor()));
			
			gr.getChildren().add(shFX);
		} else {
			System.out.println(sh.getId());
		}
	}
	
	public static Color getColor(String col) {
		if (col == null) return null;
		if (col.startsWith("#")) {
			return Color.web(col);
		} else {
			String lColor = decToHex(Long.parseLong(col)).substring(2);
			return Color.web("0x" + lColor);
		}
	}
	
	private static String decToHex(long dec) {
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	    StringBuilder hexBuilder = new StringBuilder(8);
	    hexBuilder.setLength(8);
	    for (int i = 8 - 1; i >= 0; --i)
	    {
	    	long j = dec & 0x0F;
		    hexBuilder.setCharAt(i, hexDigits[(int)j]);
		    dec >>= 4;
	    }
	    return hexBuilder.toString(); 
	}
	
//	public DigitalDevice getTextById(String id) {
//		DigitalDevice tt = null;
//		try {
//			tt = (DigitalDevice) root.lookup("#" + id);
//		} catch (Exception e) {
//			System.err.println("getTextById ...");
//		}
//		return tt;
//	}
	
	public DigitalDevice getDigitalDeviceById(String id) {
		DigitalDevice tt = null;
		try {
			tt = (DigitalDevice) root.lookup("#" + id);
		} catch (Exception e) {
			System.err.println("getDigitalDeviceById ...");
		}
		return tt;
	}
	
	public Group getDeviceById(String id) {
		Group tt = null;
		try {
			tt = (Group) root.lookup("#" + id);
		} catch (Exception e) {
			System.err.println("getDeviceById ...");
		}
		return tt;
	}

	public List<Integer> getSignalsTI() {
		return signalsTI;
	}

	public List<Integer> getSignalsTS() {
		return signalsTS;
	}
}
