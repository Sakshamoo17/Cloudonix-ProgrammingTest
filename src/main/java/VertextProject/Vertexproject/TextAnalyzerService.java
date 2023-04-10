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
	
	private static final int HTTP_PORT = 8080;
	

    @Override
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        // Enable reading the request body for all routes
        router.route().handler(BodyHandler.create());

        // Define the route for analyzing text
        router.post("/analyze").handler(this::analyzeText);

        // Serve static files in the "public" directory
        router.route().handler(StaticHandler.create("public"));

        // Create the HTTP server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(HTTP_PORT, ar -> {
                    if (ar.succeeded()) {
                        System.out.println("Server started on port " + HTTP_PORT);
                        future.complete();
                    } else {
                        System.out.println("Failed to start server: " + ar.cause());
                        future.fail(ar.cause());
                    }
                });
    }

	private void analyzeText(RoutingContext context) {
	    // Get the text from the request
	    String text = context.getBodyAsJson().getString("text");
	    String[] words = {"apple", "banana", "cherry", "date", "elderberry"};

	    // Convert the text to lowercase
	    String lowercaseText = text.toLowerCase();

	    // Find the closest matching word based on character value
	    String closestValueWord = findClosestValueWord(lowercaseText, words);

	    // Find the closest matching word based on lexical order
	    String closestLexicalWord = findClosestLexicalWord(lowercaseText, words);

	    // Return the response
	    JsonObject response = new JsonObject();
	    if (closestValueWord == null || closestLexicalWord == null) {
	        // No words found to match against
	        response.putNull("value");
	        response.putNull("lexical");
	    } else {
	        response.put("value", closestValueWord);
	        response.put("lexical", closestLexicalWord);
	    }
	    context.response().putHeader("content-type", "application/json").end(response.encode());
	}

	private String findClosestValueWord(String text,String[] words) {
	    String closestWord = null;
	    int closestDistance = Integer.MAX_VALUE;
	    int largestValue = 0;
	    
		for (String word : words) {
	        // Calculate the distance between the lowercaseText and the word based on character values
	        int distance = Math.abs(getCharacterValue(text) - getCharacterValue(word.toLowerCase()));
	        if (distance < closestDistance || (distance == closestDistance && getCharacterValue(word.toLowerCase()) > largestValue)) {
	            closestDistance = distance;
	            closestWord = word;
	            largestValue = getCharacterValue(word.toLowerCase());
	        }
	    }
	    return closestWord;
	}

	private String findClosestLexicalWord(String text, String[] words) {
	    String closestWord = null;
	    int closestDistance = Integer.MAX_VALUE;
	    for (String word : words) {
	        // Calculate the distance between the lowercaseText and the word based on lexical order
	        int distance = Math.abs(text.compareToIgnoreCase(word));
	        if (distance < closestDistance || (distance == closestDistance && word.compareTo(closestWord) < 0)) {
	            closestDistance = distance;
	            closestWord = word;
	        }
	    }
	    return closestWord;
	}

	private int getCharacterValue(String text) {
	    int value = 0;
	    for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);
	        if (Character.isLetter(c)) {
	            value += Character.toLowerCase(c) - 'a' + 1;
	        }
	    }
	    return value;
	}

}
