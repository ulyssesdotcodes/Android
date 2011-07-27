package com.upopple.android.seethatmovie.web;

import org.json.JSONException;

import android.net.Uri;

import com.google.gson.Gson;
import com.upopple.android.seethatmovie.data.Movie;


public class RottenTomatoesAPI{
	public static final String api_key = "464f28afyq7r9bgk9nrqaetm";
	
	public static RTMovieResults getMovieTitles(String search) throws JSONException{
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey="+api_key+"&q="+Uri.encode(search)+"&page_limit=10";
		
		RTMovieResults movieSearch;
		Gson gson = new Gson();
		try {
			movieSearch = gson.fromJson(RestJsonClient.connect(url), RTMovieResults.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return movieSearch;
	}
	
	public static Movie getMovieById(String id) throws JSONException{
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json?apikey="+api_key;
		
		Movie movie;
		Gson gson = new Gson();
		try {
			movie = gson.fromJson(RestJsonClient.connect(url), Movie.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return movie;
	}
}
