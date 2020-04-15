package domein;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class PopulateDB {
	public void run() {
		GebruikerDaoJpa gebruikerdao = new GebruikerDaoJpa();
		GebruikerDaoJpa.startTransaction();

		try {
			gebruikerdao.insert(new Gebruiker("Katrien", "Maasens", "katrien.maasen@student.hogent.be", "123456km",
					TypeGebruiker.Gebruiker, Status.Actief, "", "123"));
			gebruikerdao.insert(new Gebruiker("Lotte", "koekens", "lotte.koekens@hogent.be", "123478lk",
					TypeGebruiker.Verantwoordelijke, Status.Actief, "", "123"));
			gebruikerdao.insert(new Gebruiker("Tim", "timmers", "tim.timmers@hogent.be", "123468lk",
					TypeGebruiker.Hoofdverantwoordelijke, Status.Actief, "images/Lucas.png", "123"));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GebruikerDaoJpa.commitTransaction();
	}
}
