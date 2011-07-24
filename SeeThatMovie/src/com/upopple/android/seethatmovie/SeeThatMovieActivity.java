package com.upopple.android.seethatmovie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.upopple.andoid.seethatmovie.R;

public class SeeThatMovieActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button addMovie = (Button)findViewById(R.id.addMovieHome);
        addMovie.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
		        Intent i = new Intent(SeeThatMovieActivity.this, AddMovie.class);
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