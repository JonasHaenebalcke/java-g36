package gui;

import java.io.IOException;

import domein.SessieKalender;
import domein.SessieKalenderController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class BeheerSessieKalendersController extends AnchorPane {
	@FXML
	private ListView<SessieKalender> lvSessieKalender;
	@FXML
	private DatePicker inputStartDatum;
	@FXML
	private DatePicker inputEindDatum;
	@FXML
	private Button btnPasSessieKalenderAan;
	@FXML
	private Button btnVerwijderSessieKalender;
	@FXML
	private Button btnMaakNieuweAan;
	@FXML
	private Label lblError;

	private SessieKalenderController dc;
	private SessieKalenderSchermController sc;

	public BeheerSessieKalendersController(SessieKalenderController dc, SessieKalenderSchermController sc) throws IOException {
//		try {
//			this.sc = sc;
//			this.dc = dc;
//			initialize();
//			System.out.println("Laad nieuw beheer sessiekalender toe scherm");
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(getClass().getResource("BeheerSessieKalenders.fxml"));
//			loader.setRoot(this);
//			loader.setController(this);
//			loader.load();
//
//		} catch (Exception e) {
//			lblError.setVisible(true);
//			lblError.setText("ERROR " + e.getMessage());
//			System.err.println(e.getMessage());
//		}
		
		this.sc = sc;
		this.dc = dc;
		System.out.println("Laad nieuw beheer sessiekalender toe scherm");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BeheerSessieKalenders.fxml"));
		//loader.setLocation(getClass().getResource("BeheerSessieKalenders.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.load();
		initialize();

	}

//	cbMaand.getItems().addAll(FXCollections.observableArrayList( Arrays.asList(Maand.values()).

	private void initialize() {
//		System.out.println("Observable list enzo");
//		System.out.println(dc.geefSessieKalenderObservableList().toString());
		ObservableList<SessieKalender> reee = dc.geefSessieKalenderObservableList();
		System.out.println(reee);
		lvSessieKalender.setItems(reee);
		
	}

	@FXML
	public void voegSessieKalenderToe(ActionEvent event) {
		try {
			if (inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
				throw new IllegalArgumentException("Datum is verplicht in te vullen!");

			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
//			sc.initialize();
			initialize();
//			Stage stage = (Stage) this.getScene().getWindow();
//			stage.close();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}

	@FXML
	public void pasSessieKalenderAan(ActionEvent event) {
		SessieKalender sessieKalender = lvSessieKalender.getSelectionModel().getSelectedItem();
		dc.wijzigSessieKalender(sessieKalender.getSessieKalenderID(), inputStartDatum.getValue(), inputEindDatum.getValue());
	}

	@FXML
	public void verwijderSessieKalender(ActionEvent event) {
		SessieKalender sessieKalender = lvSessieKalender.getSelectionModel().getSelectedItem();
		dc.verwijderSessieKalender(sessieKalender);
		
		
	}
}
