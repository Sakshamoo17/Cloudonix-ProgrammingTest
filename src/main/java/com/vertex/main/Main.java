package com.vertex.main;


import VertextProject.Vertexproject.TextAnalyzerService;
import io.vertx.core.Vertx;

public class Main {
	
	public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TextAnalyzerService());
    }

}
