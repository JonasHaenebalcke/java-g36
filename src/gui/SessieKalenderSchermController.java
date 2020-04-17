package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import domein.GebruikerController;
import domein.Maand;
import domein.Sessie;
import domein.SessieKalender;
import domein.SessieKalenderController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class SessieKalenderSchermController extends AnchorPane {
	@FXML
	private ListView<Sessie> lvSessies;// DataType String?
	@FXML
	private Label lblAcademieJaar;
	@FXML
	private ChoiceBox<Maand> cbMaand;
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
	@FXML
	private Label lblStartDatum;
	@FXML
	private Label lblEindDatum;
	@FXML
	private Button btnLinks;
	@FXML
	private Button btnRechts;

	SessieKalender sk;
	private SessieKalenderController dc;

	public SessieKalenderSchermController() {
		this.dc = new SessieKalenderController();
		initialize();
	}

	public SessieKalenderSchermController(SessieKalenderController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieKalender.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			System.out.println("hello");
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initialize();
	}

	private void initialize() {
		
		//ALs het in commentaar staat moogt ge er mij niet voor judgen
		
//		cbMaand.getItems().addAll(FXCollections.observableArrayList( Arrays.asList(Maand.values()).
//		stream().distinct().
//		map(m -> m.toString())
//		.collect(Collectors.toList())));
		cbMaand.setItems(FXCollections.observableArrayList( Maand.values()));
//		try {
//			for (SessieKalender sk1 : dc.geefSessieKalenderList()) {
//				if (sk1.getStartDate().isAfter(LocalDate.now()) && sk1.getEindDate().isBefore(LocalDate.now())) {
//					this.sk = sk1;
//				}
//			}
//			
//			lblStartDatum = new Label(sk.getStartDate().toString());
//			lblStartDatum = new Label(sk.getEindDate().toString());
////			lvSessies.setItems(sk.geefSessiesMaand(LocalDate.now().getMonthValue()));
//		} catch (Exception e) {
//			lblError.setText(e.getMessage());
//		}
//	

	}

	@FXML
	void voegSessieKalenderToeBtn(ActionEvent event) {
		try {
		
			Scene scene = new Scene(new SessieKalenderAanmakenSchermController(dc), 600, 400);
			Stage stage = new Stage();
			stage.setTitle("Sessie Kalender aanmaken");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void voegSessieKalenderToe(ActionEvent event) {

		try {
			System.out.println("Voeg sessie Kalender Toe!");
			System.out.println(inputStartDatum.getValue().toString());
			System.out.println(inputEindDatum.getValue().toString());
			if (inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
				throw new IllegalArgumentException("Datum is verplicht in te vullen!");

			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
		} catch (Exception e) {
			lblErrorVoegSessieKalenderToe.setVisible(true);
			lblErrorVoegSessieKalenderToe.setText(e.getMessage());
		}
	}

}
