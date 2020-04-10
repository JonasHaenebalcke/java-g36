package domein;

import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class PopulateDB {
	public void run() {
        GebruikerDaoJpa gebruikerdao = new GebruikerDaoJpa();
        GebruikerDaoJpa.startTransaction();

//        gebruikerdao.insert(new Gebruiker("Katrien", "Maasens", "katrien.maasen@student.hogent.be", "123456km", Type.Gebruiker, Status.Actief, ""));
//        gebruikerdao.insert(new Gebruiker("Lotte", "koekens", "lotte.koekens@hogent.be", "123478lk", Type.Verantwoordelijke, Status.Actief, "" ));
//        gebruikerdao.insert(new Gebruiker("Tim", "timmers", "tim.timmers@hogent.be", "123478lk", Type.Hoofdverantwoordelijke, Status.Actief, "images/Lucas.png" ));

        GebruikerDaoJpa.commitTransaction();
    }
}
