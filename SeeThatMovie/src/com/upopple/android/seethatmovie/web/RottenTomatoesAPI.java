package com.upopple.android.seethatmovie.web;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class RottenTomatoesAPI {
	public static final String api_key = "464f28afyq7r9bgk9nrqaetm";
	
	
	public static ArrayList<String> getMovieTitles(ArrayList<String> movieTitles, String start) throws JSONException{
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="+api_key+"&q="+Uri.encode(start)+"&limit=10";
		
		JSONObject movieSearch = RestJsonClient.connect(url);
		JSONArray movies = movieSearch.getJSONArray("movies");
		if(movieTitles!=null && movieTitles.size() > 0)
			movieTitles.clear();
		else if(movieTitles == null)
			movieTitles = new ArrayList<String>();
		
		for(int i=0; i<movies.length(); i++){
			movieTitles.add(movies.getJSONObject(i).getString("title"));
		}
		
		return movieTitles;
	}
}
