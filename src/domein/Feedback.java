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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "GebruikerSessie")
public class Feedback implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FeedbackID")
    public int feedbackID;
	@Column(name = "Tekst")
	public String tekst;
	@Column(name = "TimeWritten")
    public LocalDateTime timeWritten;
	//AuteurId
	@ManyToOne
	@JoinColumn(name="AuteurId")
    public Gebruiker auteur;
	@ManyToOne
	@JoinColumn(name="SessieID")
    public Sessie sessie;
    @Column(name = "Score")
    public int score;

    public Feedback() { }

    public Feedback(Gebruiker auteur, Sessie sessie, String content, int score)
    {
    	this.sessie = sessie;
    	setAuteur(auteur);
        this.tekst = content;
        this.timeWritten = LocalDateTime.now();
        setScore(score);
    }
    
//    public Feedback(String content, int score)
//    {
//        this.tekst = content;
//        this.timeWritten = LocalDateTime.now();
//        this.score = score;
//    }
    
    public int getFeedbackID() {
		return feedbackID;
	}
    
	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public Gebruiker getAuteur() {
		return auteur;
	}
	
	public void setAuteur(Gebruiker auteur) {
    	if(auteur == null)
    		throw new IllegalArgumentException("Gelieve een auteur mee te geven.");
        this.auteur = auteur;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		if(score < 0 || score > 5)
			throw new IllegalArgumentException("Gelieve een geldige score tussen 0 en 5 mee te geven.");
		this.score = score;
	}

	public LocalDateTime getTimeWritten() {
		return timeWritten;
	}
	
	public void wijzigFeedback(String content, int score) {
		this.tekst = content;
		setScore(score);
	}    
}
