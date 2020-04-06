package repository;

import java.util.List;

import domein.*;

public class SessieKalenderDaoJpa extends GenericDaoJpa<SessieKalender> implements SessieKalenderDao {

	public SessieKalenderDaoJpa() {
		super(SessieKalender.class);
	}

}
