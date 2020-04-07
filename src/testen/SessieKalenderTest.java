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
		// academijaar 20-21, 16/09/2020 - 17/8/17
		sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 16), LocalDate.of(2021, 8, 17));

	}

	/*
	 * @Test public void GeenSessieKalenders_slaagt() {
	 * assertTrue(sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue()).isEmpty()); assertEquals(1,
	 * sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue()).size()); 
	 * }
	 * 
	 * @Test public void WelSessieKalenders_slaagt() { 
	 * // sessie maken en toevoegen aan de sessieKalender 
	 * Sessie sessie = new Sessie();
	 * sessieKalender.voegSessieToe(sessie); 
	 * assertEquals(1,sessieKalender.geefSessiesMaand(LocalDate.now().getMonthValue()).size()); }
	 */

	@Test
	public void SessieKalenderToevoegen_datumsToekomst_slaagt() {
		// academiejaar 16/9/2020 - 17/8/2021
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		// assertEquals(sessieKalender.getStartDate().getYear(),(LocalDate.now().getYear()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		// test gaat falen na een tijd, vanwege localDate.now(),
		sessieKalenderController.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		// nog kijken of sessiekalender is toegevoegd door count op te vragen voor en na
	}

	@Test
	public void SessieKalenderToevoegen_datumsVerleden_faalt() {
		// academiejaar 21/920/18 - 17/8/2019
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2018, 9, 21), LocalDate.of(2019, 8, 17));
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));

	}

	@Test
	public void SessieKalenderToevoegen_datumsFout_faalt() {
		// academiejaar 20/9/2020 - 24/8/2019 <=FOUT
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2019, 8, 24));
		assertFalse(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		// assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now())); moet niet
		// meer controlleren wat assertFalse is true
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));
	}

	@Test
	public void SessieKalenderAanpassen_StartDatumAanpassenCorrect_slaagt() {
		// academiejaar 24/9/2020 - 17/8/2021
		sessieKalender.setStartDate(LocalDate.of(2020, 9, 24));
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		// assertTrue(sessieKalender.getStartDate().getYear().isAfter(LocalDate.now().getYear()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertEquals(LocalDate.of(2020, 9, 24), sessieKalender.getStartDate());
		sessieKalenderController.wijzigSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		// nog kijken of sessiekalender is toegevoegd door count op te vragen voor en
		// na,

	}

	@Test
	public void SessieKalenderAanpassen_StartDatumAanpassen_faalt() {
		// academiejaar 17/9/2012 - 17/8/2021
		sessieKalender.setStartDate(LocalDate.of(2019, 9, 17));
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));
	}

	@Test
	public void SessieKalenderAanpassen_EindDatumAanpassenCorrect_slaagt() {
		// academiejaar 16/9/2020 - 3/8/2021
		sessieKalender.setEindDate(LocalDate.of(2021, 8, 3));

		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertEquals(LocalDate.of(2021, 8, 3), sessieKalender.getEindDate());
		sessieKalenderController.wijzigSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		// nog kijken of sessiekalender is toegevoegd door count op te vragen voor en na

	}

	@Test
	public void SessieKalenderAanpassen_EindDatumAanpassen_faalt() {
		// academiejaar 16/9/2020 - 15/9/2020
		sessieKalender.setEindDate(LocalDate.of(2020, 9, 15));

		assertFalse(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		// assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));
	}

}
