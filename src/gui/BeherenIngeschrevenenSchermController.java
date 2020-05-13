package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

import org.mockito.exceptions.verification.NeverWantedButInvoked;

import domein.AankondigingController;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.GebruikerSessie;
import domein.Sessie;
import domein.SessieController;
import domein.Status;
import domein.StatusSessie;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class BeherenIngeschrevenenSchermController extends GridPane {

	@FXML
	private TableView<Sessie> tvSessies;
	@FXML
	private TableColumn<Sessie, String> colTitelSessie;
	@FXML
	private TableColumn<Sessie, String> colStartSessie;
	@FXML
	private TableColumn<Sessie, String> ColEindSessie;

	@FXML
	private ComboBox<StatusSessie> cbxStatusSessie;

	@FXML
	private Label lbltitelTabelGebruikers;
	@FXML
	private TableView<GebruikerSessie> tvIngeschrevenen;
	@FXML
	private TableColumn<GebruikerSessie, Boolean> tcIngeschreven;
	@FXML
	private TableColumn<GebruikerSessie, String> tcVoornaam;
	@FXML
	private TableColumn<GebruikerSessie, String> tcFamilienaam;
	@FXML
	private TableColumn<GebruikerSessie, String> tcInschrijvingsdatum;
	@FXML
	private TableColumn<GebruikerSessie, Boolean> tcAanwezig;
	@FXML
	private ComboBox<String> cbxStatusGebruiker;// ingeschreven,aanwezig

	@FXML
	private Button btnSchrijfGebruikerIn;
	@FXML
	private Button btnZetGebruikerAanwezig;
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
	private AankondigingController ac;

	// private ObservableList<String> lijst =
	// FXCollections.observableArrayList("Alle gebruiker","Ingeschreven",
	// "Aanwezig", "Afwezig");

	public BeherenIngeschrevenenSchermController() {
		this.sc = new SessieController();
	}

	public BeherenIngeschrevenenSchermController(SessieController sc, GebruikerController gc,
			AankondigingController ac) {
		this.sc = sc;
		this.gc = gc;
		this.ac = ac;

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

	public BeherenIngeschrevenenSchermController(SessieController sc, GebruikerController gc, Sessie sessie,
			AankondigingController ac) {
		this(sc, gc, ac);
		if (sessie != null) {
			tvSessies.getSelectionModel().select(sessie);

		}
	}

	private void initialize() {
		cbxStatusSessie.setItems(FXCollections.observableArrayList(StatusSessie.values()));
		try {
			tvSessies.setItems(sc.geefSessiesObservable().sorted(Comparator.comparing(Sessie::getStartDatum)));
			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());

			cbxStatusGebruiker.setItems(FXCollections.observableArrayList());

			tabelwaardeGebruikersInvullen();

		} catch (NullPointerException e) {
			// lblErrorSessies.setVisible(true);
			// lblErrorSessies.setText(e.getMessage());
		} catch (Exception e) {
			// lblErrorSessies.setVisible(true);
			// lblErrorSessies.setText(e.toString());
		}

	}

	private void tabelwaardeGebruikersInvullen() {

		// sc.setHuidigeSessie(tvSessies.getSelectionModel().getSelectedItem());

		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				sc.setHuidigeSessie(newV);
				tvIngeschrevenen.setItems(sc.geefGebruikerSessiesObservable());
//	    	// moet gebruiker van gekozen Sessies zijn, niet alle gebruikers

				tcVoornaam.setCellValueFactory(
						cel -> new ReadOnlyStringWrapper(cel.getValue().getIngeschrevene().getVoornaam()));
				tcFamilienaam.setCellValueFactory(
						cel -> new ReadOnlyStringWrapper(cel.getValue().getIngeschrevene().getFamilienaam()));
				tcInschrijvingsdatum.setCellValueFactory(cel -> cel.getValue().getInschrijvingsDatumProperty());
				tcIngeschreven.setCellFactory(column -> new CheckBoxTableCell<>());
				tcAanwezig.setCellFactory(column -> new CheckBoxTableCell<>());

				tcAanwezig.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<GebruikerSessie, Boolean>, ObservableValue<Boolean>>() {
							@Override
							public ObservableValue<Boolean> call(
									TableColumn.CellDataFeatures<GebruikerSessie, Boolean> gs) {
								Boolean b;
								if (gs.getValue().isAanwezig()) {// .getIngeschrevene().getVoornaam().equals("Rein")) {
									b = true;
								} else {
									b = false;
								}
								return new SimpleBooleanProperty(b);
							}
						});

//    		 tcIngeschreven.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<GebruikerSessie, Boolean>, ObservableValue<Boolean>>() {
//    	            @Override
//    	            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<GebruikerSessie, Boolean> gs) {
//    	                return new SimpleBooleanProperty(gs.getValue().isAanwezig());
//    	            }
//    	        });

				tcIngeschreven.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<GebruikerSessie, Boolean>, ObservableValue<Boolean>>() {
							@Override
							public ObservableValue<Boolean> call(
									TableColumn.CellDataFeatures<GebruikerSessie, Boolean> gs) {
// 	                return new SimpleBooleanProperty(true);
								Boolean b;
								if (gs.getValue() != null) {// .getIngeschrevene().getVoornaam().equals("Rein")) {
									b = true;
								} else {
									b = false;
								}
								return new SimpleBooleanProperty(b);
							}
						});

			}
		});

		txtWaardeInvullenGebruikerGekozen();

	}

	private void txtWaardeInvullenGebruikerGekozen() {
//		tvIngeschrevenen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Gebruiker> gebruikerObs, Gebruiker oldV, Gebruiker newV) {
//				tvSessies.setItems(gc.geefSessiesGebruikerObservable(newV));
//				colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
//				colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
//				ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
//			}
//		});
		tvIngeschrevenen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GebruikerSessie>() {

			@Override
			public void changed(ObservableValue<? extends GebruikerSessie> gebruikerObs, GebruikerSessie oldV,
					GebruikerSessie newV) {
				System.out.println("Geselecteerde Gebruiker: " + newV.getIngeschrevene());
				System.out.println("GebruikerSessieLijst: " + newV.getIngeschrevene().getGebruikerSessieLijst());
				System.out.println("lijst: " + gc.geefSessiesGebruikerObservable(newV.getIngeschrevene()));
				tvSessies.setItems(gc.geefSessiesGebruikerObservable(newV.getIngeschrevene()));
				colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
				colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
				ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
			}
		});

	}

	@FXML
	void GaNaarBeherenSessie(ActionEvent event) {
		/*
		 * if(tvSessies.getSelectionModel().getSelectedItem() != null) {
		 * sc.setHuidigeSessie(tvSessies.getSelectionModel().getSelectedItem());
		 * 
		 * BeheerSessieSchermController bSessieScherm = new
		 * BeheerSessieSchermController(sc); this.getChildren().setAll(bSessieScherm); }
		 * else { lblError.setText("Je moet een sessie kiezen om het te beheren"); }
		 */

		BeheerSessieSchermController bSessieScherm = new BeheerSessieSchermController(sc, gc, ac);
		this.getChildren().setAll(bSessieScherm);
	}

	@FXML
	void geefGebruikersGekozenStatus(ActionEvent event) {
		tvIngeschrevenen.setItems(sc.geefGebruikerSessiesObservable().sorted());
		lbltitelTabelGebruikers.setText(""); // hang af van wat gekozen
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			System.out.print(cbxStatusSessie.getValue().name() + " ");

//			if (cbxStatusSessie.getValue().name().equals(StatusSessie.open.toString())	|| cbxStatusSessie.getValue().name().equals(StatusSessie.gesloten.toString()))
//			{
//				tvSessies.setItems(sc.geefSessiesObservable());// nog aanpassen

//				initialize();
//			}
			changeSessieFilter();
			
		} catch (NullPointerException e) {
			lblError.setText(e.getMessage());
		}
	}
	
	private void changeSessieFilter() {
		String filter = txtSessie.getText();
		String status = cbxStatusSessie.getValue().toString();

		System.out.println("filter: " + filter);
		System.out.println("status: " + status);
		sc.changeFilter(filter, status);
	}

	/*
	 * @FXML void verwijderIngeschrevenen(ActionEvent event) { GebruikerSessie
	 * gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
	 * sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false); }
	 */

	@FXML // zet ingeschreven /niet ingeschreven
	void voegIngeschrevenenToe(ActionEvent event) {
		GebruikerSessie gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		if (gebruiker.getIngeschrevene() != null) {// als een gebruiker is ingeschreven
			sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false);
			btnSchrijfGebruikerIn.setText("SchrijfGebruikerUit");
		}
		sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), true, false);
		btnSchrijfGebruikerIn.setText("SchrijfGebruikerIn");
	}

	@FXML // zet aanwezig /niet aanwezig
	void zetGebruikerAanwezig(ActionEvent event) {
		GebruikerSessie gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		if (gebruiker.isAanwezig()) {
			sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false);
			btnZetGebruikerAanwezig.setText("zet gebruiker afwezig");
		}
		sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), true, true);
		btnZetGebruikerAanwezig.setText("zet gebruiker aanwezig");
	}

	@FXML
	void zoekGebruiker(ActionEvent event) {// zoeken voornaam, familienaam, of mail

		String gebruikersnaam = txtGebruiker.getText();

		/*
		 * if(gebruikersnaam.matches(".*@+.*\\.+.*")) {
		 * tvIngeschrevenen.getItems().stream() .filter(gebruiker ->
		 * gebruiker.getIngeschrevene().getMailadres() == gebruikersnaam) .findAny()
		 * .ifPresent(item -> { tvIngeschrevenen.getSelectionModel().select(item);
		 * tvIngeschrevenen.scrollTo(item); });
		 * 
		 * } else { tvIngeschrevenen.getItems().stream() .filter(gebruiker ->
		 * gebruiker.getIngeschrevene().getFamilienaam() == gebruikersnaam ||
		 * gebruiker.getIngeschrevene().getVoornaam() == gebruikersnaam) .findAny()
		 * .ifPresent(item -> { tvIngeschrevenen.getSelectionModel().select(item);
		 * tvIngeschrevenen.scrollTo(item); }); }
		 */
	}

	@FXML
	void zoekSessie(ActionEvent event) {
		String sessieTitel = txtSessie.getText();

		changeSessieFilter();
    
	}

}
