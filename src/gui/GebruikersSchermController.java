package gui;
import java.io.IOException;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.Status;
import domein.TypeGebruiker;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GebruikersSchermController extends AnchorPane{

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
    private TextField inputWachtwoord;

    private GebruikerController dc;
    
    public GebruikersSchermController() {
    	this.dc = new GebruikerController();
    }
    
    public GebruikersSchermController(GebruikerController dc) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikersScherm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        this.dc = dc;

        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void initialize() {
//    	ObservableList<Gebruiker> items = dc.geefGebruikersObservableList();
//    	 lvGebruikers.setItems(items);
        //dc.GeefGebruikersList().forEach(g -> items.add(g/*g.getFamilienaam()+" "+g.getVoornaam()*/));
        lvGebruikers.setItems(dc.geefGebruikersObservableList());
        
        cbxStatus.setItems( FXCollections.observableArrayList(Status.values()));
        cbxType.setItems( FXCollections.observableArrayList(TypeGebruiker.values()));
        lvGebruikers();
        btnPasAan.setVisible(false);
        btnVerwijder.setVisible(false);
        lblTitle.setText("Voeg gebruiker toe");
    }
    
    @FXML
    void pasGebruikerAan(ActionEvent event) {
    	try {
    		dc.wijzigGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(), inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto");
    	}catch(Exception e) {
			lblError.setText(e.getMessage());
		}
    }

    @FXML
    void verwijderGebruiker(ActionEvent event) {
    	try {
    		int index = lvGebruikers.getSelectionModel().getSelectedIndex();
    		
    		lvGebruikers.getSelectionModel().clearSelection();
    		dc.verwijderGebruiker(index); //is nu null
	    }catch(Exception e) {
			lblError.setText(e.getMessage());
		}
    }
    
    @FXML
    void voegGebruikerToe(ActionEvent event) {
	    	lblTitle.setText("Voeg gebruiker toe");
	    	btnVoegToe.setVisible(true);
	    	btnVerwijder.setVisible(false);
	    	btnPasAan.setVisible(false);
	    
    }
    
    @FXML
    void voegToe(ActionEvent event) {
    	try {
    		dc.voegToeGebruiker(inputVoornaam.getText(), inputNaam.getText(), inputEmail.getText(), inputGebruikersnaam.getText(), cbxType.getValue(), cbxStatus.getValue(), "profielfoto", inputWachtwoord.getText());
    		lvGebruikers.getSelectionModel().selectLast();
    	}catch(Exception e) {
    		lblError.setText(e.getMessage());
    	}
    }
    
    void lvGebruikers() { 
    lvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {

        @Override
        public void changed(ObservableValue<? extends Gebruiker> observable, Gebruiker oldValue, Gebruiker newValue) {
        	lblTitle.setText("Gebruiker aanpassen");
        	btnVoegToe.setVisible(false);
            btnPasAan.setVisible(true);
            btnVerwijder.setVisible(true);
            inputVoornaam.setText(newValue.getVoornaam());
            inputNaam.setText(newValue.getFamilienaam());
            inputEmail.setText(newValue.getMailadres());
            inputGebruikersnaam.setText(newValue.getGebruikersnaam());
            cbxStatus.setValue(newValue.getStatus());
            cbxType.setValue(newValue.getType());
        }
    });
    }
}


