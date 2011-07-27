package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.upopple.andoid.seethatmovie.R;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;

public class AddMovie extends Activity {
	EditText categoryET;
	TextView titleBox;
	ArrayList<String> movieList;
	MoviesDbAdapter mdb;
	
	Button addbutton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_movie);
		
		mdb = new MoviesDbAdapter(this);
		mdb.open();
		
		
		Intent i = getIntent();
		titleBox = (TextView)findViewById(R.id.movieTitle);
		titleBox.setText(i.getStringExtra("movieTitle"));
		
		categoryET = (EditText)findViewById(R.id.movieCategoryEdit);
		addbutton = (Button)findViewById(R.id.addMovieButton);
		addbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					saveItToDb();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public void saveItToDb(){
		String movieId = getIntent().getStringExtra("movieId");
		String movieTitle = titleBox.getText().toString();
		String movieCategories = categoryET.getText().toString();

		mdb.insertmovie(movieId, movieTitle, movieCategories);
			
		mdb.close();
		titleBox.setText("");
		categoryET.setText("");
		Intent i = new Intent(AddMovie.this, SeeThatMovieActivity.class);
		startActivity(i);
	}	
}
