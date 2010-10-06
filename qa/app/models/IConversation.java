package models;


public interface IConversation {

	public abstract void incPositive();

	public abstract void incNegative();

	public abstract void deleteVote(VoteOutcome t);

	public abstract int getNegativeVote();

	public abstract int getPositiveVote();
}