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
	private ComboBox<String> cbxStatusSessie;

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
	private TableView<Gebruiker> tvGebruikers;
	@FXML
	private TableColumn<Gebruiker, String> colVoornaam;
	@FXML
	private TableColumn<Gebruiker, String> colFamilienaam;

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
	@FXML
	private Label lblErrorSessies;

	private SessieController sc;
	private GebruikerController gc;
	private AankondigingController ac;

	// private ObservableList<String> lijst =
	// FXCollections.observableArrayList("Alle gebruiker","Ingeschreven",
	// "Aanwezig", "Afwezig");

	public BeherenIngeschrevenenSchermController() {
		this.sc = new SessieController();
		this.gc = new GebruikerController();
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
		List<String> statussen = new ArrayList<>();
		statussen.add("Alle Types");
		for (StatusSessie status : StatusSessie.values()) {
			statussen.add(status.toString());
		}
		cbxStatusSessie.setItems(FXCollections.observableArrayList(statussen));
		cbxStatusSessie.getSelectionModel().selectFirst();

		List<String> statusGebruikers = new ArrayList<>();
		statusGebruikers.add(0, "Alle");
		statusGebruikers.add(1, "Ingeschrevenen");
		statusGebruikers.add(2, "Aanwezigen");
		statusGebruikers.add(3, "Afwezigen");
		System.out.println(statusGebruikers);
		cbxStatusGebruiker.setItems(FXCollections.observableArrayList(statusGebruikers));
		cbxStatusGebruiker.getSelectionModel().selectFirst();

		try {
			tvSessies.setItems(sc.geefSessiesSorted());// .geefSessiesObservable().sorted(Comparator.comparing(Sessie::getStartDatum)));
			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
			sc.changeSorter(null);

			// tvGebruikers.setVisible(false);
			// tvIngeschrevenen.setVisible(true);
			tvIngeschrevenen.setVisible(false);
			tvGebruikers.setItems(gc.geefGebruikersSorted());
			colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
			colFamilienaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			tabelwaardeGebruikersInvullen();

		} catch (NullPointerException e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.toString());
		}

	}

	private void tabelwaardeGebruikersInvullen() {

		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				if (newV == null)
					return;
				sc.setHuidigeSessie(newV);
				tvIngeschrevenen.setItems(sc.geefGebruikerSessiesSorted());

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
								if (gs.getValue().isAanwezig()) {
									b = true;
								} else {
									b = false;
								}
								return new SimpleBooleanProperty(b);
							}
						});
				tcIngeschreven.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<GebruikerSessie, Boolean>, ObservableValue<Boolean>>() {
							@Override
							public ObservableValue<Boolean> call(
									TableColumn.CellDataFeatures<GebruikerSessie, Boolean> gs) {
								Boolean b;
								if (gs.getValue() != null) {
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
		tvIngeschrevenen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GebruikerSessie>() {

			@Override
			public void changed(ObservableValue<? extends GebruikerSessie> gebruikerObs, GebruikerSessie oldV,
					GebruikerSessie newV) {
				if (newV == null)
					return;
				tvSessies.setItems(gc.geefSessiesGebruikerObservable(newV.getIngeschrevene()));
				colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
				colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
				ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());

				btnSchrijfGebruikerIn.setText("Schrijf uit");
				if (newV.isAanwezig()) {
					btnZetGebruikerAanwezig.setText("Zet afwezig");
				} else {
					btnZetGebruikerAanwezig.setText("Zet aanwezig");
				}
			}
		});

	}

	@FXML
	void GaNaarBeherenSessie(ActionEvent event) {
		Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		if (tvSessies.getSelectionModel().getSelectedItem() != null) {
			this.getChildren().setAll(new BeheerSessieSchermController(this.sc, this.gc, sessie, this.ac));
		} else {
			lblError.setText("Je moet een sessie kiezen om het te beheren");
		}
	}

	@FXML
	void geefGebruikersGekozenStatus(ActionEvent event) {
		if (cbxStatusGebruiker.getValue().equals("Alle")) {
			tvGebruikers.setVisible(true);
			tvIngeschrevenen.setVisible(false);
			
		} else {
			tvGebruikers.setVisible(false);
			tvIngeschrevenen.setVisible(true);
		}
		changeGebruikerSessieFilter();
//		if (cbxStatusSessie.getValue().equals(StatusSessie.open.toString()) || cbxStatusSessie.getValue().equals(StatusSessie.gesloten.toString())) {
//
//			tvIngeschrevenen.setItems(sc.geefGebruikerSessiesObservable());
//			lbltitelTabelGebruikers.setText(""); // hang af van wat gekozen
//		}
	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {

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

	private void changeGebruikerSessieFilter() {
		String filter = txtGebruiker.getText();
		String status = cbxStatusGebruiker.getValue();

		System.out.println("filter: " + filter);
		System.out.println("status: " + status);

		if (cbxStatusGebruiker.getValue().equals("Alle"))
			gc.changeFilter(filter, null);
		else if(tvSessies.getSelectionModel().getSelectedItem() != null)
			sc.changeFilterGebruikerSessie(filter, cbxStatusGebruiker.getValue());
	}

	/*
	 * @FXML void verwijderIngeschrevenen(ActionEvent event) { GebruikerSessie
	 * gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
	 * sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false); }
	 */

	@FXML // zet ingeschreven /niet ingeschreven
	void voegIngeschrevenenToe(ActionEvent event) {
		GebruikerSessie gebruikerSessie = tvIngeschrevenen.getSelectionModel().getSelectedItem();

		Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		if (gebruikerSessie != null) {
			if (sessie.isGebruikerIngeschreven(gebruikerSessie.getIngeschrevene())) {
				sc.wijzigIngeschrevenen(gebruikerSessie.getIngeschrevene(), false, gebruikerSessie.isAanwezig());
				System.out.println("gebruikers is uitgeschreven");
			} else {
				sc.wijzigIngeschrevenen(gebruikerSessie.getIngeschrevene(), true, gebruikerSessie.isAanwezig());
				System.out.println("gebruikers is ingeschreven");
			}
		} else {
			Gebruiker gebruiker = tvGebruikers.getSelectionModel().getSelectedItem();

			if (gebruiker != null) {
				if (sessie.isGebruikerIngeschreven(gebruiker)) {
					sc.wijzigIngeschrevenen(gebruiker, false, sessie.isGebruikerAanwezig(gebruiker));
					System.out.println("gebruiker is uitgeschreven");
				} else {
					sc.wijzigIngeschrevenen(gebruiker, true, sessie.isGebruikerAanwezig(gebruiker));
					System.out.println("gebruiker is ingeschreven");
				}
			}
		}
		tabelwaardeGebruikersInvullen();

	}

	@FXML // zet aanwezig /niet aanwezig
	void zetGebruikerAanwezig(ActionEvent event) {
		GebruikerSessie gebruikerSessie = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		if (gebruikerSessie != null) {
			if (sessie.isGebruikerAanwezig(gebruikerSessie.getIngeschrevene())) {
				sc.wijzigIngeschrevenen(gebruikerSessie.getIngeschrevene(),
						sessie.isGebruikerIngeschreven(gebruikerSessie.getIngeschrevene()), false);
				btnZetGebruikerAanwezig.setText("Zet afwezig");
				System.out.println("gebruikers is afwezig gezet");

			} else {
				sc.wijzigIngeschrevenen(gebruikerSessie.getIngeschrevene(), true, true);
				btnZetGebruikerAanwezig.setText("Zet aanwezig");
				System.out.println("gebruikers is aanwezig gezet");

			}
		} else {
			Gebruiker gebruiker = tvGebruikers.getSelectionModel().getSelectedItem();
			if (gebruiker != null) {
				if (sessie.isGebruikerAanwezig(gebruiker)) {
					sc.wijzigIngeschrevenen(gebruiker, sessie.isGebruikerIngeschreven(gebruiker), false);
					btnZetGebruikerAanwezig.setText("Zet afwezig");

				} else {
					sc.wijzigIngeschrevenen(gebruiker, true, true);
					btnZetGebruikerAanwezig.setText("Zet aanwezig");

				}
			}
		}
		tabelwaardeGebruikersInvullen();

	}

	@FXML
	void zoekGebruiker(ActionEvent event) {// zoeken voornaam, familienaam, of mail
		changeGebruikerSessieFilter();
//		String gebruikersnaam = txtGebruiker.getText();
//		try {
//			if(cbxStatusGebruiker.getValue().equals("Alle"))
//				gc.changeFilter(gebruikersnaam, null);
//			else
//				sc.changeFilterGebruikerSessie(gebruikersnaam, cbxStatusGebruiker.getValue());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
