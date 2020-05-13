package domein;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import repository.GenericDao;
import repository.GenericDaoJpa;

public class PopulateDB {
	public void run() {
		GenericDao gebruikerdao = new GenericDaoJpa(Gebruiker.class);
		GenericDaoJpa.startTransaction();
		Gebruiker gebruiker1 = null;
		try {
			gebruiker1 = new Gebruiker("Katrien", "Maasens", "katrien.maasen@student.hogent.be", "123456km",
					TypeGebruiker.Hoofdverantwoordelijke, Status.Actief, "");
			gebruikerdao.insert(gebruiker1);
			gebruikerdao.insert(new Gebruiker("Lotte", "koekens", "lotte.koekens@hogent.be", "123478lk",
					TypeGebruiker.Verantwoordelijke, Status.Actief, ""));
			gebruikerdao.insert(new Gebruiker("Tim", "timmers", "tim.timmers@hogent.be", "123468lk",
					TypeGebruiker.Gebruiker, Status.Actief, "images/Lucas.png"));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		GenericDaoJpa.commitTransaction();

		GenericDao skdao = new GenericDaoJpa(SessieKalender.class);
		GenericDaoJpa.startTransaction();
		try {
//			SessieKalender sk1 = new SessieKalender(LocalDate.of(2019, 9, 25), LocalDate.of(2020, 6, 15), true);
			SessieKalender sk2 = new SessieKalender(LocalDate.of(2020, 9, 25), LocalDate.of(2021, 6, 15), true);
			SessieKalender sk3 = new SessieKalender(LocalDate.of(2021, 9, 25), LocalDate.of(2022, 6, 15), true);

//		skdao.insert(sk1);
			skdao.insert(sk2);
			skdao.insert(sk3);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		GenericDaoJpa.commitTransaction();

		GenericDao aankondigingdao = new GenericDaoJpa(Aankondiging.class);
		GenericDao sessiedao = new GenericDaoJpa(Sessie.class);

		GenericDaoJpa.startTransaction();

		try {
			Sessie sessie1 = (Sessie) sessiedao.get(13);
			Aankondiging aankondiging1 = new Aankondiging("een aankondiging",
					"Belangrijke info ivbm met de aankondiging",
					new Sessie(gebruiker1, "titel", "lokaal", LocalDateTime.now().plusDays(1).plusHours(1),
							LocalDateTime.now().plusDays(1).plusHours(3), 5, "omschrijving", "gastspreker"),
//					new Gebruiker("voornaam", "familienaam", "jonas.haenebalcke@student.hogent.be", "789456ok",
//							TypeGebruiker.Gebruiker, Status.Actief, ""
					gebruiker1, true);
			aankondigingdao.insert(aankondiging1);
		} catch (Exception e) {
			e.printStackTrace();
//			System.err.println(e.toString());
		}
		GenericDaoJpa.commitTransaction();
	}

}
