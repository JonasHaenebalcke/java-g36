package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

@SuppressWarnings("serial")
@Entity
@Table(name = "Aankondiging")
public class Aankondiging implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AankondidingID")
	public int aankondigingID;

	@Column(name = "Titel")
	public String titel;

	@Column(name = "Tekst")
	public String aankondingingTekst;

	@Column(name = "DatumAangemaakt")
	public LocalDateTime datum;

	@Column(name = "isVerzonden")
	public boolean isVerzonden;

	@ManyToOne
	@JoinColumn(name = "SessieID")
	public Sessie sessie;

	@ManyToOne
	@JoinColumn(name = "publicistID")
	public Gebruiker publicist;

	@Transient
	private SimpleStringProperty publicistProperty; // = new SimpleStringProperty();
	@Transient
	private SimpleStringProperty titelAankondigingProperty; // = new SimpleStringProperty();
	@Transient
	private SimpleStringProperty datumAankondigingProperty; // = new SimpleStringProperty();

	protected Aankondiging() {

	}

	public Aankondiging(String titel, String aankondiging, Sessie sessie, Gebruiker publicist, boolean isVerzonden) {
		setTitel(titel);
		setAankondingingTekst(aankondiging);
		setSessie(sessie);
		setPublicist(publicist);
		setVerzonden(isVerzonden);
		this.datum = LocalDateTime.now();
		setStringProperties();
	}

	private void setSessie(Sessie sessie) {
		if (sessie == null) {
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

	private void setVerzonden(boolean isVerzonden) {
		if (this.isVerzonden) {
			throw new IllegalArgumentException("Aankondiging is al verzonden");
		}
		this.isVerzonden = isVerzonden;
	}

	public String getTitel() {
		return titel;
	}

	private void setTitel(String titel) {
		if (titel == null || titel.isBlank()) {
			throw new IllegalArgumentException("Gelieve de titel in te vullen!");
		}
		this.titel = titel;
	}

	public String getAankondingingTekst() {
		return aankondingingTekst;
	}

	private void setAankondingingTekst(String aankondingingTekst) {
		if (aankondingingTekst == null || aankondingingTekst.isBlank()) {
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

	private void setPublicist(Gebruiker publicist) {
		if (publicist == null) {
			throw new IllegalArgumentException("Gelieve een publicist mee te geven");
		}
		this.publicist = publicist;
	}

	public void wijzigAankondiging(String titel, String aankondigingTekst, boolean isVerzonden) {
		if (this.isVerzonden) {
			throw new IllegalArgumentException("Je kan de aankondiging niet meer aanpassen, want hij is al verzonden");
		} else {
			setTitel(titel);
			setAankondingingTekst(aankondigingTekst);
			setVerzonden(isVerzonden);
			setStringProperties();
		}
	}

	public StringProperty getPublicistProperty() {
		if (publicist == null) {
			setPublicistProperty();
		}
		return publicistProperty;
	}

	public void setPublicistProperty() {
		publicistProperty.set(this.publicist.getFamilienaam() + " " + this.publicist.getVoornaam());
	}

	public StringProperty getTitelAankondigingProperty() {
		if (titelAankondigingProperty == null) {
			setTitelAankondigingProperty();
		}
		return titelAankondigingProperty;
	}

	public void setTitelAankondigingProperty() {
		if (titelAankondigingProperty == null) {
			titelAankondigingProperty = new SimpleStringProperty();
		}
		titelAankondigingProperty.set(getTitel());
	}

	public StringProperty getDatumAankondigingProperty() {
		if (datumAankondigingProperty == null) {
			setDatumAankondigingProperty();
		}
		return datumAankondigingProperty;
	}

	public void setDatumAankondigingProperty() {
		if (datumAankondigingProperty == null) {
			datumAankondigingProperty = new SimpleStringProperty();
		}
		datumAankondigingProperty.set(getDatumAangemaakt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	public void setStringProperties() {
		setDatumAankondigingProperty();
		setTitelAankondigingProperty();
//		setAankondingingTekst();
		setPublicistProperty();
	}

	@Override
	public String toString() {
		return "Aankondiging [aankondigingID=" + aankondigingID + ", titel=" + titel + ", aankondingingTekst="
				+ aankondingingTekst + ", datum=" + datum + ", isVerzonden=" + isVerzonden + ", sessie=" + sessie
				+ ", publicist=" + publicist + "]";
	}

}
