package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class AankondigingController {

	private List<Aankondiging> aankondigingLijst;
	private ObservableList<Aankondiging> aankondigingObservableList;
	private GenericDao<Aankondiging> aankondigingRepo;
	private Aankondiging gekozenAankondiging;

	public AankondigingController() {
		setAankondigingRepo(new GenericDaoJpa<Aankondiging>(Aankondiging.class));
	}

	public void setAankondigingRepo(GenericDao<Aankondiging> aankondigingMock) {
		this.aankondigingRepo = aankondigingMock;
	}

	public List<Aankondiging> geefAankondigingen() {
		if (aankondigingLijst == null) {
			aankondigingLijst = aankondigingRepo.findAll();

		}
		return aankondigingLijst;
	}

	public ObservableList<Aankondiging> geefAankondigingObservableList() {
		if (aankondigingObservableList == null) {
			aankondigingObservableList = FXCollections.observableArrayList(geefAankondigingen());
		}
		return aankondigingObservableList;
	}

	public void setGekozenAankondiging(Aankondiging aankondiging) {
		this.gekozenAankondiging = aankondiging;
	}

	public void wijzigAankondiging(String titel, String aankondiging, boolean isVerzonden) {
		try {

			gekozenAankondiging.wijzigAankondiging(titel, aankondiging, isVerzonden);
			GenericDaoJpa.startTransaction();
			aankondigingRepo.update(gekozenAankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het wijzigen van de aankondiging");
		}

	}

	public void voegAankondigingToe(String titel, String aankondiging, boolean isVerzonden, Sessie sessie,
			Gebruiker publicist) {
		try {
			if (!(publicist.getStatus().equals(Status.Actief) || publicist.getType().equals(TypeGebruiker.Gebruiker))) {
				Aankondiging newAankondiging = new Aankondiging(titel, aankondiging, sessie, publicist, isVerzonden);

				aankondigingLijst.add(newAankondiging);
				aankondigingObservableList.add(newAankondiging);
				GenericDaoJpa.startTransaction();
				aankondigingRepo.insert(newAankondiging);
				GenericDaoJpa.commitTransaction();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
			// throw new IllegalArgumentException("Er ging iets fout bij het aanmaken van de
			// aankondiging");
		}
	}

	public void verwijderAankondiging(int index) {
		try {
			Aankondiging aankondiging = aankondigingLijst.get(index);
			if (aankondiging.isVerzonden()) {
				throw new IllegalArgumentException("Aankondiging kan niet verwijderd worden omdat hij al verzonden is");
			}
			aankondigingLijst.remove(aankondiging);
			aankondigingObservableList.remove(aankondiging);
			GenericDaoJpa.startTransaction();
			aankondigingRepo.delete(aankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het verwijderen van een aankondiging");
		}
	}

	public void verzendAankondiging() {
		try {
			if (this.gekozenAankondiging != null) {
				if (gekozenAankondiging.isVerzonden()) {
					throw new IllegalArgumentException("Je hebt deze aankondiging al verzonden");
				} 
				if(!gekozenAankondiging.getSessie().isInschrijvingenOpen()) {
					throw new IllegalAccessException("aankondiging kan niet worden verzonden want de sessie is niet open voor ingeschrijving");
				}
				
//				List<String> geadresseerden = new ArrayList<>();

				String naar = "audrey.behiels@student.hogent.be";
				String van = "ProjectITLab@outlook.com";// gc.getIngelogdeVerantwoordelijke().getMailadres();
				String host = "localhost";
				Properties properties = new Properties();

				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.host", "smtp-mail.outlook.com");
				properties.put("mail.smtp.port", "587");

				Session sessie = Session.getDefaultInstance(properties, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("ProjectITLab@outlook.com", "ProjectenSem2");
					}
				});
				try {
//					Address[] cc  = new Address[gekozenAankondiging.sessie.getGebruikerSessieLijst().size()];
//						 Address[] cc = new Address[2];
//					cc[0] = new InternetAddress(naar);
//					cc[1] = new InternetAddress("jule.dekyvere@student.hogent.be");
//					for (GebruikerSessie gs : gekozenAankondiging.sessie.getGebruikerSessieLijst()) {
//						int teller=0;
//	//					if(!geadresseerden.contains(gs.getIngeschrevene().getMailadres())) {
//	//						geadresseerden.add(gs.getIngeschrevene().getMailadres());
//							cc[teller] = new InternetAddress(gs.getIngeschrevene().getMailadres());
//							teller++;
//	//					}
//					}
					MimeMessage message = new MimeMessage(sessie);
					message.setFrom(new InternetAddress(van));
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(naar));
					// message.addRecipient(Message.RecipientType.TO, new InternetAddress("audrey.beh@student.hogent.be")); // Om te testen 
					// message.addRecipient(Message.RecipientType.TO, new InternetAddress("test@student.hogent.be"));// Om te testen 

//					message.setRecipients(Message.RecipientType.CC, cc);

//					message.setSubject("Onderwerp titel test");
//					message.setText("Dit bericht is een test.");

					message.setSubject(this.gekozenAankondiging.getTitel());
					message.setText(this.gekozenAankondiging.getAankondingingTekst());

					Transport.send(message);
					System.out.println("bericht goed verzonden..");
//					gekozenAankondiging.isVerzonden = true;
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het verzenden van de aankondiging");
		}
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}

}
