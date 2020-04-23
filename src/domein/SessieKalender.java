package domein;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
@Entity
@Table(name = "SessieKalender")
public class SessieKalender {

	@Transient
	private Collection<Sessie> sessieLijst;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int sessieKalenderID;
	@Column(name = "startDate")
	private LocalDate startDatum;
	@Column(name = "eindDate")
	private LocalDate eindDatum;
	
	@Transient
	private SimpleStringProperty startDatumProperty = new SimpleStringProperty();
	@Transient
    private SimpleStringProperty eindDatumProperty = new SimpleStringProperty();
    


	protected SessieKalender() {
		sessieLijst = new ArrayList<Sessie>();
	}
	
	public SessieKalender(LocalDate startDate, LocalDate eindDate) {
		this();
		 if(eindDate.getYear() - startDate.getYear() != 1 ) {
	            throw new IllegalArgumentException("SessieKalender moet op eenvolgende jaren hebben ");
		    }
		setStartDate(startDate);
		setEindDate(eindDate);
		
	}
	
	public void setStringProperties(LocalDate startDate, LocalDate eindDate) {
		setStartDatum(startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setEindDatum(eindDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}
	
	
	private void setStartDatum(String datum) {
        startDatumProperty.set(datum);
    }

    public String getStartDatum() {
        return startDatumProperty.get();
    }

    public StringProperty startDatumProperty() {
        return startDatumProperty;
    }

    private void setEindDatum(String datum) {
        eindDatumProperty.set(datum);
    }
    
    public String eindDatum() {
        return eindDatumProperty.get();
    }

    public StringProperty eindDatumProperty() {
        return eindDatumProperty;
    }


	

	public int getSessieKalenderID() {
		return this.sessieKalenderID;
	}

	public void setSessieKalenderID(int id) {
		this.sessieKalenderID = id;
	}

	public LocalDate getStartDate() {
		return this.startDatum;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDatum = startDate;
		if(startDate.isAfter(LocalDate.now()))
			this.startDatum = startDate;
		else
			throw new IllegalArgumentException("De startdatum moet in de toekomst liggen.");
	}

	public LocalDate getEindDate() {
		return this.eindDatum;
	}

	public void setEindDate(LocalDate eindDate) {
		this.eindDatum = eindDate;
		if(eindDate.isAfter(LocalDate.now()) && eindDate.isAfter(startDatum))
			this.eindDatum = eindDate;
		else
			throw new IllegalArgumentException("De einddatum moet in de toekomst liggen en na de startdatum komen.");
	}

	public void wijzigSessieKalender(LocalDate startDate, LocalDate eindDate) {
		if(startDate.isAfter(eindDate)) {
			throw new IllegalArgumentException("Startdatum kan niet na einddatum liggen");
		}
	    if(eindDate.getYear() - startDate.getYear() != 1 ) {
            throw new IllegalArgumentException("SessieKalender moet op eenvolgende jaren hebben ");
	    }
		setStartDate(startDate);
		setEindDate(eindDate);
	}

	public void voegSessieToe(Sessie sessie) {
		if(sessieLijst.contains(sessie))
			throw new IllegalArgumentException("De gegeven sessie komt reeds voor in deze sessiekalender.");
		else
			sessieLijst.add(sessie);
	}

	public void verwijderSessie(Sessie sessie) {
		if(sessieLijst.contains(sessie)) 
			sessieLijst.remove(sessie);
		else
			throw new NullPointerException("De gegeven sessie komt niet voor in deze sessiekalender.");
	}

	public Collection<Sessie> geefSessiesMaand(int maand) {
		//ObservableList<Sessie> sessies; //kan nog niet aan "maand" van sessie.
		/*sessieLijst.forEach( s -> {
			if()
		});*/
//		if(sessieLijst.isEmpty() || sessieLijst == null)
//			throw new NullPointerException("Sessie lijst is leeg.");
//		else
			return sessieLijst;
	}

	@Override
	public String toString() {
		return "startDate=" + startDatum + ", eindDate=" + eindDatum;
	}
	
}
