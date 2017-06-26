package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NoteViewLoggedInController implements Initializable {
	
	@FXML
	private Button logInButton;
	
	@FXML
	private Button logOutButton;
	
	@FXML
	private Button settingsButton;
	
	@FXML
	private TextField userName;
	
	@FXML
	private TextField password;
	
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			prepareNotesScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void prepareNotesScreen() throws Exception{
		
		File f = new File("saveNoteFile.txt");
		if(f.exists()){
				Scanner scanner = new Scanner(new FileReader("saveNoteFile.txt"));
				
			StringBuilder sb = new StringBuilder();
			while(scanner.hasNext()){
				sb.append(scanner.next());
			}
			scanner.close();
			String resultString = sb.toString();
			System.out.println(resultString);
			ArrayList<String> notesArrayStrings = new ArrayList<String>(Arrays.asList(resultString.split("~~")));
			System.out.println(notesArrayStrings.size());
			ArrayList<ArrayList<String>> listOfNotesLists = new ArrayList<ArrayList<String>>();
			for(int i=0; i<notesArrayStrings.size(); i++){
				listOfNotesLists.add(new ArrayList<String>(Arrays.asList(notesArrayStrings.get(i).split(":"))));
			}
			System.out.println(listOfNotesLists.get(0).size());
		
			for(int j=0; j<listOfNotesLists.size(); j++){
				yourNotesArea.setSpacing(20.0);
				yourNotesArea.setPadding(new Insets(20));
				yourNotesArea.getChildren().add(new Button(listOfNotesLists.get(j).get(0)));
			}
		}
	}

	public void logOutButtonClicked(ActionEvent event) throws IOException{
		
		System.out.println("User logged out...");
		
		Parent parent2 = FXMLLoader.load(getClass().getResource("sample.fxml"));	
		Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
		Scene loggedOutScene = new Scene(parent2, (window.getWidth()-16), (window.getHeight()-38));
		loggedOutScene.getStylesheets().add("resources/css/test.css");
		window.setScene(loggedOutScene);
		window.show();
	}
	
	public void saveNoteLocally(ActionEvent event) throws Exception{
		System.out.println("Saving Note Locally");
		
		File f = new File("saveNoteFile.txt");
		String title = titleNote.getText();
		String body = noteBody.getText();
		
		if(f.exists()){
			System.out.println("Note already exists");
			FileWriter fw = new FileWriter("saveNoteFile.txt",true);
			try{
				fw.write(title + ":" + body + "?" + "some text ~~");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if(fw!=null){
					fw.close();
				}
			}
		}
		else{
			System.out.println("Note does not already exist");
			BufferedWriter out = new BufferedWriter(new FileWriter("saveNoteFile.txt"));
			out.write(title + ":" + body + "?" + "initial text ~~");
			out.close();
		}
	}
}
