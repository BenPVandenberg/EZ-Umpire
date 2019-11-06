// Ben Vandenberg © 2019
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Control;
import application.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane background;

    @FXML
    private Pane aPane;

    @FXML
    void startAction(ActionEvent event) throws IOException, ClassNotFoundException {
    	Stage stage = (Stage) background.getScene().getWindow();
    	stage.close();

    	if (Validation.check(Control.settings.get("key"), Control.settings.get("user"))) {
	    	Stage home = new Stage();
		    Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/MainMenu.fxml"));
		    home.setScene(new Scene(root,990,600));
		    home.setTitle("Main Menu : EZ Umpires");
	    	home.getIcons().add(new Image("/Images/icon.png"));
	    	home.setResizable(false);
	    	home.show();
    	}
    }
    
    @FXML
    void aboutAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) background.getScene().getWindow();
    	stage.close();
    	
    	Stage home = new Stage();
    	Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/About.fxml"));
    	home.setScene(new Scene(root));
    	home.setTitle("About : EZ Umpires");
    	home.getIcons().add(new Image("/Images/icon.png"));
    	home.setResizable(false);
    	home.show();
    }

    @FXML
    void quitAction(ActionEvent event) {
    	System.exit(0);
    }


    @FXML
    void initialize() throws ClassNotFoundException {
        assert background != null : "fx:id=\"welcomeBackground\" was not injected: check your FXML file 'Welcome.fxml'.";
        assert aPane != null : "fx:id=\"aPane\" was not injected: check your FXML file 'Welcome.fxml'.";
        
        try {
        	Control.settings.get("key");
        	Control.settings.get("user");
        }catch (NullPointerException e) {
          Control.settings.put("user", System.getProperty("user.name"));
          Control.settings.put("key", "");
        }
    }
}
