package com.upopple.android.seethatmovie.web;

import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class RottenTomatoesAPIAsync extends AsyncTask<String, Void, Object>{
	public static final int GET_MOVIE_TITLES = 0; //Returns RTMovieResults object
	public static final int GET_MOVIE_BY_ID = 1; //Returns Movie object
	
	
	@Override
	protected Object doInBackground(String... args) {
		/*
		 * args[0]: method
		 * args[1]: param
		 */
		try{
			switch(Integer.parseInt(args[0])){
			case GET_MOVIE_TITLES: return RottenTomatoesAPI.getMovieTitles(args[1]);
			case GET_MOVIE_BY_ID: return RottenTomatoesAPI.getMovieById(args[1]);
			default: return null;
			}
		} catch(JSONException e){
			Log.v("Failed to perform API task", e.getMessage());
			return null;
		}
		
	}
}
