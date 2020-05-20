package gui;

import java.io.IOException;

import domein.AankondigingController;
import domein.GebruikerController;
import domein.SessieController;
import domein.SessieKalenderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MenuController extends BorderPane {

	@FXML
	private GridPane scherm;

	@FXML
	private Button btnGebruikersBeheren;

	@FXML
	private Label lblAangemeldAls;

	@FXML
	private Button btnLogUit;

	public GebruikerController dc;
	public SessieKalenderController skc;
	public SessieController sc;
	public AankondigingController ac;

	public MenuController(GebruikerController dc, SessieKalenderController skc, SessieController sc,
			AankondigingController ac) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
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
		lblAangemeldAls.setText("Aangemeld als \n" + dc.getIngelogdeVerantwoordelijke().getVoornaam() + " "
				+ dc.getIngelogdeVerantwoordelijke().getFamilienaam());
	}

	@FXML
	void gebruikersBeheren(ActionEvent event) throws IOException {
		/*
		 * try { FXMLLoader loader = new
		 * FXMLLoader(getClass().getResource("GebruikersScherm.fxml")); scherm =
		 * loader.<AnchorPane>load(); } catch (IOException ex) { throw new
		 * RuntimeException(ex); }
		 */
		Node node;
		// node =
		// (Node)FXMLLoader.load(getClass().getResource("GebruikersScherm.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikersScherm.fxml"));
		node = (Node) loader.load();
		scherm.getChildren().setAll(node);
	}

	@FXML
	void sessiekalenderBeheren(ActionEvent event) throws IOException {
		SessieKalenderSchermController skcScherm = new SessieKalenderSchermController(this.skc, this.sc, this.dc,
				this.ac);
		scherm.getChildren().setAll(skcScherm);
	}

	@FXML
	void sessiesBeheren(ActionEvent event) throws IOException {
		BeheerSessieSchermController bSessieScherm = new BeheerSessieSchermController(this.sc, this.dc, this.ac);
		scherm.getChildren().setAll(bSessieScherm);
	}

	@FXML
	void IngeschrevenenBeheren(ActionEvent event) throws IOException {
		BeherenIngeschrevenenSchermController bIngeschrevenenScherm = new BeherenIngeschrevenenSchermController(this.sc,
				this.dc, this.ac);
		scherm.getChildren().setAll(bIngeschrevenenScherm);
	}

	@FXML
	void statistieken(ActionEvent event) throws IOException {
		StatistiekenSchermController statiestiekenScherm = new StatistiekenSchermController(this.dc, this.sc);
		scherm.getChildren().setAll(statiestiekenScherm);
	}

	@FXML
	void aankondigingen(ActionEvent event) throws IOException {
		AankondigingenSchermController aankondigingenScherm = new AankondigingenSchermController(this.sc, this.ac,
				this.dc);
		scherm.getChildren().setAll(aankondigingenScherm);
	}

	@FXML
	void logUIt(ActionEvent event) {
		MeldAanSchermController root = new MeldAanSchermController(dc, skc, sc, ac);
		this.getScene().setRoot(root);
//		this.getChildren().setAll(root);
	}
}