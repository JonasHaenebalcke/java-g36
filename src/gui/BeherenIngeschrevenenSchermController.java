package gui;

import java.time.LocalDate;
import domein.Gebruiker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

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
}
