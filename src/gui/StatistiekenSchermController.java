package gui;
import java.io.IOException;

import domein.Gebruiker;
import domein.GebruikerController;
import domein.Sessie;
import domein.SessieController;
import domein.SessieKalenderController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.ReadOnlyStringWrapper;

public class StatistiekenSchermController extends AnchorPane{
	
	@FXML
    private TableView<Gebruiker> tvGebruikers;
	
	@FXML
    private TableView<Sessie> tvSessies;

    @FXML
    private TableColumn<Gebruiker, String> colNaam;

    @FXML
    private TableColumn<Gebruiker, String> colVoornaam;

    @FXML
    private TableColumn<Gebruiker, String> colType;

    @FXML
    private TableColumn<Gebruiker, String> colStatus;

    @FXML
    private TableColumn<Gebruiker, String> colAantalFeedbacks;

    @FXML
    private Label lblStatistiek1;

    @FXML
    private Label lblStatistiek2;

    @FXML
    private Label lblStatistiek1Value;

    @FXML
    private Label lblStatistiek2Value;

    @FXML
    private TableColumn<Sessie, String> colVerantwoordelijke;

    @FXML
    private TableColumn<Sessie, String> colTitel;

    @FXML
    private TableColumn<Sessie, String> colStart;

    @FXML
    private TableColumn<Sessie, String> colEind;

    @FXML
    private TableColumn<Sessie, String> colAantalDeelnemers;
	    
	    private GebruikerController dc;
	    private SessieController sc;
	    public StatistiekenSchermController(GebruikerController dc, SessieController sc) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StatistiekenScherm.fxml"));
			loader.setRoot(this);
			loader.setController(this);

			try {
				loader.load();

			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			this.dc = dc;
			this.sc = sc;
			initialize();
		}

	    private void initialize() {
	    	addListenerToTableGebruikers();
	    	addListenerToTableSessies();
	    	
	    	colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
	    	
			tvSessies.setVisible(false);
			tvGebruikers.setVisible(true);
			tvGebruikers.setItems(dc.geefGebruikersObservableList());
			colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
			colType.setCellValueFactory(cel ->cel.getValue().getTypeProperty());
			colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
//			colAantalFeedbacks.setCellValueFactory(cel -> );
		}

		@FXML
	    void geefStatistiekenGebruikers(ActionEvent event) {
			tvSessies.setVisible(false);
			tvGebruikers.setVisible(true);
			tvGebruikers.setItems(dc.geefGebruikersObservableList());
			colNaam.setCellValueFactory(cel -> cel.getValue().getNaamProperty());
			colVoornaam.setCellValueFactory(cel -> cel.getValue().getVoorNaamProperty());
			colType.setCellValueFactory(cel -> cel.getValue().getTypeProperty());
			colStatus.setCellValueFactory(cel -> cel.getValue().getStatusProperty());
//			colAantalFeedbacks.setCellValueFactory(cel -> new ReadOnlyStringWrapper("4"));
			
			
	    }

	    @FXML
	    void geefStatistiekenSessies(ActionEvent event) {
	    	tvGebruikers.setVisible(false);
	    	tvSessies.setVisible(true);
	    	tvSessies.setItems(sc.geefSessiesObservable());
	    	colVerantwoordelijke.setCellValueFactory(cel -> cel.getValue().getNaamVerantwoordelijke());
	    	colTitel.setCellValueFactory(cel -> cel.getValue().getTitelSessieProperty());
	    	colStart.setCellValueFactory(cel -> cel.getValue().getStartDatumSessieProperty());
	    	colEind.setCellValueFactory(cel -> cel.getValue().getEindDatumSessieProperty());
//	    	colAantalDeelnemers.setCellValueFactory(cel -> new ReadOnlyStringWrapper("28"));
	    }
	    
	    void addListenerToTableGebruikers() {
			tvGebruikers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gebruiker>() {

				@Override
				public void changed(ObservableValue<? extends Gebruiker> observable, Gebruiker oldValue,
						Gebruiker newValue) {
					lblStatistiek1.setText("Aantal maal aanwezig");
					lblStatistiek1Value.setText("3");
					lblStatistiek2.setText("Aantal maal afwezig");
					lblStatistiek2Value.setText("1");
				}
			});

		}
	    
	    void addListenerToTableSessies() {
			tvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

				@Override
				public void changed(ObservableValue<? extends Sessie> observable, Sessie oldValue,
						Sessie newValue) {
					lblStatistiek1.setText("Aantal aanwezigen");
					lblStatistiek1Value.setText("28");
					lblStatistiek2.setText("Gemiddelde score");
					lblStatistiek2Value.setText("3");
				}
			});

		}

//	    @FXML 
//	    void slaEersteStatiestiekOp(ActionEvent event) {
//
//	    }
//	    
//	    @FXML
//	    void SlaTweedeStatistiekOp(ActionEvent event) {
//
//	    }

	

}
