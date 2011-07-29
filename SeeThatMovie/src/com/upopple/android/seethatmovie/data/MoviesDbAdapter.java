package com.upopple.android.seethatmovie.data;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.upopple.android.seethatmovie.web.RottenTomatoesAPIAsync;

public class MoviesDbAdapter extends AbstractDbAdapter{
	
	private CategoriesDbAdapter cdbAdapter;

	public static final String TABLE_NAME = "movies";
	public static final String TITLE = "title";
	public static final String JSON_DATA = "movie_json";
	public static final String DATE_ADDED = "createdTs";
	public static final String KEY_ID = "_id";
	
	public static final String CREATE_TABLE = "create table " +
			MoviesDbAdapter.TABLE_NAME+" ("+
			MoviesDbAdapter.KEY_ID+" integer primary key autoincrement, "+
			MoviesDbAdapter.TITLE+" text not null, "+
			MoviesDbAdapter.JSON_DATA+" text, "+
			MoviesDbAdapter.DATE_ADDED + " long);";
	
	
	
	public MoviesDbAdapter(Context ctx) {
		super(ctx);
		cdbAdapter = new CategoriesDbAdapter(mCtx, this);
	}
	
	public CategoriesDbAdapter getCdbAdapter() {
		return cdbAdapter;
	}
	public void setCdbAdapter(CategoriesDbAdapter cdbAdapter) {
		this.cdbAdapter = cdbAdapter;
	}

	public long insertmovie(String id, String title, ArrayList<String> categories){
		try{
			ContentValues cvs = new ContentValues();
			
			cvs.put(TITLE, title.trim());
			GSONMovie movie = (GSONMovie)new RottenTomatoesAPIAsync().execute(new String[]{""+RottenTomatoesAPIAsync.GET_MOVIE_BY_ID, id}).get();
			Gson gson = new Gson();
			if(movie!=null)
				cvs.put(JSON_DATA, gson.toJson(movie));
			else
				cvs.put(JSON_DATA, "");
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

	public boolean removeMovie(String id){
		try{
			String where = KEY_ID + " = ?";
			String[] whereArgs = {id};
			
			boolean result = mDb.delete(TABLE_NAME, where, whereArgs) > 0;
			
			if(result)
				cdbAdapter.removeMovieCategories(id);
			
			return result;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return false;
		}
	}
	
	public ArrayList<DBMovie> getMovies(boolean withJson){
		Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);
		ArrayList<DBMovie> movies = new ArrayList<DBMovie>();
		if(c.moveToFirst()){
			do{
				String id = c.getString(c.getColumnIndex(MoviesDbAdapter.KEY_ID));
				String title = c.getString(c.getColumnIndex(MoviesDbAdapter.TITLE));
				String json_data = (withJson) ? c.getString(c.getColumnIndex(MoviesDbAdapter.JSON_DATA)) : "";
				long dateAdded = c.getLong(c.getColumnIndex(MoviesDbAdapter.DATE_ADDED));
				movies.add(new DBMovie(id, title,json_data, dateAdded));
			}while(c.moveToNext());
		}
		return movies;
	}
	
	public ArrayList<DBMovie> getMoviesExcludeCategory(String category, boolean withJson){
		Cursor c = mDb.query(TABLE_NAME, null, null, null, null, null, null);
		ArrayList<DBMovie> movies = new ArrayList<DBMovie>();
		if(c.moveToFirst()){
			do{
				
				String id = c.getString(c.getColumnIndex(MoviesDbAdapter.KEY_ID));
				
				if(!cdbAdapter.movieHasCategory(id, category)){
					String title = c.getString(c.getColumnIndex(MoviesDbAdapter.TITLE));
					String json_data = (withJson) ? c.getString(c.getColumnIndex(MoviesDbAdapter.JSON_DATA)) : "";
					long dateAdded = c.getLong(c.getColumnIndex(MoviesDbAdapter.DATE_ADDED));
					movies.add(new DBMovie(id, title,json_data, dateAdded));
				}
			}while(c.moveToNext());
		}
		return movies;
	}
	
	public DBMovie getMovieById(String id, boolean withJson){
		String selection = KEY_ID + " = ?";
		String[] selectionArgs = {id};
		Cursor c = mDb.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
		if(c.moveToFirst()){
			String title = c.getString(c.getColumnIndex(MoviesDbAdapter.TITLE));
			String json_data = (withJson) ? c.getString(c.getColumnIndex(MoviesDbAdapter.JSON_DATA)) : "";
			long dateAdded = c.getLong(c.getColumnIndex(MoviesDbAdapter.DATE_ADDED));
			return new DBMovie(id, title, json_data, dateAdded);
		} else {
			return null;
		}
	}
}
