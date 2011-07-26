package com.upopple.android.seethatmovie.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.annotations.SerializedName;

public class Movie {
	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("year")
	private String year;

	@SerializedName("runtime")
	private String runtime;

	@SerializedName("ratings")
	private Ratings ratings;

	@SerializedName("synopsis")
	private String synopsis;

	@SerializedName("posters")
	private Posters posters;

	@SerializedName("links")
	private Links links;
	
	private String availableOn;
	
	
	private ArrayList<String> categories, cast;

	private class Ratings{
		private String critics_rating;
		private String critics_score;
		private String audience_rating;
		private String audience_score;
		public String getCritics_rating() {
			return critics_rating;
		}
		public void setCritics_rating(String critics_rating) {
			this.critics_rating = critics_rating;
		}
		public String getCritics_score() {
			return critics_score;
		}
		public void setCritics_score(String critics_score) {
			this.critics_score = critics_score;
		}
		public String getAudience_rating() {
			return audience_rating;
		}
		public void setAudience_rating(String audience_rating) {
			this.audience_rating = audience_rating;
		}
		public String getAudience_score() {
			return audience_score;
		}
		public void setAudience_score(String audience_score) {
			this.audience_score = audience_score;
		}
		
		
	}

	private class Posters{
		private String thumbnail;

		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
	}
	private class Links{
		private String self;

		public String getSelf() {
			return self;
		}

		public void setSelf(String self) {
			this.self = self;
		}
		
		
	}
	
	//Getters and setters

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

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public Ratings getRatings() {
		return ratings;
	}

	public void setRatings(Ratings ratings) {
		this.ratings = ratings;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public Posters getPosters() {
		return posters;
	}

	public void setPosters(Posters posters) {
		this.posters = posters;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

	public String getAvailableOn() {
		return availableOn;
	}

	public void setAvailableOn(String availableOn) {
		this.availableOn = availableOn;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}
	
	
	//My methods
	
	public String getTitleAndYear(){
		return (year.equals("")) ? title : title + "(" + year + ")";
	}

}
