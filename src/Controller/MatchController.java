// Ben Vandenberg © 2019
package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import application.Control;
import application.FxDialogs;
import application.Match;
import application.Umpire;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MatchController {
	
	private static Umpire NONE = new Umpire();
	
	private final int EMPTY = -1;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane matchBackground;

    @FXML
    private Pane aPane;

    @FXML
    private JFXCheckBox recurringCheck,umpsPaidCheck;

    @FXML
    private Text untilTxt;

    @FXML
    private JFXTextField gameIdTxt,gameFeeTxt;

    @FXML
    private JFXComboBox<String> divisionCombo,locationCombo;
    
    @FXML
    private JFXComboBox<Umpire> plateCombo,baseCombo;

    @FXML
    private JFXDatePicker startTimeDP,recurringDP;

    @FXML
    private JFXRadioButton boysRadio,girlsRadio,a630Radio,a830Radio;

    @FXML
    private ToggleGroup genderGroup,timeGroup;
    
    @FXML
    private JFXButton saveBtn;

    @FXML
    void cancelAction(ActionEvent event) {
    	Stage stage = (Stage) matchBackground.getScene().getWindow();
    	stage.close();
    }

    /**
     * Updates game fee
     */
    @FXML
    void timeAction(ActionEvent event) {
    	LocalTime temp = (a630Radio.isSelected()) ? LocalTime.of(18, 30) : (a830Radio.isSelected()) ? LocalTime.of(20, 30) : null;
    	if (temp == null) {
    		gameFeeTxt.setText("");
    		return;
    	}
    	gameFeeTxt.setText("$"+Integer.toString(Match.getFee(temp)));
    	loadUmps();
    }
    
    @FXML
    void recurringAction(ActionEvent event) {
    	boolean i = recurringCheck.isSelected();
    	untilTxt.setVisible(i);
        recurringDP.setVisible(i);
    }

    /**
     * Saves all info to the Match object
     */
    @FXML
    void saveAction(ActionEvent event) {
    	Match i = Control.matchWorking;
    	
    	i.gender = (boysRadio.isSelected()) ? 'M' : 'F' ;
    	i.division = divisionCombo.getValue();
    	i.diamond = locationCombo.getValue();
    	i.startTime = LocalDateTime.of(startTimeDP.getValue(), (a630Radio.isSelected()) ? LocalTime.of(18, 30) : LocalTime.of(20, 30) );
    	i.umpsPaid = umpsPaidCheck.isSelected();
    	
    	Control.update(i);
    	
    	if (i.gameID == Match.NEXT_GAME_ID-1)
    		Control.add(i);
    	
//    	If game is recurring, a match will be added every week until the deadline (umps do not transfer to new games)
    	if (recurringCheck.isSelected() && recurringDP != null) {
    		LocalDateTime deadline;
    		if (recurringDP.getValue() == null)
    			deadline = LocalDateTime.MIN;
    		else
    			deadline = LocalDateTime.of(recurringDP.getValue(), LocalTime.of(23, 59));
    		
    		if (i.startTime.plusYears(1).isAfter(deadline)) {
    			LocalDateTime working = i.startTime.plusWeeks(1);
    			while (working.isBefore(deadline)) {
    				Control.add(new Match(i.gender,i.division,i.diamond,working));
    				working = working.plusWeeks(1);
    			}
    			
    		}else
    			FxDialogs.showError("Date range too big", "The date range provided spans over a year, recurring request will be skipped but the original match will be saved");
    			
    	}
    	
//    	Set Plate Umpire if set
    	Umpire t = plateCombo.getValue();
    	if (t != null) {
//    		If Umpire was changed from another ump then remove the old ump from the game and remove assignment from ump
        	if (i.plateUmp != -1 && i.plateUmp != Control.getReference(t)) {
        		Control.getUmp(i.plateUmp).delAssignment(i);
        		i.plateUmp = -1;
        	}
//        	Check all info is is valid before adding (will cause problems if did happen)
        	if (i.plateUmp == -1 && t != NONE) {
        		i.plateUmp = Control.getReference(t);
        		Control.getUmp(i.plateUmp).assignments.add(Control.getReference(i));
        	}
    	}
    	
//    	Set Base Umpire if set
    	t = baseCombo.getValue();
    	if (t != null) {
//    		If Umpire was changed from another ump then remove the old ump from the game and remove assignment from ump
        	if (i.baseUmp != -1 && i.baseUmp != Control.getReference(t)) {
        		Control.getUmp(i.baseUmp).delAssignment(i);
        		i.baseUmp = -1;
        	}
//        	Check all info is is valid before adding (will cause problems if did happen)
        	if (i.baseUmp == -1 && t != NONE) {
        		i.baseUmp = Control.getReference(t);
        		Control.getUmp(i.baseUmp).assignments.add(Control.getReference(i));
        	}
    	}
    	
    	Stage stage = (Stage) matchBackground.getScene().getWindow();
    	stage.close();
    }
    
    /**
     * Loads available umpires only into umpire combo boxes
     */
    @FXML
    void loadUmps() {
    	plateCombo.getItems().clear();
    	baseCombo.getItems().clear();
    	
   		if (!(a630Radio.isSelected() || a830Radio.isSelected()) || startTimeDP.getValue() == null)
       		return;
   		
        Umpire t;
        if (Control.matchWorking.plateUmp != EMPTY) {
        	t = Control.getUmp(Control.matchWorking.plateUmp);
        	plateCombo.getItems().add(t);
        	baseCombo.getItems().add(t);
        	plateCombo.getItems().add(0,NONE);
        	plateCombo.setValue(t);
        }
    	if (Control.matchWorking.baseUmp != EMPTY) {
        	t = Control.getUmp(Control.matchWorking.baseUmp);
        	plateCombo.getItems().add(t);
        	baseCombo.getItems().add(t);
        	baseCombo.getItems().add(0,NONE);
        	baseCombo.setValue(t);
    	}
        
    	LocalDateTime i = LocalDateTime.of(startTimeDP.getValue(), (a630Radio.isSelected()) ? LocalTime.of(18, 30) : LocalTime.of(20, 30));
       	if (!Control.convertTime(i).equals("INVALID"))
       		for (Umpire u : Control.umpires) {
               	if (u.isAvailable(i) && !plateCombo.getItems().contains(u)) {
                	plateCombo.getItems().add(u);
                	baseCombo.getItems().add(u);
                }
            }
       	
       	Collections.sort(plateCombo.getItems());
       	Collections.sort(baseCombo.getItems());
    	
    	update();
    }
    
    /**
     * Disable/Enable save button depending if required info is entered
     */
    @FXML
    void update() {
    	boolean time = !a630Radio.isSelected() && !a830Radio.isSelected();
    	saveBtn.setDisable(divisionCombo.getValue() == null ||
    			locationCombo.getValue() == "" ||
    			startTimeDP.getValue() == null ||
    			time );
    }

    @FXML
    void initialize() {
        assert matchBackground != null : "fx:id=\"matchBackground\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert recurringCheck != null : "fx:id=\"recurringCheck\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert untilTxt != null : "fx:id=\"untilTxt\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert umpsPaidCheck != null : "fx:id=\"umpsPaidCheck\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert gameIdTxt != null : "fx:id=\"gameIdTxt\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert divisionCombo != null : "fx:id=\"divisionCombo\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert locationCombo != null : "fx:id=\"locationCombo\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert plateCombo != null : "fx:id=\"plateCombo\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert baseCombo != null : "fx:id=\"baseCombo\" w)as not injected: check your FXML file 'MatchDialog.fxml'.";
        assert startTimeDP != null : "fx:id=\"startTimeDP\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert recurringDP != null : "fx:id=\"recurringDP\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert gameFeeTxt != null : "fx:id=\"gameFeeTxt\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert boysRadio != null : "fx:id=\"boysRadio\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert genderGroup != null : "fx:id=\"genderGroup\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert girlsRadio != null : "fx:id=\"girlsRadio\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert a630Radio != null : "fx:id=\"a630Radio\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert timeGroup != null : "fx:id=\"timeGroup\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        assert a830Radio != null : "fx:id=\"a830Radio\" was not injected: check your FXML file 'MatchDialog.fxml'.";
        
        untilTxt.setVisible(false);
        recurringDP.setVisible(false);
        
        Match i = Control.matchWorking;
        gameIdTxt.setText(Integer.toString(i.gameID));
        girlsRadio.setSelected(i.gender == 'F');
        divisionCombo.getItems().addAll("Minimite","Mite","Squirt","Mosquito","Peewee","Bantam","Midget");
        divisionCombo.setValue(i.division );
        locationCombo.getItems().addAll("D1 Ops", "D2 Ops", "D3 Ops", "JCPS", "Omemee", "Train");
        locationCombo.setValue(i.diamond);
        
        if (i.startTime != null) {
        	startTimeDP.setValue(i.startTime.toLocalDate());
        	boolean p = i.startTime.getHour() == 18;
            a630Radio.setSelected(p);
            a830Radio.setSelected(!p);
            
            loadUmps();
        }
        
    	umpsPaidCheck.setSelected(i.umpsPaid);
    	gameFeeTxt.setText("$" + Integer.toString(i.fee));
    	update();
    }
}
