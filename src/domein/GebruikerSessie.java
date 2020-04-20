package domein;

public class GebruikerSessie {

	private boolean aanwezig;
	private String gebruikerID;
	private int sessieID;
	private Sessie sessie;

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

	public Gebruiker getIngeschrevenen() {
		return ingeschrevenen;
	}

	private Gebruiker ingeschrevenen;

	protected GebruikerSessie() {
	}

	public GebruikerSessie(Sessie sessie, Gebruiker ingeschrevenen) {
		this.aanwezig = false;
		this.sessie = sessie;
		this.ingeschrevenen = ingeschrevenen;
	}

	public void wijzigAanwezigheid(boolean aanwezig) {
		this.aanwezig = aanwezig;
	}
}
