package com.dabdm.travelplan.places;

public class PlaceDetails {
	
	private String international_phone_number;
	private OpeningHours opening_hours;
	private Photo[] photos;
	private double rating ;
	private String name;
	private String[] types;
	
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	public String getInternational_phone_number() {
		return international_phone_number;
	}
	public void setInternational_phone_number(String international_phone_number) {
		this.international_phone_number = international_phone_number;
	}
	public OpeningHours getOpening_hours() {
		return opening_hours;
	}
	public void setOpening_hours(OpeningHours opening_hours) {
		this.opening_hours = opening_hours;
	}
	public Photo[] getPhotos() {
		return photos;
	}
	public void setPhotos(Photo[] photos) {
		this.photos = photos;
	}
	public class Photo {
		private String photo_reference;
		public String getPhoto_reference() {
			return photo_reference;
		}

		public void setPhoto_reference(String photo_reference) {
			this.photo_reference = photo_reference;
		}
		}
	public class OpeningHours {
		
		private boolean open_now; 
		private Periods[] periods;
		public boolean isOpen_now() {
			return open_now;
		}
		public void setOpen_now(boolean open_now) {
			this.open_now = open_now;
		}
		public Periods[] getPeriods() {
			return periods;
		}
		public void setPeriods(Periods[] periods) {
			this.periods = periods;
		}
	}
	public class Periods {
		
		private Close close;
		private Open open;
		public Close getClose() {
			return close;
		}
		public void setClose(Close close) {
			this.close = close;
		}
		public Open getOpen() {
			return open;
		}
		public void setOpen(Open open) {
			this.open = open;
		}
	}
	public class Open {
		private int day;
		private String time;
		public int getDay() {
			return day;
		}
		public void setDay(int day) {
			this.day = day;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
	}
	public class Close {
		private int day;
		private String time;
		public int getDay() {
			return day;
		}
		public void setDay(int day) {
			this.day = day;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
	}
	
}
