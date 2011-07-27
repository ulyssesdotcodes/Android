package com.upopple.android.seethatmovie.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.upopple.android.seethatmovie.util.Constants;

public abstract class AbstractDbAdapter {
	private static final String LOG_TAG = AbstractDbAdapter.class.getSimpleName();
	
	public static final String DATABASE_NAME = "datastorage";
	public static final int DATABASE_VERSION = 1;
	
	protected SQLiteDatabase mDb;
	protected static DatabaseHelper mDbHelper;

    protected final Context mCtx;
	
	public AbstractDbAdapter(Context ctx){
		mCtx = ctx;
	}
	
	public AbstractDbAdapter open() throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDbHelper.close();
	}
	
	public ArrayList<String> stringCursorToArrayList(Cursor c){
		ArrayList<String> sAr = new ArrayList<String>();
		if(c.moveToFirst()){
			do{
				sAr.add(c.getString(c.getColumnIndex(CategoriesDbAdapter.CATEGORY)));
			} while(c.moveToNext());
		}
		return sAr;
	}
	
	protected static class DatabaseHelper extends SQLiteOpenHelper{
		public static final String DATABASE_NAME = "datastorage";
		public static final int DATABASE_VERSION = 1;
		
		private DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MoviesDbAdapter.CREATE_TABLE);
			db.execSQL(CategoriesDbAdapter.CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version "+oldVersion
					+" to "+newVersion
					+", which will destroy all old data");
			db.execSQL("drop table if exists " + MoviesDbAdapter.TABLE_NAME);
			db.execSQL("drop table if exists " + CategoriesDbAdapter.TABLE_NAME);
			onCreate(db);
		}
	}

}
