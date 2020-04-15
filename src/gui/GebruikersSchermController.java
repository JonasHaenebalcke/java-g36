package gui;

import java.io.IOException;
import java.util.stream.Stream;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.Status;
import domein.TypeGebruiker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GebruikersSchermController extends AnchorPane {

	@FXML
	private Button btnVoegToe;

	@FXML
	private ListView<Gebruiker> lvGebruikers;

	@FXML
	private TextField inputNaam;

	@FXML
	private TextField inputVoornaam;

	@FXML
	private Label lblTitle;

	@FXML
	private ComboBox<TypeGebruiker> cbxType;

	@FXML
	private ComboBox<Status> cbxStatus;

	@FXML
	private ImageView imageProfielfoto;

	@FXML
	private Button btnPasAan;

	@FXML
	private Button btnVerwijder;

	@FXML
	private TextField inputEmail;

	@FXML
	private TextField inputGebruikersnaam;

	@FXML
	private Button btnGebruikerToevoegen;

	@FXML
	private Label lblError;
	@FXML
	private Label lblWachtwoord;

	@FXML
//	private TextField inputWachtwoord;
	private PasswordField  inputWachtwoord;

	private GebruikerController dc;

	public GebruikersSchermController() {
		this.dc = new GebruikerController();
	}

	public GebruikersSchermController(GebruikerController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikersScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initialize();
	}

	public void initialize() {
//    	ObservableList<Gebruiker> items = dc.geefGebruikersObservableList();
//    	 lvGebruikers.setItems(items);
		// dc.GeefGebruikersList().forEach(g ->
		// items.add(g/*g.getFamilienaam()+g.getVoornaam()*/));

		initializeList();
		// lvGebruikers.setItems(dc.geefGebruikersObservableList());
		cbxStatus.setItems(FXCollections.observableArrayList(Status.values()));
		cbxType.setItems(FXCollections.observableArrayList(TypeGebruiker.values()));

		lvGebruikers();
		btnPasAan.setVisible(false);
		btnVerwijder.setVisible(false);
		lblTitle.setText("Voeg gebruiker toe");
		inputWachtwoord.setVisible(true);
		lblWachtwoord.setVisible(true);
		btnVoegToe.setVisible(true);
		btnGebruikerToevoegen.setVisible(true);
	}

	@FXML
	private void pasGebruikerAan(ActionEvent event) {
		inputGebruikersnaam.setEditable(false);
		try {
			cbxType.setValue(cbxType.getValue());

			dc.wijzigGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(),
					inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto");
			initializeList();
			cbxType.setValue(cbxType.getValue());
		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
	}

	@FXML
	private void verwijderGebruiker(ActionEvent event) {
		try {
			int index = lvGebruikers.getSelectionModel().getSelectedIndex();

			//lvGebruikers.getSelectionModel().clearSelection();
			dc.verwijderGebruiker(index);
			initializeList();
			Stream.of(inputVoornaam, inputEmail, inputNaam, inputGebruikersnaam, inputWachtwoord)
					.forEach(TextField::clear);
		} catch (Exception e) {
			lblError.setText(e.getMessage());
			System.out.print(e.getMessage());
		}
	}

	@FXML
	void voegGebruikerToeBtn(ActionEvent event) {
		lblTitle.setText("Voeg gebruiker toe");
		Stream.of(inputVoornaam, inputEmail, inputNaam, inputGebruikersnaam, inputWachtwoord)
				.forEach(TextField::clear);
		cbxStatus.setValue(Status.Actief);
		cbxType.setValue(TypeGebruiker.Gebruiker);
		inputGebruikersnaam.setEditable(true);
		btnVoegToe.setVisible(true);
		btnVerwijder.setVisible(false);
		btnPasAan.setVisible(false);
		inputWachtwoord.setVisible(true);
		lblWachtwoord.setVisible(true);
		initializeList();
	}

	@FXML
	void voegGebruikerToe(ActionEvent event) {
		try {
			if (!inputVoornaam.getText().isBlank() && !inputNaam.getText().isBlank() && !inputEmail.getText().isBlank()
					&& !inputGebruikersnaam.getText().isBlank() && !inputWachtwoord.getText().isBlank()
				/*&&!cbxType.getValue().equals("Type") && !cbxStatus.getValue().equals("Status")*/ ) {
				
			
					dc.voegToeGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(),
							inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto",
							inputWachtwoord.getText());
					lvGebruikers.getSelectionModel().selectLast();
					
					initializeList();
					inputGebruikersnaam.setEditable(false);
					
			} else {
				lblError.setText("Tekstvakken mogen niet leeg zijn");
			}

		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
	}

	void lvGebruikers() {
		lvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {

			@Override
			public void changed(ObservableValue<? extends Gebruiker> observable, Gebruiker oldValue,
					Gebruiker newValue) {
				lblTitle.setText("Gebruiker aanpassen");
				btnVoegToe.setVisible(false);
				btnPasAan.setVisible(true);
				btnVerwijder.setVisible(true);
				inputWachtwoord.setVisible(false);
				lblWachtwoord.setVisible(false);
				btnGebruikerToevoegen.setVisible(true);
				inputVoornaam.setText(newValue.getVoornaam());
				inputNaam.setText(newValue.getFamilienaam());
				inputEmail.setText(newValue.getMailadres());
				inputGebruikersnaam.setText(newValue.getGebruikersnaam());
				cbxStatus.setValue(newValue.getStatus());
				cbxType.setValue(newValue.getType());

				inputGebruikersnaam.setEditable(false);
			}
		});

	}

	void initializeList() {
		lvGebruikers.setItems(dc.geefGebruikersObservableList());
		
	}

}
