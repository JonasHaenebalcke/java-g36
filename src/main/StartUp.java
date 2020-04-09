package main;

import domein.GebruikerController;
import gui.GebruikersSchermController;
import gui.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application{
	
	@Override
    public void start(Stage primaryStage) {
		GebruikerController dc = new GebruikerController();
		
		dc.GeefGebruikersList().forEach(g -> System.out.println(g.toString()));
        MenuController root = new MenuController(dc);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("gui/stylesheet.css");
        
        primaryStage.setTitle("ITLab");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
