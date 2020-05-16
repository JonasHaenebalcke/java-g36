package main;

import java.time.LocalDate;
import java.util.Date;

import domein.AankondigingController;
import domein.GebruikerController;
import domein.PopulateDB;
import domein.SessieController;
import domein.SessieKalenderController;
import gui.GebruikersSchermController;
import gui.MeldAanSchermController;
import gui.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application {

	@Override
	public void start(Stage primaryStage) {
		PopulateDB populatedb = new PopulateDB();
//		populatedb.run(); // Als ge db wilt resetten, moet ge eerst dotnet runnen
		GebruikerController gc = new GebruikerController();
		SessieKalenderController skc = new SessieKalenderController();
		SessieController sc = new SessieController(gc);
//		gc.geefGebruikersList().forEach(g -> System.out.println(g.toString()));

//		skc.geefSessieKalenderList().forEach(g -> System.out.println(g.toString()));
//		sc.geefSessies().forEach(s -> System.out.println(s.toString()));
//        MenuController root = new MenuController(dc, skc, sc);
		AankondigingController ac = new AankondigingController();
		MeldAanSchermController root = new MeldAanSchermController(gc, skc, sc, ac);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("gui/stylesheet.css");
		primaryStage.setMaximized(true);
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
