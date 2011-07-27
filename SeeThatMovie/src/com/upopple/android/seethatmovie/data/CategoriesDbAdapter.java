package com.upopple.android.seethatmovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.upopple.android.seethatmovie.util.Constants;

public class CategoriesDbAdapter extends AbstractDbAdapter{
	
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


	public CategoriesDbAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public long insertmovie(String id, String title, String categoriesString){
		try{
			ContentValues cvs = new ContentValues();
			long result = -1;
			
			String[] categories = categoriesString.split(",");
			for(String category: categories){
				cvs.put(MOVIE_ID, title);
				cvs.put(MOVIE_TITLE, title);
				cvs.put(CATEGORY, category);
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
	
	public Cursor getCategories(String movie){
		String[] columns = {CATEGORY};
		String selection = MOVIE_TITLE + " = " +movie;
		Cursor c = mDb.query(TABLE_NAME, columns, selection, null, null, null, null);
		return c;
	}
	
	public Cursor getMovies(String category){
		String[] columns = {MOVIE_ID, MOVIE_TITLE};
		String[] selectionArgs = {category};
		Cursor c = mDb.query(TABLE_NAME, columns, CATEGORY+" = ?", selectionArgs, null, null, null);
		return c;
	}
}
