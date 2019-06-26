// Ben Vandenberg © 2019
package application;
	
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {			
			Parent root = FXMLLoader.load(getClass().getResource("/FXML_Files/Welcome.fxml"));
			Scene scene = new Scene(root,412,366);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Welcome : EZ Umpires");
			primaryStage.getIcons().add(new Image("/Images/icon.png"));
			primaryStage.show();
		} catch(Exception e) {
			FxDialogs.showException("Unkown Error", "An unknown error has occcured, please report this bug. \n The app will now close", e);
			primaryStage.close();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
