package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import domein.AankondigingController;
import domein.Feedback;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.Sessie;
import domein.SessieController;
import domein.StatusSessie;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class BeheerSessieSchermController extends GridPane {

	@FXML
	private TableView<Sessie> tblSessies;
	@FXML
	private TableColumn<Sessie, String> colTitelSessie;
	@FXML
	private TableColumn<Sessie, String> colStartSessie;
	@FXML
	private TableColumn<Sessie, String> colStartUurSessie;
	@FXML
	private Button btnVoegSessieToe; // knop om tekstvakken leeg te maken en nieuwe sessie toe te voegen

	@FXML
	private ComboBox<String> cbxStatusSessie;

	@FXML
	private Label lblMedia;
	@FXML
	private TableView<? /* Media */> tvMedia;
	@FXML
	private TableColumn<? /* Media */, String> colMediaTitel;
	@FXML
	private TableColumn<? /* Media */, String> colMediaLink;

	@FXML
	private Label lblFeedback;
	@FXML
	private TableView<Feedback> tvFeedback;
	@FXML
	private TableColumn<Feedback, String> colAuteurFeedback;
	@FXML
	private TableColumn<Feedback, String> colScoreFeedback;
	@FXML
	private TableColumn<Feedback, String> colFeedback;
	@FXML
	private TableColumn<Feedback, String> colDatumFeedback;
	@FXML
	private Label lblGemiddeldeScore;

	@FXML
	private Label lblGemiddeldeScoreWergave;

	@FXML
	private Label lblSucces;
	@FXML
	private TextField txtGastspreker;
	@FXML
	private TextField txtTitel;

	@FXML
	private Label lblVerantwoordelijke;
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
	private Button btnBeherenIngeschrevenen;
	@FXML
	private Button btnBeherenAankondigingen;

	@FXML
	private TextArea txtOmschrvijving;

	@FXML
	private Label lblErrorSessies;
	@FXML
	private Label lblErrorDetailsSessie;

	@FXML
	private CheckBox checkboxOpenVrInSchrijvingen;

	private GebruikerController gc;
	private SessieController sc;
	private AankondigingController ac;

	public BeheerSessieSchermController() {
		this.sc = new SessieController();
		this.gc = new GebruikerController();
		this.ac = new AankondigingController();
	}

	public BeheerSessieSchermController(SessieController sc, GebruikerController gc, AankondigingController ac) {
		this.sc = sc;
		this.gc = gc;
		this.ac = ac;

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

	public BeheerSessieSchermController(SessieController sc, GebruikerController gc, Sessie sessie,
			AankondigingController ac) {
		this(sc, gc, ac);
		tblSessies.getSelectionModel().select(sessie);
		sc.changeSorter(null);
	}

	private void initialize() {
		List<String> statussen = new ArrayList<>();
		statussen.add("Alle Types");
		for (StatusSessie status : StatusSessie.values()) {
			statussen.add(status.toString());
		}
		cbxStatusSessie.setItems(FXCollections.observableArrayList(statussen));
		cbxStatusSessie.getSelectionModel().selectFirst();

		try {
			tblSessies.setItems(sc.geefSessiesSorted());
			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			colStartUurSessie.setCellValueFactory(cel -> cel.getValue().getStartUurProperty());

			sc.changeSorter(null);
			textWaardeSessieInvullen();

		} catch (NullPointerException e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
			System.err.println(e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.toString());
			System.err.println(e.getMessage());
		}
	}

	void textWaardeSessieInvullen() {

		tblSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				sc.setHuidigeSessie(tblSessies.getSelectionModel().getSelectedItem());

				if (newV == null)
					return;
				lblSucces.setText("");
				lblErrorDetailsSessie.setText("");
				btnPasAan.setDisable(false);
				btnVerwijder.setDisable(false);
				txtTitel.setText(newV.getTitel());
				txtGastspreker.setText(newV.getGastspreker());
				lblVerantwoordelijke.setText((sessieObs.getValue().getVerantwoordelijke().getFamilienaam() + " "
						+ sessieObs.getValue().getVerantwoordelijke().getVoornaam()));
				txtCapaciteit.setText(Integer.toString(newV.getCapaciteit()));
				txtLokaal.setText(newV.getLokaal());
				txtOmschrvijving.setText(newV.getOmschrijving());
				checkboxOpenVrInSchrijvingen.setSelected(newV.isInschrijvingenOpen());
				dpEinddatum.setValue(newV.getEindDatum().toLocalDate());
				dpStartdatum.setValue(newV.getStartDatum().toLocalDate());
				txtEinduur.setText(newV.getEindDatum().format(DateTimeFormatter.ofPattern("HH:mm")));
				txtStartuur.setText(newV.getStartDatum().format(DateTimeFormatter.ofPattern("HH:mm")));

				btnVoegToe.setDisable(true);

				// tvMedia.setItems();
				// colMediaTitel.setCellValueFactory(cel -> cel.getValue());
				// colMediaLink.setCellValueFactory(cel -> cel.getValue());

				if (newV.getStatusSessie() == StatusSessie.open || newV.getStatusSessie() == StatusSessie.gesloten
						|| newV.getEindDatum().isBefore(LocalDateTime.now())) {
					tvFeedback.setItems(
							newV.getFeedbackObservable().sorted(Comparator.comparing(Feedback::getTimeWritten)));
					colAuteurFeedback.setCellValueFactory(cel -> cel.getValue().getFeedbackAuteurProperty());
					colScoreFeedback.setCellValueFactory(cel -> cel.getValue().getScoreProperty());
					colFeedback.setCellValueFactory(cel -> cel.getValue().getFeedbackProperty());
					colDatumFeedback.setCellValueFactory(cel -> cel.getValue().getDatumFeedbackroperty());
					lblGemiddeldeScore.setVisible(true);
					lblFeedback.setVisible(true);
					tvFeedback.setVisible(true);
					lblGemiddeldeScoreWergave.setVisible(true);
					lblGemiddeldeScoreWergave.setText(Double.toString(newV.geefGemiddeldeScore()));
				} else {
					lblFeedback.setVisible(false);
					tvFeedback.setVisible(false);
					lblGemiddeldeScoreWergave.setVisible(false);
					lblGemiddeldeScore.setVisible(false);
				}
			}
		});
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			if (cbxStatusSessie.getValue().equals(StatusSessie.open.toString())
					|| cbxStatusSessie.getValue().equals("Alle")
					|| cbxStatusSessie.getValue().equals(StatusSessie.gesloten.toString())//
			) {
				lblFeedback.setVisible(true);
				tvFeedback.setVisible(true);
				lblGemiddeldeScore.setVisible(true);
				lblGemiddeldeScoreWergave.setVisible(true);

			} else {
				lblFeedback.setVisible(false);
				tvFeedback.setVisible(false);
				lblGemiddeldeScoreWergave.setVisible(false);
				lblGemiddeldeScore.setVisible(false);
			}
			changeFilter();

		} catch (NullPointerException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	private void changeFilter() {
		String filter = txtSessie.getText();
		String status = cbxStatusSessie.getValue().toString();
		sc.changeFilter(filter, status, null);
	}

	@FXML
	void pasSessieAan(ActionEvent event) {

		try {
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {
				// if(txtStartuur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") &&
				// txtEinduur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {

				Boolean isOpenVrInschrijvingen = checkboxOpenVrInSchrijvingen.isSelected();
				sc.wijzigSessie(/* verantwoordelijke, */ txtTitel.getText(), txtLokaal.getText(),
						zetOmNaarDateTime(dpStartdatum.getValue(), txtStartuur.getText()),
						zetOmNaarDateTime(dpEinddatum.getValue(), txtEinduur.getText()),
						Integer.parseInt(txtCapaciteit.getText()), txtOmschrvijving.getText(), txtGastspreker.getText(),
						isOpenVrInschrijvingen);

				initialize();
				lblSucces.setVisible(true);
				lblSucces.setText("De sessie werd succesvol aangepast");
				lblErrorDetailsSessie.setVisible(false);
			} else {
				lblErrorDetailsSessie.setVisible(true);
				lblErrorDetailsSessie.setText("Tekstvakken mogen niet leeg zijn");
			}
		} catch (NumberFormatException e) {
			lblErrorDetailsSessie.setText("Capaciteit moet een positief geheel getal zijn.");

		} catch (IllegalArgumentException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());

		} catch (DateTimeParseException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText("Uur moet van geschreven worden als volgt: HH:mm");

		} catch (Exception e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void verwijderSessie(ActionEvent event) {
		try {
			sc.verwijderHuidigeSessie();
			initialize();
			lblSucces.setVisible(true);
			lblSucces.setText("De sessie werd succesvol verwijderd");
			lblErrorDetailsSessie.setVisible(false);
			initialize();
		} catch (Exception e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
			System.err.print(e.getMessage());
		}
	}

	@FXML
	void voegSessieToe(ActionEvent event) {
		try {
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {
				// if (txtCapaciteit.getText().matches("[0-100000]")) {
				// if(txtStartuur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") &&
				// txtEinduur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {

				// bij nieuwe sessie is de ingelogde persoon ge verantwoordelijke
				sc.voegSessieToe(gc.getIngelogdeVerantwoordelijke(), txtTitel.getText(), txtLokaal.getText(),
						zetOmNaarDateTime(dpStartdatum.getValue(), txtStartuur.getText()),
						zetOmNaarDateTime(dpEinddatum.getValue(), txtEinduur.getText()),
						Integer.parseInt(txtCapaciteit.getText()), txtOmschrvijving.getText(),
						txtGastspreker.getText());
				// tblSessies.getSelectionModel().selectLast();
				initialize();
				lblSucces.setVisible(true);
				lblSucces.setText("De sessie werd succesvol toegevoegd");
				lblErrorDetailsSessie.setVisible(false);
			} else {
				lblErrorDetailsSessie.setVisible(true);
				lblErrorDetailsSessie.setText("Tekstvakken mogen niet leeg zijn");

			}
		} catch (NumberFormatException e) {
			lblErrorDetailsSessie.setText("Capaciteit moet een positief geheel getal zijn.");

		} catch (DateTimeParseException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText("Uur moet van geschreven worden als volgt: HH:mm");
		} catch (Exception e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void zoekSessie(ActionEvent event) {
		try {
			changeFilter();
		} catch (Exception e) {
			e.printStackTrace();
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void beherenIngeschrevenen(ActionEvent event) throws IOException {
		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
		sc.setHuidigeSessie(sessie);

		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(sc, gc,
				sessie, ac);
		this.getChildren().setAll(bIngeschrevenenScherm);

	}

	@FXML
	void beherenAankondigingen(ActionEvent event) {
		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
		sc.setHuidigeSessie(sessie);
		AankondigingenSchermController asc = new AankondigingenSchermController(sc, sessie, ac, gc);
		this.getChildren().setAll(asc);
	}

	@FXML
	void voegSessieToeButton(ActionEvent event) {
		Stream.of(txtTitel, txtStartuur, txtEinduur, txtCapaciteit, txtLokaal, txtOmschrvijving, txtGastspreker)
				.forEach(f -> f.clear());
		dpEinddatum.setValue(null);
		dpStartdatum.setValue(null);
		lblVerantwoordelijke.setText(gc.getIngelogdeVerantwoordelijke().getVoornaam() + " "
				+ gc.getIngelogdeVerantwoordelijke().getFamilienaam());
		btnBeherenAankondigingen.setDisable(true);
		btnBeherenIngeschrevenen.setDisable(true);
		checkboxOpenVrInSchrijvingen.setSelected(false);
		btnVoegToe.setDisable(false);
		btnPasAan.setDisable(true);
		btnVerwijder.setDisable(true);
		tvFeedback.setVisible(false);
		lblFeedback.setVisible(false);
		lblGemiddeldeScoreWergave.setVisible(false);
		lblGemiddeldeScore.setVisible(false);
		// lblSucces.setVisible(false);
		lblSucces.setText("");
	}

	private LocalDateTime zetOmNaarDateTime(LocalDate datum, String uur) {
		LocalDateTime res = LocalDateTime.parse(datum.toString() + uur, DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm"));
		return res;
	}

	// regex voor start-en einduur ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$
}
