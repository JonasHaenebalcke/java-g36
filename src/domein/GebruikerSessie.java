package domein;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

//@Entity
public class GebruikerSessie {
	
	@Column(name = "Aanwezig")
	private boolean aanwezig;
//	@Id
//	@Column(name = "GebruikerID")
//	private String gebruikerID;
//	@Id
//	@Column(name = "SessieID")
//	private int sessieID;
	@EmbeddedId
	GebruikerSessieId id;
	
	@ManyToOne
	@JoinColumn(name="SessieID")
	private Sessie sessie;
	@Transient
	private Gebruiker ingeschrevene;

	public boolean isAanwezig() {
		return aanwezig;
	}

//	public String getGebruikerID() {
//		return gebruikerID;
//	}
//
//	public int getSessieID() {
//		return sessieID;
//	}

	public Sessie getSessie() {
		return sessie;
	}

	public Gebruiker getIngeschrevene() {
		return ingeschrevene;
	}

	protected GebruikerSessie() {
	}

	public GebruikerSessie(Sessie sessie, Gebruiker ingeschrevenen) {
		this.aanwezig = false;
		this.sessie = sessie;
		this.ingeschrevene = ingeschrevenen;
	}

	public void wijzigAanwezigheid(boolean aanwezig) {
		this.aanwezig = aanwezig;
	}
}

@Embeddable
class GebruikerSessieId {
    int sessieID;
    String gebruikerId;
}
