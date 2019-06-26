package Controller;

import application.Control;
import application.FileManagment;
import application.FxDialogs;
import application.Match;
import application.Umpire;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController {
	
    private int 					viewMethod;
	private final int 				CURRENT = 0, CUSTOM = 1 , ALL = 2;
	public static LocalDateTime 	start,end;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainBackground;
    
    @FXML
    private ToggleGroup viewGroup;

    @FXML
    private SplitPane aPane;

    @FXML
    private TableView<Match> matchList;

    @FXML
    private TableView<Umpire> umpList;
    
	@FXML
    private TableColumn<?, ?> idCol,divCol,genderCol,timeCol,dateCol,locationCol,presentCol,paidCol,nameCol,numMatchCol;

    @FXML
    void saveAction(ActionEvent event) {
    	FileManagment.save();
    }
    
    @FXML
    void loadAction(ActionEvent event) {
    	FileManagment.load();
    	
    	update(null);
    }
    
    @FXML
    void settingListener(ActionEvent event) {
    	FxDialogs.showInformation("WIP", "");
    }
    
    @FXML
    void closeAction(ActionEvent event) {
    	System.exit(0);
    }
    
    /**
     * Changes view so that all games will display
     */
    @FXML
    void allViewUpdate(ActionEvent event) {
    	start = LocalDateTime.MIN;
    	end   = LocalDateTime.MAX;
    	viewMethod = ALL;
    	update(null);
    }
    
    /**
     * Changes view so that only games in current week or in the future will be displayed
     */    
    @FXML
    void currentViewUpdate(ActionEvent event) {
    	start = LocalDateTime.now();
    	start = start.minusDays(start.getDayOfWeek().getValue());
    	start = LocalDateTime.of(start.toLocalDate(), LocalTime.of(0, 0));
    	end   = LocalDateTime.MAX;
    	
    	viewMethod = CURRENT;
    	update(null);
    }
    
    /**
     * Changes view so that only games in custom range will be displayed
     */
    @FXML
    void customViewUpdate(ActionEvent event) throws IOException {
    	viewMethod = CUSTOM;
    	
    	Stage home = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/CustomDateRange.fxml"));
    	home.setScene(new Scene(root));
    	home.setTitle("Input : EZ Umpires");
    	home.getIcons().add(new Image("/Images/icon.png"));
    	home.setResizable(false);
    	home.initOwner(mainBackground.getScene().getWindow());
    	home.initModality(Modality.WINDOW_MODAL);
    	home.setOnHidden(e -> update(null));
    	home.show();
    }
    
    /**
     * Opens app's documentation
     */
    @FXML
    void docAction(ActionEvent event) {
    	try {
    		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    		    Desktop.getDesktop().browse(new URI("https://drive.google.com/file/d/1V5M2GNd-NdXgE7lyNv4Ho94hHim3GrWW/view?usp=sharingdxc"));
    		}
  	  	} catch (Exception ex) {
  	  		FxDialogs.showException("Unable to load", "We were unable to load this file", ex);
  	  	}
    }

    /**
     * Opens dialog to add an Match to Control
     */
    @FXML
    void matchAddAction(ActionEvent event) throws IOException {
    	Control.matchWorking = new Match();
    	
    	Stage home = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/MatchDialog.fxml"));
    	home.setScene(new Scene(root));
    	home.setTitle("New Match : EZ Umpires");
    	home.getIcons().add(new Image("/Images/icon.png"));
    	home.setResizable(false);
    	home.initOwner(mainBackground.getScene().getWindow());
    	home.initModality(Modality.WINDOW_MODAL);
    	home.setOnHidden(e -> update(null));
    	home.show();
    }

    /**
     * Deletes selected Match from Control
     */
    @FXML
    void matchDelAction(MouseEvent event) {
    	if (event.getClickCount() == 2) {
    		Match m = matchList.getSelectionModel().getSelectedItem();
    		if (m != null) {
    			if (m.plateUmp != -1)
    				Control.getUmp(m.plateUmp).delAssignment(m);
    			
    			if (m.baseUmp != -1)
    				Control.getUmp(m.baseUmp).delAssignment(m);
    			
    			Control.matches.remove(m);
    			update(null);
    		}
    	}
    }
    
    /**
     * Opens dialog to edit the currently selected Match
     */
    @FXML
    void matchEdit(MouseEvent event) throws IOException {
    	if (event.getClickCount() == 2) {
    		Match m = matchList.getSelectionModel().getSelectedItem();
    		if (m != null) {
    			Control.matchWorking = m;
    			if (m.gameID == Match.NEXT_GAME_ID-1)
    				Match.NEXT_GAME_ID++;
    			
    	    	Stage home = new Stage();
    	    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/MatchDialog.fxml"));
    	    	home.setScene(new Scene(root));
    	    	home.setTitle("Edit Match : EZ Umpires");
    	    	home.getIcons().add(new Image("/Images/icon.png"));
    	    	home.setResizable(false);
    	    	home.initOwner(mainBackground.getScene().getWindow());
    	    	home.initModality(Modality.WINDOW_MODAL);
    	    	home.setOnHidden(e -> update(null));
    	    	home.show();
    		}
    	}
    }

    /**
     * Opens dialog to add an Umpire to Control
     */
    @FXML
    void umpAddAction(ActionEvent event) throws IOException {
    	Control.umpWorking = new Umpire();
    	
    	Stage home = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/UmpireDialog.fxml"));
    	home.setScene(new Scene(root));
    	home.setTitle("New Umpire : EZ Umpires");
    	home.getIcons().add(new Image("/Images/icon.png"));
    	home.setResizable(false);
    	home.initOwner(mainBackground.getScene().getWindow());
    	home.initModality(Modality.WINDOW_MODAL);
    	home.setOnHidden(e -> update(null));
    	home.show();
    }


    /**
     * Deletes currently selected Umpire from Control
     */
    @FXML
    void umpDelAction(MouseEvent event) {
    	if (event.getClickCount() == 2) {
    		Umpire m = umpList.getSelectionModel().getSelectedItem();
    		if (m != null) {
    			for (int i : m.assignments) {
    				Match t = Control.getMatch(i);
    				if (t.plateUmp == Control.getReference(m))
    					t.plateUmp = -1;
    				
    				if (t.baseUmp == Control.getReference(m))
    					t.baseUmp = -1;    				
    			}
    			
    			Control.umpires.remove(m);
    			update(null);
    		}
    	}
    }
    
    /**
     * Opens dialog to edit selected Umpire
     */
    @FXML
    void umpEdit(MouseEvent event) throws IOException {
    	if (event.getClickCount() == 2) {
    		Umpire m = umpList.getSelectionModel().getSelectedItem();
    		if (m != null) {
    			Control.umpWorking = m;
    			
    			if (m.id == Umpire.NEXT_UMP_ID-1)
    				Umpire.NEXT_UMP_ID++;
    			
    	    	Stage home = new Stage();
    	    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/UmpireDialog.fxml"));
    	    	home.setScene(new Scene(root));
    	    	home.setTitle("Edit Umpire : EZ Umpires");
    	    	home.getIcons().add(new Image("/Images/icon.png"));
    	    	home.setResizable(false);
    	    	home.initOwner(mainBackground.getScene().getWindow());
    	    	home.initModality(Modality.WINDOW_MODAL);
    	    	home.setOnHidden(e -> update(null));
    	    	home.show();
    		}
    	}
    }
    
    /**
     * Updates both the Umpire and Match list/view
     */
    @FXML
    void update(ScrollEvent event) {
    	matchList.getItems().clear();
    	Collections.sort(Control.matches);
    	if (viewMethod == ALL)
    		matchList.setItems(FXCollections.observableArrayList(Control.matches));
    	
    	
    	else if (viewMethod == CUSTOM || viewMethod == CURRENT) {
    		if (start != null || end != null) {
        		for (Match i : Control.matches)
        			if (i.startTime.isAfter(start) && i.startTime.isBefore(end))
        				matchList.getItems().add(i);
    		}else
    			FxDialogs.showError("Custom Date Range Invalid", "The date range entered is invalid or not set");
    	}
  	
    	
    	
    	umpList.getItems().clear();
    	Collections.sort(Control.umpires);
    	umpList.setItems(FXCollections.observableArrayList(Control.umpires));
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
    void initialize() {
        assert mainBackground != null : "fx:id=\"mainBackground\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert matchList != null : "fx:id=\"matchList\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert umpList != null : "fx:id=\"umpList\" was not injected: check your FXML file 'MainMenu.fxml'.";
        
        idCol.setCellValueFactory(new PropertyValueFactory("gameID"));
        divCol.setCellValueFactory(new PropertyValueFactory("division"));
        genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
        timeCol.setCellValueFactory(new PropertyValueFactory("timeStr"));
        dateCol.setCellValueFactory(new PropertyValueFactory("dateStr"));
        locationCol.setCellValueFactory(new PropertyValueFactory("diamond"));
        presentCol.setCellValueFactory(new PropertyValueFactory("presentTxt"));
        paidCol.setCellValueFactory(new PropertyValueFactory("paidTxt"));
        
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        numMatchCol.setCellValueFactory(new PropertyValueFactory("numGamesThisWeek"));

        currentViewUpdate(null);
        
        FileManagment.load();
        update(null);
    }
}
