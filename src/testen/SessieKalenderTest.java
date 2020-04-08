package testen;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import domein.SessieKalender;
import domein.SessieKalenderController;

public class SessieKalenderTest {
	SessieKalenderController sessieKalenderController;
	SessieKalender sessieKalender;

	public SessieKalenderTest() {
		// academiejaar 20-21, 16/09/2020 - 17/8/2021
		sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 16), LocalDate.of(2021, 8, 17));
		sessieKalenderController = new SessieKalenderController();
	}

	/*
	 * @Test public void GeenSessieKalenders_slaagt() {
	 * assertTrue(sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue()).
	 * isEmpty()); assertEquals(1,
	 * sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue()).size()); }
	 * 
	 * @Test public void WelSessieKalenders_slaagt() { // sessie maken en toevoegen
	 * aan de sessieKalender Sessie sessie = new Sessie();
	 * sessieKalender.voegSessieToe(sessie);
	 * assertEquals(1,sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue(
	 * )).size()); }
	 */
	
	
	@Test
	public void GeefAlleSessieKalenders_leeg_slaagt() {
		assertNull(sessieKalenderController.geefSessieKalenderObservableList(), "Er zijn geen sessieKalenders te berschikking");
		assertTrue(sessieKalenderController.geefSessieKalenderObservableList().isEmpty());
	}

	@Test
	public void GeefAlleSessieKalenders_1_slaagt() {
		sessieKalenderController.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		assertEquals(1, sessieKalenderController.geefSessieKalenderObservableList().size());
	}


	@Test
	public void SessieKalenderToevoegen_datumsToekomst_slaagt() {
		// academiejaar 16/9/2020 - 17/8/2021
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		// assertEquals(sessieKalender.getStartDate().getYear(),(LocalDate.now().getYear()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		// test gaat falen na een tijd, vanwege localDate.now(),
		assertNull(sessieKalenderController.geefSessieKalenderObservableList().size());
		assertTrue(sessieKalenderController.geefSessieKalenderObservableList().isEmpty());
		sessieKalenderController.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		assertEquals(1,sessieKalenderController.geefSessieKalenderObservableList().size());
	}

	@Test
	public void SessieKalenderToevoegen_datumsVerleden_faalt() {
		// academiejaar 21/920/18 - 17/8/2019
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2018, 9, 21), LocalDate.of(2019, 8, 17));
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertFalse(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));
		assertTrue(sessieKalenderController.geefSessieKalenderObservableList().isEmpty());
	}

	@Test
	public void SessieKalenderToevoegen_datumsFout_faalt() {
		// academiejaar 20/9/2020 - 24/8/2019 <=FOUT
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2019, 8, 24));
		assertFalse(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertFalse(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));
		assertTrue(sessieKalenderController.geefSessieKalenderObservableList().isEmpty());
	}

	@Test
	public void SessieKalenderAanpassen_StartDatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 24/9/2020 - 17/8/2021
		LocalDate startDatum =  LocalDate.of(2020, 9, 24);
		assertTrue(sessieKalender.getEindDate().isAfter(startDatum));
		assertTrue(startDatum.isAfter(LocalDate.now()));
		// assertTrue(sessieKalender.getStartDate().getYear().isAfter(LocalDate.now().getYear()));
		// sessieKalender moet eerst toegevoegd worden om het te wijzigen
		sessieKalenderController
		.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
			sessieKalenderController.wijzigSessieKalender(startDatum, sessieKalender.getEindDate());
		assertEquals(LocalDate.of(2020, 9, 24), sessieKalender.getStartDate());		
	}

	@Test
	public void SessieKalenderAanpassen_StartDatumAanpassen_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 17/9/2019 - 17/8/2021;
		LocalDate startDatum = LocalDate.of(2019, 9, 17);
		assertFalse(startDatum.isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDate().isAfter(startDatum));
		// sessieKalender moet eerst toegevoegd worden om het te wijzigen
		sessieKalenderController
		.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.wijzigSessieKalender(startDatum, sessieKalender.getEindDate()));
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDate());
	}

	@Test
	public void SessieKalenderAanpassen_EindDatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 16/9/2020 - 3/8/2021
		LocalDate eindDatum = LocalDate.of(2021, 8, 3);
		assertTrue(eindDatum.isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		// sessieKalender moet eerst toegevoegd worden om het te wijzigen
		sessieKalenderController
		.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		sessieKalenderController.wijzigSessieKalender(sessieKalender.getStartDate(), eindDatum);
		assertEquals(LocalDate.of(2021, 8, 3), sessieKalender.getEindDate());

	}

	@Test
	public void SessieKalenderAanpassen_EindDatumAanpassen_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 16/9/2020 - 15/9/2020
		LocalDate eindDatum = LocalDate.of(2020, 9, 15);
		assertFalse(eindDatum.isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		// sessieKalender moet eerst toegevoegd worden om het te wijzigen
		sessieKalenderController
		.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.wijzigSessieKalender(sessieKalender.getStartDate(), eindDatum));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDate());
	}

}
