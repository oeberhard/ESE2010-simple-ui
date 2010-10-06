package models;

import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.Entity;

import play.db.jpa.Model;


@Entity
public class User extends Model {

	public String email;
	public String password;
	public String name;
	public ArrayList<Question> questions;
	public ArrayList<Response> answers;
	public ArrayList<Vote> votes;

	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.name = fullname;
		questions = new ArrayList<Question>();
		answers = new ArrayList<Response>();
		votes = new ArrayList<Vote>();
	}


	public void deleteUser() {
		Iterator<Vote> iterator;
		iterator = votes.iterator();
		while (iterator.hasNext()) {
			Vote v = iterator.next();
			v.getItem().deleteVote(v.getSign());
		}
		votes = null;
		questions = null;
		answers = null;
		this.name = null;
	}

	public Question ask(String s) {
		Question q = new Question(s, this).save();
		this.questions.add(q);
		this.save();
		return q;
	}

	public Response answer(String s, Question q) {
		Response a = new Response(s, q, this).save();
		this.answers.add(a);
		q.getAnswers().add(a);
		this.save();
		return a;
	}

	public void voteUp(IConversation c) {
		Vote vote = new Vote(VoteOutcome.positive, this, c);
		this.votes.add(vote);
		c.incPositive();
	}

	public void voteDown(IConversation c) {
		Vote vote = new Vote(VoteOutcome.negative, this, c);
		this.votes.add(vote);
		c.incNegative();
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}

	public String getName() {
		return name;
	}

	public Question getQuestion(Question q) {
		int i = questions.indexOf(q);
		return questions.get(i);
	}

	public ArrayList<Question> getQuestions() {
		return this.questions;
	}

	public ArrayList<Response> getAnswers() {
		return this.answers;
	}

	public ArrayList<Vote> getVotes() {
		return this.votes;
	}

}
