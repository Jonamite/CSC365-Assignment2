package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Home extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//Program start
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("HOMEPAGE.fxml"));

						Scene scene = new Scene(root,800,400);
					
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setResizable(true);
						
						primaryStage.setMinWidth(500);
						primaryStage.setMinHeight(530);
						
						
						primaryStage.setMaxWidth(970);
						primaryStage.setMaxHeight(680);
						
						primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}
}
