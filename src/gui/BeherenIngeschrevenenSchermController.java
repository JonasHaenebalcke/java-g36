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
	private Label lblTitelTabelSessies;
	@FXML
	private TableView<Sessie> tvSessies;
	@FXML
	private TableColumn<Sessie, String> colTitelSessie;
	@FXML
	private TableColumn<Sessie, String> colStartSessie;
	@FXML
	private TableColumn<Sessie, String> colStartUur;
	@FXML
	private TableColumn<Sessie, String> colDuur;

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
		statussen.add("ingeschrevenen sessies");
		cbxStatusSessie.setItems(FXCollections.observableArrayList(statussen));
		cbxStatusSessie.getSelectionModel().selectFirst();

		List<String> statusGebruikers = new ArrayList<>();
		statusGebruikers.add(0, "Alle gebruikers");
		statusGebruikers.add(1, "Ingeschrevenen");
		statusGebruikers.add(2, "Aanwezigen");
		statusGebruikers.add(3, "Afwezigen");
		System.out.println(statusGebruikers);
		cbxStatusGebruiker.setItems(FXCollections.observableArrayList(statusGebruikers));
		cbxStatusGebruiker.getSelectionModel().selectFirst();

		try {
			initializeSessiesList();
//			tvSessies.setItems(sc.geefSessiesSorted());// .geefSessiesObservable().sorted(Comparator.comparing(Sessie::getStartDatum)));
//			colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
//			colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
//			ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
			sc.changeSorter(null);

			tvIngeschrevenen.setVisible(false);
			tvGebruikers.setItems(gc.geefGebruikersSorted());
			colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
			colFamilienaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			tabelwaardeGebruikersInvullen();
			txtWaardeInvullenGebruikerGekozen();

		} catch (NullPointerException e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.toString());
		}

	}

	private void initializeSessiesList() {
		tvSessies.setItems(sc.geefSessiesSorted());// .geefSessiesObservable().sorted(Comparator.comparing(Sessie::getStartDatum)));
		colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
		colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
		colStartUur.setCellValueFactory(cel -> cel.getValue().getStartUurProperty());
		colDuur.setCellValueFactory(cel -> cel.getValue().getDuurProperty());
	}

	private void tabelwaardeGebruikersInvullen() {

		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				if (newV == null)
					return;
				sc.setHuidigeSessie(newV);
				tvIngeschrevenen.getSelectionModel().clearSelection();
				tvIngeschrevenen.setItems(sc.geefGebruikerSessiesSorted());
				changeGebruikerSessieFilter();
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

				if (tvGebruikers.getSelectionModel() != null) {
					if (newV.isGebruikerIngeschreven(tvGebruikers.getSelectionModel().getSelectedItem())) {
						btnSchrijfGebruikerIn.setText("Schrijf uit");
						if (newV.isGebruikerAanwezig(tvGebruikers.getSelectionModel().getSelectedItem()))
							btnZetGebruikerAanwezig.setText("Zet afwezig");
					} else {
						btnSchrijfGebruikerIn.setText("Schrijf in");
						btnZetGebruikerAanwezig.setText("Zet aanwezig");
					}
				}

			}
		});
		txtWaardeInvullenIngeschreveneGekozen();

	}

	private void txtWaardeInvullenIngeschreveneGekozen() {
		tvIngeschrevenen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GebruikerSessie>() {

			@Override
			public void changed(ObservableValue<? extends GebruikerSessie> gebruikerObs, GebruikerSessie oldV,
					GebruikerSessie newV) {
				if (newV == null)
					return;

//				cbxStatusSessie.getSelectionModel().selectLast();
				changeSessieFilter();
				// tvSessies.setItems(gc.geefSessiesGebruikerObservable(newV.getIngeschrevene()));
//				colTitelSessie.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
//				colStartSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
//				ColEindSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
				btnSchrijfGebruikerIn.setText("Schrijf uit");
				if (newV.isAanwezig()) {
					btnZetGebruikerAanwezig.setText("Zet afwezig");
				} else {
					btnZetGebruikerAanwezig.setText("Zet aanwezig");
				}
			}

		});

	}

	private void txtWaardeInvullenGebruikerGekozen() {
		tvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {

			@Override
			public void changed(ObservableValue<? extends Gebruiker> gebruikerObs, Gebruiker oldV, Gebruiker newV) {
				if (newV == null)
					return;
				changeSessieFilter();
				if (tvSessies.getSelectionModel() != null) {
					if (tvSessies.getSelectionModel().getSelectedItem().isGebruikerIngeschreven(newV)) {
						btnSchrijfGebruikerIn.setText("Schrijf uit");
						if (tvSessies.getSelectionModel().getSelectedItem().isGebruikerAanwezig(newV))
							btnZetGebruikerAanwezig.setText("Zet afwezig");
					} else {
						btnSchrijfGebruikerIn.setText("Schrijf in");
						btnZetGebruikerAanwezig.setText("Zet aanwezig");
					}
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
		if (cbxStatusGebruiker.getValue().equals("Alle gebruikers")) {
			tvGebruikers.setVisible(true);
			tvIngeschrevenen.setVisible(false);

		} else {
			tvGebruikers.setVisible(false);
			tvIngeschrevenen.setVisible(true);
		}
		switch (cbxStatusGebruiker.getValue()) {
		case "Alle gebruikers":
			lbltitelTabelGebruikers.setText("Alle gebruikers");
			break;
		case "Ingeschrevenen":
			lbltitelTabelGebruikers.setText("Ingeschreven gebruikers");
			break;
		case "Aanwezigen":
			lbltitelTabelGebruikers.setText("Aanwezige gebruikers");
			break;
		case "Afwezigen":
			lbltitelTabelGebruikers.setText("Afwezige gebruikers");
			break;
		}
		changeGebruikerSessieFilter();

	}

	@FXML
	void geefSessiesGekozenStatus(ActionEvent event) {
		try {
			if (!cbxStatusGebruiker.getValue().equals("Alle gebruikers")
					&& tvIngeschrevenen.getSelectionModel() != null) {
				sc.setHuidigeSessie(null);
				tvSessies.getSelectionModel().clearSelection();
				tvIngeschrevenen.setItems(sc.geefGebruikerSessiesSorted());
			}
//			else {
			switch (cbxStatusSessie.getValue()) {
			case "Alle Types":
				lblTitelTabelSessies.setText("Alle sessies");
				break;
			case "open":
				lblTitelTabelSessies.setText("Open sessies");
				break;
			case "gesloten":
				lblTitelTabelSessies.setText("Gesloten sessies");
				break;
			case "InschrijvingenOpen":
				lblTitelTabelSessies.setText("Sessies open voor inschrijvingen");
				break;
			case "nietOpen":
				lblTitelTabelSessies.setText("Niet open sessies");
				break;
			}

			changeSessieFilter();
//			}

		} catch (NullPointerException e) {
			lblError.setText(e.getMessage());
		}
	}

	private void changeSessieFilter() {
		String filter = txtSessie.getText();
		String status = cbxStatusSessie.getValue().toString();
//		GebruikerSessie gs = tvIngeschrevenen.getSelectionModel().getSelectedItem();
		Gebruiker gs = tvGebruikers.getSelectionModel().getSelectedItem();
		System.out.println(gs);
		if (gs != null && status.toLowerCase().contentEquals("ingeschrevenen sessies")) {
			// initializeSessiesList();

			sc.changeFilter(filter, status, gs);
			// .getIngeschrevene());
		} else {

			System.out.println("filter: " + filter);
			System.out.println("status: " + status);
			sc.changeFilter(filter, status, null);
		}
	}

	private void changeGebruikerSessieFilter() {
		String filter = txtGebruiker.getText();
		String status = cbxStatusGebruiker.getValue();

		System.out.println("filter: " + filter);
		System.out.println("status: " + status);

		if (cbxStatusGebruiker.getValue().equals("Alle gebruikers"))
			gc.changeFilter(filter, null, null);
		else if (tvSessies.getSelectionModel().getSelectedItem() != null)
			sc.changeFilterGebruikerSessie(filter, cbxStatusGebruiker.getValue());

	}

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
	void zoekGebruiker(ActionEvent event) {
		changeGebruikerSessieFilter();
	}

	@FXML
	void zoekSessie(ActionEvent event) {
		changeSessieFilter();
	}

}
