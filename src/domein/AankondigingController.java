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

	public List<Aankondiging> geefAankondigingen(Sessie sessie) {
		if (aankondigingLijst == null) {
			aankondigingLijst = aankondigingRepo.findAll();
		}
		List<Aankondiging> aankondigingen = new ArrayList<Aankondiging>();
		for (Aankondiging a : aankondigingLijst) {
			if (sessie.equals(a.sessie)) {
				aankondigingen.add(a);
			}
		}
		return aankondigingen;
	}

	public ObservableList<Aankondiging> geefAankondigingObservableList() {
		if (aankondigingObservableList == null || aankondigingObservableList.isEmpty()) {
			aankondigingObservableList = FXCollections.observableArrayList(geefAankondigingen());
		}
		return aankondigingObservableList;
	}

	public ObservableList<Aankondiging> geefAankondigingObservableList(Sessie sessie) {
		if (aankondigingObservableList == null || aankondigingObservableList.isEmpty()) {
			aankondigingObservableList = FXCollections.observableArrayList(geefAankondigingen(sessie));
		}
		return aankondigingObservableList;
	}

	public void setGekozenAankondiging(Aankondiging aankondiging) {
		this.gekozenAankondiging = aankondiging;
	}

	public void wijzigAankondiging(String titel, String aankondiging) {
		gekozenAankondiging.wijzigAankondiging(titel, aankondiging);
		try {
			GenericDaoJpa.startTransaction();
			aankondigingRepo.update(gekozenAankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het wijzigen van de aankondiging");
		}
	}

	public void voegAankondigingToe(String titel, String aankondiging, boolean isVerzonden, Sessie sessie,
			Gebruiker publicist) {
		Aankondiging newAankondiging = new Aankondiging(titel, aankondiging, sessie, publicist, isVerzonden);
		setGekozenAankondiging(newAankondiging);
		aankondigingLijst.add(newAankondiging);
		aankondigingObservableList.add(newAankondiging);
		try {
			GenericDaoJpa.startTransaction();
			aankondigingRepo.insert(newAankondiging);
			GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new IllegalArgumentException("Er ging iets fout bij het aanmaken van de aankondiging");
		}
	}

	public void verwijderAankondiging(Aankondiging aankondiging) {
		if (aankondiging.isVerzonden()) {
			throw new IllegalArgumentException("Aankondiging kan niet verwijderd worden omdat hij al verzonden is");
		}
		try {
			aankondigingLijst.remove(aankondiging);
			aankondigingObservableList.remove(aankondiging);
			setGekozenAankondiging(null);
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
				if (!gekozenAankondiging.getSessie().isInschrijvingenOpen()) {
					throw new IllegalAccessException(
							"Aankondiging kan niet worden verzonden \nomdat de sessie niet open is voor inschrijvingen");
				}

				List<String> geadresseerden = new ArrayList<>();
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
					for (GebruikerSessie gs : gekozenAankondiging.sessie.getGebruikerSessieLijst()) {
						geadresseerden.add(gs.getIngeschrevene().getMailadres());
					}
					Address[] cc = new Address[gekozenAankondiging.sessie.getGebruikerSessieLijst().size()];
					int teller = 0;
					for (GebruikerSessie gs : gekozenAankondiging.sessie.getGebruikerSessieLijst()) {
						geadresseerden.add(gs.getIngeschrevene().getMailadres());
						cc[teller] = new InternetAddress(gs.getIngeschrevene().getMailadres());
						teller++;
					}

					MimeMessage message = new MimeMessage(sessie);
					message.setFrom(new InternetAddress(van));
					message.setRecipients(Message.RecipientType.CC, cc);

					message.setSubject(this.gekozenAankondiging.getTitel());
					message.setText(this.gekozenAankondiging.getAankondingingTekst());
					this.gekozenAankondiging.verzend();
					Transport.send(message);
					GenericDaoJpa.startTransaction();
					aankondigingRepo.update(this.gekozenAankondiging);
					GenericDaoJpa.commitTransaction();
					gekozenAankondiging.isVerzonden = true;
					setGekozenAankondiging(null);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
//			throw new IllegalArgumentException("Er ging iets fout bij het verzenden van de aankondiging");
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void close() {
		GenericDaoJpa.closePersistency();
	}

}
