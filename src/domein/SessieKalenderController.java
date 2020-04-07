package domein;

import java.time.LocalDate;
import java.util.*;

import javafx.collections.ObservableList;
import repository.SessieKalenderDao;

public class SessieKalenderController {

	private Collection<SessieKalender> sessieKalenderList;
	private ObservableList<SessieKalender> sessieKalenderObservableList;
	private SessieKalenderDao sessieKalenderRepo;

	public SessieKalenderDao getSessieKalenderRepo() {
		return this.sessieKalenderRepo;
	}

	public void setSessieRepo(SessieKalenderDao value) {
		this.sessieKalenderRepo = value;
	}

	public void wijzigSessieKalender(LocalDate startDate, LocalDate eindDate) {
		throw new UnsupportedOperationException();
	}
	public void voegToeSessieKalender(LocalDate startDate, LocalDate eindDate) {
		throw new UnsupportedOperationException();
	}

	public void voegSessieToe(Gebruiker verantwoordelijke, LocalDate startDatum, LocalDate eindDatum, String titel,
			String lokaal, int capaciteit, StatusSessie statusSessie, String omschrijving, String gastSpreker) {
	}

	public void verwijderSessie(Sessie sessie) {
	}
}
