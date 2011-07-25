package com.upopple.android.seethatmovie.web;

public class RTMovieResult {
	private String title, year;

	public RTMovieResult(String title, String year) {
		super();
		this.title = title;
		this.year = year;
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
	
	public String getTitleYear(){
		return title + " (" + year +")";
	}
}
