package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainLoggedInController implements Initializable {

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
	private Label footerLabel;

	@FXML
	private Button deleteButton;

	@FXML
	private ScrollPane noteScrollPane;

	@FXML
	private Button addNewNote;

	private TextField userNameTextField;

	private ArrayList<Note> noteList;

	private Button currentNoteSelected;

	private Boolean isAnExistingNoteSelected = false;

	private boolean checkANoteExists = false;

	private String txtFileName;

	private Button previousNoteButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			createTextFileForCurrentUser();
			DigitalClock clock1 = new DigitalClock();
			footerArea.getChildren().remove(footerLabel);
			footerArea.getChildren().add(clock1);
			prepareNotesScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Reads saveNoteFile.txt, saving it as a string. Then splits the string and
	// saves the resultant strings in string1 and string2
	// For each "~~"(each line in the txt file), creates a Note object using
	// string1 and string2 and creates a button, adding the button
	// to the NotesArea VBox
	public void prepareNotesScreen() throws Exception {

		File f = new File(txtFileName);
		if (f.exists()) {
			Scanner scanner = new Scanner(new FileReader(txtFileName));
			String inputText = "";
			while (scanner.hasNext()) {
				inputText += scanner.nextLine();
			}
			scanner.close();
			noteList = new ArrayList<Note>();
			int count = 0;

			for (String noteString : inputText.split("~~")) {
				if (noteString != null && noteString != "" && noteString != " " && noteString.split(":")[1] != "") {
					checkANoteExists = true;
					String string1 = noteString.split(":")[0];
					String string2 = noteString.split(":")[1];
					Note note = new Note(string1, string2);
					noteList.add(note);
					Button button = new Button(note.getTitle());
					button.setMaxWidth(Double.MAX_VALUE);
					button.setId("noteSelectButton" + count);
					button.getStyleClass().add("noteyButtons");
					if(yourNotesArea.isVisible()==false){
						yourNotesArea.setVisible(true);
					}
					button.setOnAction(this::noteArchiveSelected);
					yourNotesArea.getChildren().add(button);
					count++;
				}
				//If there are no notes
				else{
					if(yourNotesArea.isVisible())
					yourNotesArea.setVisible(false);
				}
			}
			footerLabel.setText("Welcome " + userNameTextField.getText());
		}
	}

	public void noteDeleteClicked(ActionEvent event) throws IOException {

		File inputFile = new File(txtFileName);
		File tempFile = new File("myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String oldTitleOfNote = null;
		String oldBodyOfNote = null;
		for (int i = 0; i < yourNotesArea.getChildren().size(); i++) {
			if (currentNoteSelected == yourNotesArea.getChildren().get(i)) {
				oldTitleOfNote = noteList.get(i).getTitle();
				oldBodyOfNote = noteList.get(i).getBody();
			}
		}

		String lineToRemove = oldTitleOfNote + ":" + oldBodyOfNote + "~~";
		String currentLine;

		while ((currentLine = reader.readLine()) != null) {
			String trimmedCurrentLine = currentLine.trim();
			if (trimmedCurrentLine == lineToRemove) {
			}
			if (trimmedCurrentLine.equals(lineToRemove))
				continue;
			writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close();
		reader.close();
		inputFile.delete();
		boolean successful = tempFile.renameTo(inputFile);
		if (!successful) {
		}

		// These lines below redraw the screen, so the deleted note disappears
		titleNote.clear();
		noteBody.clear();
		yourNotesArea.getChildren().clear();
		try {
			prepareNotesScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// When note is selected
	public void noteArchiveSelected(ActionEvent event) {
		// This un-selects the previous pressed note button
		// if(currentNoteSelected!=null){
		// currentNoteSelected.setStyle(
		// "-fx-background-color: #f7f7f7;"
		// + "-fx-background-insets: 0,1,4,5;"
		// + "-fx-background-radius: 9,8,5,4;"
		// + "-fx-padding: 15 30 15 30;"
		// + "-fx-font-size: 12px;"
		// + "-fx-text-fill: #333333;"
		// + "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) ,
		// 1, 0.0 , 0 , 1);"
		// );
		// }
		if (currentNoteSelected != null) {
			currentNoteSelected.getStyleClass().remove("noteyButtonSelect");
			currentNoteSelected.getStyleClass().add("noteyButtonDeselect");
		}

		Button newPressedButton = (Button) event.getSource();
		newPressedButton.getStyleClass().add("noteyButtonSelect");
		newPressedButton.getStyleClass().remove("noteyButtonDeselect");

		// newPressedButton.setStyle("-fx-background-color: #8da6fc;"
		// + "-fx-background-insets: 0,1,4,5;"
		// + "-fx-background-radius: 9,8,5,4;"
		// + "-fx-padding: 15 30 15 30;"
		// + "-fx-font-size: 12px;"
		// + "-fx-text-fill: #333333;"
		// + "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) ,
		// 1, 0.0 , 0 , 1)"
		// );;
		this.currentNoteSelected = newPressedButton;
		isAnExistingNoteSelected = true;

		for (int i = 0; i < yourNotesArea.getChildren().size(); i++) {
			if (newPressedButton == yourNotesArea.getChildren().get(i)) {
				titleNote.setText(noteList.get(i).getTitle());
				noteBody.setText(noteList.get(i).getBody());
			}
		}
	}

	// When the new note button is pressed
	public void newNoteButtonClicked(ActionEvent event) {
		this.isAnExistingNoteSelected = false;
		titleNote.clear();
		noteBody.clear();
		yourNotesArea.getChildren().clear();
		try {
			prepareNotesScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logOutButtonClicked(ActionEvent event) throws IOException {

		Parent parent2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		Scene loggedOutScene = new Scene(parent2, (window.getWidth() - 16), (window.getHeight() - 38));
		loggedOutScene.getStylesheets().add("resource/test.css");
		window.setScene(loggedOutScene);
		window.show();
		resetTextFileForCurrentUser();
		isAnExistingNoteSelected = false;
	}

	public void saveNoteLocally(ActionEvent event) throws Exception {
		if (isAnExistingNoteSelected == false) {
			File f = new File(txtFileName);
			String title = titleNote.getText();
			String body = noteBody.getText();

			if (f.exists()) {
				FileWriter fw = new FileWriter(txtFileName, true);
				try {
					fw.write(title + ":" + body + "~~" + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fw != null) {
						fw.close();
					}
				}

				// These lines below redraw the screen, so the saved note
				// appears
				yourNotesArea.getChildren().clear();
				try {
					prepareNotesScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				BufferedWriter out = new BufferedWriter(new FileWriter(txtFileName));
				out.write(title + ":" + body + "~~");
				out.close();

				// These lines below redraw the screen, so the saved note
				// appears
				yourNotesArea.getChildren().clear();
				try {
					prepareNotesScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// The below code is executed when an archived note is selected for
		// editing and save is pressed
		else {
			File f = new File(txtFileName);
			String title = titleNote.getText();
			String body = noteBody.getText();

			if (f.exists()) {
				FileWriter fw = new FileWriter(txtFileName, true);
				try {
					fw.write(title + ":" + body + "~~" + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fw != null) {
						fw.close();
					}
				}

				File tempFile = new File("myTempFile.txt");

				BufferedReader reader = new BufferedReader(new FileReader(f));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

				String oldTitleOfNote = null;
				String oldBodyOfNote = null;
				// For each note button
				for (int i = 0; i < yourNotesArea.getChildren().size(); i++) {
					if (currentNoteSelected == yourNotesArea.getChildren().get(i)) {
						oldTitleOfNote = noteList.get(i).getTitle();
						oldBodyOfNote = noteList.get(i).getBody();
					}
				}

				String lineToRemove = oldTitleOfNote + ":" + oldBodyOfNote + "~~";
				String currentLine;

				while ((currentLine = reader.readLine()) != null) {
					String trimmedCurrentLine = currentLine.trim();
					if (trimmedCurrentLine.equals(lineToRemove))
						continue;
					if (trimmedCurrentLine == lineToRemove) {
					}
					writer.write(currentLine + System.getProperty("line.separator"));
				}
				writer.close();
				reader.close();
				File tempFile2 = f;
				f.delete();
				boolean successful = tempFile.renameTo(tempFile2);
				if (!successful) {
					System.out.println("this was not successful");
				}

				// These lines below redraw the screen, so the deleted note
				// disappears
				titleNote.clear();
				noteBody.clear();
				yourNotesArea.getChildren().clear();
				try {
					prepareNotesScreen();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Note text file does not exist so note cannot be edited");
			}

		}
	}

	public void setUserData(TextField userName) {
		this.userNameTextField = userName;
	}

	public void createTextFileForCurrentUser() {
		// This won't work if two users have the same username
		this.txtFileName = this.userNameTextField.getText() + ".txt";
	}

	public void resetTextFileForCurrentUser() {
		this.txtFileName = null;
	}
}
