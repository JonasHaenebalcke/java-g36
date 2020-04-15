package testen;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import domein.Gebruiker;
import domein.Status;
import domein.TypeGebruiker;
import domein.*;

public class GebruikerTest {

	Gebruiker gebruiker;

	public GebruikerTest() {
		try {
			gebruiker = new Gebruiker("Lucas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker, Status.Actief, null, "123");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void gebruiker_slaagt() {
		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
		Assertions.assertEquals("123456lv", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(TypeGebruiker.Gebruiker, gebruiker.getType());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
		Assertions.assertNull(gebruiker.getProfielfoto());
	}

//	static Stream<Arguments> fouteGegevens() {
//		return Stream.of(
//				Arguments.of(new String[] { "Jonas123", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//						Status.Actief.toString(), null }), // CTRL ALT pijltje benede
//				Arguments.of(new String[] { "Lucas", "vdh123", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//						Status.Actief.toString(), null }), // ctrl shift A
//				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email@com", "123456lv", Type.Gebruiker.toString(),
//						Status.Actief.toString(), null }),
//				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "12345678", "gebruiker",
//						Status.Actief.toString(), null }),
//				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//						"nietactief", null }),
//				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//						Status.Actief.toString(), "?" })
//				);
//	}
//	
//    static Stream<String[]> fouteGegevens() {
//        return Stream.of(new String[] { "Jonas123", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//                        Status.Actief.toString(), null }, // CTRL ALT pijltje benede
//                new String[] { "Lucas", "vdh123", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//                        Status.Actief.toString(), null }, // ctrl shift A
//                new String[] { "Lucas", "vdh", "lucas@Email@com", "123456lv", Type.Gebruiker.toString(),
//                        Status.Actief.toString(), null },
//                new String[] { "Lucas", "vdh", "lucas@Email.com", "12345678", "gebruiker",
//                        Status.Actief.toString(), null },
//                new String[] { "Lucas", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//                        "nietactief", null },
//                new String[] { "Lucas", "vdh", "lucas@Email.com", "123456lv", Type.Gebruiker.toString(),
//                        Status.Actief.toString(), "?" }
//                );
//    }
//
//	@ParameterizedTest
//	@MethodSource("fouteGegevens")
//	public void gebruiker_faalt(String[] fouteGegevens) {
//		gebruiker = null;
//		
////		String vnaam = fouteGegevens[0];
////		String fnaam = fouteGegevens[1];
////		String email = fouteGegevens[2];
////		String gnaam = fouteGegevens[3];
////		String foto = fouteGegevens[6];
//		
//		Assertions.assertThrows(IllegalArgumentException.class, () -> {
//			gebruiker = new Gebruiker(fouteGegevens[0], fouteGegevens[1], fouteGegevens[2], fouteGegevens[3],
//					Type.Gebruiker, Status.Actief, fouteGegevens[6]);
////			gebruiker = new Gebruiker(vnaam, fnaam, email, gnaam,
////					Type.Gebruiker, Status.Actief, foto);
//		});
//
//		Assertions.assertNull(gebruiker);
//	}

	@Test
	public void wijzigGebruiker_niets_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
				Status.Actief, null);

		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
		Assertions.assertEquals("123456lv", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
		Assertions.assertEquals(TypeGebruiker.Gebruiker, gebruiker.getType());
		Assertions.assertNull(gebruiker.getProfielfoto());
	}

	@Test
	public void wijzigGebruiker_VoornaamNaam_slaagt() {
		gebruiker.wijzigGebruiker("Jonas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
				Status.Actief, null);

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
	}
	
	@Test
	public void wijzigGebruiker_VoornaamNaam_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Jonas123", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
					Status.Actief, null);
		});

		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
	}
	
	@Test
	public void wijzigGebruiker_FamilieNaam_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "Behiels", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
				Status.Actief, null);

		Assertions.assertEquals("Behiels", gebruiker.getFamilienaam());
	}
	
	@Test
	public void wijzigGebruiker_FamilieNaam_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Lucas", "Behiels123", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
					Status.Actief, null);
		});

		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
	}
	
	@Test
	public void wijzigGebruiker_Email_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "Lucas@student.hogent.be", "123456lv", TypeGebruiker.Gebruiker,
				Status.Actief, null);

		Assertions.assertEquals("Lucas@student.hogent.be", gebruiker.getMailadres());
	}
	
	@Test
	public void wijzigGebruiker_Email_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas.Email.com", "123456lv", TypeGebruiker.Gebruiker,
					Status.Actief, null);
		});
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
	}
	
	@Test
	public void wijzigGebruiker_gebruikersnaam_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123456ab", TypeGebruiker.Gebruiker,
				Status.Actief, null);

		Assertions.assertEquals("123456ab", gebruiker.getGebruikersnaam());
	}
	
	@Test
	public void wijzigGebruiker_gebruikersnaam_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "12345678", TypeGebruiker.Gebruiker,
					Status.Actief, null);
		});

		Assertions.assertEquals("123456lv", gebruiker.getGebruikersnaam());
	}
	
	@Test
	public void wijzigGebruiker_Type_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Verantwoordelijke,
				Status.Actief, null);

		Assertions.assertEquals(TypeGebruiker.Verantwoordelijke, gebruiker.getType());
	}

	@Test
	public void wijzigGebruiker_Status_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
				Status.Geblokkeerd, null);

		Assertions.assertEquals(Status.Geblokkeerd, gebruiker.getStatus());
	}
	
	@Test
	public void wijzigGebruiker_foto_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "Lucas@student.hogent.be", "123456lv", TypeGebruiker.Gebruiker,
				Status.Actief, "test.png");

		Assertions.assertEquals("Lucas@student.hogent.be", gebruiker.getMailadres());
	}

	@Test
	public void wijzigGebruiker_AlleParameter_slaagt() {
		gebruiker.wijzigGebruiker("Jonas", "Behiels", "jonas@Email.be", "123456jb", TypeGebruiker.Verantwoordelijke,
				Status.NietActief, "test.png");

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
		Assertions.assertEquals("Behiels", gebruiker.getFamilienaam());
		Assertions.assertEquals("jonas@Email.be", gebruiker.getMailadres());
		Assertions.assertEquals("123456jb", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.NietActief, gebruiker.getStatus());
		Assertions.assertEquals(TypeGebruiker.Verantwoordelijke, gebruiker.getType());
		Assertions.assertEquals("test.png", gebruiker.getProfielfoto());
	}

//	@ParameterizedTest
//	@MethodSource("fouteGegevens")
//	public void wijzigGebruiker_faalt(String[] fouteGegevens) {
//		Assertions.assertThrows(IllegalArgumentException.class, () -> {
//			gebruiker.wijzigGebruiker(fouteGegevens[0], fouteGegevens[1], fouteGegevens[2], fouteGegevens[3],
//					Type.valueOf(fouteGegevens[4]), Status.valueOf(fouteGegevens[5]), fouteGegevens[6]);
//		});
//
//		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
//		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
//		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
//		Assertions.assertEquals("123456lv", gebruiker.getGebruikersnaam());
//		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
//		Assertions.assertEquals(Type.Gebruiker, gebruiker.getType());
//		Assertions.assertNull(gebruiker.getProfielfoto());
//	}
}
