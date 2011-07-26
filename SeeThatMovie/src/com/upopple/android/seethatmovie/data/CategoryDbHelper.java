package com.upopple.android.seethatmovie.data;

import com.upopple.android.seethatmovie.util.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class CategoryDbHelper extends SQLiteOpenHelper {
	private static final String CREATE_TABLE = "create table " +
			Constants.CATEGORIES_TABLE_NAME+" ("+
			Constants.KEY_ID+" integer primary key autoincrement, "+
			Constants.CATEGORIES_MOVIE_TITLE+" text not null, "+
			Constants.CATEGORIES_CATEGORY+" text not null, ";
	
	public CategoryDbHelper(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("CategoryDbHelper onCreate", "Creating all the tables");
		try{
			db.execSQL(CREATE_TABLE);
		} catch(SQLiteException e){
			Log.v("Create table exception", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "Ipgradiung from version "+oldVersion
				+" to "+newVersion
				+", which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.CATEGORIES_TABLE_NAME);
		onCreate(db);
	}

}
