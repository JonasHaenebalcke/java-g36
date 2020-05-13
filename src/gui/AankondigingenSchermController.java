package gui;

import java.io.IOException;

import domein.Aankondiging;
import domein.AankondigingController;
import domein.GebruikerController;
import domein.GebruikerSessie;
import domein.Sessie;
import domein.SessieController;
import domein.StatusSessie;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class AankondigingenSchermController extends GridPane {

	@FXML
	private TableView<Sessie> tvSessies;

	@FXML
	private TableColumn<Sessie, String> colVerantwoordelijke;

	@FXML
	private TableColumn<Sessie, String> colTitel;

	@FXML
	private TableColumn<Sessie, String> colStart;

	@FXML
	private TableColumn<Sessie, String> colEind;

	@FXML
	private TableColumn<Sessie, String> colOpnePlaatsen;

	@FXML
	private TableColumn<Sessie, String> colLokaal;

	@FXML
	private TableView<Aankondiging> tvAankondigingen;

	@FXML
	private TableColumn<Aankondiging, String> colPublicist;

	@FXML
	private TableColumn<Aankondiging, String> colTitelAankondiging;

	@FXML
	private TableColumn<Aankondiging, String> colDatumAankondiging;
	@FXML
	private TableColumn<Aankondiging, String> colMailVerstuurd;
	@FXML
	private TableColumn<Aankondiging, String> colVerstuurMail;

	@FXML
	private CheckBox cbHerinnering;

	@FXML
	private ComboBox<Integer> cbxDagenOpVoorhand;

	@FXML
	private TextArea txtAankondiging;

	@FXML
	private TextField txtTitel;

	@FXML
	private Label lblError;

	@FXML
	private TextField txtZoek;

	@FXML
	private ComboBox<StatusSessie> cbxFilter;

	private SessieController sc;
	private AankondigingController ac;
	private GebruikerController gc;

	public AankondigingenSchermController(SessieController sc, AankondigingController ac) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AankondigingenScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.sc = sc;
		this.ac = ac;
		this.gc = gc;
		initialize();
	}

	public AankondigingenSchermController(SessieController sc, Sessie sessie, AankondigingController ac,
			GebruikerController gc) {
		this(sc, ac);
		if (sessie != null) {
			tvSessies.getSelectionModel().select(sessie);
		}

	}

	private void initialize() {
		try {
			tvSessies.setItems(sc.geefSessiesObservable());
			colVerantwoordelijke.setCellValueFactory(
					cel -> new ReadOnlyStringWrapper(cel.getValue().getVerantwoordelijke().getFamilienaam() + " "
							+ cel.getValue().getVerantwoordelijke().getVoornaam()));
			colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStart.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			colEind.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
			colLokaal.setCellValueFactory(cel -> new ReadOnlyStringWrapper(cel.getValue().getLokaal()));
			colOpnePlaatsen.setCellValueFactory(
					cel -> new ReadOnlyStringWrapper(Integer.toString(cel.getValue().getCapaciteit()))); // moet nog
																											// veranderd
																											// worden

			cbxFilter.setItems(FXCollections.observableArrayList(StatusSessie.values()));
//			cbxDagenOpVoorhand.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7));

			System.out.println(ac.geefAankondigingen().toString());
			initializeTvAankondigingen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeTvAankondigingen() {
		tvAankondigingen.setItems(ac.geefAankondigingObservableList());
		colPublicist.setCellValueFactory(cel -> cel.getValue().getPublicistProperty());
		colTitelAankondiging.setCellValueFactory(cel -> cel.getValue().getTitelAankondigingProperty());
		colDatumAankondiging.setCellValueFactory(cel -> cel.getValue().getDatumAankondigingProperty());
//		colMailVerstuurd.setCellFactory(column -> new CheckBoxTableCell<>());

//		colMailVerstuurd.setCellValueFactory(
//				new Callback<TableColumn.CellDataFeatures<Aankondiging, Boolean>, ObservableValue<Boolean>>() {
//					@Override
//					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Aankondiging, Boolean> gs) {
//						return new SimpleBooleanProperty(gs.getValue().isVerzonden);
//					}
//				});

//		colVerstuurMail
//				.setCellFactory(ActionButtonTableCell.<Aankondiging>forTableColumn("Verzend Aankondiging", (Aankondiging a) -> {
//					ac.verzendAankondiging(tvAankondigingen.getSelectionModel().getSelectedIndex());
//					return a;
//				}));
	}

	@FXML
	void aankondigingPlaatsen(ActionEvent event) {
		try {
			Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
			// Controlleren of Sessie null is
			// Controleren of verantwoordelijke geplaatst mag worden
			ac.voegAankondigingToe(txtTitel.getText(), txtAankondiging.getText(), false, sessie,
					gc.getIngelogdeVerantwoordelijke());
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void beheerIngeschrevenen(ActionEvent event) {

	}

	@FXML
	void zoek(ActionEvent event) {
		changeFilter();
	}

	private void changeFilter() {
		String filter = txtZoek.getText();
		String status = cbxFilter.getValue().toString();

		System.out.println("filter: " + filter);
		System.out.println("status: " + status);
		sc.changeFilter(filter, status);
	}

}
