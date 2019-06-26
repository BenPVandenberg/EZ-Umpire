// Ben Vandenberg © 2019
package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CustomDateController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane background;

    @FXML
    private Pane aPane;

    @FXML
    private JFXDatePicker startDP,endDP;

    @FXML
    private JFXButton setBtn;

    @FXML
    void cancelAction(ActionEvent event) {
    	Stage stage = (Stage) background.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void setAction(ActionEvent event) {
    	MainMenuController.start = LocalDateTime.of(startDP.getValue(), LocalTime.of(0, 0));
    	MainMenuController.end   = LocalDateTime.of(endDP.getValue(),   LocalTime.of(23, 59));
    	Stage stage = (Stage) background.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void update(ActionEvent event) {
    	update();
    }
    
    void update() {
    	boolean t = (startDP.getValue() == null || endDP.getValue() == null);
    	if (!t)
    		t = endDP.getValue().isBefore(startDP.getValue());
    	setBtn.setDisable(t);
    }

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'CustomDateRange.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'CustomDateRange.fxml'.";
        assert startDP != null : "fx:id=\"startDP\" was not injected: check your FXML file 'CustomDateRange.fxml'.";
        assert endDP != null : "fx:id=\"endDP\" was not injected: check your FXML file 'CustomDateRange.fxml'.";
        assert setBtn != null : "fx:id=\"setBtn\" was not injected: check your FXML file 'CustomDateRange.fxml'.";
        
        update();
    }
}
