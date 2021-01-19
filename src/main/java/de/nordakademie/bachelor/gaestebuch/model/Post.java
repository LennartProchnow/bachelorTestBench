package de.nordakademie.bachelor.gaestebuch.model;

public class Post {
	
	private String name;
	private String ratingText;
	
	public Post (String name, String ratingText) {
		this.name = name;
		this.ratingText = ratingText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRatingText() {
		return ratingText;
	}

	public void setRatingText(String ratingText) {
		this.ratingText = ratingText;
	}	
}
