package com.upopple.android.seethatmovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.upopple.android.seethatmovie.util.Constants;

public class CategoryDB {
	private SQLiteDatabase db;
	private final Context context;
	private final CategoryDbHelper cdbh;
	
	public CategoryDB(Context c){
		context = c;
		cdbh = new CategoryDbHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}
	
	public void close(){
		db.close();
	}
	
	public void open() throws SQLiteException{
		try{
			db = cdbh.getWritableDatabase();
		} catch(SQLiteException e){
			Log.v("Open database exception caught", e.getMessage());
			db = cdbh.getReadableDatabase();
		}
	}
	
	public long insertmovie(String id, String title, String categoriesString){
		try{
			ContentValues cvs = new ContentValues();
			long result = -1;
			
			String[] categories = categoriesString.split(",");
			for(String category: categories){
				cvs.put(Constants.CATEGORIES_MOVIE_TITLE, title);
				cvs.put(Constants.CATEGORIES_CATEGORY, category);
				result = db.insert(Constants.CATEGORIES_TABLE_NAME, null, cvs);
				if(result == -1)
					break;
			}
			return result;
		} catch(SQLiteException e){
			Log.v("Insert into database failed", e.getMessage());
			return -1;
		}
	}
	
	public Cursor getCategories(String movie){
		String[] columns = {Constants.CATEGORIES_CATEGORY};
		String selection = Constants.CATEGORIES_MOVIE_TITLE + " = " +movie;
		Cursor c = db.query(Constants.CATEGORIES_TABLE_NAME, columns, selection, null, null, null, null);
		return c;
	}
	
	public Cursor getMovies(String category){
		String[] columns = {Constants.CATEGORIES_MOVIE_TITLE};
		String selection = Constants.CATEGORIES_CATEGORY + " = " +category;
		Cursor c = db.query(Constants.CATEGORIES_TABLE_NAME, columns, selection, null, null, null, null);
		return c;
	}
}
