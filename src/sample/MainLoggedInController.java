package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

	private String txtFileName;


	/**
	 * Initialization class (like a constructor)
	 * @param location
	 * @param resources
	 */
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


	/**
	 *Reads in JSON from the txt file. Creates a note object for each note element in the JSON.
	 *Creates a new button for each note (getting this value from a JSon array of jsonobjects).
	 * @throws Exception
	 */
	public void prepareNotesScreen() throws Exception {

		File f = new File(txtFileName);
		noteList = new ArrayList<Note>();
		if (f.exists()) {
//			noteList = new ArrayList<Note>();
			int count = 0;
			// Reading from textFile and printing to console
			Scanner jsonScanner = new Scanner(new FileReader(txtFileName));
			String jsonFile = "";
			jsonFile += jsonScanner.nextLine();
			jsonScanner.close();
			// Read from noteString and create new notes, adding actionListener
			// to a created button which is then added to the NotesArea
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(jsonFile.toString());
			if (element.isJsonObject()) {
				JsonObject albums = element.getAsJsonObject();
				JsonArray datasets = albums.getAsJsonArray(userNameTextField.getText());
				for (int i = 0; i < datasets.size(); i++) {
					if (datasets.size() > 0) {
						JsonObject dataset = datasets.get(i).getAsJsonObject();
						Note note = new Note(dataset.get("noteHeader").getAsString(),
								dataset.get("noteBody").getAsString());
						noteList.add(note);
						Button button = new Button(note.getTitle());
						button.setMaxWidth(Double.MAX_VALUE);
						button.setId("noteSelectButton" + count);
						button.getStyleClass().add("noteyButtons");
						if (yourNotesArea.isVisible() == false) {
							yourNotesArea.setVisible(true);
						}
						button.setOnAction(this::noteArchiveSelected);
						yourNotesArea.getChildren().add(button);
						count++;
					} else {
						if (yourNotesArea.isVisible())
							yourNotesArea.setVisible(false);
					}
				}
			}
			footerLabel.setText("Welcome " + userNameTextField.getText());
		}
	}

	/**
	 * If the title of the current selected notelist item is not equal to the text of the variable currentNoteSelected,
	 * I add the note to a tempNoteList. I then set the noteList to be equal to the tempNoteList.
	 * @param event
	 * @throws IOException
	 */
	public void noteDeleteClicked(ActionEvent event) throws IOException {
		File f = new File(txtFileName);

		ArrayList<Note> tempNoteList = new ArrayList<Note>();

		if (f.exists()) {

			// make new array list with updated notes
			for (int i = 0; i < noteList.size(); i++) {
				if (currentNoteSelected.getText() != noteList.get(i).getTitle()) {
					tempNoteList.add(noteList.get(i));
				}
			}
			noteList = tempNoteList;
			clearDownFile();
			JsonObject jsonO = createJson();
			FileWriter fw = new FileWriter(txtFileName, true);

			writeToFile(jsonO, fw);

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
		}
	}

	// When note is selected
	/**
	 * When a note button is selected, changes the css of the button and currentNoteSelected is updated
	 * @param event
	 */
	public void noteArchiveSelected(ActionEvent event) {

		if (currentNoteSelected != null) {
			currentNoteSelected.getStyleClass().remove("noteyButtonSelect");
			currentNoteSelected.getStyleClass().add("noteyButtonDeselect");
		}

		Button newPressedButton = (Button) event.getSource();
		newPressedButton.getStyleClass().add("noteyButtonSelect");
		newPressedButton.getStyleClass().remove("noteyButtonDeselect");


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
	/**
	 * The NotesArea, title and body text areas are cleared and the screen is redrawn
	 * @param event
	 */
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

	/**
	 * This creates a new scene and stage based on the fxml document for the login screen.
	 * @param event
	 * @throws IOException
	 */
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

	/**
	 * Determines whether to add a new note, or edit an existing note depending on whether a note button is currently selected.
	 * @param event
	 * @throws Exception
	 */
	public void saveNoteLocally(ActionEvent event) throws Exception {
		// Write to textFile

		if (isAnExistingNoteSelected == false) {
			addNewNote();
		}
		// The below code is executed when an archived note is selected for
		// editing and save is pressed
		else {
			editExistingNote();
		}
	}


	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void editExistingNote() throws FileNotFoundException, IOException {
		File f = new File(txtFileName);
		String title = titleNote.getText();
		String body = noteBody.getText();
		if (f.exists()) {

			// make new array list with updated notes
			for (int i = 0; i < noteList.size(); i++) {
				if (currentNoteSelected.getText().equals(noteList.get(i).getTitle())
						&& noteList.get(i).getBody() != body) {
					Note newNote = new Note(title, body);
					noteList.set(i, newNote);
				}
			}
			clearDownFile();
			JsonObject jsonO = createJson();
			FileWriter fw = new FileWriter(txtFileName, true);

			writeToFile(jsonO, fw);

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


	/**
	 * Creates a Json object, clears down the text file, writes details to json object, writes json object to the text file, redraws screen.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addNewNote() throws FileNotFoundException, IOException {
		File f = new File(txtFileName);
		Note note = new Note(titleNote.getText(), noteBody.getText());
		///////////////////////
//		noteList = new ArrayList<Note>();
		///////////////////////
		noteList.add(note);
		JsonObject jsonO = createJson();

		clearDownFile();

		if (f.exists()) {
			FileWriter fw = new FileWriter(txtFileName, true);

			writeToFile(jsonO, fw);

		} else {
			System.out.println("f does not exist");
			BufferedWriter out = new BufferedWriter(new FileWriter(txtFileName));
			try { 
				out.write(jsonO.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
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
	}

	/**
	 * Returns a Json object from data in noteList
	 * @return {@link JsonObject}
	 */
	private JsonObject createJson() {
		JsonArray jArray = new JsonArray();
		for (Note toSave : noteList) {
			JsonObject note2 = new JsonObject();
			note2.addProperty("noteHeader", toSave.getTitle());
			note2.addProperty("noteBody", toSave.getBody());
			jArray.add(note2);

		}

		JsonObject jsonO = new JsonObject();
		String currentUserName = userNameTextField.getText();
		jsonO.add(currentUserName, jArray);
		return jsonO;
	}

	/**
	 * Creates a new PrintWriter based on the text file, sets the text file to ""
	 * .text file cleared is defined in @param txtFileName
	 * @throws FileNotFoundException
	 */
	private void clearDownFile() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(txtFileName);
		writer.print("");
		writer.close();
	}

	/**
	 * Writes the given Json object to the filewriter
	 * @param jsonO {@link JsonObject}
	 * @param fw {@link FileWriter}
	 * @throws IOException
	 */
	private void writeToFile(JsonObject jsonO, FileWriter fw) throws IOException {
		try {
			fw.write(jsonO.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.close();
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
