package VertextProject.Vertexproject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.*;

public class TextAnalyzerService extends AbstractVerticle {

	private Map<String, Integer> wordCounts = new HashMap<>();
	private List<String> words = new ArrayList<>();

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		Router router = Router.router(vertx);

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		// POST requests
		router.route().handler(BodyHandler.create());

		// API routes
		router.post("/analyze").handler(this::analyzeText);

		// Code to start the server
		vertx.createHttpServer().requestHandler(router).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});
	}

	private String getClosestByValue(String text) {
		String closestWord = null;
		int closestValueDiff = Integer.MAX_VALUE;

		for (String word : words) {
			int valueDiff = Math.abs(getWordValue(word) - getWordValue(text));
			if (valueDiff < closestValueDiff || (valueDiff == closestValueDiff && word.compareTo(closestWord) > 0)) {
				closestValueDiff = valueDiff;
				closestWord = word;
			}
		}

		return closestWord;
	}

	private String getClosestByLexical(String text) {
		String closestWord = null;
		int closestLexicalDiff = Integer.MAX_VALUE;

		for (String word : words) {
			int lexicalDiff = Math.abs(word.compareToIgnoreCase(text));
			if (lexicalDiff < closestLexicalDiff
					|| (lexicalDiff == closestLexicalDiff && word.compareTo(closestWord) < 0)) {
				closestLexicalDiff = lexicalDiff;
				closestWord = word;
			}
		}

		return closestWord;
	}

	private int getWordValue(String word) {
		int value = 0;
		for (char c : word.toLowerCase().toCharArray()) {
			value += c - 'a' + 1;
		}
		return value;
	}

	private void analyzeText(RoutingContext routingContext) {
		JsonObject requestBody = routingContext.getBodyAsJson();
		String text = requestBody.getString("text").toLowerCase();

		updateWordCounts(text);

		String closestByValue = getClosestByValue(text);
		String closestByLexical = getClosestByLexical(text);

		JsonObject responseBody = new JsonObject().put("value", closestByValue).put("lexical", closestByLexical);

		HttpServerResponse response = routingContext.response();
		response.putHeader("content-type", "application/json");
		response.end(responseBody.encode());
	}

	private void updateWordCounts(String text) {

		String[] words = text.split("\\s+");

		for (String word : words) {
			String cleanedWord = word.replaceAll("[^a-zA-Z]", "");

			if (cleanedWord.isEmpty()) {
				continue;
			}

			int count = wordCounts.getOrDefault(cleanedWord, 0);
			wordCounts.put(cleanedWord, count + 1);

			if (!this.words.contains(cleanedWord)) {
				this.words.add(cleanedWord);
			}
		}
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new TextAnalyzerService());
	}

}
