package com.upopple.android.seethatmovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.upopple.android.seethatmovie.util.Constants;

public class MovieDB {
	private SQLiteDatabase db;
	private final Context context;
	private final MovieDbHelper mdbh;
	
	public MovieDB(Context c){
		context = c;
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
	
	public long insertmovie(String title, String categories){
		try{
			ContentValues cvs = new ContentValues();
			
			cvs.put(Constants.MOVIE_TITLE, title);
			cvs.put(Constants.MOVIE_CATEGORIES, categories);
			cvs.put(Constants.DATE_ADDED, java.lang.System.currentTimeMillis());
			return db.insert(Constants.MOVIE_TABLE_NAME, null, cvs);
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		}
	}
	
	public Cursor getMovies(){
		Cursor c = db.query(Constants.MOVIE_TABLE_NAME, null, null, null, null, null, null);
		return c;
	}
}
