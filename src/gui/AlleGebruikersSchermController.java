package gui;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.SessieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class AlleGebruikersSchermController extends AnchorPane{

    @FXML
    private TableView<Gebruiker> tvAlleGebruikers;

    @FXML
    private TableColumn<Gebruiker,String> tcVoornaam;

    @FXML
    private TableColumn<Gebruiker,String> tcFamilienaam;

    @FXML
    private TableColumn<Gebruiker,String> tcGebruikersnaam;

    @FXML
    private TableColumn<Gebruiker,String> tcmailadres;

    @FXML
    private TextField txtGebruiker;

    @FXML
    private Button btnZoekGebruiker;

    @FXML
    private Button btnTerug;

    @FXML
    private Button btnSchrijfIn;

    @FXML
    private Button btnZetAanwezig;
    
    private SessieController sc;
    private GebruikerController gc; 
    
    public AlleGebruikersSchermController() {
		this.sc = new SessieController();
		//tvAlleGebruikers.setItems(gc.geefGebruikersObservableList());
		
		gc.geefGebruikersObservableList().forEach((Gebruiker g) -> {
			tcVoornaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( g.getVoornaam()));
			tcFamilienaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( g.getFamilienaam()));
			tcGebruikersnaam.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( g.getGebruikersnaam()));
			tcmailadres.setCellValueFactory(new PropertyValueFactory<Gebruiker, String>( g.getMailadres()));
			
		});
	}
    
    @FXML
    void schrijfGebruikerIn(ActionEvent event) {
    	Gebruiker gebruiker = tvAlleGebruikers.getSelectionModel().getSelectedItem();
    	sc.wijzigIngeschrevenen(gebruiker, true, false);
    }

    @FXML
    void trgBeherenIngeschrevenen(ActionEvent event) {
    	// gaat trg naar scherm beheren alle ingeschrevenen
    }

    @FXML
    void zetGebruikerAanwezig(ActionEvent event) {
    	Gebruiker gebruiker = tvAlleGebruikers.getSelectionModel().getSelectedItem();
    	sc.wijzigIngeschrevenen(gebruiker, true, true);
    	
    }

    @FXML
    void zoekGebruiker(ActionEvent event) {
    	String gebruikersnaam = txtGebruiker.getText();
    	tvAlleGebruikers.getItems().stream()
        .filter(item -> item.getGebruikersnaam() == gebruikersnaam)
        .findAny()
        .ifPresent(item -> {
            tvAlleGebruikers.getSelectionModel().select(item);
            tvAlleGebruikers.scrollTo(item);
        });
    }
}
