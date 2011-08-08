package com.upopple.android.seethatmovie;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.CategoriesDbAdapter;
import com.upopple.android.seethatmovie.data.MoviesDbAdapter;
import com.upopple.android.seethatmovie.util.Util;

public class AddMovie extends Activity {
	CategoryMultiChoiceListAdapter cmclAdapter;
	
	TextView titleBox, categoryText;
	
	CheckBox seenIt;

	private static final int CATEGORY_SELECT = 0;
	private static final int CATEGORY_ADD = 1;
	private static final int CATEGORY_ERROR_UNDERSCORE = 100;
	private static final int CATEGORY_ERROR_ALREADY_ADDED = 101;
	Dialog inputError;
	
	ArrayList<String> movieList;
	ArrayList<String> movieCategories;
	HashSet<String> categories;
	String[] categoriesArray;
	
	MoviesDbAdapter mdb;
	CategoriesDbAdapter cdb;
	
	Button addbutton, cancelButton;
	
	boolean addMovie;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_movie);
		
		mdb = new MoviesDbAdapter(this);
		mdb.open();

		cdb = mdb.getCdbAdapter();
		cdb.open();
		
		cmclAdapter = new CategoryMultiChoiceListAdapter(this);
		
		addMovie = true;
		
		categories = cdb.getAllCategories();
		categoriesArray = categories.toArray(new String[]{});
		movieCategories = new ArrayList<String>();
		
		
		Intent i = getIntent();
		titleBox = (TextView)findViewById(R.id.movieTitle);
		titleBox.setText(i.getStringExtra("movieTitle"));
		
		categoryText = (TextView)findViewById(R.id.movieCategoryText);
		
		seenIt = (CheckBox) findViewById(R.id.seenIt);
		
		addbutton = (Button)findViewById(R.id.addMovieButton);
		addbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				try{
					Intent i = new Intent(AddMovie.this, MovieTabWidget.class);
					startActivity(i);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		cancelButton = (Button) findViewById(R.id.cancelAddButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				addMovie = false;
				Intent i = new Intent(AddMovie.this, MovieTabWidget.class);
				startActivity(i);
			}
		});
	}
	
	public void saveItToDb(){
		if(addMovie){
			String movieId = getIntent().getStringExtra("movieId");
			String movieTitle = titleBox.getText().toString();
			
			if(seenIt.isChecked())
				movieCategories.add("_seen");
	
			mdb.insertmovie(movieId, movieTitle, movieCategories);
				
			cdb.close();
			mdb.close();
			titleBox.setText("");
			categoryText.setText("");
		}
	}	
	
	@Override
	protected void onPause() {
		try{
			saveItToDb();
		} catch(Exception e){
			e.printStackTrace();
		}
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_movie_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.addMovieNewCategory:
			showDialog(CATEGORY_ADD);
			return true;
		case R.id.addMovieEditCategories:
			showDialog(CATEGORY_SELECT);
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		Dialog inputError;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id){
		case CATEGORY_ADD:
			final EditText newCategory = new EditText(this);
			newCategory.setPadding(10, 10, 10, 10);
			builder.setTitle("Add Categories")
			.setView(newCategory)
			.setCancelable(true)
			.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(newCategory.getText().toString().startsWith("_"))
						showDialog(CATEGORY_ERROR_UNDERSCORE);
					else if(categories.contains(newCategory.getText()))
						showDialog(CATEGORY_ERROR_ALREADY_ADDED);
					else{
						categories.add(newCategory.getText().toString());
						categoriesArray = categories.toArray(new String[]{});
						movieCategories.add(newCategory.getText().toString());
						categoryText.setText(Util.arrayListToString(movieCategories));
						cmclAdapter.notifyDataSetChanged();
						dialog.cancel();
					}
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		inputError = builder.create();
		break;
		case CATEGORY_SELECT: 
			builder.setTitle("Add Categories")
				.setCancelable(true)
				.setAdapter(cmclAdapter, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {}
				})
				.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						categoryText.setText(movieCategories.toString());
						dialog.cancel();
					}
				})
				.setNegativeButton("Clear", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						movieCategories = new ArrayList<String>();
					}
				});
			inputError = builder.create();
			break;
		case CATEGORY_ERROR_ALREADY_ADDED: 
			builder.setMessage("The same category cannot be created twice!")
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			inputError = builder.create();
			break;
		case CATEGORY_ERROR_UNDERSCORE: 
			builder.setMessage("Sorry! The category name cannot start with a _ (underscore).")
				.setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
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
	
	private class CategoryMultiChoiceListAdapter extends BaseAdapter{
		protected LayoutInflater li;
		
		public CategoryMultiChoiceListAdapter(Context context){
			li = LayoutInflater.from(context);
		}

		public int getCount() {return categoriesArray.length;}

		public String getItem(int position) {return categoriesArray[position];}

		public long getItemId(int position) {return position;}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.checkboxrow, null);
				holder = new ViewHolder();
				holder.categoryText = (TextView)v.findViewById(R.id.name);
				holder.addCategory = (CheckBox)v.findViewById(R.id.checkbox);
				
				holder.addCategory.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if(holder.addCategory.isChecked()){
							movieCategories.add(holder.categoryText.getText().toString());
						} else {
							movieCategories.remove(holder.categoryText.getText().toString());
						}
					}
				});
				
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			holder.categoryText.setText(categoriesArray[position]);
			holder.categoryText.setTextColor(Color.BLACK);
			holder.addCategory.setChecked(movieCategories.contains(holder.categoryText.getText().toString()));
			
			v.setTag(holder);
			return v;
		}
		
		public class ViewHolder {
			TextView categoryText;
			CheckBox addCategory;
		}
	}
}
