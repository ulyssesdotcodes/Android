package com.upopple.android.seethatmovie;

import android.app.AlertDialog;
import android.app.Dialog;
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
	public static final int DIALOG_NO_SEARCH = 100;
	
	private Button homeBtnAdd;
	private EditText addMovieSearch;
	
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
		TabHost.TabSpec tabSpec;
		Intent intent;
		
		intent = new Intent().setClass(this, ToSeeList.class);
		
		tabSpec = tabHost.newTabSpec("home").setIndicator("Home").setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass(this, CategoryView.class);

		tabSpec = tabHost.newTabSpec("categories").setIndicator("Categories").setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent = new Intent().setClass(this, CategoryView.class);
		
		tabSpec = tabHost.newTabSpec("view_all").setIndicator("View All").setContent(intent);
		tabHost.addTab(tabSpec);
		
		
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
}
