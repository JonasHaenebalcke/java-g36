package domein;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GebruikerDao;
import repository.GebruikerDaoJpa;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class GebruikerController {

	private ObservableList<Gebruiker> gebruikerObservableList;
	private List<Gebruiker> gebruikerList;
	private GebruikerDao gebruikerRepo;

	public GebruikerController() {		
		setGebruikerRepo(new GebruikerDaoJpa());
	}

	public void setGebruikerRepo(GebruikerDao mock) {
		gebruikerRepo = mock;
	}
	
	public List<Gebruiker> GeefGebruikersObservableList() {
		if (gebruikerObservableList == null) {
			gebruikerObservableList = FXCollections.observableArrayList(GeefGebruikersList());
		}
		return gebruikerObservableList;
	}

	private List<Gebruiker> GeefGebruikersList() {
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
			Gebruiker gebruiker = new Gebruiker(voornaam, familienaam, mailadres, gebruikersnaam, type, status,
					profielfoto);

			if (GeefGebruikersList().stream().map(Gebruiker::getGebruikersnaam).collect(Collectors.toList())
					.contains(gebruiker.getGebruikersnaam()))
				throw new IllegalArgumentException("Deze gebruiker bestaat al!");

			gebruikerList.add(gebruiker);
			GenericDaoJpa.startTransaction();
			gebruikerRepo.insert(gebruiker);
			GenericDaoJpa.commitTransaction();

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

			gebruikerList.remove(gebruiker);
			GenericDaoJpa.startTransaction();
			gebruikerRepo.delete(gebruiker);
			GenericDaoJpa.commitTransaction();

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
			throw new EntityNotFoundException("Er ging iets fout bij het teruggeven van de gebruiker.");
		}
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}

}