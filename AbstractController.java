package client.common.controllers;

import java.io.IOException;

import client.MainClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * this is the base controller that each screen uses.
 * 
 * @author henco
 * @version 1.0
 */
public abstract class AbstractController implements Initializable {

	/**
	 * this method is responsible for switching scenes in the application. it
	 * recives the fxml file path and the css asotiated with it.
	 * 
	 * @param fxmlFile file we want to show
	 * @param cssFile  css we want to style
	 */
	public void switchScenes(String fxmlFile, String cssFile) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Parent current;
				try {
					double width = MainClientGUI.primaryStage.getWidth();
					double height = MainClientGUI.primaryStage.getHeight();
					MainClientGUI.loader = new FXMLLoader(getClass().getResource(fxmlFile));
					current = (Parent) MainClientGUI.loader.load();
					Scene scene = new Scene(current);
					scene.getStylesheets().add("/client/boundry/StationManagerReports.css");
					MainClientGUI.primaryStage.setScene(scene);
					MainClientGUI.primaryStage.setWidth(width);
					MainClientGUI.primaryStage.setHeight(height);
					MainClientGUI.primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

}