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

	/*
	 * public SessieKalenderDao getSessieKalenderRepo() { return
	 * this.sessieKalenderRepo; }
	 */

	public SessieKalenderController() {
		setSessieRepo(new SessieKalenderDaoJpa());
		
		for (SessieKalender sk : geefSessieKalenderList()) {
			if (sk.getStartDate().isBefore(LocalDate.now()) && sk.getEindDate().isAfter(LocalDate.now())) {
				this.huidigeSessieKalender = sk;
			}
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

	public SessieKalender geefSessieKalender(int index) {
		try {
			SessieKalender sessieKalender = sessieKalenderList.get(index);
			return sessieKalender;
		} catch (Exception e) {
			System.err.println("Er ging iets fout bij het teruggeven van de sessiekalender.");
			throw new EntityNotFoundException("Er ging iets fout bij het teruggeven van de sessiekalender.");
		}
	}

	public void wijzigSessieKalender(int Id, LocalDate startDate, LocalDate eindDate) {
		for (SessieKalender sessieKalender : sessieKalenderList) {
			if (sessieKalender.getSessieKalenderID() == Id) {

				GenericDaoJpa.startTransaction();
				sessieKalender.wijzigSessieKalender(startDate, eindDate);
				sessieKalenderRepo.update(sessieKalender);
				GenericDaoJpa.commitTransaction();
			}
		}
	}

	public void voegToeSessieKalender(LocalDate startDate, LocalDate eindDate) {
		try {
			SessieKalender sessieKalender = new SessieKalender(startDate, eindDate);
			// sessieKalender.setSessieKalenderID(); //random id setten?

			if (geefSessieKalenderList().stream().map(SessieKalender::getSessieKalenderID).collect(Collectors.toList())
					.contains(sessieKalender.getSessieKalenderID()))
				throw new IllegalArgumentException("Deze sessiekalender bestaat al!");

			sessieKalenderList.add(sessieKalender);
			GenericDaoJpa.startTransaction();
			sessieKalenderRepo.insert(sessieKalender);
			GenericDaoJpa.commitTransaction();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void voegSessieToe(int index, Gebruiker verantwoordelijke, LocalDate startDatum, LocalDate eindDatum,
			String titel, String lokaal, int capaciteit, StatusSessie statusSessie, String omschrijving,
			String gastSpreker) {
		Sessie sessie = new Sessie(verantwoordelijke, startDatum, eindDatum, titel, lokaal, capaciteit, statusSessie,
				omschrijving, gastSpreker);
		GenericDaoJpa.startTransaction();
		geefSessieKalender(index).voegSessieToe(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public void verwijderSessie(int index, Sessie sessie) {
		GenericDaoJpa.startTransaction();
		geefSessieKalender(index).verwijderSessie(sessie);
		GenericDaoJpa.commitTransaction();
	}

	public ObservableList<Sessie> geefSessiesVanMaand(Maand maand) {
		return huidigeSessieKalender.geefSessiesMaand(maand.ordinal() + 1);
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}
}
