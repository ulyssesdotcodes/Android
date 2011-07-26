package com.upopple.android.seethatmovie.web;

import com.google.gson.annotations.SerializedName;

public class RTMovieResult {
	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("year")
	private String year;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	//My methods and constructors
		
	public RTMovieResult(String title){
		this.title = title;
		this.year = "";
		this.id = "0";
	}
	
	public String getTitleYear(){
		return (year.equals("")) ? title : title + "(" + year +")";
	}
	
}
