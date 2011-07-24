package com.upopple.android.seethatmovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.upopple.andoid.seethatmovie.R;
import com.upopple.android.seethatmovie.data.MovieDB;

public class AddMovie extends Activity {
	EditText titleET, categoryET;
	MovieDB mdb;
	
	Button addbutton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_movie);
		
		mdb = new MovieDB(this);
		mdb.open();
		
		titleET = (EditText)findViewById(R.id.movieTitleEdit);
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
		mdb.insertmovie(titleET.getText().toString(), categoryET.getText().toString());
		mdb.close();
		titleET.setText("");
		categoryET.setText("");
		Intent i = new Intent(AddMovie.this, SeeThatMovieActivity.class);
		startActivity(i);
	}
}
