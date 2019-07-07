// Ben Vandenberg © 2019
package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import application.Control;
import application.FxDialogs;
import application.Match;
import application.SendEmail;
import application.Umpire;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UmpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane umpBackground;

    @FXML
    private JFXTextField nameTxt,emailTxt,levelTxt,payTxt;
    
    @FXML
    private JFXCheckBox m6,m8,t6,t8,w6,w8,th6,th8,f6,f8;

    @FXML
    private TableView<Match> matchList;

	@FXML
    private TableColumn<?, ?> idCol,divCol,genderCol,timeCol,dateCol,locationCol,paidCol;

    @FXML
    private TextArea msgTxt;

    @FXML
    private JFXButton saveBtn;

    @FXML
    void cancelAction(ActionEvent event) {
    	Stage stage = (Stage) umpBackground.getScene().getWindow();
    	stage.close();
    }
    
    /**
     * Saves all info to Umpire object
     */
    @FXML
    void saveAction(ActionEvent event) {
    	Umpire i = Control.umpWorking;
    	
    	i.name = nameTxt.getText().trim();
    	i.email = emailTxt.getText().trim();
    	i.level = Float.parseFloat(levelTxt.getText().trim());
    	
    	i.availability.put("m6", m6.isSelected());
    	i.availability.put("m8", m8.isSelected());
    	i.availability.put("t6", t6.isSelected());
    	i.availability.put("t8", t8.isSelected());
    	i.availability.put("w6", w6.isSelected());
    	i.availability.put("w8", w8.isSelected());
    	i.availability.put("th6", th6.isSelected());
    	i.availability.put("th8", th8.isSelected());
    	i.availability.put("f6", f6.isSelected());
    	i.availability.put("f8", f8.isSelected());
    	
    	if (i.id == Umpire.NEXT_UMP_ID-1)
    		Control.add(i);
    	
    	Stage stage = (Stage) umpBackground.getScene().getWindow();
    	stage.close();
    }
    
    /**
     * Sends the message in msgTxt to the saved email for the umpire
     */
    @FXML
    void sendAction(ActionEvent event) {
    	String t = Control.umpWorking.email;
    	if (t == null) {
    		FxDialogs.showError("Invalid Email", "Email for this umpire seems to be empty");
    		return;
    	}
    	SendEmail.send(t, "New Assignments",  "\n" + msgTxt.getText());
    }

    /**
     * updates the dialog
     */
    @FXML
    void update(KeyEvent event) {
    	saveBtn.setDisable(nameTxt.getText().trim().equals(""));
    	payTxt.setText("$" + Integer.toString(Control.umpWorking.payDue()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
    void initialize() {
        assert umpBackground != null : "fx:id=\"umpBackground\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert nameTxt != null : "fx:id=\"nameTxt\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert emailTxt != null : "fx:id=\"emailTxt\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert levelTxt != null : "fx:id=\"levelTxt\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert m6 != null : "fx:id=\"m6\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert m8 != null : "fx:id=\"m8\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert t6 != null : "fx:id=\"t6\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert t8 != null : "fx:id=\"t8\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert w6 != null : "fx:id=\"w6\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert w8 != null : "fx:id=\"w8\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert th6 != null : "fx:id=\"th6\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert th8 != null : "fx:id=\"th8\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert f6 != null : "fx:id=\"f6\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert f8 != null : "fx:id=\"f8\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert payTxt != null : "fx:id=\"payTxt\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert matchList != null : "fx:id=\"matchList\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert divCol != null : "fx:id=\"divCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert genderCol != null : "fx:id=\"genderCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert timeCol != null : "fx:id=\"timeCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert dateCol != null : "fx:id=\"dateCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert locationCol != null : "fx:id=\"locationCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert paidCol != null : "fx:id=\"paidCol\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert msgTxt != null : "fx:id=\"msgTxt\" was not injected: check your FXML file 'UmpireDialog.fxml'.";
        assert saveBtn != null : "fx:id=\"saveBtn\" was not injected: check your FXML file 'UmpireDialog.fxml'.";

        
        Umpire i = Control.umpWorking;
        nameTxt.setText(i.name);
        emailTxt.setText(i.email);
        levelTxt.setText(Float.toString(i.level));
        
        m6.setSelected(i.availability.get("m6"));
        m8.setSelected(i.availability.get("m8"));
        t6.setSelected(i.availability.get("t6"));
        t8.setSelected(i.availability.get("t8"));
        w6.setSelected(i.availability.get("w6"));
        w8.setSelected(i.availability.get("w8"));
        th6.setSelected(i.availability.get("th6"));
        th8.setSelected(i.availability.get("th8"));
        f6.setSelected(i.availability.get("f6"));
        f8.setSelected(i.availability.get("f8"));
        
        update(null);
        
        
        idCol.setCellValueFactory(new PropertyValueFactory("gameID"));
        divCol.setCellValueFactory(new PropertyValueFactory("division"));
        genderCol.setCellValueFactory(new PropertyValueFactory("gender"));
        timeCol.setCellValueFactory(new PropertyValueFactory("timeStr"));
        dateCol.setCellValueFactory(new PropertyValueFactory("dateStr"));
        locationCol.setCellValueFactory(new PropertyValueFactory("diamond"));
        paidCol.setCellValueFactory(new PropertyValueFactory("paidTxt"));
        
        for (Match t : Control.matches)
        	if (t.startTime.isAfter(MainMenuController.start) && t.startTime.isBefore(MainMenuController.end) && i.assignments.contains(Control.getReference(t)))
        		matchList.getItems().add(t);
        
        msgTxt.setText(i.generateMessage() + "\nPlease text me to confirm");
    }
}
