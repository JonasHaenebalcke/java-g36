package domein;

public class GebruikerSessie {

	private boolean aanwezig;
	private String gebruikerID;
	private int sessieID;
	private Sessie sessie;
	private Gebruiker ingeschrevene;

	public boolean isAanwezig() {
		return aanwezig;
	}

	public String getGebruikerID() {
		return gebruikerID;
	}

	public int getSessieID() {
		return sessieID;
	}

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
