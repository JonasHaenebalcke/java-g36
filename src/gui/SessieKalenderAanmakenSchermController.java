package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import domein.SessieKalenderController;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

public class SessieKalenderAanmakenSchermController extends AnchorPane{
	@FXML
	private DatePicker inputStartDatum;
	@FXML
	private DatePicker inputEindDatum;
	@FXML
	private Button btnVoegSessieKalenderToe;
	@FXML
	private Label lblErrorVoegSessieKalenderToe;
	

	private SessieKalenderController dc;
	
	
	public SessieKalenderAanmakenSchermController(SessieKalenderController dc) {
		try 
		{
			this.dc = dc;
			System.out.println("Laad nieuw voeg sessie kalender toe scherm");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("SessieKalenderAanmaken.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	// Event Listener on Button[#btnVoegSessieKalenderToe].onAction
	@FXML
	public void voegSessieKalenderToe(ActionEvent event) {
		try {
			System.out.println("Voeg sessie Kalender Toe!");
			System.out.println(inputStartDatum.getValue().toString());
			System.out.println(inputEindDatum.getValue().toString());
			if (inputStartDatum.getValue() == null || inputEindDatum.getValue() == null)
				throw new IllegalArgumentException("Datum is verplicht in te vullen!");

			dc.voegToeSessieKalender(inputStartDatum.getValue(), inputEindDatum.getValue());
		} catch (Exception e) {
			lblErrorVoegSessieKalenderToe.setVisible(true);
			lblErrorVoegSessieKalenderToe.setText(e.getMessage());
		}
	}
}
