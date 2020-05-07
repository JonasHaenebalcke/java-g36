package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

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

public class BeheerSessieSchermController extends AnchorPane {

	@FXML
	private TableView<Sessie> tblSessies;
	@FXML
	private TableColumn<Sessie, String> colTitelSessie;
	@FXML
	private TableColumn<Sessie, String> colStartSessie;
	@FXML
	private TableColumn<Sessie, String> ColEindSessie;
	@FXML
	private Button btnVoegSessieToe; // knop om tekstvakken leeg te maken en nieuwe sessie toe te voegen

	@FXML
	private ComboBox<StatusSessie> cbxStatusSessie;

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
	private Button btnBeherenHerringeringen;

	@FXML
	private TextArea txtOmschrvijving;

	@FXML
	private Label lblErrorSessies;
	@FXML
	private Label lblErrorDetailsSessie;

	@FXML
	private CheckBox checkboxOpenVrInSchrijvingen;

	private Gebruiker verantwoordelijke;
	private GebruikerController ingelogde;

	private SessieController sc;

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

	public BeheerSessieSchermController(SessieController sc, Sessie sessie) {
		this(sc);
		textWaardeSessieInvullen(sessie);
	}

	private void initialize() {
		cbxStatusSessie.setItems(FXCollections.observableArrayList(StatusSessie.values()));
		// degene die ingelogd is maakt de sessie aan, dus niet kunnen kieze
		try {
			tblSessies.setItems(sc.geefSessiesObservable().sorted(Comparator.comparing(Sessie::getStartDatum)));
			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());

			textWaardeSessieInvullen();

		} catch (NullPointerException e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
			System.out.println(e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.toString());
			System.out.println(e.getMessage());
		}
	}

	void textWaardeSessieInvullen() {

		tblSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				// sc.setHuidigeSessie(tblSessies.getSelectionModel().getSelectedItem());

				if (newV == null)
					return;
				btnPasAan.setDisable(false);
				btnVerwijder.setDisable(false);
				txtTitel.setText(newV.getTitel());
				txtGastspreker.setText(newV.getGastspreker());
				verantwoordelijke = sessieObs.getValue().getVerantwoordelijke();
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

					tvFeedback.setItems(FXCollections.observableArrayList(newV.getFeedbackLijst())
							.sorted(Comparator.comparing(Feedback::getTimeWritten)));
					colAuteurFeedback.setCellValueFactory(cel -> cel.getValue().getFeedbackAuteurProperty());
					colScoreFeedback.setCellValueFactory(cel -> cel.getValue().getScoreProperty());
					colFeedback.setCellValueFactory(cel -> cel.getValue().getFeedbackProperty());
					colDatumFeedback.setCellValueFactory(cel -> cel.getValue().getDatumFeedbackroperty());
					lblGemiddeldeScore.setVisible(true);
					lblGemiddeldeScoreWergave.setText(Integer.toString(newV.geefGemiddeldeScore()));
				} else {
					lblFeedback.setVisible(false);
					tvFeedback.setVisible(false);
					lblGemiddeldeScoreWergave.setVisible(false);
					lblGemiddeldeScore.setVisible(false);
				}
			}
		});
	}

	private void textWaardeSessieInvullen(Sessie sessie) {
		System.out.println("Deze code wordt gestolen van Audrey als hij werkt");
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			System.out.println(cbxStatusSessie.getValue().name() + " ");
			tblSessies.setItems(sc.geefSessiesObservable()); // nog aanpassen
			if (cbxStatusSessie.getValue() == StatusSessie.open
					|| cbxStatusSessie.getValue() == StatusSessie.gesloten) {
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

		} catch (NullPointerException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void pasSessieAan(ActionEvent event) {

		try {
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {
				// if (txtCapaciteit.getText().matches("")) {
				// if(txtStartuur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") &&
				// txtEinduur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {

				Boolean isOpenVrInschrijvingen = checkboxOpenVrInSchrijvingen.isSelected();
				sc.wijzigSessie(verantwoordelijke, txtTitel.getText(), txtLokaal.getText(),
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
			System.out.print(e.getMessage());
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

				// Boolean isOpenVrInschrijvingen = checkboxOpenVrInSchrijvingen.isSelected();
				// bij nieuwe sessie is de ingelogde persoon ge verantwoordelijke

				sc.voegSessieToe(ingelogde.geefIngelogdeVerantwoordelijke(), txtTitel.getText(), txtLokaal.getText(),
						zetOmNaarDateTime(dpStartdatum.getValue(), txtStartuur.getText()),
						zetOmNaarDateTime(dpEinddatum.getValue(), txtEinduur.getText()),
						Integer.parseInt(txtCapaciteit.getText()), txtOmschrvijving.getText(),
						txtGastspreker.getText());

				tblSessies.getSelectionModel().selectLast();
				initialize();
				lblSucces.setVisible(true);
				lblSucces.setText("De sessie werd succesvol toegevoegd");
				lblErrorDetailsSessie.setVisible(false);
			} else {
				lblErrorDetailsSessie.setVisible(true);
				lblErrorDetailsSessie.setText("Tekstvakken mogen niet leeg zijn");

			}
			/*
			 * } catch (IllegalArgumentException e) {
			 * lblErrorDetailsSessie.setVisible(true);
			 * lblErrorDetailsSessie.setText(e.getMessage());
			 */
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
			String sessieTitel = txtSessie.getText();
			System.out.println(sessieTitel); // werkt

//		Stream<Sessie> l = tblSessies.getItems().stream()
//			.filter(sessie -> sessie.getTitel() == sessieTitel); // stream;
//			
//			
//			.findAny()
//			.ifPresent(item -> 
//			{
//				tblSessies.getSelectionModel().select(item);
//				tblSessies.scrollTo(item);
//			});*/
		} catch (Exception e) {
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void beherenIngeschrevenen(ActionEvent event) throws IOException {
		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
		sc.setHuidigeSessie(sessie);

		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(sc);
		this.getChildren().setAll(bIngeschrevenenScherm);

	}

	@FXML
	void beherenHerinneringen(ActionEvent event) {
		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
		sc.setHuidigeSessie(sessie);
		// .. nog code toevoegen om naar anders scherm te verwijzen
	}

	@FXML
	void voegSessieToeButton(ActionEvent event) {
		Stream.of(txtTitel, txtStartuur, txtEinduur, txtCapaciteit, txtLokaal, txtOmschrvijving, txtGastspreker)
				.forEach(f -> f.clear());
		dpEinddatum.setValue(null);
		dpStartdatum.setValue(null);

		btnBeherenHerringeringen.setDisable(true);
		btnBeherenIngeschrevenen.setDisable(true);
		checkboxOpenVrInSchrijvingen.setSelected(false);
		btnVoegToe.setDisable(false);
		btnPasAan.setDisable(true);
		btnVerwijder.setDisable(true);
		tvFeedback.setVisible(false);
		lblGemiddeldeScoreWergave.setVisible(false);
		lblGemiddeldeScore.setVisible(false);
	}

	private LocalDateTime zetOmNaarDateTime(LocalDate datum, String uur) {
		LocalDateTime res = LocalDateTime.parse(datum.toString() + uur, DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm"));
		return res;
	}

	// regex voor start-en einduur ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$
}
