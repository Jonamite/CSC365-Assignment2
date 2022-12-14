package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
public class HomeController implements Initializable {
	
	Website Driver;
	@FXML
    private Text LoadingText;	//Red colored text which tells loading status
	
	@FXML
    private TextField UserWebsite;	// text field to enter URL
	
	@FXML
	private Button SearchButton;	//"Search Website" button

	@FXML
    private Text SimilarText;	//Green-colored Similar websites text
	
	@FXML
    private Text SimilarLink1;
	
	// FIND MATCHING URL
	@FXML
	private void FindMatchingURL(ActionEvent event) throws IOException
	{
		try {
			String[] SimilarLinks = Driver.InputFromUser(UserWebsite.getText());
			SimilarText.setText("Website in cluster: " + SimilarLinks[0]);
			SimilarLink1.setText("Most similar website: " + SimilarLinks[1]);
	
		} catch (IOException e) 
		{
			LoadingText.setText(e.toString());
		}
	}
	
	// To load the websites
	@FXML
    private void SearchButton(ActionEvent event) throws IOException 
	{
		LoadingText.setText("Done loading websites. You may input your website now.");
		UserWebsite.setDisable(false);
		SearchButton.setDisable(false);
		try {
			Driver = new Website();
		} catch (IOException e) {
			LoadingText.setText(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		UserWebsite.setDisable(true);
		SearchButton.setDisable(true);
		
		SimilarText.setDisable(true);

		LoadingText.setText("First load websites.");
	}
}
