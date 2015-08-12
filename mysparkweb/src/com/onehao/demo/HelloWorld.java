package com.onehao.demo;
import static spark.Spark.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloWorld {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
        
        get("/hellojson", (req, res) ->  new MyMessage("Hello World"), new JsonTransformer());
    }
}

class MyMessage{
	
	public MyMessage(String message){
		this.message = message;
		this.message2 = message;
		routes = new ArrayList<String>();
        routes.add("A");
        routes.add("B");
        routes.add("Hello");
        routes.add("World");
        map = new HashMap<String, Integer>();
        map.put("A", 1);
        map.put("Michael", 10);
        map.put("One", 1024);
	}
	
	private String message;
	
	private String message2;
	
	/**
	 * @return the message2
	 */
	public String getMessage2() {
		return message2;
	}

	/**
	 * @param message2 the message2 to set
	 */
	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	private List<String> routes;
//	
	private Map<String, Integer> map;

	/**
	 * @return the map
	 */
	public Map<String, Integer> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	/**
	 * @return the routes
	 */
	public List<String> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
		

	}
	
}