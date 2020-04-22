package gui;

import java.io.IOException;
import domein.Gebruiker;
import domein.Sessie;
import domein.SessieController;
import domein.StatusSessie;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
	
public class BeheerSessieSchermController extends AnchorPane{
	@FXML
    private ListView<Sessie> lvSessies;
	
    @FXML
    private ComboBox<StatusSessie> cbxStatusSessie;
	
    @FXML
    private ListView<?/*Media*/> lvMedia;

    @FXML
    private TextField txtGastspreker;

    @FXML
    private TextField txtTitel;

    @FXML
    private DatePicker dpEinddatum;

    @FXML
    private DatePicker dpStartdatum;

    @FXML
    private TextField txtCapaciteit;

    @FXML
    private TextField txtStartuur;

    @FXML
    private TextField txtEinduur;

    @FXML
    private TextField txtLokaal;

    @FXML
    private Button btnPasAan;

    @FXML
    private Button btnVoegToe;

    @FXML
    private Button btnVerwijder;
    
    @FXML
    private ListView<?/*Feedback*/> lvFeedback;

    @FXML
    private ComboBox<Gebruiker> cbxVerantwoordelijke;

    @FXML
   private Button btnBeherenIngeschrevenen;

    @FXML
    private TextArea txtOmschrvijving;

    @FXML
	private Label lblError;
    
    @FXML
    private Label lblMedia;

    @FXML
    private Label lblFeedback;
    
    @FXML
    private ScrollBar sbFeedback;
    
    private SessieController sc;
    private Sessie sessie;
    
    public BeheerSessieSchermController() {
    	this.sc = new SessieController();
    }

	public BeheerSessieSchermController(SessieController sc)  {
		this.sc = sc;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Sessie.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {	
			loader.load();

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		initialize();
	}
	
	private void initialize() {
	//	cbxStatusSessie.setItems(FXCollections.observableArrayList(StatusSessie.values()));
	//  lvSessies.setItems(sc.geefSessiesObservable(StatusSessie.nietOpen));
		lblFeedback.setVisible(false);
		lvFeedback.setVisible(false);
		sbFeedback.setVisible(false);
		textWaardeSessieInvullen();
		
	}
	
	void textWaardeSessieInvullen() {
		lvSessies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sessie>() {

			@Override
			public void changed(ObservableValue<? extends Sessie> sessieObs, Sessie oldV, Sessie newV) {
				
				txtTitel.setText(newV.getTitel());
				txtGastspreker.setText(newV.getGastspreker());
				cbxVerantwoordelijke.setValue(sessieObs.getValue().getVerantwoordelijke());// (newV.getVerantwoordelijke().getVoornaam()+ ' ' + sessie.getVerantwoordelijke().getFamilienaam());
				txtCapaciteit.setText(Integer.toString(newV.getCapaciteit()));
				txtLokaal.setText(newV.getLokaal());
				txtOmschrvijving.setText(newV.getOmschrijving());
				//lvFeedback.setItems();
				//lvMedia.setItems();
			}
		
		});
		
	}

    @FXML
    void geefSessiesGekozenStatus(ActionEvent event) {
    	lvSessies.setItems(sc.geefSessiesObservable((cbxStatusSessie.getValue())));
    	if(cbxStatusSessie.getValue() != StatusSessie.nietOpen) {
    		lblFeedback.setVisible(true);
    		lvFeedback.setVisible(true);
    	}
    	
    }
	@FXML
    void beherenIngeschrevenen(ActionEvent event) {
		//Sessie sessie = lvSessies.getSelectionModel().getSelectedItem();
		//sc.setHuidigeSessie(sessie);

		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(sc);
		this.getChildren().add(bIngeschrevenenScherm);
	}

    @FXML
    void pasSessieAan(ActionEvent event) {
    	cbxVerantwoordelijke.setDisable(true);
    	try {
    		if(!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank()
    				&& (dpStartdatum.getValue() != null) && (dpEinddatum.getValue() != null)  && !txtStartuur.getText().isBlank() && !txtEinduur.getText().isBlank() 
    				&& !txtCapaciteit.getText().isBlank()) {
    			
    			sc.wijzigSessie(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(), 
    					dpStartdatum.getValue(), dpEinddatum.getValue(), Integer.parseInt(txtCapaciteit.getText()), 
    					txtOmschrvijving.getText(), txtGastspreker.getText() );
    		}else {
    			lblError.setText("Tekstvakken mogen niet leeg zijn");	
    		}
    	}
    	catch(Exception e) {
			lblError.setText(e.getMessage());
    	}
    }

    @FXML
    void verwijderSessie(ActionEvent event) {
    	try {
    		Sessie sessie = lvSessies.getSelectionModel().getSelectedItem();
    		sc.verwijderSessie(sessie);
    	
    	} catch (Exception e) {
			lblError.setText(e.getMessage());
			System.out.print(e.getMessage());
    	}
    }

    @FXML
    void voegSessieToe(ActionEvent event) {
    	try {
    		if(!txtTitel.getText().isBlank() && !txtLokaal.getText().isBlank()
    				&& (dpStartdatum.getValue() != null) && (dpEinddatum.getValue() != null)  && !txtStartuur.getText().isBlank() && !txtEinduur.getText().isBlank() 
    				&& !txtCapaciteit.getText().isBlank()) {
    			sc.voegSessieToe(cbxVerantwoordelijke.getValue(), txtTitel.getText(), txtLokaal.getText(), 
    					dpStartdatum.getValue(), dpEinddatum.getValue(),Integer.parseInt(txtCapaciteit.getText()), txtOmschrvijving.getText(), 
    					 txtGastspreker.getText());
    		
    			lvSessies.getSelectionModel().selectLast();
    		} else {
    			lblError.setText("Tekstvakken mogen niet leeg zijn");
    		}
    	}catch (Exception e) {
			lblError.setText(e.getMessage());
			}
    }
    
	// regex voor start-en einduur ^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$
}


