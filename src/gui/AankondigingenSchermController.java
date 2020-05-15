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
	private ComboBox<String> cbxFilter;

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
			tvSessies.setItems(sc.geefSessiesSorted());
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

			List<String> statussen = new ArrayList<>();
			statussen.add("Alle");
			for (StatusSessie status : StatusSessie.values()) {
				statussen.add(status.toString());
			}
			cbxFilter.setItems(FXCollections.observableArrayList(statussen));
			cbxFilter.getSelectionModel().selectFirst();
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
	void verzendMail(ActionEvent event) {
		String naar = "jule.dekyvere@student.hogent.be"; //"audrey.behiels@student.hogent.be"; 
		String van = "ProjectITLab@outlook.com";//gc.getIngelogdeVerantwoordelijke().getMailadres(); 
		String host = "localhost";
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth","true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp-mail.outlook.com");
		properties.put("mail.smtp.port", "587");
		
	Session sessie = Session.getDefaultInstance(properties, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("ProjectITLab@outlook.com", "ProjectenSem2");
		}
	});
		try {
			MimeMessage message = new MimeMessage(sessie);
			message.setFrom(new InternetAddress(van));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(naar));
			message.setSubject("Onderwerp test");
			message.setText("Dit bericht is een test");
			Transport.send(message);
			System.out.println("bericht goed verzonden..");
		} catch(MessagingException e)
		{
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void beheerSessie(ActionEvent event) {
		Sessie sessie = tvSessies.getSelectionModel().getSelectedItem();
		this.getChildren().setAll(new BeheerSessieSchermController(this.sc, this.gc, sessie, this.ac));
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
