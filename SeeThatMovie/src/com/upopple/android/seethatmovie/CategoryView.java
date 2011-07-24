package com.upopple.android.seethatmovie;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.upopple.andoid.seethatmovie.R;
import com.upopple.android.seethatmovie.data.MovieDB;
import com.upopple.android.seethatmovie.util.Constants;

public class CategoryView extends ListActivity {
	MovieDB mdb;
	MovieAdapter movieAdapter;
	private class Movie{
		public Movie(String title, String cat, String date){
			this.title = title;
			this.categories = cat;
			this.createdTs = date;
		}
		public String title, categories, createdTs;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mdb = new MovieDB(this);
		mdb.open();
		setContentView(R.layout.category_view);
		
		super.onCreate(savedInstanceState);
		movieAdapter = new MovieAdapter(this);
		this.setListAdapter(movieAdapter);
	}
	
	private class MovieAdapter extends BaseAdapter{
		private LayoutInflater li;
		private ArrayList<Movie> movies;
		
		public MovieAdapter(Context context){
			li = LayoutInflater.from(context);
			movies = new ArrayList<Movie>();
			getdata();
		}
		
		public void getdata(){
			Cursor c = mdb.getMovies();
			startManagingCursor(c);
			if(c.moveToFirst()){
				do{
					String title = c.getString(c.getColumnIndex(Constants.MOVIE_TITLE));
					String categories = c.getString(c.getColumnIndex(Constants.MOVIE_CATEGORIES));
					DateFormat dateFormat = DateFormat.getDateTimeInstance();
					String dateData = dateFormat.format(new Date(c.getLong(c.getColumnIndex(Constants.DATE_ADDED))));
					Movie temp = new Movie(title, categories, dateData);
					movies.add(temp);
				} while(c.moveToNext());
			}
		}
		
		public int getCount() {return movies.size();}
		public Movie getItem(int i){return movies.get(i);}
		public long getItemId(int i){return i;}
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.movierow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mDate = (TextView)v.findViewById(R.id.dateText);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			
			holder.movie = getItem(position);
			holder.mTitle.setText(holder.movie.title);
			holder.mDate.setText(holder.movie.createdTs);
			
			v.setTag(holder);
			
			return v;
		}
		
		public class ViewHolder {
			Movie movie;
			TextView mTitle;
			TextView mDate;
		}
	}
	
	
}
