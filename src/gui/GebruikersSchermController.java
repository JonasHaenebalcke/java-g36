package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.Status;
import domein.StatusSessie;
import domein.TypeGebruiker;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class GebruikersSchermController extends GridPane {

	@FXML
	private Button btnVoegToe;

	@FXML
	private TableView<Gebruiker> tvGebruikers;

	@FXML
	private TableColumn<Gebruiker, String> colNaam;

	@FXML
	private TableColumn<Gebruiker, String> colVoornaam;

	@FXML
	private TableColumn<Gebruiker, String> colEmail;

	@FXML
	private TableColumn<Gebruiker, String> colType;

	@FXML
	private TableColumn<Gebruiker, String> colStatus;

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
	private TextField txtZoek;

	@FXML
	private ComboBox<String> cbxFilter;

	@FXML
	private ComboBox<String> cbxFilterType;

	@FXML
	private Label lblError;

	@FXML
	private Label lblSucces;

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
		List<String> statussen = new ArrayList<>();
		statussen.add("Alle");
		for (Status status : Status.values()) {
			statussen.add(status.toString());
		}
		cbxFilter.setItems(FXCollections.observableArrayList(statussen));
		cbxFilter.getSelectionModel().selectFirst();

		List<String> types = new ArrayList<>();
		types.add("Alle");
		for (TypeGebruiker type : TypeGebruiker.values()) {
			types.add(type.toString());
		}
		cbxFilterType.setItems(FXCollections.observableArrayList(types));
		cbxFilterType.getSelectionModel().selectFirst();
		tvGebruikers();
		statusChangeListener();
		typeChangeListener();
		btnPasAan.setVisible(false);
		btnVerwijder.setVisible(false);
		lblTitle.setText("Voeg gebruiker toe");

		btnVoegToe.setVisible(true);
		btnGebruikerToevoegen.setVisible(true);
	}

	@FXML
	private void pasGebruikerAan(ActionEvent event) {
		try {
			lblSucces.setText("");
			inputGebruikersnaam.setEditable(false);
			lblError.setText("");
			cbxType.setValue(cbxType.getValue());
			dc.wijzigGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(),
					inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto");

			cbxType.setValue(cbxType.getValue());

			lblSucces.setText("Gebruiker werd succesvol aangepast");
		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
		initializeList();
	}

	@FXML
	private void verwijderGebruiker(ActionEvent event) {
		try {
			lblSucces.setText("");
			lblError.setText("");
			// int index = tvGebruikers.getSelectionModel().getSelectedIndex();
			Gebruiker gebruiker = tvGebruikers.getSelectionModel().getSelectedItem();

			// lvGebruikers.getSelectionModel().clearSelection();
			dc.verwijderGebruiker(gebruiker);
			initializeList();
			Stream.of(inputVoornaam, inputEmail, inputNaam, inputGebruikersnaam).forEach(TextField::clear);
			lblSucces.setText("Gebruiker werd succesvol verwijderd");
		} catch (Exception e) {
			lblError.setText(e.getMessage());
			System.out.print(e.getMessage());
		}
	}

	@FXML
	void voegGebruikerToeBtn(ActionEvent event) {
		lblTitle.setText("Voeg gebruiker toe");
		Stream.of(inputVoornaam, inputEmail, inputNaam, inputGebruikersnaam).forEach(TextField::clear);
		cbxStatus.setValue(Status.Actief);
		cbxType.setValue(TypeGebruiker.Gebruiker);
		inputGebruikersnaam.setEditable(true);
		btnVoegToe.setVisible(true);
		btnVerwijder.setVisible(false);
		btnPasAan.setVisible(false);

		initializeList();
	}

	@FXML
	void voegGebruikerToe(ActionEvent event) {
		try {
			lblError.setText("");
			lblSucces.setText("");
			if (!inputVoornaam.getText().isBlank() && !inputNaam.getText().isBlank() && !inputEmail.getText().isBlank()
					&& !inputGebruikersnaam.getText().isBlank()
			/*
			 * &&!cbxType.getValue().equals("Type") &&
			 * !cbxStatus.getValue().equals("Status")
			 */ ) {

				dc.voegToeGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(),
						inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto");
				tvGebruikers.getSelectionModel().selectLast();

				initializeList();
				inputGebruikersnaam.setEditable(false);
				lblSucces.setText("Gebruiker is succesvol toegevoegd!");
			} else {
				lblError.setText("Tekstvakken mogen niet leeg zijn");
			}

		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}
	}

	void tvGebruikers() {
		tvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {

			@Override
			public void changed(ObservableValue<? extends Gebruiker> observable, Gebruiker oldValue,
					Gebruiker newValue) {
				if (newValue == null)
					return;
				lblTitle.setText("Gebruiker aanpassen");
				btnVoegToe.setVisible(false);
				btnPasAan.setVisible(true);
				btnVerwijder.setVisible(true);

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
		tvGebruikers.setItems(dc.geefGebruikersSorted());
		try {
			colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
			colEmail.setCellValueFactory(cel -> cel.getValue().getEmailProperty());
			colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
			colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void statusChangeListener() {
		cbxFilter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeFilter();
			}
		});

	}

	void typeChangeListener() {
		cbxFilterType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeFilter();
			}
		});

	}

	private void changeFilter() {
		tvGebruikers.getSelectionModel().clearSelection();
		String filter = txtZoek.getText();
		String status = cbxFilter.getValue().toString();
		String type = cbxFilterType.getValue().toString();

		System.out.println("filter: " + filter);
		System.out.println("status: " + status);
		System.out.println("type: " + type);
		dc.changeFilter(filter, type, status);
	}

	@FXML
	void zoek(ActionEvent event) {
		try {
			changeFilter();

		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}

	}

}
