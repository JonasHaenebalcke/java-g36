package gui;

import java.io.IOException;
import java.time.LocalDate;
import domein.Gebruiker;
import domein.GebruikerController;
import domein.GebruikerSessie;
import domein.Sessie;
import domein.SessieController;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class BeherenIngeschrevenenSchermController extends AnchorPane{

	@FXML
    private Button btnTerug;

    @FXML
    private TableView<Gebruiker> tvIngeschrevenen;

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

    private SessieController sc; 
    private GebruikerController gc; 
    
    public BeherenIngeschrevenenSchermController() {
    	this.sc = new SessieController();
    }
    
    public BeherenIngeschrevenenSchermController(SessieController sc) {
    	this.sc = sc;
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("BeheerIngeschrevenen.fxml"));
    	
    	//loader.setRoot(this);
		loader.setController(this);
		try {	
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
    	
    	
    	// sessie selecteren en in huigedeSessie steken
    	tcAanwezig.setCellFactory( kolom -> new CheckBoxTableCell<>());
//    	sc.geefGebruikerSessiesObservable().forEach((GebruikerSessie gs) -> {
//    		tcVoornaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevenen().getVoornaam()));
//			tcFamilienaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevenen().getFamilienaam()));
//			tcGebruikersnaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( gs.getIngeschrevenen().getGebruikersnaam()));
//			//tcInschrijvingsdatum.setCellValueFactory();
			
		/*	tcAanwezig.setCellValueFactory(CheckBoxTableCell.forTableColumn(new Callback<>(){

    	})); */ 
		// geeft observable van GebruikerSessie mee
    	// moet gebruikerSessies zijn
    	
    //	});
    }
    @FXML
    void trgBeherenSessies(ActionEvent event) {
    	// gaat naar scherm Beheren sessies
    }

    @FXML
    void verwijderIngeschrevenen(ActionEvent event) {
    	Gebruiker gebruiker = tvIngeschrevenen.getSelectionModel().getSelectedItem();
    	sc.wijzigIngeschrevenen(gebruiker, false, false);  	
    }

    @FXML
    void voegIngeschrevenenToe(ActionEvent event) {
    	// gaat naar scherm alleGebruikers
    	
    }
}
