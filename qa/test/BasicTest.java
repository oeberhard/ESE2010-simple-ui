import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import models.Response;
import models.IConversation;
import models.Question;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	Question haraldQuestion;
	Response carolAnswer, charlyAnswer;
	User harald, carol, charly;

	@Before
	public void setUp() {
		Fixtures.deleteAll();
		harald = new User("harald@gmail.com", "secret", "harald").save();
		carol = new User("carol@gmail.com", "asdfgh", "carol").save();
		charly = new User("charly@gmail.com", "mymom", "charly").save();
		haraldQuestion = harald.ask("My new blog is cool?");
		carolAnswer = carol.answer("Nice post", haraldQuestion);
		charlyAnswer = charly.answer("Yeah", haraldQuestion);
	}

	@Test
	public void createAndRetrieveUser() {
		User x = User.find("byEmail", "harald@gmail.com").first();
		assertNotNull(x);
		assertEquals("harald", x.name);
	}

	@Test
	public void tryConnectAsUser() {
		assertNotNull(User.connect("harald@gmail.com", "secret"));
		assertNull(User.connect("harald@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));
	}

	@Test
	public void createQuestion() {
		// Test that the question has been created
		assertEquals(1, Question.count());
		// Retrieve all questions created by Bob
		List<Question> haraldPosts = Question.find("byOwner", harald).fetch();
		assertEquals(1, haraldPosts.size());

		Question firstPost = haraldPosts.get(0);
		assertNotNull(firstPost);
		assertEquals(harald, firstPost.author);
		assertEquals("My new blog is cool?", firstPost.toString());
		assertNotNull(firstPost.timestamp);
	}

	@Test
	public void answerQuestion() {
		List<Response> haraldQuestionAnswers = Response
				.find("byQuestion", haraldQuestion).fetch();
		assertEquals(2, haraldQuestion.answers.size());
		assertEquals(2, haraldQuestionAnswers.size());
		// assertEquals(2, bob.getAnswers().size());

		Response firstComment = haraldQuestionAnswers.get(0);
		assertNotNull(firstComment);
		assertEquals(carol, firstComment.author);
		assertEquals("Nice post", firstComment.content);
		assertNotNull(firstComment.timestamp);

		Response secondComment = haraldQuestionAnswers.get(1);
		assertNotNull(secondComment);
		assertEquals(charly, secondComment.author);
		assertEquals("Yeah", secondComment.content);
		assertNotNull(secondComment.timestamp);
	}

	@Test
	public void useTheCommentsRelation() {
		// Count things
		assertEquals(3, User.count());
		assertEquals(1, Question.count());
		assertEquals(2, Response.count());

		// Retrieve Bob's post
		Question haraldPost = Question.find("byOwner", harald).first();
		assertNotNull(haraldPost);

		// Navigate to comments
		assertEquals(2, haraldPost.answers.size());
		assertEquals(carol, haraldPost.answers.get(0).author);

		// Delete the post
		haraldPost.delete();

		// Check that all comments have been deleted
		assertEquals(3, User.count());
		assertEquals(0, Question.count());
		assertEquals(0, Response.count());
	}

	@Test
	public void fullTest() {
		Fixtures.deleteAll();
		Fixtures.load("TestData.yml");

		// Count things
		assertEquals(3, User.count());
		assertEquals(2, Question.count());
		assertEquals(2, Response.count());

		// Try to connect as users
		assertNotNull(User.connect("harald@gmail.com", "secret"));
		assertNotNull(User.connect("charly@gmail.com", "mymom"));
		assertNotNull(User.connect("george@gmail.com", "asdfgh"));

		assertNull(User.connect("harald@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));

		// Find all of Bob's posts
		List<Question> haraldQuestions = Question.find("owner.email",
				"harald@gmail.com").fetch();
		assertEquals(2, haraldQuestions.size());

		// Find all comments related to Bob's posts
		List<Response> haraldComments = Response.find("question.owner.email",
				"harald@gmail.com").fetch();
		assertEquals(2, haraldComments.size());

		// Tests voting
		IConversation bobQuestion = Question.find("owner.email",
				"harald@gmail.com").first();
		User bob = User.find("byFullname", "harald").first();
		assertNotNull(haraldQuestion);

	}

}
