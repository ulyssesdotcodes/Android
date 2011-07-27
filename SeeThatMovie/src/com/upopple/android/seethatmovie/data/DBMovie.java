package com.upopple.android.seethatmovie.data;

import java.text.DateFormat;
import java.util.Date;

public class DBMovie {
	private String title, id, json_data;
	private long dateAdded;
	
	public DBMovie(String id, String title, long dateAdded) {
		super();
		this.title = title;
		this.id = id;
		this.json_data = "";
		this.dateAdded = dateAdded;
	}
	
	public DBMovie(String id, String title, String json_data,
			long dateAdded) {
		super();
		this.title = title;
		this.id = id;
		this.json_data = json_data;
		this.dateAdded = dateAdded;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJson_data() {
		return json_data;
	}
	public void setJson_data(String json_data) {
		this.json_data = json_data;
	}
	public long getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(long dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getFormattedDate(){
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		return dateFormat.format(new Date(dateAdded));
	}
	
	
}
