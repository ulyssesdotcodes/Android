package com.upopple.android.seethatmovie.web;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class RottenTomatoesAPI {
	public static final String api_key = "464f28afyq7r9bgk9nrqaetm";
	
	
	public static ArrayList<RTMovieResult> getMovieTitles(String search) throws JSONException{
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="+api_key+"&q="+Uri.encode(search)+"&limit=10";
		
		JSONObject movieSearch;
		try {
			movieSearch = RestJsonClient.connect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		JSONArray movies = movieSearch.getJSONArray("movies");
		
		ArrayList<RTMovieResult> movieResults = new ArrayList<RTMovieResult>();
		
		JSONObject currentMovie;
		for(int i=0; i<movies.length(); i++){
			currentMovie = movies.getJSONObject(i);
			movieResults.add(new RTMovieResult(currentMovie.getString("title"), currentMovie.getString("year")));
		}
		
		return movieResults;
	}
}
