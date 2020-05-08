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
			gebruiker = new Gebruiker("Lucas", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker,
					Status.Actief, null);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
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

	static Stream<Arguments> fouteGegevens() {
		return Stream.of(
				Arguments.of("Jonas123", "vdh", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker, Status.Actief,
						null), // CTRL ALT pijltje benede
				Arguments.of("Lucas", "vdh123", "lucas@Email.com", "123456lv", TypeGebruiker.Gebruiker, Status.Actief,
						null), // ctrl shift A
				Arguments.of("Lucas", "vdh", "lucas@Email@com", "123456lv", TypeGebruiker.Gebruiker, Status.Actief,
						null)
		);
	}

	@ParameterizedTest
	@MethodSource("fouteGegevens")
	public void gebruiker_faalt(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			TypeGebruiker type, Status status, String profielfoto) {
		gebruiker = null;

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker = new Gebruiker(voornaam, familienaam, mailadres, gebruikersnaam, type, status, profielfoto);
		});
	}

	@Test
	public void wijzigGebruiker_niets_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Actief, null);

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
		gebruiker.wijzigGebruiker("Jonas", "vdh", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Actief, null);

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
	}

	@Test
	public void wijzigGebruiker_VoornaamNaam_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Jonas123", "vdh", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Actief,
					null);
		});

		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
	}

	@Test
	public void wijzigGebruiker_FamilieNaam_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "Behiels", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Actief, null);

		Assertions.assertEquals("Behiels", gebruiker.getFamilienaam());
	}

	@Test
	public void wijzigGebruiker_FamilieNaam_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Lucas", "Behiels123", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Actief,
					null);
		});

		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
	}

	@Test
	public void wijzigGebruiker_Email_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "Lucas@student.hogent.be", TypeGebruiker.Gebruiker, Status.Actief,
				null);

		Assertions.assertEquals("Lucas@student.hogent.be", gebruiker.getMailadres());
	}

	@Test
	public void wijzigGebruiker_Email_faalt() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas.Email.com", TypeGebruiker.Gebruiker, Status.Actief, null);
		});
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
	}

	@Test
	public void wijzigGebruiker_Type_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", TypeGebruiker.Verantwoordelijke, Status.Actief,
				null);

		Assertions.assertEquals(TypeGebruiker.Verantwoordelijke, gebruiker.getType());
	}

	@Test
	public void wijzigGebruiker_Status_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", TypeGebruiker.Gebruiker, Status.Geblokkeerd, null);

		Assertions.assertEquals(Status.Geblokkeerd, gebruiker.getStatus());
	}

	@Test
	public void wijzigGebruiker_foto_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "Lucas@student.hogent.be", TypeGebruiker.Gebruiker, Status.Actief,
				"test.png");

		Assertions.assertEquals("Lucas@student.hogent.be", gebruiker.getMailadres());
	}

	@Test
	public void wijzigGebruiker_AlleParameter_slaagt() {
		gebruiker.wijzigGebruiker("Jonas", "Behiels", "jonas@Email.be", TypeGebruiker.Verantwoordelijke,
				Status.NietActief, "test.png");

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
		Assertions.assertEquals("Behiels", gebruiker.getFamilienaam());
		Assertions.assertEquals("jonas@Email.be", gebruiker.getMailadres());
		Assertions.assertEquals("123456lv", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.NietActief, gebruiker.getStatus());
		Assertions.assertEquals(TypeGebruiker.Verantwoordelijke, gebruiker.getType());
		Assertions.assertEquals("test.png", gebruiker.getProfielfoto());
	}
}
