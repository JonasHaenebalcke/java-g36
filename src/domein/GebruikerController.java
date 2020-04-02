package domein;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import repository.GenericDao;
import repository.GenericDaoJpa;

public class GebruikerController {

	private FXCollections.observableArrayList<Gebruiker> gebruikerList;
	private GenericDao<Gebruiker> gebruikerRepo;

	public GebruikerController() {
		setGebruikerRepo(new GenericDaoJpa<>(Gebruiker.class));
	}

	public void setGebruikerRepo(GenericDao<Gebruiker> mock) {
		gebruikerRepo = mock;
	}

	public List<Gebruiker> toonAlleGebruikers() {
		if (gebruikerList == null) {
			gebruikerList = gebruikerRepo.findAll();
		}
		return gebruikerList;
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 */
	public void voegToeGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			Type type, Status status, String profielfoto) {
		try {
			Gebruiker gebruiker = new Gebruiker(voornaam, familienaam, mailadres, gebruikersnaam, type,
					status, profielfoto);
			if (gebruiker == null)
				throw new NullPointerException("Er ging iets mis bij het toevoegen van de gebruiker.");
			gebruikerList.add(gebruiker);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param id
	 */
	public void verwijderGebruiker(int index) {
		try {
			Gebruiker gebruiker = gebruikerList.get(index);
			gebruikerList.remove();
		} catch (Exception e) {
			System.err.println("Er ging iets fout bij het verwijderen van de gebruiker.");
		}
	}

	/**
	 * 
	 * @param id
	 */
	public Gebruiker geefGebruiker(int index) {
		try {
			Gebruiker gebruiker = gebruikerList.get(index);
			return gebruiker;
		} catch (Exception e) {
			System.err.println("Er ging iets fout bij het teruggeven van de gebruiker.");
		}
	}

}