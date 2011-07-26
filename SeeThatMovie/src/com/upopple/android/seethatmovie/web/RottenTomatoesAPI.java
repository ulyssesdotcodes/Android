package com.upopple.android.seethatmovie.web;

import java.io.IOException;

import org.json.JSONException;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;


public class RottenTomatoesAPI extends AsyncTask<String, Void, RTMovieResults>{
	public static final String api_key = "464f28afyq7r9bgk9nrqaetm";
	
	protected RTMovieResults doInBackground(String...searches){
		try{
			return getMovieTitles(searches[0]);
		} catch(JSONException e){
			Log.v("Error getting movies.", e.getMessage());
			return null;
		}
	}
	
	public RTMovieResults getMovieTitles(String search) throws JSONException{
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
	
	protected void onPostExecute(RTMovieResults result) {
		
    }
}
