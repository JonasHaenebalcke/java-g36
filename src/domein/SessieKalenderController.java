package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class SessieKalenderController {

	private List<SessieKalender> sessieKalenderList;
	private ObservableList<SessieKalender> sessieKalenderObservableList;
	private GenericDao sessieKalenderRepo;
	private SessieKalender huidigeSessieKalender;

	public SessieKalenderController() {

		setSessieRepo(new GenericDaoJpa(SessieKalender.class));

		for (SessieKalender sk : geefSessieKalenderList()) {
			if (sk.getStartDatum().isBefore(LocalDate.now()) && sk.getEindDatum().isAfter(LocalDate.now())) {
				this.huidigeSessieKalender = sk;
//				break;
			}
			sk.setStringProperties();
		}
	}

	public void setSessieRepo(GenericDaoJpa mock) {
		this.sessieKalenderRepo = mock;
	}

	public ObservableList<SessieKalender> geefSessieKalenderObservableList() {
		if (sessieKalenderObservableList == null) {
			sessieKalenderObservableList = FXCollections.observableArrayList(geefSessieKalenderList());
		}
		return sessieKalenderObservableList;
	}

	public Collection<SessieKalender> geefSessieKalenderList() {
		if (sessieKalenderList == null) {
			sessieKalenderList = sessieKalenderRepo.findAll();
		}
		return sessieKalenderList;
	}

	public SessieKalender geefSessieKalender(boolean volgende) {
		try {
			SessieKalender sessieKalender;
			if (volgende) {
				sessieKalender = geefSessieKalenderList().stream()
						.filter(s -> s.getStartDatum().getYear() > huidigeSessieKalender.getStartDatum().getYear())
						.sorted(Comparator.comparing(SessieKalender::getStartDatum)).findFirst().get();
			} else {
				sessieKalender = geefSessieKalenderList().stream()
						.filter(s -> s.getStartDatum().getYear() < huidigeSessieKalender.getStartDatum().getYear())
						.sorted(Comparator.comparing(SessieKalender::getStartDatum).reversed()).findFirst().get();
			}

			huidigeSessieKalender = sessieKalender;
			return sessieKalender;
		} catch (Exception e) {
			System.err.println("Er ging iets fout bij het teruggeven van de sessiekalender.");
			throw new EntityNotFoundException("Er ging iets fout bij het teruggeven van de sessiekalender.");
		}
	}

	public void wijzigSessieKalender(int id, LocalDate startDate, LocalDate eindDate) {
//      try {

		if ((LocalDate.now().getYear() == startDate.getYear() && LocalDate.now().getMonthValue() >= 9)
				|| (LocalDate.now().getYear() == eindDate.getYear() && LocalDate.now().getMonthValue() < 9))
			throw new IllegalArgumentException("Jaar van huidig sessiekalender mag niet worden aangepast");

		for (SessieKalender sessieKalender : sessieKalenderList) {
			if (sessieKalender.getSessieKalenderID() == id) {
				sessieKalender.wijzigSessieKalender(startDate, eindDate);
//				huidigeSessieKalender = sessieKalender;
				sessieKalenderObservableList.stream().filter(s -> s.getSessieKalenderID() == id).findFirst().get()
						.wijzigSessieKalender(startDate, eindDate);
				GenericDaoJpa.startTransaction();
				sessieKalenderRepo.update(sessieKalender);
				GenericDaoJpa.commitTransaction();
//					setStringProperties();
			}
		}
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
	}

	public void voegToeSessieKalender(LocalDate startDate, LocalDate eindDate) {
//		try {
		if (geefSessieKalenderList().stream().map(SessieKalender::getStartDatum).map(LocalDate::getYear)
				.collect(Collectors.toList()).contains(startDate.getYear())
				|| geefSessieKalenderList().stream().map(SessieKalender::getEindDatum).map(LocalDate::getYear)
						.collect(Collectors.toList()).contains(eindDate.getYear()))
			throw new IllegalArgumentException("Deze sessiekalender bestaat al!");

		SessieKalender sessieKalender = new SessieKalender(startDate, eindDate);

		sessieKalenderObservableList.add(sessieKalender);
		sessieKalenderList.add(sessieKalender);
		huidigeSessieKalender = sessieKalender;
		GenericDaoJpa.startTransaction();
		sessieKalenderRepo.insert(sessieKalender);
		GenericDaoJpa.commitTransaction();
//			setStringProperties();
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
	}

	public void voegSessieToe(int index, Gebruiker verantwoordelijke, LocalDateTime startDatum, LocalDateTime eindDatum,
			String titel, String lokaal, int capaciteit, String omschrijving, String gastSpreker) {
		Sessie sessie = new Sessie(verantwoordelijke, titel, lokaal, startDatum, eindDatum, capaciteit, omschrijving,
				gastSpreker);
		GenericDaoJpa.startTransaction();
		huidigeSessieKalender.voegSessieToe(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public void verwijderSessie(int index, Sessie sessie) {
		GenericDaoJpa.startTransaction();
		huidigeSessieKalender.verwijderSessie(sessie);
		GenericDaoJpa.commitTransaction();
	}

//	public void verwijderSessieKalender(SessieKalender sessieKalender) {
//		try {
//		sessieKalenderList.remove(sessieKalender);
//		GenericDaoJpa.startTransaction();
//		sessieKalenderRepo.delete(sessieKalender);
//		GenericDaoJpa.commitTransaction();
//		} catch(Exception e) {
//			System.err.println(e.getMessage());
//		}
//	}

	public ObservableList<Sessie> geefSessiesMaand(Number maand) {
		try {
			
		return FXCollections.observableArrayList(huidigeSessieKalender.geefSessiesMaand(maand));
		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public SessieKalender getHuidigeSessieKalender() {
		return huidigeSessieKalender;
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
