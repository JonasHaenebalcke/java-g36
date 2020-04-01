package domein;

import java.util.List;

public class Gebruiker {

	GebruikerRepository gebruikerrR;
	private Status status;
	private Type type;
	private int gebruikerID;
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
	public Gebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, String profielfoto) {
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
	public void wijzigGebruiker(String voornaam, String familienaam, String mailadres, String gebruikersnaam, String profielfoto) {
		// TODO - implement Gebruiker.wijzigGebruiker
		throw new UnsupportedOperationException();
	}

	public List<String> detailsGebruiker() {
		// TODO - implement Gebruiker.detailsGebruiker
		throw new UnsupportedOperationException();
	}

}