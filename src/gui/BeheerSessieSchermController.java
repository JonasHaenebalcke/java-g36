package gui;

import java.io.IOException;
import java.time.LocalDate;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieController;
import domein.StatusSessie;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class BeheerSessieSchermController extends AnchorPane {

	@FXML
	private TableView<Sessie> tvSessies;
	@FXML
	private TableColumn<Sessie, String> colTitelSessie;
	@FXML
	private TableColumn<Sessie, String> colStartSessie;
	@FXML
	private TableColumn<Sessie, String> ColEindSessie;
	@FXML
	private TableColumn<Sessie, String> colDuurSessie;

	@FXML
	private ComboBox<StatusSessie> cbxStatusSessie;

	@FXML
	private TableView<? /* Media */> tvMedia;
	@FXML
	private TableColumn<? /* Media */, String> colMediaTitel;
	@FXML
	private TableColumn<? /* Media */, String> colMediaLink;

	@FXML
	private TextField txtGastspreker;

	@FXML
	private TextField txtTitel;

	@FXML
	private DatePicker dpEinddatum;
	@FXML
	private DatePicker dpStartdatum;

	@FXML
	private TextField txtCapaciteit;

	@FXML
	private TextField txtStartuur;

	@FXML
	private TextField txtEinduur;

	@FXML
	private TextField txtLokaal;

	@FXML
	private Button btnPasAan;
	@FXML
	private Button btnVoegToe;
	@FXML
	private Button btnVerwijder;

	@FXML
	private Button btnZoek;

	@FXML
	private TextField txtSessie;

	@FXML
	private TableView<?/* Feedback */> tvFeedback;
	@FXML
	private TableColumn<? /* Feedback */, ?> colAuteurFeedback;
	@FXML
	private TableColumn<? /* Feedback */, Integer> colScoreFeedback;
	@FXML
	private TableColumn<? /* Feedback */, String> colFeedback;
	@FXML
	private TableColumn<? /* Feedback */, LocalDate> colDatumFeedback;

	@FXML
	private ComboBox<Gebruiker> cbxVerantwoordelijke;

	@FXML
	private Button btnBeherenIngeschrevenen;

	@FXML
	private TextArea txtOmschrvijving;

	@FXML
	private Label lblError;

	@FXML
	private Label lblMedia;

	@FXML
	private Label lblFeedback;

	private SessieController sc;
	private Sessie sessie;

	public BeheerSessieSchermController() {
		this.sc = new SessieController();
	}

	public BeheerSessieSchermController(SessieController sc) {
		this.sc = sc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Sessie.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initialize();
	}

	private void initialize() {
		cbxStatusSessie.setItems(FXCollections.observableArrayList(StatusSessie.values()));
		//tvSessies.setItems(sc.geefSessiesObservable(StatusSessie.nietOpen));
		colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
		colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
		ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
		colDuurSessie.setCellValueFactory(cel -> cel.getValue().getDuurSessieProperty());

		lblFeedback.setVisible(false);
		tvFeedback.setVisible(false);
		// textWaardeSessieInvullen();

	}

	void textWaardeSessieInvullen() {
		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {

				txtTitel.setText(newV.getTitel());
				txtGastspreker.setText(newV.getGastspreker());
				cbxVerantwoordelijke.setValue(sessieObs.getValue().getVerantwoordelijke());// (newV.getVerantwoordelijke().getVoornaam()+
																							// sessie.getVerantwoordelijke().getFamilienaam());
				txtCapaciteit.setText(Integer.toString(newV.getCapaciteit()));
				txtLokaal.setText(newV.getLokaal());
				txtOmschrvijving.setText(newV.getOmschrijving());
				
				// tvMedia.setItems();
				// colMediaTitel.setCellValueFactory(cel -> cel.getValue());
				// colMediaLink.setCellValueFactory(cel -> cel.getValue());

//				tvFeedback.setItems();
//				colAuteurFeedback.setCellValueFactory();
//				colScoreFeedback.setCellValueFactory();
//				colFeedback.setCellValueFactory(cel -> cel.getValue());
//				colDatumFeedback.setCellValueFactory();
				
			}

		});

	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		tvSessies.setItems(sc.geefSessiesObservable((cbxStatusSessie.getValue())));
		if (cbxStatusSessie.getValue() == StatusSessie.open/* cbxStatusSessie.getSelectionModel().equals(StatusSessie.open)|| cbxStatusSessie.selectionModelProperty().equals(StatusSessie.gesloten)*/) {
			lblFeedback.setVisible(true);
			tvFeedback.setVisible(true);
		}

	}

	@FXML
	void beherenIngeschrevenen(ActionEvent event) {
		// Sessie sessie = lvSessies.getSelectionModel().getSelectedItem();
		// sc.setHuidigeSessie(sessie);

		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(sc);
		// this.getChildren().add(bIngeschrevenenScherm);
	}

	@FXML
	void pasSessieAan(ActionEvent event) {
		cbxVerantwoordelijke.setDisable(true);
		try {
			// Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
			// textWaardeSessieInvullen();
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {

				sc.wijzigSessie(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(),
						dpStartdatum.getValue(), dpEinddatum.getValue(), Integer.parseInt(txtCapaciteit.getText()),
						txtOmschrvijving.getText(), txtGastspreker.getText());
				initialize();
			} else {
				lblError.setText("Tekstvakken mogen niet leeg zijn");
			}
		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
	}

	@FXML
	void verwijderSessie(ActionEvent event) {
		try {
			Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
			sc.verwijderSessie(sessie);
			initialize();
		} catch (Exception e) {
			lblError.setText(e.getMessage());
			System.out.print(e.getMessage());
		}
	}

	@FXML
	void voegSessieToe(ActionEvent event) {
		try {
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {
				sc.voegSessieToe(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(),
						dpStartdatum.getValue(), dpEinddatum.getValue(), Integer.parseInt(txtCapaciteit.getText()),
						txtOmschrvijving.getText(), txtGastspreker.getText());

				tvSessies.getSelectionModel().selectLast();
				initialize();
			} else {
				lblError.setText("Tekstvakken mogen niet leeg zijn");
			}
		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
	}

	// regex voor start-en einduur ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$
}
