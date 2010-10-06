package controllers;

import java.util.List;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

import models.Question;

public class Application extends Controller {

	@Before
	static void addDefaults() {
		renderArgs.put("blogTitle", Play.configuration
				.getProperty("blog.title"));
		renderArgs.put("blogBaseline", Play.configuration
				.getProperty("blog.baseline"));
	}

	public static void index() {
		Question frontPost = Question.find("order by timestamp desc").first();
		List<Question> olderPosts = Question.find("order by timestamp desc")
				.from(1).fetch(10);
		System.out.println(olderPosts.size());
		render(frontPost, olderPosts);
	}

}