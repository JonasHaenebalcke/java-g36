//package gui;
//
//import java.io.IOException;
//import java.util.Comparator;
//
//import domein.Gebruiker;
//import domein.SessieKalender;
//import domein.SessieKalenderController;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.control.Button;
//
//import javafx.scene.control.ListView;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
//
//public class BeheerSessieKalendersController extends AnchorPane {
//	@FXML
//	private ListView<SessieKalender> lvSessieKalender;
//	@FXML
//	private DatePicker inputStartDatum;
//	@FXML
//	private DatePicker inputEindDatum;
//	@FXML
//	private Button btnPasSessieKalenderAan;
//	@FXML
//	private Button btnMaakNieuweAan;
//	@FXML
//	private Label lblErrorSessieKalender;
//	@FXML
//	private Label lblSucces;
//
//	private SessieKalenderController dc;
//	private SessieKalenderSchermController sc;
//
//	public BeheerSessieKalendersController(SessieKalenderController dc, SessieKalenderSchermController sc)
//			throws IOException {
//		try {
//			this.sc = sc;
//			this.dc = dc;
//
//			System.out.println("Laad nieuw beheer sessiekalender toe scherm");
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(getClass().getResource("BeheerSessieKalenders.fxml"));
//			loader.setRoot(this);
//			loader.setController(this);
//			loader.load();
//
//		} catch (Exception e) {
//			lblError.setVisible(true);
//			lblError.setText("ERROR " + e.getMessage());
//			System.err.println(e.getMessage());
//		}
//		initialize();
//		addListenerToList();
//	}
//
//	private void initialize() {
//		try {
//			lblSucces.setVisible(false);
//			lblError.setVisible(false);
//			lvSessieKalender.setItems(dc.geefSessieKalenderObservableList().sorted());
//
//			
//		} catch (Exception e) {
//			lblError.setVisible(true);
//			lblError.setText(e.getMessage());
//		}
//	}
//	
//	
//	private void addListenerToList() {
//		lvSessieKalender.getSelectionModel().selectedItemProperty()
//		.addListener((new ChangeListener<SessieKalender>() {
//			@Override
//			public void changed(ObservableValue<? extends SessieKalender> observable,
//					SessieKalender oldValue, SessieKalender newValue) {
//				if(newValue != null) {
//				inputStartDatum.setValue(newValue.getStartDate());
//				inputEindDatum.setValue(newValue.getEindDate());
//				}
//			}
//		}));
//	}
//
//	@FXML
//	public void voegSessieKalenderToe(ActionEvent event) {
//		try {
//			lvSessieKalender.getSelectionModel().clearSelection();
//			initialize();
//			if (inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
//				throw new IllegalArgumentException("Datum is verplicht in te vullen!");
//			
//			System.out.println(inputStartDatum.getValue().toString());
//			
//			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
////			sc.initialize();
//			initialize();
////			Stage stage = (Stage) this.getScene().getWindow();
////			stage.close();
//			lblSucces.setVisible(true);
//			lblSucces.setText("Sessie Kalender werd succesvol toegevoegd!");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			lblError.setVisible(true);
//			lblError.setText(e.getMessage());
//		}
//	}
//
//	@FXML
//	public void pasSessieKalenderAan(ActionEvent event) {
//		try {
//			int skId = lvSessieKalender.getSelectionModel().getSelectedItem().getSessieKalenderID();
//			lvSessieKalender.getSelectionModel().clearSelection();
//			initialize();
////			SessieKalender sessieKalender = lvSessieKalender.getSelectionModel().getSelectedItem();
//			dc.wijzigSessieKalender(/*sessieKalender.getSessieKalenderID()*/ skId, inputStartDatum.getValue(),
//					inputEindDatum.getValue());
//			initialize();
//			lblSucces.setVisible(true);
//			lblSucces.setText("Sessie Kalender werd succesvol aangepast!");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			lblError.setVisible(true);
//			lblError.setText(e.getMessage());
//		}
//	}
//}
