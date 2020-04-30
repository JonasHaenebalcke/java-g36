package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.GebruikerSessie;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class BeherenIngeschrevenenSchermController extends AnchorPane {

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
	private TableView<GebruikerSessie> tvIngeschrevenen;
	@FXML
	private TableColumn<Gebruiker, String> tcVoornaam;
	@FXML
	private TableColumn<Gebruiker, String> tcFamilienaam;
	@FXML
	private TableColumn<Gebruiker, LocalDate> tcInschrijvingsdatum;
	@FXML
	private TableColumn<Gebruiker, Boolean> tcAanwezig;
	@FXML
	private ComboBox<String> cbxStatusGebruiker;// ingeschreven,aanwezig

	@FXML
	private Button btnVoegToe;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnBeherenSessie;

	@FXML
	private TextField txtGebruiker;
	@FXML
	private Button btnZoekGebruiker;
	@FXML
	private TextField txtSessie;
	@FXML
	private Button btnZoekSessie;

	@FXML
	private Label lblError;

	private SessieController sc;
	private GebruikerController gc;
	private ArrayList<String> stringGebruiker = new ArrayList<String>();
	// private String[] stringGebruiker = {"Ingeschreven", "Aanwezig"};

	public BeherenIngeschrevenenSchermController() {
		this.sc = new SessieController();
	}

	public BeherenIngeschrevenenSchermController(SessieController sc) {
		this.sc = sc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("BeheerIngeschrevenen.fxml"));
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

			cbxStatusGebruiker.setItems(FXCollections.observableArrayList(stringGebruiker));

			tabelwaardeGebruikersInvullen();

		} catch (NullPointerException e) {
			// nog geen lbl vr error
			// lblErrorSessies.setVisible(true);
			// lblErrorSessies.setText(e.getMessage());
		} catch (Exception e) {
			// lblErrorSessies.setVisible(true);
			// lblErrorSessies.setText(e.toString());
		}

	}

	private void tabelwaardeGebruikersInvullen() {
		sc.setHuidigeSessie(tvSessies.getSelectionModel().getSelectedItem());
		
		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {

			tvIngeschrevenen.setItems(sc.geefGebruikerSessiesObservable());

//				tcVoornaam.setCellValueFactory(cel -> cel.getValue().getVoornaamProperty());
//				tcFamilienaam.setCellValueFactory(cel -> cel.getValue().getFamilieNnaamProperty());
//				tcInschrijvingsdatum.setCellValueFactory(cel -> cel.getValue().getIngeschrijvingsdatumProperty());
//				tcAanwezig.setCellValueFactory(arg0);

//    	// sessie selecteren en in huigedeSessie steken

//    	tcAanwezig.setCellFactory( kolom -> new CheckBoxTableCell<>());

//    	// moet gebruikerSessies zijn, niet alle gebruikers
			
//    	sc.geefGebruikerSessiesObservable().forEach((GebruikerSessie gs) -> {  // geeft observable van GebruikerSessie mee
//    		tcVoornaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevene().getVoornaam()));
//			tcFamilienaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevene().getFamilienaam()));
//			//tcInschrijvingsdatum.setCellValueFactory();

				/*
				 * tcAanwezig.setCellValueFactory(CheckBoxTableCell.forTableColumn(new
				 * Callback<>(){
				 * 
				 * }));
				 */

				// });

			}
		});
	}

	@FXML
	void GaNaarBeherenSessie(ActionEvent event) {
		// Sessie sessie = lvSessies.getSelectionModel().getSelectedItem();
		// sc.setHuidigeSessie(sessie);
		BeheerSessieSchermController bSessieScherm = new BeheerSessieSchermController(sc);
		this.getChildren().setAll(bSessieScherm);
	}

	@FXML
	void geefGebruikersGekozenStatus(ActionEvent event) {

	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			System.out.print(cbxStatusSessie.getValue().name() + " ");
			if (cbxStatusSessie.getValue().name().equals(StatusSessie.open.toString())	|| cbxStatusSessie.getValue().name().equals(StatusSessie.gesloten.toString()))
			{
				tvSessies.setItems(sc.geefSessiesObservable());

			}
		} catch (NullPointerException e) {

		}
	}

	/*
	 * @FXML void verwijderIngeschrevenen(ActionEvent event) { GebruikerSessie
	 * gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
	 * sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false); }
	 */

	@FXML // zet ingeschreven /niet ingeschreven
	void voegIngeschrevenenToe(ActionEvent event) {
		GebruikerSessie gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		// if() {// als een gebruiker is ingeschreven
		sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false);
		// }
		sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), true, false);
	}

	@FXML // zet aanwezig /niet aanwezig
	void zetGebruikerAanwezig(ActionEvent event) {
		GebruikerSessie gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		if (gebruiker.isAanwezig()) {
			sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false);
		}
		sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), true, true);
	}

	@FXML
	void zoekGebruiker(ActionEvent event) {//zoeken  voornaam, familienaam, of mail 
	
		String gebruikersnaam = txtGebruiker.getText();
		
    	if(gebruikersnaam.matches(".*@+.*\\.+.*")) {
    		tvIngeschrevenen.getItems().stream()
            .filter(gebruiker -> gebruiker.getIngeschrevene().getMailadres() == gebruikersnaam)
            .findAny()
            .ifPresent(item -> {
                tvIngeschrevenen.getSelectionModel().select(item);
                tvIngeschrevenen.scrollTo(item);
            });
    		
    	} else {
    		tvIngeschrevenen.getItems().stream()
            .filter(gebruiker -> gebruiker.getIngeschrevene().getFamilienaam() == gebruikersnaam || gebruiker.getIngeschrevene().getVoornaam() == gebruikersnaam)
            .findAny()
            .ifPresent(item -> {
                tvIngeschrevenen.getSelectionModel().select(item);
                tvIngeschrevenen.scrollTo(item);
            });
    	}
	}
	
	@FXML
    void zoekSessie(ActionEvent event) {
		String sessieTitel = txtSessie.getText();
		
		tvSessies.getItems().stream()
		.filter(sessie -> sessie.getTitel()== sessieTitel).findAny().ifPresent(item -> 
		{
			tvSessies.getSelectionModel().select(item);
			tvSessies.scrollTo(item);
		});
    }

}
