package repository;

import java.util.List;

import domein.Gebruiker;
import domein.*;

public class GebruikerDaoJpa extends GenericDaoJpa<Gebruiker> implements GebruikerDao {

	public GebruikerDaoJpa() {
		super(Gebruiker.class);
	}

}
