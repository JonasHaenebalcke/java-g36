package domein;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class AankondigingController {

	private List<Aankondiging> aankondigingLijst;
	private ObservableList<Aankondiging> aankondigingObservableList;
	private GenericDao<Aankondiging> aankondigingRepo;
	private Aankondiging gekozenAankondiging;

	public AankondigingController() {
		setAankondigingRepo(new GenericDaoJpa<Aankondiging>(Aankondiging.class));
	}

	public void setAankondigingRepo(GenericDao<Aankondiging> aankondigingMock) {
		this.aankondigingRepo = aankondigingMock;
	}

	public List<Aankondiging> geefAankondigingen() {
		if (aankondigingLijst == null) {
			aankondigingLijst = aankondigingRepo.findAll();

		}
		return aankondigingLijst;
	}

	public ObservableList<Aankondiging> geefAankondigingObservableList() {
		if (aankondigingObservableList == null) {
			aankondigingObservableList = FXCollections.observableArrayList(geefAankondigingen());
		}
		return aankondigingObservableList;
	}

	public void setGekozenAankondiging(Aankondiging aankondiging) {
		this.gekozenAankondiging = aankondiging;
	}

	public void wijzigAankondiding(String titel, String aankondiging, boolean isVerzonden) {
		try {
			GenericDaoJpa.startTransaction();

			gekozenAankondiging.wijzigAankondiging(titel, aankondiging, isVerzonden);
			aankondigingRepo.update(gekozenAankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het wijzigen van de aankondiging");
		}

	}

	public void voegAankondigingToe(String titel, String aankondiging, boolean isVerzonden, Sessie sessie,
			Gebruiker publicist) {
		try {
			Aankondiging newAankondiging = new Aankondiging(titel, aankondiging, sessie, publicist, isVerzonden);
			if (!sessie.isInschrijvingenOpen() ) {
				throw new IllegalArgumentException(
						"Aankondiging kan niet aangemaakt worden want sessie is nog niet open voor inschrijvingen");
			}
//			if( publicist.getStatus().equals(Status.NietActief) 
//					||publicist.getStatus().equals(Status.Geblokkeerd)) {
//				throw new IllegalArgumentException("De publicist is niet actief of geblokeerd");
//			}
			GenericDaoJpa.startTransaction();
			aankondigingLijst.add(newAankondiging);
			aankondigingObservableList.add(newAankondiging);
			aankondigingRepo.insert(newAankondiging);
			GenericDaoJpa.commitTransaction();
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het aanmaken van de aankondiging");
		}
	}

	public void verwijderAankondiging(int index) {
		try {
			Aankondiging aankondiging = aankondigingLijst.get(index);
			if (aankondiging.isVerzonden()) {
				throw new IllegalArgumentException("Aankondiging kan niet verwijderd worden omdat hij al verzonden is");
			}
			aankondigingLijst.remove(aankondiging);
			aankondigingObservableList.remove(aankondiging);
			GenericDaoJpa.startTransaction();
			aankondigingRepo.delete(aankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het verwijderen van een aankondiging");
		}
	}

	public void verzendAankondiging(int index) {
		try {
			Aankondiging aankondiging = aankondigingLijst.get(index);
			if (aankondiging.isVerzonden()) {
				throw new IllegalArgumentException("Je hebt deze aankondiging al verzonden");
			}
//			if( aankondiging.getPublicist().getStatus().equals(Status.NietActief) 
//			||aankondiging.getPublicist().getStatus().equals(Status.Geblokkeerd)) {
//				throw new IllegalArgumentException("De publicist is niet actief of geblokeerd");
//			}
			// nog code voor verzenden van mail
			
			aankondiging.isVerzonden = true;

		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het verzenden van de aankondiging");
		}
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}

}
