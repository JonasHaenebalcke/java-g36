package domein;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class SessieKalenderController {

	private List<SessieKalender> sessieKalenderList;
	private ObservableList<SessieKalender> sessieKalenderObservableList;
	private SessieKalenderDao sessieKalenderRepo;
	private SessieKalender huidigeSessieKalender;
	private SessieController sc;

	/*
	 * public SessieKalenderDao getSessieKalenderRepo() { return
	 * this.sessieKalenderRepo; }
	 */

	public SessieKalenderController(SessieController sc) {
		this.sc = sc;
		
		setSessieRepo(new SessieKalenderDaoJpa());

		for (SessieKalender sk : geefSessieKalenderList()) {
			if (sk.getStartDate().isBefore(LocalDate.now()) && sk.getEindDate().isAfter(LocalDate.now())) {
				this.huidigeSessieKalender = sk;
				break;
			}
		}
		setStringProperties();
	}
	
	private void setStringProperties() {
		for (SessieKalender sessieKalender : geefSessieKalenderList()) {
			sessieKalender.setStringProperties(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		}
	}

	public void setSessieRepo(SessieKalenderDao mock) {
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
						.filter(s -> s.getStartDate().getYear() > huidigeSessieKalender.getStartDate().getYear())
						.sorted(Comparator.comparing(SessieKalender::getStartDate)).findFirst().get();
			} else {
				sessieKalender = geefSessieKalenderList().stream()
						.filter(s -> s.getStartDate().getYear() < huidigeSessieKalender.getStartDate().getYear())
						.sorted(Comparator.comparing(SessieKalender::getStartDate).reversed()).findFirst().get();
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
      
      if((LocalDate.now().getYear() == startDate.getYear() && LocalDate.now().getMonthValue() >= 9) || 
              (LocalDate.now().getYear() == eindDate.getYear() && LocalDate.now().getMonthValue() < 9))
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
					setStringProperties();
				}
			}
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
	}

	public void voegToeSessieKalender(LocalDate startDate, LocalDate eindDate) {
//		try {
			if (geefSessieKalenderList().stream().map(SessieKalender::getStartDate).map(LocalDate::getYear).collect(Collectors.toList())
					.contains(startDate.getYear())
					|| geefSessieKalenderList().stream().map(SessieKalender::getEindDate).map(LocalDate::getYear).collect(Collectors.toList())
							.contains(eindDate.getYear()))
				throw new IllegalArgumentException("Deze sessiekalender bestaat al!");

				SessieKalender sessieKalender = new SessieKalender(startDate, eindDate);

			sessieKalenderObservableList.add(sessieKalender);
			sessieKalenderList.add(sessieKalender);
			huidigeSessieKalender = sessieKalender;
			GenericDaoJpa.startTransaction();
			sessieKalenderRepo.insert(sessieKalender);
			GenericDaoJpa.commitTransaction();
			setStringProperties();
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
	}

	public void voegSessieToe(int index, Gebruiker verantwoordelijke, LocalDate startDatum, LocalDate eindDatum,
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

	public ObservableList<Sessie> geefSessiesMaand(int maand) {
		return FXCollections.observableArrayList(huidigeSessieKalender.geefSessiesMaand(maand));// maand.ordinal() +
																								// 1));
	}

	public SessieKalender getHuidigeSessieKalender() {
		return huidigeSessieKalender;
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
