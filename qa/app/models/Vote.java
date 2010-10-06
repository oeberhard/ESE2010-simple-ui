package models;

import play.db.jpa.Model;



// @Entity
public class Vote extends Model {

	public VoteOutcome sign;
	// @ManyToOne
	public User owner;
	// @ManyToOne
	public IConversation item;
	private long timestamp;

	public Vote(VoteOutcome t, User u, IConversation item) {
		sign = t;
		owner = u;
		this.item = item;
		timestamp = System.currentTimeMillis();
		setToItem();
	}

	private void setToItem() {
		if (sign.equals(VoteOutcome.positive))
			this.item.incPositive();
		else
			this.item.incNegative();
	}

	public IConversation getItem() {
		return item;
	}

	public VoteOutcome getSign() {
		return sign;
	}

}
