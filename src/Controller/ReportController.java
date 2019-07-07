package Controller;

import com.jfoenix.controls.JFXTextField;

import application.Control;
import application.Match;
import application.SendEmail;
import application.Umpire;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ReportController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane background;

    @FXML
    private Pane aPane;

    @FXML
    private TextArea messageField;

    @FXML
    private JFXTextField emailField;
    
    @FXML
    private Button sendBtn;

    @FXML
    void sendAction(ActionEvent event) {
    	SendEmail.send(emailField.getText().trim(), "Ops Umpires Pay Report", messageField.getText());
    }
    
    @FXML
    void update(KeyEvent event) {
    	sendBtn.setDisable(emailField.getText().trim().isEmpty());
    }

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'ReportDialog.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'ReportDialog.fxml'.";
        assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'ReportDialog.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'ReportDialog.fxml'.";

        DateTimeFormatter one = DateTimeFormatter.ofPattern("MMMM d");
        String rtn = "Pay Report for " + MainMenuController.start.format(one) + " to " + MainMenuController.end.format(one) + "\n\n";
        int count;
        Match temp;
        
        for (Umpire u : Control.umpires) { // go through all umps
        	count = 0;
        	for (int m : u.assignments) { // count all games to be paid for 
        		temp = Control.getMatch(m);
        		if (temp.getStartTime().isAfter(MainMenuController.start) && temp.getStartTime().isBefore(MainMenuController.end) && !temp.getUmpsPaid()) {        	
        			count++;
        		}
        	}
        	if (count > 0) // if ump needs to be paid, display ump, games umped, and pay due
        		rtn += u.toString() + ":\n"
        				+ "\t- " + count + " game(s) umpired\n"
        				+ "\t- $" + u.payDue() + " owed\n";
        }
        messageField.setText(rtn);
        update(null);
    }
}
