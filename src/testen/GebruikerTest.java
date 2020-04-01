package testen;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import domein.Gebruiker;
import domein.Status;
import domein.Type;

public class GebruikerTest {

	Gebruiker gebruiker;

	public GebruikerTest() {
		gebruiker = new Gebruiker("Lucas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker, Status.Actief, null);
	}

	@Test
	public void gebruiker_slaagt() {
		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
		Assertions.assertEquals("123jb", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Type.Gebruiker, gebruiker.getType());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
		Assertions.assertNull(gebruiker.getProfielfoto());
	}

	static Stream<Arguments> fouteGegevens() {
		return Stream.of(
				Arguments.of(new String[] { "Jonas123", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
						Status.Actief.toString(), null }), // CTRL ALT pijltje benede
				Arguments.of(new String[] { "Lucas", "vdh123", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
						Status.Actief.toString(), null }), // ctrl shift A
				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email@com", "123lv", Type.Gebruiker.toString(),
						Status.Actief.toString(), null }),
				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "12345", "gebruiker",
						Status.Actief.toString(), null }),
				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
						"nietactief", null }),
				Arguments.of(new String[] { "Lucas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
						Status.Actief.toString(), "?" }));
	}

	@ParameterizedTest
	@MethodSource("fouteGegevens")
	public void gebruiker_faalt(String[] fouteGegevens) {
		gebruiker = null;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker = new Gebruiker(fouteGegevens[0], fouteGegevens[1], fouteGegevens[2], fouteGegevens[3],
					Type.Gebruiker, Status.Actief, fouteGegevens[6]);
		});

		Assertions.assertNull(gebruiker);
	}

	@Test
	public void wijzigGebruiker_niets_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
				Status.Actief.toString(), null);

		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
		Assertions.assertEquals("123jb", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
		Assertions.assertEquals(Type.Gebruiker, gebruiker.getType());
		Assertions.assertNull(gebruiker.getProfielfoto());
	}

	@Test
	public void wijzigGebruiker_Naam_slaagt() {
		gebruiker.wijzigGebruiker("Jonas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
				Status.Actief.toString(), null);

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus()); // ongewijzigd?
	}

	@Test
	public void wijzigGebruiker_Status_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123lv", Type.Gebruiker.toString(),
				Status.Geblokkeerd.toString(), null);

		Assertions.assertEquals(Status.Geblokkeerd, gebruiker.getStatus());
	}

	@Test
	public void wijzigGebruiker_Gebruiker_slaagt() {
		gebruiker.wijzigGebruiker("Lucas", "vdh", "lucas@Email.com", "123lv", Type.Verantwoordelijke.toString(),
				Status.Actief.toString(), null);

		Assertions.assertEquals(Type.Verantwoordelijke, gebruiker.getType());
	}

	@Test
	public void wijzigGebruiker_AlleParameter_slaagt() {
		gebruiker.wijzigGebruiker("Jonas", "Behiels", "jonas@Email.be", "123jb", Type.Verantwoordelijke.toString(),
				Status.NietActief.toString(), "test.png");

		Assertions.assertEquals("Jonas", gebruiker.getVoornaam());
		Assertions.assertEquals("Behiels", gebruiker.getFamilienaam());
		Assertions.assertEquals("jonas@Email.be", gebruiker.getMailadres());
		Assertions.assertEquals("123jb", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.NietActief, gebruiker.getStatus());
		Assertions.assertEquals(Type.Verantwoordelijke, gebruiker.getType());
		Assertions.assertEquals("test.png", gebruiker.getProfielfoto());
	}

	@ParameterizedTest
	@MethodSource("fouteGegevens")
	public void wijzigGebruiker_faalt(String[] fouteGegevens) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			gebruiker.wijzigGebruiker(fouteGegevens[0], fouteGegevens[1], fouteGegevens[2], fouteGegevens[3],
					fouteGegevens[4], fouteGegevens[5], fouteGegevens[6]);
		});

		Assertions.assertEquals("Lucas", gebruiker.getVoornaam());
		Assertions.assertEquals("vdh", gebruiker.getFamilienaam());
		Assertions.assertEquals("lucas@Email.com", gebruiker.getMailadres());
		Assertions.assertEquals("123jb", gebruiker.getGebruikersnaam());
		Assertions.assertEquals(Status.Actief, gebruiker.getStatus());
		Assertions.assertEquals(Type.Gebruiker, gebruiker.getType());
		Assertions.assertNull(gebruiker.getProfielfoto());
	}
}
