package gui;

import java.io.IOException;

import domein.Sessie;
import domein.SessieController;
import domein.StatusSessie;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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

	@FXML // MOET VERANDERd WORDEN VAN SESSIE NAAR AANKODIGING!!!
	private TableView<Sessie> tvAankondigingen; // puur voor demo

	@FXML // MOET VERANDERd WORDEN VAN SESSIE NAAR AANKODIGING!!!
	private TableColumn<Sessie, String> colPublicist;

	@FXML // MOET VERANDERd WORDEN VAN SESSIE NAAR AANKODIGING!!!
	private TableColumn<Sessie, String> colTitelAankondiging;

	@FXML // MOET VERANDERd WORDEN VAN SESSIE NAAR AANKODIGING!!!
	private TableColumn<Sessie, String> colDatumAankondiging;

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

	public AankondigingenSchermController(SessieController sc) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AankondigingenScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.sc = sc;
		initialize();
	}
	
	public AankondigingenSchermController(SessieController sc, Sessie sessie) {
		this(sc);
		if(sessie!=null) {
		tvSessies.getSelectionModel().select(sessie);
		}
		
	}

	private void initialize() {
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
		cbxDagenOpVoorhand.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7));

		tvAankondigingen.setItems(sc.geefSessiesObservable());
		colPublicist.setCellValueFactory(cel -> new ReadOnlyStringWrapper("Lucas Van Der Haegen"));
		colTitelAankondiging.setCellValueFactory(
				cel -> new ReadOnlyStringWrapper("Verandering van lokaal sessie \"Omgaan met frustratie problemen\""));
		colDatumAankondiging.setCellValueFactory(cel -> new ReadOnlyStringWrapper("05/05/2020 15:30"));
	}

	@FXML
	void aankondigingPlaatsen(ActionEvent event) {

	}

	@FXML
	void beheerIngeschrevenen(ActionEvent event) {

	}

	@FXML
	void zoek(ActionEvent event) {

	}

}
