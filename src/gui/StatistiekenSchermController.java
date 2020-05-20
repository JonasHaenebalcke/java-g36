package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.Sessie;
import domein.SessieController;
import domein.SessieKalenderController;
import domein.StatusSessie;
import domein.TypeGebruiker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.beans.property.ReadOnlyStringWrapper;

public class StatistiekenSchermController extends GridPane {

	@FXML
	private TableView<Gebruiker> tvGebruikers;

	@FXML
	private TableView<Sessie> tvSessies;

	@FXML
	private TableColumn<Gebruiker, String> colNaam;

	@FXML
	private TableColumn<Gebruiker, String> colVoornaam;

	@FXML
	private TableColumn<Gebruiker, String> colType;

	@FXML
	private TableColumn<Gebruiker, String> colStatus;

	@FXML
	private TableColumn<Gebruiker, String> colAantalFeedbacks;
	@FXML
	private TableColumn<Gebruiker, String> colAantalAanwezig;
	@FXML
	private TableColumn<Gebruiker, String> colAantalAfwezig;
	@FXML
	private TableColumn<Gebruiker, String> colProcentueelAanwezig;
	@FXML
	private Label lblStatistiek1Omschrijving;

	@FXML
	private Label lblStatistiek2Omschrijving;

	@FXML
	private Label lblStatistiek1Value;

	@FXML
	private Label lblStatistiek2Value;
	@FXML
	private Label lblVoorAlle;
	@FXML
	private Label lblTitel;

	@FXML
	private TableColumn<Sessie, String> colVerantwoordelijke;

	@FXML
	private TableColumn<Sessie, String> colTitel;

	@FXML
	private TableColumn<Sessie, String> colStart;

	@FXML
	private TableColumn<Sessie, String> colStartUur;

	@FXML
	private TableColumn<Sessie, String> colAantalIngeschrevenen;
	@FXML
	private TableColumn<Sessie, String> colDuur;
	@FXML
	private TableColumn<Sessie, String> colAantalAanwezigen;
	@FXML
	private TableColumn<Sessie, String> colGemiddleScore;

	@FXML
	private TextField txfSessie;
	@FXML
	private Button zoekSessie;
	@FXML
	private ComboBox<String> cbxStatusSessie;
	@FXML
	private ComboBox<String> cbxSessieGegevens;

	@FXML
	private ComboBox<String> cbxType;

	private GebruikerController gc;
	private SessieController sc;

	public StatistiekenSchermController(GebruikerController dc, SessieController sc) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StatistiekenScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.gc = dc;
		this.sc = sc;
		initialize();
	}

	private void initialize() {
		lblTitel.setText("Gebruikers");
		lblVoorAlle.setText("Voor alle gebruikers");
		lblStatistiek1Omschrijving.setText("Totaal aantal keer aanwezig");
		lblStatistiek2Omschrijving.setText("Totaal aantal keer afwezig");
		lblStatistiek1Value.setText(String.valueOf(gc.geefAantalAanwezigen()));
		lblStatistiek2Value.setText(String.valueOf(gc.geefAantalAfwezigen()));

		colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());

		tvSessies.setVisible(false);
		tvGebruikers.setVisible(true);
		txfSessie.setVisible(true);
		cbxStatusSessie.setVisible(false);
		cbxSessieGegevens.setVisible(false);
		zoekSessie.setVisible(true);
		tvGebruikers.setItems(gc.geefGebruikersSorted());
		colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
		colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
		colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
		colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
		colAantalFeedbacks.setCellValueFactory(cel -> cel.getValue().getAantalFeedbacksProperty());
		colAantalAanwezig.setCellValueFactory(cel -> cel.getValue().getAantalAanwezigProprty());
		colAantalAfwezig.setCellValueFactory(cel -> cel.getValue().getAantalAfwezigProperty());
		colProcentueelAanwezig.setCellValueFactory(cel -> cel.getValue().getProcentueelAanwezigProperty());

		List<String> statussen = new ArrayList<>();
		statussen.add("Alle Types");
		for (StatusSessie status : StatusSessie.values()) {
			statussen.add(status.toString());
		}
		cbxStatusSessie.setItems(FXCollections.observableArrayList(statussen));
		cbxStatusSessie.getSelectionModel().selectFirst();

		List<String> sorters = new ArrayList<>();
		sorters.add("Ongesorteerd");
		sorters.add("Bij score");
		sorters.add("Bij aanwezigen");
		sorters.add("Bij duur");
		cbxSessieGegevens.setItems(FXCollections.observableArrayList(sorters));
		cbxSessieGegevens.getSelectionModel().selectFirst();

		List<String> types = new ArrayList<>();
		types.add("Alle");
		for (TypeGebruiker type : TypeGebruiker.values()) {
			types.add(type.toString());
		}
		cbxType.setItems(FXCollections.observableArrayList(types));
		cbxType.getSelectionModel().selectFirst();
		typeChangeListener();
	}

	@FXML
	void geefStatistiekenGebruikers(ActionEvent event) {
		lblTitel.setText("Gebruikers");
		lblVoorAlle.setText("Voor alle gebruikers");

		txfSessie.setText("");
		txfSessie.setPromptText("Gebruiker");
		cbxType.setVisible(true);
		cbxStatusSessie.setVisible(false);
		cbxSessieGegevens.setVisible(false);
		lblStatistiek1Omschrijving.setText("Totaal aantal keer aanwezig");
		lblStatistiek2Omschrijving.setText("Totaal aantal keer afwezig");
		lblStatistiek1Value.setText(String.valueOf(gc.geefAantalAanwezigen()));
		lblStatistiek2Value.setText(String.valueOf(gc.geefAantalAfwezigen()));
		tvSessies.setVisible(false);
		tvGebruikers.setVisible(true);
		tvGebruikers.setItems(gc.geefGebruikersSorted());
		colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
		colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
		colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
		colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
		colAantalFeedbacks.setCellValueFactory(cel -> cel.getValue().getAantalFeedbacksProperty());
		colAantalAanwezig.setCellValueFactory(cel -> cel.getValue().getAantalAanwezigProprty());
		colAantalAfwezig.setCellValueFactory(cel -> cel.getValue().getAantalAfwezigProperty());
		colProcentueelAanwezig.setCellValueFactory(cel -> cel.getValue().getProcentueelAanwezigProperty());
		changeFilterGebruikers();
	}

	@FXML
	void geefStatistiekenSessies(ActionEvent event) {
		lblTitel.setText("Sessies");
		lblVoorAlle.setText("Voor alle sessies");

		txfSessie.setText("");
		txfSessie.setPromptText("Sessie");
		cbxType.setVisible(false);
		txfSessie.setVisible(true);
		cbxStatusSessie.setVisible(true);
		cbxSessieGegevens.setVisible(true);
		zoekSessie.setVisible(true);
		lblStatistiek1Omschrijving.setText("Populairste start uur");
		lblStatistiek2Omschrijving.setText("Populairste duratie van een sessie");
		lblStatistiek1Value.setText(sc.geefPopulaireStartUur());
		lblStatistiek2Value.setText(String.valueOf(sc.geefPopulaireDuur()));
		tvGebruikers.setVisible(false);
		tvSessies.setVisible(true);
		tvSessies.setItems(sc.geefSessiesSorted());
		colVerantwoordelijke.setCellValueFactory(cel -> cel.getValue().getNaamVerantwoordelijke());
		colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
		colStart.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
		colAantalAanwezigen.setCellValueFactory(cel -> cel.getValue().getAantalAanwezigenProperty());
		colAantalIngeschrevenen.setCellValueFactory(cel -> cel.getValue().getAantalIngeschrevenenProperty());
		colGemiddleScore.setCellValueFactory(cel -> cel.getValue().getGemiddleScoreProperty());
		colDuur.setCellValueFactory(cel -> cel.getValue().getDuurProperty());
		colStartUur.setCellValueFactory(cel -> cel.getValue().getStartUurProperty());
		sc.changeSorter(null);
		changeFilter();
	}

	@FXML
	void zoekSessie(ActionEvent event) {
		if (cbxType.isVisible())
			changeFilterGebruikers();
		else
			changeFilter();
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		changeFilter();
	}

	@FXML
	void geefSessiesGekozenTypeGegevens(ActionEvent event) {
		String order = cbxSessieGegevens.getValue().toString();
		sc.changeSorter(order);
	}

	private void changeFilter() {
		String filter = txfSessie.getText();
		String status = cbxStatusSessie.getValue().toString();
		sc.changeFilter(filter, status, null);
	}

	void typeChangeListener() {
		cbxType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeFilterGebruikers();
			}
		});

	}

	private void changeFilterGebruikers() {
		String filter = txfSessie.getText();
		String type = cbxType.getValue().toString();
		gc.changeFilter(filter, type, null);
	}
}
