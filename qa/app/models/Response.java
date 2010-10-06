package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;


@Entity
public class Response extends Model implements IConversation {

	@ManyToOne
	public User author;
	@OneToOne
	public Question question;
	@Lob
	public String content;
	public long timestamp;
	public int positivevote, negativevote; // separate counts for up'n'down
											

	public Response(String s, Question q, User u) {
		content = s;
		author = u;
		question = q;
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

	public Question getQuestion() {
		return this.question;
	}

	public String toString() {
		return content;
	}

	public int getPositiveVote() {
		return positivevote;
	}

	public int getNegativeVote() {
		return negativevote;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
