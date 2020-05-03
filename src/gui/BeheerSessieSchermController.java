package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import domein.Feedback;
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
	private TableView<Feedback > tvFeedback;
	@FXML
	private TableColumn< Feedback , String> colAuteurFeedback;
	@FXML
	private TableColumn< Feedback, String> colScoreFeedback;
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
	private ComboBox<Gebruiker> cbxVerantwoordelijke; // <String>

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

	private void initialize() {
		cbxStatusSessie.setItems(FXCollections.observableArrayList(StatusSessie.values()));
		try {
			tvSessies.setItems(sc.geefSessiesObservable());
			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
			colDuurSessie.setCellValueFactory(cel -> cel.getValue().getDuurSessieProperty());
			
			textWaardeSessieInvullen();
			btnPasAan.setDisable(true);
			btnVerwijder.setDisable(true);
			btnBeherenIngeschrevenen.setDisable(true);
			
		
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
		sc.setHuidigeSessie(tvSessies.getSelectionModel().getSelectedItem());
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
				checkboxOpenVrInSchrijvingen.setSelected(newV.isInschrijvingenOpen());
				
				cbxVerantwoordelijke.setDisable(true);
				btnVoegToe.setDisable(true);
				
				// tvMedia.setItems();
				// colMediaTitel.setCellValueFactory(cel -> cel.getValue());
				// colMediaLink.setCellValueFactory(cel -> cel.getValue());

				if (newV.getStatusSessie().name().equals(StatusSessie.open.toString())
						|| newV.getStatusSessie().name().equals(StatusSessie.gesloten.toString())) {
					
					tvFeedback.setItems(FXCollections.observableArrayList(newV.getFeedbackLijst()));
					colAuteurFeedback.setCellValueFactory(cel ->cel.getValue().getFeedbackAuteurProperty());
					colScoreFeedback.setCellValueFactory(cel -> cel.getValue().getScoreProperty());
					colFeedback.setCellValueFactory(cel -> cel.getValue().getFeedbackProperty());
					colDatumFeedback.setCellValueFactory(cel -> cel.getValue().getDatumFeedbackroperty());
					lblGemiddeldeScoreWergave.setText(Integer.toString(newV.geefGemiddeldeScore()));
				} else {
					lblFeedback.setVisible(false);
					tvFeedback.setVisible(false);
					lblGemiddeldeScore.setVisible(false);
				}
			}
		});
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			System.out.print(cbxStatusSessie.getValue().name() + " ");
				tvSessies.setItems(sc.geefSessiesObservable()); 
			if (cbxStatusSessie.getValue().name().equals(StatusSessie.open.toString()) || cbxStatusSessie.getValue().name().equals(StatusSessie.gesloten.toString())) 
			{
				lblFeedback.setVisible(true);
				tvFeedback.setVisible(true);
			}
		} catch (NullPointerException e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void pasSessieAan(ActionEvent event) {
		cbxVerantwoordelijke.setDisable(true);
		try {
			if (!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank() && (dpStartdatum.getValue() != null)
					&& (dpEinddatum.getValue() != null) && !txtStartuur.getText().isBlank()
					&& !txtEinduur.getText().isBlank() && !txtCapaciteit.getText().isBlank()) {
				if (!txtCapaciteit.getText().matches("[0-1000]")) {
					//if(txtStartuur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") && txtEinduur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
						
						Boolean isOpenVrInschrijvingen = checkboxOpenVrInSchrijvingen.isSelected();
					 sc.wijzigSessie(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(),
	                            zetOmNaarDateTime(dpStartdatum.getValue(), txtStartuur.getText()), zetOmNaarDateTime(dpEinddatum.getValue(), txtEinduur.getText()), Integer.parseInt(txtCapaciteit.getText()),
	                            txtOmschrvijving.getText(), txtGastspreker.getText(), isOpenVrInschrijvingen);
					 
					initialize();
					lblSucces.setVisible(true);
					lblSucces.setText("De sessie werd succesvol aangepast");
					} /*else {
						lblErrorDetailsSessie.setText("Je startuur en einduur moet op deze manier geschreven worden 00:00");	
					}
					}*/ else {
					lblErrorDetailsSessie.setText("De capaciteit van het aantal personen moet een geheel getal boven 0 zijn.");
				}
			} else {
				lblErrorDetailsSessie.setVisible(true);
				lblErrorDetailsSessie.setText("Tekstvakken mogen niet leeg zijn");
			}
		} catch (DateTimeParseException e) {
            lblErrorDetailsSessie.setVisible(true);
            lblErrorDetailsSessie.setText("Uur moet van geschreven worden als volgt: hh:mm");
		
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
					&& !txtEinduur.getText().isBlank()  && !txtCapaciteit.getText().isBlank()) {
				if (!txtCapaciteit.getText().matches("[0-1000]")) {
					//if(txtStartuur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$") && txtEinduur.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
						 
					//Boolean isOpenVrInschrijvingen = checkboxOpenVrInSchrijvingen.isSelected();
						sc.voegSessieToe(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(),
	                            zetOmNaarDateTime(dpStartdatum.getValue(), txtStartuur.getText()), zetOmNaarDateTime(dpEinddatum.getValue(), txtEinduur.getText()), Integer.parseInt(txtCapaciteit.getText()),
	                            txtOmschrvijving.getText(), txtGastspreker.getText());
	                 
					tvSessies.getSelectionModel().selectLast();
					initialize();
					lblSucces.setVisible(true);
					lblSucces.setText("De sessie werd succesvol toegevoegd");
					} /*else {
						lblErrorDetailsSessie.setText("Je startuur en einduur moet op deze manier geschreven worden hh:mm");	
					}
					}*/ 
				else {
					lblErrorDetailsSessie.setText("De capaciteit van het aantal personen moet een geheel getal boven 0 zijn.");
				}
			} else {
				lblErrorDetailsSessie.setVisible(true);
				lblErrorDetailsSessie.setText("Tekstvakken mogen niet leeg zijn");
			}
		} catch (DateTimeParseException e) {
            lblErrorDetailsSessie.setVisible(true);
            lblErrorDetailsSessie.setText("Uur moet van geschreven worden als volgt: hh:mm");
		} catch (Exception e) {
			lblErrorDetailsSessie.setVisible(true);
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void zoekSessie(ActionEvent event) {
		try {
			String sessieTitel = txtSessie.getText();
			
			tvSessies.getItems().stream()
			.filter(sessie -> sessie.getTitel() == sessieTitel)
			.findAny()
			.ifPresent(item -> 
			{
				tvSessies.getSelectionModel().select(item);
				tvSessies.scrollTo(item);
			});
		} catch (Exception e) {
			lblErrorDetailsSessie.setText(e.getMessage());
		}
	}

	@FXML
	void beherenIngeschrevenen(ActionEvent event) throws IOException {
		 Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		 sc.setHuidigeSessie(sessie);

		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(sc);
		this.getChildren().setAll(bIngeschrevenenScherm);
		
	}
	@FXML
	void beherenHerinneringen(ActionEvent event) {
		 Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		 sc.setHuidigeSessie(sessie);
		 // .. nog code toevoegen om naar anders scherm te verwijzen
    }
	
	private LocalDateTime zetOmNaarDateTime(LocalDate datum, String uur) {
        LocalDateTime res = LocalDateTime.parse(datum.toString() + uur, DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm"));
        return res;
    }

	// regex voor start-en einduur ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$
}
