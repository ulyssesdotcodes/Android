package com.upopple.android.seethatmovie;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.upopple.android.seethatmovie.data.DBMovie;

public class ToSeeView extends CategoryView{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		categoryViewAdapter = new ToSeeViewAdapter(this, this.getIntent().getStringExtra("category"));
		
		
	}
	
	@Override
	protected void onStop() {
		CheckBox temp;
		for(int i = 0; i < categoryViewAdapter.getCount(); i++){
		}
		
		super.onStop();
	}
	
	private class ToSeeViewAdapter extends CategoryView.CategoryViewAdapter{

		public ToSeeViewAdapter(Context context, String category) {
			super(context, "_seen");
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.movierow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mCheck = (CheckBox)v.findViewById(R.id.checkbox);
				
				holder.mCheck.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						if(holder.mCheck.isChecked()){
							cdb.insertMovieCategory(holder.movie.getId(), holder.movie.getTitle(), "_seen");
							v.setEnabled(true);
						} else {
							cdb.removeMovieCategory(holder.movie.getId(), "_seen");
							v.setEnabled(false);
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
}
