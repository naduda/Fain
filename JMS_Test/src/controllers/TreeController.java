package controllers;

import ui.Scheme;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class TreeController {

	@FXML
	private Accordion accordTree;
	
	@FXML
	private TreeView<Scheme> tvSchemes;
	
	@FXML
	private TitledPane tpSchemes;
	
	@FXML
	private TreeItem<Scheme> trSchemes;
	
	public void addScheme(TreeItem<Scheme> ti) {
		trSchemes.getChildren().add(ti);
	}

	public void expandSchemes() {
		accordTree.setExpandedPane(tpSchemes);
	}
	
	final ContextMenu rootContextMenu = new ContextMenu();
	
	public void addContMenu() {		
		tvSchemes.setCellFactory(new Callback<TreeView<Scheme>, TreeCell<Scheme>>(){
            @Override
            public TreeCell<Scheme> call(TreeView<Scheme> p) {
                return new SchemeTreeCellImpl();
            }
        });
	}
	
	private final class SchemeTreeCellImpl extends TreeCell<Scheme> {

		private Scheme scheme;
        private ContextMenu addMenu = new ContextMenu();
 
        public SchemeTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Закрити");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(e -> {
            	System.out.println("act " + ((Scheme)getItem()).getIdScheme());
            });
        }
        
        @Override
        public void startEdit() {
        	System.out.println("startEdit");
            super.startEdit();
 
            if (scheme == null) {
                //createTextField();
            }
            setText(null);
            setGraphic(scheme);
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setGraphic(getTreeItem().getGraphic());
        }
 
        @Override
        public void updateItem(Scheme item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (scheme != null) {
                    }
                    setText(null);
                    setGraphic(scheme);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());

                    setContextMenu(addMenu);
                }
            }
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
	}
}
