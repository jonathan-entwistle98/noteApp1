package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainModel extends Application {
	
	private boolean isLoggedIn = false;

	/* 
	 * Creates a new Scene, adds this scene to the stage and sets the css and fxml files to be used.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Notes Application");
		Scene scene = new Scene(root, 800, 500);
		scene.getStylesheets().add("resource/test.css");
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icon.png")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	public void logInStatusChange(TextField userName, Button logInButton) {
		
		isLoggedIn = true;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("loggedInSample.fxml"));
		MainLoggedInController mainLoggedInController = new MainLoggedInController();
		mainLoggedInController.setUserData(userName);
		loader.setController(mainLoggedInController);// Not sure about this
														// line
		Scene scene = logInButton.getScene();
		try {
			Parent pane = loader.load();
			Scene loggedInScene = new Scene(pane, scene.getWidth(), scene.getHeight());
			loggedInScene.getStylesheets().add("resource/test.css");
			Stage loggedInStage = (Stage) scene.getWindow();
			loggedInStage.setScene(loggedInScene);
			loggedInStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
