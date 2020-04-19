package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
//	@FXML
//	private Button btnNieweSessieKlalender;
	@FXML
	private Button btnBeheerSessie;
	@FXML
	private Label lblError;
	@FXML
	private Button btnVoegSessieKalenderToe;
	@FXML
	private Label lblStartDatum;
	@FXML
	private Label lblEindDatum;
	@FXML
	private Label lblStartJaar;
	@FXML
	private Label lblEindJaar;
	@FXML
	private Button btnLinks;
	@FXML
	private Button btnRechts;
	@FXML
	private Button btnBeheerSessieKalenders;

	private SessieKalender sk;
	private SessieKalenderController dc;

	public SessieKalenderSchermController() {
		this.dc = new SessieKalenderController();

		initialize();
	}

	public SessieKalenderSchermController(SessieKalenderController dc) {
		this.dc = dc;
		sk = dc.getHuidigeSessieKalender();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieKalender.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initialize();
	}

	public void initialize() {

		// ALs het in commentaar staat moogt ge er mij niet voor judgen
//
//		cbMaand.getItems().addAll(FXCollections.observableArrayList( Arrays.asList(Maand.values()).
//		stream().distinct().
//		map(m -> m.toString())
//		.collect(Collectors.toList())));
		try {
			sk = dc.getHuidigeSessieKalender();
			System.out.println("INITIALIZE");
			lblError.setText("");
			cbMaand.setItems(FXCollections.observableArrayList(Maand.values()));
			cbMaand.setValue(Maand.valueOf(LocalDate.now().getMonthValue()));

			//gives error, should work maybe?
//			cbMaand.getSelectionModel().selectedIndexProperty().addListener((observableValue, value, newValue) -> {
//				if(newValue != null) {
//					lvSessies.setItems(dc.geefSessiesMaand((int)newValue));
//				System.out.println(cbMaand.getSelectionModel().getSelectedIndex());
//				System.out.println(cbMaand.getSelectionModel().getSelectedItem().toString());
//				}
//				}
//			);

			System.out.println(sk.toString());
			lvSessies.setItems(dc.geefSessiesMaand(LocalDate.now().getMonthValue()));
			System.out.println(cbMaand.getSelectionModel().getSelectedIndex());
			System.out.println(cbMaand.getSelectionModel().getSelectedItem().toString());

			lblStartDatum.setText(sk.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblEindDatum.setText(sk.getEindDate().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblStartJaar.setText("" + sk.getStartDate().getYear());
			;
			lblEindJaar.setText("" + sk.getEindDate().getYear());
//			lblStartDatum = new Label(sk.getStartDate().toString());
//			lblEindDatum = new Label(sk.getEindDate().toString());

		} catch (NullPointerException e) {
			lblError.setVisible(true);
			lblError.setText("ERROR " + "Er is geen SessieKalender voor het geselecteerde jaar");
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText("ERROR " + e.toString());
		}
	}

//	@FXML
//	void voegSessieKalenderToeBtn(ActionEvent event) {
//		try {
//
//			Scene scene = new Scene(new SessieKalenderAanmakenSchermController(dc, this), 600, 400);
//			Stage stage = new Stage();
//			stage.setTitle("Sessie Kalender aanmaken");
//			stage.setScene(scene);
//			stage.show();
//		} catch (Exception e) {
//			lblError.setVisible(true);
//			lblError.setText(e.getMessage());
//			System.err.println(e.getMessage());
//		}
//	}

	@FXML
	void volgendeSessieKalender(ActionEvent event) {
		try {
			dc.geefSessieKalender(true);
			initialize();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void vorigeSessieKalender(ActionEvent event) {
		try {
			dc.geefSessieKalender(false);
			initialize();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}
	
	@FXML
	void beheerSessieKalenders(ActionEvent event) {
		try {
		Scene scene = new Scene(new BeheerSessieKalendersController(dc, this), 700, 400);
		Stage stage = new Stage();
		stage.setTitle("Sessie Kalender aanmaken");
		stage.setScene(scene);
		stage.show();
	} catch (Exception e) {
		lblError.setVisible(true);
		lblError.setText(e.getMessage());
		System.err.println(e.getMessage());
	}
	}
}
