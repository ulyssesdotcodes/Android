package com.upopple.android.seethatmovie;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.upopple.andoid.seethatmovie.R;
import com.upopple.android.seethatmovie.data.MovieDB;
import com.upopple.android.seethatmovie.web.RottenTomatoesAPI;

public class AddMovie extends Activity {
	EditText categoryET;
	EditText titleBox;
	ArrayList<String> movieList;
	MovieDB mdb;
	
	Button addbutton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_movie);
		
		mdb = new MovieDB(this);
		mdb.open();
		
		Intent i = getIntent();
		titleBox = (EditText)findViewById(R.id.movieTitleEdit);
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
		mdb.insertmovie(titleBox.getText().toString(), categoryET.getText().toString());
		mdb.close();
		titleBox.setText("");
		categoryET.setText("");
		Intent i = new Intent(AddMovie.this, SeeThatMovieActivity.class);
		startActivity(i);
	}	
}
