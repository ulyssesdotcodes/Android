package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

public class MovieTabWidget extends TabActivity {
	public static final int CATEGORY_SELECT = 0;
	public static final int DIALOG_NO_SEARCH = 100;

	protected ArrayList<String> categories;
	
	private Button homeBtnAdd;
	private EditText addMovieSearch;
	
	ProgressDialog fetchMoviesDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		addMovieSearch = (EditText)findViewById(R.id.add_movie_search);

		homeBtnAdd = (Button) findViewById(R.id.home_btn_add);
		homeBtnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String searchText = addMovieSearch.getText().toString();
				if(!searchText.equals("")){
					fetchMoviesDialog = ProgressDialog.show(MovieTabWidget.this, "", "Fetching movies, please wait...", true);
					Intent i = new Intent(MovieTabWidget.this, MovieSearchResults.class);
					i.putExtra("search", searchText);
					startActivity(i);
				} else {
					showDialog(DIALOG_NO_SEARCH);
				}
			}
		});
		
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		TabHost.TabSpec tabSpec;
		Intent intent;
		
		intent = new Intent().setClass(this, ToSeeList.class);
		
		tabSpec = tabHost.newTabSpec("home").setIndicator("To See").setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass(this, CategoryView.class);
		
		tabSpec = tabHost.newTabSpec("view_all").setIndicator("All").setContent(intent);
		tabHost.addTab(tabSpec);
		
		
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
						Intent i = new Intent(MovieTabWidget.this, CategoryView.class);
						i.putExtra("category", categories.get(which));
						startActivity(i);
					}
				});
			dialog = builder.create();
			break;
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
	
	@Override
	protected void onPause() {
		if(fetchMoviesDialog != null)
			fetchMoviesDialog.dismiss();
		super.onPause();
	}
}
