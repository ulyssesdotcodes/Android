package com.upopple.android.seethatmovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.upopple.andoid.seethatmovie.R;

public class SeeThatMovieActivity extends Activity {
	EditText searchMovies;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        searchMovies =  (EditText) findViewById(R.id.searchMovies);
        
        Button addMovie = (Button)findViewById(R.id.addMovieHome);
        addMovie.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
		        Intent i = new Intent(SeeThatMovieActivity.this, MovieSearchResults.class);
		        i.putExtra("search", searchMovies.getText().toString());
		        startActivity(i);
			}
		});
        
        Button movieList = (Button)findViewById(R.id.movieList);
        movieList.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
		        Intent i = new Intent(SeeThatMovieActivity.this, CategoryView.class);
		        startActivity(i);
			}
		});
        
    }
}