package gui;

import java.io.IOException;

import domein.GebruikerController;
import domein.SessieKalender;
import domein.SessieKalenderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class SessieKalenderSchermController extends AnchorPane {
	@FXML
	private ListView lvSessies;
	@FXML
	private Label lblAcademieJaar;
	@FXML
	private ComboBox cbMaand;
	@FXML
	private Button btnNieweSessieKlalender;
	@FXML
	private Button btnBeheerSessie;
	@FXML
	private Label lblError;
	@FXML
	private DatePicker inputStartDatum;
	@FXML
	private DatePicker inputEindDatum;
	@FXML
	private Button btnVoegSessieKalenderToe;
	@FXML
	private Label lblErrorVoegSessieKalenderToe;
	
	private SessieKalenderController dc;

//	public SessieKalenderController(GebruikerController dc) {
//		this.dc = dc;
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieKalender.fxml"));
//        loader.setRoot(this);
//        loader.setController(this);
//        try {
//            loader.load();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//	}

	public SessieKalenderSchermController() {
		this.dc = new SessieKalenderController();
	}

	public SessieKalenderSchermController(SessieKalenderController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieKalender.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@FXML
	void voegSessieKalenderToeBtn(ActionEvent event) {
		try {
			System.out.println("Laad nieuw voeg sessie kalender toe scherm");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("SessieKalenderAanmaken.fxml"));
			/*
			 * if "fx:controller" is not set in fxml
			 * fxmlLoader.setController(NewWindowController);
			 */
			Scene scene = new Scene(fxmlLoader.load(), 600, 400);
			Stage stage = new Stage();
			stage.setTitle("Sessie Kalender aanmaken");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}
	
	@FXML
	void voegSessieKalenderToe(ActionEvent event) {
		
		try {
			System.out.println("Voeg sessie Kalender Toe!");
			System.out.println(inputStartDatum.getValue().toString());
			System.out.println(inputEindDatum.getValue().toString());
			if(inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
				throw new IllegalArgumentException("Datum is verplicht in te vullen!");
			
			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
		} catch (Exception e) {
			lblErrorVoegSessieKalenderToe.setVisible(true);
			lblErrorVoegSessieKalenderToe.setText(e.getMessage());
		}
	}
	
}
