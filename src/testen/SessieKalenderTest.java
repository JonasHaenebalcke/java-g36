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

	private SessieKalender sk;
	
	public SessieKalenderTest() {
		// academiejaar 20-21, 16/09/2020 - 17/8/2021
		sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 16), LocalDate.of(2021, 8, 17));
		//sessieKalenderController = new SessieKalenderController();
	}
	
	@Test
	public void sessieKalender_slaagt() {
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDatum());
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDatum());
		assertEquals(1, (sessieKalender.getEindDatum().getYear() - sessieKalender.getStartDatum().getYear()));
	}
	
	
	@Test
	public void GeefAlleSessieKalenders_leeg_slaagt() {
		Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(null);
		//Mockito.doThrow(new NullPointerException("geen sessieKalenders")).when(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
		assertNull(sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		//assertEquals(0, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
		//assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
		//assertEquals(null, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
		
		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
	}

//	@Test
//	public void GeefAlleSessieKalenders_1_slaagt() {
//		
//		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 21), LocalDate.of(2021, 8, 20));
//		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(),sessieKalender.getEindDate());
//			 List<SessieKalender> sessieKalenderLijst= new ArrayList<SessieKalender>();
//			 sessieKalenderLijst.add(sessieKalender);
//		 Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(sessieKalenderLijst);
//	
//		// assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		assertEquals(sessieKalenderLijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//	
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
//	}

	// Testen SessieKalender toevoegen
	
//	@Test
//	public void SessieKalenderToevoegen_datumsToekomst_slaagt() {
//		// academiejaar 16/9/2020 - 17/8/2021
//		assertTrue(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
//		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
//		// test gaat falen na een tijd, vanwege localDate.now(),
//		
//		//assertEquals(0, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		//assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
//		
//		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
//		 lijst.add(sessieKalender);
//		Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(lijst);
//		
//		//assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//		
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
//		
//	}
//
//	@Test
//	public void SessieKalenderToevoegen_einddatumVerledenFout_faalt() {
//		// academiejaar 20/9/2020 - 24/8/2019 <=FOUT
//		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2019, 8, 24));
//		
//		assertFalse(sessieKalender.getEindDate().isAfter(sessieKalender.getStartDate()));
//		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
//		assertFalse(sessieKalender.getEindDate().isAfter(LocalDate.now()));
//		
//		Mockito.doThrow(new IllegalArgumentException("De einddatum ligt in het verleden"))
//		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		
//		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
//				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
//		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
//		
//		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
//	}
//	@Test
//	public void SessieKalenderToevoegen_startdatumVerledenFout_faalt() {
//		// academiejaar 20/9/2019 - 24/8/2021 <=FOUT
//		//SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2019, 9, 20), LocalDate.of(2021, 8, 24));
//		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2021, 8, 24));
//		sessieKalender.setSessieKalenderID(1);
//		this.sessieKalender = sessieKalender;
//		LocalDate startdatum = LocalDate.of(2019, 9, 20);
//		LocalDate einddatum = LocalDate.of(2021, 8, 24);
//		assertThrows(IllegalArgumentException.class, () -> this.sessieKalender.setStartDate(startdatum));
//		 		
//		assertFalse(startdatum.isAfter(LocalDate.now()));
//		assertTrue(startdatum.isBefore(einddatum));
//		assertTrue(einddatum.isAfter(LocalDate.now()));
//		
////		assertFalse(sessieKalender.getStartDate().isAfter(LocalDate.now()));
////		assertTrue(sessieKalender.getStartDate().isBefore(sessieKalender.getEindDate()));
////		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
//
//		Mockito.doThrow(new IllegalArgumentException("De startdatum moet in de toekomst liggen"))
//		.when(sessieKalenderControllerDummy).voegToeSessieKalender(this.sessieKalender.getStartDate(), this.sessieKalender.getEindDate());
//		
//		
//		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
//				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
//		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
//		
//		//Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(startdatum, einddatum);
//	//Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(this.sessieKalender.getStartDate(), this.sessieKalender.getEindDate());
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
//	}
//	@Test
//	public void SessieKalenderToevoegen_startdatumToekomst_faalt() {
//		// academiejaar 20/9/2022 - 24/8/2021 <=FOUT
//		
//		SessieKalender sessieKalender = new SessieKalender(LocalDate.of(2022, 9, 20), LocalDate.of(2021, 9, 24));
//		
//		LocalDate startdatum =  LocalDate.of(2022, 9, 20);
//		LocalDate einddatum =  LocalDate.of(2021, 8, 24);
//		assertThrows(IllegalArgumentException.class, () -> sessieKalender.setEindDate(einddatum));
//		/*Mockito.doThrow(new IllegalArgumentException("De einddatum moet in de toekomst liggen en na de startdatum komen."))
//		.when(sessieKalender = new SessieKalender(startdatum, einddatum));*/
//		
//		
//		assertFalse(sessieKalender.getStartDate().isBefore(sessieKalender.getEindDate()));
//		assertTrue(sessieKalender.getStartDate().isAfter(LocalDate.now()));
//		assertTrue(sessieKalender.getEindDate().isAfter(LocalDate.now()));
//		
//		Mockito.doThrow(new IllegalArgumentException("De startdatum ligt na de einddatum"))
//		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		
//		/*assertThrows(IllegalArgumentException.class, () -> sessieKalenderController
//				.voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate()));*/
//		assertTrue(sessieKalenderControllerDummy.geefSessieKalenderObservableList().isEmpty());
//		
//		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList().isEmpty();
//	}
//	@Test
//	public void SessieKalenderToevoegen_1zitErAlIn_2sessieKalendersUniek_slaagt() {
//		// academiejaar 16/9/2020 - 17/8/2021
//		// academiejaar 20/9/2021 - 24/8/2022 <= Uniek
//		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
//		 lijst.add(sessieKalender);
//		 Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderList()).thenReturn(lijst);
//		
//		//assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		//assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//		
//		//nieuwe sessieKalender toevoegen
//		SessieKalender sessieKalender2 = new SessieKalender(LocalDate.of(2021, 9, 20), LocalDate.of(2022, 8, 24));
//		
//		assertNotEquals(sessieKalender2.getStartDate(), sessieKalender.getStartDate());
//		assertNotEquals(sessieKalender.getStartDate().getYear(), sessieKalender2.getStartDate().getYear());
//		assertTrue(sessieKalender2.getStartDate().isAfter(sessieKalender.getEindDate()));
//		//assertTrue(sessieKalender2.getStartDate().isAfter(LocalDate.now()));
//		
//		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
//		lijst.add(sessieKalender2);
//		Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(lijst);
//		
//		//assertEquals(2, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//		
//		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
//		
//	}
//	@Test
//	public void SessieKalenderToevoegen_1zitErAlIn_2sessieKalendersNietUniek_faalt() {
//		// academiejaar 16/9/2020 - 17/8/2021
//		// academiejaar 20/9/2020 - 24/8/2021 <= Niet uniek
//		Mockito.doNothing().when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender.getStartDate(), sessieKalender.getEindDate());
//		
//		
//		 List<SessieKalender> lijst= new ArrayList<SessieKalender>();
//		 lijst.add(sessieKalender);
//		 Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(lijst);
//		// assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//			
//		//assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//		
//		//nieuwe sessieKalender proberen toevoegen <= faalt
//		SessieKalender sessieKalender2 = new SessieKalender(LocalDate.of(2020, 9, 20), LocalDate.of(2021, 8, 24));
//		
//		assertNotEquals(sessieKalender.getStartDate(), sessieKalender2.getStartDate());
//		assertEquals(sessieKalender2.getStartDate().getYear(), sessieKalender.getStartDate().getYear());
//		assertFalse(sessieKalender2.getStartDate().isAfter(sessieKalender.getEindDate()));
//		assertTrue(sessieKalender2.getStartDate().isAfter(LocalDate.now()));
//		
//	Mockito.doThrow(new IllegalArgumentException("Sessiekalender moet uniek zijn"))
//		.when(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
//	
//		 Mockito.when(sessieKalenderControllerDummy.geefSessieKalenderObservableList()).thenReturn(lijst);
//		//assertEquals(1, sessieKalenderControllerDummy.geefSessieKalenderObservableList().size());
//		assertEquals(lijst, sessieKalenderControllerDummy.geefSessieKalenderObservableList());
//			
//		//Mockito.verify(sessieKalenderControllerDummy).voegToeSessieKalender(sessieKalender2.getStartDate(), sessieKalender2.getEindDate());
//		
//		Mockito.verify(sessieKalenderControllerDummy).geefSessieKalenderObservableList();
//		
//	}

	// Testen wijzig SessieKalender 
	
	@Test
	public void WijzigSessieKalender_StartdatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 24/9/2020 - 17/8/2021
		LocalDate startDatum =  LocalDate.of(2020, 9, 24);
		assertTrue(sessieKalender.getEindDatum().isAfter(startDatum));
		assertTrue(startDatum.isAfter(LocalDate.now()));
		sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDatum());
		assertEquals(LocalDate.of(2020, 9, 24), sessieKalender.getStartDatum());		
	}

	@Test
	public void WijzigSessieKalender_startdatumAanpassenNaarVerleden_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 17/9/2019 - 17/8/2021;
		LocalDate startDatum = LocalDate.of(2019, 9, 17);
		assertFalse(startDatum.isAfter(LocalDate.now()));
		assertTrue(sessieKalender.getEindDatum().isAfter(startDatum));
		assertTrue(sessieKalender.getEindDatum().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDatum()));
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDatum());
	}

	@Test
	public void WijzigSessieKalender_startdatumAanpassenNaarToekomst_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 17/9/2022 - 17/8/2021;
		LocalDate startDatum = LocalDate.of(2022, 9, 17);
		assertTrue(startDatum.isAfter(LocalDate.now()));
		assertFalse(startDatum.isBefore(sessieKalender.getEindDatum()));
		assertTrue(sessieKalender.getEindDatum().isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(startDatum, sessieKalender.getEindDatum()));
		assertEquals(LocalDate.of(2020, 9, 16), sessieKalender.getStartDatum());
	}
	@Test
	public void WijzigSessieKalender_einddatumAanpassenCorrect_slaagt() {
		// voor 16/09/2020 - 17/8/2021
		// na 16/09/2020 - 3/8/2021
		LocalDate eindDatum = LocalDate.of(2021, 8, 3);
		assertTrue(eindDatum.isAfter(sessieKalender.getStartDatum()));
		assertTrue(sessieKalender.getStartDatum().isAfter(LocalDate.now()));
		sessieKalender.wijzigSessieKalender(sessieKalender.getStartDatum(), eindDatum);
		assertEquals(LocalDate.of(2021, 8, 3), sessieKalender.getEindDatum());

	}

	@Test
	public void WijzigSessieKalender_einddatumAanpassen_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 16/09/2020 - 15/9/2020
		LocalDate eindDatum = LocalDate.of(2020, 9, 15);
		assertFalse(eindDatum.isAfter(sessieKalender.getStartDatum()));
		assertTrue(sessieKalender.getStartDatum().isAfter(LocalDate.now()));
		assertTrue(eindDatum.isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(sessieKalender.getStartDatum(), eindDatum));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDatum());
	}
	@Test
	public void WijzigSessieKalender_einddatumAanpassenNaarVerleden_faalt() {
		// voor 16/09/2020 - 17/8/2021
		// faalt 16/09/2020 - 15/9/2019
		LocalDate eindDatum = LocalDate.of(2019, 9, 15);
		assertFalse(eindDatum.isAfter(sessieKalender.getStartDatum()));
		assertTrue(sessieKalender.getStartDatum().isAfter(LocalDate.now()));
		assertFalse(eindDatum.isAfter(LocalDate.now()));
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(sessieKalender.getStartDatum(), eindDatum));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDatum());
	}
	
	@Test
	public void WijzigSessieKalender_NietOpeenVolgendeJaren_faalt() {
		// academiejaar 20-21, 16/09/2020 - 17/8/2021
		// academiejaar 20-21, 16/09/2020 - 17/8/2022
		assertNotEquals(1, (LocalDate.of(2020, 8, 17)).getYear() - sessieKalender.getStartDatum().getYear());
		assertThrows(IllegalArgumentException.class, () -> sessieKalender.wijzigSessieKalender(sessieKalender.getStartDatum(), LocalDate.of(2020, 8, 17)));
		assertEquals(LocalDate.of(2021, 8, 17), sessieKalender.getEindDatum());
	}

}

