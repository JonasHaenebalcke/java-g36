package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import domein.AankondigingController;
import domein.GebruikerController;
import domein.SessieController;
import domein.SessieKalenderController;
import javafx.event.ActionEvent;

public class MeldAanSchermController extends BorderPane {
	@FXML
	private Button btnMeldAan;
	@FXML
	private TextField inputGebruikersnaam;
	@FXML
	private PasswordField inputWachtwoord;
	@FXML
	private Label lblError;

	public GebruikerController dc;
	public SessieKalenderController skc;
	public SessieController sc;
	public AankondigingController ac;

	public MeldAanSchermController(GebruikerController dc, SessieKalenderController skc, SessieController sc, AankondigingController ac) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MeldAanScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		this.dc = dc;
		this.skc = skc;
		this.sc = sc;
		this.ac = ac;
	}

	// Event Listener on Button[#btnMeldAan].onAction
	@FXML
	public void meldAan(ActionEvent event) throws NoSuchAlgorithmException, InvalidKeySpecException {
		try {
			dc.meldAan(inputGebruikersnaam.getText(), inputWachtwoord.getText());			

			
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(new MenuController(dc, skc, sc, ac), stage.getWidth(), stage.getHeight());
//			Screen screen = Screen.getPrimary();
//			Rectangle2D bounds = screen.getVisualBounds();
//
//			stage.setX(bounds.getMinX());
//			stage.setY(bounds.getMinY());
//			stage.setWidth(bounds.getWidth());
//			stage.setHeight(bounds.getHeight());
			stage.setTitle("IT LAB");
			stage.setScene(scene);
//			stage.setMaximized(true);
//			stage.setMinWidth(1000); DOE IK IN STARTUP
//			stage.setMinHeight(500);
			stage.show();
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
			System.err.println(e.getMessage());
		}
	}
}
