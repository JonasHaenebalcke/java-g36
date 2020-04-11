package testen;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.SessieKalender;
import domein.SessieKalenderController;

@ExtendWith(MockitoExtension.class)
public class SessieKalenderTest {
	
	@Mock
	private SessieKalenderController sessieKalenderControllerDummy;
	
	@InjectMocks
	private SessieKalender sessieKalender;

	public SessieKalenderTest() {
		// academiejaar 20-21, 16/09/2020 - 17/8/2021
		sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 16), LocalDate.of(2021, 8, 17));
		//sessieKalenderController = new SessieKalenderController();
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
		Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(null);
		
		assertNull(sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		assertEquals(null, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
	}

	@Test
	public void GeefAlleSessieKalenders_1_slaagt() {
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 21), LocalDate.of(2019, 8, 20));
		
		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(),sessieKalender.getEindDate());
		
		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
		 lijst.add(sessieKalender);
		 assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		 assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
	
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
	}

	// Testen SessieKalender toevoegen
	
	@Test
	public void SessieKalenderToevoegen_datumsToekomst_slaagt() {
		// academiejaar 16/9/2020 - 17/8/2021
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		// test gaat falen na een tijd, vanwege localDate.now(),
		assertNull(sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		
		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
		 lijst.add(sessieKalender);
		assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().equals(lijst);
		
	}

	@Test
	public void SessieKalenderToevoegen_datumsVerleden_faalt() {
		// academiejaar 21/920/18 - 17/8/2019
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2018, 9, 21), LocalDate.of(2019, 8, 17));
		
		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertFalse(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		
		Mockito.doThrow(new IllegalArgumentException("De datums liggen in het verleden"))
		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
			
		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		
		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
	}

	@Test
	public void SessieKalenderToevoegen_einddatumVerledenFout_faalt() {
		// academiejaar 20/9/2020 - 24/8/2019 <=FOUT
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2019, 8, 24));
		
		assertFalse(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertFalse(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		
		Mockito.doThrow(new IllegalArgumentException("De einddatum ligt in het verleden"))
		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		
		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
	}
	@Test
	public void SessieKalenderToevoegen_startdatumVerledenFout_faalt() {
		// academiejaar 20/9/2019 - 24/8/2021 <=FOUT
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2019, 9, 20), LocalDate.of(2021, 8, 24));
		
		assertTrue(sessieKalender.getStartDate().isBefore(sessieKalender.getEindDate()));
		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		
		Mockito.doThrow(new IllegalArgumentException("De startdatum ligt in het verleden"))
		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		
		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
	}
	@Test
	public void SessieKalenderToevoegen_startdatumToekomst_faalt() {
		// academiejaar 20/9/2022 - 24/8/2021 <=FOUT
		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2022, 9, 20), LocalDate.of(2021, 8, 24));
		
		assertFalse(sessieKalender.getStartDate().isBefore(sessieKalender.getEindDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		
		Mockito.doThrow(new IllegalArgumentException("De startdatum ligt na de einddatum"))
		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		
		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
	}
	@Test
	public void SessieKalenderToevoegen_1zitErAlIn_2sessieKalendersUniek_slaagt() {
		// academiejaar 16/9/2020 - 17/8/2021
		// academiejaar 20/9/2021 - 24/8/2022 <= Uniek
		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
		 lijst.add(sessieKalender);
		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		//nieuwe sessieKalender toevoegen
		SessieKalender sessieKalender2 = new SessieKalender(LocalDate.of(2021, 9, 20), LocalDate.of(2022, 8, 24));
		
		assertTrue(!sessieKalender2.getStartDate().isEqual(sessieKalender.getStartDate()));
		//assertNotEquals(sessieKalender.getStartDate().getYear(), sessieKalender2.getStartDate().getYear());
		assertTrue(sessieKalender2.getStartDate().getYear() != sessieKalender.getStartDate().getYear());
		assertTrue(sessieKalender2.getStartDate().isAfter(sessieKalender.getEindDate()));
		assertTrue(sessieKalender2.getStartDate().isAfter(LocalDate.now()));
		assertTrue(sessieKalender2.getEindDate().isAfter(sessieKalender2.getStartDate()));
		
		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
		
		assertEquals(2, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		lijst.add(sessieKalender2);
		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().equals(lijst);
		
	}
	@Test
	public void SessieKalenderToevoegen_1zitErAlIn_2sessieKalendersNietUniek_faalt() {
		// academiejaar 16/9/2020 - 17/8/2021
		// academiejaar 20/9/2020 - 24/8/2021 <= Niet uniek
		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
		
		assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
		 lijst.add(sessieKalender);
		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		//nieuwe sessieKalender proberen toevoegen <= faalt
		SessieKalender sessieKalender2 = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2021, 8, 24));
		
		assertTrue(!sessieKalender2.getStartDate().isEqual(sessieKalender.getStartDate()));
		assertTrue(sessieKalender2.getStartDate().getYear() == sessieKalender.getStartDate().getYear());
		assertFalse(sessieKalender2.getStartDate().isAfter(sessieKalender.getEindDate()));
		assertTrue(sessieKalender2.getStartDate().isAfter(LocalDate.now()));
		assertTrue(sessieKalender2.getEindDate().isAfter(sessieKalender2.getStartDate()));
		
		Mockito.doThrow(new IllegalArgumentException("Sessiekalender moet uniek zijn"))
		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
		
		assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
			
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().equals(lijst);
		
	}

	// Testen SessieKalender aanpassen
	
	@Test
	public void SessieKalenderAanpassen_StartdatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 24/9/2020 - 17/8/2021
		LocalDate startDatum =  LocalDate.of(2020, 9, 24);
		assertTrue(sessieKalender.getEindDate().isAfter(startDatum));
		assertTrue(startDatum.isAfter(LocalDate.now()));
		sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDate());
		assertEquals(LocalDate.of(2020, 9, 24), sessieKalender.getStartDate());		
	}

	@Test
	public void SessieKalenderAanpassen_startdatumAanpassenNaarVerleden_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 17/9/2019 - 17/8/2021;
		LocalDate startDatum = LocalDate.of(2019, 9, 17);
		assertFalse(startDatum.isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDate().isAfter(startDatum));
		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDate()));
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDate());
	}

	@Test
	public void SessieKalenderAanpassen_startdatumAanpassenNaarToekomst_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 17/9/2022 - 17/8/2021;
		LocalDate startDatum = LocalDate.of(2022, 9, 17);
		assertTrue(startDatum.isAfter(LocalDate.now()));
		assertFalse(startDatum.isBefore(sessieKalender.getEindDate()));
		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDate()));
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDate());
	}
	@Test
	public void SessieKalenderAanpassen_einddatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 16/09/2020 - 3/8/2021
		LocalDate eindDatum = LocalDate.of(2021, 8, 3);
		assertTrue(eindDatum.isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		sessieKalender.wijzigSessieKalender(sessieKalender.getStartDate(), eindDatum);
		assertEquals(LocalDate.of(2021, 8, 3), sessieKalender.getEindDate());

	}

	@Test
	public void SessieKalenderAanpassen_einddatumAanpassen_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 16/09/2020 - 15/9/2020
		LocalDate eindDatum = LocalDate.of(2020, 9, 15);
		assertFalse(eindDatum.isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertTrue(eindDatum.isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(sessieKalender.getStartDate(), eindDatum));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDate());
	}
	@Test
	public void SessieKalenderAanpassen_einddatumAanpassenNaarVerleden_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 16/09/2020 - 15/9/2019
		LocalDate eindDatum = LocalDate.of(2020, 9, 15);
		assertFalse(eindDatum.isAfter(sessieKalender.getStartDate()));
		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
		assertFalse(eindDatum.isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(sessieKalender.getStartDate(), eindDatum));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDate());
	}

}

