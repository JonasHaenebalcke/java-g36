package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Aankondiging")
public class Aankondiging implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="AankondidingID")
	public int aankondigingID;
	
	@Column(name="Titel")
	public String titel;
	
	@Column(name="Tekst")
	public String aankondingingTekst;
	
	@Column(name="DatumAangemaakt")
	public LocalDateTime datum;
	
	@Column(name="isVerzonden")
	public boolean isVerzonden;
	
	@ManyToOne
	@JoinColumn(name="SessieID")
    public Sessie sessie;
	
	@ManyToOne
	@JoinColumn(name="publicistID")
	public Gebruiker publicist;
	
	
	protected Aankondiging() {
		
	}
	
	public Aankondiging(String titel, String aankondiging, Sessie sessie, Gebruiker publicist, boolean isVerzonden) {
		setTitel(titel);
		setAankondingingTekst(aankondiging);
		setSessie(sessie);
		setPublicist(publicist);
		setVerzonden(isVerzonden);
		this.datum = LocalDateTime.now();
	}

	private void setSessie(Sessie sessie) {
		if(sessie==null) {
			throw new IllegalArgumentException("Sessie moet ingevuld zijn");
		}
		this.sessie = sessie;
		
	}

	public Sessie getSessie() {
		return sessie;
	}
	public boolean isVerzonden() {
		return isVerzonden;
	}

	public void setVerzonden(boolean isVerzonden) {
		if(this.isVerzonden) {
			throw new IllegalArgumentException("Aankondiging is al verzonden");
		}
		this.isVerzonden = isVerzonden;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		if(titel == null || titel.isBlank()) {
			throw new IllegalArgumentException("Gelieve de titel in te vullen!");
		}
		this.titel = titel;
	}

	public String getAankondingingTekst() {
		return aankondingingTekst;
	}

	public void setAankondingingTekst(String aankondingingTekst) {
		if(aankondingingTekst == null || aankondingingTekst.isBlank()) {
		throw new IllegalArgumentException("Gelieve je aankondiging in te vullen!");
	}
		this.aankondingingTekst = aankondingingTekst;
	}
	
	public LocalDateTime getDatumAangemaakt() {
		return datum;
	}

	public Gebruiker getPublicist() {
		return publicist;
	}

	public void setPublicist(Gebruiker publicist) {
		if(publicist==null) {
			throw new IllegalArgumentException("Gelieve een publicist mee te geven");
		}
		this.publicist = publicist;
	}

	@Override
	public String toString() {
		return "Aankondiging [titel=" + titel + ", aankondingingTekst=" + aankondingingTekst + "]";
	}
	
	
}
