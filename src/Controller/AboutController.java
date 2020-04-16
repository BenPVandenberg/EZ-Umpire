// Ben Vandenberg © 2019
package Controller;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane background;

    @FXML
    private Pane aPane;

    @FXML
    private JFXButton backBtn;

    @FXML
    void backAction(ActionEvent event) throws IOException {
    	
    	Stage stage = (Stage) background.getScene().getWindow();
    	stage.close();
    	
    	Stage home = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/Welcome.fxml"));
    	home.setScene(new Scene(root));
    	home.setTitle("Welcome : EZ Umpires");
    	home.getIcons().add(new Image("/Images/icon.png"));
    	home.setResizable(false);
    	home.show();
    }

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'About.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'About.fxml'.";
        assert backBtn != null : "fx:id=\"quitBtn\" was not injected: check your FXML file 'About.fxml'.";
    }
}
