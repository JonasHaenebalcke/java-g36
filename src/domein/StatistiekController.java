package domein;

import java.util.Arrays;

public class StatistiekController {

	public StatistiekController() {
	}

	public int geefAantalKeerAanwezig(Gebruiker gebruiker) {
		int ret = 0;

		for (GebruikerSessie gs : gebruiker.getGebruikerSessieLijst()) {
			if (gs.isAanwezig())
				ret++;
		}
		return ret;
	}

	public int geefAantalKeerAfwezig(Gebruiker gebruiker) {
		return gebruiker.getAantalKeerAfwezig();
	}

	public int geefAantalAanwezigen(Sessie sessie) {
		int ret = 0;

		for (GebruikerSessie gs : sessie.getGebruikerSessieLijst()) {
			if (gs.isAanwezig())
				ret++;
		}

		return ret;
	}

	public double geefGemiddeldeScore(Sessie sessie) {
		return sessie.geefGemiddeldeScore();
	}
}
