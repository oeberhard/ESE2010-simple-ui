package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;


@Entity
public class Question extends Model implements IConversation {

	@ManyToOne
	public User author;
	@Lob
	public String content;
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	public List<Response> answers;
	public long timestamp;
	public int positivevote, negativevote; 
											

	public Question(String s, User u) {
		author = u;
		content = s;
		answers = new ArrayList<Response>();
		timestamp = System.currentTimeMillis();
		positivevote = 0;
		negativevote = 0;
	}

	public void incPositive() { 
		this.positivevote++;
	}

	public void incNegative() { 
		this.negativevote++;
	}

	public void deleteVote(VoteOutcome t) { 
		if (t.equals(VoteOutcome.positive)) {
			positivevote--;
		} else
			negativevote--;
	}

	public User getOwner() {
		return this.author;
	}

	public String toString() {
		return content;
	}

	public List<Response> getAnswers() {
		return this.answers;
	}

	public int getNegativeVote() {
		return negativevote;
	}

	public int getPositiveVote() {
		return positivevote;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
