package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.DBMovie;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class MoviePage extends Activity{
	MoviesDbAdapter mdb;
	CategoriesDbAdapter cdb;
	DBMovie thisMovie;
	ArrayList<String> categories;
	boolean seen;
	
	private static final int DELETE = 1001;
	
	TextView title;
	CheckBox seenCheck;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_page);
		
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		cdb = mdb.getCdbAdapter();
		cdb.open();
		
		String movieId = getIntent().getStringExtra("id");
		
		thisMovie = mdb.getMovieById(movieId, true);
		categories = cdb.getCategoriesForMovie(thisMovie.getId());
		
		title = (TextView) findViewById(R.id.moviePageTitle);
		seenCheck = (CheckBox) findViewById(R.id.moviePageSeen);
		
		title.setText(thisMovie.getTitle());
		
		seen = cdb.movieHasCategory(movieId, "_seen");
		if(seen){
			seenCheck.setChecked(true);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		Dialog inputError;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case DELETE: 
			builder.setMessage("Are you sure you want to delete this movie?")
				.setCancelable(true)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						mdb.removeMovie(thisMovie.getId());
						startActivity(new Intent(MoviePage.this, ToSeeList.class));
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					   public void onClick(DialogInterface dialog, int id) {
						   dialog.cancel();
					   }
				   });
			inputError = builder.create();
			break;
		default:
			builder.setMessage("Oh no! Something broke.")
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			inputError = builder.create();
			break;
		}
		return inputError;
	}
	
	@Override
	protected void onPause() {
		if(seen && !seenCheck.isChecked())
			cdb.removeMovieCategory(thisMovie.getId(), "_seen");
		else if(!seen && seenCheck.isChecked())
			cdb.insertMovieCategory(thisMovie.getId(), thisMovie.getTitle(), "_seen");
		
		super.onPause();
	}
	
	//Menu logic
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.movie_page_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.moviePageMenuHome:
	        goHome();
	        return true;
	    case R.id.moviePageMenuEdit:
	        editMovie();
	        return true;
	    case R.id.moviePageMenuDelete:
			showDialog(DELETE);
	        return true;
	    case R.id.moviePageMenuHelp:
	        showHelp();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void goHome(){
		startActivity(new Intent(MoviePage.this, ToSeeList.class));
	}

	private void showHelp() {
		
	}

	private void editMovie() {
		
	}
	
}
