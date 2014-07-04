package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import objects.Dissconnector;
import xml.Document;
import xml.ShapeX;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Scheme extends Stage {
	
	public Scheme(String fileName) {
		setTitle(fileName);
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        Controller.exitProgram();
		    }
		});
		Group root = new Group();
		
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
		Document doc = (Document) result;
		List<ShapeX> shapes = doc.getPage().getShapes();
		for (ShapeX shapeX : shapes) {
			if (shapeX.getType() != null) {
				if ("gdisconnector".equals(shapeX.getType().toLowerCase())) {
					root.getChildren().add(new Dissconnector(shapeX.getX(), shapeX.getY(), shapeX.getLineWeight(), 
							shapeX.getLineColor(), shapeX.getAngle(), shapeX.getZoomX(), shapeX.getZoomY()));
				}
				if ("line".equals(shapeX.getType().toLowerCase())) {
					double w = shapeX.getLineWeight();
					Line line = new Line(shapeX.getX() - w/2, shapeX.getY() - w/2, shapeX.getX() + shapeX.getWidth() - w/2, shapeX.getY() + shapeX.getHeight() - w/2);
					line.setStroke(Color.RED);
					line.setStrokeWidth(w);
					root.getChildren().add(line);
				}
			}
		}

		Scene scene = new Scene(new ScrollPane(root));
		scene.setFill(Color.LIGHTGRAY);
		setScene(scene);
	}
}
