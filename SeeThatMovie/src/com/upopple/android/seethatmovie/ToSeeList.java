package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class ToSeeList extends ListActivity {
	private static final int DIALOG_NO_SEARCH = 100;
	
	private MoviesDbAdapter mdb;
	private CategoriesDbAdapter cdb;
	private HomeAdapter homeAdapter;
	
	protected ArrayList<DBMovie> movies;
	
	TextView homeNoMovies;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		cdb = mdb.getCdbAdapter();
		cdb.open();
		
		setContentView(R.layout.to_see_list);
		
		super.onCreate(savedInstanceState);

		//setUpBottomBar();
		
		homeNoMovies = (TextView)findViewById(R.id.home_no_movies_to_see);
		
		
		homeAdapter = new HomeAdapter(this);
		this.setListAdapter(homeAdapter);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case DIALOG_NO_SEARCH:
			builder.setMessage("Add a movie with a title!")
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
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
	
	protected class HomeAdapter extends BaseAdapter{
		protected LayoutInflater li;
		
		public HomeAdapter(Context context){
			li = LayoutInflater.from(context);
			movies = new ArrayList<DBMovie>();
			getdata();
		}
		
		public void getdata(){
			movies = mdb.getMoviesExcludeCategory("_seen", false);
			if(movies.size() == 0){
				homeNoMovies.setVisibility(TextView.VISIBLE);
			}
		}
		
		public int getCount() {return movies.size();}
		public DBMovie getItem(int i){return movies.get(i);}
		public long getItemId(int i){return i;}
		public void remove(DBMovie dbm){movies.remove(dbm);}
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.checkboxrow, null);
				holder = new ViewHolder();
				holder.position = position;
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mCheck = (CheckBox)v.findViewById(R.id.checkbox);
				
				holder.mCheck.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if(holder.mCheck.isChecked()){
							cdb.insertMovieCategory(holder.movie.getId(), holder.movie.getTitle(), "_seen");
							movies.remove(holder.movie);
							homeAdapter.notifyDataSetChanged();
							if(movies.size() == 0)
								homeNoMovies.setVisibility(View.VISIBLE);
						} else {
							cdb.removeMovieCategory(holder.movie.getId(), "_seen");
						}
					}
				});
				
				v.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(ToSeeList.this, MoviePage.class);
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
			int position;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(v.isEnabled()){
			Intent i = new Intent(ToSeeList.this, MoviePage.class);
			i.putExtra("id", ((DBMovie)l.getItemAtPosition(position)).getId());
			startActivity(i);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		movies = mdb.getMoviesExcludeCategory("_seen", false);
		if(movies.size() == 0){
			homeNoMovies.setVisibility(TextView.VISIBLE);
		} else {
			homeNoMovies.setVisibility(TextView.GONE);
		}
		homeAdapter.notifyDataSetChanged();
	}
//	
//	private void setUpBottomBar(){
//		Button bottomBtnHome, bottomBtnCategoryList, bottomBtnViewAll;
//		
//		bottomBtnHome = (Button) findViewById(R.id.bottom_btn_home);
//		bottomBtnHome.setBackgroundColor(Color.parseColor("#0276FD"));
//		
//		categories = new ArrayList<String>();
//		categories.addAll(cdb.getAllCategories());
//		bottomBtnCategoryList = (Button) findViewById(R.id.bottom_btn_category_list);
//		bottomBtnCategoryList.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				showDialog(CATEGORY_SELECT);
//			}
//		});
//		
//		bottomBtnViewAll = (Button) findViewById(R.id.bottom_btn_view_all);
//		bottomBtnViewAll.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Intent i = new Intent(ToSeeList.this, CategoryView.class);
//				startActivity(i);
//			}
//		});
//	}
}