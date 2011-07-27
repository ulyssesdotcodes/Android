package com.upopple.android.seethatmovie;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
			super(context, category);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View v = convertView;
			if((v==null) || v.getTag() == null){
				v = li.inflate(R.layout.movierow, null);
				holder = new ViewHolder();
				holder.mTitle = (TextView)v.findViewById(R.id.name);
				holder.mCheck = (CheckBox)v.findViewById(R.id.toSeeSawItCheck);
				
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
