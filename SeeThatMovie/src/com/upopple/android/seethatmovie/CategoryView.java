package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.DBMovie;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class CategoryView extends ListActivity {
	private static final int CATEGORY_SELECT = 0;
	
	protected MoviesDbAdapter mdb;
	protected CategoriesDbAdapter cdb;
	protected CategoryViewAdapter categoryViewAdapter;
	protected ArrayList<DBMovie> movies;
	protected ArrayList<String> categories;
	
	
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
		
		setUpBottomBar();
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
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case CATEGORY_SELECT:
			builder.setTitle("View Movies From...")
				.setCancelable(true)
				.setItems(categories.toArray(new String[]{}), new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(CategoryView.this, CategoryView.class);
						i.putExtra("category", categories.get(which));
						startActivity(i);
					}
				});
			dialog = builder.create();
			break;
		default:
			builder.setMessage("Oh no! Something broke.")
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			dialog = builder.create();
			break;
		}
		return dialog;
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
	

	private void setUpBottomBar(){
		
		Button bottomBtnHome, bottomBtnCategoryList, bottomBtnViewAll;
		
		bottomBtnHome = (Button) findViewById(R.id.bottom_btn_home);
		bottomBtnHome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(CategoryView.this, Home.class);
				startActivity(i);
			}
		});
		
		categories = new ArrayList<String>();
		categories.addAll(cdb.getAllCategories());
		bottomBtnCategoryList = (Button) findViewById(R.id.bottom_btn_category_list);
		bottomBtnCategoryList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(CATEGORY_SELECT);
			}
		});
		
		bottomBtnViewAll = (Button) findViewById(R.id.bottom_btn_view_all);
		bottomBtnViewAll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(CategoryView.this, CategoryView.class);
				startActivity(i);
			}
		});
		
		if(category == null || category.equals(""))
			bottomBtnViewAll.setBackgroundColor(Color.parseColor("#0276FD"));
		else
			bottomBtnCategoryList.setBackgroundColor(Color.parseColor("#0276FD"));
			
	}
}
