package main;

import java.time.LocalDate;
import java.util.Date;

import domein.GebruikerController;
import domein.PopulateDB;
import domein.SessieController;
import domein.SessieKalenderController;
import gui.GebruikersSchermController;
import gui.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application{
	
	@Override
    public void start(Stage primaryStage) {
		PopulateDB populatedb = new PopulateDB();
//		populatedb.run(); //Als ge db wilt resetten, moet ge setter van startdatumsessiekalender ook aanpassen
		SessieController sc = new SessieController();
		GebruikerController dc = new GebruikerController();
		dc.geefGebruikersList().forEach(g -> System.out.println(g.toString()));
		SessieKalenderController skc = new SessieKalenderController(sc);
		skc.geefSessieKalenderList().forEach(g -> System.out.println(g.toString()));
        MenuController root = new MenuController(dc, skc);
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
