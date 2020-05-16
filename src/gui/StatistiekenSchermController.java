package gui;

import java.io.IOException;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.Sessie;
import domein.SessieController;
import domein.SessieKalenderController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
//		addListenerToTableGebruikers();
//		addListenerToTableSessies();

		lblTitel.setText("Gebruikers");
		lblVoorAlle.setText("Voor alle gebruikers");
		lblStatistiek1Omschrijving.setText("Totaal aantal keer aanwezig");
		lblStatistiek2Omschrijving.setText("Totaal aantal keer afwezig");
		lblStatistiek1Value.setText(String.valueOf(gc.geefAantalAanwezigen()));
		lblStatistiek2Value.setText(String.valueOf(gc.geefAantalAfwezigen()));

		colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());

		tvSessies.setVisible(false);
		tvGebruikers.setVisible(true);
		tvGebruikers.setItems(gc.geefGebruikersObservableList());
		colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
		colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
		colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
		colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
		colAantalFeedbacks.setCellValueFactory(cel -> cel.getValue().getAantalFeedbacksProperty());
		colAantalAanwezig.setCellValueFactory(cel -> cel.getValue().getAantalAanwezigProprty());
		colAantalAfwezig.setCellValueFactory(cel -> cel.getValue().getAantalAfwezigProperty());
		colProcentueelAanwezig.setCellValueFactory(cel -> cel.getValue().getProcentueelAanwezigProperty());
	}

	@FXML
	void geefStatistiekenGebruikers(ActionEvent event) {
		lblTitel.setText("Gebruikers");
		lblVoorAlle.setText("Voor alle gebruikers");

		lblStatistiek1Omschrijving.setText("Totaal aantal keer aanwezig");
		lblStatistiek2Omschrijving.setText("Totaal aantal keer afwezig");
		lblStatistiek1Value.setText(String.valueOf(gc.geefAantalAanwezigen()));
		lblStatistiek2Value.setText(String.valueOf(gc.geefAantalAfwezigen()));
		tvSessies.setVisible(false);
		tvGebruikers.setVisible(true);
		tvGebruikers.setItems(gc.geefGebruikersObservableList());
		colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
		colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
		colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
		colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
		colAantalFeedbacks.setCellValueFactory(cel -> cel.getValue().getAantalFeedbacksProperty());
		colAantalAanwezig.setCellValueFactory(cel -> cel.getValue().getAantalAanwezigProprty());
		colAantalAfwezig.setCellValueFactory(cel -> cel.getValue().getAantalAfwezigProperty());
		colProcentueelAanwezig.setCellValueFactory(cel -> cel.getValue().getProcentueelAanwezigProperty());

//		colStatus.setCellValueFactory(cel -> System.out.println(cel.getValue().getStatusProperty().getValue()));
//		colAantalFeedbacks.setCellValueFactory(cel ->  System.out.println(cel.getValue().getAantalFeedbacksProperty().getValue()));

	}

	@FXML
	void geefStatistiekenSessies(ActionEvent event) {
		lblTitel.setText("Sessies");
		lblVoorAlle.setText("Voor alle sessies");
//		lblStatistiek1Omschrijving.setText("Totaal aantal keer aanwezig");
//		lblStatistiek2Omschrijving.setText("Totaal aantal keer afwezig");
//		lblStatistiek1Value.setText(String.valueOf(gc.geefAantalAanwezigen()));
//		lblStatistiek2Value.setText(String.valueOf(gc.geefAantalAfwezigen()));

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
//		colEind.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
//		colAantalDeelnemers.setCellValueFactory(cel -> cel.getValue().getAantalDeelnemersProperty());
	}

//	void addListenerToTableGebruikers() {
//		tvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Gebruiker> observable, Gebruiker oldValue,
//					Gebruiker newValue) {
//				lblStatistiek1Omschrijving.setText("Aantal maal aanwezig");
//				lblStatistiek1Value.setText("TE VERANDEREN");
//				lblStatistiek2Omschrijving.setText("Aantal maal afwezig");
//				lblStatistiek2Value.setText("TE VERANDEREN");
//			}
//		});
//
//	}

//	void addListenerToTableSessies() {
//		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Sessie> observable, Sessie oldValue, Sessie newValue) {
//				lblStatistiek1Omschrijving.setText("Aantal aanwezigen");
//				lblStatistiek1Value.setText(String.valueOf(statc.geefAantalAanwezigen(newValue)));
//				lblStatistiek2Omschrijving.setText("Gemiddelde score");
//				lblStatistiek2Value.setText(String.valueOf(rondAf((statc.geefGemiddeldeScore(newValue)), 2) + "/5"));
//			}
//		});
//
//	}

//	private static double rondAf(double value, int places) { // Source
//																// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
//		if (places < 0)
//			throw new IllegalArgumentException();
//
//		long factor = (long) Math.pow(10, places);
//		value = value * factor;
//		long tmp = Math.round(value);
//		return (double) tmp / factor;
//	}

//	    @FXML 
//	    void slaEersteStatiestiekOp(ActionEvent event) {
//
//	    }
//	    
//	    @FXML
//	    void SlaTweedeStatistiekOp(ActionEvent event) {
//
//	    }

}
