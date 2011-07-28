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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.DBMovie;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class Home extends ListActivity {
	private static final int DIALOG_ADD_MOVIE = 0;
	
	private MoviesDbAdapter mdb;
	private CategoriesDbAdapter cdb;
	private HomeAdapter homeAdapter;
	
	private Button homeBtnAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		cdb = mdb.getCdbAdapter();
		cdb.open();
		setContentView(R.layout.main);
		
		super.onCreate(savedInstanceState);
		
		homeAdapter = new HomeAdapter(this);
		this.setListAdapter(homeAdapter);
		
		homeBtnAdd = (Button) findViewById(R.id.home_btn_add);
		homeBtnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case DIALOG_ADD_MOVIE: 
			builder.setView(findViewById(R.id.addMovieDialogContent))
				.setCancelable(true)
				.setPositiveButton("Find", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(Home.this, MovieSearchResults.class);
						EditText search = (EditText)findViewById(R.id.addMovieSearch);
						i.putExtra("search", search.getText().toString());
					}
					
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
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
		protected ArrayList<DBMovie> movies;
		
		public HomeAdapter(Context context){
			li = LayoutInflater.from(context);
			movies = new ArrayList<DBMovie>();
			getdata();
		}
		
		public void getdata(){
			movies = cdb.getMovies("_toSee", false);
			if(movies == null){
				movies = new ArrayList<DBMovie>();
				TextView homeNoMovies = (TextView)findViewById(R.id.home_no_movies_to_see);
				homeNoMovies.setVisibility(TextView.VISIBLE);
			}
		}
		
		public int getCount() {return movies.size();}
		public DBMovie getItem(int i){return movies.get(i);}
		public long getItemId(int i){return i;}
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.toseemovierow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mCheck = (CheckBox)v.findViewById(R.id.toSeeSawItCheck);
				
				holder.mCheck.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						if(holder.mCheck.isChecked()){
							cdb.removeMovieCategory(holder.movie.getId(), "_toSee");
						} else {
							cdb.insertMovieCategory(holder.movie.getId(), holder.movie.getTitle(), "_toSee");
						}
					}
				});
				
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			
			holder.movie = getItem(position);
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
			Intent i = new Intent(Home.this, MoviePage.class);
			i.putExtra("id", ((DBMovie)l.getItemAtPosition(position)).getId());
			startActivity(i);
		}
	}
}