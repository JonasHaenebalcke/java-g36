package domein;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.ObservableList;
import repository.SessieKalenderDao;

public class SessieKalenderController {

	private Collection<SessieKalender> sessieKalenderList;
	private ObservableList<domein.SessieKalender> sessieKalenderObservableList;
	private SessieKalenderDao sessieRepo;

	public SessieKalenderDao getSessieRepo() {
		return this.sessieRepo;
	}

	public void setSessieRepo(SessieKalenderDao value) {
		this.sessieRepo = value;
	}

	public void wijzigSessieKalender(LocalDate startDate, LocalDate eindDate) {
		throw new UnsupportedOperationException();
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, LocalDate startDatum, LocalDate eindDatum, String titel,
			String lokaal, int capaciteit, StatusSessie statusSessie, String omschrijving, String gastSpreker) {
	}

	public void verwijderSessie(Sessie sessie) {
	}
}
