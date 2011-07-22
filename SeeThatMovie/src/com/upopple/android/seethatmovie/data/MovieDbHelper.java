package com.upopple.android.seethatmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.upopple.android.seethatmovie.util.Constants;

public class MovieDbHelper extends SQLiteOpenHelper{
	private static final String CREATE_TABLE = "create table " +
			Constants.MOVIE_TABLE_NAME+" ("+
			Constants.KEY_ID+" integer primary key autoincrement, "+
			Constants.MOVIE_TITLE+" text not null, "+
			Constants.MOVIE_CATEGORIES+" text not null, "+
			Constants.DATE_ADDED + " long);";
	
	public MovieDbHelper(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		Log.v("MovieDbHelper onCreate", "Creating all the tables");
		try{
			db.execSQL(CREATE_TABLE);
		} catch(SQLiteException e){
			Log.v("Create table exception", e.getMessage());
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w("TaskDBAdapter", "Ipgradiung from version "+oldVersion
				+" to "+newVersion
				+", which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.MOVIE_TABLE_NAME);
		onCreate(db);
	}
}
