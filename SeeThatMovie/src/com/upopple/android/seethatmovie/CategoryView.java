package com.upopple.android.seethatmovie;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.DBMovie;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class CategoryView extends ListActivity {
	MoviesDbAdapter mdb;
	CategoriesDbAdapter cdb;
	CategoryViewAdapter categoryViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		cdb = mdb.getCdbAdapter();
		cdb.open();
		setContentView(R.layout.category_view);
		
		super.onCreate(savedInstanceState);
		categoryViewAdapter = new CategoryViewAdapter(this, this.getIntent().getStringExtra("category"));
		this.setListAdapter(categoryViewAdapter);
	}
	
	private class CategoryViewAdapter extends BaseAdapter{
		private LayoutInflater li;
		private ArrayList<DBMovie> movies;
		
		public CategoryViewAdapter(Context context, String category){
			li = LayoutInflater.from(context);
			movies = new ArrayList<DBMovie>();
			getdata(category);
		}
		
		public void getdata(String category){
			if(category.equals("")){
				movies = mdb.getMovies(false);
			} else {
				movies = cdb.getMovies(category, false);
			}
		}
		
		public int getCount() {return movies.size();}
		public DBMovie getItem(int i){return movies.get(i);}
		public long getItemId(int i){return i;}
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.movierow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mDate = (TextView)v.findViewById(R.id.dateAdded);
				
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			
			holder.movie = getItem(position);
			holder.mTitle.setText(holder.movie.getTitle());
			holder.mDate.setText(holder.movie.getFormattedDate());
			
			v.setTag(holder);
			
			return v;
		}
		
		
		public class ViewHolder {
			DBMovie movie;
			TextView mTitle;
			TextView mDate;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(CategoryView.this, MoviePage.class);
		i.putExtra("id", ((DBMovie)l.getItemAtPosition(position)).getId());
		startActivity(i);
	}
}
