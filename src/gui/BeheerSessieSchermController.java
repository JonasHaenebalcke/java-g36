package gui;

import java.io.IOException;

import domein.Sessie;
import domein.SessieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
	
public class BeheerSessieSchermController extends AnchorPane{
	@FXML
    private ListView<Sessie> lvSessies;

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
    private TextField txtVerantwoordelijke;

    @FXML
   private Button btnBeherenIngeschrevenen;

    @FXML
    private TextArea txtOmschrvijving;

    @FXML
	private Label lblError;
    
    public SessieController sc;
    
    
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
		//lvSessies.setItems(sc.geefSessies(statusSessie));
		
	}
	
	@FXML
    void BeherenIngeschrevenen(ActionEvent event) {
		
	}
}


