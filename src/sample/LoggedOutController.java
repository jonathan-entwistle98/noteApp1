package sample;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoggedOutController {

	@FXML
	private Button logInButton;

	@FXML
	private Button logOutButton;

	@FXML
	private Button settingsButton;

	@FXML
	private TextField userName;

	@FXML
	private PasswordField password;

	@FXML
	private Label userNameLabel;

	@FXML
	private HBox footerArea;

	@FXML
	private VBox leftArea;

	@FXML
	private VBox yourNotesArea;

	@FXML
	private Button saveNote;

	@FXML
	private TextField titleNote;

	@FXML
	private TextArea noteBody;

	@FXML
	private ImageView imageView;
	
	@FXML
	private Label footerText;
	
	private Calendar cal;
	private SimpleDateFormat sdf;
	private String mainTime;
	

	
	public void initialize() {
//		currentTime();
		DigitalClock clock1 = new DigitalClock(); 
		footerArea.getChildren().remove(footerText);
		footerArea.getChildren().add(clock1);
//		bindToTime();
		System.out.println(mainTime);
//		mainTime.
//		footerText.setText(mainTime);
//		DigitalClock clock1 = new DigitalClock();
//		footerText = clock1;		
	}
	
	private void bindToTime() {
		  Timeline timeline = new Timeline(
		    new KeyFrame(Duration.seconds(0),
		      new EventHandler<ActionEvent>() {
		        @Override public void handle(ActionEvent actionEvent) {
		          Calendar time = Calendar.getInstance();
		          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		          mainTime=simpleDateFormat.format(time.getTime()).toString();
		        }
		      }
		    ),
		    new KeyFrame(Duration.seconds(1))
		  );
		  timeline.setCycleCount(Animation.INDEFINITE);
		  timeline.play();
	}
		
//	
//	public void currentTime(){
//		cal = Calendar.getInstance();
//        sdf = new SimpleDateFormat("HH:mm:ss");
//       mainTime = sdf.format(cal.getTime());
//	}
	
//	public void refreshTime(){
//		Timer timer = new Timer();
//		timer.sceduleAtFixedRate(new TimerTask(){
//			
//		});
//	}

	public void loginButtonClicked(ActionEvent event) throws IOException {
		String uNameText = userName.getText();
		//String passwordText = password.getText();

		if (uNameText.isEmpty()) {
			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle("Alert: Invalid Username");
			alert1.setHeaderText("Invalid Username");
			alert1.setContentText("Please Enter a Value Into the Username Field and Try Again.");
			alert1.showAndWait();
		} else {
			System.out.println(userName.getText());
			System.out.println("User logged in...");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("loggedInSample.fxml"));
			MainLoggedInController mainLoggedInController = new MainLoggedInController();
			mainLoggedInController.setUserData(userName);
			loader.setController(mainLoggedInController);//Not sure about this line
			Scene scene = logInButton.getScene();
			try {
				Parent pane = loader.load();
				Scene loggedInScene = new Scene(pane, scene.getWidth(), scene.getHeight());
				loggedInScene.getStylesheets().add("resources/css/test.css");
				Stage loggedInStage = (Stage) scene.getWindow();
				loggedInStage.setScene(loggedInScene);
				loggedInStage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	

	
}
