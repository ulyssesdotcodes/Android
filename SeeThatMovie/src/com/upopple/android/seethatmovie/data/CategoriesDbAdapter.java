package com.upopple.android.seethatmovie.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class CategoriesDbAdapter extends AbstractDbAdapter{

	MoviesDbAdapter mdb;
	
	public static final String TABLE_NAME = "categories";
	public static final String MOVIE_ID = "movie_id";
	public static final String MOVIE_TITLE = "movie";
	public static final String CATEGORY = "category";
	public static final String KEY_ID = "_id";
	
	public static final String CREATE_TABLE = "create table " +
			TABLE_NAME+" ("+
			KEY_ID+" integer primary key autoincrement, "+
			MOVIE_ID+" text not null, "+
			MOVIE_TITLE+" text not null, "+
			CATEGORY+" text not null);";

	public CategoriesDbAdapter(Context ctx, MoviesDbAdapter mdb) {
		super(ctx);
		this.mdb = mdb;
	}
	
	public boolean movieHasCategory(String id, String category){
		try{
			String[] columns = {CATEGORY};
			String selection = MOVIE_ID + " = ? AND " + CATEGORY + " = ?" ;
			String[] selectionArgs = {id, category};
			
			return mDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null).getCount() > 0;
		} catch(SQLiteException e){
			return false;
		}
	}
	
	public long insertmovie(String id, String title, ArrayList<String> categories){
		
		try{
			ContentValues cvs = new ContentValues();
			long result = -1;
			
			for(String category: categories){
				cvs.put(MOVIE_ID, id);
				cvs.put(MOVIE_TITLE, title.trim());
				cvs.put(CATEGORY, category.trim());
				result = mDb.insert(TABLE_NAME, null, cvs);
				if(result == -1)
					break;
			}
			return result;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		}
	}
	
	public boolean removeMovieCategories(String id){
		try{
			String where = MOVIE_ID + " = ?";
			String[] whereArgs = {id};
			
			return mDb.delete(TABLE_NAME, where, whereArgs) > 0;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return false;
		}
	}
	
	public long insertMovieCategory(String id, String title, String category){
		if(movieHasCategory(id, category)){
			return 1;
		}
		try{
			ContentValues cvs = new ContentValues();
			long result = -1;
			
			cvs.put(MOVIE_ID, id);
			cvs.put(MOVIE_TITLE, title.trim());
			cvs.put(CATEGORY, category.trim());
			result = mDb.insert(TABLE_NAME, null, cvs);
			
			return result;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		}
	}
	
	public boolean removeMovieCategory(String id, String category){
		try{
			String where = MOVIE_ID + " = ? AND " + CATEGORY + " = ?";
			String[] whereArgs = {id, category};
			
			return mDb.delete(TABLE_NAME, where, whereArgs) > 0;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return false;
		}
	}
	
	
	public Cursor getAllCategories(){
		String[] columns = {CATEGORY};
		Cursor c = mDb.query(TABLE_NAME, columns, null, null, null, null, null);
		return c;
	}
	
	public ArrayList<String> getCategoriesForMovie(String movieId){
		String[] columns = {CATEGORY};
		String selection = MOVIE_ID + " = ?";
		String[] selectionArgs = {movieId};
		Cursor c = mDb.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		ArrayList<String> categories = new ArrayList<String>();
		if(c.moveToFirst()){
			do{
				categories.add(c.getString(c.getColumnIndex(CATEGORY)));
			}while(c.moveToNext());
			return categories;
		} else {
			return null;
		}
	}
	
	public ArrayList<DBMovie> getMovies(String category, boolean withJson){
		String[] columns = {MOVIE_ID, MOVIE_TITLE};
		String[] selectionArgs = {category};
		Cursor c = mDb.query(TABLE_NAME, columns, CATEGORY+" = ?", selectionArgs, null, null, null);
		ArrayList<DBMovie> movies = new ArrayList<DBMovie>();
		if(c.moveToFirst()){
			do{
				movies.add(mdb.getMovieById(c.getString(c.getColumnIndex(CategoriesDbAdapter.MOVIE_ID)), withJson));
			}while(c.moveToNext());
			return movies;
		} else {
			return null;
		}
	}
}
