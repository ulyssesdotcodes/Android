package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.DBMovie;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class CategoryView extends ListActivity {
	protected MoviesDbAdapter mdb;
	protected CategoriesDbAdapter cdb;
	protected CategoryViewAdapter categoryViewAdapter;
	protected ArrayList<DBMovie> movies;
	
	protected String category;
	
	private TextView listTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		cdb = mdb.getCdbAdapter();
		cdb.open();
		setContentView(R.layout.category_view);
		
		super.onCreate(savedInstanceState);
		
		listTitle = (TextView) findViewById(R.id.categoryViewText);
		
		if(categoryViewAdapter == null) categoryViewAdapter = new CategoryViewAdapter(this, this.getIntent().getStringExtra("category"));
		this.setListAdapter(categoryViewAdapter);
	}
	
	protected class CategoryViewAdapter extends BaseAdapter{
		protected LayoutInflater li;
		
		public CategoryViewAdapter(Context context, String category){
			li = LayoutInflater.from(context);
			movies = new ArrayList<DBMovie>();
			getdata(category);
		}
		
		public void getdata(String cat){
			category = cat;
			if(category == null || category.equals("")){
				listTitle.setText("All Movies");
				movies = mdb.getMovies(false);
			} else {
				listTitle.setText(category + " Movies");
				movies = cdb.getMovies(category, false);
			}
			if(movies == null){
				movies = new ArrayList<DBMovie>();
				TextView homeNoMovies = (TextView)findViewById(R.id.home_no_movies_to_see);
				homeNoMovies.setVisibility(TextView.VISIBLE);
			}
		}
		
		public int getCount() {return movies.size();}
		public DBMovie getItem(int i){return movies.get(i);}
		public long getItemId(int i){return i;}
		public void remove(int i){movies.remove(i);}
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.checkboxrow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mCheck = (CheckBox)v.findViewById(R.id.checkbox);
				
				holder.mCheck.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if(holder.mCheck.isChecked()){
							cdb.insertMovieCategory(holder.movie.getId(), holder.movie.getTitle(), "_seen");
						} else {
							cdb.removeMovieCategory(holder.movie.getId(), "_seen");
						}
					}
				});
				
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(CategoryView.this, MoviePage.class);
						i.putExtra("id", holder.movie.getId());
						startActivity(i);
					}
				});
				
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			
			holder.movie = getItem(position);
			holder.mCheck.setChecked(cdb.movieHasCategory(holder.movie.getId(), "_seen"));
			holder.mTitle.setText(holder.movie.getTitle());
			
			v.setTag(holder);
			
			return v;
		}
		
		public class ViewHolder {
			DBMovie movie;
			TextView mTitle;
			CheckBox mCheck;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(v.isEnabled()){
			Intent i = new Intent(CategoryView.this, MoviePage.class);
			i.putExtra("id", ((DBMovie)l.getItemAtPosition(position)).getId());
			startActivity(i);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(category == null || category.equals("")){
			movies = mdb.getMovies(false);
		} else {
			movies = cdb.getMovies(category, false);
		}
		if(movies == null){
			movies = new ArrayList<DBMovie>();
		}
		categoryViewAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.moviePageMenuHome:
	        goHome();
	        return true;
	    case R.id.moviePageMenuCategory:
	        return true;
	    case R.id.moviePageMenuAllMovies:
	        return true;
	    case R.id.moviePageMenuHelp:
	        showHelp();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void goHome(){
		startActivity(new Intent(CategoryView.this, Home.class));
	}

	private void showHelp() {
		
	}
}
