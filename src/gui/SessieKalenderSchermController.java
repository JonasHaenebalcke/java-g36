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
//	@FXML
//	private Button btnNieweSessieKlalender;
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
	private TableView<Sessie> tblSessies;// DataType String?
	@FXML
	private TableColumn<Sessie, String> colTitel;
	@FXML
	private TableColumn<Sessie, String> colStartDatumSessie;
	@FXML
	private TableColumn<Sessie, String> colEindDatumSessie;

	private SessieKalender sk;
	private SessieKalenderController dc;
	private SessieController sc;
	private AankondigingController ac;
	private GebruikerController gc;
	
	private final Comparator<String> byDatum = (s1, s2) -> s1.substring(6).compareTo(s2.substring(6));

	public SessieKalenderSchermController() {
		this.dc = new SessieKalenderController();

//		initialize();
//		initializeSessieKalender();
	}

	public SessieKalenderSchermController(SessieKalenderController dc, SessieController sc, GebruikerController gc, AankondigingController ac) {
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

		// ALs het in commentaar staat moogt ge er mij niet voor judgen
//
//		cbMaand.getItems().addAll(FXCollections.observableArrayList( Arrays.asList(Maand.values()).
//		stream().distinct().
//		map(m -> m.toString())
//		.collect(Collectors.toList())));

		// gives error, should work maybe?
//		cbMaand.getSelectionModel().selectedIndexProperty().addListener((observableValue, value, newValue) -> {
//			if(newValue != null) {
//				lvSessies.setItems(dc.geefSessiesMaand((int)newValue));
//			System.out.println(cbMaand.getSelectionModel().getSelectedIndex());
//			System.out.println(cbMaand.getSelectionModel().getSelectedItem().toString());
//			}
//			}
//		);
		try {
			sk = dc.getHuidigeSessieKalender();
//			System.out.println("INITIALIZE");
			lblErrorSessies.setText("");
			cbMaand.setItems(FXCollections.observableArrayList(Maand.values()));
			cbMaand.setValue(Maand.valueOf(LocalDate.now().getMonthValue()));

//			System.out.println(sk.toString());
//
//			System.out.println(cbMaand.getSelectionModel().getSelectedIndex());
//			System.out.println(cbMaand.getSelectionModel().getSelectedItem().toString());

			lblStartDatum.setText(sk.getStartDatum().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblEindDatum.setText(sk.getEindDatum().format(DateTimeFormatter.ofPattern("MM/dd")));
			lblStartJaar.setText("" + sk.getStartDatum().getYear());
			;
			lblEindJaar.setText("" + sk.getEindDatum().getYear());
//			lblStartDatum = new Label(sk.getStartDate().toString());
//			lblEindDatum = new Label(sk.getEindDate().toString());
//			lvSessies.setItems(dc.geefSessiesMaand(LocalDate.now().getMonthValue()));

			lblSucces.setVisible(false);
			lblErrorSessies.setVisible(false);

		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
			lblErrorSessies.setVisible(true);
//			lblErrorSessies.setText("ERROR " + "Er is geen SessieKalender voor het geselecteerde jaar");
			lblErrorSessies.setText("ERROR " + e.getMessage());
		} catch (Exception e) {
			lblErrorSessies.setVisible(true);
			lblErrorSessies.setText("ERROR " + e.toString());
		}
	}

	private void initializeSessieKalenderTable() {
		try {
//		System.out.println("INITIALIZE SESSIE KALENDER");
			lblSucces.setVisible(false);
			lblErrorSessieKalender.setVisible(false);
//			lvSessieKalender.setItems(dc.geefSessieKalenderObservableList().sorted());
			System.out.println(dc.geefSessieKalenderObservableList().toString());
			tblSessieKalenders.setItems(dc.geefSessieKalenderObservableList());
			tblSessieKalenders.getSortOrder().add(colStartDatum);
//			colStartDatum.setComparator(byDatum);
//			colEindDatum.setComparator(byDatum);
			
			
//			System.out.println(sk.getStartDatumProperty());
//			colStartDatum.setCellValueFactory(cel -> cel.getValue().startDatumProperty()());
//			System.out.println(sk.getStartDatumProperty());
			colStartDatum.setCellValueFactory(cel -> cel.getValue().getStartDatumProperty());

			colEindDatum.setCellValueFactory(cel -> cel.getValue().getEindDatumProperty());

		} catch (Exception e) {
			lblErrorSessieKalender.setVisible(true);
			lblErrorSessieKalender.setText(e.getMessage());

		}
	}

	private void initalizeSessieTable(Number maand) {
		try {
			sc.geefSessies().forEach(s -> s.setStringProperties());
			dc.geefSessiesMaand(maand).forEach(s -> System.out.println(s.toString()));
			tblSessies.setItems(dc.geefSessiesMaand(maand));
			tblSessies.getSortOrder().add(colStartDatumSessie);

			colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
//		colDuur.setCellValueFactory(cel -> cel.getValue().getDuurSessieProperty());
			
			colStartDatumSessie.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
			colEindDatumSessie.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
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
//					Number ret = (Number) temp;
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

//			System.out.println(inputStartDatum.getValue().toString());

			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
//			sc.initialize();
			initializeSessieKalenderTable();
//			Stage stage = (Stage) this.getScene().getWindow();
//			stage.close();
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
//			SessieKalender sessieKalender = lvSessieKalender.getSelectionModel().getSelectedItem();
			dc.wijzigSessieKalender(/* sessieKalender.getSessieKalenderID() */ skId, inputStartDatum.getValue(),
					inputEindDatum.getValue());

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
		this.getChildren().setAll(new BeheerSessieSchermController(this.sc, this.gc, sessie, this.ac));
		
//		System.out.println("beheer sessie");    
//		Sessie sessie = tblSessies.getSelectionModel().getSelectedItem();
//		Scene scene = new Scene(new BeheerSessieSchermController(this.sc, sessie));
//    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//		stage.setTitle("IT LAB");
//		stage.setScene(scene);
//		stage.show();
	}

//	@FXML
//	void beheerSessieKalenders(ActionEvent event) {
//		try {
//		Scene scene = new Scene(new BeheerSessieKalendersController(dc, this), 700, 400);
//		Stage stage = new Stage();
//		stage.setTitle("Sessie Kalender aanmaken");
//		stage.setScene(scene);
//		stage.show();
//	} catch (Exception e) {
//		lblError.setVisible(true);
//		lblError.setText(e.getMessage());
//		System.err.println(e.getMessage());
//	}

//	@FXML
//	void voegSessieKalenderToeBtn(ActionEvent event) {
//		try {
//
//			Scene scene = new Scene(new SessieKalenderAanmakenSchermController(dc, this), 600, 400);
//			Stage stage = new Stage();
//			stage.setTitle("Sessie Kalender aanmaken");
//			stage.setScene(scene);
//			stage.show();
//		} catch (Exception e) {
//			lblError.setVisible(true);
//			lblError.setText(e.getMessage());
//			System.err.println(e.getMessage());
//		}
//	}
}
