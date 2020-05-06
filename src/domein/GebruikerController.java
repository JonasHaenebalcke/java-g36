package domein;

import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import repository.GebruikerDao;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;
import javafx.collections.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
import java.util.*;

public class GebruikerController {

	private ObservableList<Gebruiker> gebruikerObservableList;
	private List<Gebruiker> gebruikerList;
	private GebruikerDao gebruikerRepo;
	private Gebruiker ingelogdeVerantwoordelijke;

	public GebruikerController() {
		setGebruikerRepo(new GebruikerDaoJpa());
		geefGebruikersList();
	}

	public void setGebruikerRepo(GebruikerDao mock) {
		gebruikerRepo = mock;
	}

	public List<Gebruiker> geefGebruikersList() {
		if (gebruikerList == null) {
			gebruikerList = gebruikerRepo.findAll();
		}
		return gebruikerList;
	}

	public ObservableList<Gebruiker> geefGebruikersObservableList() {
		if (gebruikerObservableList == null) {
			gebruikerObservableList = FXCollections.observableArrayList(geefGebruikersList());
		}
		return gebruikerObservableList;
	}

	public void voegToeGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			TypeGebruiker type, Status status, String profielfoto, String wachtwoord) throws NoSuchAlgorithmException, InvalidKeySpecException {

		Gebruiker gebruiker;
			gebruiker = new Gebruiker(voornaam, familienaam, mailadres, gebruikersnaam, type, status, profielfoto);

		if (geefGebruikersList().stream().map(Gebruiker::getGebruikersnaam).collect(Collectors.toList())
				.contains(gebruiker.getGebruikersnaam()))
			throw new IllegalArgumentException("Deze gebruiker bestaat al!");

		try {
			gebruikerList.add(gebruiker);
			gebruikerObservableList.add(gebruiker);
			GenericDaoJpa.startTransaction();
			gebruikerRepo.insert(gebruiker);
			GenericDaoJpa.commitTransaction();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets mis bij het opslaan van de nieuwe gebruiker");
		}
	}

	public void verwijderGebruiker(int index) {
		try {
			Gebruiker gebruiker = gebruikerList.get(index);

			gebruikerList.remove(gebruiker);
			gebruikerObservableList.remove(gebruiker);
			GenericDaoJpa.startTransaction();
			gebruikerRepo.delete(gebruiker);

			GenericDaoJpa.commitTransaction();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IllegalArgumentException("Er ging iets fout bij het verwijderen van de gebruiker.");
		}
	}

	public Gebruiker geefIngelogdeVerantwoordelijke() {
		return ingelogdeVerantwoordelijke;
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}

	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			TypeGebruiker type, Status status, String profielfoto) {

		for (Gebruiker gebruiker : gebruikerList) {
			if (gebruiker.getGebruikersnaam().equals(gebruikersnaam)) {

//				try {
					GenericDaoJpa.startTransaction();
					gebruiker.wijzigGebruiker(voornaam, familienaam, mailadres, type, status, profielfoto);
					gebruikerRepo.update(gebruiker);
					GenericDaoJpa.commitTransaction();
					break;
//				} catch (Exception e) {
//					System.err.println(e.getMessage());
//					throw new IllegalArgumentException(
//							"Er ging iets fout bij het opslaan van de gewijzigde gebruiker.");
//				}
			}
		}
	}

	public void startTransaction() {
		for (Gebruiker g : gebruikerList) {
			g.fillPersistent();
		}
	}

	public void comittTransaction() {
		for (Gebruiker g : gebruikerList) {
			g.fillTransient();
		}
	}

	public void meldAan(String gebruikersnaam, String wachtwoord)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		boolean flagGebruikersnaam = false;
		boolean flagWachtwoord = false;

		for (Gebruiker g : geefGebruikersList()) {
			if (gebruikersnaam.equalsIgnoreCase(g.getGebruikersnaam())
					|| gebruikersnaam.equalsIgnoreCase(g.getMailadres())) {
				flagGebruikersnaam = true;
				if (g.getType().equals(TypeGebruiker.Gebruiker))
					throw new IllegalArgumentException(
							"Gebruiker is geen (Hoofd)Verantwoordelijke en heeft geen toegang tot de applicatie.");
				if (PasswordHasher.verifyPasswordHash(g.getPasswordHashJava(), wachtwoord)) {
					flagWachtwoord = true;
					ingelogdeVerantwoordelijke = g;
					break;
				}
			}
		}
		if (!flagGebruikersnaam || !flagWachtwoord) {
			throw new IllegalArgumentException("Gebruikersnaam of wachtwoord is incorrect");
		}
	}

}
