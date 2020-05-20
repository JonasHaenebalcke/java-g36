package gui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import domein.Aankondiging;
import domein.AankondigingController;
import domein.GebruikerController;
import domein.GebruikerSessie;
import domein.Sessie;
import domein.SessieController;
import domein.SessieKalender;
import domein.Status;
import domein.StatusSessie;
import domein.TypeGebruiker;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
import javafx.scene.paint.Paint;
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
	private TableColumn<Sessie, String> colStartUur;

	@FXML
	private TableColumn<Sessie, String> colOpenPlaatsen;

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
	private TableColumn<Aankondiging, Boolean> colMailVerstuurd;
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
	private Button btnMailVerzenden;

	@FXML
	private Button btnVerwijder;

	@FXML
	private Button btnWijzig;

	@FXML
	private ComboBox<String> cbxFilter;

	private SessieController sc;
	private AankondigingController ac;
	private GebruikerController gc;

	public AankondigingenSchermController(SessieController sc, AankondigingController ac, GebruikerController gc) {
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
		this(sc, ac, gc);
		if (sessie != null) {
			tvSessies.getSelectionModel().select(sessie);
		}
	}

	private void initialize() {
		try {

			tvSessies.setItems(sc.geefSessiesSorted());
			colVerantwoordelijke.setCellValueFactory(
					cel -> new ReadOnlyStringWrapper(cel.getValue().getVerantwoordelijke().getFamilienaam() + " "
							+ cel.getValue().getVerantwoordelijke().getVoornaam()));
			colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStart.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			colStartUur.setCellValueFactory(cel -> cel.getValue().getStartUurProperty());
			colLokaal.setCellValueFactory(cel -> new ReadOnlyStringWrapper(cel.getValue().getLokaal()));
			colOpenPlaatsen.setCellValueFactory(cel -> cel.getValue().getOpenPlaatsenCapaciteitProperty());
			sc.changeSorter(null);
			List<String> statussen = new ArrayList<>();
			statussen.add("Alle Types");
			for (StatusSessie status : StatusSessie.values()) {
				statussen.add(status.toString());
			}
			cbxFilter.setItems(FXCollections.observableArrayList(statussen));
			cbxFilter.getSelectionModel().selectFirst();
			btnMailVerzenden.setDisable(true);
			btnVerwijder.setDisable(true);
			btnWijzig.setDisable(true);
			initializeTvAankondigingen();
			addListenerToTableAankondigingen();
			addListenerToTableSessies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addListenerToTableAankondigingen() {

		tvAankondigingen.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Aankondiging>() {

			@Override
			public void changed(ObservableValue<? extends Aankondiging> observable, Aankondiging oldValue,
					Aankondiging newValue) {
				lblError.setVisible(false);
				btnVerwijder.setDisable(false);
				btnWijzig.setDisable(false);
				btnMailVerzenden.setDisable(false);
				if (newValue != null) {
					txtTitel.setText(newValue.titel);
					txtAankondiging.setText(newValue.aankondingingTekst);
				} else {
					txtTitel.setText(oldValue.titel);
					txtAankondiging.setText(oldValue.aankondingingTekst);
				}
			}
		});
	}

	private void addListenerToTableSessies() {
		tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> observable, Sessie oldValue, Sessie newValue) {
				if (newValue != null) {
					initializeTvAankondigingen(newValue);
				} else {
					initializeTvAankondigingen(oldValue);
				}
			}
		});
	}

	private void initializeTvAankondigingen() {
		tvAankondigingen.setItems(ac.geefAankondigingObservableList());
		colPublicist.setCellValueFactory(cel -> cel.getValue().getPublicistProperty());
		colTitelAankondiging.setCellValueFactory(cel -> cel.getValue().getTitelAankondigingProperty());
		colDatumAankondiging.setCellValueFactory(cel -> cel.getValue().getDatumAankondigingProperty());
		colMailVerstuurd.setCellFactory(column -> new CheckBoxTableCell<>());

		colMailVerstuurd.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Aankondiging, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<Aankondiging, Boolean> a) {
						return new SimpleBooleanProperty(a.getValue().isVerzonden);
					}
				});
	}

	private void initializeTvAankondigingen(Sessie sessie) {
		tvAankondigingen.getItems().clear();
		tvAankondigingen.setItems(ac.geefAankondigingObservableList(sessie));
		colPublicist.setCellValueFactory(cel -> cel.getValue().getPublicistProperty());
		colTitelAankondiging.setCellValueFactory(cel -> cel.getValue().getTitelAankondigingProperty());
		colDatumAankondiging.setCellValueFactory(cel -> cel.getValue().getDatumAankondigingProperty());
		colMailVerstuurd.setCellFactory(column -> new CheckBoxTableCell<>());

		colMailVerstuurd.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Aankondiging, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<Aankondiging, Boolean> a) {
						return new SimpleBooleanProperty(a.getValue().isVerzonden);
					}
				});
	}

	@FXML
	void aankondigingPlaatsen(ActionEvent event) {
		try {
			lblError.setVisible(false);
			Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
			ac.voegAankondigingToe(txtTitel.getText(), txtAankondiging.getText(), false, sessie,
					gc.getIngelogdeVerantwoordelijke());
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("green"));
			lblError.setText("Aankondiging werd succesvol opgeslagen");
		} catch (Exception e) {
			lblError.setTextFill(Paint.valueOf("red"));
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void verzendMail(ActionEvent event) {
		try {
			if (tvSessies.getSelectionModel().getSelectedItem() == null) {
				throw new IllegalArgumentException("Sessie moet ingevuld zijn!");
			}
			Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();

			lblError.setVisible(true);
			if (tvAankondigingen.getSelectionModel().getSelectedItem() != null) {
				ac.setGekozenAankondiging(tvAankondigingen.getSelectionModel().getSelectedItem());
			} else {
				btnMailVerzenden.setDisable(true);
				Aankondiging nieuwAankondiging = new Aankondiging(txtTitel.getText(), txtAankondiging.getText(), sessie,
						gc.getIngelogdeVerantwoordelijke(), false);
				ac.setGekozenAankondiging(nieuwAankondiging);
			}
			ac.verzendAankondiging();
			initializeTvAankondigingen(tvSessies.getSelectionModel().getSelectedItem());
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("green"));
			lblError.setText("De mail is verzonden!");
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("red"));
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void aankondigingWijzigen(ActionEvent event) {
		try {
			lblError.setVisible(false);
			ac.setGekozenAankondiging(tvAankondigingen.getSelectionModel().getSelectedItem());
			ac.wijzigAankondiging(txtTitel.getText(), txtAankondiging.getText());
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("green"));
			lblError.setText("Aankondiging werd succesvol gewijzigd");
		} catch (Exception e) {
			lblError.setTextFill(Paint.valueOf("red"));
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void aankondigingVerwijderen(ActionEvent event) {
		try {
			lblError.setVisible(false);
			ac.verwijderAankondiging(tvAankondigingen.getSelectionModel().getSelectedItem());
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("green"));
			lblError.setText("Aankondiging werd succesvol verwijderd");
		} catch (Exception e) {
			lblError.setTextFill(Paint.valueOf("red"));
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void beheerSessie(ActionEvent event) {
		Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		if (tvSessies.getSelectionModel().getSelectedItem() != null) {
			this.getChildren().setAll(new BeheerSessieSchermController(this.sc, this.gc, sessie, this.ac));
		} else {
			lblError.setVisible(true);
			lblError.setTextFill(Paint.valueOf("red"));
			lblError.setText("Je moet een sessie kiezen om het te beheren");
		}
	}

	@FXML
	void zoek(ActionEvent event) {
		changeFilter();
	}

	private void changeFilter() {
		String filter = txtZoek.getText();
		String status = cbxFilter.getValue().toString();
		sc.changeFilter(filter, status, null);
	}

}
