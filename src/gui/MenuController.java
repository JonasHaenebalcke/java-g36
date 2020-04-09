package gui;

import java.io.IOException;
import domein.GebruikerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;



public class MenuController extends AnchorPane{

	    @FXML
	    private AnchorPane scherm;
	
	    @FXML
	    private Button btnGebruikersBeheren;
        
	    GebruikerController dc;
        public MenuController(GebruikerController dc) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            loader.setRoot(this);
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            this.dc = dc;
        }
        
        public void initialize() {
            
        }

        @FXML
        void gebruikersBeheren(ActionEvent event) throws IOException {
        	/*try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikersScherm.fxml"));
        		scherm = loader.<AnchorPane>load();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}*/
        	Node node;
        	//node = (Node)FXMLLoader.load(getClass().getResource("GebruikersScherm.fxml"));
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikersScherm.fxml"));
        	node = (Node) loader.load();
        	scherm.getChildren().setAll(node);
        }

        @FXML
        void sessiekalenderBeheren(ActionEvent event) {

        }

        @FXML
        void sessiesBeheren(ActionEvent event) {

        }

    }
