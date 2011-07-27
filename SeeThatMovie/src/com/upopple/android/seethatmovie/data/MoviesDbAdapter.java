package com.upopple.android.seethatmovie.data;

import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.upopple.android.seethatmovie.util.Constants;
import com.upopple.android.seethatmovie.web.RottenTomatoesAPIAsync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MoviesDbAdapter extends AbstractDbAdapter{
	
	CategoriesDbAdapter cdbAdapter;

	public static final String TABLE_NAME = "movies";
	public static final String TITLE = "title";
	public static final String CATEGORIES = "categories";
	public static final String API_ID = "movie_id";
	public static final String JSON_DATA = "movie_json";
	public static final String DATE_ADDED = "createdTs";
	public static final String KEY_ID = "_id";
	
	public static final String CREATE_TABLE = "create table " +
			MoviesDbAdapter.TABLE_NAME+" ("+
			MoviesDbAdapter.KEY_ID+" integer primary key autoincrement, "+
			MoviesDbAdapter.TITLE+" text not null, "+
			MoviesDbAdapter.CATEGORIES+" text not null, "+
			MoviesDbAdapter.JSON_DATA+" text, "+
			MoviesDbAdapter.DATE_ADDED + " long);";
	

	public MoviesDbAdapter(Context ctx) {
		super(ctx);
		cdbAdapter = new CategoriesDbAdapter(mCtx);
	}
	

	public long insertmovie(String id, String title, String categories){
		try{
			ContentValues cvs = new ContentValues();
			
			cvs.put(TITLE, title);
			cvs.put(CATEGORIES, categories);
			Movie movie = (Movie)new RottenTomatoesAPIAsync().execute(new String[]{""+RottenTomatoesAPIAsync.GET_MOVIE_BY_ID, id}).get();
			Gson gson = new Gson();
			if(movie!=null)
				cvs.put(JSON_DATA, gson.toJson(movie));
			cvs.put(DATE_ADDED, java.lang.System.currentTimeMillis());
			long result = mDb.insert(TABLE_NAME, null, cvs);
			
			cdbAdapter.open();
			cdbAdapter.insertmovie(""+result, title, categories);
			cdbAdapter.close();
			
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
		Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);
		return c;
	}
	
	public Cursor getMovieById(String id){
		String selection = KEY_ID + " = ?";
		String[] selectionArgs = {id};
		Cursor c = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
		return c;
	}
}
