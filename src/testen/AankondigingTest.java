package testen;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Aankondiging;
import domein.Gebruiker;
import domein.Sessie;
import domein.Status;
import domein.TypeGebruiker;

public class AankondigingTest {

	private Gebruiker hoofdVerantwoordelijke;
	private Gebruiker verantwoordelijke;
	private Sessie sessie;

	public AankondigingTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
		hoofdVerantwoordelijke = new Gebruiker("voornaam", "familienaam", "v.f@student.be", "123456vf",
				TypeGebruiker.Hoofdverantwoordelijke, Status.Actief, "foto");
		verantwoordelijke = new Gebruiker("voornaamtwee", "familienaamtwee", "v.f@student2.be", "123457vf",
				TypeGebruiker.Verantwoordelijke, Status.Actief, "foto2");
		sessie = new Sessie(verantwoordelijke, "titel", "lokaal", LocalDateTime.now().plusDays(2),
				LocalDateTime.now().plusDays(2).plusHours(2), 30, "omschrijving", "gastspreker");
	}

	private void zetSessieOpen() {
		sessie.wijzigSessie(sessie.getTitel(), sessie.getLokaal(), sessie.getStartDatum(), sessie.getEindDatum(),
				sessie.getCapaciteit(), sessie.getOmschrijving(), sessie.getGastspreker(), true);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void aankondiging_Verantwoordelijke_isVerzonden_slaagt(boolean isVerzonden) {
		zetSessieOpen();
		Aankondiging aankondiging = new Aankondiging("titel", "aankondiging", sessie, verantwoordelijke, isVerzonden);

		Assertions.assertEquals("titel", aankondiging.getTitel());
		Assertions.assertEquals("aankondiging", aankondiging.getAankondingingTekst());
		Assertions.assertEquals(sessie, aankondiging.getSessie());
		Assertions.assertEquals(verantwoordelijke, aankondiging.getPublicist());
		Assertions.assertEquals(isVerzonden, aankondiging.isVerzonden());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void aankondiging_HoofdVerantwoordelijke_isVerzonden_slaagt(boolean isVerzonden) {
		zetSessieOpen();
		Aankondiging aankondiging = new Aankondiging("titel", "aankondiging", sessie, hoofdVerantwoordelijke,
				isVerzonden);

		Assertions.assertEquals("titel", aankondiging.getTitel());
		Assertions.assertEquals("aankondiging", aankondiging.getAankondingingTekst());
		Assertions.assertEquals(sessie, aankondiging.getSessie());
		Assertions.assertEquals(hoofdVerantwoordelijke, aankondiging.getPublicist());
		Assertions.assertEquals(isVerzonden, aankondiging.isVerzonden());
	}

	@ParameterizedTest
	@NullSource
	public void aankondiging_SessieNull_faalt(Sessie sessie) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging("titel", "aankondiging", sessie, verantwoordelijke, false);
		});
	}

	@ParameterizedTest
	@NullSource
	public void aankondiging_SessieNietOpenVoorInschrijvingen_faalt(Sessie sessie) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging("titel", "aankondiging", sessie, verantwoordelijke, true);
		});
	}

	@ParameterizedTest
	@NullSource
	public void aankondiging_GebruikerNull_faalt(Gebruiker publicist) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging("titel", "aankondiging", sessie, publicist, false);
		});
	}

//	@Test
//	public void aankondiging_GebruikerNietActief_faalt() {
//		verantwoordelijke.setStatus(Status.NietActief);
//		Assertions.assertThrows(IllegalArgumentException.class, () -> {
//			new Aankondiging("titel", "aankondiging", sessie, publicist, false);
//		});
//	}
//	
//	@Test
//	public void aankondiging_GebruikerGeblokkeerd_faalt() {
//		verantwoordelijke.setStatus(Status.NietActief);
//		Assertions.assertThrows(IllegalArgumentException.class, () -> {
//			new Aankondiging("titel", "aankondiging", sessie, publicist, false);
//		});
//	}

	@ParameterizedTest
	@NullAndEmptySource
	public void aankondiging_Titel_faalt(String titel) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging(titel, "aankondiging", sessie, verantwoordelijke, false);
		});
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void aankondiging_Tekst_faalt(String tekst) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging("titel", tekst, sessie, verantwoordelijke, false);
		});
	}

	static Stream<Arguments> fouteGegevens() {
		return Stream.of(Arguments.of("", "aankondiging", true), // CTRL ALT pijltje benede
				Arguments.of("titel", "", true), // ctrl shift A
				Arguments.of("", "", true), Arguments.of("", "", false), Arguments.of(null, null, true),
				Arguments.of(null, null, false));
	}

	@ParameterizedTest
	@MethodSource("fouteGegevens")
	public void wijzigAankondiging_faalt(String titel, String tekst, boolean isVerzonden) {
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Aankondiging(titel, tekst, sessie, verantwoordelijke, false);
		});
	}

	static Stream<Arguments> juisteGegevens() {
		return Stream.of(Arguments.of("nieuwe titel", "aankondiging", false), // CTRL ALT pijltje benede
				Arguments.of("titel", "nieuwe aankondiging", false), // ctrl shift A
				Arguments.of("nieuwe titel", "aankondiging", false));
	}

	@ParameterizedTest
	@MethodSource("juisteGegevens")
	public void wijzigAankondiging_slaagt(String titel, String tekst, boolean isVerzonden) {
		Aankondiging aankondiging = new Aankondiging("titel", "aankondiging", sessie, verantwoordelijke, isVerzonden);
		aankondiging.wijzigAankondiging(titel, tekst);

		Assertions.assertEquals(titel, aankondiging.getTitel());
		Assertions.assertEquals(tekst, aankondiging.getAankondingingTekst());
		Assertions.assertEquals(sessie, aankondiging.getSessie());
		Assertions.assertEquals(verantwoordelijke, aankondiging.getPublicist());
		Assertions.assertEquals(isVerzonden, aankondiging.isVerzonden());
	}
	
	@Test
	public void wijzigAankondiging_isAlVerzonden_faalt() {
		zetSessieOpen();
		Aankondiging aankondiging = new Aankondiging("titel", "aankondiging", sessie, hoofdVerantwoordelijke,
				true);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
		aankondiging.wijzigAankondiging("nieuwe titel", "nieuwe aankondiging");
		});

		Assertions.assertEquals("titel", aankondiging.getTitel());
		Assertions.assertEquals("aankondiging", aankondiging.getAankondingingTekst());
		Assertions.assertEquals(sessie, aankondiging.getSessie());
		Assertions.assertEquals(hoofdVerantwoordelijke, aankondiging.getPublicist());
		Assertions.assertTrue(aankondiging.isVerzonden());
	}
}
