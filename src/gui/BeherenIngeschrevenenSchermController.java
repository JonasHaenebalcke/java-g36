package gui;

import java.io.IOException;
import java.time.LocalDate;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.GebruikerSessie;
import domein.SessieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class BeherenIngeschrevenenSchermController extends AnchorPane{

	@FXML
    private Button btnTerug;

    @FXML
    private TableView<GebruikerSessie> tvIngeschrevenen;
    @FXML
    private TableColumn<Gebruiker, String> tcVoornaam;
    @FXML
    private TableColumn<Gebruiker, String> tcFamilienaam;
    @FXML
    private TableColumn<Gebruiker, String> tcGebruikersnaam;
    @FXML
    private TableColumn<Gebruiker, LocalDate> tcInschrijvingsdatum;
    @FXML
    private TableColumn<Gebruiker, Boolean> tcAanwezig;

    @FXML
    private Button btnVoegToe;

    @FXML
    private Button btnVerwijder;
    
    @FXML
    private TextField txtGebruiker;

    @FXML
    private Button btnZoekGebruiker;

    @FXML
    private Label lblError;
    
    private SessieController sc; 
    private GebruikerController gc; 
    
    public BeherenIngeschrevenenSchermController() {
    	this.sc = new SessieController();
    }
    
    public BeherenIngeschrevenenSchermController(SessieController sc) {
    	this.sc = sc;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("BeheerIngeschrevenen.fxml"));
    	loader.setRoot(this);
		loader.setController(this);
		try {	
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
    //	tvIngeschrevenen.setItems(sc.geefGebruikerSessiesObservable());
//    	tcVoornaam.setCellValueFactory(cel -> cel.getValue());
//    	tcFamilienaam.setCellValueFactory(cel -> cel.getValue());
//    	tcGebruikersnaam.setCellValueFactory(cel -> cel.getValue());
//    	tcInschrijvingsdatum.setCellValueFactory(arg0);
//    	tcAanwezig.setCellValueFactory(arg0);
    	
//    	// sessie selecteren en in huigedeSessie steken
    	
//    	tcAanwezig.setCellFactory( kolom -> new CheckBoxTableCell<>());
    	
//    	// moet gebruikerSessies zijn, niet alle gebruikers
//    	sc.geefGebruikerSessiesObservable().forEach((GebruikerSessie gs) -> {  // geeft observable van GebruikerSessie mee
//    		tcVoornaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevene().getVoornaam()));
//			tcFamilienaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevene().getFamilienaam()));
//			tcGebruikersnaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevene().getGebruikersnaam()));
//			//tcInschrijvingsdatum.setCellValueFactory();
			
		/*	tcAanwezig.setCellValueFactory(CheckBoxTableCell.forTableColumn(new Callback<>(){

    	})); */ 
	
    //	});
    }
    @FXML
    void trgBeherenSessies(ActionEvent event) {
    	// gaat naar scherm Beheren sessies
    }

    @FXML
    void verwijderIngeschrevenen(ActionEvent event) {
    	GebruikerSessie gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
    	sc.wijzigIngeschrevenen(gebruiker.getIngeschrevene(), false, false);  	
    }

    @FXML
    void voegIngeschrevenenToe(ActionEvent event) {
    	// gaat naar scherm alleGebruikers
    	
    }

    @FXML
    void zoekGebruiker(ActionEvent event) {

    }
}
