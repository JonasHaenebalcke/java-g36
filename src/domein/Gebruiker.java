package domein;

import java.util.List;

public class Gebruiker {

	private Status status;
	private Type type;
	private String gebruikerID;
	private String voornaam;
	private String familienaam;
	private String mailadres;
	private String gebruikersnaam;
	private String profielfoto;

	public Gebruiker() {
		// TODO - implement Gebruiker.Gebruiker
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 */
	public Gebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, Type type,
			Status status, String profielfoto) {
		// TODO - implement Gebruiker.Gebruiker
		throw new UnsupportedOperationException();
	}

	public Status getStatus() {
		return status;
	}

	private void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	private void setType(Type type) {
		this.type = type;
	}

	public String getGebruikerID() {
		return gebruikerID;
	}

	private void setGebruikerID(String gebruikerID) {
		this.gebruikerID = gebruikerID;
	}

	public String getVoornaam() {
		return voornaam;
	}

	private void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getFamilienaam() {
		return familienaam;
	}

	private void setFamilienaam(String familienaam) {
		this.familienaam = familienaam;
	}

	public String getMailadres() {
		return mailadres;
	}

	private void setMailadres(String mailadres) {
		this.mailadres = mailadres;
	}

	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	private void setGebruikersnaam(String gebruikersnaam) {
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getProfielfoto() {
		return profielfoto;
	}

	private void setProfielfoto(String profielfoto) {
		this.profielfoto = profielfoto;
	}

	/**
	 * 
	 * @param voornaam
	 * @param familienaam
	 * @param mailadres
	 * @param gebruikersnaam
	 * @param profielfoto
	 */
	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam,
			String type, String status, String profielfoto) {
		// TODO - implement Gebruiker.wijzigGebruiker
		throw new UnsupportedOperationException();
	}

	public List<String> detailsGebruiker() {
		// TODO - implement Gebruiker.detailsGebruiker
		throw new UnsupportedOperationException();
	}

}