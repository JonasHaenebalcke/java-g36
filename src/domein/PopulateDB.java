package domein;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Date;

import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;
import repository.SessieKalenderDao;
import repository.SessieKalenderDaoJpa;

public class PopulateDB {
	public void run() {
//		GebruikerDaoJpa gebruikerdao = new GebruikerDaoJpa();
//		GebruikerDaoJpa.startTransaction();
//
//		try {
//			gebruikerdao.insert(new Gebruiker("Katrien", "Maasens", "katrien.maasen@student.hogent.be", "123456km",
//					TypeGebruiker.Gebruiker, Status.Actief, "", "123"));
//			gebruikerdao.insert(new Gebruiker("Lotte", "koekens", "lotte.koekens@hogent.be", "123478lk",
//					TypeGebruiker.Verantwoordelijke, Status.Actief, "", "123"));
//			gebruikerdao.insert(new Gebruiker("Tim", "timmers", "tim.timmers@hogent.be", "123468lk",
//					TypeGebruiker.Hoofdverantwoordelijke, Status.Actief, "images/Lucas.png", "123"));
//		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		GebruikerDaoJpa.commitTransaction();
		
		
		
		SessieKalenderDaoJpa skdao = new SessieKalenderDaoJpa();
		SessieKalenderDaoJpa.startTransaction();
		try {
			
//			SessieKalender s1 = new SessieKalender(LocalDate.of(2021, 9, 25), LocalDate.of(2022, 6, 15));
//			SessieKalender s2 = new SessieKalender(LocalDate.of(2022, 9, 25), LocalDate.of(2023, 6, 15));
//			SessieKalender s3 = new SessieKalender(LocalDate.of(2023, 9, 25), LocalDate.of(2024, 6, 15));
			
//		skdao.insert(s1);
//		skdao.insert(s2);
//		skdao.insert(s3);
		} catch(Exception e) {
			System.err.println(e.toString());
		}
		SessieKalenderDaoJpa.commitTransaction();
	}
}
