package gui;

import domein.Gebruiker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    

}
