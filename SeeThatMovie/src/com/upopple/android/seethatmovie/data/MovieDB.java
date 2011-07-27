package com.upopple.android.seethatmovie.data;

import java.util.concurrent.ExecutionException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.upopple.android.seethatmovie.util.Constants;
import com.upopple.android.seethatmovie.web.RottenTomatoesAPIAsync;

public class MovieDB {
	private SQLiteDatabase db;
	private final Context context;
	private final MovieDbHelper mdbh;
	private final CategoryDB cdb;
	
	public MovieDB(Context c){
		context = c;
		cdb = new CategoryDB(c);
		mdbh = new MovieDbHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}
	
	public void close(){
		db.close();
	}
	
	public void open() throws SQLiteException{
		try{
			db = mdbh.getWritableDatabase();
		} catch(SQLiteException e){
			Log.v("Open database exception caught", e.getMessage());
			db = mdbh.getReadableDatabase();
		}
	}
	
	public long insertmovie(String id, String title, String categories){
		try{
			ContentValues cvs = new ContentValues();
			
			cvs.put(Constants.MOVIE_TITLE, title);
			cvs.put(Constants.MOVIE_CATEGORIES, categories);
			Movie movie = (Movie)new RottenTomatoesAPIAsync().execute(new String[]{""+RottenTomatoesAPIAsync.GET_MOVIE_BY_ID, id}).get();
			Gson gson = new Gson();
			if(movie!=null)
				cvs.put(Constants.MOVIE_JSON_DATA, gson.toJson(movie));
			cvs.put(Constants.DATE_ADDED, java.lang.System.currentTimeMillis());
			long result = db.insert(Constants.MOVIE_TABLE_NAME, null, cvs);
			
			cdb.open(db);
			cdb.insertmovie(""+result, title, categories);
			cdb.close();
			
			return result;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		} catch (InterruptedException e) {
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		} catch (ExecutionException e) {
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		}
	}
	
	public Cursor getMovies(){
		Cursor c = db.query(Constants.MOVIE_TABLE_NAME, null, null, null, null, null, null);
		return c;
	}
	
	public Cursor getMovieById(String id){
		String selection = Constants.KEY_ID + " = " + id;
		Cursor c = db.query(Constants.MOVIE_TABLE_NAME, null, selection, null, null, null, null);
		return c;
	}
}
