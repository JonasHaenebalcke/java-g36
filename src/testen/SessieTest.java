package testen;

import org.junit.jupiter.params.provider.Arguments;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import domein.Gebruiker;
import domein.Sessie;
import domein.Status;
import domein.TypeGebruiker;

public class SessieTest {
	Sessie sessie;
	Gebruiker gebruiker;
	Gebruiker ingeschrevene;
	
	public SessieTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
		gebruiker = new Gebruiker("Rein", "Daelman", "rein@gmail.com", "752460rd", TypeGebruiker.Hoofdverantwoordelijke, Status.Actief, null);
		ingeschrevene = new Gebruiker("Trein", "Daelman", "trein@gmail.com", "752461rd", TypeGebruiker.Gebruiker, Status.Actief, null);
		sessie = new Sessie(gebruiker, "titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker");
	}
	
	@Test
	public void maakSessie_slaagt() {
		Assertions.assertEquals(gebruiker, sessie.getVerantwoordelijke());
		Assertions.assertEquals("titel", sessie.getTitel());
		Assertions.assertEquals("B1.012", sessie.getLokaal());
		Assertions.assertEquals(LocalDate.now().plus(1, ChronoUnit.DAYS), sessie.getStartDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).getHour(), sessie.getStartDatum().getHour());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).toLocalDate(), sessie.getEindDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).getHour(), sessie.getEindDatum().getHour());

		Assertions.assertEquals(20, sessie.getCapaciteit());
		Assertions.assertEquals("omschrijving", sessie.getOmschrijving());
		Assertions.assertEquals("gastspreker", sessie.getGastspreker());
		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void maakSessie_BeschrijvingNull_slaagt() {
		sessie = new Sessie(gebruiker, "titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, null, "gastspreker");
		Assertions.assertEquals(gebruiker, sessie.getVerantwoordelijke());
		Assertions.assertEquals("titel", sessie.getTitel());
		Assertions.assertEquals("B1.012", sessie.getLokaal());
		Assertions.assertEquals(LocalDate.now().plus(1, ChronoUnit.DAYS), sessie.getStartDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).getHour(), sessie.getStartDatum().getHour());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).toLocalDate(), sessie.getEindDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).getHour(), sessie.getEindDatum().getHour());
		Assertions.assertEquals(20, sessie.getCapaciteit());
		Assertions.assertEquals(null, sessie.getOmschrijving());
		Assertions.assertEquals("gastspreker", sessie.getGastspreker());
		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void maakSessie_gastsprekerNull_slaagt() {
		sessie = new Sessie(gebruiker, "titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", null);
		Assertions.assertEquals(gebruiker, sessie.getVerantwoordelijke());
		Assertions.assertEquals("titel", sessie.getTitel());
		Assertions.assertEquals("B1.012", sessie.getLokaal());
		Assertions.assertEquals(LocalDate.now().plus(1, ChronoUnit.DAYS), sessie.getStartDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).getHour(), sessie.getStartDatum().getHour());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).toLocalDate(), sessie.getEindDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).getHour(), sessie.getEindDatum().getHour());
		Assertions.assertEquals(20, sessie.getCapaciteit());
		Assertions.assertEquals("omschrijving", sessie.getOmschrijving());
		Assertions.assertEquals(null, sessie.getGastspreker());
		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}

	@Test
	public void maakSessie_datumNu_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie = new Sessie(gebruiker, "titel", "B1.012", LocalDateTime.now(), LocalDateTime.now().plus(2, ChronoUnit.HOURS),
					20, "omschrijving", "gastspreker");
			
		});
	}
	
	@Test
	public void maakSessie_EindDatumNetNaStartDatum_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie = new Sessie(gebruiker, "titel", "B1.012", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusMinutes(10),
					20, "omschrijving", "gastspreker");
		});

		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void wijzigSessie_niets_slaagt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", false);
		
		Assertions.assertEquals(gebruiker, sessie.getVerantwoordelijke());
		Assertions.assertEquals("titel", sessie.getTitel());
		Assertions.assertEquals("B1.012", sessie.getLokaal());
		Assertions.assertEquals(LocalDate.now().plusDays(1), sessie.getStartDatum().toLocalDate());
		Assertions.assertEquals(LocalDateTime.now().plusDays(1).plusHours(2).toLocalDate(), sessie.getEindDatum().toLocalDate());
		Assertions.assertEquals(20, sessie.getCapaciteit());
		Assertions.assertEquals("omschrijving", sessie.getOmschrijving());
		Assertions.assertEquals("gastspreker", sessie.getGastspreker());
		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void wijzigSessie_Datum_slaagt() {
		sessie.wijzigSessie("sessie 360 noscopes", "B1.012", LocalDateTime.now().plus(3, ChronoUnit.DAYS), LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS),
				20, "omschrijving", "gastspreker", false);
		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void wijzigSessie_EindDatumVoorStartDatum_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigSessie("sessie 360 noscopes", "B1.012", LocalDateTime.now().plus(3, ChronoUnit.DAYS), LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(-1, ChronoUnit.HOURS),
					20, "omschrijving", "gastspreker", false);
		});

		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void wijzigSessie_EindDatumNetNaStartDatum_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigSessie("sessie 360 noscopes", "B1.012", LocalDateTime.now().plus(3, ChronoUnit.DAYS), LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(10, ChronoUnit.MINUTES),
					20, "omschrijving", "gastspreker", false);
		});

		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	@Test
	public void wijzigSessie_StartDatumReedsGepasseerd_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigSessie("sessie 360 noscopes", "B1.012", LocalDateTime.now().plus(-5, ChronoUnit.HOURS), LocalDateTime.now(),
					20, "omschrijving", "gastspreker", false);
		});

		Assertions.assertTrue(sessie.getStartDatum().isBefore(sessie.getEindDatum().plus(-30, ChronoUnit.MINUTES)));
	}
	
	
	@Test
	public void wijzigSessie_capaciteit_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigSessie("sessie 360 noscopes", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS),
					-2, "omschrijving", "gastspreker", false);
		});

		Assertions.assertEquals(20, sessie.getCapaciteit());
	}
	
	
	@Test
	public void wijzigSessie_capaciteit_slaagt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
					30, "omschrijving", "gastspreker", false);
		Assertions.assertEquals(30, sessie.getCapaciteit());
	}
	
	@Test 
	public void inschrijvenSessie_slaagt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", true);
		sessie.wijzigIngeschrevenen(ingeschrevene, true, false);
		Assertions.assertTrue(sessie.isGebruikerIngeschreven(ingeschrevene));
	}
	
	@Test 
	public void inschrijvenSessie_sessieNietOpen_faalt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", false);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigIngeschrevenen(ingeschrevene, true, false);
			
		});
	}
	
	@Test 
	public void inschrijvenSessie_gebruikerReedsUitgeschreven_faalt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", true);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigIngeschrevenen(ingeschrevene, false, false);
			
		});
	}
	
	@Test 
	public void aawezigZettenSessie_slaagt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", true);
		sessie.wijzigIngeschrevenen(ingeschrevene, true, true);
		Assertions.assertTrue(sessie.isGebruikerAanwezig(ingeschrevene));
	}
	
	@Test 
	public void aanwezigZettenSessie_sessieNietOpen_faalt() {
		sessie.wijzigSessie("titel", "B1.012", LocalDateTime.now().plus(1, ChronoUnit.DAYS).plusMinutes(5), LocalDateTime.now().plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS).plusMinutes(5),
				20, "omschrijving", "gastspreker", false);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			sessie.wijzigIngeschrevenen(ingeschrevene, true, true);
			
		});
	}
}
