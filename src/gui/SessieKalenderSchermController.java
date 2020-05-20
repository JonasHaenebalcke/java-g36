package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import domein.AankondigingController;
import domein.GebruikerController;
import domein.Maand;
import domein.Sessie;
import domein.SessieController;
import domein.SessieKalender;
import domein.SessieKalenderController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class SessieKalenderSchermController extends GridPane {
	@FXML
	private Label lblAcademieJaar;
	@FXML
	private ChoiceBox<Maand> cbMaand;
	@FXML
	private Button btnBeheerSessie;
	@FXML
	private Label lblErrorSessies;
	@FXML
	private Button btnVoegSessieKalenderToe;
	@FXML
	private Label lblStartDatum;
	@FXML
	private Label lblEindDatum;
	@FXML
	private Label lblStartJaar;
	@FXML
	private Label lblEindJaar;
	@FXML
	private Button btnLinks;
	@FXML
	private Button btnRechts;
	@FXML
	private Button btnBeheerSessieKalenders;
	@FXML
	private DatePicker inputStartDatum;
	@FXML
	private DatePicker inputEindDatum;
	@FXML
	private Button btnPasSessieKalenderAan;
	@FXML
	private Button btnMaakNieuweAan;
	@FXML
	private Label lblErrorSessieKalender;
	@FXML
	private Label lblSucces;
	@FXML
	private TableView<SessieKalender> tblSessieKalenders;
	@FXML
	private TableColumn<SessieKalender, String> colStartDatum;
	@FXML
	private TableColumn<SessieKalender, String> colEindDatum;

	@FXML
	private TableView<Sessie> tblSessies;
	@FXML
	private TableColumn<Sessie, String> colTitel;
	@FXML
	private TableColumn<Sessie, String> colStartDatumSessie;
	@FXML
	private TableColumn<Sessie, String> colEindDatumSessie;
	@FXML
	private TableColumn<Sessie, String> colDuur;

	private SessieKalender sk;
	private SessieKalenderController dc;
	private SessieController sc;
	private AankondigingController ac;
	private GebruikerController gc;

	private final Comparator<String> byDatum = (s1, s2) -> s1.substring(6).compareTo(s2.substring(6));

	public SessieKalenderSchermController() {
		this.dc = new SessieKalenderController();
	}

	public SessieKalenderSchermController(SessieKalenderController dc, SessieController sc, GebruikerController gc,
			AankondigingController ac) {
		this.dc = dc;
		this.sc = sc;
		this.gc = gc;
		this.ac = ac;
		sk = dc.getHuidigeSessieKalender();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieKalender.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		inputStartDatum.setEditable(false);
		inputEindDatum.setEditable(false);
		initialize();
		initializeSessieKalenderTable();
		initalizeSessieTable(LocalDate.now().getMonthValue());
		addListenerToTable();
		addListenerToChoiceBox();
	}

	public void initialize() {
		try {
			sk = dc.getHuidigeSessieKalender();
			lblErrorSessies.setText("");
			cbMaand.setItems(FXCollections.observableArrayList(Maand.values()));
			cbMaand.setValue(Maand.valueOf(LocalDate.now().getMonthValue()));

			lblStartDatum.setText(sk.getStartDatum().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblEindDatum.setText(sk.getEindDatum().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblStartJaar.setText("" + sk.getStartDatum().getYear());
			;
			lblEindJaar.setText("" + sk.getEindDatum().getYear());

			lblSucces.setVisible(false);
			lblErrorSessies.setVisible(false);

		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText("ERROR " + e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText("ERROR " + e.toString());
		}
	}

	private void initializeSessieKalenderTable() {
		try {
			lblSucces.setVisible(false);
			lblErrorSessieKalender.setVisible(false);
			tblSessieKalenders.setItems(dc.geefSessieKalenderObservableList());
			colStartDatum.setCellValueFactory(cel -> cel.getValue().getStartDatumProperty());

			colEindDatum.setCellValueFactory(cel -> cel.getValue().getEindDatumProperty());
			colStartDatum.setComparator(byDatum);
			colEindDatum.setComparator(byDatum);
			tblSessieKalenders.getSortOrder().add(colStartDatum);

		} catch (Exception e) {
			lblErrorSessieKalender.setVisible(true);
			lblErrorSessieKalender.setText(e.getMessage());

		}
	}

	private void initalizeSessieTable(Number maand) {
		try {
			sc.geefSessies().forEach(s -> s.setStringProperties());
			tblSessies.setItems(dc.geefSessiesMaand(maand));
			tblSessies.getSortOrder().add(colStartDatumSessie);

			colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
			colStartDatumSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			colEindDatumSessie.setCellValueFactory(cel -> cel.getValue().getStartUurProperty());
			colDuur.setCellValueFactory(cel -> cel.getValue().getDuurProperty());
		} catch (Exception e) {
			tblSessies.getItems().clear();
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
		}
	}

	private void addListenerToTable() {
		tblSessieKalenders.getSelectionModel().selectedItemProperty()
				.addListener((new ChangeListener<SessieKalender>() {
					@Override
					public void changed(ObservableValue<? extends SessieKalender> observable, SessieKalender oldValue,
							SessieKalender newValue) {
						if (newValue != null) {
							inputStartDatum.setValue(newValue.getStartDatum());
							inputEindDatum.setValue(newValue.getEindDatum());
						}
					}
				}));
	}

	private void addListenerToChoiceBox() {
		try {
			cbMaand.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					lblErrorSessies.setVisible(false);
					int temp = (int) newValue + 1;
					initalizeSessieTable((Number) temp);
				}
			});
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	public void voegSessieKalenderToe(ActionEvent event) {
		try {
			tblSessieKalenders.getSelectionModel().clearSelection();
			initializeSessieKalenderTable();
			if (inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
				throw new IllegalArgumentException("Datum is verplicht in te vullen!");
			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
			initializeSessieKalenderTable();
			inputStartDatum.setValue(null);
			inputEindDatum.setValue(null);
			lblSucces.setVisible(true);
			lblSucces.setText("Sessie Kalender werd succesvol toegevoegd!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			lblErrorSessieKalender.setVisible(true);
			lblErrorSessieKalender.setText(e.getMessage());
		}
	}

	@FXML
	public void pasSessieKalenderAan(ActionEvent event) {
		try {
			int skId = tblSessieKalenders.getSelectionModel().getSelectedItem().getSessieKalenderID();
			tblSessieKalenders.getSelectionModel().clearSelection();

			initializeSessieKalenderTable();
			dc.wijzigSessieKalender(skId, inputStartDatum.getValue(), inputEindDatum.getValue());

			inputStartDatum.setValue(null);
			inputEindDatum.setValue(null);
			initializeSessieKalenderTable();
			lblSucces.setVisible(true);
			lblSucces.setText("Sessie Kalender werd succesvol aangepast!");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			lblErrorSessieKalender.setVisible(true);
			lblErrorSessieKalender.setText(e.getMessage());
		}
	}

	@FXML
	void volgendeSessieKalender(ActionEvent event) {
		try {
			dc.geefSessieKalender(true);
			initialize();
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	void vorigeSessieKalender(ActionEvent event) {
		try {
			dc.geefSessieKalender(false);
			initialize();
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}

	@FXML
	public void beheerSessie(ActionEvent event) {
		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
		if (tblSessies.getSelectionModel().getSelectedItem() != null) {
			this.getChildren().setAll(new BeheerSessieSchermController(this.sc, this.gc, sessie, this.ac));
		} else {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText("Je moet een sessie kiezen om het te beheren");
		}

	}
}
